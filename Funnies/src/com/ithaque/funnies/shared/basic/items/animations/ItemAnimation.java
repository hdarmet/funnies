package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.Trace;
import com.ithaque.funnies.shared.basic.AbstractAnimation;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.AnimationContext.MoveableFinder;
import com.ithaque.funnies.shared.basic.Item;

public abstract class ItemAnimation extends AbstractAnimation {

	Item item;
	MoveableFinder itemFinder = null;

	public ItemAnimation() {
	}
	
	public void setItem(MoveableFinder itemFinder) {
		this.itemFinder = itemFinder;
	}

	@Override
	public boolean start(long time) {
		if (super.start(time)) {
			if (Trace.debug) {
				Trace.debug("Item : "+getItem()+" "+itemFinder+" "+item+" "+this);
			}
			return true;
		}
		return false;
	}
	
	public ItemAnimation setItem(Item item) {
		this.item = item;
		return this;
	}
	
	public Item getItem() {
		return item==null ? (Item)itemFinder.find(getContext()) : item;
	}
	
	public static abstract class Builder implements Animation.Factory {
		MoveableFinder itemFinder;
		
		public Builder() {
			super();
		}

		public Builder setItem(MoveableFinder itemFinder) {
			this.itemFinder = itemFinder;
			return this;
		}

		protected void prepare(ItemAnimation animation) {
			if (itemFinder!=null) {
				animation.setItem(itemFinder);
			}
		}
		
		public abstract ItemAnimation create();
	}
}
