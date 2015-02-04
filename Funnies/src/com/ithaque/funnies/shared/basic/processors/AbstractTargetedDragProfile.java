package com.ithaque.funnies.shared.basic.processors;

import java.util.ArrayList;
import java.util.Collection;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.AnimationContext;
import com.ithaque.funnies.shared.basic.AnimationContext.MoveableFinder;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemHolder;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.Moveable;
import com.ithaque.funnies.shared.basic.items.animations.ParallelAnimation;

public abstract class AbstractTargetedDragProfile extends AbstractDragProfile {
	
	Item currentTarget = null;
	Item newTarget = null;
	Item otherTarget = null;
	
	public AbstractTargetedDragProfile(Board board) {
		super(board);
	}
	
	protected abstract Collection<Item> getTargets();
	
	@Override
	protected void processDrag(MouseEvent event, Board board) {
		Item target = getTarget(dragged, event);
		if (target!=currentTarget) {
			if (currentTarget!=null && getExitTargetAnimation(currentTarget)!=null) {
				Animation animation = getExitTargetAnimation(currentTarget).create();
				animation.setContext(retrieveAnimationContext());
				getBoard().launchAnimation(animation);
			}
			if (target!=null && getEnterTargetAnimation(target)!=null) {
				Animation animation = getEnterTargetAnimation(target).create();
				newTarget = target;
				animation.setContext(retrieveAnimationContext());
				getBoard().launchAnimation(animation);
			}
			currentTarget = target;
		}
	}
	
	@Override
	protected void launchDragAnimation(Item dragged) {
		super.launchDragAnimation(dragged);
		showAllowedTargets(dragged);
	}
 
	protected boolean resolveDrop(MouseEvent event, Board board, ParallelAnimation animation) {
		Item target = getTarget(dragged, event);
		if (target!=null) {
			adjustDraggedOnDrop(animation, 
				getDropItemHolder(dragged, target), 
				getDropLocation(dragged, target),
				getDropRotation(dragged, target),
				getDropScale(dragged, target));
			animateTargetOnDrop(dragged, target);
			executeDrop(dragged, target);
			currentTarget = null;
			return true;
		}
		else {
			hideAllowedTargets(dragged, target, null);
			currentTarget = null;
			return false;
		}
	}
	
	protected ItemHolder getDropItemHolder(Item dragged, Item target) {
		return initialHolder!=null ? initialHolder : target.getParent();
	}
	
	protected Location getDropLocation(Item dragged, Item target) {
		return target.getLocation();
	}

	protected Float getDropRotation(Item dragged, Item target) {
		return adjustDraggedRotation(dragged, getDropItemHolder(dragged, target));
	}

	protected Float getDropScale(Item dragged, Item target) {
		return adjustDraggedScale(dragged, getDropItemHolder(dragged, target));
	}

	void animateTargetOnDrop(Item dragged, Item target) {
		Animation.Factory itemAnimation = getTargetDropAnimation(target);
		if (itemAnimation==null) {
			itemAnimation = getExitTargetAnimation(target);
		}
		ParallelAnimation targetAnimation = new ParallelAnimation();
		if (itemAnimation!=null) {
			Animation animation = itemAnimation.create();
			newTarget = target;
			targetAnimation.addAnimation(animation);
		}
		hideAllowedTargets(dragged, target, targetAnimation);
		targetAnimation.setContext(retrieveAnimationContext());
		getBoard().launchAnimation(targetAnimation);
	}

	void hideAllowedTargets(Item dragged, Item target,
			ParallelAnimation targetAnimation) 
	{
		for (Item aTarget : getTargets()) {
			if (acceptTarget(dragged, aTarget)) {
				Animation.Factory aTargetAnimation = getHideAllowedTargetAnimation(aTarget);
				if (aTargetAnimation != null) {
					if (getHighlightItem(aTarget)==target && targetAnimation!=null) {
						targetAnimation.addAnimation(aTargetAnimation.create());
					}
					else {
						Animation animation = aTargetAnimation.create();
						otherTarget = getHighlightItem(aTarget);
						animation.setContext(retrieveAnimationContext());
						getBoard().launchAnimation(animation);
					}
				}
			}
		}
	}

	void showAllowedTargets(Item dragged) {
		for (Item target : getTargets()) {
			if (acceptTarget(dragged, target)) {
				Animation.Factory targetAnimationFactory = getShowAllowedTargetAnimation(target);
				if (targetAnimationFactory != null) {
					Animation targetAnimation = targetAnimationFactory.create();
					otherTarget = getHighlightItem(target);
					targetAnimation.setContext(retrieveAnimationContext());
					getBoard().launchAnimation(targetAnimation);
				}
			}
		}
	}
	
	protected Item getHighlightItem(Item target) {
		return target;
	}
	
	protected abstract Animation.Factory getTargetDropAnimation(Item target);

	protected abstract Animation.Factory getEnterTargetAnimation(Item target);
	
	protected abstract Animation.Factory getShowAllowedTargetAnimation(Item target);

	protected abstract Animation.Factory getHideAllowedTargetAnimation(Item target);

	protected abstract Animation.Factory getExitTargetAnimation(Item target);
	
	protected void executeDrop(Item dragged, Item target) {
	}

	protected Item getTarget(Item dragged, MouseEvent event) {
		for (Item target : new ArrayList<Item>(getTargets())) {
			if (target.acceptEvent(event) && acceptTarget(dragged, target)) {
				return target;
			}
		}
		return null;
	}

	protected boolean acceptTarget(Item draggable, Item target) {
		return true;
	}

	@Override
	public AnimationContext retrieveAnimationContext() {
		return new DragDropAnimationContext(dragged, dropLocation, dropHolder, dropRotation, dropScale, currentTarget, newTarget, otherTarget);
	}
	
	public static class DragDropAnimationContext extends DragAnimationContext {

		Item previousTarget = null;
		Item newTarget = null;
		Item otherTarget = null;

		public DragDropAnimationContext(Item dragged, Location dragLocation, ItemHolder dropHolder, Float dropRotation, Float dropScale, Item previousTarget, Item newTarget, Item otherTarget) {
			super(dragged, dragLocation, dropHolder, dropRotation, dropScale);
			this.previousTarget = previousTarget;
			this.newTarget = newTarget;
			this.otherTarget = otherTarget;
		}
		
	}
	
	public static MoveableFinder previousDropTarget() {
		return new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return ((DragDropAnimationContext)context).previousTarget;
			}			
		};
	}
	
	public static MoveableFinder newDropTarget() {
		return new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return ((DragDropAnimationContext)context).newTarget;
			}			
		};
	}
	
	public static MoveableFinder possibleDropTarget() {
		return new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return ((DragDropAnimationContext)context).otherTarget;
			}			
		};
	}

}

