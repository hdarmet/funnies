package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.AnimationContext.Key;
import com.ithaque.funnies.shared.basic.items.animations.easing.SineInOutEasing;


public class ScalingAnimation extends SoftenAnimation {

	Float newScale = null;
	float baseScale;
	Key scaleKey ;
	
	public ScalingAnimation(Easing easing) {
		super(easing);
	}
	
	public ScalingAnimation(Easing easing, Float newScale) {
		super(easing);
		this.newScale = newScale;
	}
	
	public ScalingAnimation(long duration) {
		this(new SineInOutEasing(duration));
	}
	
	public ScalingAnimation(long duration, Float newScale) {
		this(new SineInOutEasing(duration), newScale);
	}
	
	@Override
	public boolean start(long time) {
		boolean result = super.start(time);
		if (result) {
			this.baseScale = getItem().getScale();
		}
		return result;
	}
	
	public void setScalingKey(Key scaleKey) {
		this.scaleKey = scaleKey;
	}
	
	public Float getScale() {
		return newScale==null ? getContext().getFactor(scaleKey) : newScale;
	}
	
	@Override
	public boolean executeAnimation(long time) {
		getItem().setScale(easing.getValue(baseScale, getScale()));
		return true;
	}

	@Override
	public void finish(long time) {
		getItem().setScale(newScale);
		super.finish(time);
	}

	public static class Builder extends SoftenAnimation.Builder {
		Float newScale;
		Key scaleKey;
		
		public Builder(Easing.Factory easing, Float newScale) {
			super(easing);
			this.newScale = newScale;
		}

		public Builder(long duration, Float newScale) {
			this(new SineInOutEasing.Builder(duration), newScale);
		}
		
		public Builder setScaleKey(Key scaleKey) {
			this.scaleKey = scaleKey;
			return this;
		}
		
		@Override
		public Builder setItemKey(Key itemKey) {
			super.setItemKey(itemKey);
			return this;
		}

		@Override
		public ScalingAnimation create() {
			ScalingAnimation animation = new ScalingAnimation(easing.create(), newScale);
			prepare(animation);
			return animation;
		}

		protected void prepare(SoftenAnimation animation) {
			super.prepare(animation);
			if (scaleKey!=null) {
				((ScalingAnimation)animation).setScalingKey(scaleKey);
			}
		}
		
	}
	
}

