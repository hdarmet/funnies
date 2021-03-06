package com.ithaque.funnies.shared.basic.processors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.items.animations.SoftenAnimation;

public class SimpleTargetedDragProfile extends AbstractTargetedDragProfile {

	SoftenAnimation.Builder enterTargetAnimation = null;
	SoftenAnimation.Builder showAllowedTargetAnimation = null;
	SoftenAnimation.Builder hideAllowedTargetAnimation = null;
	SoftenAnimation.Builder exitTargetAnimation = null;
	SoftenAnimation.Builder targetDropAnimation = null;
	SoftenAnimation.Builder beginDragAnimation = null;
	SoftenAnimation.Builder adjustLocationAnimation = null;
	SoftenAnimation.Builder draggedDropAnimation = null;

	List<Item> draggeables = new ArrayList<Item>();
	List<Item> targets = new ArrayList<Item>();

	public SimpleTargetedDragProfile(Board board) {
		super(board);
	}
	
	@Override
	protected boolean acceptDraggeable(Item draggeable) {
		return draggeables.contains(draggeable);
	}
	
	public void addDraggeable(Item draggeable) {
		if (draggeable==null) {
			throw new NullPointerException();
		}
		draggeables.add(draggeable);
	}
	
	public void removeDraggeable(Item draggeable) {
		if (draggeable==null) {
			throw new NullPointerException();
		}		
		draggeables.remove(draggeable);
	}
	
	public void addTarget(Item target) {
		if (target==null) {
			throw new NullPointerException();
		}
		targets.add(target);
	}
	
	public void removeTarget(Item target) {
		if (target==null) {
			throw new NullPointerException();
		}
		targets.remove(target);
	}
	
	protected Collection<Item> getTargets() {
		return targets;
	}

	public void setBeginDragAnimation(SoftenAnimation.Builder beginDragAnimation) {
		this.beginDragAnimation = beginDragAnimation;
	}

	public void setAdjustLocationAnimation(SoftenAnimation.Builder adjustLocationAnimation) {
		this.adjustLocationAnimation = adjustLocationAnimation;
	}

	public void setEnterTargetAnimation(SoftenAnimation.Builder enterTargetAnimation) {
		this.enterTargetAnimation = enterTargetAnimation;
	}

	public void setExitTargetAnimation(SoftenAnimation.Builder exitTargetAnimation) {
		this.exitTargetAnimation = exitTargetAnimation;
	}

	public void setTargetDropAnimation(SoftenAnimation.Builder targetDropAnimation) {
		this.targetDropAnimation = targetDropAnimation;
	}

	public void setDraggedDropAnimation(SoftenAnimation.Builder draggedDropAnimation) {
		this.draggedDropAnimation = draggedDropAnimation;
	}

	public void setShowAllowedTargetAnimation(SoftenAnimation.Builder showAllowedTargetAnimation) {
		this.showAllowedTargetAnimation = showAllowedTargetAnimation;
	}

	public void setHideAllowedTargetAnimation(SoftenAnimation.Builder hideAllowedTargetAnimation) {
		this.hideAllowedTargetAnimation = hideAllowedTargetAnimation;
	}
	
	@Override
	protected SoftenAnimation.Builder getTargetDropAnimation(Item target) {
		return targetDropAnimation;
	}

	@Override
	protected SoftenAnimation.Builder getEnterTargetAnimation(Item target) {
		return enterTargetAnimation;
	}

	@Override
	protected SoftenAnimation.Builder getShowAllowedTargetAnimation(Item target) {
		return showAllowedTargetAnimation;
	}
	
	@Override
	protected SoftenAnimation.Builder getHideAllowedTargetAnimation(Item target) {
		return hideAllowedTargetAnimation;
	}
	
	@Override
	protected SoftenAnimation.Builder getExitTargetAnimation(Item target) {
		return exitTargetAnimation;
	}

	@Override
	protected SoftenAnimation.Builder getBeginDragAnimation(Item dragged) {
		return beginDragAnimation;
	}

	@Override
	protected SoftenAnimation.Builder getAdjustLocationAnimation(Item dragged) {
		return adjustLocationAnimation;
	}

	@Override
	protected SoftenAnimation.Builder getDraggedDropAnimation(Item dragged) {
		return draggedDropAnimation;
	}
	
}
