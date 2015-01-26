package com.ithaque.funnies.shared.basic;


public class KeyboardEvent extends Event {
	
	char key;
	boolean shift;
	boolean ctrl;
	boolean alt;
	
	public KeyboardEvent(Type type, char key, boolean shift, boolean ctrl, boolean alt) {
		super(type);
		this.key = key;
		this.shift = shift;
		this.ctrl = ctrl;
		this.alt = alt;
	}
	
	public int getKey() {
		return key;
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
	
	@Override
	public String[] getParams() {
		return new String[] {""+getType(), ""+getKey(), ""+isShift(), ""+isCtrl(), ""+isAlt()};
	}
}
