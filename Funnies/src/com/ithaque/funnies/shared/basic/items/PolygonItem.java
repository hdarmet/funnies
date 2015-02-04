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
		float displayOpacity = getDisplayOpacity();
		if (displayOpacity>0.0f) {
			graphics.drawPolygon(this, fillColor, lineColor, lineWidth, displayOpacity);
		}
	}
	
	public static PolygonItem createRect(Color fillColor, Color lineColor, float lineWidth, float opacity, float width, float height) {
		Location[] shape = new Location[] {
			new Location(-width/2.0f, -height/2.0f), 
			new Location(width/2.0f, -height/2.0f), 
			new Location(width/2.0f, height/2.0f), 
			new Location(-width/2.0f, height/2.0f)};
		return new PolygonItem(fillColor, lineColor, lineWidth, opacity, shape);
	}
}
