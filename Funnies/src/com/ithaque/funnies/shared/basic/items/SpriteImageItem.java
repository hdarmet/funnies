package com.ithaque.funnies.shared.basic.items;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.AnimationContext.MoveableFinder;
import com.ithaque.funnies.shared.basic.Token;
import com.ithaque.funnies.shared.basic.items.animations.ChangeFaceAnimation;
import com.ithaque.funnies.shared.basic.items.animations.SequenceAnimation;

public class SpriteImageItem extends AbstractImageItem {

	public interface ImageConfiguration {
		Facet[] createFacets();
	}
	
	public static class SimpleConfiguration implements ImageConfiguration {
		float x;
		float y;
		float width;
		float height;
		
		public SimpleConfiguration(float x, float y, float width, float height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
		
		@Override
		public Facet[] createFacets() {
			return new Facet[] {new Facet(x, y, width, height)};
		}
	}
	
	public static class GridConfiguration implements ImageConfiguration {
		float x;
		float y;
		int colCount;
		int rowCount;
		float width;
		float height;
		
		public GridConfiguration(float x, float y, int colCount, int rowCount, float width, float height) {
			this.x = x;
			this.y = y;
			this.colCount = colCount;
			this.rowCount = rowCount;
			this.width = width;
			this.height = height;
		}
		
		@Override
		public Facet[] createFacets() {
			Facet[] facets = new Facet[colCount*rowCount];
			for (int col=0; col<colCount; col++) {
				for (int row=0; row<rowCount; row++) {
					facets[row*colCount+col] = new Facet(x+col*width, y+row*height, width, height);
				}
			}
			return facets;
		}
	}
	
	protected static class Facet extends AbstractFacet {
		public Facet(Float x, Float y, Float width, Float height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
		Float x = null;
		Float y = null;
		Float width = null;
		Float height = null;
	}

	Token token = null;
	String url = null;
	
	public SpriteImageItem(String url, ImageConfiguration ... configurations) {
		this.url = url;
		for (int i=0; i<configurations.length; i++) {
			Facet[] facets=configurations[i].createFacets();
			for (Facet facet : facets) {
				this.facets.add(facet);
			}
		}
	}

	public SpriteImageItem(String url, int colCount, int rowCount, float imageWidth, float imageHeight) {
		this(url, new GridConfiguration(0.0f, 0.0f, colCount, rowCount, imageWidth/colCount, imageHeight/rowCount));
	}
	
	public String getUrl() {
		return url;
	}

	public Token[] getTokens() {
		if (token==null) {
			token = getBoard().getGraphics().loadImage(url);
		}
		return new Token[] {token};
	}
	
	public String toString() {
		return "SpriteImageItem["+getUrl()+"]";
	}

	public Float getImageLeft(int index) {
		Facet facet = (Facet)facets.get(index);
		return facet==null ? null : facet.x;
	}
	
	public Float getImageTop(int index) {
		Facet facet = (Facet)facets.get(index);
		return facet==null ? null : facet.y;
	}

	public Float getImageWidth(int index) {
		Facet facet = (Facet)facets.get(index);
		return facet==null ? null : facet.width;
	}

	public Float getImageHeight(int index) {
		Facet facet = (Facet)facets.get(index);
		return facet==null ? null : facet.height;
	}

	@Override
	public Token getToken(int index) {
		return token;
	}

	public static Animation.Factory play(long duration, int imageCount, MoveableFinder finder) {
		SequenceAnimation.Builder animationBuilder = new SequenceAnimation.Builder();
		long stepDuration = duration/imageCount;
		for (int index=0; index<imageCount-1; index++) {
			animationBuilder.addAnimation(new ChangeFaceAnimation.Builder(stepDuration, index+1).setItem(finder));
		}
		animationBuilder.addAnimation(new ChangeFaceAnimation.Builder(stepDuration, 0).setItem(finder));
		return animationBuilder;
	}

}
