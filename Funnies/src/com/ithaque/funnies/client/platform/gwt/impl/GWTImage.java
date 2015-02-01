package com.ithaque.funnies.client.platform.gwt.impl;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.ithaque.funnies.client.platform.gwt.GraphicsImpl;
import com.ithaque.funnies.client.platform.gwt.GraphicsImpl.ImageElementRecord;
import com.ithaque.funnies.client.platform.gwt.ImageInterface;

public class GWTImage implements ImageInterface {

	ImageElement image;

	public GWTImage(final String url, final GraphicsImpl graphics, final ImageElementRecord record) {
		final Image img = new Image(url);
	    image = ImageElement.as(img.getElement());
	    img.addLoadHandler(new LoadHandler() {
	        @Override
	        public void onLoad(LoadEvent event) {
	        	loaded(url, graphics, record, img);
	        }

	    });
	    img.setVisible(false);
	    RootPanel.get("images").add(img);
	}

	protected void loaded(final String url, final GraphicsImpl graphics,
			final ImageElementRecord record, final Image img) {
		graphics.drawPendingImages(url, record);
    	RootPanel.get("images").remove(img);
	}

	ImageElement getImage() {
		return image;
	}

	@Override
	public float getWidth() {
		return image.getWidth();
	}
	
	@Override
	public float getHeight() {
		return image.getHeight();
	}
}
