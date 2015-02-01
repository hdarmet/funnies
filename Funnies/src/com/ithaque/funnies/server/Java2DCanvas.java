package com.ithaque.funnies.server;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.VolatileImage;

import com.ithaque.funnies.client.platform.gwt.CanvasInterface;
import com.ithaque.funnies.client.platform.gwt.Context2D;

public class Java2DCanvas implements CanvasInterface {

	Java2DPlatform platform;
	Java2DContext2D context = new Java2DContext2D(this);
	VolatileImage image;
	boolean visible = false;
	int width;
	int height;

	
	public Java2DCanvas(Java2DPlatform platform, boolean visible) {
		setVisible(visible);
		this.platform = platform;
	}

	@Override
	public float getCoordinateSpaceWidth() {
		return width;
	}

	@Override
	public float getCoordinateSpaceHeight() {
		return height;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public Context2D getContext2d() {
		return context;
	}

	@Override
	public void restoreContext2D() {
		if (context!=null) {
			context.renew();
		}
	}

	@Override
	public void saveContext2D() {
	}

	@Override
	public void addToRootPanel() {
		platform.addToJFrame(this);
	}

	@Override
	public void setAttribute(String key, String value) {
	}

	@Override
	public void setCoordinateSpaceWidth(int width) {
		this.width = width;
		this.platform.setFrameWidth(width);
		this.image = null;
	}

	@Override
	public void setCoordinateSpaceHeight(int height) {
		this.height = height;
		this.platform.setFrameHeight(height);
		this.image = null;
	}

	public VolatileImage getImage() {
		if (image==null) {
		    image = platform.configuration.createCompatibleVolatileImage(width, height, Transparency.TRANSLUCENT);
		    Graphics2D imgGraphics = (Graphics2D)image.createGraphics();
		    imgGraphics.setBackground(new Color(0, 0, 0, 0));
		    imgGraphics.clearRect(0, 0, width, height);
		}
		return image;
	}
}
