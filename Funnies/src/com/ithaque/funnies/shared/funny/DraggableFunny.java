package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.items.animations.ItemAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ItemMoveAnimation;

public interface DraggableFunny extends Funny {

	Item[] getDraggableItems();

	ItemAnimation getBeginDragAnimation();

	ItemAnimation getAdjustLocationAnimation();

	ItemMoveAnimation getDraggedDropAnimation();

}
