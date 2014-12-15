package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.Location;

public class ItemJumpAnimation extends ItemMoveAnimation {

	Location baseLocation;
	float factor;
	
	public ItemJumpAnimation(Easing easing, float factor) {
		super(easing);
		this.factor = factor;
	}

	public ItemJumpAnimation(long duration, float factor) {
		this(new LinearEasing(duration), factor);
	}
	
	@Override
	public void launch() {
		super.launch();
		this.baseLocation = getItem().getLocation();
		System.out.println("start jump !");
	}
	
	@Override
	protected boolean executeAnimation(long time) {
		System.out.println("animate jump !");
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
		System.out.println("finish jump !");
		if (getLocation()!=null) {
			getItem().setLocation(getLocation());
		}
		super.finish(time);
	}

	public static class Builder implements ItemMoveAnimation.Builder {
		Easing.Factory easing;
		float factor;
		Location location;
		
		public Builder(Easing.Factory easing, float factor) {
			super();
			this.easing = easing;
			this.factor = factor;
		}

		public Builder(long duration, float factor) {
			this(new LinearEasing.Builder(duration), factor);
		}
		
		@Override
		public ItemJumpAnimation create() {
			ItemJumpAnimation animation =  new ItemJumpAnimation(easing.create(), factor);
			animation.setLocation(location);
			return animation;
		}

		@Override
		public void setLocation(Location location) {
			this.location = location;
		}	

	}
}
