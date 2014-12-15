package com.ithaque.funnies.shared.basic.items.animations;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Item;

public class ParallelItemAnimation extends Animation {

	Long duration = null;
	long endTime = 0;
	
	public ParallelItemAnimation() {
		super();
	}

	List<Animation> animations = new ArrayList<Animation>();
	
	public void setItem(Item item) {
		super.setItem(item);
		if (!animations.isEmpty()) {
			for (Animation child : animations) {
				child.setItem(item);
			}
		}
	}
	
	@Override
	public void launch() {
		System.out.println("start //");
		if (!animations.isEmpty()) {
			registerOnBoard(getItem());
			for (Animation child : animations) {
				child.launch();
			}
			endTime = getItem().getBoard().getTime()+getDuration();
		}
	}
	
	@Override
	public long getEndTime() {
		return endTime;
	}

	@Override
	public long getDuration() {
		if (duration==null) {
			for (Animation child : animations) {
				duration = 0L;
				if (child.getDuration()>duration) {
					duration = child.getDuration();
				}
			}
		}
		return duration;
	}
	
	@Override
	protected boolean executeAnimation(long time) {
		System.out.println("animate //");
		for (Animation child : new ArrayList<Animation>(animations)) {
			if (!child.animate(time)) {
				child.finish(time);
				animations.remove(child);
			}
		}
		System.out.println("end animate //");
		return true;
	}
	
	@Override
	public void finish(long time) {
		for (Animation child : animations) {
			child.finish(time);
		}		
		super.finish(time);
		System.out.println("finish //");
	}
	
	public void addAnimation(Animation animation) {
		animation.manage();
		animations.add(animation);
	}
	
	public static class Builder implements Factory {

		List<Animation.Factory> animations = new ArrayList<Animation.Factory>();
		
		public Builder() {
			super();
		}

		public void addAnimation(Animation.Factory animation) {
			animations.add(animation);
		}
		
		@Override
		public ParallelItemAnimation create() {
			ParallelItemAnimation animation = new ParallelItemAnimation();
			for (Animation.Factory child : animations) {
				Animation childAnimation = child.create();
				animation.addAnimation(childAnimation);
			}
			return animation;
		}	

	}


}
