package com.ithaque.funnies.shared.basic.items;

import com.ithaque.funnies.shared.basic.Color;
import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;

public class PolygonItem extends Item {

	Color fillColor;
	Color lineColor; 
	float lineWidth;

	public PolygonItem(Color fillColor, Color lineColor, float lineWidth, float opacity, Location[] shape) {
		setShape(shape);
		setStyle(fillColor, lineColor, lineWidth, opacity);
	}
	
	public void setStyle(Color fillColor, Color lineColor, float lineWidth, float opacity) {
		this.fillColor = fillColor;
		this.lineColor = lineColor;
		this.lineWidth = lineWidth;
		setOpacity(opacity);
		dirty();
	}
	
	public PolygonItem(Color fillColor, Color lineColor, float lineWidth, float opacity, float ... shape) {
		this(fillColor, lineColor, lineWidth, opacity, buildShape(shape));
	}

	@Override
	public void render(Graphics graphics) {
		graphics.drawPolygon(this, fillColor, lineColor, lineWidth, getDisplayOpacity());
	}
	
}
