package com.ithaque.funnies.shared.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Animation {

	public static final long INTERVAL = 40;
	
	Item item;
	boolean managed = false;

	static Map<Item, List<Animation>> programmed = new HashMap<Item, List<Animation>>();

	public Animation() {
	}
	
	public void manage() {
		managed = true;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	public void launchFor() {
		if (managed || register()) {
			launch();
		}
	}

	protected boolean register() {
		boolean launch = false;
		List<Animation> forItem = programmed.get(item);
		if (forItem==null || forItem.isEmpty()) {
			if (forItem==null) {
				forItem = new ArrayList<Animation>();
				programmed.put(item, forItem);
			}
			launch = true;
		}
		forItem.add(this);
		return launch;
	}

	protected void unregister() {
		List<Animation> forItem = programmed.get(item);
		forItem.remove(this);
		if (forItem.isEmpty()) {
			programmed.remove(forItem);
		}
		else {
			Animation nextAnimation = forItem.get(0);
			nextAnimation.launch();
		}
	}
	
	public void launch() {
		registerOnBoard(item);
	}

	protected void registerOnBoard(Item item) {
		this.item = item;
		if (!managed) {
			item.getBoard().launchAnimation(this);
		}
	}
	
	public boolean animate(long time) {
		if (time+Animation.INTERVAL>=getEndTime()) {
			return false;
		}
		else {
			return executeAnimation(time);
		}
	}

	protected abstract boolean executeAnimation(long time);

	public boolean start(long time) {
		return true;
	}

	public void finish(long time) {
		if (!managed) {
			unregister();
		}
	}
	
//	public abstract Animation duplicate();

	public Item getItem() {
		return item;
	}

	public abstract long getDuration();
	
	public abstract long getEndTime();
	
	public interface Factory {
		Animation create();
	}
}
