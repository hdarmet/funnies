package com.ithaque.funnies.shared.funny;




public interface Funny {

	String getId();
	
	void enterRing(AbstractRing ring);

	void exitRing(AbstractRing ring);
	
	AbstractRing getRing();

	void addObserver(FunnyObserver observer);

	void removeObserver(FunnyObserver observer);

	void addSpy(FunnySpy spy);
	
	void removeSpy(FunnySpy spy);
}
