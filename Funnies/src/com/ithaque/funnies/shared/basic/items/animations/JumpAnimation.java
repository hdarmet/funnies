package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.items.animations.easing.LinearEasing;

public class JumpAnimation extends MoveAnimation {

	float factor;
	
	public JumpAnimation(Easing easing, float factor) {
		super(easing);
		this.factor = factor;
	}

	public JumpAnimation(long duration, float factor) {
		this(new LinearEasing(duration), factor);
	}
	
	@Override
	public boolean executeAnimation(long time) {
		float dx = baseLocation.getX()-getLocation().getX();
		if (dx==0) {
			return false;
		}
		float x = getEasing().getValue(baseLocation.getX(), getLocation().getX());
		float y = getEasing().getValue(baseLocation.getY(), getLocation().getY());
		float dy = (baseLocation.getX()-x)*(getLocation().getX()-x)/dx*4*factor;
		dy=dy<0?dy:-dy;
		getItem().setLocation(new Location(x, y+dy));
		return true;
	}

	@Override
	public void finish(long time) {
		if (getLocation()!=null) {
			getItem().setLocation(getLocation());
		}
		super.finish(time);
	}

	public static class Builder extends MoveAnimation.Builder {
		float factor;
		
		public Builder(Easing.Factory easing, float factor) {
			super(easing);
			this.factor = factor;
		}

		public Builder(long duration, float factor) {
			this(new LinearEasing.Builder(duration), factor);
		}
		
		@Override
		public JumpAnimation create() {
			JumpAnimation animation =  new JumpAnimation(easing.create(), factor);
			prepare(animation);
			return animation;
		}

	}
}
