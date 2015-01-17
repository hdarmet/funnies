package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.AnimationContext.FactorFinder;
import com.ithaque.funnies.shared.basic.AnimationContext.MoveableFinder;
import com.ithaque.funnies.shared.basic.items.animations.easing.SineInOutEasing;


public class RotateAnimation extends SoftenAnimation {

	Float newAngle = null;
	float baseAngle;
	float targetAngle;
	FactorFinder rotationFinder ;
	
	public RotateAnimation(Easing easing) {
		super(easing);
	}
	
	public RotateAnimation(Easing easing, Float newAngle) {
		super(easing);
		this.newAngle = newAngle;
	}
	
	public RotateAnimation(long duration) {
		this(new SineInOutEasing(duration));
	}
	
	public RotateAnimation(long duration, Float newAngle) {
		this(new SineInOutEasing(duration), newAngle);
	}
	
	@Override
	public boolean start(long time) {
		boolean result = super.start(time);
		if (result) {
			this.baseAngle = getItem().getRotation();
			this.targetAngle = getRotation();
		}
		return result;
	}
	
	@Override
	public void reset() {
		super.reset();
		this.baseAngle = 0.0f;
		this.targetAngle = 0.0f;
	}
	
	public void setRotation(FactorFinder rotationFinder) {
		this.rotationFinder = rotationFinder;
	}
	
	public Float getRotation() {
		return newAngle==null ? rotationFinder.find(getContext()) : newAngle;
	}
	
	@Override
	public boolean executeAnimation(long time) {
		getItem().setRotation(easing.getValue(baseAngle, targetAngle));
		return true;
	}

	@Override
	public void finish(long time) {
		getItem().setRotation(getRotation());
		super.finish(time);
	}

	public static class Builder extends SoftenAnimation.Builder {
		Float newRotation;
		FactorFinder rotationFinder;
		
		public Builder(Easing.Factory easing, Float newAngle) {
			super(easing);
			this.newRotation = newAngle;
		}

		public Builder(long duration, Float newAngle) {
			this(new SineInOutEasing.Builder(duration), newAngle);
		}
		
		public Builder(Easing.Factory easing) {
			this(easing, null);
		}

		public Builder(long duration) {
			this(duration, null);
		}

		public Builder setItem(MoveableFinder itemFinder) {
			super.setItem(itemFinder);
			return this;
		}

		public Builder setRotation(FactorFinder rotationFinder) {
			this.rotationFinder = rotationFinder;
			return this;
		}
		
		@Override
		public RotateAnimation create() {
			RotateAnimation animation = new RotateAnimation(easing.create(), newRotation);
			prepare(animation);
			return animation;
		}

		protected void prepare(SoftenAnimation animation) {
			super.prepare(animation);
			if (rotationFinder!=null) {
				((RotateAnimation)animation).setRotation(rotationFinder);
			}
		}

	}
	
}
