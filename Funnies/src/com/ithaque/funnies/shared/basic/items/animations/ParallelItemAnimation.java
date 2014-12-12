package com.ithaque.funnies.shared.basic.items.animations;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.basic.Item;

public class ParallelItemAnimation extends ItemAnimation {

	Long duration = null;
	long endTime = 0;
	
	public ParallelItemAnimation() {
		super();
	}

	List<ItemAnimation> animations = new ArrayList<ItemAnimation>();
	
	protected void launch(Item item) {
		if (!animations.isEmpty()) {
			registerOnBoard(item);
			for (ItemAnimation child : animations) {
				child.launch(item);
			}
			endTime = item.getBoard().getTime()+getDuration();
		}
	}
	
	@Override
	public long getEndTime() {
		return endTime;
	}

	@Override
	public long getDuration() {
		if (duration==null) {
			for (ItemAnimation child : animations) {
				duration = 0L;
				if (child.getDuration()>duration) {
					duration = child.getDuration();
				}
			}
		}
		return duration;
	}
	
	@Override
	protected boolean executeAnimation(Easing easing, long time) {
		for (ItemAnimation child : new ArrayList<ItemAnimation>(animations)) {
			if (!child.animate(time)) {
				child.finish(time);
				animations.remove(child);
			}
		}
		return true;
	}

	@Override
	public ItemAnimation duplicate() {
		ParallelItemAnimation animation = new ParallelItemAnimation();
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
		for (ItemAnimation child : animations) {
			child.finish(time);
		}		
		super.finish(time);
	}
}
