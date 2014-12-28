package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.funny.manager.Notification;

public class Ring {

	private static final String RING = "Ring";
	
	Circus circus;
	Item layer;
	float width;
	float height;
	
	public Ring(Circus circus, float width, float height) {
		this.circus = circus;
		this.width = width;
		this.height = height;
	}

	public void init() {
		layer = buildContent(width, height);
		circus.board.addItem(layer);
	}
	
	protected Board getBoard() {
		return circus.board;
	}
	
	protected Item buildContent(float width, float height) {
		return new Layer(RING, -width/2.0f, -height/2.0f, width/2.0f, height/2.0f);
	}
	
	protected boolean enterRing(Funny funny) {
		funny.enterRing(this);
		return true;
	}

	protected boolean exitRing(Funny funny) {
		funny.exitRing(this);
		return true;
	}
	
	public void notify(Notification fact) {
		circus.notify(fact);
	}

}
