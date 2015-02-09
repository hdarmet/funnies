package com.ithaque.funnies.shared.funny.boardgame;

import com.ithaque.funnies.shared.Geometric;
import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.Location;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Animation.Factory;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemHolder;
import com.ithaque.funnies.shared.basic.ItemObserver;
import com.ithaque.funnies.shared.basic.TransformUtil;
import com.ithaque.funnies.shared.basic.items.DecoratedItem;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.StackItem;
import com.ithaque.funnies.shared.basic.items.animations.ChangeAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ChangeFaceAnimation;
import com.ithaque.funnies.shared.basic.items.animations.DragAnimation;
import com.ithaque.funnies.shared.basic.items.animations.DropAnimation;
import com.ithaque.funnies.shared.basic.items.animations.OptimizedRotateAnimation;
import com.ithaque.funnies.shared.basic.items.animations.RotateAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ScalingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.SequenceAnimation;
import com.ithaque.funnies.shared.basic.items.animations.easing.LinearEasing;
import com.ithaque.funnies.shared.basic.items.animations.easing.OutBackEasing;
import com.ithaque.funnies.shared.basic.processors.AbstractDragProfile;
import com.ithaque.funnies.shared.basic.processors.RandomAnimationProcessor;
import com.ithaque.funnies.shared.basic.processors.TargetedRotateProfile;
import com.ithaque.funnies.shared.funny.AnimatedFunny;
import com.ithaque.funnies.shared.funny.DecoratedFunny;
import com.ithaque.funnies.shared.funny.DraggableFunny;
import com.ithaque.funnies.shared.funny.FunnyObserver;
import com.ithaque.funnies.shared.funny.FunnySpy;
import com.ithaque.funnies.shared.funny.IncompatibleRingException;
import com.ithaque.funnies.shared.funny.AbstractRing;
import com.ithaque.funnies.shared.funny.RotatableFunny;
import com.ithaque.funnies.shared.funny.TargetFunny;
import com.ithaque.funnies.shared.funny.TrackableFunny;

public class CounterFunny extends DecoratedFunny implements DraggableFunny, RotatableFunny, TrackableFunny, AnimatedFunny {
	private static final float BIG_FLOAT = 10000f;

	public static final Animation.Factory DEFAULT_BEGIN_DRAG_ANIMATION = 
		new ScalingAnimation.Builder(500, 1.1f)
			.setItem(AbstractDragProfile.draggedItem());
	public static final Animation.Factory DEFAULT_MOVE_ANIMATION = 
		new ChangeAnimation.Builder(new OutBackEasing.Builder(1000))
			.setItem(AbstractDragProfile.draggedItem())
			.setDestinationHolder(AbstractDragProfile.dropItemHolder())
			.setLocation(AbstractDragProfile.dropLocation())
			.setRotation(AbstractDragProfile.dropRotation())
			.setScale(AbstractDragProfile.dropScale());
	public static final Animation.Factory DEFAULT_DRAGGED_DROP_ANIMATION = 
		new ScalingAnimation.Builder(500, 1.0f)
			.setItem(AbstractDragProfile.draggedItem());
	public static final Animation.Factory DEFAULT_FINISH_ROTATE_ANIMATION = 
		new RotateAnimation.Builder(1000)
			.setItem(TargetedRotateProfile.rotatableItem())
			.setRotation(TargetedRotateProfile.rotation());

	Item counterItem;
	StackItem counterStackItem;
	DecoratedItem counterDecorationItem;
	Animation.Factory beginDragAnimation=DEFAULT_BEGIN_DRAG_ANIMATION;
	Animation.Factory adjustLocationAnimation=DEFAULT_MOVE_ANIMATION;
	Animation.Factory draggedDropAnimation=DEFAULT_DRAGGED_DROP_ANIMATION;
	Animation.Factory finishRotateAnimation=DEFAULT_FINISH_ROTATE_ANIMATION;
	
	ItemObserver observer;
	float[] allowedAngles = null;
	
	public CounterFunny(String id, Item counterItem) {
		super(id);
		this.counterItem = counterItem;
		this.counterDecorationItem = new DecoratedItem(counterItem);
		this.counterStackItem = new StackItem(this.counterDecorationItem);
		this.observer = new ItemObserver() {			
			@Override
			public void change(ItemObserver.ChangeType type, Item item) {
				if (type==ItemObserver.ChangeType.LOCATION || type==ItemObserver.ChangeType.PARENT || type==ItemObserver.ChangeType.ANCESTOR) {
					fire(FunnyObserver.ChangeType.LOCATION);
				}
			}
		};
	}
	
	public CounterFunny(String id, String ... counterImageUrl) {
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
	public void enterRing(AbstractRing ring) {
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
	public void exitRing(AbstractRing ring) {
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
			while (aDiff>Math.PI) {
				aDiff= aDiff-(float)(2.0f*Math.PI);
				aAngle = aAngle-(float)(2.0f*Math.PI);
			}
			while (aDiff<-Math.PI) {
				aDiff= aDiff+(float)(2.0f*Math.PI);
				aAngle = aAngle+(float)(2.0f*Math.PI);
			}
			if (aDiff<0) {
				aDiff=-aDiff;
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
		return counterStackItem.getDisplayLocation();
	}

	@Override
	public DecoratedItem getDecorationSupport() {
		return counterDecorationItem;
	}

	public Move move() {
		return new Move();
	}
	
	public class Move {
		SequenceAnimation sequenceAnimation = new SequenceAnimation();
		TargetFunny lastTarget = null;
		
		public Move turnTo(TargetFunny target) {
			Location counterLocation = getLocation();
			Location targetLocation = target.getLocation();
			Float angle = Geometric.computeAngle(counterLocation, targetLocation);
			TransformUtil.transformAbsoluteRotation(counterStackItem.getParent(), angle);
			angle = adjustRotation(angle);			
			RotateAnimation animation = new OptimizedRotateAnimation(1000, angle);
			animation.setItem(counterStackItem);
			sequenceAnimation.addAnimation(animation);
			return this;
		}
		
		public Move goTo(TargetFunny target) {
			lastTarget = target;
			ItemHolder targetHolder = target.getTargetHolder();
			Location targetLocation = target.getTargetLocation(counterStackItem);
			Location dragTargetLocation = TransformUtil.transformLocation(targetHolder, getRing().dragLayer, targetLocation);
			DragAnimation dragAnimation = new DragAnimation(new LinearEasing(1000), getRing().dragLayer, dragTargetLocation);
			dragAnimation.setItem(counterStackItem);
			sequenceAnimation.addAnimation(dragAnimation);
			return this;
		}	
			
		void stopTo(TargetFunny target) {
			ItemHolder targetHolder = target.getTargetHolder();
			if (targetHolder==counterStackItem) {
				targetHolder=counterStackItem.getParent();
			}
			Location targetLocation = target.getTargetLocation(counterStackItem);
			DropAnimation dropAnimation = new DropAnimation(new LinearEasing(1000), targetHolder, targetLocation);
			dropAnimation.setItem(counterStackItem);
			sequenceAnimation.addAnimation(dropAnimation);
		}
		
		public Animation getAnimation() {
			if (lastTarget!=null) {
				stopTo(lastTarget);
			}
			return sequenceAnimation;
		}
		
	}

	@Override
	public Animation.Factory getAnimation(Item item) {
		return new SequenceAnimation.Builder()
				.addAnimation(new ChangeFaceAnimation.Builder(500, 1).setItem(RandomAnimationProcessor.animatedItem()))
				.addAnimation(new ChangeFaceAnimation.Builder(500, 0).setItem(RandomAnimationProcessor.animatedItem()));
	}

	@Override
	public Item[] getAnimatedItems() {
		return new Item[] {counterItem};
	}
	
	@Override
	public void addSpy(FunnySpy spy) {
		counterItem.addObserver(spy);
		counterStackItem.addObserver(spy);
	}

	@Override
	public void removeSpy(FunnySpy spy) {
		counterItem.removeObserver(spy);
		counterStackItem.addObserver(spy);
	}

}
