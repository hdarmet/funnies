package com.ithaque.funnies.client.platform.gwt.test;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.client.platform.gwt.Context2D;
import com.ithaque.funnies.client.platform.gwt.ImageInterface;
import com.ithaque.funnies.shared.basic.Color;

public class TestContext2D implements Context2D {

	static List<Double> textWidthMeasures = new ArrayList<Double>();
	
	String id;
	
	public TestContext2D(String id) {
		this.id = id;
	}

	@Override
	public void setTransform(float m11, float m12, float m21, float m22, float dx, float dy) {
		TestRegistry.addCall("Context2D", id, "setTransform", ""+m11, ""+m12, ""+m21, ""+m22, ""+dx, ""+dy);
	}

	@Override
	public void setGlobalAlpha(float opacity) {
		TestRegistry.addCall("Context2D", id, "setGlobalAlpha", ""+opacity);
	}

	@Override
	public void translate(float x, float y) {
		TestRegistry.addCall("Context2D", id, "translate", ""+x, ""+y);
	}

	@Override
	public void drawImage(
			ImageInterface image, 
			int sx, int sy, int sw, int sh, 
			int dx, int dy, int dw, int dh) 
	{
		TestRegistry.addCall("Context2D", id, "drawImage", ((TestImage)image).getUrl(), ""+sx, ""+sy, ""+sw, ""+sh, ""+dx, ""+dy, ""+dw, ""+dh);
	}

	@Override
	public void setLineWidth(float lineWidth) {
		TestRegistry.addCall("Context2D", id, "setLineWidth", ""+lineWidth);
	}

	@Override
	public void beginPath() {
		TestRegistry.addCall("Context2D", id, "beginPath");
	}
	
	@Override
	public void setLineJoin(String lineJoin) {
		TestRegistry.addCall("Context2D", id, "setLineJoin", ""+lineJoin);
	}

	@Override
	public void setStrokeStyle(Color color) {
		TestRegistry.addCall("Context2D", id, "setStrokeStyle", ""+color);
	}

	@Override
	public void setFillStyle(Color color) {
		TestRegistry.addCall("Context2D", id, "setFillStyle", ""+color);
	}

	@Override
	public void fill() {
		TestRegistry.addCall("Context2D", id, "fill");
	}

	@Override
	public void stroke() {
		TestRegistry.addCall("Context2D", id, "stroke");
	}

	@Override
	public void moveTo(float x, float y) {
		TestRegistry.addCall("Context2D", id, "moveTo", ""+x, ""+y);
	}

	@Override
	public void lineTo(float x, float y) {
		TestRegistry.addCall("Context2D", id, "lineTo", ""+x, ""+y);
	}

	@Override
	public void clip() {
		TestRegistry.addCall("Context2D", id, "clip");
	}

	@Override
	public void setFont(String font) {
		TestRegistry.addCall("Context2D", id, "setFont", ""+font);
	}

	@Override
	public void setTextBaseline(String baseline) {
		TestRegistry.addCall("Context2D", id, "setTextBaseline", ""+baseline);
	}

	@Override
	public void fillText(String text, float x, float y) {
		TestRegistry.addCall("Context2D", id, "fillText", ""+x, ""+y);
	}

	public void addTextMeasures(double measure) {
		textWidthMeasures.add(measure);
	}
	
	@Override
	public double measureTextWidth(String text) {
		TestRegistry.addCall("Context2D", id, "measureTextWidth", text);
		return textWidthMeasures.remove(0);
	}

	@Override
	public void clearRect(float x, float y, float w, float h) {
		TestRegistry.addCall("Context2D", id, "clearRect", ""+x, ""+y, ""+w, ""+h);
	}

}
