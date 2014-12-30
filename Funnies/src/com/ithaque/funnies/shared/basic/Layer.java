package com.ithaque.funnies.shared.basic;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.Trace;


public class Layer extends GroupItem implements LayoutDevice {
			
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
	public void setParent(ItemHolder itemHolder) {
		if (!(itemHolder instanceof BaseDevice)) {
			throw new IllegalInvokeException();
		}
		super.setParent(itemHolder);
	}
	
	@Override
	public void setLocation(Location location) {
		super.setLocation(location);
		adjusted = false;
	}
	
	@Override
	public Token getLayerToken() {
		return token;
	}

	@Override
	public int prepare() {
		if (dirty) {
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
	
	@Override
	public void dirty() {
		dirty = true;
		super.dirty();
	}
	
	public void render(Graphics graphics, int currentLevel, int level) {
		if (dirty && currentLevel <= level) {
			setLayout(graphics);
			long time = getBoard().getTime();
			super.render(graphics, currentLevel, level);
			if (Trace.debug) {
				Trace.debug("Render layer content : "+this+". "+getItemCount()+" items rendered in "+(getBoard().getTime()-time)+" ms.\n");
			}
		}
	}
	
	@Override
	public void render(Graphics graphics) {
		if (dirty) {
			setLayout(graphics);
			graphics.clear();
			long time = getBoard().getTime();
			super.render(graphics);
			if (Trace.debug) {
				Trace.debug("Render layer : "+this+" in "+(getBoard().getTime()-time)+" ms.\n");
			}
		}
	}

	private void setLayout(Graphics graphics) {
		if (token==null) {
			token = graphics.createLayer();
		}
		graphics.setLayer(token);
	}
	
	@Override
	public void unsetDirty() {
		dirty = false;
		super.unsetDirty();
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
