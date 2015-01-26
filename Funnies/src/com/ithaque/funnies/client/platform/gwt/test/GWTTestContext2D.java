package com.ithaque.funnies.client.platform.gwt.test;

import com.google.gwt.canvas.dom.client.Context2d;
import com.ithaque.funnies.client.platform.gwt.ImageInterface;
import com.ithaque.funnies.client.platform.gwt.impl.GWTContext2D;
import com.ithaque.funnies.shared.basic.Color;

public class GWTTestContext2D extends GWTContext2D {

	TestContext2D context2D;
	
	public GWTTestContext2D(Context2d context2d, String id) {
		super(context2d);
		context2D = new TestContext2D(id);
	}

	@Override
	public void setTransform(float m11, float m12, float m21, float m22, float dx, float dy) {
		super.setTransform(m11, m12, m21, m22, dx, dy);
		context2D.setTransform(m11, m12, m21, m22, dx, dy);
	}

	@Override
	public void setGlobalAlpha(float opacity) {
		super.setGlobalAlpha(opacity);
		context2D.setGlobalAlpha(opacity);
	}

	@Override
	public void translate(float x, float y) {
		super.translate(x, y);
		context2D.translate(x, y);
	}

	@Override
	public void drawImage(
			ImageInterface image, int sx, int sy, int sw, int sh, 
			int dx, int dy, int dw, int dh) 
	{
		super.drawImage(image, sx, sy, sw, sh, dx, dy, dw, dh);
		context2D.drawImage(image, sx, sy, sw, sh, dx, dy, dw, dh);
	}

	@Override
	public void setLineWidth(float lineWidth) {
		super.setLineWidth(lineWidth);
		context2D.setLineWidth(lineWidth);
	}

	@Override
	public void beginPath() {
		super.beginPath();
		context2D.beginPath();		
	}
	
	@Override
	public void setLineJoin(String lineJoin) {
		super.setLineJoin(lineJoin);
		context2D.setLineJoin(lineJoin);
	}

	@Override
	public void setStrokeStyle(Color color) {
		super.setStrokeStyle(color);
		context2D.setStrokeStyle(color);
	}

	@Override
	public void setFillStyle(Color color) {
		super.setFillStyle(color);
		context2D.setFillStyle(color);
	}

	@Override
	public void fill() {
		super.fill();
		context2D.fill();
	}

	@Override
	public void stroke() {
		super.stroke();
		context2D.stroke();
	}

	@Override
	public void moveTo(float x, float y) {
		super.moveTo(x, y);
		context2D.moveTo(x, y);
	}

	@Override
	public void lineTo(float x, float y) {
		super.lineTo(x, y);
		context2D.lineTo(x, y);
	}

	@Override
	public void clip() {
		super.clip();
		context2D.clip();
	}

	@Override
	public void setFont(String font) {
		super.setFont(font);
		context2D.setFont(font);
	}

	@Override
	public void setTextBaseline(String baseline) {
		super.setTextBaseline(baseline);
		context2D.setTextBaseline(baseline);
	}

	@Override
	public void fillText(String text, float x, float y) {
		super.fillText(text, x, y);
		context2D.fillText(text, x, y);
	}

	@Override
	public double measureTextWidth(String text) {
		double result = super.measureTextWidth(text);
		context2D.addTextMeasures(result);
		context2D.measureTextWidth(text);
		return result;
	}

	@Override
	public void clearRect(float x, float y, float w, float h) {
		super.clearRect(x, y, w, h);
		context2D.clearRect(x, y, w, h);
	}

}
