package com.ithaque.funnies.shared.basic;

import java.util.HashSet;
import java.util.Set;

import com.ithaque.funnies.shared.Trace;

public class Item implements Moveable {

	public static final float TWO_PI = (float)(Math.PI*2);
	
	ItemHolder parent = null;
	Location location = Location.ORIGIN;
	float scale = ItemHolder.STANDARD_SCALE;
	float rotation = ItemHolder.NO_ROTATION;
	Set<Event.Type> eventTypes = new HashSet<Event.Type>();
	Location[] shape = null;
	
	public void setParent(ItemHolder parent) {
		Board oldBoard = getBoard();
		this.parent = parent;
		Board newBoard = getBoard();
		registerOnBoard(oldBoard, newBoard);
		dirty();
	}

	protected void registerOnBoard(Board oldBoard, Board newBoard) {
		if (Trace.debug) {
			Trace.debug("Register item on board : "+this+" "+oldBoard+" => "+newBoard+".\n");
		}
		if (oldBoard == newBoard) {
			return;
		}
		if (oldBoard != null) {
			oldBoard.unregister(this);
		}
		if (newBoard != null) {
			newBoard.register(this);
		}
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
	
	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
		dirty();
	}

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

	public void prepare() {	
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

}
