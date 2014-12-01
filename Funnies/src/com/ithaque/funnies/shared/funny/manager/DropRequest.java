package com.ithaque.funnies.shared.funny.manager;

public class DropRequest extends CircusManager.Request {

	String droppedId;
	String targetId;
	
	public DropRequest(String droppedId, String targetId) {
		super();
		this.droppedId = droppedId;
		this.targetId = targetId;
	}

	public String getDroppedId() {
		return droppedId;
	}
	
	public String getTargetId() {
		return targetId;
	}
	
}
