package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.basic.Animation.Factory;
import com.ithaque.funnies.shared.basic.Item;



public interface HoverFunny extends Funny {

	Item[] getHoverables();

	Factory getHoverAnimation(Item item);
}
