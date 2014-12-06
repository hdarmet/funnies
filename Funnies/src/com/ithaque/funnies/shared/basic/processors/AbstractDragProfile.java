package com.ithaque.funnies.shared.basic.processors;

import com.ithaque.funnies.shared.Geometric;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.GraphicsUtil;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemHolder;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.items.animations.ItemAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ItemMoveAnimation;
import com.ithaque.funnies.shared.basic.items.animations.SimultaneousItemAnimation;
import com.ithaque.funnies.shared.basic.processors.DragProcessor.DragProfile;

public abstract class AbstractDragProfile implements DragProfile {

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
		ItemAnimation animation = getBeginDragAnimation(dragged);
		if (animation!=null) {
			animation.duplicate().launchFor(dragged);
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
		SimultaneousItemAnimation animation = new SimultaneousItemAnimation();
		if (!executeDrop(event, board, animation)) {
			adjustDraggedLocationOnDrop(animation, startLocation);
		}
		if (getAdjustLocationAnimation(dragged)!=null) {
		    animation.addAnimation(getAdjustLocationAnimation(dragged).duplicate());
		}
		animation.launchFor(dragged);
		dragged = null;
	}

	protected void adjustDraggedLocationOnDrop(
			SimultaneousItemAnimation animation, Location draggedLocation) {
		ItemMoveAnimation revertToOrigin = getDraggedDropAnimation(dragged);
		if (revertToOrigin!=null) {
			revertToOrigin = revertToOrigin.duplicate();
			revertToOrigin.setLocation(draggedLocation);
			animation.addAnimation(revertToOrigin);
		}
		else {
			dragged.setLocation(draggedLocation);
		}
	}

	protected abstract ItemAnimation getBeginDragAnimation(Item dragged);

	protected abstract ItemAnimation getAdjustLocationAnimation(Item dragged);
	
	protected abstract ItemMoveAnimation getDraggedDropAnimation(Item dragged);

	protected boolean executeDrop(MouseEvent event, Board board, SimultaneousItemAnimation animation) {
		Location mouseLocation = DragProcessor.followMouse(event, dragged, anchor);
		dragged.setLocation(mouseLocation);
		return true;
	}
	
}

