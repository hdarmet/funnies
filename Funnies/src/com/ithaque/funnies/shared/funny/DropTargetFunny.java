package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.items.animations.ItemAnimation;

public interface DropTargetFunny extends Funny {

	Item[] getDropTargetItems();

	ItemAnimation getTargetDropAnimation();

	ItemAnimation getEnterTargetAnimation();

	ItemAnimation getExitTargetAnimation();

}
