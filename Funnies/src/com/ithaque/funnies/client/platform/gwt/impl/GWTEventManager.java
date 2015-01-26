package com.ithaque.funnies.client.platform.gwt.impl;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.MouseEvent.Button;

public class GWTEventManager {

	boolean drag = false;
	GWTPlatform platform;
	
	public GWTEventManager(GWTPlatform platform) {
		this.platform = platform;
		createMouseCanvas();
	}
	
	ClickHandler clickHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			click(event);
		}
	};

	DoubleClickHandler doubleClickHandler = new DoubleClickHandler() {
		@Override
		public void onDoubleClick(DoubleClickEvent event) {
			doubleClick(event);
		}
	};
	
	MouseDownHandler mouseDownHandler = new MouseDownHandler() {
		@Override
		public void onMouseDown(MouseDownEvent event) {
			mouseDown(event);
		}
	};
	
	MouseUpHandler mouseUpHandler = new MouseUpHandler() {
		@Override
		public void onMouseUp(MouseUpEvent event) {
			mouseUp(event);
		}
	};
	
	MouseMoveHandler mouseMoveHandler = new MouseMoveHandler() {
		@Override
		public void onMouseMove(MouseMoveEvent event) {
			mouseMove(event);
		}
	};
	
	MouseWheelHandler mouseWheelHandler = new MouseWheelHandler() {
		@Override
		public void onMouseWheel(MouseWheelEvent event) {
			mouseWheel(event);
		}
	};

	public void click(ClickEvent event) {
		platform.sendEvent(new MouseEvent(Type.MOUSE_CLICK, event.getX(), event.getY(), 
				getButton(event.getNativeButton()), 
				event.isShiftKeyDown(), event.isControlKeyDown(), event.isAltKeyDown()));
	}

	public void doubleClick(DoubleClickEvent event) {
		platform.sendEvent(new MouseEvent(Type.MOUSE_DOUBLE_CLICK, event.getX(), event.getY(), 
				getButton(event.getNativeButton()), 
				event.isShiftKeyDown(), event.isControlKeyDown(), event.isAltKeyDown()));
	}

	public void mouseDown(MouseDownEvent event) {
		drag = true;
		platform.sendEvent(new MouseEvent(Type.MOUSE_DOWN, event.getX(), event.getY(), 
			getButton(event.getNativeButton()), 
			event.isShiftKeyDown(), event.isControlKeyDown(), event.isAltKeyDown()));
	}

	public void mouseUp(MouseUpEvent event) {
		drag = false;
		platform.sendEvent(new MouseEvent(Type.MOUSE_UP, event.getX(), event.getY(), 
			getButton(event.getNativeButton()), 
			event.isShiftKeyDown(), event.isControlKeyDown(), event.isAltKeyDown()));
	}

	public void mouseMove(MouseMoveEvent event) {
		platform.sendEvent(new MouseEvent(drag?Type.MOUSE_DRAG:Type.MOUSE_MOVE, event.getX(), event.getY(), 
				getButton(event.getNativeButton()), 
				event.isShiftKeyDown(), event.isControlKeyDown(), event.isAltKeyDown()));
	}

	public void mouseWheel(MouseWheelEvent event) {
		platform.sendEvent(new MouseEvent(Type.MOUSE_WHEEL, event.getX(), event.getY(), 
				event.isNorth()?Button.WHEEL_NORTH:Button.WHEEL_SOUTH, 
				event.isShiftKeyDown(), event.isControlKeyDown(), event.isAltKeyDown()));
	}

	Button getButton(int nativeButton) {
		switch (nativeButton) {
		case NativeEvent.BUTTON_RIGHT : return Button.RIGHT;
		case NativeEvent.BUTTON_LEFT : return Button.LEFT;
		case NativeEvent.BUTTON_MIDDLE : return Button.MIDDLE;
		default : return Button.NONE;
		}
	}

	void createMouseCanvas() {
	    Canvas canvas = Canvas.createIfSupported();
	    canvas.getElement().setAttribute("style", "position:absolute;left:0px;top:0px;z-index:100;");
	    canvas.setVisible(true);
	    canvas.setCoordinateSpaceWidth(1000);
	    canvas.setCoordinateSpaceHeight(500);
	    RootPanel.get("board").add(canvas);
	    canvas.addClickHandler(clickHandler);
	    canvas.addDoubleClickHandler(doubleClickHandler);
	    canvas.addMouseDownHandler(mouseDownHandler);
	    canvas.addMouseUpHandler(mouseUpHandler);
	    canvas.addMouseMoveHandler(mouseMoveHandler);
	    canvas.addMouseWheelHandler(mouseWheelHandler);
	}
}
