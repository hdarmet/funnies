package com.ithaque.funnies.shared.basic;

import java.util.HashSet;
import java.util.Set;

import com.ithaque.funnies.shared.IllegalInvokeException;

public class Item implements Moveable {

	public static final float TWO_PI = (float)(Math.PI*2);
	
	ItemHolder parent = null;
	Location location = Location.ORIGIN;
	float scale = ItemHolder.STANDARD_SCALE;
	float rotation = ItemHolder.NO_ROTATION;
	Set<Event.Type> eventTypes = new HashSet<Event.Type>();
	Location[] shape = null;
	
	public void removeFromParent() {
		if (getParent()==null) {
			throw new IllegalInvokeException();
		}
		Board oldBoard = getBoard();
		this.parent = null;
		unregisterOnBoard(oldBoard);
		dirty();		
	}
	
	public void setParent(ItemHolder parent) {
		if (getParent()!=null) {
			throw new IllegalInvokeException();
		}
		this.parent = parent;
		Board newBoard = getBoard();
		if (newBoard!=null) {
			registerOnBoard(newBoard);
		}
		dirty();
	}
	
	public void changeParent(ItemHolder newParent) {
		ItemHolder currentParent = getParent();
		if (newParent!=currentParent) {
			currentParent.removeItem(this);
			newParent.addItem(this);
			Location absoluteLocation = getBoard().getGraphics().transformLocation(currentParent, getLocation());
			Location relativeLocation = getBoard().getGraphics().invertTransformLocation(getParent(), absoluteLocation);
			setLocation(relativeLocation);
		}
	}
	
	protected void registerOnBoard(Board newBoard) {
		newBoard.register(this);
	}
	
	protected void unregisterOnBoard(Board oldBoard) {
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
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(float x, float y) {
		setLocation(new Location(x, y));
		dirty();
	}
	
	public void setLocation(Location location) {
		this.location = location;
		dirty();
	}
	
	@Override
	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
		dirty();
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		while (rotation<0) {
			rotation+=TWO_PI;
		}
		while (rotation>TWO_PI) {
			rotation-=TWO_PI;
		}
		this.rotation = rotation;
		dirty();
	}
	
	public void free() {
		if (parent!=null) {
			dirty();
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
			return getBoard().getGraphics().isTarget(this, new Location(event.getX(), event.getY()), shape);
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
	}
	
	public void setShape(float ... coords) {
		Location[] shape = new Location[coords.length/2];
		for (int i=0; i<shape.length; i++) {
			shape[i] = new Location(coords[i*2], coords[i*2+1]);
		}
		setShape(shape);
	}

	public void render(Graphics graphics, int currentLevel, int level) {
		if (currentLevel == level) {
			render(graphics);
		}
	}

}
