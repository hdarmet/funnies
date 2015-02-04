package com.ithaque.funnies.shared.funny;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.basic.Animation.Factory;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.processors.RandomAnimationProcessor;

public class CircusRandomAnimationProcessor extends RandomAnimationProcessor {

	public CircusRandomAnimationProcessor(AbstractRing ring, long timeout, String alarmId, int segCount) {
		super(ring.getBoard(), timeout, alarmId, segCount);
		this.ring = ring;
	}

	FunnyRegistry<AnimatedFunny> animatedFunnies = new FunnyRegistry<AnimatedFunny>(
			new FunnyRegistry.ItemsFinder<AnimatedFunny>() {
				@Override
				public Item[] getItems(AnimatedFunny funny) {
					return funny.getAnimatedItems();
				}
			}
		) {
		@Override
		protected Record<AnimatedFunny> createRecord(Item item, AnimatedFunny funny) {
			return new Record<AnimatedFunny>(funny, item);
		}
	};
	AbstractRing ring;

	@Override
	protected List<Item> getAnimatedItems() {
		return new ArrayList<Item>(animatedFunnies.getItems());
	}

	@Override
	protected Factory getAnimation(Item item) {
		AnimatedFunny animated = animatedFunnies.getFunny(item);
		if (animated!=null) {
			return animated.getAnimation(item);
		}
		else {
			return null; 
		}
	}

	public void registerAnimatedFunny(AnimatedFunny funny) {
		animatedFunnies.registerFunny(funny);
	}
	
	public void unregisterAnimatedFunny(AnimatedFunny funny) {
		animatedFunnies.unregisterFunny(funny);
	}

}
