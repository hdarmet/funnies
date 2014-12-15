package com.ithaque.funnies.shared.basic.items.animations;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.items.ImageItem;

public class ImageItemFadingAnimation extends ItemAnimation {

	static class FacetChange {
		public FacetChange(Integer index, String url, Float targetOpacity) {
			this.index = index;
			this.url = url;
			this.targetOpacity = targetOpacity;
		}

		float baseOpacity;
		Integer index;
		String url;
		Float targetOpacity;
		
		public Integer getIndex(ImageItem item) {
			if (index==null) {
				index = item.getIndex(url);
			}
			return index;
		}
	}

	List<FacetChange> facetChanges = new ArrayList<FacetChange>();

	public ImageItemFadingAnimation(Easing easing) {
		super(easing);
	}
	
	@Override
	public void launch() {
		super.launch();
		for (FacetChange change : facetChanges) {
			change.baseOpacity = getOpacity(change);
		}
	}
	
	@Override
	public ImageItem getItem() {
		return (ImageItem)super.getItem();
	}

	@Override
	protected boolean executeAnimation(long time) {
		for (FacetChange change : facetChanges) {
			setOpacity(change, getEasing().getValue(change.baseOpacity, change.targetOpacity));
		}
		return true;
	}

	@Override
	public void finish(long time) {
		for (FacetChange change : facetChanges) {
			setOpacity(change, change.targetOpacity);
		}
		super.finish(time);
	}

	float getOpacity(FacetChange facet) {
		return getItem().getOpacity(facet.getIndex(getItem()));
	}
	
	void setOpacity(FacetChange facet, float opacity) {
		getItem().setOpacity(facet.getIndex(getItem()), opacity);
	}

	public ImageItemFadingAnimation fade(String url, float opacity) {
		facetChanges.add(new FacetChange(null, url, opacity));
		return this;
	}
	
	public ImageItemFadingAnimation fade(Integer index, float opacity) {
		facetChanges.add(new FacetChange(index, null, opacity));
		return this;
	}
	
	public void addFacetChange(FacetChange facetChange) {
		facetChanges.add(facetChange);
	}

	public static class Builder implements Factory {
		
		List<FacetChangeBuilder> facetChanges = new ArrayList<FacetChangeBuilder>();
		Easing.Factory easing;

		public Builder(Easing.Factory easing) {
			super();
			this.easing = easing;
		}

		public Builder(long duration) {
			this(new SineInOutEasing.Builder(duration));
		}
		
		public ImageItemFadingAnimation.Builder fade(String url, float opacity) {
			facetChanges.add(new FacetChangeBuilder(null, url, opacity));
			return this;
		}
		
		public ImageItemFadingAnimation.Builder fade(Integer index, float opacity) {
			facetChanges.add(new FacetChangeBuilder(index, null, opacity));
			return this;
		}
		
		@Override
		public Animation create() {
			ImageItemFadingAnimation animation = new ImageItemFadingAnimation(easing.create());
			for (FacetChangeBuilder fcBuilder : facetChanges) {
				animation.addFacetChange(fcBuilder.create());
			}
			return animation;
		}	

	}

	static class FacetChangeBuilder {
		
		public FacetChangeBuilder(Integer index, String url, float opacity) {
			this.url = url;
			this.index = index;
			this.targetOpacity = opacity;
		}

		FacetChange create() {
			return new FacetChange(index, url, targetOpacity);
		}
		
		String url = null;
		Integer index = null;
		float targetOpacity;		
	}

}
