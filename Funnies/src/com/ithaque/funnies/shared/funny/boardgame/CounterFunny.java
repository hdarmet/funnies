package com.ithaque.funnies.shared.funny.boardgame;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemObserver;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.TransformUtil;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.StackItem;
import com.ithaque.funnies.shared.basic.items.animations.MoveAnimation;
import com.ithaque.funnies.shared.basic.items.animations.SoftenAnimation;
import com.ithaque.funnies.shared.funny.AbstractFunny;
import com.ithaque.funnies.shared.funny.DraggableFunny;
import com.ithaque.funnies.shared.funny.FunnyObserver;
import com.ithaque.funnies.shared.funny.IncompatibleRingException;
import com.ithaque.funnies.shared.funny.Ring;
import com.ithaque.funnies.shared.funny.TrackableFunny;

public class CounterFunny extends AbstractFunny implements DraggableFunny, TrackableFunny {

	StackItem counterItem;
	SoftenAnimation.Builder beginDragAnimation;
	MoveAnimation.Builder adjustLocationAnimation;
	SoftenAnimation.Builder draggedDropAnimation;
	
	public CounterFunny(String id, Item counterItem) {
		super(id);
		this.counterItem = new StackItem(counterItem);
		this.counterItem.addObserver(new ItemObserver() {			
			@Override
			public void change(ItemObserver.ChangeType type, Item item) {
				if (type==ItemObserver.ChangeType.LOCATION || type==ItemObserver.ChangeType.PARENT || type==ItemObserver.ChangeType.ANCESTOR) {
					fire(FunnyObserver.ChangeType.LOCATION);
				}
			}
		});
	}
	
	public CounterFunny(String id, String counterImageUrl) {
		this(id,
			new ImageItem(counterImageUrl)
		);
		if (counterItem!=null) {
			counterItem.addEventType(Type.MOUSE_DOWN);
		}
	}
	
	public void setBeginDragAnimation(SoftenAnimation.Builder beginDragAnimation) {
		this.beginDragAnimation = beginDragAnimation;
	}

	public void setAdjustLocationAnimation(MoveAnimation.Builder adjustLocationAnimation) {
		this.adjustLocationAnimation = adjustLocationAnimation;
	}

	public void setDraggedDropAnimation(SoftenAnimation.Builder draggedDropAnimation) {
		this.draggedDropAnimation = draggedDropAnimation;
	}

	@Override
	public void enterRing(Ring ring) {
		if (!(ring instanceof GameBoardRing)) {
			throw new IncompatibleRingException();
		}
		super.enterRing(ring);
		GameBoardRing gbRing = (GameBoardRing)ring;
		if (counterItem!=null) {
			gbRing.piecesLayer.addItem(counterItem);
		}
	}

	@Override
	public void exitRing(Ring ring) {
		if (ring != getRing()) {
			throw new IllegalInvokeException();
		}
		GameBoardRing gbRing = (GameBoardRing)ring;
		if (counterItem!=null) {
			gbRing.piecesLayer.removeItem(counterItem);
		}
		super.exitRing(ring);
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
	public SoftenAnimation.Builder getBeginDragAnimation() {
		return beginDragAnimation;
	}

	@Override
	public MoveAnimation.Builder getAdjustLocationAnimation() {
		return adjustLocationAnimation;
	}

	@Override
	public SoftenAnimation.Builder getDraggedDropAnimation() {
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

	@Override
	public GameBoardRing getRing() {
		return (GameBoardRing) super.getRing();
	}

	@Override
	public Location getLocation() {
		return TransformUtil.transformLocation(counterItem.getParent(), counterItem.getLocation());
	}

}
