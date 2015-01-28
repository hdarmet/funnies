package com.ithaque.funnies.shared.basic;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.ItemObserver.ChangeType;

public class WrapperItem extends Item implements ItemHolder {

	Item wrapped;
	
	public WrapperItem(Item wrapped) {
		addItem(wrapped);
	}
	
	@Override
	public int prepare() {
		super.prepare();
		if (wrapped!=null) {
			return wrapped.prepare();
		}
		return 0;
	}
	
	public void render(Graphics graphics, int currentLevel, int level) {
		super.render(graphics, currentLevel, level);
		if (wrapped!=null) {
			if (currentLevel<=level) {
				wrapped.render(graphics, currentLevel, level);
			}
		}
	}

	public void unsetDirty() {
		super.unsetDirty();
		if (wrapped!=null) {
			wrapped.unsetDirty();
		}
	}

	public void fire(ChangeType changeType) {
		super.fire(changeType);
		if (wrapped!=null) {
			if (changeType==ChangeType.PARENT || changeType==ChangeType.ANCESTOR) {
				wrapped.fire(ChangeType.ANCESTOR);
			}
			else {
				wrapped.fire(changeType);
			}
		}
	}

	@Override
	public void registerOnLayout(LayoutDevice newLayout) {
		super.registerOnLayout(newLayout);
		if (wrapped!=null) {
			wrapped.registerOnLayout(newLayout);
		}
	}

	@Override
	public void unregisterOnLayout(LayoutDevice oldLayout) {
		super.unregisterOnLayout(oldLayout);
		if (wrapped!=null) {
			wrapped.unregisterOnLayout(oldLayout);
		}
	}
	
	@Override
	public Location[] getShape() {
		return wrapped==null ? null : wrapped.getShape();
	}

	@Override
	public void addItem(Item item) {
		if (item.getParent()!=null) {
			throw new AlreadyAttachedItemException();
		}
		if (wrapped!=null) {
			throw new IllegalInvokeException();
		}
		wrapped = item;
		wrapped.doSetParent(this);
		dirty();
	}

	@Override
	public int getItemCount() {
		return wrapped==null ? 0 : 1;
	}

	@Override
	public void removeItem(Item item) {
		if (item.getParent()!=this) {
			throw new ItemNotAttachedException();
		}
		wrapped=null;
		item.removeFromParent();
		dirty();
	}

	public void setWrapped(Item item) {
		addItem(item);
	}
	
	public Item getWrapped() {
		return wrapped;
	}
	
	@Override
	public boolean contains(Item item) {
		return wrapped == item;
	}

	@Override
	public int indexOfItem(Item item) {
		return wrapped == item ? 0 : -1;
	}
}
