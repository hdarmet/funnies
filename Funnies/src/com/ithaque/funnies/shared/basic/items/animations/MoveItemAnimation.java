package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;

public class MoveItemAnimation extends ItemAnimation {

	Location newLocation;
	Float newAngle;
	Float newScale;
	
	Location baseLocation;
	float baseAngle;
	float baseScale;
	
	public MoveItemAnimation(Easing easing, Location newLocation, Float newAngle, Float newScale) {
		super(easing);
		this.newLocation = newLocation;
		this.newAngle = newAngle;
		this.newScale = newScale;
	}

	public MoveItemAnimation(long duration, float x, float y, Float newAngle, Float newScale) {
		this(new SineInOutEasing(duration), new Location(x, y), newAngle, newScale);
	}
	
	public MoveItemAnimation(long duration, Location newLocation, Float newAngle, Float newScale) {
		this(new SineInOutEasing(duration), newLocation, newAngle, newScale);
	}
	
	@Override
	protected void launch(Item item) {
		super.launch(item);
		this.baseLocation = item.getLocation();
		this.baseAngle = item.getRotation();
		this.baseScale = item.getScale();
	}
	
	@Override
	public boolean executeAnimation(Easing easing, long time) {
		if (this.newLocation != null) {
			Location location = item.getLocation();
			if (location!=null && newLocation!=null) {
				item.setLocation(
					easing.getValue(baseLocation.getX(), newLocation.getX()),
					easing.getValue(baseLocation.getY(), newLocation.getY()));
			}
		}
		if (newAngle!=null) {
			item.setRotation(easing.getValue(baseAngle, newAngle));
		}
		if (newScale!=null) {
			item.setScale(easing.getValue(baseScale, newScale));
		}
		return true;
	}

	@Override
	public void finish(long time) {
		if (this.newLocation!=null) {
			item.setLocation(newLocation);
		}
		if (this.newAngle!=null) {
			item.setRotation(newAngle);
		}
		if (this.newScale!=null) {
			item.setScale(newScale);
		}
		super.finish(time);
	}

	@Override
	public ItemAnimation duplicate() {
		return new MoveItemAnimation(easing.duplicate(), newLocation, newAngle, newScale);
	}

}
