package com.ithaque.funnies.shared.funny.manager;

public class DropFact extends Fact {

	String droppedId;
	String targetId;
	
	public DropFact(String droppedId, String targetId) {
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
