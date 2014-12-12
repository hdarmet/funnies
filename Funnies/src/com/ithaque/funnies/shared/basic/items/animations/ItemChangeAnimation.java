package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;

public class ItemChangeAnimation extends ItemMoveAnimation {

	Float newAngle;
	Float newScale;
	
	Location baseLocation;
	float baseAngle;
	float baseScale;
	
	public ItemChangeAnimation(Easing easing, Location newLocation, Float newAngle, Float newScale) {
		super(easing);
		setLocation(newLocation);
		this.newAngle = newAngle;
		this.newScale = newScale;
	}

	public ItemChangeAnimation(long duration, float x, float y, Float newAngle, Float newScale) {
		this(new SineInOutEasing(duration), new Location(x, y), newAngle, newScale);
	}
	
	public ItemChangeAnimation(long duration, Location newLocation, Float newAngle, Float newScale) {
		this(new SineInOutEasing(duration), newLocation, newAngle, newScale);
	}
	
	@Override
	protected void launch(Item item) {
		System.out.println("I: "+item);
		super.launch(item);
		this.baseLocation = item.getLocation();
		this.baseAngle = item.getRotation();
		this.baseScale = item.getScale();
		System.out.println("I: "+getItem());

	}
	
	@Override
	public boolean executeAnimation(Easing easing, long time) {
		if (getLocation() != null) {
			Location location = getItem().getLocation();
			if (location!=null && getLocation()!=null) {
				getItem().setLocation(
					easing.getValue(baseLocation.getX(), getLocation().getX()),
					easing.getValue(baseLocation.getY(), getLocation().getY()));
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
			System.out.println("A : "+this.newAngle+" "+getItem());
			getItem().setRotation(newAngle);
		}
		if (this.newScale!=null) {
			getItem().setScale(newScale);
		}
		super.finish(time);
	}

	@Override
	public ItemMoveAnimation duplicate() {
		System.out.println("dup");
		return new ItemChangeAnimation(easing.duplicate(), getLocation(), newAngle, newScale);
	}

}
