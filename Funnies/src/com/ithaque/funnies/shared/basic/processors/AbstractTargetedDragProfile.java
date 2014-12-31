package com.ithaque.funnies.shared.basic.processors;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.AnimationContext;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.items.animations.ParallelAnimation;

public abstract class AbstractTargetedDragProfile extends AbstractDragProfile {

	public static final AnimationContext.Key PREVIOUS_TARGET_KEY = new AnimationContext.Key("PREVIOUS_TARGET_KEY");
	public static final AnimationContext.Key NEW_TARGET_KEY = new AnimationContext.Key("NEW_TARGET_KEY");
	public static final AnimationContext.Key OTHER_TARGET_KEY = new AnimationContext.Key("OTHER_TARGET_KEY");
	
	List<Item> draggeables = new ArrayList<Item>();
	List<Item> targets = new ArrayList<Item>();
	Item currentTarget = null;
	Item newTarget = null;
	Item otherTarget = null;
	
	public AbstractTargetedDragProfile(Board board) {
		super(board);
	}
	
	protected boolean acceptDraggeable(Item draggeable) {
		return draggeables.contains(draggeable);
	}
	
	public void addDraggeable(Item draggeable) {
		draggeables.add(draggeable);
	}
	
	public void removeDraggeable(Item draggeable) {
		draggeables.remove(draggeable);
	}
	
	public void addTarget(Item target) {
		targets.add(target);
	}
	
	public void removeTarget(Item target) {
		targets.remove(target);
	}

	@Override
	protected void reactToDrag(MouseEvent event, Board board) {
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
			putDraggedOnHolder(dragged, target);
			adjustDraggedLocationOnDrop(animation, getDropLocation(dragged, target));
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

	protected void putDraggedOnHolder(Item dragged, Item target) {
		returnDraggedToOriginalHolder();		
	}
	
	protected Location getDropLocation(Item dragged, Item target) {
		return target.getLocation();
	}
	
	protected void animateTargetOnDrop(Item dragged, Item target) {
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
		for (Item aTarget : targets) {
			if (acceptTarget(dragged, aTarget)) {
				Animation.Factory aTargetAnimation = getHideAllowedTargetAnimation(aTarget);
				if (aTargetAnimation != null) {
					if (getHilightItem(aTarget)==target && targetAnimation!=null) {
						targetAnimation.addAnimation(aTargetAnimation.create());
					}
					else {
						Animation animation = aTargetAnimation.create();
						otherTarget = getHilightItem(aTarget);
						animation.setContext(retrieveAnimationContext());
						getBoard().launchAnimation(animation);
					}
				}
			}
		}
	}

	void showAllowedTargets(Item dragged) {
		for (Item target : targets) {
			if (acceptTarget(dragged, target)) {
				Animation targetAnimation = getShowAllowedTargetAnimation(target).create();
				if (targetAnimation != null) {
					otherTarget = getHilightItem(target);
					targetAnimation.setContext(retrieveAnimationContext());
					getBoard().launchAnimation(targetAnimation);
				}
			}
		}
	}
	
	protected Item getHilightItem(Item target) {
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
		for (Item target : new ArrayList<Item>(targets)) {
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
		return new DragDropAnimationContext(dragged, dropLocation, currentTarget, newTarget, otherTarget);
	}
	
	public static class DragDropAnimationContext extends DragAnimationContext {

		Item previousTarget = null;
		Item newTarget = null;
		Item otherTarget = null;

		public DragDropAnimationContext(Item dragged, Location dragLocation, Item previousTarget, Item newTarget, Item otherTarget) {
			super(dragged, dragLocation);
			this.previousTarget = previousTarget;
			this.newTarget = newTarget;
			this.otherTarget = otherTarget;
		}
		
		public Item getItem(Key itemKey) {
			Item item = super.getItem(itemKey);
			if (item==null) {
				if (itemKey==PREVIOUS_TARGET_KEY) {
					return previousTarget;
				}
				else if (itemKey==NEW_TARGET_KEY) {
					return newTarget;
				}			
				else if (itemKey==OTHER_TARGET_KEY) {
					return otherTarget;
				}	
			}
			return item;
		}
		
	}
}

