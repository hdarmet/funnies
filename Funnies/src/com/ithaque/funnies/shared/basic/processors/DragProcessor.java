package com.ithaque.funnies.shared.basic.processors;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.Geometric;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.GraphicsUtil;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemHolder;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.Processor;
import com.ithaque.funnies.shared.basic.items.animations.ItemAnimation;
import com.ithaque.funnies.shared.basic.items.animations.MoveItemAnimation;
import com.ithaque.funnies.shared.basic.items.animations.OutBackEasing;
import com.ithaque.funnies.shared.basic.items.animations.SimultaneousItemAnimation;

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
		dragProfiles.remove(dragProfile);
	}
	
	public static Location getAnchor(MouseEvent event, Item dragged) {
		Location mouseLocation = new Location(event.getX(), event.getY());
		Location newLocation = dragged.getBoard().getGraphics().invertTransformLocation(dragged.getParent(), mouseLocation);
		Location startLocation = dragged.getLocation();
		return new Location(newLocation.getX()-startLocation.getX(), newLocation.getY()-startLocation.getY());
	}

	public static Location followMouse(MouseEvent event, Item dragged, Location anchor) {
		Location mouseLocation = new Location(event.getX(), event.getY());
		Location newLocation = dragged.getBoard().getGraphics().invertTransformLocation(dragged.getParent(), mouseLocation);
		return new Location(
			newLocation.getX()-anchor.getX(), 
			newLocation.getY()-anchor.getY());
	}
	
	public static MoveItemAnimation movetoTarget(Item dragged, Item target) {
		Location targetLocation = target.getLocation();
		Location absLocation = target.getBoard().getGraphics().transformLocation(target.getParent(), targetLocation);
		Location draggedLocation = dragged.getBoard().getGraphics().invertTransformLocation(dragged.getParent(), absLocation);
		return new MoveItemAnimation(new OutBackEasing(1000), draggedLocation, null, null);
	}
	
	public static abstract class AbstractDragProfile implements DragProfile {

		Item dragged;
		ItemHolder originalHolder;
		Location anchor;
		Location startLocation;
		ItemHolder dragLayer;
		
		@Override
		public boolean beginDrag(MouseEvent event, Board board) {
			Item dragged = board.getMouseTarget(event);
			if (dragged!=null && acceptDraggeable(dragged)) {
				this.dragged = dragged;
				startLocation = dragged.getLocation();
				anchor = getAnchor(event, dragged);
				if (getBeginDragAnimation(dragged)!=null) {
					getBeginDragAnimation(dragged).duplicate().launchFor(dragged);
				}
				if (dragLayer!=null) {
					originalHolder = this.dragged.getParent();
					originalHolder.removeItem(dragged);
					dragLayer.addItem(dragged);
				}
			}
			return this.dragged!=null;
		}

		public void setDragLayer(ItemHolder dragLayer) {
			this.dragLayer = dragLayer;
		}
				
		protected boolean acceptDraggeable(Item draggeable) {
			return true;
		}

		@Override
		public void drag(MouseEvent event, Board board) {
			Location mouseLocation = DragProcessor.followMouse(event, dragged, anchor);
			dragged.setLocation(mouseLocation);
			if (dragged.getParent() instanceof Layer) {
				Layer layer = (Layer)dragged.getParent();
				Graphics graphics = board.getGraphics();
				Location[] absShape = graphics.transformShape(dragged, dragged.getShape());
				Location[] area = Geometric.getArea(absShape);
				Location overhead = GraphicsUtil.inDisplayLimits( 
					0.0f, 0.0f, graphics.getDisplayWidth(), graphics.getDisplayHeight(),
					area[0].getX(), area[0].getY(), area[1].getX(), area[1].getY());
				if (overhead != null) {
					Location absLocation = board.getGraphics().transformLocation(layer.getParent(), layer.getLocation());
					Location newLocation = new Location(absLocation.getX()-overhead.getX(), absLocation.getY()-overhead.getY());
					layer.setLocation(board.getGraphics().invertTransformLocation(layer.getParent(), newLocation));
				}
			}
			reactToDrag(event, board);
		}

		protected void reactToDrag(MouseEvent event, Board board) {
		}

		@Override
		public void drop(MouseEvent event, Board board) {
			if (dragLayer!=null) {
				dragLayer.removeItem(dragged);
				originalHolder.addItem(dragged);
			}
			SimultaneousItemAnimation animation = new SimultaneousItemAnimation();
			if (executeDrop(event, board, animation)) {
				Location mouseLocation = DragProcessor.followMouse(event, dragged, anchor);
				dragged.setLocation(mouseLocation);
			}
			else {
				animation.addAnimation(new MoveItemAnimation(new OutBackEasing(1000), startLocation, null, null));
			}
			if (getDropAnimation(dragged)!=null) {
			    animation.addAnimation(getDropAnimation(dragged).duplicate());
			}
			animation.launchFor(dragged);
			dragged = null;
		}

		protected abstract ItemAnimation getBeginDragAnimation(Item dragged);

		protected abstract ItemAnimation getDropAnimation(Item dragged);

		protected boolean executeDrop(MouseEvent event, Board board, SimultaneousItemAnimation animation) {
			return false;
		}
		
	}
	
	public static abstract class AbstractTargetedDragProfile extends AbstractDragProfile {

		List<Item> draggeables = new ArrayList<Item>();
		List<Item> targets = new ArrayList<Item>();
		Item currentTarget = null;
		
		protected boolean acceptDraggeable(Item draggeable) {
			return draggeables.contains(draggeable);
		}
		
		public void addDraggeable(Item draggeable) {
			draggeables.add(draggeable);
		}
		
		public void removeDraggeable(Item draggeable) {
			draggeables.remove(draggeable);
		}
		
		public void addTarget(Item target) {
			targets.add(target);
		}
		
		public void removeTarget(Item target) {
			targets.remove(target);
		}

		@Override
		protected void reactToDrag(MouseEvent event, Board board) {
			Item target = getTarget(event);
			if (target!=currentTarget) {
				if (currentTarget!=null && getExitTargetAnimation(currentTarget)!=null) {
					getExitTargetAnimation(currentTarget).duplicate().launchFor(currentTarget);
				}
				if (target!=null && getEnterTargetAnimation(target)!=null) {
					getEnterTargetAnimation(target).duplicate().launchFor(target);
				}
				currentTarget = target;
			}
		}

		protected boolean executeDrop(MouseEvent event, Board board, SimultaneousItemAnimation animation) {
			currentTarget = null;
			Item target = getTarget(event);
			return target==null ? false : dropOnTarget(dragged, target, animation);
		}
		
		protected boolean dropOnTarget(Item dragged, Item target, SimultaneousItemAnimation animation) {
			ItemAnimation itemAnimation = getTargetDropAnimation(target);
			if (itemAnimation==null) {
				itemAnimation = getExitTargetAnimation(target);
			}
			if (itemAnimation!=null) {
				itemAnimation.duplicate().launchFor(target);
			}
			if (executeDrop(dragged, target)) {
				animation.addAnimation(movetoTarget(dragged, target));
				return true;
			}
			else {
				return false;
			}
		}

		protected abstract ItemAnimation getTargetDropAnimation(Item target);

		protected abstract ItemAnimation getEnterTargetAnimation(Item target);

		protected abstract ItemAnimation getExitTargetAnimation(Item target);
		
		protected boolean executeDrop(Item dragged, Item target) {
			return true;
		}

		protected Item getTarget(MouseEvent event) {
			for (Item target : new ArrayList<Item>(targets)) {
				if (target.acceptEvent(event) && acceptTarget(target)) {
					return target;
				}
			}
			return null;
		}

		protected boolean acceptTarget(Item target) {
			return true;
		}

	}

	public static class SimpleTargetedDragProfile extends AbstractTargetedDragProfile {

		ItemAnimation enterTargetAnimation = null;
		ItemAnimation exitTargetAnimation = null;
		ItemAnimation targetDropAnimation = null;
		ItemAnimation beginDragAnimation = null;
		ItemAnimation dropAnimation = null;
		
		public void setBeginDragAnimation(ItemAnimation beginDragAnimation) {
			this.beginDragAnimation = beginDragAnimation;
		}

		public void setDropAnimation(ItemAnimation dropAnimation) {
			this.dropAnimation = dropAnimation;
		}

		public void setEnterTargetAnimation(ItemAnimation enterTargetAnimation) {
			this.enterTargetAnimation = enterTargetAnimation;
		}

		public void setExitTargetAnimation(ItemAnimation exitTargetAnimation) {
			this.exitTargetAnimation = exitTargetAnimation;
		}

		public void setTargetDropAnimation(ItemAnimation targetDropAnimation) {
			this.targetDropAnimation = targetDropAnimation;
		}

		@Override
		protected ItemAnimation getTargetDropAnimation(Item target) {
			return targetDropAnimation;
		}

		@Override
		protected ItemAnimation getEnterTargetAnimation(Item target) {
			return enterTargetAnimation;
		}

		@Override
		protected ItemAnimation getExitTargetAnimation(Item target) {
			return exitTargetAnimation;
		}

		@Override
		protected ItemAnimation getBeginDragAnimation(Item dragged) {
			return beginDragAnimation;
		}

		@Override
		protected ItemAnimation getDropAnimation(Item dragged) {
			return dropAnimation;
		}
		
	}
	
}
