package com.ithaque.funnies.shared.basic;

import com.ithaque.funnies.shared.Shape;
import com.ithaque.funnies.shared.basic.items.AbstractImageItem;

public interface Graphics {
	
	Token loadImage(String url);

	void drawImage(AbstractImageItem imageItem);

	void drawPolygon(Item item, Color fillColor, Color lineColor, float lineWidth, float opacity);

	void drawText(Item item, String text, Color color, Font font, float opacity);
	
	void clip(Item item, Shape shape);

	void clear();

	float getTextWidth(Font font, String text);
	
	float getTextHeight(Font font, String text);
	
	Float getImageWidth(Token token);
	
	Float getImageHeight(Token token);
	
	float getDisplayWidth();

	float getDisplayHeight();

	Token createLayer();

	void setLayer(Token token);
	
	int compareLayers(Token layer1, Token layer2);

	void show();

	public class Singleton {
		
		static Graphics instance;
		
		static public void setGraphics(Graphics graphics) {
			instance = graphics;
		}
		
		static public Graphics getGraphics() {
			return instance;
		}
	}

}
