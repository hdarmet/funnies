package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.Geometric;
import com.ithaque.funnies.shared.Location;
import com.ithaque.funnies.shared.basic.items.animations.easing.LinearEasing;

public class BezierAnimation extends MoveAnimation {

	float factor;
	Location[] points = null;
	
	protected BezierAnimation(Easing easing, Location ... points) {
		super(easing);
		this.points = points;
	}

	public BezierAnimation(long duration, Location ... points) {
		this(new LinearEasing(duration), points);
	}
	
	@Override
	public boolean executeAnimation(long time) {
		float pos = getEasing().getValue(0.0f, 1.0f);
		getItem().setLocation(Geometric.getBezier(pos, baseLocation, points[0], getLocation()), getUpdateSerial());
		return true;
	}

	public static class Builder extends MoveAnimation.Builder {
		Location[] points = null;
		
		protected Builder(Easing.Factory easing, Location[] points) {
			super(easing);
			this.points = points;
		}

		public Builder(Easing.Factory easing, Location point) {
			this(easing, new Location[] {point});
		}
		
		public Builder(Easing.Factory easing, Location point0, Location point1) {
			this(easing, new Location[] {point0, point1});
		}
		
		public Builder(long duration, Location point) {
			this(new LinearEasing.Builder(duration), new Location[] {point});
		}
		
		public Builder(long duration, Location point0, Location point1) {
			this(new LinearEasing.Builder(duration), new Location[] {point0, point1});
		}
		
		@Override
		public BezierAnimation create() {
			BezierAnimation animation =  new BezierAnimation(easing.create(), points);
			prepare(animation);
			return animation;
		}

	}
}
