package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.Geometric;



public class OptimizedRotateAnimation extends RotateAnimation {

	public OptimizedRotateAnimation(Easing easing) {
		super(easing);
	}

	public OptimizedRotateAnimation(Easing easing, Float newAngle) {
		super(easing, newAngle);
	}
	
	public OptimizedRotateAnimation(long duration) {
		super(duration);
	}
	
	public OptimizedRotateAnimation(long duration, Float newAngle) {
		super(duration, newAngle);
	}

	@Override
	public Float getRotation() {
		Float currentAngle = getItem().getRotation();
		Float targetAngle = super.getRotation();
		Float result =  Geometric.optimizeRotation(currentAngle, targetAngle);
		System.out.println("Angles : "+currentAngle+" "+targetAngle+" "+result);
		return result;
	}

	public static class Builder extends RotateAnimation.Builder {
		
		public Builder(Easing.Factory easing, Float newAngle) {
			super(easing, newAngle);
		}

		public Builder(long duration, Float newAngle) {
			super(duration, newAngle);
		}
		
		public Builder(Easing.Factory easing) {
			super(easing);
		}

		public Builder(long duration) {
			super(duration);
		}

		@Override
		public OptimizedRotateAnimation create() {
			OptimizedRotateAnimation animation = new OptimizedRotateAnimation(easing.create(), newRotation);
			prepare(animation);
			return animation;
		}
	}

}
