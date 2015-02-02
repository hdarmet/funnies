package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.funny.manager.Notification;

public abstract class AbstractRing implements Ring {

	private static final String RING = "Ring";
	
	Circus circus;
	Item layer;
	float width;
	float height;
	
	public AbstractRing(Circus circus, float width, float height) {
		this.circus = circus;
		this.width = width;
		this.height = height;
	}

	@Override
	public void init() {
		layer = buildContent(width, height);
		circus.board.addItem(layer);
	}
	
	public Board getBoard() {
		return circus.board;
	}
	
	protected Item buildContent(float width, float height) {
		return new Layer(RING, -width/2.0f, -height/2.0f, width/2.0f, height/2.0f);
	}
	
	@Override
	public boolean enterRing(Funny funny) {
		funny.enterRing(this);
		return true;
	}

	@Override
	public boolean exitRing(Funny funny) {
		funny.exitRing(this);
		return true;
	}
	
	@Override
	public void notify(Notification fact) {
		circus.notify(fact);
	}

	@Override
	public float getWidth() {
		return width;
	}
	
	@Override
	public float getHeight() {
		return height;
	}
	
	@Override
	public void launch(Animation animation) {
		getBoard().launchAnimation(animation);
	}
}
