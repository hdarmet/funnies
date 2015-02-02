package com.ithaque.funnies.shared.funny.boardgame;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Color;
import com.ithaque.funnies.shared.basic.FrameLayer;
import com.ithaque.funnies.shared.basic.GroupItem;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.MultiLayered;
import com.ithaque.funnies.shared.basic.items.PolygonItem;
import com.ithaque.funnies.shared.basic.items.animations.FadingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ParallelAnimation;
import com.ithaque.funnies.shared.basic.items.animations.SequenceAnimation;
import com.ithaque.funnies.shared.basic.processors.DragProcessor;
import com.ithaque.funnies.shared.basic.processors.ScalingProcessor;
import com.ithaque.funnies.shared.basic.processors.ScrollProfile;
import com.ithaque.funnies.shared.funny.AbstractRing;
import com.ithaque.funnies.shared.funny.ActivableFunny;
import com.ithaque.funnies.shared.funny.ActivationProcessor;
import com.ithaque.funnies.shared.funny.AnimatedFunny;
import com.ithaque.funnies.shared.funny.Circus;
import com.ithaque.funnies.shared.funny.CircusDnDProfile;
import com.ithaque.funnies.shared.funny.CircusRandomAnimationProcessor;
import com.ithaque.funnies.shared.funny.CircusRotateProfile;
import com.ithaque.funnies.shared.funny.DraggableFunny;
import com.ithaque.funnies.shared.funny.DropTargetFunny;
import com.ithaque.funnies.shared.funny.Funny;
import com.ithaque.funnies.shared.funny.HoverFunny;
import com.ithaque.funnies.shared.funny.HoverProcessor;
import com.ithaque.funnies.shared.funny.PanelRing;
import com.ithaque.funnies.shared.funny.RotatableFunny;
import com.ithaque.funnies.shared.funny.ToolProcessor;
import com.ithaque.funnies.shared.funny.TooledFunny;
import com.ithaque.funnies.shared.funny.TooledRing;
import com.ithaque.funnies.shared.funny.standard.ToolbarFunny;

public class GameBoardRing extends AbstractRing implements TooledRing, PanelRing {

	public static final String BACKGROUND_LAYER = "background";
	public static final String BOARD_LAYER = "board";
	public static final String HILIGHT_LAYER = "hilight";
	public static final String TILESET_LAYER = "tileset";
	public static final String PIECES_LAYER = "pieces";
	public static final String INFO_LAYER = "info";
	public static final String ANIMATION_LAYER = "animation";
	public static final String DRAG_LAYER = "drag";
	public static final String TOOLS_LAYER = "tools";
	public static final String PANEL_LAYER = "panel";
	public static final String RANDOM = "random";
	public static final String TOOLBAR = "toolbar";
	
	public static final Color MODAL_GREY = new Color(50, 50, 50);

	public GameBoardRing(Circus circus, float width, float height) {
		super(circus, width, height);
	}

	Layer backgroundLayer;
	Layer boardLayer;
	Layer hilightLayer;
	Layer tilesetLayer;
	Layer piecesLayer;
	Layer infoLayer;
	Layer animationLayer;
	FrameLayer toolsLayer;
	FrameLayer panelLayer;
	Layer dragLayer;
	
	DragProcessor dragProcessor;
	CircusRotateProfile rotateCounterProfile;
	CircusDnDProfile dragCounterProfile;
	ScrollProfile scrollProfile;
	ScalingProcessor scalingProcessor;
	HoverProcessor hoverProcessor;
	ActivationProcessor activationProcessor;
	CircusRandomAnimationProcessor animationProcessor;
	ToolProcessor toolProcessor;
	
	ToolbarFunny toolbar;
	PolygonItem modalGlass;
	
	@Override
	protected Item buildContent(float width, float height) {
		MultiLayered baseLayer = new MultiLayered(-width/2.0f, -height/2.0f, width/2.0f, height/2.0f);
		backgroundLayer = baseLayer.createAttachedLayer(BACKGROUND_LAYER);
		boardLayer = baseLayer.createAttachedLayer(BOARD_LAYER);
		hilightLayer = baseLayer.createAttachedLayer(HILIGHT_LAYER);
		tilesetLayer = baseLayer.createAttachedLayer(TILESET_LAYER);
		piecesLayer = baseLayer.createAttachedLayer(PIECES_LAYER);
		infoLayer = baseLayer.createAttachedLayer(INFO_LAYER);
		animationLayer = baseLayer.createAttachedLayer(ANIMATION_LAYER);
		toolsLayer = new FrameLayer(TOOLS_LAYER,-width/2.0f, -height/2.0f, width/2.0f, height/2.0f);
		baseLayer.addDevice(toolsLayer);
		panelLayer = new FrameLayer(PANEL_LAYER,-width/2.0f, -height/2.0f, width/2.0f, height/2.0f);
		baseLayer.addDevice(panelLayer);
		
		dragLayer = baseLayer.createAttachedLayer(DRAG_LAYER);

		dragProcessor = new DragProcessor();
		rotateCounterProfile = new CircusRotateProfile(this);
		dragProcessor.addDragProfile(rotateCounterProfile);
		dragCounterProfile = new CircusDnDProfile(this);
		dragCounterProfile.setDragLayer(dragLayer);
		dragProcessor.addDragProfile(dragCounterProfile);
		scrollProfile = new ScrollProfile(backgroundLayer); 
		dragProcessor.addDragProfile(scrollProfile);
		scalingProcessor = new ScalingProcessor(1.1f, 0.25f, 4.0f);
		scalingProcessor.addScalable(backgroundLayer);
		hoverProcessor = new HoverProcessor(this);
		activationProcessor= new ActivationProcessor(this);
		animationProcessor=new CircusRandomAnimationProcessor(this, 1000, RANDOM, 4);
		toolProcessor=new ToolProcessor(this);
		
		toolbar = new ToolbarFunny(TOOLBAR, ToolbarFunny.BOTTOM_LEFT, ToolbarFunny.RIBBON_BACKGROUND);
		modalGlass = PolygonItem.createRect(MODAL_GREY, MODAL_GREY, 1.0f, 0.0f, width, height);
		panelLayer.addItem(modalGlass);
		 
		getBoard().addProcessor(dragProcessor);
		getBoard().addProcessor(scalingProcessor);
		getBoard().addProcessor(activationProcessor);
		getBoard().addProcessor(hoverProcessor);
		getBoard().addProcessor(animationProcessor);
		getBoard().addProcessor(toolProcessor);
		
		toolProcessor.registerToolbar(toolbar);
		return baseLayer;
	}

	@Override
	public boolean enterRing(Funny funny) {
		boolean result = super.enterRing(funny);
		if (funny instanceof DraggableFunny) {
			dragCounterProfile.registerDraggableFunny((DraggableFunny)funny);
		}
		if (funny instanceof RotatableFunny) {
			rotateCounterProfile.registerRotatableFunny((RotatableFunny)funny);
		}
		if (funny instanceof DropTargetFunny) {
			dragCounterProfile.registerDroppableFunny((DropTargetFunny)funny);
		}
		if (funny instanceof HoverFunny) {
			hoverProcessor.registerHoverableFunny((HoverFunny)funny);
		}
		if (funny instanceof ActivableFunny) {
			activationProcessor.registerActivableFunny((ActivableFunny)funny);
		}
		if (funny instanceof AnimatedFunny) {
			animationProcessor.registerAnimatedFunny((AnimatedFunny)funny);
		}
		if (funny instanceof TooledFunny) {
			toolProcessor.registerToolFunny((TooledFunny)funny);
		}
		return result;
	}

	@Override
	public boolean exitRing(Funny funny) {
		boolean result = super.exitRing(funny);
		if (funny instanceof DraggableFunny) {
			dragCounterProfile.unregisterDraggableFunny((DraggableFunny)funny);
		}
		if (funny instanceof RotatableFunny) {
			rotateCounterProfile.unregisterRotatableFunny((RotatableFunny)funny);
		}
		if (funny instanceof DropTargetFunny) {
			dragCounterProfile.unregisterDroppableFunny((DropTargetFunny)funny);
		}
		if (funny instanceof HoverFunny) {
			hoverProcessor.unregisterHoverableFunny((HoverFunny)funny);
		}
		if (funny instanceof ActivableFunny) {
			activationProcessor.unregisterActivableFunny((ActivableFunny)funny);
		}
		if (funny instanceof AnimatedFunny) {
			animationProcessor.unregisterAnimatedFunny((AnimatedFunny)funny);
		}
		if (funny instanceof TooledFunny) {
			toolProcessor.unregisterToolFunny((TooledFunny)funny);
		}
		return result;
	}

	@Override
	public GroupItem getToolSupport() {
		return toolsLayer;
	}	

	@Override
	public GroupItem getPanelSupport() {
		return panelLayer;
	}

	@Override
	public void enterModalMode(Animation animation) {
		backgroundLayer.disable();
		boardLayer.disable();
		hilightLayer.disable();
		tilesetLayer.disable();
		piecesLayer.disable();
		infoLayer.disable();
		animationLayer.disable();
		toolsLayer.disable();
		ParallelAnimation parallel = new ParallelAnimation();
		parallel.addAnimation(animation);
		FadingAnimation fading = new FadingAnimation(500, 0.5f).setItem(modalGlass);
		parallel.addAnimation(fading);
		getBoard().launchAnimation(parallel);
	}

	@Override
	public void exitModalMode(Animation animation) {
		ParallelAnimation parallel = new ParallelAnimation();
		SequenceAnimation sequence = new SequenceAnimation() {
			@Override
			public void finish(long time) {
				super.finish(time);
				backgroundLayer.enable();
				boardLayer.enable();
				hilightLayer.enable();
				tilesetLayer.enable();
				piecesLayer.enable();
				infoLayer.enable();
				animationLayer.enable();
				toolsLayer.enable();
			}
		};
		sequence.addAnimation(animation);
		parallel.addAnimation(sequence);
		FadingAnimation fading = new FadingAnimation(500, 0.0f).setItem(modalGlass);
		parallel.addAnimation(fading);
		getBoard().launchAnimation(parallel);
	}
}
