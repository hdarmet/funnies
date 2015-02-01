package com.ithaque.funnies.client.platform.gwt.test;

import java.util.Date;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.Random;
import com.ithaque.funnies.client.platform.gwt.CanvasInterface;
import com.ithaque.funnies.client.platform.gwt.GraphicsImpl;
import com.ithaque.funnies.client.platform.gwt.GraphicsImpl.ImageElementRecord;
import com.ithaque.funnies.client.platform.gwt.ImageInterface;
import com.ithaque.funnies.client.platform.gwt.impl.GWTPlatform;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event;
import com.ithaque.funnies.shared.basic.Token;

public class GWTTestPlatform extends GWTPlatform {

	int frameNumber;
	int currentFrameNumber = 0;
	
	public GWTTestPlatform(int frameNumber) {
		this.frameNumber = frameNumber;
	}
	
	static int canvasCount = 0;
	
	@Override
	public long getTime() {
		TestRegistry.addCall("Platform", "0", "getTime");
		long result = new Date().getTime();
		return result;
	}

	@Override
	public Token start(Board board) {
		TestRegistry.addCall("Platform", "0", "startTimer");
	    return super.start(board);
	}
	
	@Override
	public boolean process(long time) 
	{
		if (frameNumber==currentFrameNumber) {
			TestRegistry.reset();
		}
		boolean dirty = super.process(time);
		if (frameNumber==currentFrameNumber) {
			TestRegistry.dump();
		}
		currentFrameNumber++;
		return dirty;
	};
	
	@Override
	public void sendEvent(Event event) {
		TestRegistry.addCall("Platform", "0", "sendEvent", event.getParams());
		super.sendEvent(event);
	}
	
	@Override
	public float randomize() {
		TestRegistry.addCall("Platform", "0", "randomize");
		return (float)Random.nextDouble();
	}

	@Override
	public CanvasInterface createCanvas(boolean visible) {
		TestRegistry.addCall("Platform", "0", "createCanvas");
	    GWTTestCanvas canvas = new GWTTestCanvas(Canvas.createIfSupported(), ""+canvasCount++);
	    initCanvas(canvas, visible);
		return canvas; 
	}

	@Override
	public ImageInterface createImage(final String url, final ImageElementRecord record) {
		return new GWTTestImage(url, (GraphicsImpl)getGraphics(), record);
	}
	

}
