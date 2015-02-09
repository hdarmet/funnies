package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.Location;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemHolder;

public interface TargetFunny extends Funny {

	Location getTargetLocation(Item dragged);
	
	Location getLocation();

	ItemHolder getTargetHolder();

}
