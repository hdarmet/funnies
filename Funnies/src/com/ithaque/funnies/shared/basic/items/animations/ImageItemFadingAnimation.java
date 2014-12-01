package com.ithaque.funnies.shared.basic.items.animations;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.items.ImageItem;

public class ImageItemFadingAnimation extends ItemAnimation {

	class FacetChange {
		public FacetChange(Integer index, String url, float opacity) {
			this.url = url;
			this.index = index;
			this.targetOpacity = opacity;
		}

		String url = null;
		Integer index = null;
		float baseOpacity;
		float targetOpacity;
	}
	
	List<FacetChange> facetChanges = new ArrayList<FacetChange>();

	public ImageItemFadingAnimation(Easing easing) {
		super(easing);
	}

	public ImageItemFadingAnimation(long duration) {
		this(new SineInOutEasing(duration));
	}

	
	ImageItemFadingAnimation fade(Integer index, String url, float opacity) {
		facetChanges.add(new FacetChange(index, url, opacity));
		return this;
	}
	
	public ImageItemFadingAnimation fade(String url, float opacity) {
		facetChanges.add(new FacetChange(null, url, opacity));
		return this;
	}
	
	public ImageItemFadingAnimation fade(Integer index, float opacity) {
		facetChanges.add(new FacetChange(index, null, opacity));
		return this;
	}
	
	@Override
	protected void launch(Item item) {
		super.launch(item);
		for (FacetChange change : facetChanges) {
			change.baseOpacity = getOpacity(change);
		}
	}
	
	private ImageItem getImageItem() {
		return (ImageItem)item;
	}

	@Override
	protected boolean executeAnimation(Easing easing, long time) {
		for (FacetChange change : facetChanges) {
			change.baseOpacity = getOpacity(change);
			setOpacity(change, easing.getValue(change.baseOpacity, change.targetOpacity));
		}
		return true;
	}

	@Override
	public void finish(long time) {
		for (FacetChange change : facetChanges) {
			change.baseOpacity = getOpacity(change);
			setOpacity(change, change.targetOpacity);
		}
		super.finish(time);
	}

	@Override
	public ItemAnimation duplicate() {
		ImageItemFadingAnimation animation =  new ImageItemFadingAnimation(easing);
		for (FacetChange change : facetChanges) {
			animation.fade(change.index, change.url, change.targetOpacity);
		}
		return animation;
	}

	float getOpacity(FacetChange facet) {
		if (facet.index!=null) {
			return getImageItem().getOpacity(facet.index);
		}
		else {
			return getImageItem().getOpacity(facet.url);
		}
	}
	
	void setOpacity(FacetChange facet, float opacity) {
		if (facet.index!=null) {
			getImageItem().setOpacity(facet.index, opacity);
		}
		else {
			getImageItem().setOpacity(facet.url, opacity);
		}
	}
}
