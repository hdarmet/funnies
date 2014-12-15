package com.ithaque.funnies.shared.basic.processors;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.items.animations.ParallelItemAnimation;

public abstract class AbstractTargetedDragProfile extends AbstractDragProfile {

	List<Item> draggeables = new ArrayList<Item>();
	List<Item> targets = new ArrayList<Item>();
	Item currentTarget = null;
	
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
				animation.setItem(currentTarget);
				animation.launchFor();
			}
			if (target!=null && getEnterTargetAnimation(target)!=null) {
				Animation animation = getEnterTargetAnimation(target).create();
				animation.setItem(target);
				animation.launchFor();
			}
			currentTarget = target;
		}
	}
	
	@Override
	protected void launchDragAnimation(Item dragged) {
		super.launchDragAnimation(dragged);
		showAllowedTargets(dragged);
	}

	protected boolean resolveDrop(MouseEvent event, Board board, ParallelItemAnimation animation) {
		currentTarget = null;
		Item target = getTarget(dragged, event);
		if (target!=null) {
			adjustDraggedLocationOnDrop(animation, target.getLocation());
			animateTargetOnDrop(dragged, target);
			executeDrop(dragged, target);
			return true;
		}
		else {
			hideAllowedTargets(dragged, target, null);
			return false;
		}
	}
	
	protected void animateTargetOnDrop(Item dragged, Item target) {
		Animation.Factory itemAnimation = getTargetDropAnimation(target);
		if (itemAnimation==null) {
			itemAnimation = getExitTargetAnimation(target);
		}
		ParallelItemAnimation targetAnimation = new ParallelItemAnimation();
		if (itemAnimation!=null) {
			targetAnimation.addAnimation(itemAnimation.create());
		}
		hideAllowedTargets(dragged, target, targetAnimation);
		targetAnimation.setItem(target);
		targetAnimation.launchFor();
	}

	void hideAllowedTargets(Item dragged, Item target,
			ParallelItemAnimation targetAnimation) 
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
						animation.setItem(getHilightItem(aTarget));
						animation.launchFor();
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
					targetAnimation.setItem(getHilightItem(target));
					targetAnimation.launchFor();
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

}

