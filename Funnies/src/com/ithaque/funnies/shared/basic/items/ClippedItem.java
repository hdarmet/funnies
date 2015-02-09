package com.ithaque.funnies.shared.basic.items;

import com.ithaque.funnies.shared.Shape;
import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.GroupItem;

public class ClippedItem extends GroupItem {
	
	public void render(Graphics graphics, int currentLevel, int level) {
		Shape shape = getDisplayShape();
		graphics.clip(this, shape);
		super.render(graphics, currentLevel, level);
	}
}
