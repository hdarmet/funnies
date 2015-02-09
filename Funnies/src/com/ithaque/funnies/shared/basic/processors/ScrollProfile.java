package com.ithaque.funnies.shared.basic.processors;

import com.ithaque.funnies.shared.Location;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.processors.DragProcessor.DragProfile;

public class ScrollProfile implements DragProfile {

	Layer layer;
	Location anchor;
	
	public ScrollProfile(Layer layer) {
		this.layer = layer;
	}
	
	@Override
	public boolean beginDrag(MouseEvent event, Board board) {
		if (board.getMouseTarget(event)==layer) {
			anchor = DragProcessor.getAnchor(event, layer);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void drag(MouseEvent event, Board board) {
		layer.setLocation(DragProcessor.followMouse(event, layer, anchor));
	}

	@Override
	public void drop(MouseEvent event, Board board) {
		drag(event, board);
	}

}
