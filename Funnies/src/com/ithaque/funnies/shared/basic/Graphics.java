package com.ithaque.funnies.shared.basic;

import com.ithaque.funnies.shared.basic.items.ImageItem;

public interface Graphics {
	
	Integer loadImage(String url);

	void drawImage(ImageItem imageItem);

	boolean isTarget(Item item, Location point, Location[] shape);
	
	void clear();

	Location[] getShape(ImageItem imageItem);
	
	Location[] transformShape(Item item, Location[] shape);

	Location invertTransformLocation(Moveable item, Location location);

	Location transformLocation(Moveable item, Location location);

	Location invertTransformLocationToParent(Moveable item, Location location);

	Location transformLocationToParent(Moveable item, Location location);
	
	float getDisplayWidth();

	float getDisplayHeight();

	Integer createLayer();

	void setLayer(Integer token);
	
}
