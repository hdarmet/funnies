package com.ithaque.funnies.shared.basic.items;

import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;

public class ZoneItem extends DecoratedItem {

	public ZoneItem(Item baseItem) {
		super(baseItem);
	}

	public void render(Graphics graphics, int currentLevel, int level) {
		Location[] shape = getDisplayShape();
		graphics.clip(this, shape);
		super.render(graphics, currentLevel, level);
	}

}
