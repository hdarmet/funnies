package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Item;

public interface DraggableFunny extends Funny {

	Item[] getDraggableItems();

	Animation.Factory getBeginDragAnimation();

	Animation.Factory getAdjustLocationAnimation();

	Animation.Factory getDraggedDropAnimation();

}
