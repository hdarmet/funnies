package com.ithaque.funnies.shared.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ithaque.funnies.shared.Trace;

public class Board implements BaseDevice, LayoutDevice {

	List<Item> items = new ArrayList<Item>();
	List<Alarm> alarms = new ArrayList<Alarm>();
	List<Animation> animations = new ArrayList<Animation>();
	List<Processor> processors = new ArrayList<Processor>();
	Platform platform;
	ItemRegistry eventRegistry = new ItemRegistry(this);
	
	boolean dirty = false;
	Token token;
	
	public Board(Platform platform) {
		this.platform = platform;
	}
	
	public void dirty() {
		this.dirty = true;
	}
	
	public boolean isDirty() {
		return this.dirty;
	}
	
	public Graphics getGraphics() {
		return platform.getGraphics();
	}
	
	public long getTime() {
		return platform.getTime();
	}
	
	public float randomize() {
		return platform.randomize();
	}
	
	@Override
	public LayoutDevice getLayout() {
		return this;
	}
	
	public void render() {
		if (dirty) {
			int maxLevel=0;
			for (Item item : items) {
				int highestLevel = item.prepare();
				if (maxLevel<highestLevel) {
					maxLevel = highestLevel;
				}
			}
			Graphics graphics = getGraphics();
			graphics.setLayer(token);
			graphics.clear();
			for (int level=0; level <= maxLevel; level++) {
				for (Item item : items) {
					item.render(graphics, 0, level);
				}
			}
			graphics.show();
			for (Item item : items) {
				item.unsetDirty();
			}
			dirty = false;
		}
	}
	
	public void animate(long time) {
		List<Animation> finished = null;
		boolean atLeastOne = false;
		for (Animation animation : new ArrayList<Animation>(animations)) {
			atLeastOne = true;
			if (!animation.animate(time)) {
				animation.finish(time);
				if (finished==null) {
					finished = new ArrayList<Animation>();
				}
				finished.add(animation);
			}
		}
		if (finished != null) {
			for (Animation animation : finished) {
				animations.remove(animation);
			}
		}
		if (Trace.debug && atLeastOne) {
			Trace.debug("Animation done in "+(platform.getTime()-time)+" ms.\n");
		}
	}
	
	@Override
	public void addItem(Item item) {
		if (item.getParent()!=null) {
			throw new AlreadyAttachedItemException();
		}
		items.add(item);
		item.doSetParent(this);
		dirty();
	}
	
	public void setItem(int index, Item item) {
		if (item.getParent()!=null) {
			throw new AlreadyAttachedItemException();
		}
		items.get(index).free();
		items.add(index, item);
		item.doSetParent(this);
		dirty();
	}

	public void addItem(int index, Item item) {
		if (item.getParent()!=null) {
			throw new AlreadyAttachedItemException();
		}
		items.add(index, item);
		item.doSetParent(this);
		dirty();
	}
	
	@Override
	public void removeItem(Item item) {
		if (item.getParent()!=this) {
			throw new ItemNotAttachedException();
		}
		items.remove(item);
		item.removeFromParent();
		dirty();
	}

	public Item getItem(int index) {
		return items.get(index);
	}

	@Override
	public int getItemCount() {
		return items.size();
	}
	
	@Override
	public boolean contains(Item item) {
		return items.contains(item);
	}

	@Override
	public Board getBoard() {
		return this;
	}

	public void launchAnimation(Animation animation) {
		long time = platform.getTime();
		if (animation.start(time)) {
			animations.add(animation);			
		}
		else {
			animation.finish(time);
		}
	}
	
	public void stopAnimation(Animation animation) {
		long time = platform.getTime();
		if (animations.contains(animation)) {
			animation.finish(time);
			animations.remove(animation);
		}
	}
	
	public void processEvent(Event event) {
		for (Processor processor : processors) {
			boolean processed = processor.process(event, this);
			if (Trace.debug) {
				long time = platform.getTime();
				Trace.debug("Processing of "+event.getType()+" done in "+(platform.getTime()-time)+" ms.\n");
			}
			if (processed) {
				return;
			}
		}
	}
	
	public void addProcessor(Processor processor) {
		processors.remove(processor);
		processors.add(processor);
	}

	public void setProcessor(int index, Processor processor) {
		processors.remove(processor);
		processors.set(index, processor);
	}
	
	public void removeProcessor(Processor processor) {
		processors.remove(processor);
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
		for (int index=getItemCount()-1; index>=0; index--) {
			Item child = getItem(index);
			if (child instanceof Device) {
				Item target = ((Device)child).getMouseTarget(event);
				if (target!=null) {
					return target;
				}
			}
		}
		return eventRegistry.getMouseTarget(event);
	}

	@Override
	public Collection<Item> getMouseTargets(MouseEvent event) {
		Set<Item> targets = new HashSet<Item>();
		for (int index=getItemCount()-1; index>=0; index--) {
			Item child = getItem(index);
			if (child instanceof Device) {
				Collection<Item> childTargets = ((Device)child).getMouseTargets(event);
				targets.addAll(childTargets);
			}
		}
		targets.addAll(eventRegistry.getMouseTargets(event));
		return targets;
	}
	
	@Override
	public int indexOfItem(Item item) {
		return items.indexOf(item);
	}

	@Override
	public float getScale() {
		return ItemHolder.STANDARD_SCALE;
	}

	public void start() {
		token = platform.start(this);
	}

	@Override
	public float getRotation() {
		return NO_ROTATION;
	}

	@Override
	public float getDisplayRotation() {
		return getRotation();
	}
	
	@Override
	public float getDisplayScale() {
		return getScale();
	}
	
	@Override
	public Location getLocation() {
		return Location.ORIGIN;
	}

	@Override
	public float getOpacity() {
		return FULL_OPACITY;
	}
	
	@Override
	public float getDisplayOpacity() {
		return FULL_OPACITY;
	}
	
	public boolean isReady() {
		return platform.isReady();
	}

	@Override
	public Token getLayerToken() {
		return token;
	}

	public void addAlarm(long timeout, boolean repeat, String alarmId) {
		alarms.add(new Alarm(timeout, repeat, alarmId)); 
	}

	public void alarm(long time) {
		for (Alarm alarm : new ArrayList<Alarm>(alarms)) {
			Event event = alarm.process(time);
			if (alarm.isFinished()) {
				alarms.remove(alarm);
			}
			if (event!=null) {
				processEvent(event);
			}
		}
	}

}
