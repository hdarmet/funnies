package com.ithaque.funnies.shared.basic.items.animations;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.basic.Item;

public class SimultaneousItemAnimation extends ItemAnimation {

	long endTime = 0;
	
	public SimultaneousItemAnimation() {
		super(null);
	}

	List<ItemAnimation> animations = new ArrayList<ItemAnimation>();
	
	protected void launch(Item item) {
		if (!animations.isEmpty()) {
			registerOnBoard(item);
			for (ItemAnimation child : animations) {
				child.launch(item);
				if (child.getEasing().getEndTime()>endTime) {
					this.easing = child.getEasing();
					endTime = child.getEasing().getEndTime();
				}
			}
		}
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
		SimultaneousItemAnimation animation = new SimultaneousItemAnimation();
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
