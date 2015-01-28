package com.ithaque.funnies.shared.basic;

import com.ithaque.funnies.shared.Geometric;
import com.ithaque.funnies.shared.Transform;

public class TransformUtil {

	static public Transform transform(Moveable item) {
		Transform transform = new Transform();
		transform.translate(
			Graphics.Singleton.getGraphics().getDisplayWidth()/2.0f, 
			Graphics.Singleton.getGraphics().getDisplayHeight()/2.0f);
		if (!transform(item, transform)) {
			return null;
		}
		return transform;
	}
	
	static boolean transform(Moveable moveable, Transform transform) {
		if (moveable==null) {
			return false;
		}
		else {
			if (!(moveable instanceof Board)) {
				if (!transform(((Item)moveable).getParent(), transform)) {
					return false;
				}
			}
			transformToParent(moveable, transform);
			return true;
		}
	}

	static void transformToParent(Moveable moveable, Transform transform) {
		Location translation = moveable.getLocation();
		if (translation!=null && !translation.equals(Location.ORIGIN)) {
			transform.translate(translation.getX(), translation.getY());
		}
		if (moveable.getScale()!=ItemHolder.STANDARD_SCALE) {
			transform.scale(moveable.getScale(), moveable.getScale());
		}
		if (moveable.getRotation()!=ItemHolder.NO_ROTATION) {
			transform.rotate(moveable.getRotation());
		}
	}
	
	static Transform transformToParent(Moveable item) {
		Transform transform = new Transform();
		transformToParent(item, transform);
		return transform;
	}
	
	static public boolean isTarget(Item item, Location point, Location[] shape) {
		Transform transform = transform(item).invert();
		Location trPoint = transform.transformPoint(point);
		Location[] area = Geometric.getArea(shape);
		if (trPoint.getX()>=area[0].getX() && trPoint.getX()<=area[1].getX()
		  && trPoint.getY()>=area[0].getY() && trPoint.getY()<=area[1].getY()) {
			return Geometric.inside(trPoint, shape);			
		}
		else {
			return false;
		}
	}

	static public Location[] transformShape(Moveable item, Location[] shape) {
		Transform transform = transform(item);
		Location[] trShape = transformShape(shape, transform);
		return trShape; 
	}

	static public Location[] invertTransformShape(Moveable item, Location[] shape) {
		Transform transform = transform(item).invert();
		Location[] trShape = transformShape(shape, transform);
		return trShape; 
	}

	static Location[] transformShape(Location[] shape,
			Transform transform) {
		Location[] trShape = new Location[shape.length];
		for (int i=0; i<shape.length; i++) {
			trShape[i] = transform.transformPoint(shape[i]);
		}
		return trShape;
	}

	static public Location transformLocation(Moveable sourceItem, Moveable destItem, Location location) {
		Location result = transformLocation(sourceItem, location);
		return invertTransformLocation(destItem, result);
	}

	static public Location[] transformShape(Moveable sourceItem, Moveable destItem, Location[] shape) {
		Location[] result = transformShape(sourceItem, shape);
		return invertTransformShape(destItem, result);
	}

	static public Location invertTransformLocation(Moveable item, Location location) {
		Transform transform = transform(item).invert();
		return transform==null ? null : transform.transformPoint(location);
	}

	static public Location transformLocation(Moveable item, Location location) {
		Transform transform = transform(item);
		return transform==null ? null : transform.transformPoint(location);
	}

	static public Location invertTransformLocationToParent(Moveable item, Location location) {
		Transform transform = transformToParent(item).invert();
		return transform==null ? null : transform.transformPoint(location);
	}

	static public Location transformLocationToParent(Moveable item, Location location) {
		Transform transform = transformToParent(item);
		return transform==null ? null : transform.transformPoint(location);
	}

	public static Float transformRotation(ItemHolder source, ItemHolder destination, Float rotation) {
		System.out.println(source.getDisplayRotation()+" "+destination.getDisplayRotation()+" "+rotation);
		return source.getDisplayRotation()-destination.getDisplayRotation()+rotation;
	}

	public static Float transformScale(ItemHolder source, ItemHolder destination, Float scale) {
		return source.getDisplayScale()/destination.getDisplayScale()*scale;
	}

	public static Float transformRotation(ItemHolder source, Float rotation) {
		return source.getDisplayRotation()+rotation;
	}

	public static Float transformScale(ItemHolder source, Float scale) {
		return source.getDisplayScale()*scale;
	}
	
	public static Float transformAbsoluteRotation(ItemHolder destination, Float rotation) {
		return rotation-destination.getDisplayRotation();
	}

	public static Float transformAbsoluteScale(ItemHolder destination, Float scale) {
		return scale/destination.getDisplayScale();
	}
}
