package com.ithaque.funnies.shared.funny;

import java.util.HashMap;
import java.util.Map;

import com.ithaque.funnies.shared.basic.GroupItem;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;

public abstract class DecoratedFunny extends AbstractFunny {

	Map<Integer, Item> decorations = null;
	Map<Item, Integer> keys = null;
	
	public DecoratedFunny(String id) {
		super(id);
	}

	public abstract GroupItem getDecorationSupport();
	
	public void putDecoration(Integer key, Item decoration, Location location) {
		if (decorations==null) {
			decorations = new HashMap<Integer, Item>();
			keys = new HashMap<Item, Integer>();
		}
		if (decorations.get(key)!=null) {
			getDecorationSupport().removeItem(decorations.get(key));
		}
		decorations.put(key, decoration);
		keys.put(decoration, key);
		boolean done = false;
		for (int index=1; index<getDecorationSupport().getItemCount(); index++) {
			if (keys.get(getDecorationSupport().getItem(index))>key) {
				getDecorationSupport().addItem(index, decoration);
				decoration.setLocation(location);
				done = true;
				break;
			}
		}
		if (!done) {
			getDecorationSupport().addItem(decoration);
		}
	}
	
	public void removeDecoration(Integer key) {
		if (decorations != null) {
			Item decoration = decorations.get(key);
			if (decoration != null) {
				decorations.remove(key);
				keys.remove(decoration);
				if (decorations.isEmpty()) {
					decorations = null;
					keys = null;
				}
				getDecorationSupport().removeItem(decoration);
			}
		}
	}
}
