package com.ithaque.funnies.client;

import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.items.animations.ImageItemFadingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.MoveAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ScalingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.easing.OutBackEasing;
import com.ithaque.funnies.shared.basic.processors.AbstractDragProfile;
import com.ithaque.funnies.shared.basic.processors.AbstractTargetedDragProfile;
import com.ithaque.funnies.shared.funny.Circus;
import com.ithaque.funnies.shared.funny.Funny;
import com.ithaque.funnies.shared.funny.SimpleSketch;
import com.ithaque.funnies.shared.funny.Sketch;
import com.ithaque.funnies.shared.funny.boardgame.CounterFunny;
import com.ithaque.funnies.shared.funny.boardgame.DiceFunny;
import com.ithaque.funnies.shared.funny.boardgame.TileFunny;
import com.ithaque.funnies.shared.funny.manager.CircusManager;
import com.ithaque.funnies.shared.funny.notifications.AcceptDropTargetQuestion;
import com.ithaque.funnies.shared.funny.notifications.ActivateEvent;
import com.ithaque.funnies.shared.funny.notifications.DropEvent;

public class FunnyManager extends CircusManager {

	Funny lastTarget = null;
	Circus circus;
	DiceFunny dice;
	
	CircusManager.Handler<DropEvent> dropHandler = 
		new Handler<DropEvent>(DropEvent.class, this) {
		@Override
		public Sketch process(DropEvent dropRequest) {
			System.out.println("Drop : "+dropRequest.getDropped().getId()+" on : "+dropRequest.getTarget().getId());
			lastTarget = dropRequest.getTarget();
			return null;
		}
	};

	CircusManager.Handler<ActivateEvent> activateHandler = 
		new Handler<ActivateEvent>(ActivateEvent.class, this) {
		@Override
		public Sketch process(ActivateEvent activateRequest) {
			SimpleSketch sketch = new SimpleSketch();
			System.out.println("Activate : "+activateRequest.getActivated().getId());
			sketch.addAnimaation(dice.rollFor(1));
			return sketch;
		}
	};
		
	CircusManager.Handler<AcceptDropTargetQuestion> acceptDropTargetHandler = 
		new Handler<AcceptDropTargetQuestion>(AcceptDropTargetQuestion.class, this) {
		@Override
		public Sketch process(AcceptDropTargetQuestion adtQuestion) {
			 if (lastTarget==null || lastTarget.getId().equals("t1")!=adtQuestion.getTarget().getId().equals("t1")) {
				 adtQuestion.accept();
			 }
			 return null;
		}
	};

	public FunnyManager(Circus circus) {
		this.circus = circus;
	}
	
	TileFunny createTile(int id, int x, int y) {
		float dx = 13.0f;
		float dy = (float)(13.0f*Math.sqrt(3.0));
		TileFunny.HHexFunny tile = new TileFunny.HHexFunny("t"+id, "hexagon.png", "hhexagon.png", "rhexagon.png", 26.0f, x*dx, y*dy);
		tile.setEnterTargetAnimation(new ImageItemFadingAnimation.Builder(100, "rhexagon.png", 1.0f).setItemKey(AbstractTargetedDragProfile.NEW_TARGET_KEY));
		tile.setExitTargetAnimation(new ImageItemFadingAnimation.Builder(100, "rhexagon.png", 0.0f).setItemKey(AbstractTargetedDragProfile.PREVIOUS_TARGET_KEY));
		tile.setShowAllowedTargetAnimation(new ImageItemFadingAnimation.Builder(100, "hhexagon.png", 0.2f).setItemKey(AbstractTargetedDragProfile.OTHER_TARGET_KEY));
		tile.setHideAllowedTargetAnimation(new ImageItemFadingAnimation.Builder(100, "hhexagon.png", 0.0f).setItemKey(AbstractTargetedDragProfile.OTHER_TARGET_KEY));
		return tile;
	}
	
	CounterFunny createCounter(int id, String url, float x, float y) {
		CounterFunny counter = new CounterFunny("c"+id, url+".png");
		counter.setLocation(new Location(x, y));
		counter.setBeginDragAnimation(new ScalingAnimation.Builder(500, 1.1f).setItemKey(AbstractDragProfile.DRAGGED_ITEM_KEY));
		counter.setAdjustLocationAnimation(new MoveAnimation.Builder(new OutBackEasing.Builder(1000)).
				setItemKey(AbstractDragProfile.DRAGGED_ITEM_KEY).setLocationKey(AbstractDragProfile.DROP_LOCATION_KEY));
		counter.setDraggedDropAnimation(new ScalingAnimation.Builder(500, 1.0f).setItemKey(AbstractDragProfile.DRAGGED_ITEM_KEY));
		return counter;
	}
	
	DiceFunny createDiceFunny() {
		DiceFunny dice = new DiceFunny("d6", -300.0f, -100.0f, "d{0}.png", 1, 6);
		return dice;
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
		
		circus.enterRing(dice=createDiceFunny());
	}


}
