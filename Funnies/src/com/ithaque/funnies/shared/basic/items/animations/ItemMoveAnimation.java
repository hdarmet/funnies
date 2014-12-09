package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.Location;

public abstract class ItemMoveAnimation extends ItemAnimation {

	Location location;
	
	public ItemMoveAnimation(Easing easing) {
		super(easing);
	}

	protected ItemMoveAnimation(Easing easing, Location location) {
		super(easing);
		this.location = location;
	}
	
	public ItemMoveAnimation(long duration) {
		super(duration);
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public abstract ItemMoveAnimation duplicate();
}
