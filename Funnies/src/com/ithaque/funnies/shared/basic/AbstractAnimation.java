package com.ithaque.funnies.shared.basic;

public abstract class AbstractAnimation implements Animation {
	
	AnimationContext context;
	
	public AbstractAnimation() {
	}
	
	@Override
	public boolean animate(long time) {
		if (time+Animation.INTERVAL>=getEndTime()) {
			return false;
		}
		else {
			return executeAnimation(time);
		}
	}

	protected abstract boolean executeAnimation(long time);

	@Override
	public boolean start(long time) {
		return true;
	}
	
	@Override
	public AnimationContext getContext() {
		return context;
	}

	@Override
	public void setContext(AnimationContext context) {
		this.context = context;
	}
	
	@Override
	public void finish(long time) {
	}
	
	public abstract long getEndTime();
	
}
