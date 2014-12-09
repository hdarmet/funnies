package com.ithaque.funnies.client;

import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.items.animations.ImageItemFadingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ItemChangeAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ItemJumpAnimation;
import com.ithaque.funnies.shared.basic.items.animations.OutBackEasing;
import com.ithaque.funnies.shared.funny.Circus;
import com.ithaque.funnies.shared.funny.Funny;
import com.ithaque.funnies.shared.funny.boardgame.CounterFunny;
import com.ithaque.funnies.shared.funny.boardgame.TileFunny;
import com.ithaque.funnies.shared.funny.manager.CircusManager;
import com.ithaque.funnies.shared.funny.notifications.AcceptDropTargetQuestion;
import com.ithaque.funnies.shared.funny.notifications.DropEvent;

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
	
	TileFunny createTile(int id, int x, int y) {
		float dx = 13.0f;
		float dy = (float)(13.0f*Math.sqrt(3.0));
		TileFunny.HHexFunny tile = new TileFunny.HHexFunny("t"+id, "hexagon.png", "hhexagon.png", "rhexagon.png", 26.0f, x*dx, y*dy);
		tile.setEnterTargetAnimation(new ImageItemFadingAnimation(100).fade("rhexagon.png", 1.0f));
		tile.setExitTargetAnimation(new ImageItemFadingAnimation(100).fade("rhexagon.png", 0.0f));
		tile.setShowAllowedTargetAnimation(new ImageItemFadingAnimation(0).fade("hhexagon.png", 0.2f));
		tile.setHideAllowedTargetAnimation(new ImageItemFadingAnimation(0).fade("hhexagon.png", 0.0f));
		return tile;
	}
	
	CounterFunny createCounter(int id, String url, float x, float y) {
		CounterFunny counter = new CounterFunny("c"+id, url+".png");
		counter.setLocation(new Location(x, y));
		counter.setBeginDragAnimation(new ItemChangeAnimation(500, null, null, 1.1f));
//		counter.setAdjustLocationAnimation(new ItemChangeAnimation(new OutBackEasing(1000), null, null, null));
		counter.setAdjustLocationAnimation(new ItemJumpAnimation(1000, 1.0f));
		counter.setDraggedDropAnimation(new ItemChangeAnimation(500, null, null, 1.0f));
		return counter;
	}
	
	public void init() {
		for (int col=-6; col<=6; col++) {
			int inc = (col%2)==0?1:0;
			for (int row=-9+inc; row<=9; row+=2) {
				circus.enterRing(createTile(col*100+(row/2), col*3, row));
			}
		}

		circus.enterRing(createCounter(1, "u1", -400, -50));
		circus.enterRing(createCounter(1, "u1", -400, 0));
		circus.enterRing(createCounter(1, "u1", -400, 50));
		circus.enterRing(createCounter(1, "u2", 400, -50));
		circus.enterRing(createCounter(1, "u2", 400, 0));
		circus.enterRing(createCounter(1, "u2", 400, 50));
	}


}
