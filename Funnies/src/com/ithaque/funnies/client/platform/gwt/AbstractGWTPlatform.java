package com.ithaque.funnies.client.platform.gwt;

import com.ithaque.funnies.client.platform.gwt.GraphicsImpl.ImageElementRecord;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event;
import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.Platform;
import com.ithaque.funnies.shared.basic.Token;

public abstract class AbstractGWTPlatform implements Platform {

	GraphicsImpl graphics = new GraphicsImpl(this);
	Board board;
	
	@Override
	public Graphics getGraphics() {
		return graphics;
	}
	
	@Override
	public Token start(Board board) {
		Token token = graphics.start();
		this.board = board;
	    return token;
	}

	public boolean process(long time) {
    	board.alarm(time);
    	board.animate(time);
    	boolean dirty = board.isDirty();
    	board.render();
    	return dirty;
	}
	
	public void sendEvent(Event event) {
		board.processEvent(event);
	}

	@Override
	public boolean isReady() {
		return graphics.isReady();
	}

	protected void initCanvas(CanvasInterface canvas, boolean visible) {
	    canvas.setAttribute("style", "position:absolute;left:0px;top:0px;");
	    canvas.setVisible(visible);
	    canvas.setCoordinateSpaceWidth(1000);
	    canvas.setCoordinateSpaceHeight(500);
	    canvas.saveContext2D();
	    canvas.addToRootPanel();
	}

	public abstract CanvasInterface createCanvas(boolean visible);

	public abstract ImageInterface createImage(final String url, final ImageElementRecord record);

}
