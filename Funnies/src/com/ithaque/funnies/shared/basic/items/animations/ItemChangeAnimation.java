package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.Location;

public class ItemChangeAnimation extends ItemMoveAnimation {

	Location baseLocation;
	Float newAngle;
	Float newScale;
	float baseAngle;
	float baseScale;
	
	public ItemChangeAnimation(Easing easing, Float newAngle, Float newScale) {
		super(easing);
		this.newAngle = newAngle;
		this.newScale = newScale;
	}
	
	public ItemChangeAnimation(long duration, Float newAngle, Float newScale) {
		this(new SineInOutEasing(duration), newAngle, newScale);
	}
	
	@Override
	public void launch() {
		super.launch();
		this.baseLocation = getItem().getLocation();
		this.baseAngle = getItem().getRotation();
		this.baseScale = getItem().getScale();
	}
	
	@Override
	public boolean executeAnimation(long time) {
		if (getLocation() != null) {
			Location location = getItem().getLocation();
			if (location!=null && getLocation()!=null) {
				getItem().setLocation(
					getEasing().getValue(baseLocation.getX(), getLocation().getX()),
					getEasing().getValue(baseLocation.getY(), getLocation().getY()));
			}
		}
		if (newAngle!=null) {
			getItem().setRotation(easing.getValue(baseAngle, newAngle));
		}
		if (newScale!=null) {
			getItem().setScale(easing.getValue(baseScale, newScale));
		}
		return true;
	}

	@Override
	public void finish(long time) {
		if (getLocation()!=null) {
			getItem().setLocation(getLocation());
		}
		if (this.newAngle!=null) {
			getItem().setRotation(newAngle);
		}
		if (this.newScale!=null) {
			getItem().setScale(newScale);
		}
		super.finish(time);
	}

	public static class Builder implements ItemMoveAnimation.Builder {
		Easing.Factory easing;
		Float newAngle;
		Float newScale;
		Location location;
		
		public Builder(Easing.Factory easing, Float newAngle, Float newScale) {
			super();
			this.easing = easing;
			this.newAngle = newAngle;
			this.newScale = newScale;
		}

		public Builder(long duration, Float newAngle, Float newScale) {
			this(new SineInOutEasing.Builder(duration), newAngle, newScale);
		}
		
		@Override
		public ItemChangeAnimation create() {
			ItemChangeAnimation animation = new ItemChangeAnimation(easing.create(), newAngle, newScale);
			animation.setLocation(location);
			return animation;
		}

		@Override
		public void setLocation(Location location) {
			this.location = location;
		}	

	}
	
}
