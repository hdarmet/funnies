package com.ithaque.funnies.shared.basic.processors;

import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.items.animations.ItemAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ItemMoveAnimation;

public class SimpleTargetedDragProfile extends AbstractTargetedDragProfile {

	ItemAnimation enterTargetAnimation = null;
	ItemAnimation showAllowedTargetAnimation = null;
	ItemAnimation hideAllowedTargetAnimation = null;
	ItemAnimation exitTargetAnimation = null;
	ItemAnimation targetDropAnimation = null;
	ItemAnimation beginDragAnimation = null;
	ItemMoveAnimation adjustLocationAnimation = null;
	ItemAnimation draggedDropAnimation = null;
	
	public void setBeginDragAnimation(ItemAnimation beginDragAnimation) {
		this.beginDragAnimation = beginDragAnimation;
	}

	public void setAdjustLocationAnimation(ItemMoveAnimation adjustLocationAnimation) {
		this.adjustLocationAnimation = adjustLocationAnimation;
	}

	public void setEnterTargetAnimation(ItemAnimation enterTargetAnimation) {
		this.enterTargetAnimation = enterTargetAnimation;
	}

	public void setExitTargetAnimation(ItemAnimation exitTargetAnimation) {
		this.exitTargetAnimation = exitTargetAnimation;
	}

	public void setTargetDropAnimation(ItemAnimation targetDropAnimation) {
		this.targetDropAnimation = targetDropAnimation;
	}

	public void setDraggedDropAnimation(ItemAnimation draggedDropAnimation) {
		this.draggedDropAnimation = draggedDropAnimation;
	}

	public void setShowAllowedTargetAnimation(ItemAnimation showAllowedTargetAnimation) {
		this.showAllowedTargetAnimation = showAllowedTargetAnimation;
	}

	public void setHideAllowedTargetAnimation(ItemAnimation hideAllowedTargetAnimation) {
		this.hideAllowedTargetAnimation = hideAllowedTargetAnimation;
	}
	
	@Override
	protected ItemAnimation getTargetDropAnimation(Item target) {
		return targetDropAnimation;
	}

	@Override
	protected ItemAnimation getEnterTargetAnimation(Item target) {
		return enterTargetAnimation;
	}

	@Override
	protected ItemAnimation getShowAllowedTargetAnimation(Item target) {
		return showAllowedTargetAnimation;
	}
	
	@Override
	protected ItemAnimation getHideAllowedTargetAnimation(Item target) {
		return hideAllowedTargetAnimation;
	}
	
	@Override
	protected ItemAnimation getExitTargetAnimation(Item target) {
		return exitTargetAnimation;
	}

	@Override
	protected ItemAnimation getBeginDragAnimation(Item dragged) {
		return beginDragAnimation;
	}

	@Override
	protected ItemMoveAnimation getAdjustLocationAnimation(Item dragged) {
		return adjustLocationAnimation;
	}

	@Override
	protected ItemAnimation getDraggedDropAnimation(Item dragged) {
		return draggedDropAnimation;
	}
	
}
