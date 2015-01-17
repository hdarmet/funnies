package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.AnimationContext;

public class RepeatableAnimation implements Animation {

	Animation animation;
	boolean stopped = false;
	
	public RepeatableAnimation(Animation animation) {
		this.animation = animation;
	}
	
	@Override
	public boolean animate(long time) {
		boolean result = animation.animate(time);
		if (!result) {
			animation.finish(time);
			if (!stopped) {
				animation.reset();
				return animation.start(time);
			}
		}
		return true;
	}

	@Override
	public void finish(long time) {
		animation.finish(time);
	}

	@Override
	public boolean start(long time) {
		return animation.start(time);
	}

	@Override
	public void reset() {
		stopped = false;
		animation.reset();
	}

	@Override
	public AnimationContext getContext() {
		return animation.getContext();
	}

	@Override
	public void setContext(AnimationContext context) {
		animation.setContext(context);
	}

	@Override
	public long getDuration() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isFinished() {
		return stopped || animation.isFinished();
	}

	public void stop() {
		stopped = true;
	}
	
	public static class Builder implements Factory {

		Animation.Factory animationFactory;
		
		public Builder(Animation.Factory animationFactory) {
			super();
			this.animationFactory = animationFactory;
		}
		
		@Override
		public RepeatableAnimation create() {
			return new RepeatableAnimation(animationFactory.create());
		}	

	}

}
