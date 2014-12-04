package com.ithaque.funnies.shared.funny;

import java.util.HashSet;
import java.util.Set;

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
	
	Set<Funny> funnies = new HashSet<Funny>();
	
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
		return new Ring(this, width, height);
	}
	
	public boolean enterRing(Funny funny) {
		if (!funnies.contains(funny)) {
			if (ring.enterRing(funny)) {
				funnies.add(funny);
				return true;
			}
		}
		return false;
	}

	public void notify(Notification request) {
		if (manager!=null) {
			manager.process(request);
		}
	}
	
}
