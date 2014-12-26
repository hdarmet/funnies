package com.ithaque.funnies.shared.basic.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.Token;

public abstract class AbstractImageItem extends Item {

	protected static class AbstractFacet {
		public AbstractFacet() {
		}
		float opacity = 1.0f;
	}
	
	List<AbstractFacet> facets = new ArrayList<AbstractFacet>();
	Map<Token, AbstractFacet> facetsMap = new HashMap<Token, AbstractFacet>();
	
	public AbstractImageItem() {
	}
	
	public float getOpacity(int facetIndex) {
		return facets.get(facetIndex).opacity;
	}
	
	public void setOpacity(int facetIndex, float opacity) {
		AbstractFacet facet = facets.get(facetIndex);
		facet.opacity = opacity;
		dirty();
	}
	
	public abstract Token[] getTokens();

	public int getImageCount() {
		return facets.size();
	}
	
	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(this);
		super.render(graphics);
	}
	
	@Override
	public Location[] getShape() {
		Location[] shape = super.getShape();
		return shape!=null ? shape : getShapeByImages();
	}

	public Location[] getShapeByImages() {
		float width = 0.0f;
		float height = 0.0f;
		for (int index=0; index<facets.size(); index++) {
			Float imageWidth = getImageWidth(index);
			if (imageWidth!=null && imageWidth>width) {
				width = imageWidth;
			}
			Float imageHeight = getImageHeight(index);
			if (imageHeight!=null && imageHeight>height) {
				height = imageHeight;
			}
		}
		Location upperLeft = new Location(-width/2.0f, -height/2.0f);
		Location upperRight = new Location(width/2.0f, -height/2.0f);
		Location bottomRight = new Location(width/2.0f, height/2.0f);
		Location bottomLeft = new Location(-width/2.0f, height/2.0f);
		return new Location[] {upperLeft, upperRight, bottomRight, bottomLeft};
	}

	public abstract Float getImageLeft(int index);
	
	public abstract Float getImageTop(int index);

	public abstract Float getImageWidth(int index);

	public abstract Float getImageHeight(int index);

	public abstract Token getToken(int index);
	
}
