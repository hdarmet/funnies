package com.ithaque.funnies.shared.funny;




public interface Funny {

	String getId();
	
	void enterRing(Ring ring);

	void exitRing(Ring ring);
	
	Ring getRing();

	void addObserver(FunnyObserver observer);

	void removeObserver(FunnyObserver observer);


	
}
