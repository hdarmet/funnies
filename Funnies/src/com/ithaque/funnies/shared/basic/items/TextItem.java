package com.ithaque.funnies.shared.basic.items;

import com.ithaque.funnies.shared.basic.Color;
import com.ithaque.funnies.shared.basic.Font;
import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemObserver.ChangeType;
import com.ithaque.funnies.shared.basic.Location;

public class TextItem extends Item {

	String message;
	Color color; 
	Font font;

	public TextItem(String message, Color color, Font font) {
		this.message = message;
		this.color = color;
		this.font = font;
		fire(ChangeType.SHAPE);
		dirty();
	}
	
	@Override
	public void render(Graphics graphics) {
		graphics.drawText(this, message, color, font, getDisplayOpacity());
	}
	
	@Override
	public Location[] getShape() {
		float width = Graphics.Singleton.getGraphics().getTextWidth(font, message);
		float height = Graphics.Singleton.getGraphics().getTextHeight(font, message);
		return new Location[] {
			new Location(-width/2.0f, -height/2.0f), 
			new Location(width/2.0f, -height/2.0f),
			new Location(width/2.0f, height/2.0f), 
			new Location(-width/2.0f, height/2.0f)
		};
	}
}
