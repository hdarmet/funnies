package com.ithaque.funnies.shared.funny;


public interface FunnyObserver {
	public enum ChangeType {
		ENTER_RING,
		EXIT_RING,
		LOCATION
	}
	
	void change(ChangeType type, Funny funny);
}
