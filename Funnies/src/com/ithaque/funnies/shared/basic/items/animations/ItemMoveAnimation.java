package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.Animation;
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public interface Builder extends Animation.Factory {
		@Override
		ItemMoveAnimation create();
		
		void setLocation(Location location);
	}
}
