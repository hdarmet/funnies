package com.ithaque.funnies.shared.funny.boardgame;

import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.MultiLayered;
import com.ithaque.funnies.shared.basic.processors.DragProcessor;
import com.ithaque.funnies.shared.basic.processors.ScalingProcessor;
import com.ithaque.funnies.shared.basic.processors.ScrollProfile;
import com.ithaque.funnies.shared.funny.ActivableFunny;
import com.ithaque.funnies.shared.funny.ActivationProcessor;
import com.ithaque.funnies.shared.funny.Circus;
import com.ithaque.funnies.shared.funny.CircusDnDProfile;
import com.ithaque.funnies.shared.funny.DraggableFunny;
import com.ithaque.funnies.shared.funny.DropTargetFunny;
import com.ithaque.funnies.shared.funny.Funny;
import com.ithaque.funnies.shared.funny.Ring;

public class GameBoardRing extends Ring {

	private static final String BACKGROUND_LAYER = "background";
	private static final String BOARD_LAYER = "board";
	private static final String HILIGHT_LAYER = "hilight";
	private static final String TILESET_LAYER = "tileset";
	private static final String PIECES_LAYER = "pieces";
	private static final String DRAG_LAYER = "drag";

	public GameBoardRing(Circus circus, float width, float height) {
		super(circus, width, height);
	}

	Layer backgroundLayer;
	Layer boardLayer;
	Layer hilightLayer;
	Layer tilesetLayer;
	Layer piecesLayer;
	Layer dragLayer;
	DragProcessor dragProcessor;
	CircusDnDProfile dragCounterProfile;
	ScrollProfile scrollProfile;
	ScalingProcessor scalingProcessor;
	ActivationProcessor activationProcessor;
	
	@Override
	protected Item buildContent(float width, float height) {
		MultiLayered baseLayer = new MultiLayered(-width/2.0f, -height/2.0f, width/2.0f, height/2.0f);
		backgroundLayer = baseLayer.addLayer(BACKGROUND_LAYER);
		boardLayer = baseLayer.addLayer(BOARD_LAYER);
		hilightLayer = baseLayer.addLayer(HILIGHT_LAYER);
		tilesetLayer = baseLayer.addLayer(TILESET_LAYER);
		piecesLayer = baseLayer.addLayer(PIECES_LAYER);
		dragLayer = baseLayer.addLayer(DRAG_LAYER);
		getBoard().addItem(baseLayer);

		dragProcessor = new DragProcessor();
		dragCounterProfile = new CircusDnDProfile(this);
		dragProcessor.addDragProfile(dragCounterProfile);
		scrollProfile = new ScrollProfile(backgroundLayer); 
		dragProcessor.addDragProfile(scrollProfile);
		scalingProcessor = new ScalingProcessor(1.1f, 0.25f, 4.0f);
		scalingProcessor.addScalable(backgroundLayer);
		activationProcessor= new ActivationProcessor(this);
		
		getBoard().addProcessor(dragProcessor);
		getBoard().addProcessor(scalingProcessor);
		getBoard().addProcessor(activationProcessor);
		return baseLayer;
	}

	@Override
	protected boolean enterRing(Funny funny) {
		boolean result = super.enterRing(funny);
		if (funny instanceof DraggableFunny) {
			dragCounterProfile.registerDraggableFunny((DraggableFunny)funny);
		}
		if (funny instanceof DropTargetFunny) {
			dragCounterProfile.registerDroppableFunny((DropTargetFunny)funny);
		}
		if (funny instanceof ActivableFunny) {
			activationProcessor.registerActivableFunny((ActivableFunny)funny);
		}
		return result;
	}

}
