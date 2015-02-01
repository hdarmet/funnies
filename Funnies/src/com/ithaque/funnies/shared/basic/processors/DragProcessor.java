package com.ithaque.funnies.shared.basic.processors;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.Processor;
import com.ithaque.funnies.shared.basic.TransformUtil;

public class DragProcessor implements Processor {

	List<DragProfile> dragProfiles = new ArrayList<DragProfile>();
	DragProfile inDrag = null;
	
	@Override
	public boolean process(Event event, Board board) {
		if (event instanceof MouseEvent) {
			MouseEvent mouseEvent = (MouseEvent)event;
			if (event.getType()==Type.MOUSE_DOWN) {
				if (inDrag==null) {
					inDrag = processBeginDrag(mouseEvent, board);
				}
				return inDrag!=null;
			}
			else if(inDrag!=null && event.getType()==Type.MOUSE_UP) {
				processDrop(mouseEvent, board);
				inDrag=null;
				return true;
			}
			else if (inDrag!=null && event.getType()==Type.MOUSE_DRAG) {
				processDrag(mouseEvent, board);
				return true;
			}
		}
		return false;
	}

	protected void processDrag(MouseEvent event, Board board) {
		inDrag.drag(event, board);
	}

	protected void processDrop(MouseEvent event, Board board) {
		inDrag.drop(event, board);
	}

	protected DragProfile processBeginDrag(MouseEvent event, Board board) {
		for (DragProfile profile : new ArrayList<DragProfile>(dragProfiles)) {
			if (profile.beginDrag(event, board)) {
				return profile;
			}
		}
		return null;
	}

	public interface DragProfile {
		boolean beginDrag(MouseEvent event, Board board);
		void drag(MouseEvent event, Board board);
		void drop(MouseEvent event, Board board);
	}
	
	public void addDragProfile(DragProfile dragProfile) {
		dragProfiles.add(dragProfile);
	}

	public void removeDragProfile(DragProfile dragProfile) {
		if (inDrag!=null) {
			throw new IllegalInvokeException();
		}
		dragProfiles.remove(dragProfile);
	}
	
	public static Location getAnchor(MouseEvent event, Item dragged) {
		Location mouseLocation = new Location(event.getX(), event.getY());
		Location newLocation = TransformUtil.invertTransformLocation(dragged.getParent(), mouseLocation);
		Location startLocation = dragged.getLocation();
		return new Location(newLocation.getX()-startLocation.getX(), newLocation.getY()-startLocation.getY());
	}

	public static Location followMouse(MouseEvent event, Item dragged, Location anchor) {
		Location mouseLocation = new Location(event.getX(), event.getY());
		Location newLocation = TransformUtil.invertTransformLocation(dragged.getParent(), mouseLocation);
		return new Location(
			newLocation.getX()-anchor.getX(), 
			newLocation.getY()-anchor.getY());
	}
		
}
