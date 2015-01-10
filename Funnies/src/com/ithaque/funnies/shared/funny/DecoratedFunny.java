package com.ithaque.funnies.shared.funny;

import java.util.HashMap;
import java.util.Map;

import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.items.DecoratedItem;

public abstract class DecoratedFunny extends AbstractFunny {

	Map<Integer, Item> decorations = null;
	Map<Item, Integer> keys = null;
	
	public DecoratedFunny(String id) {
		super(id);
	}

	public abstract DecoratedItem getDecorationSupport();
	
	public Item getDecoration(Integer key) {
		return decorations.get(key);
	}
	
	public void putDecoration(Integer key, Item decoration, Location location) {
		if (decorations==null) {
			decorations = new HashMap<Integer, Item>();
			keys = new HashMap<Item, Integer>();
		}
		Item previousDecoration = decorations.get(key);
		if (previousDecoration!=null) {
			getDecorationSupport().removeItem(previousDecoration);
			keys.remove(previousDecoration);
		}
		decorations.put(key, decoration);
		keys.put(decoration, key);
		boolean done = false;
		for (int index=0; index<getDecorationSupport().getItemCount(); index++) {
			if (keys.get(getDecorationSupport().getItem(index))>key) {
				getDecorationSupport().addItem(index, decoration);
				done = true;
				break;
			}
		}
		if (!done) {
			getDecorationSupport().addItem(decoration);
		}
		decoration.setLocation(location);
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
