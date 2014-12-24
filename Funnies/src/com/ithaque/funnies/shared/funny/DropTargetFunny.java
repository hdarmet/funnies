package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.items.animations.SoftenAnimation;

public interface DropTargetFunny extends Funny {

	Item[] getDropTargetItems();

	SoftenAnimation.Builder getTargetDropAnimation();

	SoftenAnimation.Builder getEnterTargetAnimation();

	SoftenAnimation.Builder getExitTargetAnimation();

	SoftenAnimation.Builder getShowAllowedTargetAnimation();

	SoftenAnimation.Builder getHideAllowedTargetAnimation();

	Item getHilightItem(Item target);
}
