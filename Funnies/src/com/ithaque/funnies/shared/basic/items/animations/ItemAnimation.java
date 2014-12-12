package com.ithaque.funnies.shared.basic.items.animations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Item;

public abstract class ItemAnimation implements Animation {
	
	Item item;
	Easing easing;
	boolean managed = false;

	static Map<Item, List<ItemAnimation>> programmed = new HashMap<Item, List<ItemAnimation>>();

	public ItemAnimation() {
	}
	
	public ItemAnimation(Easing easing) {
		this.easing = easing;
	}

	public ItemAnimation(long duration) {
		this(new SineInOutEasing(duration));
	}
	
	public void manage() {
		managed = true;
	}
	
	public void launchFor(Item item) {
		this.item = item;
		if (managed || register()) {
			launch(item);
		}
	}

	protected boolean register() {
		boolean launch = false;
		List<ItemAnimation> forItem = programmed.get(item);
		if (forItem==null || forItem.isEmpty()) {
			if (forItem==null) {
				forItem = new ArrayList<ItemAnimation>();
				programmed.put(item, forItem);
			}
			launch = true;
		}
		forItem.add(this);
		return launch;
	}

	protected void unregister() {
		List<ItemAnimation> forItem = programmed.get(item);
		forItem.remove(this);
		if (forItem.isEmpty()) {
			programmed.remove(forItem);
		}
		else {
			ItemAnimation nextAnimation = forItem.get(0);
			nextAnimation.launch(item);
		}
	}
	
	protected Easing getEasing() {
		return easing;
	}
	
	protected void launch(Item item) {
		registerOnBoard(item);
		this.easing.launch(item.getBoard());
	}

	protected void registerOnBoard(Item item) {
		this.item = item;
		if (!managed) {
			item.getBoard().launchAnimation(this);
		}
	}
	
	@Override
	public boolean animate(long time) {
		if (time+Animation.INTERVAL>=getEndTime()) {
			System.out.println("T:"+time+" "+getEndTime()+" "+this);
			return false;
		}
		else {
			return executeAnimation(easing, time);
		}
	}

	protected abstract boolean executeAnimation(Easing easing, long time);

	@Override
	public boolean start(long time) {
		return true;
	}

	@Override
	public void finish(long time) {
		if (!managed) {
			unregister();
		}
	}
	
	@Override
	public abstract ItemAnimation duplicate();

	public Item getItem() {
		return item;
	}

	public long getDuration() {
		return getEasing().getDuration();
	}
	
	public long getEndTime() {
		return getEasing().getEndTime();
	}
}
