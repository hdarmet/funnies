package com.ithaque.funnies.shared.basic;

public abstract class AbstractAnimation implements Animation {
	
	AnimationContext context;
	boolean finished = false;
	long updateSerial = Item.getUpdateSerial();
	
	public AbstractAnimation() {
	}
	
	public long getUpdateSerial() {
		return updateSerial;
	}
	
	@Override
	public boolean animate(long time) {
		if (!isFinished()) {
			if (time+Animation.INTERVAL>=getEndTime()) {
				return false;
			}
			else {
				return executeAnimation(time);
			}
		}
		return false;
	}
	
	protected abstract boolean executeAnimation(long time);

	@Override
	public boolean start(long time) {
		return !isFinished();
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
		finished = true;
	}
	
	public boolean isFinished() {
		return finished;
	}

	@Override
	public void reset() {
		finished = false;
		updateSerial = Item.getUpdateSerial();
	}
	
	public abstract long getEndTime();
	
}
