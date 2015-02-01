package com.ithaque.funnies.client.platform.gwt.impl;

import java.util.Date;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.ithaque.funnies.client.platform.gwt.AbstractGWTPlatform;
import com.ithaque.funnies.client.platform.gwt.CanvasInterface;
import com.ithaque.funnies.client.platform.gwt.GraphicsImpl;
import com.ithaque.funnies.client.platform.gwt.GraphicsImpl.ImageElementRecord;
import com.ithaque.funnies.client.platform.gwt.ImageInterface;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Token;

public class GWTPlatform extends AbstractGWTPlatform {

	GWTEventManager eventManager = new GWTEventManager(this);

	@Override
	public long getTime() {
		return new Date().getTime();
	}
	
	@Override
	public Token start(Board board) {
	    Timer timer = new Timer() {
	        @Override
	        public void run() {
	        	process(getTime());
	        }
	    };
	    timer.scheduleRepeating((int)Animation.INTERVAL);
	    return super.start(board);
	}

	@Override
	public float randomize() {
		return (float)Random.nextDouble();
	}

	@Override
	public CanvasInterface createCanvas(boolean visible) {
	    GWTCanvas canvas = new GWTCanvas(Canvas.createIfSupported());
	    initCanvas(canvas, visible);
		return canvas; 
	}

	@Override
	public ImageInterface createImage(final String url, final ImageElementRecord record) {
		return new GWTImage(url, (GraphicsImpl)getGraphics(), record);
	}
	

}
