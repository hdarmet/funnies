package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.AnimationContext.MoveableFinder;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.items.animations.easing.LinearEasing;

public abstract class SoftenAnimation extends ItemAnimation {

	public static final long INTERVAL = 40;

	Easing easing;

	public SoftenAnimation(Easing easing) {
		this.easing = easing;
	}

	@Override
	public boolean start(long time) {
		if (super.start(time)) {
			Board board = getItem().getBoard();
			if (board!=null) {
				this.easing.launch(board);
				return true;
			}
		}
		return false;
	}
	
	protected Easing getEasing() {
		return easing;
	}
	
	public SoftenAnimation setItem(Item item) {
		return (SoftenAnimation)super.setItem(item);
	}

	public long getDuration() {
		return getEasing().getDuration();
	}
	
	public long getEndTime() {
		return getEasing().getEndTime();
	}
	
	public static abstract class Builder extends ItemAnimation.Builder {
		Easing.Factory easing;
		
		public Builder(Easing.Factory easing) {
			super();
			this.easing = easing;
		}

		public Builder(long duration) {
			this(new LinearEasing.Builder(duration));
		}

		protected Easing.Factory getEasing() {
			return easing;
		}

		public Builder setItem(MoveableFinder finder) {
			return (Builder)super.setItem(finder);
		}
		
		public abstract SoftenAnimation create();
	}
}
