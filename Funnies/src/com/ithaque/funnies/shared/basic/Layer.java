package com.ithaque.funnies.shared.basic;

import com.ithaque.funnies.shared.Trace;


public class Layer extends GroupItem {
			
	String id;
	boolean adjusted = false;
	boolean dirty = true;
	Token token = null;
	
	public Layer(String id, float minX, float minY, float maxX, float maxY) {
		super.setShape(new Location(minX, minY), new Location(maxX, minY), new Location(maxX, maxY), new Location(minX, maxY));
		this.id = id;
	}
	
	public void addItem(Item item, float x, float y) {
		item.setLocation(new Location(x, y));
		addItem(item);
	}
	
	public String toString() {
		return "layer : \""+id+"\"";
	}
	
	@Override
	public void setLocation(Location location) {
		super.setLocation(location);
		adjusted = false;
	}

	@Override
	public void prepare() {
		super.prepare();
		if (!adjusted) {
			adjustLocation();
			adjusted = true;
		}
	}
	
	@Override
	public void dirty() {
		dirty = true;
		super.dirty();
	}
	
	@Override
	public void render(Graphics graphics) {
		if (dirty) {
			if (token==null) {
				token = graphics.createLayer();
			}
			dirty = false;
			graphics.setLayer(token);
			graphics.clear();
			long time = getBoard().getTime();
			super.render(graphics);
			if (Trace.debug) {
				Trace.debug("Render : "+this+". "+getItemCount()+" items rendered in "+(getBoard().getTime()-time)+" ms.\n");
			}
		}
	}
	
	private void adjustLocation() {
		Board board = getBoard();
		if (board!=null && board.isReady()) {
			Location[] absShape = board.getGraphics().transformShape(this, getShape());
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
				Location absLocation = board.getGraphics().transformLocation(getParent(), getLocation());
				Location newLocation = new Location(absLocation.getX()+overhead.getX(), absLocation.getY()+overhead.getY());
				super.setLocation(board.getGraphics().invertTransformLocation(getParent(), newLocation));
			}
		}
	}
	
}
