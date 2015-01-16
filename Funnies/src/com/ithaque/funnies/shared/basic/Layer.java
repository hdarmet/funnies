package com.ithaque.funnies.shared.basic;

public class Layer extends AbstractLayer implements LayoutDevice {
			
	boolean adjusted = false;
	
	public Layer(String id, float minX, float minY, float maxX, float maxY) {
		super(id, minX, minY, maxX, maxY);
	}
	
	@Override
	public void setLocation(Location location) {
		super.setLocation(location);
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
			Location[] absShape = TransformUtil.transformShape(this, getShape());
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
