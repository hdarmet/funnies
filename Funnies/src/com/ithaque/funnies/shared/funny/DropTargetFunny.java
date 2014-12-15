package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Item;

public interface DropTargetFunny extends Funny {

	Item[] getDropTargetItems();

	Animation.Factory getTargetDropAnimation();

	Animation.Factory getEnterTargetAnimation();

	Animation.Factory getExitTargetAnimation();

	Animation.Factory getShowAllowedTargetAnimation();

	Animation.Factory getHideAllowedTargetAnimation();

	Item getHilightItem(Item target);
}
