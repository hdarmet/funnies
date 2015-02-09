package com.ithaque.funnies.client;

import com.ithaque.funnies.shared.Location;
import com.ithaque.funnies.shared.basic.Color;
import com.ithaque.funnies.shared.basic.Font;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.StatusItem;
import com.ithaque.funnies.shared.funny.Circus;
import com.ithaque.funnies.shared.funny.FunnySpy;
import com.ithaque.funnies.shared.funny.Icon;
import com.ithaque.funnies.shared.funny.SimpleSketch;
import com.ithaque.funnies.shared.funny.Sketch;
import com.ithaque.funnies.shared.funny.boardgame.ArrowFunny;
import com.ithaque.funnies.shared.funny.boardgame.CounterFunny;
import com.ithaque.funnies.shared.funny.boardgame.DiceFunny;
import com.ithaque.funnies.shared.funny.boardgame.EphemeralFunny;
import com.ithaque.funnies.shared.funny.boardgame.GameBoardRing;
import com.ithaque.funnies.shared.funny.boardgame.MessageFunny;
import com.ithaque.funnies.shared.funny.boardgame.TileFunny;
import com.ithaque.funnies.shared.funny.manager.AbstractCircusManager;
import com.ithaque.funnies.shared.funny.notifications.AcceptDropTargetQuestion;
import com.ithaque.funnies.shared.funny.notifications.AcceptRotatableQuestion;
import com.ithaque.funnies.shared.funny.notifications.AcceptRotationQuestion;
import com.ithaque.funnies.shared.funny.notifications.ActivateEvent;
import com.ithaque.funnies.shared.funny.notifications.DropEvent;
import com.ithaque.funnies.shared.funny.standard.CommandFunny;
import com.ithaque.funnies.shared.funny.standard.PanelFunny;

public class FunnyManager extends AbstractCircusManager {

	TileFunny lastTarget = null;
	Circus circus;
	DiceFunny dice;
	EphemeralFunny boom;
	MessageFunny message;
	TileFunny tile;
	CounterFunny counter1;
	CounterFunny counter2;
	
	AbstractCircusManager.Handler<DropEvent> dropHandler = 
		new Handler<DropEvent>(DropEvent.class, this) {
		@Override
		public Sketch process(DropEvent dropRequest) {
			SimpleSketch sketch = new SimpleSketch();
			if (lastTarget!=null) {
				sketch.addAnimation(counter2.move().turnTo(lastTarget).goTo(lastTarget).getAnimation());
			}
			lastTarget = (TileFunny)dropRequest.getTarget();
			CounterFunny counter = (CounterFunny)dropRequest.getDropped();
			StatusItem item = (StatusItem)counter.getDecoration(1);
			item.changeStatus(99);
			return sketch;
		}
	};

	int tryCount = 0;
	
	AbstractCircusManager.Handler<ActivateEvent> activateHandler = 
		new Handler<ActivateEvent>(ActivateEvent.class, this) {
		@Override
		public Sketch process(ActivateEvent activateRequest) {
			SimpleSketch sketch = new SimpleSketch();
			System.out.println("Activate : "+activateRequest.getActivated().getId());
			sketch.addAnimation(dice.rollFor(1));
			sketch.inParallel();
			sketch.addAnimation(boom.play(0.0f, 0.0f, 1000L));
			sketch.addAnimation(message.play(counter1, 200, 3000L));
			sketch.close();
			sketch.addAnimation(dice.rollFor(2));
			return sketch;
		}
	};
		
	AbstractCircusManager.Handler<AcceptDropTargetQuestion> acceptDropTargetHandler = 
		new Handler<AcceptDropTargetQuestion>(AcceptDropTargetQuestion.class, this) {
		@Override
		public Sketch process(AcceptDropTargetQuestion adtQuestion) {
			 if (lastTarget==null || lastTarget.getId().equals("t[0,0]")!=adtQuestion.getTarget().getId().equals("t[0,0]")) {
				 adtQuestion.accept();
			 }
			 return null;
		}
	};

	AbstractCircusManager.Handler<AcceptRotatableQuestion> acceptRotatableHandler = 
		new Handler<AcceptRotatableQuestion>(AcceptRotatableQuestion.class, this) {
		@Override
		public Sketch process(AcceptRotatableQuestion arQuestion) {
			 arQuestion.accept();
			 return null;
		}
	};

	AbstractCircusManager.Handler<AcceptRotationQuestion> acceptRotationHandler = 
		new Handler<AcceptRotationQuestion>(AcceptRotationQuestion.class, this) {
		@Override
		public Sketch process(AcceptRotationQuestion arQuestion) {
			 arQuestion.accept();
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
		if ((x+y)%7==0) {
			ImageItem tree = new ImageItem("tree.png");
			tree.setScale(0.05f);
			tile.putDecoration(1, tree, new Location(0.0f, 0.0f));
		}
		return tile;
	}
	
	CounterFunny createCounter(int id, String url1, String url2, float x, float y) {
		CounterFunny counter = new CounterFunny("c"+id, url1+".png", url2+".png");
		counter.setLocation(new Location(x, y));
		counter.setAllowedAngles(new float[] {
				0.0f,
				(float)(Math.PI/3.0f),
				(float)(Math.PI/3.0f*2.0f),
				(float)(Math.PI),
				(float)(Math.PI/3.0f*4.0f),
				(float)(Math.PI/3.0f*5.0f)
			});
		StatusItem status1 = new StatusItem("status.png", 50, 10, 10, 380, 380);
		status1.setScale(0.5f);
		StatusItem status2 = new StatusItem("status.png", 90, 10, 10, 380, 380);
		status2.setScale(0.5f);
		StatusItem status3 = new StatusItem("status.png", 10, 10, 10, 380, 380);
		status3.setScale(0.5f);
		counter.putDecoration(1, status1, new Location(-20, -20));
		counter.putDecoration(2, status2, new Location(0, -20));
		counter.putDecoration(3, status3, new Location(20, -20));
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
	
	MessageFunny createMessage() {
		MessageFunny message = new MessageFunny("message", "Hello\nWorld", new Color(255,0,0), new Font("arial", 14, 4));
		return message;
	}

	public void init() {
		for (int col=-6; col<=6; col++) {
			int inc = (col%2)==0?1:0;
			for (int row=-9+inc; row<=9; row+=2) {
				circus.enterRing(tile = createTile("["+col+","+row+"]", col*3, row));
			}
		}

		circus.enterRing(counter1 = createCounter(1, "u1", "u2", -400, -50));
		circus.enterRing(createCounter(2, "u1", "u2", -400, 0));
		circus.enterRing(createCounter(3, "u1", "u2", -400, 50));
		circus.enterRing(createCounter(4, "u2", "u1", 400, -50));
		circus.enterRing(createCounter(5, "u2", "u1", 400, 0));
		circus.enterRing(counter2 = createCounter(6, "u2", "u1", 400, 50));
//		counter2.addSpy(new FunnySpy());
		
		circus.enterRing(dice=createDiceFunny());
		circus.enterRing(boom=createExplosion());
		
		circus.enterRing(createArrow());
		circus.enterRing(message=createMessage());
		
		PanelFunny panel = new PanelFunny("sel", 500.0f, 400.0f, new Color(200, 200, 200), new Color(100, 100, 100), 2.0f);
		Icon icon = new Icon.SingleImageIcon(GameBoardRing.TOOLBAR, 80, 80, "load.png"/*, "rload.png"*/);
		panel.setToolIcon(icon);
		circus.enterRing(panel);

		final CommandFunny command = new CommandFunny("cmd") {
			@Override
			public void activateTool(Icon icon) {
				System.out.println("Command activated :)");	
			}
		};
		icon = new Icon.DoubleImageIcon(GameBoardRing.TOOLBAR, 80, 80, "again.png", "ragain.png");
		command.setToolIcon(icon);
		circus.enterRing(command);

		CommandFunny save = new CommandFunny("sav") {
			@Override
			public void activateTool(Icon icon) {
				command.setEnabled(!command.isEnabled());	
			}
		};
		
		icon = new Icon.DoubleImageIcon(GameBoardRing.TOOLBAR, 80, 80, "save.png", "rsave.png");
		save.setToolIcon(icon);
		circus.enterRing(save);

		final CommandFunny loading = new CommandFunny("lod") {
			@Override
			public void activateTool(Icon icon) {
				System.out.println("Loading activated :)");	
			}
		};
		icon = new Icon.SpriteIcon(GameBoardRing.TOOLBAR, 80, 80, "loading.png", 4, 2, 320, 160);
		loading.setToolIcon(icon);
		circus.enterRing(loading);
	}


}
