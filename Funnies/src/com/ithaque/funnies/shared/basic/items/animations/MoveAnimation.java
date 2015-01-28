package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.AnimationContext.LocationFinder;
import com.ithaque.funnies.shared.basic.AnimationContext.MoveableFinder;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.items.animations.easing.LinearEasing;
import com.ithaque.funnies.shared.basic.items.animations.easing.SineInOutEasing;

public class MoveAnimation extends SoftenAnimation {

	Location location;
	Location destLocation;
	Location baseLocation;
	LocationFinder locationFinder;
	
	public MoveAnimation(Easing easing) {
		super(easing);
	}
	
	public MoveAnimation(Easing easing, Location location) {
		super(easing);
		this.location = location;
	}

	public MoveAnimation(long duration) {
		this(new SineInOutEasing(duration));
	}
	
	public MoveAnimation(long duration, Location location) {
		this(new SineInOutEasing(duration), location);
	}
	
	public void setLocation(LocationFinder locationKey) {
		this.locationFinder = locationKey;
	}

	@Override
	public boolean executeAnimation(long time) {
		Location location = getItem().getLocation();
		if (location!=null && getLocation()!=null) {
			getItem().setLocation(
				getEasing().getValue(baseLocation.getX(), destLocation.getX()),
				getEasing().getValue(baseLocation.getY(), destLocation.getY()), 
				getUpdateSerial());
		}
		return true;
	}

	@Override
	public void finish(long time) {
		getItem().setLocation(getLocation(), getUpdateSerial());
		super.finish(time);
	}
	
	@Override
	public boolean start(long time) {
		boolean result = super.start(time);
		if (result) {
			this.baseLocation = getItem().getLocation();
			destLocation = getLocation();
		}
		return result;
	}
	
	@Override
	public void reset() {
		super.reset();
		this.baseLocation = null;
		this.destLocation = null;
	}
	
	public Location getLocation() {
		return location==null ? locationFinder.find(getContext()) : location;
	}

	public MoveAnimation setLocation(Location location) {
		this.location = location;
		return this;
	}
	
	public static class Builder extends SoftenAnimation.Builder {
		Location location;
		LocationFinder locationFinder;

		public Builder(Easing.Factory easing) {
			super(easing);
		}

		public Builder(long duration) {
			this(new LinearEasing.Builder(duration));
		}
		
		@Override
		public MoveAnimation create() {
			MoveAnimation animation =  new MoveAnimation(getEasing().create());
			prepare(animation); 
			return animation;
		}

		@Override
		public Builder setItem(MoveableFinder itemFinder) {
			super.setItem(itemFinder);
			return this;
		}
		
		public Builder setLocation(LocationFinder locationFinder) {
			this.locationFinder = locationFinder;
			return this;
		}

		@Override
		protected void prepare(SoftenAnimation animation) {
			super.prepare(animation);
			if (location!=null) {
				((MoveAnimation)animation).setLocation(location);
			}
			else if (locationFinder!=null) {
				((MoveAnimation)animation).setLocation(locationFinder);
			}
		}

		public Builder setLocation(Location location) {
			this.location = location;
			return this;
		}	

	}
	
}
