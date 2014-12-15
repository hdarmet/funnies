package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.items.animations.ItemMoveAnimation;

public interface DraggableFunny extends Funny {

	Item[] getDraggableItems();

	Animation.Factory getBeginDragAnimation();

	ItemMoveAnimation.Builder getAdjustLocationAnimation();

	Animation.Factory getDraggedDropAnimation();

}
