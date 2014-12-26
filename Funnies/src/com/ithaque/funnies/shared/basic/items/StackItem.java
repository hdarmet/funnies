package com.ithaque.funnies.shared.basic.items;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.GroupItem;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;

public class StackItem extends GroupItem {
	public static final float MARGIN = 8.0f;

	Item baseItem;
	StackItem stack = null;
	
	public StackItem(Item baseItem) {
		this.baseItem = baseItem;
		super.addItem(baseItem);
	}

	public void addItem(Item item) {
		if (!(item instanceof StackItem)) {
			throw new IllegalInvokeException();
		}
		if (this.stack!=null) {
			stack.addItem(item);
		}
		else {
			this.stack = (StackItem)item;
			super.addItem(item);
		}
	}

	public void removeItem(Item item) {
		if (item == stack) {
			stack=null;
			super.removeItem(item);
		}
	}
	
	@Override
	public Location[] getShape() {
		return baseItem.getShape();
	}

	public Location getTargetLocation(Item content) {
		return stack!=null && stack!=content  ? stack.getTargetLocation(content) : new Location(MARGIN, -MARGIN);
	}
	
}
