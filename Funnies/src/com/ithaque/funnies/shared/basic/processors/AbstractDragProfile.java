package com.ithaque.funnies.shared.basic.processors;

import com.ithaque.funnies.shared.Geometric;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.GraphicsUtil;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemHolder;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.items.animations.ItemMoveAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ParallelItemAnimation;
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
		Animation animation = getBeginDragAnimation(dragged).create();
		if (animation!=null) {
			animation.setItem(dragged);
			animation.launchFor();
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
		    animation.addAnimation(getDraggedDropAnimation(dragged).create());
		}
		animation.setItem(dragged);
		animation.launchFor();
		dragged = null;
	}

	protected void adjustDraggedLocationOnDrop(
			ParallelItemAnimation animation, Location draggedLocation) {
		ItemMoveAnimation.Builder revertToOrigin = getAdjustLocationAnimation(dragged);
		if (revertToOrigin!=null) {
			ItemMoveAnimation revertToOriginInstance = revertToOrigin.create();
			revertToOriginInstance.setLocation(draggedLocation);
			animation.addAnimation(revertToOriginInstance);
		}
		else {
			dragged.setLocation(draggedLocation);
		}
	}

	protected abstract Animation.Factory getBeginDragAnimation(Item dragged);

	protected abstract ItemMoveAnimation.Builder getAdjustLocationAnimation(Item dragged);
	
	protected abstract Animation.Factory getDraggedDropAnimation(Item dragged);

	protected boolean resolveDrop(MouseEvent event, Board board, ParallelItemAnimation animation) {
		Location mouseLocation = DragProcessor.followMouse(event, dragged, anchor);
		dragged.setLocation(mouseLocation);
		return true;
	}
	
}

