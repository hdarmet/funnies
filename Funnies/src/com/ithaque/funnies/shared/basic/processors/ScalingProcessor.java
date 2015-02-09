package com.ithaque.funnies.shared.basic.processors;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.Location;
import com.ithaque.funnies.shared.Shape;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.MouseEvent.Button;
import com.ithaque.funnies.shared.basic.Processor;
import com.ithaque.funnies.shared.basic.TransformUtil;

public class ScalingProcessor implements Processor {

	float increment;
	float minScale;
	float maxScale;
	List<Item> scalables = new ArrayList<Item>();

	public ScalingProcessor(float increment, float minScale, float maxScale) {
		this.increment = increment;
		this.minScale = minScale;
		this.maxScale = maxScale;
	}
	
	@Override
	public boolean process(Event event, Board board) {
		if (event.getType()==Type.MOUSE_WHEEL) {
			MouseEvent mouseEvent = (MouseEvent)event;
			Item scaled = board.getMouseTarget(mouseEvent);
			if (scaled != null && acceptScalable(scaled)) {
				if (mouseEvent.getButton()==Button.WHEEL_NORTH) {
					zoomIn(scaled, mouseEvent.getX(), mouseEvent.getY());
					return true;
				}
				else {
					zoomOut(scaled, mouseEvent.getX(), mouseEvent.getY());
					return true;
				}
			}
		}
		return false;
	}

	boolean acceptScalable(Item scaled) {
		return scalables.contains(scaled);
	}

	public void addScalable(Item item) {
		scalables.add(item);
	}
	
	public void removeScalable(Item item) {
		scalables.remove(item);
	}

	protected void zoom(Item scaled, int x, int y, float increment) {
		Location oldLocation = TransformUtil.invertTransformLocation(scaled, new Location(x, y));
		scaled.setScale(limits(scaled, scaled.getScale()*increment));
		Location delta = TransformUtil.transformLocation(scaled, oldLocation);

		Location absLocation = TransformUtil.transformLocation(scaled.getParent(), scaled.getLocation());
		Location newLocation = new Location(absLocation.getX()-(delta.getX()-x), absLocation.getY()-(delta.getY()-y));
		scaled.setLocation(TransformUtil.invertTransformLocation(scaled.getParent(), newLocation));
	}

	protected void zoomIn(Item scaled, int x, int y) {
		zoom(scaled, x, y, increment);
	}

	protected void zoomOut(Item scaled, int x, int y) {
		zoom(scaled, x, y, 1.0f/increment);
	}

	protected float getMinScale(Item scaled) {
		Board board = scaled.getBoard();
		Shape absShape = TransformUtil.transformShape(scaled, scaled.getShape());
		float minX = absShape.getX(0);
		float maxX = absShape.getX(0);
		float minY = absShape.getY(0);
		float maxY = absShape.getY(0);
		for (int i=1; i<absShape.size(); i++) {
			if (minX>absShape.getX(i)) {
				minX=absShape.getX(i);
			}
			if (maxX<absShape.getX(i)) {
				maxX=absShape.getX(i);
			}
			if (minY>absShape.getY(i)) {
				minY=absShape.getY(i);
			}
			if (maxY<absShape.getY(i)) {
				maxY=absShape.getY(i);
			}
		}
		float scaleX = board.getGraphics().getDisplayWidth()/(maxX - minX);
		float scaleY = board.getGraphics().getDisplayHeight()/(maxY - minY);
		float scale = scaleX>scaleY ? scaleX : scaleY;
		return scale>minScale ? scale : minScale;
	}
	
	protected float limits(Item scaled, float scale) {
		float minScale = getMinScale(scaled);
		return scale<minScale ? minScale : scale>maxScale ? maxScale : scale;
	}
}
