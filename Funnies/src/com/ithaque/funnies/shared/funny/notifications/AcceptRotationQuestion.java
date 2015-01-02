package com.ithaque.funnies.shared.funny.notifications;

import com.ithaque.funnies.shared.funny.Funny;
import com.ithaque.funnies.shared.funny.manager.Notification;

public class AcceptRotationQuestion extends Notification {

	Funny rotatable;
	float angle;
	boolean accepted = false;
	
	public AcceptRotationQuestion(Funny rotatable, float angle) {
		super();
		this.rotatable = rotatable;
		this.angle = angle;
	}

	public Funny getRotatable() {
		return rotatable;
	}
	
	public float getRotation() {
		return angle;
	}
	
	public void accept() {
		accepted = true;
	}
	
	public boolean isAccepted() {
		return accepted;
	}
}
