package com.ithaque.funnies.shared.basic.items;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.Transform;
import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.GroupItem;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemHolder;
import com.ithaque.funnies.shared.basic.Location;

public class StackItem extends GroupItem {
	public static final float MARGIN = 8.0f;

	Item baseItem;
	StackItem stack = null;
	
	public StackItem(Item baseItem) {
		this.baseItem = baseItem;
		super.addItem(baseItem);
	}

	@Override
	public int prepare() {
		return super.prepare() + (stack==null?0:1);
	}
	
	protected void renderItem(Graphics graphics, Item item, int currentLevel, int level) {
		baseItem.render(graphics, currentLevel, level);
		if (stack!=null) {
			stack.render(graphics, currentLevel+1, level);
		}
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
		return stack!=null && stack!=content  ? stack.getTargetLocation(content) : computeStackLocation(this);
	}

	@Override
	public Location getLocation() {
		if (getParent()!=null && getParent() instanceof StackItem) {
			return computeStackLocation(getParent());
		}
		else {
			return super.getLocation();
		}
	}

	public Location computeStackLocation(ItemHolder parent) {
		Location location = new Location(MARGIN, -MARGIN);
		float angle = parent.getAbsoluteRotation(); 
		if (angle!=0.0) {
			location = new Transform().rotate(-angle).transformPoint(location);
		}
		return location;
	}

	public ItemHolder getStackTop() {
		return stack==null ? this : stack.getStackTop();
	}
	
}
