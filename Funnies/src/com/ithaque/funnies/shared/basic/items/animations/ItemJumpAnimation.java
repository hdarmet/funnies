package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;

public class ItemJumpAnimation extends ItemMoveAnimation {

	float factor;
	
	public ItemJumpAnimation(long duration, float factor) {
		this(new LinearEasing(duration), factor);
	}

	public ItemJumpAnimation(Easing easing, float factor) {
		super(easing);
		this.factor = factor;
	}
	
	protected ItemJumpAnimation(Easing easing, Location location, float factor) {
		super(easing, location);
		this.factor = factor;
	}
	
	Location baseLocation;
	
	@Override
	public ItemMoveAnimation duplicate() {
		return new ItemJumpAnimation(getEasing().duplicate(), getLocation(), factor);
	}

	@Override
	protected void launch(Item item) {
		super.launch(item);
		this.baseLocation = item.getLocation();
	}
	
	@Override
	protected boolean executeAnimation(Easing easing, long time) {
		float dx = baseLocation.getX()-getLocation().getX();
		if (dx==0) {
			return false;
		}
		float x = easing.getValue(baseLocation.getX(), getLocation().getX());
		float y = easing.getValue(baseLocation.getY(), getLocation().getY());
		float dy = (baseLocation.getX()-x)*(getLocation().getX()-x)/dx*4*factor;
		dy=dy<0?dy:-dy;
		getItem().setLocation(new Location(x, y+dy));
		return true;
	}

	@Override
	public void finish(long time) {
		if (getLocation()!=null) {
			item.setLocation(getLocation());
		}
		super.finish(time);
	}

}
