package com.ithaque.funnies.server;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.ithaque.funnies.client.platform.gwt.GraphicsImpl;
import com.ithaque.funnies.client.platform.gwt.GraphicsImpl.ImageElementRecord;
import com.ithaque.funnies.client.platform.gwt.ImageInterface;
import com.ithaque.funnies.shared.IllegalInvokeException;

public class Java2DImage implements ImageInterface {

	private static final String IMAGE_REP = "C:\\Developpement\\git\\Funnies\\war\\";
	VolatileImage image;
	
	public Java2DImage(GraphicsConfiguration configuration, final String url, final GraphicsImpl graphics, final ImageElementRecord record) {
		try {
		    BufferedImage bufImage = ImageIO.read(new File(IMAGE_REP+url));
		    image = configuration.createCompatibleVolatileImage(bufImage.getWidth(), bufImage.getHeight(), Transparency.TRANSLUCENT);
		    Graphics2D imgGraphics = (Graphics2D)image.getGraphics();
		    imgGraphics.setBackground(new Color(0, 0, 0, 0));
		    imgGraphics.clearRect(0, 0, bufImage.getWidth(), bufImage.getHeight());
		    imgGraphics.drawImage(bufImage, 0, 0, null);
		    graphics.drawPendingImages(url, record);
		} catch (IOException exception) {
			throw new IllegalInvokeException();
		}
	}

	@Override
	public float getWidth() {
		return image.getWidth();
	}

	@Override
	public float getHeight() {
		return image.getHeight();
	}

	public Image getImage() {
		return image;
	}

}
