package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.funny.manager.Notification;

public interface Ring {

	void init();

	boolean enterRing(Funny funny);

	boolean exitRing(Funny funny);
	
	void notify(Notification fact);

	float getWidth();
	
	float getHeight();

	void launch(Animation animation);
}
