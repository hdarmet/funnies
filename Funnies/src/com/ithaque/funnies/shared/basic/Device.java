package com.ithaque.funnies.shared.basic;

import java.util.Collection;

public interface Device extends ItemHolder {
	public Item getMouseTarget(MouseEvent event);

	public Collection<Item> getMouseTargets(MouseEvent event);
}
