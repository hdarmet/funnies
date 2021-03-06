package com.ithaque.funnies.shared.basic.items;

import com.ithaque.funnies.shared.Shape;
import com.ithaque.funnies.shared.basic.Color;
import com.ithaque.funnies.shared.basic.Font;
import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemObserver.ChangeType;

public class TextItem extends Item {

	String text;
	Color color; 
	Font font;

	public TextItem(String text, Color color, Font font) {
		this.text = text;
		this.color = color;
		this.font = font;
		dirty();
	}
	
	public void setText(String text) {
		this.text = text;
		fire(ChangeType.SHAPE);
		dirty();
	}

	public void setTextStyle(Color color, Font font) {
		this.color = color;
		this.font = font;
		dirty();
	}

	@Override
	public void render(Graphics graphics) {
		graphics.drawText(this, text, color, font, getDisplayOpacity());
	}
	
	@Override
	public Shape getShape() {
		float width = Graphics.Singleton.getGraphics().getTextWidth(font, text);
		float height = Graphics.Singleton.getGraphics().getTextHeight(font, text);
		return new Shape(width, height);
	}
}
