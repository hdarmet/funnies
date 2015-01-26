package com.ithaque.funnies.client.platform.gwt.impl;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.RootPanel;
import com.ithaque.funnies.client.platform.gwt.CanvasInterface;
import com.ithaque.funnies.client.platform.gwt.Context2D;

public class GWTCanvas implements CanvasInterface {

	Canvas canvas;
	GWTContext2D context2D;
	
	public GWTCanvas(Canvas canvas) {
		this.canvas = canvas;
		this.context2D = new GWTContext2D(canvas.getContext2d());
	}

	@Override
	public void setVisible(boolean visible) {
		canvas.setVisible(visible);
	}

	@Override
	public void setAttribute(String key, String value) {
		canvas.getElement().setAttribute(key, value);
	}

	@Override
	public void setCoordinateSpaceWidth(int width) {
		canvas.setCoordinateSpaceWidth(width);
	}

	@Override
	public void setCoordinateSpaceHeight(int height) {
		canvas.setCoordinateSpaceHeight(height);
	}

	@Override
	public void saveContext2D() {
		canvas.getContext2d().save();
	}

	@Override
	public void restoreContext2D() {
		canvas.getContext2d().restore();
	}
	
	@Override
	public void addToRootPanel() {
	    RootPanel.get("board").add(canvas);
	}

	@Override
	public float getCoordinateSpaceWidth() {
		return canvas.getCoordinateSpaceWidth();
	}

	@Override
	public float getCoordinateSpaceHeight() {
		return canvas.getCoordinateSpaceHeight();
	}

	@Override
	public Context2D getContext2d() {
		return context2D;
	}

}
