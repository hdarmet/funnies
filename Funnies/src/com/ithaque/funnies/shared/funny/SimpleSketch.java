package com.ithaque.funnies.shared.funny;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.items.animations.CompositeAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ParallelAnimation;
import com.ithaque.funnies.shared.basic.items.animations.SequenceAnimation;

public class SimpleSketch implements Sketch {

	SequenceAnimation baseAnimation = new SequenceAnimation();
	List<CompositeAnimation> animations = new ArrayList<CompositeAnimation>();
	
	public SimpleSketch() {
		baseAnimation = new SequenceAnimation();
		animations = new ArrayList<CompositeAnimation>();
		animations.add(baseAnimation);
	}
	
	public void addAnimation(Animation animation) {
		animations.get(animations.size()-1).addAnimation(animation);
	}
	
	public void inParallel() {
		CompositeAnimation animation = new ParallelAnimation();
		animations.get(animations.size()-1).addAnimation(animation);
		animations.add(animation);
	}

	public void inSequence() {
		CompositeAnimation animation = new SequenceAnimation();
		animations.get(animations.size()-1).addAnimation(animation);
		animations.add(animation);
	}

	public void close() {
		if (animations.size()==1) {
			throw new IllegalInvokeException();
		}
		animations.remove(animations.get(animations.size()-1));
	}
	@Override
	public void play(Circus circus) {
		circus.getBoard().launchAnimation(baseAnimation);
	}

}
