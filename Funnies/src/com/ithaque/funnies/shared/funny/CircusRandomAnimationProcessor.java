package com.ithaque.funnies.shared.funny;

import java.util.HashMap;
import java.util.Map;

import com.ithaque.funnies.shared.basic.Animation.Factory;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.processors.RandomAnimationProcessor;

public class CircusRandomAnimationProcessor extends RandomAnimationProcessor {

	public CircusRandomAnimationProcessor(Ring ring, long timeout, String alarmId, int segCount) {
		super(ring.getBoard(), timeout, alarmId, segCount);
		this.ring = ring;
	}

	Map<Item, AnimatedFunny> animatedFunnies = new HashMap<Item, AnimatedFunny>();
	Ring ring;

	@Override
	protected Factory getAnimation(Item item) {
		AnimatedFunny animated = animatedFunnies.get(item);
		if (animated!=null) {
			return animated.getAnimation(item);
		}
		else {
			return null; 
		}
	}

	public void registerAnimatedFunny(AnimatedFunny funny) {
		for (Item item : funny.getAnimatedItems()) { 
			animatedFunnies.put(item, funny);
			addAnimated(item);
		}
	}
	
	public void unregisterAnimatedFunny(AnimatedFunny funny) {
		for (Item item : funny.getAnimatedItems()) {
			animatedFunnies.remove(item);
			removeAnimated(item);
		}
	}

}
