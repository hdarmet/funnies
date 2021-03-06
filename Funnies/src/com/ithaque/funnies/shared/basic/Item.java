package com.ithaque.funnies.shared.basic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ithaque.funnies.shared.Geometric;
import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.Location;
import com.ithaque.funnies.shared.Shape;
import com.ithaque.funnies.shared.basic.ItemObserver.ChangeType;

public class Item implements Moveable {

	static long updateSerial = 0;
	
	long parentSerial = 0;
	long locationSerial = 0;
	long scaleSerial = 0;
	long rotationSerial = 0;
	long opacitySerial = 0;
	long shapeSerial = 0;
	
	ItemHolder parent = null;
	Location location = Location.ORIGIN;
	float scale = ItemHolder.STANDARD_SCALE;
	float rotation = ItemHolder.NO_ROTATION;
	float opacity = 1.0f;
	Set<Event.Type> eventTypes = new HashSet<Event.Type>();
	Shape shape = null;
	int level;
	
	List<ItemObserver> observers = null;
	
	public void removeFromParent() {
		if (getParent()==null) {
			throw new IllegalInvokeException();
		}
		LayoutDevice oldLayout = getLayout();
		this.parent = null;
		unregisterOnLayout(oldLayout);
		fire(ChangeType.PARENT);
		dirty();		
	}
	
	public void doSetParent(ItemHolder parent) {
		if (parent==null || getParent()!=null) {
			throw new IllegalInvokeException();
		}
		this.parent = parent;
		LayoutDevice newLayout = getLayout();
		if (newLayout!=null) {
			registerOnLayout(newLayout);
		}
		fire(ChangeType.PARENT);
		dirty();
	}
	
	public void setParent(ItemHolder newParent) {
		setParent(newParent, getUpdateSerial());
	}
	
	public void setParent(ItemHolder newParent, long serial) {
		if (parentSerial<=serial) {
			parentSerial = serial;
			ItemHolder currentParent = getParent();
			Location location = getLocation();
			if (newParent!=currentParent) {
				currentParent.removeItem(this);
				newParent.addItem(this);
				location = TransformUtil.transformLocation(currentParent, getParent(), location);
				setLocation(location, serial);
			}
		}
	}
	
	public void registerOnLayout(LayoutDevice newLayout) {
		newLayout.register(this);
	}
	
	public void unregisterOnLayout(LayoutDevice oldLayout) {
		oldLayout.unregister(this);
	}
	
	public Set<Event.Type> getEventTypes() {
		return eventTypes;
	}
	
	public void addEventType(Event.Type eventType) {
		if (!eventTypes.contains(eventType)) {
			eventTypes.add(eventType);
			LayoutDevice layout = getLayout();
			if (layout!=null) {
				layout.registerEvent(this, eventType);
			}
		}
	}

	public void removeEventType(Event.Type eventType) {
		if (eventTypes.contains(eventType)) {
			eventTypes.remove(eventType);
			LayoutDevice layout = getLayout();
			if (layout!=null) {
				layout.unregisterEvent(this, eventType);
			}
		}
	}

	@Override
	public float getOpacity() {
		return opacity;
	}
	
	@Override
	public float getDisplayOpacity() {
		if (getParent()!=null) {
			return opacity*getParent().getDisplayOpacity();
		}
		else {
			return 0.0f;
		}
	}
	
	public void setOpacity(float opacity) {
		setOpacity(opacity, getUpdateSerial());
	}

	public void setOpacity(float opacity, long serial) {
		if (opacitySerial<=serial) {
			opacitySerial = serial;
			if (opacity!=this.opacity) {
				this.opacity = opacity;
				fire(ChangeType.OPACITY);
				dirty();
			}
		}
	}

	@Override
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(float x, float y) {
		setLocation(new Location(x, y), getUpdateSerial());
	}
	
	public void setLocation(Location location) {
		setLocation(location, getUpdateSerial());
	}
	
	public void setLocation(float x, float y, long serial) {
		setLocation(new Location(x, y), serial);
	}
	
	public void setLocation(Location location, long serial) {
		if (locationSerial<=serial) {
			locationSerial = serial;
			if (!location.equals(this.location)) {
				this.location = location;
				fire(ChangeType.LOCATION);
				dirty();
			}
		}
	}
	
	@Override
	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		setScale(scale, getUpdateSerial());
	}

	public void setScale(float scale, long serial) {
		if (scaleSerial<=serial) {
			scaleSerial = serial;
			if (scale!=this.scale) {
				this.scale = scale;
				fire(ChangeType.SCALE);
				dirty();
			}
		}
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.setRotation(rotation, getUpdateSerial());
	}
	
	public void setRotation(float rotation, long serial) {
		if (rotationSerial<=serial) {
			rotationSerial = serial;
			if (rotation!=this.rotation) {
				this.rotation = Geometric.adjustAngle(rotation);
				fire(ChangeType.ROTATION);
				dirty();
			}
		}
	}
	
	public void fire(ChangeType changeType) {
		if (observers!=null) {
			for (ItemObserver observer : new ArrayList<ItemObserver>(observers)) {
				observer.change(changeType, this);
			}
		}
	}

	public void addObserver(ItemObserver observer) {
		if (observers==null) {
			observers = new ArrayList<ItemObserver>();
		}
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}
	
	public void removeObserver(ItemObserver observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
			if (observers.isEmpty()) {
				observers=null;
			}
		}
	}
	
	public void free() {
		if (parent!=null) {
			parent.removeItem(this);
		}
	}

	public ItemHolder getParent() {
		return parent;
	}

	public int prepare() {
		return 1;
	}
	
	public void render(Graphics graphics) {
	}
	
	public Board getBoard() {
		return getParent()==null ? null : getParent().getBoard();
	}

	public void dirty() {
		if (getParent()!=null) {
			getParent().dirty();
		}
	}
	
	public void unsetDirty() {	
	}
	
	public boolean acceptEvent(MouseEvent event) {		
		Shape shape = getShape();
		if (shape==null) {
			return false;
		}
		else {
			return TransformUtil.isTarget(this, new Location(event.getX(), event.getY()), shape);
		}
	}
	
	public Shape getShape() {
		return shape;
	}
	
	public Shape getArea() {
		return getShape();
	}
	
	public void setShape(Shape shape) {
		setShape(Item.getUpdateSerial(), shape);
	}

	public void setShape(long serial, Shape shape) {
		if (shapeSerial<=serial) {
			shapeSerial = serial;
			if (!Geometric.compareShape(shape, this.shape)) {
				this.shape = shape;
				fire(ChangeType.SHAPE);
				dirty();
			}
		}
	}
	
	public void setShape(Location ... locations) {
		setShape(new Shape(locations));
	}
	
	public void setShape(float ... coords) {
		setShape(Item.getUpdateSerial(), coords);
	}

	public void setShape(long serial, float ... coords) {
		setShape(serial, buildShape(coords));
	}

	public static Shape buildShape(float... coords) {
		Location[] content = new Location[coords.length/2];
		for (int i=0; i<content.length; i++) {
			content[i] = new Location(coords[i*2], coords[i*2+1]);
		}
		return new Shape(content);
	}

	public void render(Graphics graphics, int currentLevel, int level) {
		if (currentLevel == level) {
			this.level = currentLevel;
			render(graphics);
		}
	}

	public Location getDisplayLocation() {
		return TransformUtil.transformLocation(getParent(), getLocation());
	}
	
	public Shape getDisplayShape() {
		return TransformUtil.transformShape(getParent(), getShape());
	}
	
	public float getDisplayRotation() {
		ItemHolder parent = this.getParent();
		float angle = getRotation();
		while (parent!=null && (parent instanceof Item)) {
			angle = Geometric.adjustAngle(angle+parent.getRotation());
			parent = ((Item)parent).getParent();
		}
		return angle;
	}
	
	public float getDisplayScale() {
		ItemHolder parent = this.getParent();
		float scale = getScale();
		while (parent!=null && (parent instanceof Item)) {
			scale = scale*parent.getScale();
			parent = ((Item)parent).getParent();
		}
		return scale;
	}

	public LayoutDevice getLayout() {
		if (getParent()==null) {
			return null;
		}
		if (getParent() instanceof LayoutDevice) {
			return (LayoutDevice)getParent();
		}
		return getParent().getLayout();
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
	
	public static long getUpdateSerial() {
		return updateSerial++;
	}
}
