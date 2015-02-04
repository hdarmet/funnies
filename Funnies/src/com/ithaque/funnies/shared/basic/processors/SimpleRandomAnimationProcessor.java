package com.ithaque.funnies.shared.basic.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Item;

public class SimpleRandomAnimationProcessor extends RandomAnimationProcessor {

	List<Item> animatedItems = new ArrayList<Item>();
	Map<Item, Animation.Factory> factories = new HashMap<Item, Animation.Factory>();
	
	public SimpleRandomAnimationProcessor(Board board, long timeout, String alarmId, int segCount) {
		super(board, timeout, alarmId, segCount);
	}
	
	@Override
	protected List<Item> getAnimatedItems() {
		return animatedItems;
	}
	
	public void addAnimatedItem(Item item, Animation.Factory factory) {
		if (animatedItems.contains(item)) {
			animatedItems.add(item);
		}
		factories.put(item, factory);
	}
	
	public void removeAnimatedItem(Item item) {
		if (animatedItems.remove(item)) {
			factories.remove(item);
		}
	}
	
	protected Animation.Factory getAnimation(Item item) {
		return factories.get(item);
	}

}
