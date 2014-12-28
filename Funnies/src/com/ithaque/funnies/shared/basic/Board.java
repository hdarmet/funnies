package com.ithaque.funnies.shared.basic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.ithaque.funnies.shared.Trace;

public class Board implements ItemHolder {

	List<Item> items = new ArrayList<Item>();
	List<Animation> animations = new ArrayList<Animation>();
	List<Processor> processors = new ArrayList<Processor>();
	Platform platform;
	Map<Event.Type, Set<Item>> events = new HashMap<Event.Type, Set<Item>>();
	boolean dirty = false;
	Token token;
	
	public Board(Platform platform) {
		this.platform = platform;
	}
	
	public void dirty() {
		this.dirty = true;
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
	
	public void render() {
		if (dirty) {
			int maxLevel=0;
			for (Item item : items) {
				int highestLevel = item.prepare();
				if (maxLevel<highestLevel) {
					maxLevel = highestLevel;
				}
			}
			platform.getGraphics().setLayer(token);
			Graphics graphics = platform.getGraphics();
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
	
	public void animate() {
		long time = platform.getTime();
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
		if (items.isEmpty() || items.get(items.size()-1)!=item) {
			item.free();
			item.setParent(this);
			items.add(item);
		}
	}
	
	@Override
	public void setItem(int index, Item item) {
		if (items.isEmpty() || items.get(index)!=item) {
			item.free();
			item.setParent(this);
			items.set(index, item);
		}
	}

	@Override
	public Item getItem(int index) {
		return items.get(index);
	}

	@Override
	public int getItemCount() {
		return items.size();
	}
	
	@Override
	public void removeItem(Item item) {
		if (item.getParent()==this) {
			items.remove(item);
			item.setParent(this);
		}
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
			long time = platform.getTime();
			boolean processed = processor.process(event, this);
			if (Trace.debug) {
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

	public void unregister(Item item) {
		for (Event.Type eventType : item.getEventTypes()) {
			unregisterEvent(item, eventType);
		}
	}

	public void register(Item item) {
		for (Event.Type eventType : item.getEventTypes()) {
			registerEvent(item, eventType);
		}
	}

	public void registerEvent(Item item, Event.Type eventType) {
		getEventRegistery(eventType).add(item);
		if (Trace.debug) {
			Trace.debug("Add event "+eventType+" to "+item+"\n");
		}
	}

	public void unregisterEvent(Item item, Event.Type eventType) {
		getEventRegistery(eventType).remove(item);
		if (Trace.debug) {
			Trace.debug("Remove event "+eventType+" from "+item+"\n");
		}
	}

	Set<Item> getEventRegistery(Event.Type eventType) {
		Set<Item> registry = events.get(eventType);
		if (registry==null) {
			registry = new HashSet<Item>();
			events.put(eventType, registry);
		}
		return registry;
	}

	@Override
	public int indexOfItem(Item item) {
		return items.indexOf(item);
	}
	
	public Item getMouseTarget(MouseEvent event) {
		SortedMap<List<Integer>, Item> sortedItems = new TreeMap<List<Integer>, Item>(comparator);
		for (Item item : getEventRegistery(event.getType())) {
			if (item.acceptEvent(event)) {
				List<Integer> itemRanges = new ArrayList<Integer>();
				getItemRanges(item, itemRanges);
				sortedItems.put(itemRanges, item);
			}
		}
		Iterator<Item> iterator = sortedItems.values().iterator();
		return iterator.hasNext() ? iterator.next() : null;
	}
	
	void getItemRanges(Item item, List<Integer> itemRanges) {
		if (item.getParent()!=null && item.getParent() instanceof Item) {
			getItemRanges((Item)item.getParent(), itemRanges);
		}
		itemRanges.add(item.getParent().indexOfItem(item));
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
	public Location getLocation() {
		return Location.ORIGIN;
	}

	public boolean isReady() {
		return platform.isReady();
	}

	Comparator<List<Integer>> comparator = new Comparator<List<Integer>>() {

		@Override
		public int compare(List<Integer> left, List<Integer> right) {
			if (left==null || right==null) {
				if (left!=null) {
					return 1;
				}
				if (right!=null) {
					return -1;
				}
				return 0;
			}
			int length = left.size() > right.size() ? right.size() : left.size();
			for (int index=0; index<length; index++) {
				int result = right.get(index).compareTo(left.get(index));
				if (result!=0) {
					return result;
				}
			}
			if (left.size()>length) {
				return -1;
			}
			if (right.size()>length) {
				return 1;
			}
			return 0;
		}
	};

}
