package com.ithaque.funnies.shared.basic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ithaque.funnies.shared.Trace;
import com.ithaque.funnies.shared.basic.Event.Type;

public class ItemRegistry {
	LayoutDevice layoutDevice;
	Map<Event.Type, Set<Item>> events = new HashMap<Event.Type, Set<Item>>();

	public ItemRegistry(LayoutDevice device) {
		this.layoutDevice = device;
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
			Trace.debug("Add event "+eventType+" to "+item+" on "+layoutDevice+"\n");
		}
	}

	public void unregisterEvent(Item item, Event.Type eventType) {
		getEventRegistery(eventType).remove(item);
		if (Trace.debug) {
			Trace.debug("Remove event "+eventType+" from "+item+" from "+layoutDevice+"\n");
		}
	}

	public Item getMouseTarget(MouseEvent event) {
		ItemComparator itemComparator = new ItemComparator();
		Item target = null;
		for (Item item : getTargetItems(event.getType())) {
			if (item.acceptEvent(event)) {
				if (target==null) {
					target=item;
				}
				else {
					if (itemComparator.compare(target, item)<0) {
						target=item;
					}
				}
			}
		}
		return target;
	}
	
	public Set<Item> getTargetItems(Type eventType) {
		Set<Item> items = new HashSet<Item>();
		items.addAll(getEventRegistery(eventType));
		items.addAll(getEventRegistery(Type.ALL));
		return items;
	}

	Set<Item> getEventRegistery(Event.Type eventType) {
		Set<Item> registry = events.get(eventType);
		if (registry==null) {
			registry = new HashSet<Item>();
			events.put(eventType, registry);
		}
		return registry;
	}

}
