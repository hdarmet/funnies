package com.ithaque.funnies.client;

import com.ithaque.funnies.shared.funny.AcceptDropTargetQuestion;
import com.ithaque.funnies.shared.funny.Circus;
import com.ithaque.funnies.shared.funny.DropEvent;
import com.ithaque.funnies.shared.funny.Funny;
import com.ithaque.funnies.shared.funny.boardgame.CounterFunny;
import com.ithaque.funnies.shared.funny.boardgame.TileFunny;
import com.ithaque.funnies.shared.funny.manager.CircusManager;

public class FunnyManager extends CircusManager {

	Funny lastTarget = null;
	Circus circus;
	
	CircusManager.Handler<DropEvent> dropHandler = 
		new Handler<DropEvent>(DropEvent.class, this) {
		@Override
		public void process(DropEvent dropRequest) {
			System.out.println("Drop : "+dropRequest.getDropped().getId()+" on : "+dropRequest.getTarget().getId());
			lastTarget = dropRequest.getTarget();
		}
	};
	
	CircusManager.Handler<AcceptDropTargetQuestion> acceptDropTargetHandler = 
		new Handler<AcceptDropTargetQuestion>(AcceptDropTargetQuestion.class, this) {
		@Override
		public void process(AcceptDropTargetQuestion adtQuestion) {
			 if (lastTarget==null || lastTarget.getId().equals("t1")!=adtQuestion.getTarget().getId().equals("t1")) {
				 adtQuestion.accept();
			 }
		}
	};

	public FunnyManager(Circus circus) {
		this.circus = circus;
	}
	
	public void init() {
		float dx = 13.0f;
		float dy = (float)(13.0f*Math.sqrt(3.0));
		circus.enterRing(new TileFunny.HexFunny("t1", "hexagon.png", "rhexagon.png", 26.0f, 0.0f, 0.0f));
		circus.enterRing(new TileFunny.HexFunny("t2", "hexagon.png", "rhexagon.png", 26.0f, -3*dx, -dy));
		circus.enterRing(new TileFunny.HexFunny("t3", "hexagon.png", "rhexagon.png", 26.0f, 0, -dy*2));
		circus.enterRing(new TileFunny.HexFunny("t4", "hexagon.png", "rhexagon.png", 26.0f, 3*dx, -dy));
		circus.enterRing(new TileFunny.HexFunny("t5", "hexagon.png", "rhexagon.png", 26.0f, 3*dx, dy));
		circus.enterRing(new TileFunny.HexFunny("t6", "hexagon.png", "rhexagon.png", 26.0f, 0, dy*2));
		circus.enterRing(new TileFunny.HexFunny("t7", "hexagon.png", "rhexagon.png", 26.0f, -3*dx, dy));
		circus.enterRing(new CounterFunny("c1", "hhexagon.png"));
	}


}
