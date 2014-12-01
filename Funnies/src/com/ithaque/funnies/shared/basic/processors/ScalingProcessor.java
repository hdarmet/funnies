package com.ithaque.funnies.shared.basic.processors;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event;
import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.MouseEvent.Button;
import com.ithaque.funnies.shared.basic.Processor;
import com.ithaque.funnies.shared.basic.Event.Type;

public class ScalingProcessor implements Processor {

	float increment;
	float minScale;
	float maxScale;
	List<Item> scalables = new ArrayList<Item>();

	public ScalingProcessor(float increment, float minScale, float maxScale) {
		this.increment = increment;
		this.minScale = minScale;
		this.maxScale = maxScale;
	}
	
	@Override
	public boolean process(Event event, Board board) {
		if (event.getType()==Type.MOUSE_WHEEL) {
			MouseEvent mouseEvent = (MouseEvent)event;
			Item scaled = board.getMouseTarget(mouseEvent);
			if (scaled != null && acceptScalable(scaled)) {
				if (mouseEvent.getButton()==Button.WHEEL_NORTH) {
					zoomIn(scaled, mouseEvent.getX(), mouseEvent.getY());
					return true;
				}
				else {
					zoomOut(scaled, mouseEvent.getX(), mouseEvent.getY());
					return true;
				}
			}
		}
		return false;
	}

	boolean acceptScalable(Item scaled) {
		return scalables.contains(scaled);
	}

	public void addScalable(Item item) {
		scalables.add(item);
	}
	
	public void removeScalable(Item item) {
		scalables.remove(item);
	}

	protected void zoom(Item scaled, int x, int y, float increment) {
		Graphics graphics = scaled.getBoard().getGraphics();
		Location oldLocation = graphics.invertTransformLocation(scaled, new Location(x, y));
		scaled.setScale(limits(scaled, scaled.getScale()*increment));
		Location delta = graphics.transformLocation(scaled, oldLocation);

		Location absLocation = graphics.transformLocation(scaled.getParent(), scaled.getLocation());
		Location newLocation = new Location(absLocation.getX()-(delta.getX()-x), absLocation.getY()-(delta.getY()-y));
		scaled.setLocation(graphics.invertTransformLocation(scaled.getParent(), newLocation));
	}

	protected void zoomIn(Item scaled, int x, int y) {
		zoom(scaled, x, y, increment);
	}

	protected void zoomOut(Item scaled, int x, int y) {
		zoom(scaled, x, y, 1.0f/increment);
	}

	protected float getMinScale(Item scaled) {
		Board board = scaled.getBoard();
		Location[] absShape = board.getGraphics().transformShape(scaled, scaled.getShape());
		float minX = absShape[0].getX();
		float maxX = absShape[0].getX();
		float minY = absShape[0].getY();
		float maxY = absShape[0].getY();
		for (int i=1; i<absShape.length; i++) {
			if (minX>absShape[i].getX()) {
				minX=absShape[i].getX();
			}
			if (maxX<absShape[i].getX()) {
				maxX=absShape[i].getX();
			}
			if (minY>absShape[i].getY()) {
				minY=absShape[i].getY();
			}
			if (maxY<absShape[i].getY()) {
				maxY=absShape[i].getY();
			}
		}
		float scaleX = board.getGraphics().getDisplayWidth()/(maxX - minX);
		float scaleY = board.getGraphics().getDisplayHeight()/(maxY - minY);
		float scale = scaleX>scaleY ? scaleX : scaleY;
		return scale>minScale ? scale : minScale;
	}
	
	protected float limits(Item scaled, float scale) {
		float minScale = getMinScale(scaled);
		return scale<minScale ? minScale : scale>maxScale ? maxScale : scale;
	}
}
