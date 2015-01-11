package com.ithaque.funnies.shared.basic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ithaque.funnies.shared.Geometric;
import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.ItemObserver.ChangeType;

public class Item implements Moveable {

	ItemHolder parent = null;
	Location location = Location.ORIGIN;
	float scale = ItemHolder.STANDARD_SCALE;
	float rotation = ItemHolder.NO_ROTATION;
	float opacity = 1.0f;
	Set<Event.Type> eventTypes = new HashSet<Event.Type>();
	Location[] shape = null;
	int level;
	
	List<ItemObserver> observers = null;
	
	public void removeFromParent() {
		if (getParent()==null) {
			throw new IllegalInvokeException();
		}
		Board oldBoard = getBoard();
		this.parent = null;
		unregisterOnBoard(oldBoard);
		fire(ChangeType.PARENT);
		dirty();		
	}
	
	public void setParent(ItemHolder parent) {
		if (parent==null || getParent()!=null) {
			throw new IllegalInvokeException();
		}
		this.parent = parent;
		Board newBoard = getBoard();
		if (newBoard!=null) {
			registerOnBoard(newBoard);
		}
		fire(ChangeType.PARENT);
		dirty();
	}
	
	public void changeParent(ItemHolder newParent) {
		ItemHolder currentParent = getParent();
		Location location = getLocation();
		if (newParent!=currentParent) {
			currentParent.removeItem(this);
			newParent.addItem(this);
			location = TransformUtil.transformLocation(currentParent, getParent(), location);
			setLocation(location);
		}
	}
	
	public void registerOnBoard(Board newBoard) {
		newBoard.register(this);
	}
	
	public void unregisterOnBoard(Board oldBoard) {
		oldBoard.register(this);
	}
	
	public Set<Event.Type> getEventTypes() {
		return eventTypes;
	}
	
	public void addEventType(Event.Type eventType) {
		if (!eventTypes.contains(eventType)) {
			eventTypes.add(eventType);
			Board board = getBoard();
			if (board!=null) {
				board.registerEvent(this, eventType);
			}
		}
	}

	public void removeEventType(Event.Type eventType) {
		if (eventTypes.contains(eventType)) {
			eventTypes.remove(eventType);
			Board board = getBoard();
			if (board!=null) {
				board.unregisterEvent(this, eventType);
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
		this.opacity = opacity;
		dirty();
	}

	@Override
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(float x, float y) {
		setLocation(new Location(x, y));
		dirty();
	}
	
	public void setLocation(Location location) {
		this.location = location;
		fire(ChangeType.LOCATION);
		dirty();
	}
	
	@Override
	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
		fire(ChangeType.SCALE);
		dirty();
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = Geometric.adjustAngle(rotation);
		fire(ChangeType.ROTATION);
		dirty();
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
		return parent==null ? null : parent.getBoard();
	}

	public void dirty() {
		if (parent!=null) {
			parent.dirty();
		}
	}
	
	public void unsetDirty() {	
	}
	
	public boolean acceptEvent(MouseEvent event) {		
		Location[] shape = getShape();
		if (shape==null) {
			return false;
		}
		else {
			return TransformUtil.isTarget(this, new Location(event.getX(), event.getY()), shape);
		}
	}
	
	public Location[] getShape() {
		return shape;
	}
	
	public Location[] getArea() {
		return getShape();
	}
	
	public void setShape(Location ... shape) {
		this.shape = shape;
		fire(ChangeType.SHAPE);
		dirty();
	}
	
	public void setShape(float ... coords) {
		setShape(buildShape(coords));
	}

	public static Location[] buildShape(float... coords) {
		Location[] shape = new Location[coords.length/2];
		for (int i=0; i<shape.length; i++) {
			shape[i] = new Location(coords[i*2], coords[i*2+1]);
		}
		return shape;
	}

	public void render(Graphics graphics, int currentLevel, int level) {
		if (currentLevel == level) {
			this.level = currentLevel;
			render(graphics);
		}
	}

	public Location getAbsoluteLocation() {
		return TransformUtil.transformLocation(getParent(), location);
	}
	
	public float getAbsoluteRotation() {
		ItemHolder parent = this.getParent();
		float angle = getRotation();
		while (parent!=null && (parent instanceof Item)) {
			angle = Geometric.adjustAngle(angle+parent.getRotation());
			parent = ((Item)parent).getParent();
		}
		return angle;
	}
	
	public float getAbsoluteScale() {
		ItemHolder parent = this.getParent();
		float scale = getScale();
		while (parent!=null && (parent instanceof Item)) {
			scale = scale*parent.getScale();
			parent = ((Item)parent).getParent();
		}
		return scale;
	}
}
