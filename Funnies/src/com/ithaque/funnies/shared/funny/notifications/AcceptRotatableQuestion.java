package com.ithaque.funnies.shared.funny.notifications;

import com.ithaque.funnies.shared.funny.Funny;
import com.ithaque.funnies.shared.funny.manager.Notification;

public class AcceptRotatableQuestion extends Notification {

	Funny rotatable;
	boolean accepted = false;
	
	public AcceptRotatableQuestion(Funny rotatable) {
		super();
		this.rotatable = rotatable;
	}

	public Funny getRotatable() {
		return rotatable;
	}
	
	public void accept() {
		accepted = true;
	}
	
	public boolean isAccepted() {
		return accepted;
	}
}
