package com.ithaque.funnies.shared.basic.items.animations;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.basic.AbstractAnimation;
import com.ithaque.funnies.shared.basic.Animation;

public class SequenceAnimation extends AbstractAnimation implements CompositeAnimation {

	public SequenceAnimation() {
	}

	List<Animation> animations = new ArrayList<Animation>();
	Animation currentChild;
	Long duration = null;
	int index = 0;
	long endTime;
	
	@Override
	public boolean start(long time) {
		boolean result = super.start(time);
		if (result) {
			for (Animation child : animations) {
				child.setContext(getContext());
			}
			if (index<animations.size()) {
				endTime = time+getDuration();
				currentChild = animations.get(index++);
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
			if (index>=animations.size()) {
				return false;
			}
			else {
				currentChild = animations.get(index++);
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

	@Override
	public void addAnimation(Animation animation) {
		animations.add(animation);
	}
	
	@Override
	public void reset() {
		index=0;
		for (Animation animation : animations) {
			animation.reset();
		}
		super.reset();
	}
	
	public static class Builder implements Factory {

		List<Animation.Factory> animations = new ArrayList<Animation.Factory>();
		
		public Builder() {
			super();
		}

		public Builder addAnimation(Animation.Factory animation) {
			animations.add(animation);
			return this;
		}
		
		@Override
		public SequenceAnimation create() {
			SequenceAnimation animation = new SequenceAnimation();
			for (Animation.Factory child : animations) {
				Animation childAnimation = child.create();
				animation.addAnimation(childAnimation);
			}
			return animation;
		}	

	}
}
