package com.ithaque.funnies.shared.basic.processors;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.items.animations.ItemAnimation;
import com.ithaque.funnies.shared.basic.items.animations.SimultaneousItemAnimation;

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
				getExitTargetAnimation(currentTarget).duplicate().launchFor(currentTarget);
			}
			if (target!=null && getEnterTargetAnimation(target)!=null) {
				getEnterTargetAnimation(target).duplicate().launchFor(target);
			}
			currentTarget = target;
		}
	}
	
	@Override
	protected void launchDragAnimation(Item dragged) {
		super.launchDragAnimation(dragged);
		showAllowedTargets(dragged);
	}

	protected boolean executeDrop(MouseEvent event, Board board, SimultaneousItemAnimation animation) {
		currentTarget = null;
		Item target = getTarget(dragged, event);
		if (target!=null) {
			adjustDraggedLocationOnDrop(animation, target.getLocation());
			animateTargetOnDrop(dragged, target);
			return true;
		}
		else {
			hideAllowedTargets(dragged, target, null);
			return false;
		}
	}
	
	protected void animateTargetOnDrop(Item dragged, Item target) {
		ItemAnimation itemAnimation = getTargetDropAnimation(target);
		if (itemAnimation==null) {
			itemAnimation = getExitTargetAnimation(target);
		}
		SimultaneousItemAnimation targetAnimation = new SimultaneousItemAnimation();
		if (itemAnimation!=null) {
			targetAnimation.addAnimation(itemAnimation);
		}
		hideAllowedTargets(dragged, target, targetAnimation);
		targetAnimation.duplicate().launchFor(target);
	}

	void hideAllowedTargets(Item dragged, Item target,
			SimultaneousItemAnimation targetAnimation) 
	{
		for (Item aTarget : targets) {
			if (acceptTarget(dragged, aTarget)) {
				ItemAnimation aTargetAnimation = getHideAllowedTargetAnimation(target);
				if (aTargetAnimation != null) {
					if (aTarget==target && targetAnimation!=null) {
						targetAnimation.addAnimation(aTargetAnimation);
					}
					else {
						aTargetAnimation.duplicate().launchFor(aTarget);
					}
				}
			}
		}
	}

	void showAllowedTargets(Item dragged) {
		for (Item target : targets) {
			if (acceptTarget(dragged, target)) {
				ItemAnimation targetAnimation = getShowAllowedTargetAnimation(target);
				if (targetAnimation != null) {
					targetAnimation.duplicate().launchFor(target);
				}
			}
		}
	}
	
	protected abstract ItemAnimation getTargetDropAnimation(Item target);

	protected abstract ItemAnimation getEnterTargetAnimation(Item target);
	
	protected abstract ItemAnimation getShowAllowedTargetAnimation(Item target);

	protected abstract ItemAnimation getHideAllowedTargetAnimation(Item target);

	protected abstract ItemAnimation getExitTargetAnimation(Item target);
	
	protected boolean executeDrop(Item dragged, Item target) {
		return true;
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

