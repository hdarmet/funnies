package com.ithaque.funnies.shared.basic.items;

import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.GroupItem;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemObserver.ChangeType;
import com.ithaque.funnies.shared.basic.Location;

public class DecoratedItem extends GroupItem {

	Item baseItem;
	
	public DecoratedItem(Item baseItem) {
		this.baseItem = baseItem;
		this.baseItem.setParent(this);
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
	public void registerOnBoard(Board newBoard) {
		baseItem.registerOnBoard(newBoard);
		super.registerOnBoard(newBoard);
	}

	@Override
	public void unregisterOnBoard(Board oldBoard) {
		baseItem.unregisterOnBoard(oldBoard);
		super.unregisterOnBoard(oldBoard);
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
	public Location[] getShape() {
		return baseItem.getShape();
	}
}