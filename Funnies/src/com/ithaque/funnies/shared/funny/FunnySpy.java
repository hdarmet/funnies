package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.Trace;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemObserver;

public class FunnySpy implements ItemObserver {

	@Override
	public void change(ChangeType type, Item item) {
		Trace.debug(type+"("+item+"):par:"+item.getParent()+", loc:"+item.getLocation()+", rot:"+item.getRotation()+", scl:"+item.getScale()+"\n");
	}

}
