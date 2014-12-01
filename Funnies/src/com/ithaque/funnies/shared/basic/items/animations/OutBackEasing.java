package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.Board;

public class OutBackEasing implements Easing {

	long duration;
	long endTime;
	Board board;
	
	public OutBackEasing(long duration) {
		super();
		this.duration = duration;
	}

	@Override
	public void launch(Board board) {
		this.board = board;
		this.endTime = duration+board.getTime();
	}

	@Override
	public long getEndTime() {
		return endTime;
	}

	@Override
	public float getValue(float base, float target) {
		float time = board.getTime() - (endTime-duration);
		float increment = target-base;
		float factor = 1.70158f;
		time = time/duration -1.0f;
		return increment*(time*time*((factor+1)*time + factor) + 1.0f) + base;
	}

	@Override
	public Easing duplicate() {
		return new OutBackEasing(duration);
	}
	
}