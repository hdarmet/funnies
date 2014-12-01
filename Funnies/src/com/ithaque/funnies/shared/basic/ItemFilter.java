package com.ithaque.funnies.shared.basic;

import java.util.Collection;
import java.util.Iterator;

public abstract class ItemFilter {

	ItemFilter nextFilter = null;
	
	public ItemFilter() {	
	}
	
	public ItemFilter(ItemFilter next) {
		this.nextFilter = next;
	}
	
	public Collection<Item> select(Collection<Item> items) {
		return nextFilter == null ? filter(items) : filter(nextFilter.select(items));
	}
	
	public abstract Collection<Item> filter(Collection<Item> items);
	
	public Item pickItem(Collection<Item> items) {
		Iterator<Item> it = select(items).iterator();
		return it.hasNext() ? it.next() : null;
	}
}
