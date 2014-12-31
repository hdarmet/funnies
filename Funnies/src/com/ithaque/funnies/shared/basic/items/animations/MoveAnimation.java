package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.AnimationContext.Key;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.items.animations.easing.LinearEasing;

public class MoveAnimation extends SoftenAnimation {

	Location location;
	Location baseLocation;
	Key locationKey;
	
	public MoveAnimation(Easing easing) {
		super(easing);
	}
	
	public MoveAnimation(Easing easing, Location location) {
		super(easing);
		this.location = location;
	}

	public void setLocationKey(Key locationKey) {
		this.locationKey = locationKey;
	}

	@Override
	public boolean executeAnimation(long time) {
		Location location = getItem().getLocation();
		if (location!=null && getLocation()!=null) {
			getItem().setLocation(
				getEasing().getValue(baseLocation.getX(), getLocation().getX()),
				getEasing().getValue(baseLocation.getY(), getLocation().getY()));
		}
		return true;
	}

	@Override
	public void finish(long time) {
		getItem().setLocation(getLocation());
		super.finish(time);
	}
	
	@Override
	public boolean start(long time) {
		boolean result = super.start(time);
		if (result) {
			this.baseLocation = getItem().getLocation();
		}
		return result;
	}
	
	public Location getLocation() {
		return location==null ? getContext().getLocation(locationKey) : location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public static class Builder extends SoftenAnimation.Builder {
		Location location;
		Key locationKey;
		
		public Builder(Easing.Factory easing) {
			super(easing);
		}

		public Builder(long duration) {
			this(new LinearEasing.Builder(duration));
		}
		
		@Override
		public MoveAnimation create() {
			System.out.println("Get easing : "+getEasing());
			MoveAnimation animation =  new MoveAnimation(getEasing().create());
			prepare(animation); 
			return animation;
		}

		@Override
		public Builder setItemKey(Key itemKey) {
			super.setItemKey(itemKey);
			return this;
		}
		
		public Builder setLocationKey(Key locationKey) {
			this.locationKey = locationKey;
			return this;
		}
		
		@Override
		protected void prepare(SoftenAnimation animation) {
			super.prepare(animation);
			if (location!=null) {
				((MoveAnimation)animation).setLocation(location);
			}
			else if (locationKey!=null) {
				((MoveAnimation)animation).setLocationKey(locationKey);
			}
		}

		public Builder setLocation(Location location) {
			this.location = location;
			return this;
		}	

	}
	
}
