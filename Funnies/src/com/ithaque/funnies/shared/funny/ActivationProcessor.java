package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.Processor;

public class ActivationProcessor implements Processor {

	FunnyRegistry<ActivableFunny> activables = new FunnyRegistry<ActivableFunny>(
			new FunnyRegistry.ItemsFinder<ActivableFunny>() {
				@Override
				public Item[] getItems(ActivableFunny funny) {
					return funny.getActivableItems();
				}
			}
		) {
		@Override
		protected Record<ActivableFunny> createRecord(Item item, ActivableFunny funny) {
			return new Record<ActivableFunny>(funny, item);
		}
	};
	AbstractRing ring;
	
	public ActivationProcessor(AbstractRing ring) {
		this.ring = ring;
	}
	
	@Override
	public boolean process(Event event, Board board) {
		if (event instanceof MouseEvent && event.getType()==Type.MOUSE_CLICK) {
			Item activable = getActivable((MouseEvent)event);
			if (activable!=null) {
				Funny activableFunny = getActivableFunny(activable);
				if (activableFunny!=null) {
					((ActivableFunny)activableFunny).activate(activable);
				}
			}
		}
		return false;
	}
	
	Funny getActivableFunny(Item activable) {
		return activables.getFunny(activable);
	}

	protected Item getActivable(MouseEvent event) {
		Item activable = ring.getBoard().getMouseTarget(event);
		if (activables.containsItem(activable)) {
			if (activable.acceptEvent(event)) {
				return activable;
			}
		}
		return null;
	}

	public void registerActivableFunny(ActivableFunny funny) {
		activables.registerFunny(funny);
	}
	
	public void unregisterActivableFunny(ActivableFunny funny) {
		activables.unregisterFunny(funny);
	}
}
