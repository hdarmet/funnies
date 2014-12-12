package com.ithaque.funnies.shared.basic;

public interface Platform {

	Graphics getGraphics();

	long getTime();

	Integer start(Board board);

	boolean isReady();
	
	float randomize();
}
