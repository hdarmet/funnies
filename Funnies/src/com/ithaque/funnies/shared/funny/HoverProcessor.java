package com.ithaque.funnies.shared.funny;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.AnimationContext;
import com.ithaque.funnies.shared.basic.AnimationContext.MoveableFinder;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.Moveable;
import com.ithaque.funnies.shared.basic.Processor;
import com.ithaque.funnies.shared.basic.items.animations.RepeatableAnimation;

public class HoverProcessor implements Processor {

	Ring ring;
	FunnyRegistry<HoverFunny> hoverables = new FunnyRegistry<HoverFunny>(
			new FunnyRegistry.ItemsFinder<HoverFunny>() {
				@Override
				public Item[] getItems(HoverFunny funny) {
					return funny.getHoverables();
				}
			}
		) {
		@Override
		protected Record<HoverFunny> createRecord(Item item, HoverFunny funny) {
			return new HoverRecord(funny, item);
		}		
	};
	Set<HoverRecord> actives = new HashSet<HoverRecord>();
	
	public HoverProcessor(Ring ring) {
		this.ring = ring;
	}
	
	class HoverRecord extends FunnyRegistry.Record<HoverFunny> implements AnimationContext {
		public HoverRecord(HoverFunny funny, Item item) {
			super(funny, item);
		}
		
		RepeatableAnimation hoverAnimation = null;
		boolean moveOn = false;
	}
	
	@Override
	public boolean process(Event event, Board board) {
		if (event.getType()==Type.MOUSE_MOVE) {
			Set<HoverRecord> actives = this.actives;
			this.actives = new HashSet<HoverRecord>();
			Collection<Item> hoveredItems = 
				board.getMouseTargets((MouseEvent)event);
			for (Item hoverItem : hoveredItems) {
				processHover(hoverItem);
			}
			for (HoverRecord hoverRecord : actives) {
				if (!this.actives.contains(hoverRecord)) {
					processExitHover(hoverRecord);
				}
			}
		}
		return false;
	}

	void processHover(Item hover) {
		HoverRecord hoverRecord = (HoverRecord)hoverables.getRecord(hover);
		if (hoverRecord!=null) {
			if (!hoverRecord.moveOn) {
				hoverRecord.moveOn = true;
				hoverRecord.funny.moveOn(hover);
			}
			if (hoverRecord.hoverAnimation==null) {
				Animation animation = createHoverAnimation(hoverRecord);
				if (animation!=null) {
					hoverRecord.hoverAnimation = new RepeatableAnimation(animation);
					ring.launch(hoverRecord.hoverAnimation);
					actives.add(hoverRecord);
				}
			}
			else if (hoverRecord.hoverAnimation.isFinished()) {
				hoverRecord.hoverAnimation.reset();
				if (hoverRecord.hoverAnimation!=null) {
					ring.launch(hoverRecord.hoverAnimation);
					actives.add(hoverRecord);
				}
			}
			else {
				actives.add(hoverRecord);
			}
		}
	}

	Animation createHoverAnimation(HoverRecord record) {
		Animation.Factory factory = record.funny.getHoverAnimation(record.item);
		if (factory!=null) {
			Animation animation = factory.create();
			animation.setContext(record);
			return animation;
		}
		return null;
	}
	
	void processExitHover(HoverRecord hoverRecord) {
		if (hoverRecord.moveOn) {
			hoverRecord.moveOn = false;
			hoverRecord.funny.moveOut(hoverRecord.item);
		}
		if (hoverRecord.hoverAnimation!=null && !hoverRecord.hoverAnimation.isFinished()) {
			hoverRecord.hoverAnimation.stop();
		}
	}
	
	public void registerHoverableFunny(HoverFunny funny) {
		hoverables.registerFunny(funny);
	}

	public void unregisterHoverableFunny(HoverFunny funny) {
		hoverables.unregisterFunny(funny);
	}

	public static MoveableFinder item() {
		return new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return ((HoverRecord)context).item;
			}		
		};
	}
}
