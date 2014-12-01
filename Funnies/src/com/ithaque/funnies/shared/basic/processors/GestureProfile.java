package com.ithaque.funnies.shared.basic.processors;

import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.processors.GestureRecognition.MatchHandler;

public class GestureProfile implements DragProcessor.DragProfile {

	GestureRecognition gestureRecognition;
	GestureRecognition.Gesture currentGesture = null;
	Layer layer;
	
	public GestureProfile(Layer layer) {
		gestureRecognition = new GestureRecognition();
		this.layer = layer;
	}
	
	public void addGesture(String id, MatchHandler matchHandler, Integer ... moves) {
		gestureRecognition.addGesture(id, matchHandler, moves);
	}
	
	@Override
	public boolean beginDrag(MouseEvent event, Board board) {
		if (board.getMouseTarget(event)==layer) {
			currentGesture = gestureRecognition.startCapture(board.getTime(), event);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void drag(MouseEvent event, Board board) {
		if (currentGesture!=null) {
			gestureRecognition.capture(board.getTime(), event, currentGesture);
		}
	}

	@Override
	public void drop(MouseEvent event, Board board) {
		gestureRecognition.matchGesture(currentGesture);
		if (currentGesture.getMatch()!=null) {
			Event gEvent = new GestureEvent(Event.Type.GESTURE, currentGesture);
			board.processEvent(gEvent);
		}
	}

}
