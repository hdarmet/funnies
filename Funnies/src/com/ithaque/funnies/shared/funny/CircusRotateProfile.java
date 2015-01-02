package com.ithaque.funnies.shared.funny;

import java.util.HashMap;
import java.util.Map;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.processors.DiscreteRotateProfile;
import com.ithaque.funnies.shared.funny.notifications.AcceptRotatableQuestion;
import com.ithaque.funnies.shared.funny.notifications.AcceptRotationQuestion;

public class CircusRotateProfile extends DiscreteRotateProfile {
	Map<Item, RotatableFunny> rotatableFunnies = new HashMap<Item, RotatableFunny>();

	Ring ring;
	
	public CircusRotateProfile(Ring ring) {
		super();
		this.ring = ring;
	}
	
	public void registerRotatableFunny(RotatableFunny funny) {
		for (Item item : funny.getRotatableItems()) {
			rotatableFunnies.put(item, funny);
			addRotatable(item);
		}
	}
	
	public void unregisterRotatableFunny(RotatableFunny funny) {
		for (Item item : funny.getRotatableItems()) {
			rotatableFunnies.remove(item);
			removeRotatable(item);
		}
	}
	
	protected RotatableFunny getRotatableFunny(Item item) {
		return rotatableFunnies.get(item);
	}

	@Override
	protected Animation.Factory getFinishRotateAnimation(Item rotatable) {
		RotatableFunny funny = getRotatableFunny(rotatable);
		return funny.getFinishRotateAnimation();
	}

	@Override
	protected boolean acceptRotatable(Item rotatable, Location mouseLocation) {
		if (!super.acceptRotatable(rotatable, mouseLocation)) {
			return false;
		}
		else {
			RotatableFunny rotatableFunny = getRotatableFunny(rotatable);
			AcceptRotatableQuestion question = new AcceptRotatableQuestion(rotatableFunny);
			ring.notify(question);
			return question.isAccepted();
		}
	}

	@Override
	protected float[] getAllowedAngles(Item rotatable) {
		RotatableFunny rotatableFunny = getRotatableFunny(rotatable);
		return rotatableFunny.getAllowedAngles();
	}
	
	@Override
	protected boolean acceptRotation(Item rotatable, float angle) {
		RotatableFunny rotatableFunny = getRotatableFunny(rotatable);
		AcceptRotationQuestion question = new AcceptRotationQuestion(rotatableFunny, angle);
		ring.notify(question);
		return question.isAccepted();
	}

}
