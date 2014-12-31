package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.Trace;
import com.ithaque.funnies.shared.basic.AbstractAnimation;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.AnimationContext.Key;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.items.animations.easing.LinearEasing;

public abstract class SoftenAnimation extends AbstractAnimation {

	public static final long INTERVAL = 40;
	
	Item item;
	Easing easing;
	Key itemKey;

	public SoftenAnimation(Easing easing) {
		this.easing = easing;
	}
	
	public void setItemKey(Key itemKey) {
		this.itemKey = itemKey;
	}

	@Override
	public boolean start(long time) {
		super.start(time);
//		if (Trace.debug) {
			Trace.debug("Item : "+getItem()+" "+itemKey+" "+item+" "+this);
//		}
		this.easing.launch(getItem().getBoard());
		return true;
	}
	
	protected Easing getEasing() {
		return easing;
	}
	
	public SoftenAnimation setItem(Item item) {
		this.item = item;
		return this;
	}
	
	public Item getItem() {
		return item==null ? getContext().getItem(itemKey) : item;
	}

	public long getDuration() {
		return getEasing().getDuration();
	}
	
	public long getEndTime() {
		return getEasing().getEndTime();
	}
	
	public static abstract class Builder implements Animation.Factory {
		Easing.Factory easing;
		Key itemKey;
		
		public Builder(Easing.Factory easing) {
			super();
			this.easing = easing;
		}

		public Builder setItemKey(Key itemKey) {
			this.itemKey = itemKey;
			return this;
		}
		
		public Builder(long duration) {
			this(new LinearEasing.Builder(duration));
		}

		protected void prepare(SoftenAnimation animation) {
			if (itemKey!=null) {
				animation.setItemKey(itemKey);
			}
		}

		protected Easing.Factory getEasing() {
			return easing;
		}
		
		public abstract SoftenAnimation create();
	}
}
