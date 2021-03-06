package com.ithaque.funnies.shared.basic.items.animations.easing;

import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.items.animations.Easing;

public class SineInOutEasing implements Easing {

	long duration;
	long endTime;
	Board board;
	
	public SineInOutEasing(long duration) {
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
		return -increment/2.0f * ((float)(Math.cos(Math.PI*(time/duration)) - 1.0f)) + base;
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
			return new SineInOutEasing(duration);
		}
	}
}
