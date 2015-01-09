package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Item;

public interface RotatableFunny extends Funny {

	Item[] getRotatableItems();

	Animation.Factory getFinishRotateAnimation();

	Float adjustRotation(float angle);

}
