package com.ithaque.funnies.shared.basic.items;

import com.ithaque.funnies.shared.Shape;
import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.GroupItem;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemObserver.ChangeType;
import com.ithaque.funnies.shared.basic.LayoutDevice;

public class DecoratedItem extends GroupItem {

	Item baseItem;
	
	public DecoratedItem(Item baseItem) {
		this.baseItem = baseItem;
		this.baseItem.doSetParent(this);
	}
	
	@Override
	public int prepare() {
		int baseLevel = baseItem.prepare();
		int itemsLevel = super.prepare();
		return baseLevel>itemsLevel ? baseLevel : itemsLevel;
	}
	
	public void render(Graphics graphics, int currentLevel, int level) {
		baseItem.render(graphics, currentLevel, level);
		super.render(graphics, currentLevel, level);
	}

	public void unsetDirty() {
		super.unsetDirty();
		baseItem.unsetDirty();
	}
	
	public void fire(ChangeType changeType) {
		if (changeType==ChangeType.PARENT || changeType==ChangeType.ANCESTOR) {
			baseItem.fire(ChangeType.ANCESTOR);
		}
		super.fire(changeType);
	}
	
	@Override
	public void registerOnLayout(LayoutDevice newLayout) {
		baseItem.registerOnLayout(newLayout);
		super.registerOnLayout(newLayout);
	}

	@Override
	public void unregisterOnLayout(LayoutDevice oldLayout) {
		baseItem.unregisterOnLayout(oldLayout);
		super.unregisterOnLayout(oldLayout);
	}
	
	@Override
	public int indexOfItem(Item item) {
		if (item==baseItem) {
			return 0;
		}
		else {
			return super.indexOfItem(item)+1;
		}
	}

	@Override
	public Shape getShape() {
		return baseItem.getShape();
	}

	public void addItem(Item item, float x, float y) {
		item.setLocation(x, y);
		super.addItem(item);
	}
}
