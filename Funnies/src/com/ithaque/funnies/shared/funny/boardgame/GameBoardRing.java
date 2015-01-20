package com.ithaque.funnies.shared.funny.boardgame;

import com.ithaque.funnies.shared.basic.FrameLayer;
import com.ithaque.funnies.shared.basic.GroupItem;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.MultiLayered;
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
import com.ithaque.funnies.shared.funny.RotatableFunny;
import com.ithaque.funnies.shared.funny.ToolProcessor;
import com.ithaque.funnies.shared.funny.TooledFunny;
import com.ithaque.funnies.shared.funny.TooledRing;
import com.ithaque.funnies.shared.funny.standard.ToolbarFunny;

public class GameBoardRing extends AbstractRing implements TooledRing {

	private static final String BACKGROUND_LAYER = "background";
	private static final String BOARD_LAYER = "board";
	private static final String HILIGHT_LAYER = "hilight";
	private static final String TILESET_LAYER = "tileset";
	private static final String PIECES_LAYER = "pieces";
	private static final String INFO_LAYER = "info";
	private static final String ANIMATION_LAYER = "animation";
	private static final String DRAG_LAYER = "drag";
	private static final String TOOLS_LAYER = "tools";
	private static final String RANDOM = "random";
	public static final String TOOLBAR = "toolbar";

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
	FrameLayer frameLayer;
	Layer dragLayer;
	
	DragProcessor dragProcessor;
	CircusRotateProfile rotateCounterProfile;
	CircusDnDProfile dragCounterProfile;
	ScrollProfile scrollProfile;
	ScalingProcessor scalingProcessor;
	ActivationProcessor activationProcessor;
	CircusRandomAnimationProcessor animationProcessor;
	ToolProcessor toolProcessor;
	
	ToolbarFunny toolbar;
	
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
		frameLayer = new FrameLayer(TOOLS_LAYER,-width/2.0f, -height/2.0f, width/2.0f, height/2.0f);
		baseLayer.addDevice(frameLayer);
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
		activationProcessor= new ActivationProcessor(this);
		animationProcessor=new CircusRandomAnimationProcessor(this, 1000, RANDOM, 4);
		toolProcessor=new ToolProcessor(this);
		
		toolbar = new ToolbarFunny(TOOLBAR, ToolbarFunny.BOTTOM_MIDDLE);
		 
		getBoard().addProcessor(dragProcessor);
		getBoard().addProcessor(scalingProcessor);
		getBoard().addProcessor(activationProcessor);
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
		return frameLayer;
	}	
}
