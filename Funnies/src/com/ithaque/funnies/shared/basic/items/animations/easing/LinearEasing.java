package com.ithaque.funnies.shared.basic.items.animations.easing;

import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.items.animations.Easing;
import com.ithaque.funnies.shared.basic.items.animations.Easing.Factory;

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
	public long getDuration() {
		return duration;
	}
	
	public static class Builder implements Factory {
		long duration;

		public Builder(long duration) {
			this.duration = duration;
		}
		
		@Override
		public Easing create() {
			return new LinearEasing(duration);
		}
	}
}
