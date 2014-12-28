package com.ithaque.funnies.shared.basic;

public interface ItemObserver {
	public enum ChangeType {
		LOCATION,
		SCALE,
		ROTATION,
		SHAPE,
		PARENT,
		ANCESTOR
	};
	
	void change(ChangeType type, Item item);
}
