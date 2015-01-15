package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Item;

public interface AnimatedFunny extends Funny {

	Animation.Factory getAnimation(Item item);

	Item[] getAnimatedItems();

}
