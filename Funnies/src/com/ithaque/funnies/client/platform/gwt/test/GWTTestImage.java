package com.ithaque.funnies.client.platform.gwt.test;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.ithaque.funnies.client.platform.gwt.GWTGraphics;
import com.ithaque.funnies.client.platform.gwt.GWTGraphics.ImageElementRecord;
import com.ithaque.funnies.client.platform.gwt.impl.GWTImage;

public class GWTTestImage extends GWTImage {

	TestImage image;
	
	public GWTTestImage(final String url, final GWTGraphics graphics, final ImageElementRecord record) {
		super(url, graphics, record);
		image = new TestImage(url, graphics, record);
	}
	
	protected void loaded(final String url, final GWTGraphics graphics,
			final ImageElementRecord record, final Image img) {
		image.loaded(img.getWidth(), img.getHeight());
    	RootPanel.get("images").remove(img);
	}

	@Override
	public float getWidth() {
		float result = image.getWidth();
		image.getWidth();
		return result;
	}
	
	@Override
	public float getHeight() {
		float result = image.getHeight();
		image.getHeight();
		return result;
	}
}
