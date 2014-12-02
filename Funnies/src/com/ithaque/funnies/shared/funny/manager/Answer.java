package com.ithaque.funnies.shared.funny.manager;


public class Answer {
	
	public static class BooleanAnswer extends Answer {
		boolean answer;
		
		public BooleanAnswer(boolean answer) {
			this.answer = answer;
		}
		
		public boolean getAnswer() {
			return answer;
		}
	}
}
