package com.ithaque.funnies.shared.funny.notifications;

import com.ithaque.funnies.shared.funny.Funny;
import com.ithaque.funnies.shared.funny.manager.Notification;

public class ActivateEvent extends Notification {

	Funny activated;
	
	public ActivateEvent(Funny activated) {
		super();
		this.activated = activated;
	}

	public Funny getActivated() {
		return activated;
	}
	
}
