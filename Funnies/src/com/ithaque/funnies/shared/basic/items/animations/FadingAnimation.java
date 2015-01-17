package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.items.animations.easing.SineInOutEasing;

public class FadingAnimation extends SoftenAnimation {

	float baseOpacity;
	Float targetOpacity;
	
	public FadingAnimation(Easing easing, Float targetOpacity) {
		super(easing);
		this.targetOpacity = targetOpacity;
	}
	
	public FadingAnimation(long duration, Float targetOpacity) {
		this(new SineInOutEasing(duration), targetOpacity);
	}
	
	@Override
	public boolean start(long time) {
		boolean result = super.start(time);
		if (result) {
			baseOpacity = getOpacity();
		}
		return result;
	}
	
	@Override
	protected boolean executeAnimation(long time) {
		setOpacity(getEasing().getValue(baseOpacity, targetOpacity));
		return true;
	}

	@Override
	public void finish(long time) {
		setOpacity(targetOpacity);
		super.finish(time);
	}

	@Override
	public void reset() {
		super.reset();
		this.baseOpacity = 0.0f;
	}
	
	float getOpacity() {
		return getItem().getOpacity();
	}
	
	void setOpacity(float opacity) {
		getItem().setOpacity(opacity);
	}

	public static class Builder extends SoftenAnimation.Builder {
		
		float targetOpacity;
		
		public Builder(Easing.Factory easing, float targetOpacity) {
			super(easing);
			this.targetOpacity = targetOpacity;
		}
		
		public Builder(long duration, float targetOpacity) {
			this(new SineInOutEasing.Builder(duration), targetOpacity);
		}

		@Override
		public FadingAnimation create() {
			FadingAnimation animation = new FadingAnimation(easing.create(), targetOpacity);
			prepare(animation);
			return animation;
		}

	}

}
