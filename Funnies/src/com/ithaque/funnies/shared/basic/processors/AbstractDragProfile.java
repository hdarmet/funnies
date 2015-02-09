package com.ithaque.funnies.shared.basic.processors;

import com.ithaque.funnies.shared.Geometric;
import com.ithaque.funnies.shared.Location;
import com.ithaque.funnies.shared.Shape;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.AnimationContext;
import com.ithaque.funnies.shared.basic.AnimationContext.FactorFinder;
import com.ithaque.funnies.shared.basic.AnimationContext.LocationFinder;
import com.ithaque.funnies.shared.basic.AnimationContext.MoveableFinder;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.GraphicsUtil;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemHolder;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.Moveable;
import com.ithaque.funnies.shared.basic.TransformUtil;
import com.ithaque.funnies.shared.basic.items.animations.ParallelAnimation;
import com.ithaque.funnies.shared.basic.processors.DragProcessor.DragProfile;

public abstract class AbstractDragProfile implements DragProfile {

	Item dragged;
	Location anchor;
	ItemHolder dragLayer;
	Board board;
	
	ItemHolder initialHolder;
	Float initialRotation;
	Float initialScale;
	Location initialLocation;
	
	Location dropLocation;
	ItemHolder dropHolder;
	Float dropRotation;
	Float dropScale;
		
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
			initialLocation = dragged.getLocation();
			initialRotation = dragged.getRotation();
			initialScale = dragged.getScale();
			if (dragLayer!=null) {
				initialHolder = dragged.getParent();
				float dragLayerRotation = TransformUtil.transformRotation(dragged.getParent(), dragLayer, dragged.getRotation());
				dragged.setRotation(dragLayerRotation);
				float dragLayerScale = TransformUtil.transformScale(dragged.getParent(), dragLayer, dragged.getScale());
				dragged.setScale(dragLayerScale);
				this.dragged.setParent(dragLayer);
			}
			launchDragAnimation(dragged);
		}
		return this.dragged!=null;
	}

	protected void launchDragAnimation(Item dragged) {
		Animation.Factory animationFactory = getBeginDragAnimation(dragged);
		if (animationFactory!=null) {
			Animation animation = animationFactory.create();
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
			scrollLayerIfRequired((Layer)dragged.getParent(), board);
		}
		processDrag(event, board);
	}

	protected void scrollLayerIfRequired(Layer layer, Board board) {
		Graphics graphics = board.getGraphics();
		Shape absShape = TransformUtil.transformShape(dragged, dragged.getShape());
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

	protected void processDrag(MouseEvent event, Board board) {
	}

	@Override
	public void drop(MouseEvent event, Board board) {
		ParallelAnimation animation = new ParallelAnimation();
		if (!resolveDrop(event, board, animation)) {
			adjustDraggedOnDrop(animation, initialHolder, initialLocation, initialRotation, initialScale);
		}
		if (getDraggedDropAnimation(dragged)!=null) {
			Animation dropAnimation = getDraggedDropAnimation(dragged).create();
		    animation.addAnimation(dropAnimation);
		}
		animation.setContext(retrieveAnimationContext());
		board.launchAnimation(animation);
		dragged = null;
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
				dragged.setParent(draggedHolder);
			}
			dragged.setLocation(draggedLocation);
		}
	}

	protected float adjustDraggedRotation(Item dragged, ItemHolder draggedHolder) {
		return TransformUtil.transformRotation(dragged.getParent(), draggedHolder, dragged.getRotation());
	}
	
	protected float adjustDraggedScale(Item dragged, ItemHolder draggedHolder) {
		return TransformUtil.transformScale(dragged.getParent(), draggedHolder, dragged.getScale());
	}

	protected boolean resolveDrop(MouseEvent event, Board board, ParallelAnimation animation) {
		if (initialHolder!=null) {
			dragged.setParent(initialHolder);
		}
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
		ItemHolder dropHolder;
		Location dropLocation;
		Float dropRotation;
		Float dropScale;
	
		public DragAnimationContext(Item dragged, Location dropLocation, ItemHolder dropHolder, Float dropRotation, Float dropScale) {
			this.dragged = dragged;
			this.dropLocation = dropLocation;
			this.dropHolder = dropHolder;
			this.dropRotation = dropRotation;
			this.dropScale = dropScale;
		}
	}
	
	public static MoveableFinder draggedItem() {
		return new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return ((DragAnimationContext)context).dragged;
			}			
		};
	}
	
	public static LocationFinder dropLocation() {
		return new LocationFinder() {
			@Override
			public Location find(AnimationContext context) {
				return ((DragAnimationContext)context).dropLocation;
			}			
		};
	}
	
	public static MoveableFinder dropItemHolder() {
		return new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return ((DragAnimationContext)context).dropHolder;
			}			
		};
	}

	public static FactorFinder dropRotation() {
		return new FactorFinder() {
			@Override
			public Float find(AnimationContext context) {
				return ((DragAnimationContext)context).dropRotation;
			}			
		};
	}
	
	public static FactorFinder dropScale() {
		return new FactorFinder() {
			@Override
			public Float find(AnimationContext context) {
				return ((DragAnimationContext)context).dropScale;
			}			
		};
	}
	
}

