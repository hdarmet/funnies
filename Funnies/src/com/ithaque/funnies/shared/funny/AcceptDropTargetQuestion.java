package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.funny.manager.Notification;

public class AcceptDropTargetQuestion extends Notification {

	Funny dropped;
	Funny target;
	boolean accepted = false;
	
	public AcceptDropTargetQuestion(Funny dropped, Funny target) {
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
	
	public void accept() {
		accepted = true;
	}
	
	public boolean isAccepted() {
		return accepted;
	}
}
