package com.ithaque.funnies.shared.funny;

import java.util.HashMap;
import java.util.Map;

import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.items.animations.ItemAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ItemMoveAnimation;
import com.ithaque.funnies.shared.basic.processors.AbstractTargetedDragProfile;
import com.ithaque.funnies.shared.funny.notifications.AcceptDropTargetQuestion;
import com.ithaque.funnies.shared.funny.notifications.DropEvent;

public class CircusDnDProfile extends AbstractTargetedDragProfile {

	Map<Item, DraggableFunny> draggableFunnies = new HashMap<Item, DraggableFunny>();
	Map<Item, DropTargetFunny> targetFunnies = new HashMap<Item, DropTargetFunny>();
	Ring ring;
	
	public CircusDnDProfile(Ring ring) {
		this.ring = ring;
	}
	
	public void registerDraggableFunny(DraggableFunny funny) {
		for (Item item : funny.getDraggableItems()) {
			draggableFunnies.put(item, funny);
			addDraggeable(item);
		}
	}
	
	protected DraggableFunny getDraggableFunny(Item item) {
		return draggableFunnies.get(item);
	}

	public void registerDroppableFunny(DropTargetFunny funny) {
		for (Item item : funny.getDropTargetItems()) {
			targetFunnies.put(item, funny);
			addTarget(item);
		}
	}
	
	protected DropTargetFunny getDropTargetFunny(Item item) {
		return targetFunnies.get(item);
	}
	
	@Override
	protected Item getHilightItem(Item target) {
		DropTargetFunny funny = getDropTargetFunny(target);
		return funny.getHilightItem(target);
	}
	
	@Override
	protected ItemAnimation getTargetDropAnimation(Item target) {
		DropTargetFunny funny = getDropTargetFunny(target);
		return funny.getTargetDropAnimation();
	}

	@Override
	protected ItemAnimation getEnterTargetAnimation(Item target) {
		DropTargetFunny funny = getDropTargetFunny(target);
		return funny.getEnterTargetAnimation();
	}

	@Override
	protected ItemAnimation getExitTargetAnimation(Item target) {
		DropTargetFunny funny = getDropTargetFunny(target);
		return funny.getExitTargetAnimation();
	}

	@Override
	protected ItemAnimation getShowAllowedTargetAnimation(Item target) {
		DropTargetFunny targetFunny = getDropTargetFunny(target);
		return targetFunny.getShowAllowedTargetAnimation();
	}
	
	@Override
	protected ItemAnimation getHideAllowedTargetAnimation(Item target) {
		DropTargetFunny targetFunny = getDropTargetFunny(target);
		return targetFunny.getHideAllowedTargetAnimation();
	}
	
	@Override
	protected ItemAnimation getBeginDragAnimation(Item dragged) {
		DraggableFunny funny = getDraggableFunny(dragged);
		return funny.getBeginDragAnimation();
	}

	@Override
	protected ItemMoveAnimation getAdjustLocationAnimation(Item dragged) {
		DraggableFunny funny = getDraggableFunny(dragged);
		return funny.getAdjustLocationAnimation();
	}

	@Override
	protected ItemAnimation getDraggedDropAnimation(Item dragged) {
		DraggableFunny funny = getDraggableFunny(dragged);
		return funny.getDraggedDropAnimation();
	}
	
	@Override
	protected void executeDrop(Item dragged, Item target) {
		DraggableFunny draggableFunny = getDraggableFunny(dragged);
		DropTargetFunny targetFunny = getDropTargetFunny(target);
		ring.notify(new DropEvent(draggableFunny, targetFunny));
	}

	@Override
	protected boolean acceptTarget(Item dragged, Item target) {
		DraggableFunny draggableFunny = getDraggableFunny(dragged);
		DropTargetFunny targetFunny = getDropTargetFunny(target);
		AcceptDropTargetQuestion question = new AcceptDropTargetQuestion(draggableFunny, targetFunny);
		ring.notify(question);
		return question.isAccepted();
	}

}
