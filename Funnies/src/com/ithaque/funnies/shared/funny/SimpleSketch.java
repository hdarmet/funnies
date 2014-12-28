package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.items.animations.SequenceItemAnimation;

public class SimpleSketch implements Sketch {

	SequenceItemAnimation baseAnimation = new SequenceItemAnimation();
	
	public void addAnimation(Animation animation) {
		baseAnimation.addAnimation(animation);
	}
	
	@Override
	public void play(Circus circus) {
		circus.getBoard().launchAnimation(baseAnimation);
	}

}
