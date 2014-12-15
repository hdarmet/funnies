package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.items.ImageItem;

public class ChangeFaceAnimation extends ItemAnimation {

	Integer baseIndex = null;
	Integer targetIndex = null;
	String url = null;

	public ChangeFaceAnimation(Easing easing, Integer targetIndex, String url) {
		super(easing);
		this.targetIndex = targetIndex;
		this.url = url;
	}
	
	public ChangeFaceAnimation(long duration, String url) {
		this(new SineInOutEasing(duration), null, url);
	}
	
	public ChangeFaceAnimation(long duration, Integer index) {
		this(new SineInOutEasing(duration), index, null);
	}
	
	@Override
	public void launch() {
		super.launch();
		baseIndex = getCurrentIndex(getItem());
		if (targetIndex==null) {
			targetIndex = getItem().getIndex(url);
		}
		if (targetIndex<baseIndex) {
			getItem().setOpacity(targetIndex, 1.0f);
		}
	}
	
	Integer getCurrentIndex(ImageItem item) {
		for (int index=0; index<item.getImageCount(); index++) {
			if (item.getOpacity(index)==1.0f) {
				return index;
			}
		}
		return null;
	}

	@Override
	protected boolean executeAnimation(long time) {
		if (targetIndex<baseIndex) {
			setOpacity(baseIndex, getEasing().getValue(1.0f, 0.0f));
		}
		else if (targetIndex>baseIndex) {
			setOpacity(targetIndex, 1.0f-getEasing().getValue(1.0f, 0.0f));
		}
		return true;
	}

	@Override
	public ImageItem getItem() {
		return (ImageItem)super.getItem();
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
	
	public static class Builder implements Factory {
		Easing.Factory easing;
		String url = null;
		Integer targetIndex = null;
		
		public Builder(Easing.Factory easing, String url) {
			super();
			this.easing = easing;
			this.url = url;
		}

		public Builder(Easing.Factory easing, Integer targetIndex) {
			super();
			this.easing = easing;
			this.targetIndex = targetIndex;
		}

		public Builder(long duration, String url) {
			this(new SineInOutEasing.Builder(duration), url);
		}
		
		public Builder(long duration, Integer index) {
			this(new SineInOutEasing.Builder(duration), index);
		}
		
		@Override
		public ChangeFaceAnimation create() {
			return new ChangeFaceAnimation(easing.create(), targetIndex, url);
		}	

	}
}
