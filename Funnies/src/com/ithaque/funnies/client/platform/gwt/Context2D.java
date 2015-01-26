package com.ithaque.funnies.client.platform.gwt;

import com.ithaque.funnies.shared.basic.Color;

public interface Context2D {

	void setTransform(float m11, float m12, float m21, float m22, float dx, float dy);

	void setGlobalAlpha(float opacity);
	
	void translate(float x, float y);
	
	void drawImage(
			ImageInterface image, 
			int sx, int sy, int sw, int sh, 
			int dx, int dy, int dw, int dh);
	
	void setLineWidth(float lineWidth);
	
	void setLineJoin(String lineJoin);
	
	void setStrokeStyle(Color color);
	
	void setFillStyle(Color color);
	
	void fill();
	
	void stroke();
	
	void beginPath();
	
	void moveTo(float x, float y);
	
	void lineTo(float x, float y);
	
	void clip();
	
	void setFont(String font);
	
	void setTextBaseline(String baseline);
	
	void fillText(String text, float x, float y);
	
	double measureTextWidth(String text);

	void clearRect(float x, float y, float w, float h);

}
