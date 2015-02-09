package com.ithaque.funnies.shared.basic.items;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.Location;
import com.ithaque.funnies.shared.basic.GroupItem;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemHolder;

public class BaseItem extends GroupItem {

	StackItem stack = null;

	public BaseItem() {
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
	
	public Location getTargetLocation(Item content) {
		return stack!=null && stack!=content ? stack.getTargetLocation(content) : Location.ORIGIN;
	}

	public ItemHolder getStackTop() {
		return stack==null ? this : stack.getStackTop();
	}
}
