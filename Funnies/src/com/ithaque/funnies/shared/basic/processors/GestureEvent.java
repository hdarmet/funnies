package com.ithaque.funnies.shared.basic.processors;

import com.ithaque.funnies.shared.basic.Event;
import com.ithaque.funnies.shared.basic.processors.GestureRecognition.Gesture;


public class GestureEvent extends Event {

	Gesture gesture;
	
	public GestureEvent(Type type, Gesture gesture) {
		super(type);
		this.gesture = gesture;
	}
	
	public Gesture getGesture() {
		return gesture;
	}
	
	
	@Override
	public String[] getParams() {
		return new String[] {""+getType()};
	}
}
