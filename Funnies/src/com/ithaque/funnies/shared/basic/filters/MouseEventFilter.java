package com.ithaque.funnies.shared.basic.filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemFilter;
import com.ithaque.funnies.shared.basic.MouseEvent;

public class MouseEventFilter extends ItemFilter {

	MouseEvent event = null;
	
	public MouseEventFilter(ItemFilter filter, MouseEvent event) {
		super(filter);
		this.event = event;
	}

	public MouseEventFilter(MouseEvent event) {
		super();
		this.event = event;
	}

	@Override
	public Collection<Item> filter(Collection<Item> items) {
		List<Item> filteredItems = new ArrayList<Item>();
		for (Item item : items) {
			if (item.acceptEvent(event)) {
				filteredItems.add(item);
			}
		}
		return filteredItems;
	}

}
