package com.ithaque.funnies.shared.basic.processors;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.items.animations.ItemMoveAnimation;

public class SimpleTargetedDragProfile extends AbstractTargetedDragProfile {

	Animation.Factory enterTargetAnimation = null;
	Animation.Factory showAllowedTargetAnimation = null;
	Animation.Factory hideAllowedTargetAnimation = null;
	Animation.Factory exitTargetAnimation = null;
	Animation.Factory targetDropAnimation = null;
	Animation.Factory beginDragAnimation = null;
	ItemMoveAnimation.Builder adjustLocationAnimation = null;
	Animation.Factory draggedDropAnimation = null;
	
	public void setBeginDragAnimation(Animation.Factory beginDragAnimation) {
		this.beginDragAnimation = beginDragAnimation;
	}

	public void setAdjustLocationAnimation(ItemMoveAnimation.Builder adjustLocationAnimation) {
		this.adjustLocationAnimation = adjustLocationAnimation;
	}

	public void setEnterTargetAnimation(Animation.Factory enterTargetAnimation) {
		this.enterTargetAnimation = enterTargetAnimation;
	}

	public void setExitTargetAnimation(Animation.Factory exitTargetAnimation) {
		this.exitTargetAnimation = exitTargetAnimation;
	}

	public void setTargetDropAnimation(Animation.Factory targetDropAnimation) {
		this.targetDropAnimation = targetDropAnimation;
	}

	public void setDraggedDropAnimation(Animation.Factory draggedDropAnimation) {
		this.draggedDropAnimation = draggedDropAnimation;
	}

	public void setShowAllowedTargetAnimation(Animation.Factory showAllowedTargetAnimation) {
		this.showAllowedTargetAnimation = showAllowedTargetAnimation;
	}

	public void setHideAllowedTargetAnimation(Animation.Factory hideAllowedTargetAnimation) {
		this.hideAllowedTargetAnimation = hideAllowedTargetAnimation;
	}
	
	@Override
	protected Animation.Factory getTargetDropAnimation(Item target) {
		return targetDropAnimation;
	}

	@Override
	protected Animation.Factory getEnterTargetAnimation(Item target) {
		return enterTargetAnimation;
	}

	@Override
	protected Animation.Factory getShowAllowedTargetAnimation(Item target) {
		return showAllowedTargetAnimation;
	}
	
	@Override
	protected Animation.Factory getHideAllowedTargetAnimation(Item target) {
		return hideAllowedTargetAnimation;
	}
	
	@Override
	protected Animation.Factory getExitTargetAnimation(Item target) {
		return exitTargetAnimation;
	}

	@Override
	protected Animation.Factory getBeginDragAnimation(Item dragged) {
		return beginDragAnimation;
	}

	@Override
	protected ItemMoveAnimation.Builder getAdjustLocationAnimation(Item dragged) {
		return adjustLocationAnimation;
	}

	@Override
	protected Animation.Factory getDraggedDropAnimation(Item dragged) {
		return draggedDropAnimation;
	}
	
}
