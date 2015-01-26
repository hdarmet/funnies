package com.ithaque.funnies.client.platform.gwt;


public interface CanvasInterface {

	float getCoordinateSpaceWidth();

	float getCoordinateSpaceHeight();

	void setVisible(boolean visible);

	Context2D getContext2d();

	void restoreContext2D();

	void saveContext2D();

	void addToRootPanel();

	void setAttribute(String key, String value);

	void setCoordinateSpaceWidth(int width);

	void setCoordinateSpaceHeight(int height);

}
