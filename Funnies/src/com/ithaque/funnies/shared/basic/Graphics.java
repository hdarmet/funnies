package com.ithaque.funnies.shared.basic;

import com.ithaque.funnies.shared.basic.items.AbstractImageItem;

public interface Graphics {
	
	Token loadImage(String url);

	void drawImage(AbstractImageItem imageItem);

	void clear();

	Float getImageWidth(Token token);
	
	Float getImageHeight(Token token);
	
	float getDisplayWidth();

	float getDisplayHeight();

	Token createLayer();

	void setLayer(Token token);

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
