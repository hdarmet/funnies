package com.ithaque.funnies.shared.basic.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;

public class ImageItem extends Item {

	class Facet {
		public Facet(String url) {
			this.url = url;
		}
		Integer token = null;
		String url = null;
		float opacity = 1.0f;
	}
	
	List<Facet> facets = new ArrayList<ImageItem.Facet>();
	Map<Integer, Facet> facetsMap = new HashMap<Integer, Facet>();
	Map<String, Facet> urlsMap = new HashMap<String, Facet>();
	
	public ImageItem(String ... urls) {
		setUrls(urls);
	}
	
	public float getOpacity(String url) {
		Facet facet = urlsMap.get(url);
		return facet==null ? 0.0f : facet.opacity;
	}
	
	public void setOpacity(String url, float opacity) {
		Facet facet = urlsMap.get(url);
		if (facet!=null) {
			facet.opacity = opacity;
			dirty();
		}
	}
	
	public float getOpacity(int facetIndex) {
		return facets.get(facetIndex).opacity;
	}
	
	public void setOpacity(int facetIndex, float opacity) {
		Facet facet = facets.get(facetIndex);
		facet.opacity = opacity;
	}
	
	public void setUrls(String ... urls) {
		facets.clear();
		for (String url : urls) {
			Facet facet = new Facet(url);
			facets.add(facet);
			urlsMap.put(url, facet);
		}
	}

	public String[] getUrls() {
		String[] urls = new String[facets.size()];
		int i = 0;
		for (Facet facet : facets) {
			urls[i++] = facet.url;
		}
		return urls;
	}

	public Integer[] getTokens() {
		Integer[] tokens = new Integer[facets.size()];
		int i = 0;
		for (Facet facet : facets) {
			if (facet.token==null) {
				facet.token = getBoard().getGraphics().loadImage(facet.url);
				facetsMap.put(facet.token, facet);
			}
			tokens[i++] = facet.token;
		}
		return tokens;
	}
	
	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(this);
		super.render(graphics);
	}
	
	@Override
	public Location[] getShape() {
		Location[] shape = super.getShape();
		return shape!=null ? shape : getBoard().getGraphics().getShape(this);
	}

	public float getOpacity(Integer token) {
		Facet facet = facetsMap.get(token);
		return facet!=null ? facet.opacity : 0.0f;
	}
	
	public String toString() {
		StringBuffer label = new StringBuffer("ImageItem[");
		boolean start = true;
		for (Facet facet : facets) {
			if (start) {
				start=false;
			}
			else {
				label.append(", ");
			}
			label.append(facet.url);
		}
		label.append("]");
		return label.toString();
	}
}
