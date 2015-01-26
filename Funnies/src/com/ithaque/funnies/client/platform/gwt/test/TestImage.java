package com.ithaque.funnies.client.platform.gwt.test;

import com.ithaque.funnies.client.platform.gwt.GWTGraphics;
import com.ithaque.funnies.client.platform.gwt.GWTGraphics.ImageElementRecord;
import com.ithaque.funnies.client.platform.gwt.ImageInterface;

public class TestImage implements ImageInterface {

	float width;
	float height;
	GWTGraphics graphics;
	String url;
	ImageElementRecord record;
	
	public TestImage(final String url, final GWTGraphics graphics, final ImageElementRecord record) {
		TestRegistry.addCall("Image", url, "Image");
		this.url = url;
		this.graphics = graphics;
		this.record = record;
	}

	public void loaded(float width, float height) {
    	this.graphics.drawPendingImages(url, record);
		this.width = width;
		this.height = height;
	}
	
	@Override
	public float getWidth() {
		TestRegistry.addCall("Image", url, "getWidth");
		return this.width;
	}
	
	@Override
	public float getHeight() {
		TestRegistry.addCall("Image", url, "getHeight");
		return this.height;
	}
}
