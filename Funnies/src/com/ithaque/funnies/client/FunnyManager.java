package com.ithaque.funnies.client;

import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.items.animations.FaceFadingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.MoveAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ScalingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.easing.OutBackEasing;
import com.ithaque.funnies.shared.basic.processors.AbstractDragProfile;
import com.ithaque.funnies.shared.basic.processors.AbstractTargetedDragProfile;
import com.ithaque.funnies.shared.funny.Circus;
import com.ithaque.funnies.shared.funny.Funny;
import com.ithaque.funnies.shared.funny.SimpleSketch;
import com.ithaque.funnies.shared.funny.Sketch;
import com.ithaque.funnies.shared.funny.boardgame.ArrowFunny;
import com.ithaque.funnies.shared.funny.boardgame.CounterFunny;
import com.ithaque.funnies.shared.funny.boardgame.DiceFunny;
import com.ithaque.funnies.shared.funny.boardgame.EphemeralFunny;
import com.ithaque.funnies.shared.funny.boardgame.TileFunny;
import com.ithaque.funnies.shared.funny.manager.AbstracCircusManager;
import com.ithaque.funnies.shared.funny.notifications.AcceptDropTargetQuestion;
import com.ithaque.funnies.shared.funny.notifications.ActivateEvent;
import com.ithaque.funnies.shared.funny.notifications.DropEvent;

public class FunnyManager extends AbstracCircusManager {

	Funny lastTarget = null;
	Circus circus;
	DiceFunny dice;
	EphemeralFunny boom;
	TileFunny tile;
	CounterFunny counter1;
	CounterFunny counter2;
	
	AbstracCircusManager.Handler<DropEvent> dropHandler = 
		new Handler<DropEvent>(DropEvent.class, this) {
		@Override
		public Sketch process(DropEvent dropRequest) {
			System.out.println("Drop : "+dropRequest.getDropped().getId()+" on : "+dropRequest.getTarget().getId());
			lastTarget = dropRequest.getTarget();
			return null;
		}
	};

	AbstracCircusManager.Handler<ActivateEvent> activateHandler = 
		new Handler<ActivateEvent>(ActivateEvent.class, this) {
		@Override
		public Sketch process(ActivateEvent activateRequest) {
			SimpleSketch sketch = new SimpleSketch();
			System.out.println("Activate : "+activateRequest.getActivated().getId());
			sketch.addAnimation(dice.rollFor(1));
			sketch.addAnimation(boom.play(0.0f, 0.0f, 1000L));
			circus.enterRing(createArrow());
			return sketch;
		}
	};
		
	AbstracCircusManager.Handler<AcceptDropTargetQuestion> acceptDropTargetHandler = 
		new Handler<AcceptDropTargetQuestion>(AcceptDropTargetQuestion.class, this) {
		@Override
		public Sketch process(AcceptDropTargetQuestion adtQuestion) {
			 if (lastTarget==null || lastTarget.getId().equals("t[0,0]")!=adtQuestion.getTarget().getId().equals("t[0,0]")) {
				 adtQuestion.accept();
			 }
			 return null;
		}
	};

	public FunnyManager(Circus circus) {
		this.circus = circus;
	}
	
	TileFunny createTile(String id, int x, int y) {
		float dx = 13.0f;
		float dy = (float)(13.0f*Math.sqrt(3.0));
		TileFunny.HHexFunny tile = new TileFunny.HHexFunny("t"+id, "hexagon.png", "hhexagon.png", "rhexagon.png", 26.0f, x*dx, y*dy);
		tile.setEnterTargetAnimation(new FaceFadingAnimation.Builder(100, 1.0f).setItemKey(AbstractTargetedDragProfile.NEW_TARGET_KEY));
		tile.setExitTargetAnimation(new FaceFadingAnimation.Builder(100, 0.0f).setItemKey(AbstractTargetedDragProfile.PREVIOUS_TARGET_KEY));
		tile.setShowAllowedTargetAnimation(new FaceFadingAnimation.Builder(100, 0.2f).setItemKey(AbstractTargetedDragProfile.OTHER_TARGET_KEY));
		tile.setHideAllowedTargetAnimation(new FaceFadingAnimation.Builder(100, 0.0f).setItemKey(AbstractTargetedDragProfile.OTHER_TARGET_KEY));
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
	
	EphemeralFunny createExplosion() {
		EphemeralFunny explosion = new EphemeralFunny("boom", "explosion.png", 10, 4, 930.0f, 400.0f);
		return explosion;
	}

	ArrowFunny createArrow() {
		ArrowFunny arrow = new ArrowFunny("arrow", counter1, counter2);
		return arrow;
	}
	
	public void init() {
		for (int col=-6; col<=6; col++) {
			int inc = (col%2)==0?1:0;
			for (int row=-9+inc; row<=9; row+=2) {
				circus.enterRing(tile = createTile("["+col+","+row+"]", col*3, row));
			}
		}

		circus.enterRing(counter1 = createCounter(1, "u1", -400, -50));
		circus.enterRing(createCounter(2, "u1", -400, 0));
		circus.enterRing(createCounter(3, "u1", -400, 50));
		circus.enterRing(createCounter(4, "u2", 400, -50));
		circus.enterRing(createCounter(5, "u2", 400, 0));
		circus.enterRing(counter2 = createCounter(6, "u2", 400, 50));
		
		circus.enterRing(dice=createDiceFunny());
		circus.enterRing(boom=createExplosion());
		

	}


}
