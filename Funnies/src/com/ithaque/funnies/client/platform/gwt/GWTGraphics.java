package com.ithaque.funnies.client.platform.gwt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ithaque.funnies.shared.Trace;
import com.ithaque.funnies.shared.Transform;
import com.ithaque.funnies.shared.basic.Color;
import com.ithaque.funnies.shared.basic.Font;
import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.Token;
import com.ithaque.funnies.shared.basic.TransformUtil;
import com.ithaque.funnies.shared.basic.items.AbstractImageItem;

public class GWTGraphics implements Graphics {

	AbstractGWTPlatform platform;
	int tokenCount = 0;
	Context2D context2d;
	Map<String, Token> imageTokens = new HashMap<String, Token>();
	Map<Token, ImageElementRecord> imageElements = new HashMap<Token, ImageElementRecord>();
	GWTLayer currentLayer;
	Map<Token, GWTLayer> layerMap = new HashMap<Token, GWTLayer>();
	int layerTokenGenerator = 0;
	
	boolean debug = false;
	
	public GWTGraphics(AbstractGWTPlatform platform) {
		this.platform = platform;
		Graphics.Singleton.setGraphics(this);
	}
	
	
	public static class ImageElementRecord {
		int count = 1;
		String url;
		boolean ready = false;
		ImageInterface image = null;
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
		
		public void draw(Context2D context2d, ImageInterface image, float opacity, int x, int y, int width, int height) {
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
				request.draw(context2d, record.image, request.getImageItem().getDisplayOpacity(index), 
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
		record.image = platform.createImage(url, record);
	    return token;
	}

	public void drawPendingImages(final String url, final ImageElementRecord record) {
		if (Trace.debug) {
    		Trace.debug("loaded : "+url);
    	}
    	record.ready = true;
    	for (DrawImageRequest request : new ArrayList<DrawImageRequest>(record.requests)) {
    		request.getImageItem().dirty();
    	}
	}
	
	@Override
	public void drawPolygon(Item item, Color fillColor, Color lineColor, float lineWidth, float opacity) {
		Location[] trShape = TransformUtil.transformShape(item, item.getShape());
		resetContext2d();
		context2d.setGlobalAlpha(opacity);
		context2d.setLineWidth(lineWidth);
		context2d.setLineJoin("round");
		context2d.setStrokeStyle(lineColor);
		context2d.setFillStyle(fillColor);
		
		definePath(context2d, trShape);
		context2d.fill();
		context2d.stroke();
	}
	
	@Override
	public void drawText(Item item, String text, Color color, Font font, float opacity) {
		//drawShape(item, item.getShape());
		context2d.setFont(font.getSize()+"pt "+font.getFontName());
		context2d.setTextBaseline("top");
		context2d.setFillStyle(color);
		float width = getTextWidth(font, text);
		float height = getTextHeight(font, text);
		Transform transform = TransformUtil.transform(item);
		if (transform!=null) {
			context2d.setTransform(transform.m[0], transform.m[1], transform.m[2], transform.m[3], transform.m[4], transform.m[5]);
			context2d.setGlobalAlpha(opacity);
			float margin = 0f;
			for (String line : text.split("\n")) {
				context2d.fillText(line, -width/2.0f, -height/2.0f+margin);
				margin+=font.getSize()+font.getMargin();
			}
		}
	}
	
	@Override
	public float getTextWidth(Font font, String text) {
		context2d.setFont(font.getSize()+"pt "+font.getFontName());
		double width=0;
		for (String line : text.split("\n")) {
			double lineWidth = context2d.measureTextWidth(line);
			if (lineWidth>width) {
				width = lineWidth;
			}
		}
		return (float)width;
	}
	
	@Override
	public float getTextHeight(Font font, String text) {
		context2d.setFont(font.getSize()+"pt "+font.getFontName());
		int rowCount = text.split("\n").length;
		return rowCount*font.getSize()+font.getMargin();
	}
	
	void drawShape(Item item, Location[] shape) {
		Location[] trShape = TransformUtil.transformShape(item, shape);
		resetContext2d();
		
		context2d.setLineWidth(0.0f);
		context2d.setLineJoin("round");
		
//		context2d.setShadowBlur(8);
//		context2d.setShadowColor("#000000");
//		context2d.setShadowOffsetX(8);
//		context2d.setShadowOffsetY(8);
		
		definePath(context2d, trShape);
		context2d.stroke();
	}

	void definePath(Context2D context2d, Location[] trShape) {
		if (trShape.length>0) {
			context2d.moveTo(trShape[0].getX(), trShape[0].getY());
			for (int i=1; i<trShape.length; i++) {
				context2d.lineTo(trShape[i].getX(), trShape[i].getY());
			}		
			context2d.lineTo(trShape[0].getX(), trShape[0].getY());
		}
	}


	@Override
	public void clip(Item item, Location[] shape) {
		Location[] absShape = TransformUtil.transformShape(item, shape);
		definePath(context2d, absShape);
		context2d.clip();
	}

	@Override
	public Float getImageWidth(Token token) {
		if (token==null) {
			return null;
		}
		ImageElementRecord imageRecord = imageElements.get(token);
		return !imageRecord.ready ? null : (float)imageRecord.image.getWidth();
	}

	@Override
	public Float getImageHeight(Token token) {
		if (token==null) {
			return null;
		}
		ImageElementRecord imageRecord = imageElements.get(token);
		return !imageRecord.ready ? null : (float)imageRecord.image.getHeight();
	}
	
	@Override
	public void clear() {
		currentLayer.clear();
		context2d = currentLayer.getContext();
		resetContext2d();
		context2d.clearRect(0, 0, getDisplayWidth(), getDisplayHeight());
	}

	public Token start() {
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

	@Override
	public Token createLayer() {
		Token token = new Token(layerTokenGenerator++);
		GWTLayer layer = new GWTLayer(platform);
		layerMap.put(token, layer);
		if (currentLayer==null) {
			currentLayer = layer;
		}
		return token;
	}

	@Override
	public void setLayer(Token token) {
		currentLayer = layerMap.get(token);
		context2d = currentLayer.getContext();
	}
	
	@Override
	public int compareLayers(Token layer1, Token layer2) {
		return layer1.value() - layer2.value();
	}


	@Override
	public void show() { 
		for (GWTLayer layer : layerMap.values()) {
			layer.show();
		}
	}
	
	public void resetContext2d() {
		currentLayer.resetContext();
		context2d.beginPath();
	}

}
