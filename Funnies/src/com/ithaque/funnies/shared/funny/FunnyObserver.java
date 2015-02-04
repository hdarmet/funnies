package com.ithaque.funnies.shared.funny;


public interface FunnyObserver {
	public enum ChangeType {
		ENTER_RING,
		EXIT_RING,
		REGISTERING,
		LOCATION,
		ENABLING
	}
	
	void change(ChangeType type, Funny funny);
}
