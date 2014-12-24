package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.items.animations.SoftenAnimation;
import com.ithaque.funnies.shared.basic.items.animations.MoveAnimation;

public interface DraggableFunny extends Funny {

	Item[] getDraggableItems();

	SoftenAnimation.Builder getBeginDragAnimation();

	MoveAnimation.Builder getAdjustLocationAnimation();

	SoftenAnimation.Builder getDraggedDropAnimation();

}
