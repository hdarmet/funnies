package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.items.AbstractImageItem;
import com.ithaque.funnies.shared.basic.items.animations.easing.SineInOutEasing;

public class ChangeFaceAnimation extends SoftenAnimation {

	Integer baseIndex = null;
	Integer targetIndex = null;

	public ChangeFaceAnimation(Easing easing, Integer targetIndex) {
		super(easing);
		this.targetIndex = targetIndex;
	}
	
	public ChangeFaceAnimation(long duration, Integer index) {
		this(new SineInOutEasing(duration), index);
	}
	
	@Override
	public boolean start(long time) {
		boolean result = super.start(time);
		if (result) {
			baseIndex = getCurrentIndex(getItem());
			if (baseIndex!=null && targetIndex<baseIndex) {
				getItem().setOpacity(targetIndex, 1.0f);
			}
		}
		return result;
	}
	
	@Override
	public void reset() {
		super.reset();
		this.baseIndex = null;
	}
	
	Integer getCurrentIndex(AbstractImageItem item) {
		for (int index=0; index<item.getImageCount(); index++) {
			if (item.getOpacity(index)==1.0f) {
				return index;
			}
		}
		return null;
	}

	@Override
	protected boolean executeAnimation(long time) {
		if (baseIndex!=null && targetIndex<baseIndex) {
			setOpacity(baseIndex, getEasing().getValue(1.0f, 0.0f));
		}
		else if (baseIndex==null || targetIndex>baseIndex) {
			setOpacity(targetIndex, getEasing().getValue(0.0f, 1.0f));
		}
		return true;
	}

	@Override
	public AbstractImageItem getItem() {
		return (AbstractImageItem)super.getItem();
	}
	
	@Override
	public void finish(long time) {
		for (int index=0; index<getItem().getImageCount(); index++) {
			if (index!=targetIndex) {
				getItem().setOpacity(index, 0.0f);
			}
		}
		getItem().setOpacity(targetIndex, 1.0f);
		super.finish(time);
	}

	void setOpacity(int imageIndex, float opacity) {
		getItem().setOpacity(imageIndex, opacity);
	}
	
	public static class Builder extends SoftenAnimation.Builder {
		Integer targetIndex = null;
		
		public Builder(Easing.Factory easing) {
			super(easing);
		}

		public Builder(Easing.Factory easing, Integer targetIndex) {
			super(easing);
			this.targetIndex = targetIndex;
		}

		public Builder(long duration, Integer index) {
			this(new SineInOutEasing.Builder(duration), index);
		}
		
		@Override
		public ChangeFaceAnimation create() {
			ChangeFaceAnimation animation = new ChangeFaceAnimation(easing.create(), targetIndex);
			prepare(animation);
			return animation;
		}	

	}
}
