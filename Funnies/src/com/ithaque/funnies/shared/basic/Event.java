package com.ithaque.funnies.shared.basic;

public class Event {

	public enum Type {
		ALL,
		MOUSE_DOWN,
		MOUSE_UP,
		MOUSE_CLICK,
		MOUSE_DOUBLE_CLICK,
		MOUSE_MOVE,
		MOUSE_DRAG,
		MOUSE_WHEEL,
		KEY_PRESSED,
		KEY_RELEASED,
		KEY_ENTER,
		ALARM,
		GESTURE
	}
	
	Type type;
	
	public Event(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}

	public String[] getParams() {
		return new String[] {""+type};
	}
	
}
