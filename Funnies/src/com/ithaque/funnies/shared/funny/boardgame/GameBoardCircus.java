package com.ithaque.funnies.shared.funny.boardgame;

import com.ithaque.funnies.shared.basic.Platform;
import com.ithaque.funnies.shared.funny.Circus;
import com.ithaque.funnies.shared.funny.AbstractRing;

public class GameBoardCircus extends Circus {

	public GameBoardCircus(Platform platform, float width, float height) {
		super(platform, width, height);
	}

	@Override
	public AbstractRing buildRing(float width, float height) {
		return new GameBoardRing(this, width, height);
	}
	
	
}
