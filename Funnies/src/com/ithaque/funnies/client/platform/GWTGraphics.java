package com.ithaque.funnies.client.platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
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
import com.ithaque.funnies.shared.Geometric;
import com.ithaque.funnies.shared.Trace;
import com.ithaque.funnies.shared.Transform;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemHolder;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.MouseEvent.Button;
import com.ithaque.funnies.shared.basic.Moveable;
import com.ithaque.funnies.shared.basic.Token;
import com.ithaque.funnies.shared.basic.items.ImageItem;

public class GWTGraphics implements Graphics {

	GWTPlatform platform;
	int tokenCount = 0;
	Canvas mouseCanvas;
	Context2d context2d;
	Map<String, Token> imageTokens = new HashMap<String, Token>();
	Map<Token, ImageElementRecord> imageElements = new HashMap<Token, ImageElementRecord>();
	boolean drag = false;
	boolean debug = false;
	
	public GWTGraphics(GWTPlatform platform) {
		this.platform = platform;
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
		
		ImageItem imageItem;
		
		public DrawImageRequest(ImageItem imageItem) {
			this.imageItem = imageItem;
		}
		
		public ImageItem getImageItem() {
			return imageItem;
		}
		
		public void draw(Context2d context2d, ImageElement image, float opacity) {
			if (opacity>0.0f) {
				Transform transform = transform(imageItem);
				if (transform!=null) {
					context2d.setTransform(transform.m[0], transform.m[1], transform.m[2], transform.m[3], transform.m[4], transform.m[5]);
					context2d.setGlobalAlpha(opacity);
					context2d.translate(-image.getWidth()/2.0f, -image.getHeight()/2.0f);
					context2d.drawImage(image, 0, 0, image.getWidth(), image.getHeight());
				}
			}
		}
	}
	
	public Transform transform(Moveable item) {
		Transform transform = new Transform();
		transform.translate(currentLayer.getCenterX(), currentLayer.getCenterY());
		if (!transform(item, transform)) {
			return null;
		}
		return transform;
	}
	
	boolean transform(Moveable moveable, Transform transform) {
		if (moveable==null) {
			return false;
		}
		else {
			if (!(moveable instanceof Board)) {
				if (!transform(((Item)moveable).getParent(), transform)) {
					return false;
				}
			}
			transformToParent(moveable, transform);
			return true;
		}
	}

	protected void transformToParent(Moveable moveable, Transform transform) {
		Location translation = moveable.getLocation();
		if (translation!=null && !translation.equals(Location.ORIGIN)) {
			transform.translate(translation.getX(), translation.getY());
		}
		if (moveable.getScale()!=ItemHolder.STANDARD_SCALE) {
			transform.scale(moveable.getScale(), moveable.getScale());
		}
		if (moveable.getRotation()!=ItemHolder.NO_ROTATION) {
			transform.rotate(moveable.getRotation());
		}
	}
	
	public Transform transformToParent(Moveable item) {
		Transform transform = new Transform();
		transformToParent(item, transform);
		return transform;
	}
	
	@Override
	public void drawImage(ImageItem imageItem) {
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
			for (Token token : request.getImageItem().getTokens()) {
				ImageElementRecord record = imageElements.get(token);
				request.draw(context2d, record.image, request.getImageItem().getOpacity(token));
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

	public boolean isTarget(Item item, Location point, Location[] shape) {
		Transform transform = transform(item).invert();
		Location trPoint = transform.transformPoint(point);
		Location[] area = Geometric.getArea(shape);
		if (trPoint.getX()>=area[0].getX() && trPoint.getX()<=area[1].getX()
		  && trPoint.getY()>=area[0].getY() && trPoint.getY()<=area[1].getY()) {
			return Geometric.inside(trPoint, shape);			
		}
		else {
			return false;
		}
	}

	public void drawShape(Item item, Location[] shape) {
		Location[] trShape = transformShape(item, shape);
		resetContext2d();
		context2d.moveTo(trShape[0].getX(), trShape[0].getY());
		for (int i=1; i<trShape.length; i++) {
			context2d.lineTo(trShape[i].getX(), trShape[i].getY());
		}		
		context2d.lineTo(trShape[0].getX(), trShape[0].getY());
		context2d.stroke();
	}

	@Override
	public Location[] transformShape(Item item, Location[] shape) {
		Transform transform = transform(item);
		Location[] trShape = new Location[shape.length];
		for (int i=0; i<shape.length; i++) {
			trShape[i] = transform.transformPoint(shape[i]);
		}
		return trShape; 
	}
	
	@Override
	public Location[] getShape(ImageItem imageItem) {
		float width = 0.0f;
		float height = 0.0f;
		for (Token token : imageItem.getTokens()) {
			ImageElementRecord imageRecord = imageElements.get(token);
			if (!imageRecord.ready) {
				return null;
			}
			if (imageRecord.image.getWidth()>width) {
				width = imageRecord.image.getWidth();
			}
			if (imageRecord.image.getHeight()>height) {
				height = imageRecord.image.getHeight();
			}
		}
		Location upperLeft = new Location(-width/2.0f, -height/2.0f);
		Location upperRight = new Location(width/2.0f, -height/2.0f);
		Location bottomRight = new Location(width/2.0f, height/2.0f);
		Location bottomLeft = new Location(-width/2.0f, height/2.0f);
		return new Location[] {upperLeft, upperRight, bottomRight, bottomLeft};
	}

	@Override
	public Location invertTransformLocation(Moveable item, Location location) {
		Transform transform = transform(item).invert();
		return transform==null ? null : transform.transformPoint(location);
	}

	@Override
	public Location transformLocation(Moveable item, Location location) {
		Transform transform = transform(item);
		return transform==null ? null : transform.transformPoint(location);
	}

	@Override
	public Location invertTransformLocationToParent(Moveable item, Location location) {
		Transform transform = transformToParent(item).invert();
		return transform==null ? null : transform.transformPoint(location);
	}

	@Override
	public Location transformLocationToParent(Moveable item, Location location) {
		Transform transform = transformToParent(item);
		return transform==null ? null : transform.transformPoint(location);
	}
	
	GWTLayer currentLayer;
	
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
		return token;
	}

	@Override
	public void setLayer(Token token) {
		currentLayer = layers.get(token);
	}

	public void resetContext2d() {
		context2d.beginPath();
		context2d.setTransform(1, 0, 0, 1, 0, 0);
		context2d.setGlobalAlpha(1.0);
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
