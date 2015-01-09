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
import com.ithaque.funnies.shared.basic.Moveable;
import com.ithaque.funnies.shared.basic.TransformUtil;
import com.ithaque.funnies.shared.basic.items.animations.ParallelAnimation;
import com.ithaque.funnies.shared.basic.processors.DragProcessor.DragProfile;

public abstract class AbstractDragProfile implements DragProfile {

	public static final AnimationContext.Key DROP_LOCATION_KEY = new AnimationContext.Key("DROP_LOCATION_KEY");
	public static final AnimationContext.Key DROP_HOLDER_KEY = new AnimationContext.Key("DROP_HOLDER_KEY");
	public static final AnimationContext.Key DROP_ROTATION_KEY = new AnimationContext.Key("DROP_ROTATION_KEY");
	public static final AnimationContext.Key DROP_SCALE_KEY = new AnimationContext.Key("DROP_SCALE_KEY");
	public static final AnimationContext.Key DRAGGED_ITEM_KEY = new AnimationContext.Key("DRAGGED_ITEM_KEY");
	
	Item dragged;
	ItemHolder initialHolder;
	Float startRotation;
	Float startScale;
	Location anchor;
	Location startLocation;
	
	Location dropLocation;
	ItemHolder dropHolder;
	Float dropRotation;
	Float dropScale;
	
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
			anchor = DragProcessor.getAnchor(event, dragged);
			launchDragAnimation(dragged);
			startLocation = dragged.getLocation();
			startRotation = dragged.getRotation();
			startScale = dragged.getScale();
			if (dragLayer!=null) {
				initialHolder = dragged.getParent();
				float dragLayerRotation = TransformUtil.transformRotation(dragged.getParent(), dragLayer, dragged.getRotation());
				dragged.setRotation(dragLayerRotation);
				float dragLayerScale = TransformUtil.transformScale(dragged.getParent(), dragLayer, dragged.getScale());
				dragged.setScale(dragLayerScale);
				this.dragged.changeParent(dragLayer);
			}
		}
		return this.dragged!=null;
	}

	protected void launchDragAnimation(Item dragged) {
		Animation animation = getBeginDragAnimation(dragged).create();
		if (animation!=null) {
			animation.setContext(retrieveAnimationContext());
			board.launchAnimation(animation);
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
			Location[] absShape = TransformUtil.transformShape(dragged, dragged.getShape());
			Location[] area = Geometric.getArea(absShape);
			Location overhead = GraphicsUtil.inDisplayLimits( 
				0.0f, 0.0f, graphics.getDisplayWidth(), graphics.getDisplayHeight(),
				area[0].getX(), area[0].getY(), area[1].getX(), area[1].getY());
			if (overhead != null) {
				Location absLocation = TransformUtil.transformLocation(layer.getParent(), layer.getLocation());
				Location newLocation = new Location(absLocation.getX()-overhead.getX(), absLocation.getY()-overhead.getY());
				layer.setLocation(TransformUtil.invertTransformLocation(layer.getParent(), newLocation));
			}
		}
		reactToDrag(event, board);
	}

	protected void reactToDrag(MouseEvent event, Board board) {
	}

	@Override
	public void drop(MouseEvent event, Board board) {
		ParallelAnimation animation = new ParallelAnimation();
		if (!resolveDrop(event, board, animation)) {
			adjustDraggedOnDrop(animation, initialHolder, startLocation, startRotation, startScale);
		}
		if (getDraggedDropAnimation(dragged)!=null) {
			Animation dropAnimation = getDraggedDropAnimation(dragged).create();
		    animation.addAnimation(dropAnimation);
		}
		animation.setContext(retrieveAnimationContext());
		board.launchAnimation(animation);
		dragged = null;
	}

	protected void returnDraggedToInitialHolder() {
		if (initialHolder!=null) {
			dragged.changeParent(initialHolder);
		}
	}
	
	protected void adjustDraggedOnDrop(
			ParallelAnimation animation, 
			ItemHolder draggedHolder,
			Location draggedLocation,
			Float draggedRotation,
			Float draggedScale) 
	{
		Animation.Factory moveAnimationFactory = getAdjustLocationAnimation(dragged);
		if (moveAnimationFactory!=null) {
			Animation moveAnimation = moveAnimationFactory.create();
			dropRotation = draggedRotation;
			dropScale = draggedScale;
			dropLocation = draggedLocation;
			dropHolder = draggedHolder;
			animation.addAnimation(moveAnimation);
		}
		else {
			if (draggedHolder!=null) {
				dragged.setRotation(draggedRotation);
				dragged.setScale(draggedScale);
				dragged.changeParent(draggedHolder);
			}
			dragged.setLocation(draggedLocation);
		}
	}

	protected float adjustDraggedRotation(Item dragged, ItemHolder draggedHolder) {
		return TransformUtil.transformRotation(dragged.getParent(), draggedHolder, dragged.getRotation());
	}
	
	protected float adjustDraggedScale(Item dragged, ItemHolder draggedHolder) {
		return TransformUtil.transformScale(dragged.getParent(), draggedHolder, dragged.getRotation());
	}

	protected boolean resolveDrop(MouseEvent event, Board board, ParallelAnimation animation) {
		returnDraggedToInitialHolder();
		Location mouseLocation = DragProcessor.followMouse(event, dragged, anchor);
		dragged.setLocation(mouseLocation);
		return true;
	}
	
	protected abstract Animation.Factory getBeginDragAnimation(Item dragged);

	protected abstract Animation.Factory getAdjustLocationAnimation(Item dragged);
	
	protected abstract Animation.Factory getDraggedDropAnimation(Item dragged);

	public AnimationContext retrieveAnimationContext() {
		return new DragAnimationContext(dragged, dropLocation, dropHolder, dropRotation, dropScale);
	}
	
	public static class DragAnimationContext implements AnimationContext {
		Item dragged;
		Location dropLocation;
		ItemHolder dropHolder;
		Float dropRotation;
		Float dropScale;
	
		public DragAnimationContext(Item dragged, Location dropLocation, ItemHolder dropHolder, Float dropRotation, Float dropScale) {
			this.dragged = dragged;
			this.dropLocation = dropLocation;
			this.dropHolder = dropHolder;
			this.dropRotation = dropRotation;
			this.dropScale = dropScale;
		}
		
		public Location getLocation(Key locationKey) {
			if (locationKey==DROP_LOCATION_KEY) {
				return dropLocation;
			}
			return null;
		}
		
		public Moveable getItem(Key itemKey) {
			if (itemKey==DRAGGED_ITEM_KEY) {
				return dragged;
			}
			else if (itemKey==DROP_HOLDER_KEY) {
				return dropHolder;
			}
			return null;
		}
		
		public Float getFactor(Key itemKey) {
			if (itemKey==DROP_ROTATION_KEY) {
				return dropRotation;
			}
			else if (itemKey==DROP_SCALE_KEY) {
				return dropScale;
			}
			return null;
		}
	}
	
}

