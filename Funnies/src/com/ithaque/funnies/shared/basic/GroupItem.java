package com.ithaque.funnies.shared.basic;

import java.util.ArrayList;
import java.util.List;

public class GroupItem extends Item implements ItemHolder {

	List<Item> items = new ArrayList<Item>();
	
	public void prepare() {
		for (Item item : items) {
			item.prepare();
		}
		super.prepare();
	}
	
	public void render(Graphics graphics) {
		for (Item item : items) {
			renderItem(graphics, item);
		}
		super.render(graphics);
	}

	protected void renderItem(Graphics graphics, Item item) {
		item.render(graphics);
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
			item.setParent(null);
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
	protected void registerOnBoard(Board oldBoard, Board newBoard) {
		super.registerOnBoard(oldBoard, newBoard);
		for (Item item : items) {
			item.registerOnBoard(oldBoard, newBoard);
		}
	}
	
	@Override
	public int indexOfItem(Item item) {
		return items.indexOf(item);
	}
	
}
