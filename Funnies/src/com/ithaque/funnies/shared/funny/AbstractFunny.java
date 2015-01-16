package com.ithaque.funnies.shared.funny;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.funny.FunnyObserver.ChangeType;

public abstract class AbstractFunny implements Funny {
	
	List<FunnyObserver> observers = new ArrayList<FunnyObserver>();
	String id;
	AbstractRing ring;
	
	public AbstractFunny(String id) {
		this.id = id;
	}
	
	@Override
	public void enterRing(AbstractRing ring) {
		this.ring = ring;
		fire(ChangeType.ENTER_RING);
	}

	@Override
	public void exitRing(AbstractRing ring) {
		this.ring = null;
		fire(ChangeType.EXIT_RING);
	}
	
	public void fire(ChangeType changeType) {
		if (observers!=null) {
			for (FunnyObserver observer : new ArrayList<FunnyObserver>(observers)) {
				observer.change(changeType, this);
			}
		}
	}

	@Override
	public void addObserver(FunnyObserver observer) {
		if (observers==null) {
			observers = new ArrayList<FunnyObserver>();
		}
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}
	
	@Override
	public void removeObserver(FunnyObserver observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
			if (observers.isEmpty()) {
				observers=null;
			}
		}
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public AbstractRing getRing() {
		return ring;
	}
	
}
