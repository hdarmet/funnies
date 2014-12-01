package com.ithaque.funnies.shared.basic;


public class MouseEvent extends Event {
	
	public enum Button {
		NONE,
		LEFT,
		MIDDLE,
		RIGHT,
		WHEEL_NORTH,
		WHEEL_SOUTH
	}
	
	Type type;
	int x, y;
	Button button;
	boolean shift;
	boolean ctrl;
	boolean alt;

	public MouseEvent(Type type, int x, int y, Button button, boolean shift, boolean ctrl, boolean alt) {
		super(type);
		this.x = x;
		this.y = y;
		this.button = button;
		this.shift = shift;
		this.ctrl = ctrl;
		this.alt = alt;
	}
	

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Button getButton() {
		return button;
	}


	public boolean isShift() {
		return shift;
	}

	public boolean isCtrl() {
		return ctrl;
	}

	public boolean isAlt() {
		return alt;
	}

}
