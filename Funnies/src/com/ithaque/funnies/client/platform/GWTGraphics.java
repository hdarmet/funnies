package com.ithaque.funnies.client.platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.LineJoin;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.FillStrokeStyle;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.ithaque.funnies.shared.Trace;
import com.ithaque.funnies.shared.Transform;
import com.ithaque.funnies.shared.basic.Color;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.MouseEvent.Button;
import com.ithaque.funnies.shared.basic.Token;
import com.ithaque.funnies.shared.basic.TransformUtil;
import com.ithaque.funnies.shared.basic.items.AbstractImageItem;

public class GWTGraphics implements Graphics {

	GWTPlatform platform;
	int tokenCount = 0;
	Canvas mouseCanvas;
	Context2d context2d;
	Map<String, Token> imageTokens = new HashMap<String, Token>();
	Map<Token, ImageElementRecord> imageElements = new HashMap<Token, ImageElementRecord>();
	GWTLayer currentLayer;
	
	boolean drag = false;
	boolean debug = false;
	
	public GWTGraphics(GWTPlatform platform) {
		this.platform = platform;
		Graphics.Singleton.setGraphics(this);
	}
	
	ClickHandler clickHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			processClick(event);
		}
	};

	DoubleClickHandler doubleClickHandler = new DoubleClickHandler() {
		@Override
		public void onDoubleClick(DoubleClickEvent event) {
			processDoubleClick(event);
		}
	};
	
	MouseDownHandler mouseDownHandler = new MouseDownHandler() {
		@Override
		public void onMouseDown(MouseDownEvent event) {
			processMouseDown(event);
		}
	};
	
	MouseUpHandler mouseUpHandler = new MouseUpHandler() {
		@Override
		public void onMouseUp(MouseUpEvent event) {
			processMouseUp(event);
		}
	};
	
	MouseMoveHandler mouseMoveHandler = new MouseMoveHandler() {
		@Override
		public void onMouseMove(MouseMoveEvent event) {
			processMouseMove(event);
		}
	};
	
	MouseWheelHandler mouseWheelHandler = new MouseWheelHandler() {
		@Override
		public void onMouseWheel(MouseWheelEvent event) {
			processMouseWheel(event);
		}
	};
	
	static class ImageElementRecord {
		int count = 1;
		String url;
		boolean ready = false;
		ImageElement image = null;
		List<DrawImageRequest> requests = null;
		
		public ImageElementRecord(String url) {
			this.url = url;
		}
		
		public void addRequest(DrawImageRequest request) {
			if (requests == null) {
				requests = new ArrayList<DrawImageRequest>();
			}
			requests.add(request);
		}
	}
	
	class DrawImageRequest {
		
		AbstractImageItem imageItem;
		
		public DrawImageRequest(AbstractImageItem imageItem) {
			this.imageItem = imageItem;
		}
		
		public AbstractImageItem getImageItem() {
			return imageItem;
		}
		
		public void draw(Context2d context2d, ImageElement image, float opacity, int x, int y, int width, int height) {
			if (opacity>0.0f) {
				//drawShape(imageItem, imageItem.getShape());

				Transform transform = TransformUtil.transform(imageItem);
				if (transform!=null) {
					context2d.setTransform(transform.m[0], transform.m[1], transform.m[2], transform.m[3], transform.m[4], transform.m[5]);
					context2d.setGlobalAlpha(opacity);
					context2d.translate(-width/2.0f, -height/2.0f);
										
					context2d.drawImage(image, x, y, width, height, 0, 0, width, height);
				}
			}
		}
		
	}
	
	@Override
	public void drawImage(AbstractImageItem imageItem) {
		drawRequest(new DrawImageRequest(imageItem));
	}
	
	void drawRequest(DrawImageRequest request) {
		boolean ready = true;
		for (Token token : request.getImageItem().getTokens()) {
			ImageElementRecord record = imageElements.get(token);
			if (record.ready && record.requests!=null) {
				record.requests.remove(request);
	        	if (record.requests.isEmpty()) {
	        		record.requests = null;
	        	}
			}
			ready &= record.ready;
		}
		if (ready) {
			AbstractImageItem item = request.getImageItem();
			for (int index=0; index<item.getImageCount(); index++) {
				Token token = item.getToken(index);
				ImageElementRecord record = imageElements.get(token);
				request.draw(context2d, record.image, request.getImageItem().getOpacity(index), 
					(int)(float)item.getImageLeft(index), (int)(float)item.getImageTop(index), (int)(float)item.getImageWidth(index), (int)(float)item.getImageHeight(index));
				if (debug) {
					drawShape(request.getImageItem(), request.getImageItem().getShape());
				}
			}
		}
		else {
			for (Token token : request.getImageItem().getTokens()) {
				ImageElementRecord record = imageElements.get(token);
				if (!record.ready && (record.requests==null || !record.requests.contains(request))) {
					record.addRequest(request);
				}
			}
		}
	}
	
	@Override
	public Token loadImage(final String url) {
		Token token = imageTokens.get(url);
		if (token!=null) {
			ImageElementRecord record = imageElements.get(token);
			record.count++;
			return token; 
		}
		token = new Token(tokenCount++);
		final ImageElementRecord record = new ImageElementRecord(url);
		imageElements.put(token, record);
		imageTokens.put(url, token);
	    final Image img = new Image(url);
	    record.image = ImageElement.as(img.getElement());
	    img.addLoadHandler(new LoadHandler() {
	        @Override
	        public void onLoad(LoadEvent event) {
	        	if (Trace.debug) {
	        		Trace.debug("loaded : "+url);
	        	}
	        	RootPanel.get("images").remove(img);
	        	record.ready = true;
	        	for (DrawImageRequest request : new ArrayList<DrawImageRequest>(record.requests)) {
	        		request.getImageItem().dirty();
	        	}
	        }
	    });
	    img.setVisible(false);
	    RootPanel.get("images").add(img);
	    return token;
	}
	
	void processDoubleClick(DoubleClickEvent event) {
		platform.sendEvent(new MouseEvent(Type.MOUSE_DOUBLE_CLICK, event.getX(), event.getY(), 
			getButton(event.getNativeButton()), 
			event.isShiftKeyDown(), event.isControlKeyDown(), event.isAltKeyDown()));
	}
	
	void processClick(ClickEvent event) {
		platform.sendEvent(new MouseEvent(Type.MOUSE_CLICK, event.getX(), event.getY(), 
			getButton(event.getNativeButton()), 
			event.isShiftKeyDown(), event.isControlKeyDown(), event.isAltKeyDown()));
	}

	void processMouseDown(MouseDownEvent event) {
		drag = true;
		platform.sendEvent(new MouseEvent(Type.MOUSE_DOWN, event.getX(), event.getY(), 
			getButton(event.getNativeButton()), 
			event.isShiftKeyDown(), event.isControlKeyDown(), event.isAltKeyDown()));
	}
	
	void processMouseUp(MouseUpEvent event) {
		drag = false;
		platform.sendEvent(new MouseEvent(Type.MOUSE_UP, event.getX(), event.getY(), 
			getButton(event.getNativeButton()), 
			event.isShiftKeyDown(), event.isControlKeyDown(), event.isAltKeyDown()));
	}
	
	void processMouseMove(MouseMoveEvent event) {
		platform.sendEvent(new MouseEvent(drag?Type.MOUSE_DRAG:Type.MOUSE_MOVE, event.getX(), event.getY(), 
			getButton(event.getNativeButton()), 
			event.isShiftKeyDown(), event.isControlKeyDown(), event.isAltKeyDown()));
	}
	
	void processMouseWheel(MouseWheelEvent event) {
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

	@Override
	public void drawPolygon(Item item, Color fillColor, Color lineColor, float lineWidth, float opacity) {
		Location[] trShape = TransformUtil.transformShape(item, item.getShape());
		resetContext2d();
		context2d.setGlobalAlpha(opacity);
		context2d.setLineWidth(lineWidth);
		context2d.setLineJoin(LineJoin.ROUND);
		context2d.setStrokeStyle(getColor(lineColor));
		context2d.setFillStyle(getColor(fillColor));
		
		context2d.moveTo(trShape[0].getX(), trShape[0].getY());
		for (int i=1; i<trShape.length; i++) {
			context2d.lineTo(trShape[i].getX(), trShape[i].getY());
		}		
		context2d.lineTo(trShape[0].getX(), trShape[0].getY());
		context2d.fill();
		context2d.stroke();
	}
	
	FillStrokeStyle getColor(Color color) {
		return CssColor.make(color.getRed(), color.getGreen(), color.getBlue());
	}

	void drawShape(Item item, Location[] shape) {
		Location[] trShape = TransformUtil.transformShape(item, shape);
		resetContext2d();
		
		context2d.setLineWidth(0.0f);
		context2d.setLineJoin(LineJoin.ROUND);
		
		context2d.setShadowBlur(8);
		context2d.setShadowColor("#000000");
		context2d.setShadowOffsetX(8);
		context2d.setShadowOffsetY(8);
		
		context2d.moveTo(trShape[0].getX(), trShape[0].getY());
		for (int i=1; i<trShape.length; i++) {
			context2d.lineTo(trShape[i].getX(), trShape[i].getY());
		}		
		context2d.lineTo(trShape[0].getX(), trShape[0].getY());
		context2d.stroke();
	}

	@Override
	public Float getImageWidth(Token token) {
		ImageElementRecord imageRecord = imageElements.get(token);
		return !imageRecord.ready ? null : (float)imageRecord.image.getWidth();
	}

	@Override
	public Float getImageHeight(Token token) {
		ImageElementRecord imageRecord = imageElements.get(token);
		return !imageRecord.ready ? null : (float)imageRecord.image.getHeight();
	}
	
	@Override
	public void clear() {
		context2d = currentLayer.clear();
		resetContext2d();
		context2d.clearRect(0, 0, getDisplayWidth(), getDisplayHeight());
	}

	public Token start() {
	    mouseCanvas = createMouseCanvas();
	    Token token = createLayer();
	    return token;
	}	
	
	@Override
	public float getDisplayWidth() {
		return currentLayer.getDisplayWidth();
	}

	@Override
	public float getDisplayHeight() {
		return currentLayer.getDisplayHeight();
	}

	public boolean isReady() {
		return currentLayer != null;
	}

	Map<Token, GWTLayer> layers = new HashMap<Token, GWTLayer>();
	int layerTokenGenerator = 0;
	
	@Override
	public Token createLayer() {
		Token token = new Token(layerTokenGenerator++);
		GWTLayer layer = new GWTLayer(this);
		layers.put(token, layer);
		if (currentLayer==null) {
			currentLayer = layer;
		}
		return token;
	}

	@Override
	public void setLayer(Token token) {
		currentLayer = layers.get(token);
		context2d = currentLayer.getContext();
	}

	@Override
	public void show() { 
		for (GWTLayer layer : layers.values()) {
			layer.show();
		}
	}
	
	public void resetContext2d() {
		context2d.beginPath();
		context2d.setTransform(1, 0, 0, 1, 0, 0);
		context2d.setGlobalAlpha(1.0);
		//context2d.setShadowColor("#00000000");
	}
	
	Canvas createMouseCanvas() {
	    Canvas canvas = Canvas.createIfSupported();
	    canvas.getElement().setAttribute("style", "position:absolute;left:0px;top:0px;z-index:100;");
	    canvas.setVisible(true);
	    canvas.setCoordinateSpaceWidth(1000);
	    canvas.setCoordinateSpaceHeight(500);
	    canvas.addClickHandler(clickHandler);
	    canvas.addDoubleClickHandler(doubleClickHandler);
	    canvas.addMouseDownHandler(mouseDownHandler);
	    canvas.addMouseUpHandler(mouseUpHandler);
	    canvas.addMouseMoveHandler(mouseMoveHandler);
	    canvas.addMouseWheelHandler(mouseWheelHandler);
	    RootPanel.get("board").add(canvas);
		return canvas;
	}

}
