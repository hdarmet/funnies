package com.ithaque.funnies.shared.funny.boardgame;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.animations.ItemMoveAnimation;
import com.ithaque.funnies.shared.funny.DraggableFunny;
import com.ithaque.funnies.shared.funny.IncompatibleRingException;
import com.ithaque.funnies.shared.funny.Ring;

public class CounterFunny implements DraggableFunny {

	String id;
	Item counterItem;
	Animation.Factory beginDragAnimation;
	ItemMoveAnimation.Builder adjustLocationAnimation;
	Animation.Factory draggedDropAnimation;
	
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
	
	public void setBeginDragAnimation(Animation.Factory beginDragAnimation) {
		this.beginDragAnimation = beginDragAnimation;
	}

	public void setAdjustLocationAnimation(ItemMoveAnimation.Builder adjustLocationAnimation) {
		this.adjustLocationAnimation = adjustLocationAnimation;
	}

	public void setDraggedDropAnimation(Animation.Factory draggedDropAnimation) {
		this.draggedDropAnimation = draggedDropAnimation;
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
	public Animation.Factory getBeginDragAnimation() {
		return beginDragAnimation;
	}

	@Override
	public ItemMoveAnimation.Builder getAdjustLocationAnimation() {
		return adjustLocationAnimation;
	}

	@Override
	public Animation.Factory getDraggedDropAnimation() {
		return draggedDropAnimation;
	}

	public void setLocation(Location location) {
		if (counterItem!=null) {
			counterItem.setLocation(location);
		}
	}
	
	public void setRotation(float rotation) {
		if (counterItem!=null) {
			counterItem.setRotation(rotation);
		}
	}
	
	public void setScale(float scale) {
		if (counterItem!=null) {
			counterItem.setScale(scale);
		}
	}

}
