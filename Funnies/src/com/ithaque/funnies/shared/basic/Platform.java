package com.ithaque.funnies.shared.basic;

public interface Platform {

	Graphics getGraphics();

	long getTime();

	Token start(Board board);

	boolean isReady();
	
	float randomize();
}
