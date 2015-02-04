package com.ithaque.funnies.shared.funny;

import java.util.HashMap;
import java.util.Map;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.Trace;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Platform;
import com.ithaque.funnies.shared.funny.manager.CircusManager;
import com.ithaque.funnies.shared.funny.manager.Notification;

public class Circus {
	
	Platform platform;
	Board board;
	Ring ring;
	float width;
	float height;
	CircusManager manager;
	
	Map<String, Funny> funnies = new HashMap<String, Funny>();
		
	public Circus(Platform platform, float width, float height) {
		this.platform = platform;
		this.width = width;
		this.height = height;
	}
	
	public void init(CircusManager manager) {
		this.manager = manager;
		board = buildBoard(platform);
		ring = buildRing(width, height);
		ring.init();
		board.start();
	}
	
	public Board buildBoard(Platform platform) {
		return new Board(platform);
	}
	
	public Ring buildRing(float width, float height) {
		return new AbstractRing(this, width, height) {};
	}
	
	public boolean enterRing(Funny funny) {
		register(funny);
		return ring.enterRing(funny);
	}

	public boolean exitRing(Funny funny) {
		unregister(funny);
		return ring.exitRing(funny);
	}
	
	public void notify(Notification request) {
		if (manager!=null) {
			Sketch sketch = manager.process(request);
			if (sketch!=null) {
				sketch.play(this);
			}
		}
	}

	public Board getBoard() {
		return board;
	}

	void unregister(Funny funny) {
		if (funnies.get(funny.getId())==null || funnies.get(funny.getId())!=funny) {
			throw new IllegalInvokeException();
		}
		funnies.remove(funny.getId());
	}
	
	void register(Funny funny) {
		if (Trace.debug) {
			Trace.debug("Register funny : "+funny.getId()+"\n");
		}
		if (funnies.get(funny.getId())!=null) {
			throw new IllegalInvokeException();
		}
		funnies.put(funny.getId(), funny);
	}

}
