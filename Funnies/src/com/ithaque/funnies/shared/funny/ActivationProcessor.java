package com.ithaque.funnies.shared.funny;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.Processor;
import com.ithaque.funnies.shared.funny.notifications.ActivateEvent;

public class ActivationProcessor implements Processor {

	Map<Item, ActivableFunny> activables = new HashMap<Item, ActivableFunny>();
	Ring ring;
	
	public ActivationProcessor(Ring ring) {
		this.ring = ring;
	}
	
	@Override
	public boolean process(Event event, Board board) {
		if (event instanceof MouseEvent && event.getType()==Type.MOUSE_DOUBLE_CLICK) {
			Item activable = getActivable((MouseEvent)event);
			if (activable!=null) {
				Funny activableFunny = getActivableFunny(activable);
				if (activableFunny!=null) {
					ring.notify(new ActivateEvent(activableFunny));
				}
			}
		}
		return false;
	}
	
	Funny getActivableFunny(Item activable) {
		return activables.get(activable);
	}

	protected Item getActivable(MouseEvent event) {
		for (Item activable : new ArrayList<Item>(activables.keySet())) {
			if (activable.acceptEvent(event)) {
				return activable;
			}
		}
		return null;
	}

	public void registerActivableFunny(ActivableFunny funny) {
		for (Item item : funny.getActivableItems()) {
			activables.put(item, funny);
		}
	}
	
	public void unregisterActivableFunny(ActivableFunny funny) {
		for (Item item : funny.getActivableItems()) {
			activables.remove(item);
		}
	}
}
