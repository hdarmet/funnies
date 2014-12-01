package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.Board;

public class LinearEasing implements Easing {

	long duration;
	long endTime;
	Board board;
	
	public LinearEasing(long duration) {
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
		float increment = (target-base)/duration;
		return base + time*increment;
	}

	@Override
	public Easing duplicate() {
		return new LinearEasing(duration);
	}

}
