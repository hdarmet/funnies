package com.ithaque.funnies.shared.funny.boardgame;

import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.animations.ItemAnimation;
import com.ithaque.funnies.shared.funny.DraggableFunny;
import com.ithaque.funnies.shared.funny.IncompatibleRingException;
import com.ithaque.funnies.shared.funny.Ring;

public class CounterFunny implements DraggableFunny {

	String id;
	Item counterItem;
	ItemAnimation beginDragAnimation;
	ItemAnimation dropAnimation;
	
	public CounterFunny(String id, Item counterItem) {
		this.id = id;
		this.counterItem = counterItem;
	}
	
	public CounterFunny(String id, String counterImageUrl) {
		this(id,
			new ImageItem(counterImageUrl)
		);
		if (counterItem!=null) {
			((ImageItem)counterItem).addEventType(Type.MOUSE_DOWN);
		}
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void enterRing(Ring ring) {
		if (ring instanceof GameBoardRing) {
			GameBoardRing gbRing = (GameBoardRing)ring;
			if (counterItem!=null) {
				gbRing.piecesLayer.addItem(counterItem);
			}
		}
		else {
			throw new IncompatibleRingException();
		}
	}

	@Override
	public Item[] getDraggableItems() {
		if (counterItem!=null) {
			return new Item[] {counterItem};
		}
		else {
			return new Item[0];
		}
	}

	@Override
	public ItemAnimation getBeginDragAnimation() {
		return beginDragAnimation;
	}

	@Override
	public ItemAnimation getDropAnimation() {
		return dropAnimation;
	}

}
