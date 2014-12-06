package com.ithaque.funnies.shared.funny.notifications;

import com.ithaque.funnies.shared.funny.Funny;
import com.ithaque.funnies.shared.funny.manager.Notification;

public class DropEvent extends Notification {

	Funny dropped;
	Funny target;
	
	public DropEvent(Funny dropped, Funny target) {
		super();
		this.dropped = dropped;
		this.target = target;
	}

	public Funny getDropped() {
		return dropped;
	}
	
	public Funny getTarget() {
		return target;
	}
	
}
