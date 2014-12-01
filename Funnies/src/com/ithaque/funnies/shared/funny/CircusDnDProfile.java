package com.ithaque.funnies.shared.funny;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.items.animations.ItemAnimation;
import com.ithaque.funnies.shared.basic.processors.DragProcessor.AbstractTargetedDragProfile;
import com.ithaque.funnies.shared.funny.manager.DropRequest;

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
	protected ItemAnimation getBeginDragAnimation(Item dragged) {
		DraggableFunny funny = getDraggableFunny(dragged);
		return funny.getBeginDragAnimation();
	}

	@Override
	protected ItemAnimation getDropAnimation(Item dragged) {
		DraggableFunny funny = getDraggableFunny(dragged);
		return funny.getDropAnimation();
	}

	@Override
	protected boolean executeDrop(Item dragged, Item target) {
		DraggableFunny draggableFunny = getDraggableFunny(dragged);
		DropTargetFunny targetFunny = getDropTargetFunny(target);
		ring.sendRequest(new DropRequest(draggableFunny.getId(), targetFunny.getId()));
		return true;
	}

	@Override
	protected boolean acceptTarget(Item target) {
		return true;
	}

}
