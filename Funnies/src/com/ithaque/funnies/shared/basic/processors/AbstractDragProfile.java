package com.ithaque.funnies.shared.basic.processors;

import com.ithaque.funnies.shared.Geometric;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.AnimationContext;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.GraphicsUtil;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemHolder;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.items.animations.SoftenAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ParallelItemAnimation;
import com.ithaque.funnies.shared.basic.processors.DragProcessor.DragProfile;

public abstract class AbstractDragProfile implements DragProfile {

	public static final AnimationContext.Key DROP_LOCATION_KEY = new AnimationContext.Key("DROP_LOCATION_KEY");
	public static final AnimationContext.Key DRAGGED_ITEM_KEY = new AnimationContext.Key("DRAGGED_ITEM_KEY");
	
	Item dragged;
	ItemHolder originalHolder;
	Location anchor;
	Location startLocation;
	Location dropLocation;
	ItemHolder dragLayer;
	Board board;
	
	public AbstractDragProfile(Board board) {
		this.board = board;
	}
	
	public Board getBoard() {
		return board;
	}
	
	@Override
	public boolean beginDrag(MouseEvent event, Board board) {
		Item dragged = board.getMouseTarget(event);
		if (dragged!=null && acceptDraggeable(dragged)) {
			this.dragged = dragged;
			startLocation = dragged.getLocation();
			anchor = DragProcessor.getAnchor(event, dragged);
			launchDragAnimation(dragged);
			if (dragLayer!=null) {
				originalHolder = this.dragged.getParent();
				originalHolder.removeItem(dragged);
				dragLayer.addItem(dragged);
			}
		}
		return this.dragged!=null;
	}

	protected void launchDragAnimation(Item dragged) {
		Animation animation = getBeginDragAnimation(dragged).create();
		if (animation!=null) {
			board.launchAnimation(animation, retrieveAnimationContext());
		}
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
		ParallelItemAnimation animation = new ParallelItemAnimation();
		if (!resolveDrop(event, board, animation)) {
			adjustDraggedLocationOnDrop(animation, startLocation);
		}
		if (getDraggedDropAnimation(dragged)!=null) {
			Animation dropAnimation = getDraggedDropAnimation(dragged).create();
		    animation.addAnimation(dropAnimation);
		}
		board.launchAnimation(animation, retrieveAnimationContext());
		dragged = null;
	}

	protected void adjustDraggedLocationOnDrop(
			ParallelItemAnimation animation, Location draggedLocation) {
		SoftenAnimation.Builder revertToOrigin = getAdjustLocationAnimation(dragged);
		if (revertToOrigin!=null) {
			SoftenAnimation revertToOriginInstance = revertToOrigin.create();
			dropLocation = draggedLocation;
			animation.addAnimation(revertToOriginInstance);
		}
		else {
			dragged.setLocation(draggedLocation);
		}
	}
	
	protected abstract SoftenAnimation.Builder getBeginDragAnimation(Item dragged);

	protected abstract SoftenAnimation.Builder getAdjustLocationAnimation(Item dragged);
	
	protected abstract SoftenAnimation.Builder getDraggedDropAnimation(Item dragged);

	protected boolean resolveDrop(MouseEvent event, Board board, ParallelItemAnimation animation) {
		Location mouseLocation = DragProcessor.followMouse(event, dragged, anchor);
		dragged.setLocation(mouseLocation);
		return true;
	}
	
	public AnimationContext retrieveAnimationContext() {
		return new DragAnimationContext(dragged, dropLocation);
	}
	
	public static class DragAnimationContext implements AnimationContext {
	
		Item dragged;
		Location dropLocation;
	
		public DragAnimationContext(Item dragged, Location dropLocation) {
			this.dragged = dragged;
			this.dropLocation = dropLocation;
		}
		
		public Location getLocation(Key locationKey) {
			if (locationKey==DROP_LOCATION_KEY) {
				return dropLocation;
			}
			return null;
		}
		
		public Item getItem(Key itemKey) {
			if (itemKey==DRAGGED_ITEM_KEY) {
				return dragged;
			}
			return null;
		}
		
		public Float getFactor(Key itemKey) {
			return null;
		}
	}
	
}

