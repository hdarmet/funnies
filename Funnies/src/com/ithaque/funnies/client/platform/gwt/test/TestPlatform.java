package com.ithaque.funnies.client.platform.gwt.test;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.client.platform.gwt.AbstractPlatform;
import com.ithaque.funnies.client.platform.gwt.CanvasInterface;
import com.ithaque.funnies.client.platform.gwt.GraphicsImpl;
import com.ithaque.funnies.client.platform.gwt.GraphicsImpl.ImageElementRecord;
import com.ithaque.funnies.client.platform.gwt.ImageInterface;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.MouseEvent.Button;
import com.ithaque.funnies.shared.basic.Token;

public class TestPlatform extends AbstractPlatform {

	long now = 0;
	int canvasCount = 0;
	List<Float> randoms = new ArrayList<Float>();

	@Override
	public boolean process(long time) {
		now = time;
		return super.process(time);
	}
	
	@Override
	public long getTime() {
		TestRegistry.addCall("Platform", "0", "getTime");
		return now;
	}
	
	@Override
	public Token start(Board board) {
		TestRegistry.addCall("Platform", "0", "startTimer");
	    return super.start(board);
	}

	@Override
	public float randomize() {
		TestRegistry.addCall("Platform", "0", "randomize");
		return randoms.remove(0);
	}

	@Override
	public CanvasInterface createCanvas(boolean visible) {
		TestRegistry.addCall("Platform", "0", "createCanvas");
	    TestCanvas canvas = new TestCanvas(""+canvasCount++);
	    initCanvas(canvas, visible);
		return canvas; 
	}

	public void sendEvent(Event event) {
		TestRegistry.addCall("Platform", "0", "sendEvent", event.getParams());
		super.sendEvent(event);
	}

	@Override
	public ImageInterface createImage(final String url, final ImageElementRecord record) {
		TestRegistry.addCall("Platform", "0", "createImage", url);
		return new TestImage(url, (GraphicsImpl)getGraphics(), record);
	}
	
	public void click(int x, int y, Button button, boolean shiftKey, boolean ctrlKey, boolean altKey) {
		sendEvent(new MouseEvent(Type.MOUSE_CLICK, x, y, button, shiftKey, ctrlKey, altKey)); 
	}

	public void doubleClick(int x, int y, Button button, boolean shiftKey, boolean ctrlKey, boolean altKey) {
		sendEvent(new MouseEvent(Type.MOUSE_DOUBLE_CLICK, x, y, button, shiftKey, ctrlKey, altKey)); 
	}

	public void mouseDown(int x, int y, Button button, boolean shiftKey, boolean ctrlKey, boolean altKey) {
		sendEvent(new MouseEvent(Type.MOUSE_DOWN, x, y, button, shiftKey, ctrlKey, altKey)); 
	}

	public void mouseUp(int x, int y, Button button, boolean shiftKey, boolean ctrlKey, boolean altKey) {
		sendEvent(new MouseEvent(Type.MOUSE_UP, x, y, button, shiftKey, ctrlKey, altKey)); 
	}

	public void mouseMove(int x, int y, Button button, boolean shiftKey, boolean ctrlKey, boolean altKey) {
		sendEvent(new MouseEvent(Type.MOUSE_MOVE, x, y, button, shiftKey, ctrlKey, altKey)); 
	}

	public void mouseDrag(int x, int y, Button button, boolean shiftKey, boolean ctrlKey, boolean altKey) {
		sendEvent(new MouseEvent(Type.MOUSE_DRAG, x, y, button, shiftKey, ctrlKey, altKey)); 
	}

	public void mouseWheel(int x, int y, Button button, boolean shiftKey, boolean ctrlKey, boolean altKey) {
		sendEvent(new MouseEvent(Type.MOUSE_WHEEL, x, y, button, shiftKey, ctrlKey, altKey)); 
	}
}
