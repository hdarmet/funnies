package com.ithaque.funnies.shared.basic.items;

import java.util.HashMap;
import java.util.Map;

import com.ithaque.funnies.shared.basic.Token;

public class ImageItem extends AbstractImageItem {

	protected static class Facet extends AbstractFacet {
		public Facet(String url) {
			this.url = url;
		}
		Float width = null;
		Float height = null;
		Token token = null;
		String url = null;
	}
	
	Map<String, Facet> urlsMap = new HashMap<String, Facet>();
	
	public ImageItem(String ... urls) {
		setUrls(urls);
	}
	
	public ImageItem(int index, String ... urls) {
		setUrls(urls);
		for (int face=0; face<getImageCount(); face++) {
			setOpacity(face, face==index?1.0f:0.0f);
		}
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
		for (AbstractFacet abstractFacet : facets) {
			Facet facet = (Facet)abstractFacet;
			urls[i++] = facet.url;
		}
		return urls;
	}

	public Token[] getTokens() {
		Token[] tokens = new Token[facets.size()];
		int i = 0;
		for (AbstractFacet abstractFacet : facets) {
			Facet facet = (Facet)abstractFacet;
			initToken(facet);
			tokens[i++] = facet.token;
		}
		return tokens;
	}

	private void initToken(Facet facet) {
		if (facet.token==null) {
			facet.token = getBoard().getGraphics().loadImage(facet.url);
			facetsMap.put(facet.token, facet);
		}
	}
	
	public String toString() {
		StringBuffer label = new StringBuffer("ImageItem[");
		boolean start = true;
		for (AbstractFacet abstractFacet : facets) {
			Facet facet = (Facet)abstractFacet;
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

	public Float getImageLeft(int index) {
		return 0.0f;
	}
	
	public Float getImageTop(int index) {
		return 0.0f;
	}

	public Float getImageWidth(int index) {
		Facet facet = (Facet)facets.get(index);
		if (facet.width==null) {
			facet.width=getBoard().getGraphics().getImageWidth(facet.token);
		}
		return facet.width;
	}

	public Float getImageHeight(int index) {
		Facet facet = (Facet)facets.get(index);
		if (facet.height==null) {
			facet.height=getBoard().getGraphics().getImageHeight(facet.token);
		}
		return facet.height;
	}

	@Override
	public Token getToken(int index) {
		Facet facet = (Facet)facets.get(index);
		initToken(facet);
		return facet.token;
	}
	
}
