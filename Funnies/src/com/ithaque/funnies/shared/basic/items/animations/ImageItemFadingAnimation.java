package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.AnimationContext;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.animations.easing.SineInOutEasing;

public class ImageItemFadingAnimation extends SoftenAnimation {

	float baseOpacity;
	Integer index = null;
	String url = null;
	Float targetOpacity;
	
	public ImageItemFadingAnimation(Easing easing, int index, Float targetOpacity) {
		this(easing, index, null, targetOpacity);
	}
	
	public ImageItemFadingAnimation(Easing easing, String url, Float targetOpacity) {
		this(easing, null, url, targetOpacity);
	}
	
	protected ImageItemFadingAnimation(Easing easing, Integer index, String url, Float targetOpacity) {
		super(easing);
		this.url = url;
		this.index = index;
		this.targetOpacity = targetOpacity;
	}
	
	public ImageItemFadingAnimation(long duration, int index, Float targetOpacity) {
		this(new SineInOutEasing(duration), index, null, targetOpacity);
	}
	
	public ImageItemFadingAnimation(long duration, String url, Float targetOpacity) {
		this(new SineInOutEasing(duration), null, url, targetOpacity);
	}
	
	public Integer getIndex(ImageItem item) {
		if (index==null) {
			index = item.getIndex(url);
		}
		return index;
	}

	@Override
	public boolean start(long time, AnimationContext context) {
		boolean result = super.start(time, context);
		if (result) {
			baseOpacity = getOpacity();
		}
		return result;
	}
	
	@Override
	public ImageItem getItem() {
		return (ImageItem)super.getItem();
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
		return getItem().getOpacity(getIndex(getItem()));
	}
	
	void setOpacity(float opacity) {
		getItem().setOpacity(getIndex(getItem()), opacity);
	}

	public static class Builder extends SoftenAnimation.Builder {
		
		String url = null;
		Integer index = null;
		float targetOpacity;
		
		protected Builder(Easing.Factory easing, Integer index, String url, float targetOpacity) {
			super(easing);
			this.url = url;
			this.index = index;
			this.targetOpacity = targetOpacity;
		}
		
		public Builder(Easing.Factory easing, int index, float targetOpacity) {
			this(easing, index, null, targetOpacity);
		}
		
		public Builder(Easing.Factory easing, String url, float targetOpacity) {
			this(easing, null, url, targetOpacity);
		}
		
		public Builder(long duration, int index, float targetOpacity) {
			this(new SineInOutEasing.Builder(duration), index, null, targetOpacity);
		}
		
		public Builder(long duration, String url, float targetOpacity) {
			this(new SineInOutEasing.Builder(duration), null, url, targetOpacity);
		}
		
		@Override
		public ImageItemFadingAnimation create() {
			ImageItemFadingAnimation animation = new ImageItemFadingAnimation(easing.create(), index, url, targetOpacity);
			prepare(animation);
			return animation;
		}

	}

}
