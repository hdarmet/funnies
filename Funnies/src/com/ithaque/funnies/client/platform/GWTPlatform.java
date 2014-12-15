package com.ithaque.funnies.client.platform;

import java.util.Date;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event;
import com.ithaque.funnies.shared.basic.Graphics;
import com.ithaque.funnies.shared.basic.Platform;
import com.ithaque.funnies.shared.basic.Token;

public class GWTPlatform implements Platform {

	GWTGraphics graphics = new GWTGraphics(this);
	Board board;
	
	@Override
	public Graphics getGraphics() {
		return graphics;
	}

	@Override
	public long getTime() {
		return new Date().getTime();
	}
	
	@Override
	public Token start(Board board) {
		Token token = graphics.start();
		this.board = board;
	    Timer timer = new Timer() {
	        @Override
	        public void run() {
	        	GWTPlatform.this.board.animate();
	        	GWTPlatform.this.board.render();
	        }
	    };
	    timer.scheduleRepeating((int)Animation.INTERVAL);
	    return token;
	}

	public void sendEvent(Event event) {
		board.processEvent(event);
	}

	@Override
	public boolean isReady() {
		return graphics.isReady();
	}

	@Override
	public float randomize() {
		return (float)Random.nextDouble();
	}

}
