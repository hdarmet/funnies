package com.ithaque.funnies.shared.funny.boardgame;

import com.ithaque.funnies.shared.Geometric;
import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.Color;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.TransformUtil;
import com.ithaque.funnies.shared.basic.items.PolygonItem;
import com.ithaque.funnies.shared.funny.AbstractFunny;
import com.ithaque.funnies.shared.funny.Funny;
import com.ithaque.funnies.shared.funny.FunnyObserver;
import com.ithaque.funnies.shared.funny.IncompatibleRingException;
import com.ithaque.funnies.shared.funny.Ring;
import com.ithaque.funnies.shared.funny.TrackableFunny;

public class ArrowFunny extends AbstractFunny {

	TrackableFunny source;
	TrackableFunny destination;
	PolygonItem arrowItem = null;
	float headHeight = 24.0f;
	float headWidth = 18.0f; 
	float queueWidth = 6.0f;
	float margin = 20.0f;
	Color lineColor = Color.BLACK;
	Color fillColor = Color.RED;
	float lineWidth = 1.0f;
	float opacity = 1.0f;
	FunnyObserver observer;
	
	public ArrowFunny(String id, TrackableFunny source, TrackableFunny destination) {
		super(id);
		this.observer = new FunnyObserver() {			
			@Override
			public void change(FunnyObserver.ChangeType type, Funny funny) {
				reshape(arrowItem);
			}
		};
		this.source = source;
		this.destination = destination;
	}

	public ArrowFunny setArrowMetrics(float headHeight,	float headWidth, float queueWidth, float margin) {
		this.headHeight = headHeight;
		this.headWidth = headWidth; 
		this.queueWidth = queueWidth;
		this.margin = margin;
		if (arrowItem != null) {
			reshape(arrowItem);
		}
		return this;
	}
	
	public ArrowFunny setArrowStyle(Color lineColor, Color fillColor, float lineWidth, float opacity) {
		this.lineColor = lineColor;
		this.fillColor = fillColor;
		this.lineWidth = lineWidth;
		this.opacity = opacity;
		if (arrowItem != null) {
			arrowItem.setStyle(lineColor, fillColor, lineWidth, opacity);
		}
		return this;
	}
	
	PolygonItem buildArrowItem() {
		PolygonItem result = new PolygonItem(fillColor, lineColor, lineWidth, opacity);
		reshape(result);
		return result;
	}

	void reshape(PolygonItem item) {
		Location sourceLocation = source.getLocation();
		Location destLocation = destination.getLocation();
		if (sourceLocation==null || destLocation==null) {
			item.setRotation(0.0f);
			item.setLocation(Location.ORIGIN);
			item.setShape(Location.EMPTY_SHAPE);
		}
		else {
			sourceLocation = TransformUtil.invertTransformLocation(getArrowSupport(), source.getLocation());
			destLocation = TransformUtil.invertTransformLocation(getArrowSupport(), destination.getLocation());
			float distance = Geometric.computeDistance(sourceLocation, destLocation);
			float angle = Geometric.computeAngle(sourceLocation, destLocation);
			Location[] arrowShape = computeArrowShape(distance-margin, headHeight, headWidth, queueWidth);
			Location location=new Location((sourceLocation.getX()+destLocation.getX())/2.0f, (sourceLocation.getY()+destLocation.getY())/2.0f);
			item.setRotation(angle);
			item.setLocation(location);
			item.setShape(arrowShape);
		}
	}
	
	@Override
	public GameBoardRing getRing() {
		return (GameBoardRing) super.getRing();
	}
	
	protected Layer getArrowSupport() {
		return getRing().animationLayer;
	}

	protected static Location[] computeArrowShape(float arrowHeight, float headHeight, float headWidth, float queueWidth) {
		if (headHeight>arrowHeight) {
			return Item.buildShape(
				0.0f, -headHeight/2.0f, 
				headWidth/2.0f, headHeight/2.0f, 
				-headWidth/2.0f, headHeight/2.0f);
		}
		else {
			return Item.buildShape(
				0.0f, -arrowHeight/2.0f, 
				headWidth/2.0f, -arrowHeight/2.0f +headHeight, 
				queueWidth/2.0f, -arrowHeight/2.0f +headHeight,
				queueWidth/2.0f, arrowHeight/2.0f,
				-queueWidth/2.0f, arrowHeight/2.0f,
				-queueWidth/2.0f, -arrowHeight/2.0f +headHeight,
				-headWidth/2.0f, -arrowHeight/2.0f +headHeight);
		}
	}
	
	@Override
	public void enterRing(Ring ring) {
		if (!(ring instanceof GameBoardRing)) {
			throw new IncompatibleRingException();
		}
		super.enterRing(ring);
		arrowItem = buildArrowItem();
		getArrowSupport().addItem(arrowItem);
		this.source.addObserver(observer);
		this.destination.addObserver(observer);
	}

	@Override
	public void exitRing(Ring ring) {
		if (ring != getRing()) {
			throw new IllegalInvokeException();
		}
		getArrowSupport().removeItem(arrowItem);
		this.source.removeObserver(observer);
		this.destination.removeObserver(observer);
		super.exitRing(ring);
	}

}
