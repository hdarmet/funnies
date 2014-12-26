package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.items.AbstractImageItem;
import com.ithaque.funnies.shared.basic.items.animations.easing.SineInOutEasing;

public class FaceFadingAnimation extends SoftenAnimation {

	float baseOpacity;
	Integer index = null;
	Float targetOpacity;
	
	public FaceFadingAnimation(Easing easing, Integer index, Float targetOpacity) {
		super(easing);
		this.index = index;
		this.targetOpacity = targetOpacity;
	}
	
	public FaceFadingAnimation(long duration, int index, Float targetOpacity) {
		this(new SineInOutEasing(duration), index, targetOpacity);
	}

	public FaceFadingAnimation(long duration, Float targetOpacity) {
		this(duration, 0, targetOpacity);
	}
	
	@Override
	public boolean start(long time) {
		boolean result = super.start(time);
		if (result) {
			baseOpacity = getOpacity();
		}
		return result;
	}
	
	@Override
	public AbstractImageItem getItem() {
		return (AbstractImageItem)super.getItem();
	}

	@Override
	protected boolean executeAnimation(long time) {
		setOpacity(getEasing().getValue(baseOpacity, targetOpacity));
		return true;
	}

	@Override
	public void finish(long time) {
		setOpacity(targetOpacity);
		super.finish(time);
	}

	float getOpacity() {
		return getItem().getOpacity(index);
	}
	
	void setOpacity(float opacity) {
		getItem().setOpacity(index, opacity);
	}

	public static class Builder extends SoftenAnimation.Builder {
		
		Integer index = null;
		float targetOpacity;
		
		public Builder(Easing.Factory easing, Integer index, float targetOpacity) {
			super(easing);
			this.index = index;
			this.targetOpacity = targetOpacity;
		}
		
		public Builder(long duration, int index, float targetOpacity) {
			this(new SineInOutEasing.Builder(duration), index, targetOpacity);
		}
		
		public Builder(long duration, float targetOpacity) {
			this(new SineInOutEasing.Builder(duration), 0, targetOpacity);
		}

		@Override
		public FaceFadingAnimation create() {
			FaceFadingAnimation animation = new FaceFadingAnimation(easing.create(), index, targetOpacity);
			prepare(animation);
			return animation;
		}

	}

}
