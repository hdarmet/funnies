package com.ithaque.funnies.shared.basic;

import com.ithaque.funnies.shared.basic.items.AbstractImageItem;

public interface Graphics {
	
	Token loadImage(String url);

	void drawImage(AbstractImageItem imageItem);

	void drawPolygon(Item item, Color fillColor, Color lineColor, float lineWidth, float opacity);

	void clear();

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
