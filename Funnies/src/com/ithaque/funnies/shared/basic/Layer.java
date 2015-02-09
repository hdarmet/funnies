package com.ithaque.funnies.shared.basic;

import com.ithaque.funnies.shared.Location;
import com.ithaque.funnies.shared.Shape;

public class Layer extends AbstractLayer {
			
	boolean adjusted = false;
	
	public Layer(String id, float minX, float minY, float maxX, float maxY) {
		super(id, minX, minY, maxX, maxY);
	}
	
	@Override
	public void setLocation(Location location, long serial) {
		super.setLocation(location, serial);
		adjusted = false;
	}

	@Override
	public int prepare() {
		if (isDirty()) {
			int maxLevel = super.prepare();
			if (!adjusted) {
				adjustLocation();
				adjusted = true;
			}
			return maxLevel;
		}
		else {
			return 0;
		}
	}
	
	private void adjustLocation() {
		Board board = getBoard();
		if (board!=null && board.isReady()) {
			Shape absShape = TransformUtil.transformShape(this, getShape());
			float minX = absShape.getX(0);
			float maxX = absShape.getX(0);
			float minY = absShape.getY(0);
			float maxY = absShape.getY(0);
			for (int i=1; i<absShape.size(); i++) {
				if (minX>absShape.getX(i)) {
					minX=absShape.getX(i);
				}
				if (maxX<absShape.getX(i)) {
					maxX=absShape.getX(i);
				}
				if (minY>absShape.getY(i)) {
					minY=absShape.getY(i);
				}
				if (maxY<absShape.getY(i)) {
					maxY=absShape.getY(i);
				}
			}
			Location overhead = GraphicsUtil.inDisplayLimits(
				minX, minY, maxX, maxY,
				0.0f, 0.0f, board.getGraphics().getDisplayWidth(), board.getGraphics().getDisplayHeight());
			if (overhead != null) {
				Location absLocation = TransformUtil.transformLocation(getParent(), getLocation());
				Location newLocation = new Location(absLocation.getX()+overhead.getX(), absLocation.getY()+overhead.getY());
				super.setLocation(TransformUtil.invertTransformLocation(getParent(), newLocation));
			}
		}
	}

	
}
