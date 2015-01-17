package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.AnimationContext;

public class CancelableAnimation implements Animation {

	Animation animation;
	boolean canceled = false;
	
	public CancelableAnimation(Animation animation) {
		this.animation = animation;
	}
	
	@Override
	public boolean animate(long time) {
		if (!canceled) {
			return animation.animate(time);
		}
		return false;
	}

	@Override
	public void finish(long time) {
		if (!canceled) {
			animation.finish(time);
		}
	}

	@Override
	public boolean start(long time) {
		if (!canceled) {
			return animation.start(time);
		}
		return false;
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
		return animation.getDuration();
	}

	@Override
	public boolean isFinished() {
		return canceled || animation.isFinished();
	}

	@Override
	public void reset() {
		canceled = true;
	}
	
	public void cancel() {
		canceled = true;
	}
	
	public static class Builder implements Factory {

		Animation.Factory animationFactory;
		
		public Builder(Animation.Factory animationFactory) {
			super();
			this.animationFactory = animationFactory;
		}
		
		@Override
		public CancelableAnimation create() {
			return new CancelableAnimation(animationFactory.create());
		}	

	}

}
