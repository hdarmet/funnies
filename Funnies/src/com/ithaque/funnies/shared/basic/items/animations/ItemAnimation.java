package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.Animation;

public abstract class ItemAnimation extends Animation {
	
	Easing easing;

	public ItemAnimation(Easing easing) {
		this.easing = easing;
	}

	protected Easing getEasing() {
		return easing;
	}
	
	public void launch() {
		super.launch();
		this.easing.launch(getItem().getBoard());
	}

	public long getDuration() {
		return getEasing().getDuration();
	}
	
	public long getEndTime() {
		return getEasing().getEndTime();
	}
}
