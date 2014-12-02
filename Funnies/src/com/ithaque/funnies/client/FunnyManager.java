package com.ithaque.funnies.client;

import com.ithaque.funnies.shared.funny.manager.AcceptDropTargetQuestion;
import com.ithaque.funnies.shared.funny.manager.Answer;
import com.ithaque.funnies.shared.funny.manager.Answer.BooleanAnswer;
import com.ithaque.funnies.shared.funny.manager.CircusManager;
import com.ithaque.funnies.shared.funny.manager.DropFact;
import com.ithaque.funnies.shared.funny.manager.Fact;
import com.ithaque.funnies.shared.funny.manager.Question;

public class FunnyManager implements CircusManager {

	String lastTarget = null;
	
	@Override
	public void process(Fact fact) {
		if (fact instanceof DropFact) {
			DropFact dropRequest = (DropFact)fact;
			System.out.println("Drop : "+dropRequest.getDroppedId()+" on : "+dropRequest.getTargetId());
			lastTarget = dropRequest.getTargetId();
		}
	}

	@Override
	public Answer respond(Question question) {
		if (question instanceof AcceptDropTargetQuestion) {
			 AcceptDropTargetQuestion adtQuestion = (AcceptDropTargetQuestion)question;
			 if (lastTarget!=null && lastTarget.equals("t1")) {
				 return new BooleanAnswer(!adtQuestion.getTargetId().equals("t1"));
			 }
			 else {
				 return new BooleanAnswer(adtQuestion.getTargetId().equals("t1"));
			 }
		}
		return null;
	}

}
