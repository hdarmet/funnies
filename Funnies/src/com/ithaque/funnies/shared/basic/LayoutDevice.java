package com.ithaque.funnies.shared.basic;

public interface LayoutDevice extends ItemHolder, Device {

	Token getLayerToken();
	
	void register(Item item);

	void unregister(Item item);

	void registerEvent(Item item, Event.Type eventType);

	void unregisterEvent(Item item, Event.Type eventType);
	
	public Item getMouseTarget(MouseEvent event);
}
