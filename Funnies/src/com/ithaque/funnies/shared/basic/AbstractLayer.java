package com.ithaque.funnies.shared.basic;

import java.util.Collection;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.Trace;


public abstract class AbstractLayer extends GroupItem implements LayoutDevice {
			
	String id;
	boolean dirty = true;
	Token token = null;
	ItemRegistry eventRegistry = new ItemRegistry(this);

	public AbstractLayer(String id, float minX, float minY, float maxX, float maxY) {
		super.setShape(new Location(minX, minY), new Location(maxX, minY), new Location(maxX, maxY), new Location(minX, maxY));
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void addItem(Item item, float x, float y) {
		item.setLocation(new Location(x, y));
		addItem(item);
	}
	
	public String toString() {
		return "layer : \""+id+"\"";
	}
	
	@Override
	public void doSetParent(ItemHolder itemHolder) {
		if (!(itemHolder instanceof BaseDevice)) {
			throw new IllegalInvokeException();
		}
		super.doSetParent(itemHolder);
	}
	
	@Override
	public Token getLayerToken() {
		return token;
	}

	@Override
	public void dirty() {
		dirty = true;
		super.dirty();
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public void render(Graphics graphics, int currentLevel, int level) {
		if (dirty && currentLevel <= level) {
			setLayout(graphics);
			super.render(graphics, currentLevel, level);
			if (Trace.debug) {
				long time = getBoard().getTime();
				Trace.debug("Render layer content : "+this+". "+getItemCount()+" items rendered in "+(getBoard().getTime()-time)+" ms.\n");
			}
		}
	}
	
	@Override
	public void render(Graphics graphics) {
		if (dirty) {
			setLayout(graphics);
			graphics.clear();
			super.render(graphics);
			if (Trace.debug) {
				long time = getBoard().getTime();
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
	public void unregister(Item item) {
		eventRegistry.unregister(item);
	}

	@Override
	public void register(Item item) {
		eventRegistry.register(item);
	}
	
	@Override
	public void registerEvent(Item item, Event.Type eventType) {
		eventRegistry.registerEvent(item, eventType);
	}

	@Override
	public void unregisterEvent(Item item, Event.Type eventType) {
		eventRegistry.unregisterEvent(item, eventType);
	}

	@Override
	public Item getMouseTarget(MouseEvent event) {
		return eventRegistry.getMouseTarget(event);
	}
	
	@Override
	public Collection<Item> getMouseTargets(MouseEvent event) {
		return eventRegistry.getMouseTargets(event);
	}

	@Override
	public void unsetDirty() {
		dirty = false;
		super.unsetDirty();
	}
	
}
