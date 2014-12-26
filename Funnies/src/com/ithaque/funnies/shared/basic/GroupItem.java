package com.ithaque.funnies.shared.basic;

import java.util.ArrayList;
import java.util.List;

public class GroupItem extends Item implements ItemHolder {

	List<Item> items = new ArrayList<Item>();
	
	@Override
	public int prepare() {
		int maxLevel=0;
		for (Item item : items) {
			int highestLevel = item.prepare();
			if (highestLevel>maxLevel) {
				maxLevel = highestLevel;
			}
		}
		super.prepare();
		return maxLevel+1;
	}
	
	public void render(Graphics graphics, int currentLevel, int level) {
		if (currentLevel<=level) {
			super.render(graphics, currentLevel, level);
			for (Item item : items) {
				renderItem(graphics, item, currentLevel, level);
			}
		}
	}

	public void unsetDirty() {
		super.unsetDirty();
		for (Item item : items) {
			item.unsetDirty();
		}
	}

	protected void renderItem(Graphics graphics, Item item, int currentLevel, int level) {
		item.render(graphics, currentLevel+1, level);
	}
	
	@Override
	public void addItem(Item item) {
		if (items.isEmpty() || items.get(items.size()-1)!=item) {
			item.free();
			item.setParent(this);
			items.add(item);
			dirty();
		}
	}
	
	@Override
	public void setItem(int index, Item item) {
		if (items.isEmpty() || items.get(index)!=item) {
			item.free();
			item.setParent(this);
			items.set(index, item);
			dirty();
		}
	}

	@Override
	public void removeItem(Item item) {
		if (item.getParent()==this) {
			items.remove(item);
			item.removeFromParent();
			dirty();
		}
	}

	@Override
	public boolean contains(Item item) {
		return items.contains(item);
	}
	
	@Override
	public Item getItem(int index) {
		return items.get(index);
	}
	
	@Override
	public int getItemCount() {
		return items.size();
	}

	@Override
	protected void registerOnBoard(Board newBoard) {
		super.registerOnBoard(newBoard);
		for (Item item : items) {
			item.registerOnBoard(newBoard);
		}
	}

	@Override
	protected void unregisterOnBoard(Board oldBoard) {
		super.registerOnBoard(oldBoard);
		for (Item item : items) {
			item.registerOnBoard(oldBoard);
		}
	}
	
	@Override
	public int indexOfItem(Item item) {
		return items.indexOf(item);
	}
	
}
