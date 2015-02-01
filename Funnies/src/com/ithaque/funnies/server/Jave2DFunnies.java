package com.ithaque.funnies.server;

import com.ithaque.funnies.client.FunnyManager;
import com.ithaque.funnies.client.platform.gwt.test.GWTTestPlatform;
import com.ithaque.funnies.shared.funny.boardgame.GameBoardCircus;

public class Jave2DFunnies {

	public static void main(String[] args) {
		final Java2DPlatform platform = new Java2DPlatform();
		platform.execute(()->{
			GameBoardCircus gbc = new GameBoardCircus(platform, 1000.0f, 500.0f);
			FunnyManager funnyManager = new FunnyManager(gbc);
			gbc.init(funnyManager);
			funnyManager.init();
		});
	}
}
