package com.ithaque.funnies.shared.basic.items.animations;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.basic.Animation;

public class SequenceItemAnimation extends Animation {

	public SequenceItemAnimation() {
	}

	List<Animation> animations = new ArrayList<Animation>();
	Animation currentChild;
	Long duration = null;
	long endTime;
	
	@Override
	public boolean start(long time) {
		boolean result = super.start(time);
		if (result) {
			if (!animations.isEmpty()) {
				endTime = time+getDuration();
				currentChild = animations.remove(0);
				if (currentChild.getContext()==null) {
					currentChild.setContext(getContext());
				}
				currentChild.start(time);
			}
		}
		return true;
	}
	
	@Override
	public long getDuration() {
		if (duration==null) {
			duration = 0L;
			for (Animation child : animations) {
				duration += child.getDuration();
			}
		}
		return duration;
	}
	
	@Override
	protected boolean executeAnimation(long time) {
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
				if (currentChild.getContext()==null) {
					currentChild.setContext(getContext());
				}
				currentChild.start(time);
			}
		}
		return true;
	}

	@Override
	public void finish(long time) {
		if (currentChild!=null) {
			currentChild.finish(time);
		}
		for (Animation child : animations) {
			child.finish(time);
		}		
		super.finish(time);
	}
	
	@Override
	public long getEndTime() {
		return endTime;
	}

	public void addAnimation(Animation animation) {
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
		public SequenceItemAnimation create() {
			SequenceItemAnimation animation = new SequenceItemAnimation();
			for (Animation.Factory child : animations) {
				Animation childAnimation = child.create();
				animation.addAnimation(childAnimation);
			}
			return animation;
		}	

	}
}
