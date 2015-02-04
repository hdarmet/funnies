package com.ithaque.funnies.shared.funny;

import java.util.Collection;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemHolder;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.processors.AbstractTargetedDragProfile;
import com.ithaque.funnies.shared.funny.notifications.AcceptDropTargetQuestion;
import com.ithaque.funnies.shared.funny.notifications.DropEvent;

public class CircusDnDProfile extends AbstractTargetedDragProfile {

	FunnyRegistry<DraggableFunny> draggableFunnies = new FunnyRegistry<DraggableFunny>(
			new FunnyRegistry.ItemsFinder<DraggableFunny>() {
				@Override
				public Item[] getItems(DraggableFunny funny) {
					return funny.getDraggableItems();
				}
			}
		) {
		@Override
		protected Record<DraggableFunny> createRecord(Item item, DraggableFunny funny) {
			return new Record<DraggableFunny>(funny, item);
		}
	};
	FunnyRegistry<DropTargetFunny> targetFunnies = new FunnyRegistry<DropTargetFunny>(
			new FunnyRegistry.ItemsFinder<DropTargetFunny>() {
				@Override
				public Item[] getItems(DropTargetFunny funny) {
					return funny.getDropTargetItems();
				}
			}
		) {
		@Override
		protected Record<DropTargetFunny> createRecord(Item item, DropTargetFunny funny) {
			return new Record<DropTargetFunny>(funny, item);
		}
	};
	AbstractRing ring;
	
	public CircusDnDProfile(AbstractRing ring) {
		super(ring.getBoard());
		this.ring = ring;
	}
	
	public void registerDraggableFunny(DraggableFunny funny) {
		draggableFunnies.registerFunny(funny);
	}
	
	public void unregisterDraggableFunny(DraggableFunny funny) {
		draggableFunnies.unregisterFunny(funny);
	}
	
	protected DraggableFunny getDraggableFunny(Item item) {
		return draggableFunnies.getFunny(item);
	}

	public void registerDroppableFunny(DropTargetFunny funny) {
		targetFunnies.registerFunny(funny);
	}
	
	public void unregisterDroppableFunny(DropTargetFunny funny) {
		targetFunnies.unregisterFunny(funny);
	}
	
	protected DropTargetFunny getDropTargetFunny(Item item) {
		return targetFunnies.getFunny(item);
	}
	
	@Override
	protected Item getHighlightItem(Item target) {
		DropTargetFunny funny = getDropTargetFunny(target);
		return funny.getHilightItem(target);
	}
	
	@Override
	protected Animation.Factory getTargetDropAnimation(Item target) {
		DropTargetFunny funny = getDropTargetFunny(target);
		return funny.getTargetDropAnimation();
	}

	@Override
	protected Animation.Factory getEnterTargetAnimation(Item target) {
		DropTargetFunny funny = getDropTargetFunny(target);
		return funny.getEnterTargetAnimation();
	}

	@Override
	protected Animation.Factory getExitTargetAnimation(Item target) {
		DropTargetFunny funny = getDropTargetFunny(target);
		return funny.getExitTargetAnimation();
	}

	@Override
	protected Animation.Factory getShowAllowedTargetAnimation(Item target) {
		DropTargetFunny targetFunny = getDropTargetFunny(target);
		return targetFunny.getShowAllowedTargetAnimation();
	}
	
	@Override
	protected Animation.Factory getHideAllowedTargetAnimation(Item target) {
		DropTargetFunny targetFunny = getDropTargetFunny(target);
		return targetFunny.getHideAllowedTargetAnimation();
	}
	
	@Override
	protected Animation.Factory getBeginDragAnimation(Item dragged) {
		DraggableFunny funny = getDraggableFunny(dragged);
		return funny.getBeginDragAnimation();
	}

	@Override
	protected Animation.Factory getAdjustLocationAnimation(Item dragged) {
		DraggableFunny funny = getDraggableFunny(dragged);
		return funny.getAdjustLocationAnimation();
	}

	@Override
	protected Animation.Factory getDraggedDropAnimation(Item dragged) {
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

	@Override
	protected Location getDropLocation(Item dragged, Item target) {
		DropTargetFunny targetFunny = getDropTargetFunny(target);
		return targetFunny.getDropLocation(dragged, target);
	}

	@Override
	protected Float getDropRotation(Item dragged, Item target) {
		Float angle = super.getDropRotation(dragged, target);
		DraggableFunny draggableFunny = getDraggableFunny(dragged);
		return draggableFunny.adjustRotation(angle);
	}

	@Override
	protected Float getDropScale(Item dragged, Item target) {
		Float scale = super.getDropScale(dragged, target);
		DraggableFunny draggableFunny = getDraggableFunny(dragged);
		return draggableFunny.adjustScale(scale);
	}

	@Override
	protected ItemHolder getDropItemHolder(Item dragged, Item target) {
		DropTargetFunny targetFunny = getDropTargetFunny(target);
		return targetFunny.getDropHolder(dragged, target);
	}

	@Override
	protected Collection<Item> getTargets() {
		return targetFunnies.getItems();
	}
	
	@Override
	protected boolean acceptDraggeable(Item draggeable) {
		return draggableFunnies.containsItem(draggeable);
	}
	
}
