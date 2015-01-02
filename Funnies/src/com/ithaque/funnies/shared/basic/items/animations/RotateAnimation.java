package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.AnimationContext.Key;
import com.ithaque.funnies.shared.basic.items.animations.easing.SineInOutEasing;


public class RotateAnimation extends SoftenAnimation {

	Float newAngle = null;
	float baseAngle;
	Key rotationKey ;
	
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
		}
		return result;
	}
	
	public void setRotationKey(Key rotationKey) {
		this.rotationKey = rotationKey;
	}
	
	public Float getRotation() {
		return newAngle==null ? getContext().getFactor(rotationKey) : newAngle;
	}
	
	@Override
	public boolean executeAnimation(long time) {
		System.out.println("animate : "+baseAngle+" "+getRotation());
		getItem().setRotation(easing.getValue(baseAngle, getRotation()));
		return true;
	}

	@Override
	public void finish(long time) {
		getItem().setRotation(getRotation());
		super.finish(time);
	}

	public static class Builder extends SoftenAnimation.Builder {
		Float newRotation;
		Key rotationKey;
		
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

		public Builder setItemKey(Key itemKey) {
			super.setItemKey(itemKey);
			return this;
		}

		public Builder setRotationKey(Key rotationKey) {
			this.rotationKey = rotationKey;
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
			if (rotationKey!=null) {
				((RotateAnimation)animation).setRotationKey(rotationKey);
			}
		}

	}
	
}