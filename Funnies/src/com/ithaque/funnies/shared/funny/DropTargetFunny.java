package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemHolder;
import com.ithaque.funnies.shared.basic.Location;

public interface DropTargetFunny extends Funny {

	Item[] getDropTargetItems();

	Animation.Factory getTargetDropAnimation();

	Animation.Factory getEnterTargetAnimation();

	Animation.Factory getExitTargetAnimation();

	Animation.Factory getShowAllowedTargetAnimation();

	Animation.Factory getHideAllowedTargetAnimation();

	Item getHilightItem(Item target);

	Location getDropLocation(Item dragged, Item target);

	ItemHolder getDropHolder(Item dragged, Item target);

}
