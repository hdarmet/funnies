package com.ithaque.funnies.shared.funny.boardgame;

import com.ithaque.funnies.shared.Geometric;
import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Animation.Factory;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemObserver;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.TransformUtil;
import com.ithaque.funnies.shared.basic.items.DecoratedItem;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.StackItem;
import com.ithaque.funnies.shared.basic.items.animations.DropAnimation;
import com.ithaque.funnies.shared.basic.items.animations.RotateAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ScalingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.easing.OutBackEasing;
import com.ithaque.funnies.shared.basic.processors.AbstractDragProfile;
import com.ithaque.funnies.shared.basic.processors.TargetedRotateProfile;
import com.ithaque.funnies.shared.funny.DecoratedFunny;
import com.ithaque.funnies.shared.funny.DraggableFunny;
import com.ithaque.funnies.shared.funny.FunnyObserver;
import com.ithaque.funnies.shared.funny.IncompatibleRingException;
import com.ithaque.funnies.shared.funny.Ring;
import com.ithaque.funnies.shared.funny.RotatableFunny;
import com.ithaque.funnies.shared.funny.TrackableFunny;

public class CounterFunny extends DecoratedFunny implements DraggableFunny, RotatableFunny, TrackableFunny {
	private static final float BIG_FLOAT = 10000f;

	public static final Animation.Factory DEFAULT_BEGIN_DRAG_ANIMATION = 
		new ScalingAnimation.Builder(500, 1.1f)
			.setItem(AbstractDragProfile.draggedItem());
	public static final Animation.Factory DEFAULT_ADJUST_LOCATION_ANIMATION = 
		new DropAnimation.Builder(new OutBackEasing.Builder(1000))
			.setItem(AbstractDragProfile.draggedItem())
			.setDestinationHolder(AbstractDragProfile.dropItemHolder())
			.setLocation(AbstractDragProfile.dropLocation());
	public static final Animation.Factory DEFAULT_DRAGGED_DROP_ANIMATION = 
		new ScalingAnimation.Builder(500, 1.0f)
			.setItem(AbstractDragProfile.draggedItem());
	public static final Animation.Factory DEFAULT_FINISH_ROTATE_ANIMATION = 
		new RotateAnimation.Builder(1000)
			.setItem(TargetedRotateProfile.rotatableItem())
			.setRotation(TargetedRotateProfile.rotation());

	StackItem counterStackItem;
	DecoratedItem counterItem;
	Animation.Factory beginDragAnimation=DEFAULT_BEGIN_DRAG_ANIMATION;
	Animation.Factory adjustLocationAnimation=DEFAULT_ADJUST_LOCATION_ANIMATION;
	Animation.Factory draggedDropAnimation=DEFAULT_DRAGGED_DROP_ANIMATION;
	Animation.Factory finishRotateAnimation=DEFAULT_FINISH_ROTATE_ANIMATION;
	ItemObserver observer;
	float[] allowedAngles = null;
	
	public CounterFunny(String id, Item counterItem) {
		super(id);
		this.counterItem = new DecoratedItem(counterItem);
		this.counterStackItem = new StackItem(this.counterItem);
		this.observer = new ItemObserver() {			
			@Override
			public void change(ItemObserver.ChangeType type, Item item) {
				if (type==ItemObserver.ChangeType.LOCATION || type==ItemObserver.ChangeType.PARENT || type==ItemObserver.ChangeType.ANCESTOR) {
					fire(FunnyObserver.ChangeType.LOCATION);
				}
			}
		};
	}
	
	public CounterFunny(String id, String counterImageUrl) {
		this(id,
			new ImageItem(counterImageUrl)
		);
		if (counterStackItem!=null) {
			counterStackItem.addEventType(Type.MOUSE_DOWN);
		}
	}
	
	public void setBeginDragAnimation(Animation.Factory beginDragAnimation) {
		this.beginDragAnimation = beginDragAnimation;
	}

	public void setAdjustLocationAnimation(Animation.Factory adjustLocationAnimation) {
		this.adjustLocationAnimation = adjustLocationAnimation;
	}

	public void setDraggedDropAnimation(Animation.Factory draggedDropAnimation) {
		this.draggedDropAnimation = draggedDropAnimation;
	}

	@Override
	public void enterRing(Ring ring) {
		if (!(ring instanceof GameBoardRing)) {
			throw new IncompatibleRingException();
		}
		super.enterRing(ring);
		GameBoardRing gbRing = (GameBoardRing)ring;
		if (counterStackItem!=null) {
			gbRing.piecesLayer.addItem(counterStackItem);
		}
		this.counterStackItem.addObserver(observer);
	}

	@Override
	public void exitRing(Ring ring) {
		if (ring != getRing()) {
			throw new IllegalInvokeException();
		}
		GameBoardRing gbRing = (GameBoardRing)ring;
		if (counterStackItem!=null) {
			gbRing.piecesLayer.removeItem(counterStackItem);
		}
		this.counterStackItem.removeObserver(observer);
		super.exitRing(ring);
	}
	
	@Override
	public Item[] getDraggableItems() {
		if (counterStackItem!=null) {
			return new Item[] {counterStackItem};
		}
		else {
			return new Item[0];
		}
	}

	@Override
	public Item[] getRotatableItems() {
		if (allowedAngles!=null) {
			if (counterStackItem!=null) {
				return new Item[] {counterStackItem};
			}
		}
		return new Item[0];
	}

	@Override
	public Factory getFinishRotateAnimation() {
		return finishRotateAnimation;
	}

	public void setFinishRotateAnimation(Factory finishRotateAnimation) {
		this.finishRotateAnimation = finishRotateAnimation;
	}

	@Override
	public Float adjustRotation(float angle) {
		angle = Geometric.adjustAngle(angle);
		if (allowedAngles==null) {
			return angle;
		}
		Float bestAngle = null;
		float diff = BIG_FLOAT;
		for (Float aAngle : allowedAngles) {
			float aDiff = aAngle-angle;
			if (aDiff<0) {
				aDiff=-aDiff;
			}
			if (aDiff>Math.PI) {
				aDiff=(float)(2.0f*Math.PI-aDiff);
				aAngle = (float)(2.0f*Math.PI)-aAngle;
			}
			if (aDiff<diff) {
				bestAngle = aAngle;
				diff = aDiff;
			}
		}
		return bestAngle;
	}

	@Override
	public Float adjustScale(float scale) {
		return scale;
	}
	
	public CounterFunny setAllowedAngles(float[] allowedAngles) {
		this.allowedAngles = allowedAngles;
		return this;
	}
	
	@Override
	public Animation.Factory getBeginDragAnimation() {
		return beginDragAnimation;
	}

	@Override
	public Animation.Factory getAdjustLocationAnimation() {
		return adjustLocationAnimation;
	}

	@Override
	public Animation.Factory getDraggedDropAnimation() {
		return draggedDropAnimation;
	}

	public void setLocation(Location location) {
		if (counterStackItem!=null) {
			counterStackItem.setLocation(location);
		}
	}
	
	public void setRotation(float rotation) {
		if (counterStackItem!=null) {
			counterStackItem.setRotation(rotation);
		}
	}
	
	public void setScale(float scale) {
		if (counterStackItem!=null) {
			counterStackItem.setScale(scale);
		}
	}

	@Override
	public GameBoardRing getRing() {
		return (GameBoardRing) super.getRing();
	}

	@Override
	public Location getLocation() {
		return TransformUtil.transformLocation(counterStackItem.getParent(), counterStackItem.getLocation());
	}

	@Override
	public DecoratedItem getDecorationSupport() {
		return counterItem;
	}

}
