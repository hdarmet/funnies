package com.ithaque.funnies.shared.funny.manager;

public interface CircusManager {

	void process(Fact fact);
	
	Answer respond(Question question);
	
}
