package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.items.ImageItem;

public class ChangeFaceAnimation extends ItemAnimation {

	String url = null;
	Integer targetIndex = null;
	Integer baseIndex = null;

	public ChangeFaceAnimation(Easing easing, String url, Integer index) {
		super(easing);
		this.url = url;
		this.targetIndex = index;
	}

	public ChangeFaceAnimation(long duration, String url, Integer index) {
		this(new SineInOutEasing(duration), url, index);
	}
	
	@Override
	protected void launch(Item item) {
		super.launch(item);
		if (url!=null) {
			targetIndex = ((ImageItem)item).getIndex(url);
		}
		baseIndex = getCurrentIndex((ImageItem)item);
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

	private ImageItem getImageItem() {
		return (ImageItem)item;
	}

	@Override
	protected boolean executeAnimation(Easing easing, long time) {
		if (targetIndex<baseIndex) {
			setOpacity(baseIndex, easing.getValue(1.0f, 0.0f));
		}
		else if (targetIndex>baseIndex) {
			setOpacity(targetIndex, 1.0f-easing.getValue(1.0f, 0.0f));
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

	@Override
	public ItemAnimation duplicate() {
		return new ChangeFaceAnimation(easing.duplicate(), this.url, this.targetIndex);
	}
	
	void setOpacity(int imageIndex, float opacity) {
		getImageItem().setOpacity(imageIndex, opacity);
	}
	
}
