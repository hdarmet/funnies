package com.ithaque.funnies.shared.basic.items.animations;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.basic.Item;

public class SequenceItemAnimation extends ItemAnimation {

	public SequenceItemAnimation() {
		super(null);
	}

	List<ItemAnimation> animations = new ArrayList<ItemAnimation>();
	ItemAnimation currentChild;
	Long duration = null;
	long endTime;
	
	protected void launch(Item item) {
		if (!animations.isEmpty()) {
			registerOnBoard(item);
			endTime = item.getBoard().getTime()+getDuration();
			currentChild = animations.remove(0);
			currentChild.launch(item);
		}
	}
	
	@Override
	public long getDuration() {
		if (duration==null) {
			duration = 0L;
			for (ItemAnimation child : animations) {
				duration += child.getDuration();
			}
		}
		return duration;
	}
	
	@Override
	protected boolean executeAnimation(Easing easing, long time) {
		if (currentChild==null) {
			return false;
		}
		if (!currentChild.animate(time)) {
			currentChild.finish(time);
			if (animations.isEmpty()) {
				return false;
			}
			else {
				currentChild = animations.remove(0);
				currentChild.launch(item);
			}
		}
		return true;
	}

	@Override
	public ItemAnimation duplicate() {
		SequenceItemAnimation animation = new SequenceItemAnimation();
		for (ItemAnimation child : animations) {
			animation.addAnimation(child.duplicate());
		}
		return animation;
	}

	public void addAnimation(ItemAnimation animation) {
		animation.manage();
		animations.add(animation);
	}
	
	@Override
	public void finish(long time) {
		if (currentChild!=null) {
			currentChild.finish(time);
		}
		for (ItemAnimation child : animations) {
			child.finish(time);
		}		
		super.finish(time);
	}
	
	@Override
	public long getEndTime() {
		return endTime;
	}

}
