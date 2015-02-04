package com.ithaque.funnies.shared.funny;

import java.util.HashMap;
import java.util.Map;

import com.ithaque.funnies.shared.basic.Item;

public class FunnyRegistry<F extends Funny> implements FunnyObserver {
	
	int currentVersion = 0;
	
	public interface ItemsFinder<F extends Funny> {
		Item[] getItems(F funny);
	}
	
	public static class Record<F> {
		
		public Record(F funny, Item item) {
			this.funny = funny;
			this.item = item;
		}
		
		Item item;
		F funny;
	}

	Map<F, Item[]> funnyToItems = new HashMap<F, Item[]>();
	Map<Item, Record<F>> itemToFunnies = new HashMap<Item, Record<F>>();
	ItemsFinder<F> finder;
	
	public FunnyRegistry(ItemsFinder<F> finder) {
		this.finder = finder;
	}
	
	public void registerFunny(F funny) {
		Map<Item, Record<F>> itemToFunnies = new HashMap<Item, Record<F>>();
		Item[] items = finder.getItems(funny);
		for (Item item : items) {
			Record<F> record = this.itemToFunnies.remove(item);
			if (record==null) {
				record = createRecord(item, funny);
			}
			itemToFunnies.put(item, record);
		}
		unregisterFunny(funny);
		funnyToItems.put(funny, items);
		this.itemToFunnies.putAll(itemToFunnies);
		funny.addObserver(this);
	}

	protected Record<F> createRecord(Item item, F funny) {
		return new Record<F>(funny, item);
	}

	public void unregisterFunny(F funny) {
		Item[] items = funnyToItems.remove(funny);
		if (items != null) {
			for (Item item : items) {
				this.itemToFunnies.remove(item);
			}
			funny.removeObserver(this);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void change(ChangeType type, Funny funny) {
		if (type==ChangeType.REGISTERING) {
			registerFunny((F)funny);
		}
	}

	public Record<F> getRecord(Item hover) {
		return itemToFunnies.get(hover);
	}
	
}
