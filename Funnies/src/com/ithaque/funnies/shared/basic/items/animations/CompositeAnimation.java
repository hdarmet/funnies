package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.Animation;

public interface CompositeAnimation extends Animation {
	void addAnimation(Animation animation);
}
