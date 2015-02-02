package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.GroupItem;

public interface PanelRing extends Ring {

	GroupItem getPanelSupport();
	
	void enterModalMode(Animation animation);
	
	void exitModalMode(Animation animation);

}
