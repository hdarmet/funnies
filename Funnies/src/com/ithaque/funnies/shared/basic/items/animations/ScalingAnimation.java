package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.AnimationContext.FactorFinder;
import com.ithaque.funnies.shared.basic.AnimationContext.MoveableFinder;
import com.ithaque.funnies.shared.basic.items.animations.easing.SineInOutEasing;


public class ScalingAnimation extends SoftenAnimation {

	Float newScale = null;
	float baseScale;
	FactorFinder scaleFinder ;
	
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
	
	public ScalingAnimation setItem(Item item) {
		return (ScalingAnimation)super.setItem(item);
	}
	
	public void setScaling(FactorFinder scaleFinder) {
		this.scaleFinder = scaleFinder;
	}
	
	public void setScaling(float scale) {
		this.newScale = scale;
	}
	
	public Float getScale() {
		return newScale==null ? scaleFinder.find(getContext()) : newScale;
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
	
	@Override
	public boolean executeAnimation(long time) {
		getItem().setScale(easing.getValue(baseScale, getScale()), getUpdateSerial());
		return true;
	}

	@Override
	public void finish(long time) {
		getItem().setScale(newScale, getUpdateSerial());
		super.finish(time);
	}

	@Override
	public void reset() {
		super.reset();
		this.baseScale = 0.0f;
	}
	
	public static class Builder extends SoftenAnimation.Builder {
		Float newScale = null;
		FactorFinder scaleFinder = null;
		
		public Builder(Easing.Factory easing, Float newScale) {
			super(easing);
			this.newScale = newScale;
		}

		public Builder(Easing.Factory easing) {
			super(easing);
		}

		public Builder(long duration) {
			this(new SineInOutEasing.Builder(duration));
		}

		public Builder(long duration, Float newScale) {
			this(new SineInOutEasing.Builder(duration), newScale);
		}
		
		public Builder setScaling(FactorFinder scaleFinder) {
			this.scaleFinder = scaleFinder;
			return this;
		}
		
		@Override
		public Builder setItem(MoveableFinder itemFinder) {
			super.setItem(itemFinder);
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
			if (scaleFinder!=null) {
				((ScalingAnimation)animation).setScaling(scaleFinder);
			}
		}
		
	}
	
}

