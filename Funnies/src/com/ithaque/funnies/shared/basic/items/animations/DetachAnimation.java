package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.Item;

public class DetachAnimation extends ItemAnimation {

	long endTime;
	
	@Override
	public long getDuration() {
		return 0;
	}

	@Override
	public boolean start(long time) {
		if (super.start(time)) {
			endTime = time;
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean executeAnimation(long time) {
		return false;
	}

	@Override
	public long getEndTime() {
		return endTime;
	}

	@Override 
	public void finish(long time) {
		Item item = getItem();
		if (item!=null) {
			item.getParent().removeItem(item);
		}
	}
	
	public static class Builder extends ItemAnimation.Builder {
		
		public Builder() {
			super();
		}
		
		public DetachAnimation create() {
			DetachAnimation animation =  new DetachAnimation();
			prepare(animation); 
			return animation;
		};
	}
}
