package com.ithaque.funnies.client.platform.gwt.test;

import java.util.HashMap;
import java.util.Map;

import com.ithaque.funnies.client.platform.gwt.GraphicsImpl;
import com.ithaque.funnies.client.platform.gwt.GraphicsImpl.ImageElementRecord;
import com.ithaque.funnies.client.platform.gwt.ImageInterface;

public class TestImage implements ImageInterface {

	float width;
	float height;
	GraphicsImpl graphics;
	String url;
	ImageElementRecord record;
	
	static Map<String, TestImage> images = new HashMap<String, TestImage>();
	
	public TestImage(final String url, final GraphicsImpl graphics, final ImageElementRecord record) {
		TestRegistry.addCall("Image", url, "Image");
		this.url = url;
		this.graphics = graphics;
		this.record = record;
		images.put(url, this);
	}

	public void loaded(float width, float height) {
		this.width = width;
		this.height = height;
    	this.graphics.drawPendingImages(url, record);
	}
	
	public String getUrl() {
		return url;
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
	
	public static TestImage getImage(String url) {
		return images.get(url);
	}
	
	public static void reset() {
		images.clear();
	}
}
