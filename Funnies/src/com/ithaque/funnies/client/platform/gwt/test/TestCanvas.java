package com.ithaque.funnies.client.platform.gwt.test;

import com.ithaque.funnies.client.platform.gwt.CanvasInterface;
import com.ithaque.funnies.client.platform.gwt.Context2D;

public class TestCanvas implements CanvasInterface {

	float width = 0.0f;
	float height = 0.0f;
	String id;
	TestContext2D context2D;
	
	public TestCanvas(String id) {
		this.id = id;
		this.context2D = new TestContext2D(id);
	}

	public void setVisible(boolean visible) {
		TestRegistry.addCall("Canvas", id, "setVisible", ""+visible);
	}

	public void setAttribute(String key, String value) {
		TestRegistry.addCall("Canvas", id, "setAttribute", value);
	}

	public void setCoordinateSpaceWidth(int width) {
		TestRegistry.addCall("Canvas", id, "setCoordinateSpaceWidth", ""+width);
		this.width = width;
	}

	public void setCoordinateSpaceHeight(int height) {
		TestRegistry.addCall("Canvas", id, "setCoordinateSpaceHeight", ""+height);
		this.height = height;
	}

	public void saveContext2D() {
		TestRegistry.addCall("Canvas", id, "saveContext2D");
	}

	public void restoreContext2D() {
		TestRegistry.addCall("Canvas", id, "restoreContext2D");
	}
	
	public void addToRootPanel() {
		TestRegistry.addCall("Canvas", id, "addToRootPanel");
	}

	public float getCoordinateSpaceWidth() {
		TestRegistry.addCall("Canvas", id, "getCoordinateSpaceWidth");
		return width;
	}

	public float getCoordinateSpaceHeight() {
		TestRegistry.addCall("Canvas", id, "getCoordinateSpaceHeight");
		return height;
	}

	public Context2D getContext2d() {
		return context2D;
	}

}
