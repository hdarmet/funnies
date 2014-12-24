package com.ithaque.funnies.shared.basic;

public abstract class Animation {

	public static final long INTERVAL = 40;
	
	AnimationContext context;
	
	public Animation() {
	}
	
	public boolean animate(long time) {
		if (time+Animation.INTERVAL>=getEndTime()) {
			return false;
		}
		else {
			return executeAnimation(time);
		}
	}

	protected abstract boolean executeAnimation(long time);

	public boolean start(long time, AnimationContext context) {
		this.context = context;
		return true;
	}
	
	public AnimationContext getContext() {
		return context;
	}

	public void finish(long time) {
	}

	public abstract long getDuration();
	
	public abstract long getEndTime();
	
	public interface Factory {
		Animation create();
	}
}
