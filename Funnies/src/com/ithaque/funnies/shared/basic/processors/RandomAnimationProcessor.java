package com.ithaque.funnies.shared.basic.processors;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.basic.AlarmEvent;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.AnimationContext;
import com.ithaque.funnies.shared.basic.AnimationContext.MoveableFinder;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Moveable;
import com.ithaque.funnies.shared.basic.Processor;

public abstract class RandomAnimationProcessor implements Processor {

	Board board;
	int segCount;
	int segIndex=0;
	String alarmId;
	
	List<Item> animated = new ArrayList<Item>();
	
	public RandomAnimationProcessor(Board board, long timeout, String alarmId, int segCount) {
		this.board = board;
		this.segCount = segCount;
		this.alarmId = alarmId;
		board.addAlarm(timeout, true, alarmId);
	}
	
	public Board getBoard() {
		return board;
	}
	
	@Override
	public boolean process(Event event, Board board) {
		if (event instanceof AlarmEvent && ((AlarmEvent)event).getAlarm().equals(alarmId)) {
			for (int index=segIndex; index<animated.size(); index+=segCount) {
				animate(animated.get(index));
			}
			segIndex = (segIndex+1)%segCount;
			return true;
		}
		else {
			return false;
		}
	}

	protected void animate(Item item) {
		Animation.Factory animationFactory = getAnimation(item);
		if (animationFactory!=null) {
			Animation animation = animationFactory.create();
			animation.setContext(new RandomAnimationContext(item));
			getBoard().launchAnimation(animation);
		}
	}
	
	protected abstract Animation.Factory getAnimation(Item item);
	
	public void addAnimated(Item item) {
		animated.add(item);
	}
	
	public void removeAnimated(Item item) {
		animated.remove(item);
	}

	public static class RandomAnimationContext implements AnimationContext {

		Item animatedItem = null;

		public RandomAnimationContext(Item animatedItem) {
			this.animatedItem = animatedItem;
		}
		
	}
	
	public static MoveableFinder animatedItem() {
		return new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return ((RandomAnimationContext)context).animatedItem;
			}			
		};
	}

}
