package com.ithaque.funnies.shared.basic.items.animations;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.basic.AbstractAnimation;
import com.ithaque.funnies.shared.basic.Animation;

public class ParallelAnimation extends AbstractAnimation implements CompositeAnimation {

	Long duration = null;
	long endTime = 0;
	
	public ParallelAnimation() {
		super();
	}

	List<Animation> animations = new ArrayList<Animation>();
	
	@Override
	public boolean start(long time) {
		boolean result = super.start(time);
		if (result) {
			if (!animations.isEmpty()) {
				for (Animation child : animations) {
					if (child.getContext()==null) {
						child.setContext(getContext());
					}
					child.start(time);
				}
				endTime = time+getDuration();
			}
		}
		return result;
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
		for (Animation child : new ArrayList<Animation>(animations)) {
			if (!child.animate(time)) {
				child.finish(time);
				animations.remove(child);
			}
		}
		return true;
	}
	
	@Override
	public void finish(long time) {
		for (Animation child : animations) {
			child.finish(time);
		}		
		super.finish(time);
	}
	
	@Override
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
		public ParallelAnimation create() {
			ParallelAnimation animation = new ParallelAnimation();
			for (Animation.Factory child : animations) {
				Animation childAnimation = child.create();
				animation.addAnimation(childAnimation);
			}
			return animation;
		}	

	}


}
