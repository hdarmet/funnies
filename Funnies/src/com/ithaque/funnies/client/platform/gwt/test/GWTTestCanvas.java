package com.ithaque.funnies.client.platform.gwt.test;

import com.google.gwt.canvas.client.Canvas;
import com.ithaque.funnies.client.platform.gwt.Context2D;
import com.ithaque.funnies.client.platform.gwt.impl.GWTCanvas;

public class GWTTestCanvas extends GWTCanvas {

	TestCanvas canvas;
	TestContext2D context2D;
	
	public GWTTestCanvas(Canvas canvas, String id) {
		super(canvas);
		this.canvas = new TestCanvas(id);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		canvas.setVisible(visible);
	}

	@Override
	public void setAttribute(String key, String value) {
		super.setAttribute(key, value);
		canvas.setAttribute(key, value);
	}

	@Override
	public void setCoordinateSpaceWidth(int width) {
		super.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceWidth(width);
	}

	@Override
	public void setCoordinateSpaceHeight(int height) {
		super.setCoordinateSpaceHeight(height);
		canvas.setCoordinateSpaceHeight(height);
	}

	@Override
	public void saveContext2D() {
		super.saveContext2D();
		canvas.saveContext2D();
	}

	@Override
	public void restoreContext2D() {
		super.restoreContext2D();
		canvas.restoreContext2D();
	}
	
	@Override
	public void addToRootPanel() {
		super.addToRootPanel();
	    canvas.addToRootPanel();
	}

	@Override
	public float getCoordinateSpaceWidth() {
		float result = super.getCoordinateSpaceWidth();
		canvas.getCoordinateSpaceWidth();
		return result;
	}

	@Override
	public float getCoordinateSpaceHeight() {
		float result = super.getCoordinateSpaceHeight();
		canvas.getCoordinateSpaceHeight();
		return result;
	}

	@Override
	public Context2D getContext2d() {
		Context2D result = super.getContext2d(); 
		canvas.getContext2d();
		return result;
	}

}
