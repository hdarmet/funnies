package com.ithaque.funnies.shared.funny.standard;

import com.ithaque.funnies.shared.basic.Color;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.items.ClippedItem;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.funny.AbstractFunny;
import com.ithaque.funnies.shared.funny.FunnySpy;

public class GridPaneFunny extends AbstractFunny /*implements ActivableFunny, HoverFunny*/ {

	ImageItem scrollUp;
	ImageItem scrollDown;
	ClippedItem content;
	
	public GridPaneFunny(String id, float paneWidth, float paneHeight, Color fillColor, Color lineColor, float lineWidth) {
		super(id);
		content = new ClippedItem();
		scrollUp = new ImageItem("scrollUp.png");
		scrollUp.addEventType(Type.MOUSE_CLICK);
		scrollUp.addEventType(Type.MOUSE_MOVE);
		scrollDown = new ImageItem("scrollDown.png");
		scrollDown.addEventType(Type.MOUSE_CLICK);
		scrollDown.addEventType(Type.MOUSE_MOVE);
	}

	@Override
	public void addSpy(FunnySpy spy) {
		content.addObserver(spy);
	}

	@Override
	public void removeSpy(FunnySpy spy) {
		content.removeObserver(spy);
	}
}
