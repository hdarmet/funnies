package com.ithaque.funnies.shared.funny.manager;

public class AcceptDropTargetQuestion extends Question {

	String droppedId;
	String targetId;
	
	public AcceptDropTargetQuestion(String droppedId, String targetId) {
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
