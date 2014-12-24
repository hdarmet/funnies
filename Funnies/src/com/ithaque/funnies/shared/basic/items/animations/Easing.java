package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.Board;

public interface Easing {

	void launch(Board board);

	long getEndTime();
	
	long getDuration();

	float getValue(float base, float target);

	public interface Factory {
		Easing create();
	}
}
