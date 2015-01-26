package com.ithaque.funnies.client.platform.gwt.impl;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.FillStrokeStyle;
import com.ithaque.funnies.client.platform.gwt.Context2D;
import com.ithaque.funnies.client.platform.gwt.ImageInterface;
import com.ithaque.funnies.shared.basic.Color;

public class GWTContext2D implements Context2D {

	Context2d context2d;
	
	public GWTContext2D(Context2d context2d) {
		this.context2d = context2d;
	}

	@Override
	public void setTransform(float m11, float m12, float m21, float m22, float dx, float dy) {
		context2d.setTransform(m11, m12, m21, m22, dx, dy);
	}

	@Override
	public void setGlobalAlpha(float opacity) {
		context2d.setGlobalAlpha(opacity);
	}

	@Override
	public void translate(float x, float y) {
		context2d.translate(x, y);
	}

	@Override
	public void drawImage(
			ImageInterface image, int sx, int sy, int sw, int sh, 
			int dx, int dy, int dw, int dh) 
	{
		context2d.drawImage(((GWTImage)image).image, sx, sy, sw, sh, dx, dy, dw, dh);
	}

	@Override
	public void setLineWidth(float lineWidth) {
		context2d.setLineWidth(lineWidth);
	}

	@Override
	public void beginPath() {
		context2d.beginPath();		
	}
	
	@Override
	public void setLineJoin(String lineJoin) {
		context2d.setLineJoin(lineJoin);
	}

	@Override
	public void setStrokeStyle(Color color) {
		context2d.setStrokeStyle(getColor(color));
	}

	@Override
	public void setFillStyle(Color color) {
		context2d.setFillStyle(getColor(color));
	}

	FillStrokeStyle getColor(Color color) {
		return CssColor.make(color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public void fill() {
		context2d.fill();
	}

	@Override
	public void stroke() {
		context2d.stroke();
	}

	@Override
	public void moveTo(float x, float y) {
		context2d.moveTo(x, y);
	}

	@Override
	public void lineTo(float x, float y) {
		context2d.lineTo(x, y);
	}

	@Override
	public void clip() {
		context2d.clip();
	}

	@Override
	public void setFont(String font) {
		context2d.setFont(font);
	}

	@Override
	public void setTextBaseline(String baseline) {
		context2d.setTextBaseline(baseline);
	}

	@Override
	public void fillText(String text, float x, float y) {
		context2d.fillText(text, x, y);
	}

	@Override
	public double measureTextWidth(String text) {
		return context2d.measureText(text).getWidth();
	}

	@Override
	public void clearRect(float x, float y, float w, float h) {
		context2d.clearRect(x, y, w, h);
	}

}
