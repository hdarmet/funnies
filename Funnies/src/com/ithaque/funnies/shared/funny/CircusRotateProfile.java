package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.Location;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.processors.RotateProfile;
import com.ithaque.funnies.shared.funny.notifications.AcceptRotatableQuestion;
import com.ithaque.funnies.shared.funny.notifications.AcceptRotationQuestion;

public class CircusRotateProfile extends RotateProfile {
	FunnyRegistry<RotatableFunny> rotatableFunnies = new FunnyRegistry<RotatableFunny>(
			new FunnyRegistry.ItemsFinder<RotatableFunny>() {
				@Override
				public Item[] getItems(RotatableFunny funny) {
					return funny.getRotatableItems();
				}
			}
		) {
		@Override
		protected Record<RotatableFunny> createRecord(Item item, RotatableFunny funny) {
			return new Record<RotatableFunny>(funny, item);
		}
	};

	AbstractRing ring;
	
	public CircusRotateProfile(AbstractRing ring) {
		super();
		this.ring = ring;
	}
	
	public void registerRotatableFunny(RotatableFunny funny) {
		rotatableFunnies.registerFunny(funny);
	}
	
	public void unregisterRotatableFunny(RotatableFunny funny) {
		rotatableFunnies.unregisterFunny(funny);
	}
	
	protected RotatableFunny getRotatableFunny(Item item) {
		return rotatableFunnies.getFunny(item);
	}

	@Override
	protected Animation.Factory getFinishRotateAnimation(Item rotatable) {
		RotatableFunny funny = getRotatableFunny(rotatable);
		return funny.getFinishRotateAnimation();
	}

	@Override
	protected boolean acceptRotatable(Item rotatable, Location mouseLocation) {
		if (!rotatableFunnies.containsItem(rotatable) || 
			!super.acceptRotatable(rotatable, mouseLocation)) 
		{
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
	protected Float resolveRotation(Item rotatable, float angle) {
		RotatableFunny rotatableFunny = getRotatableFunny(rotatable);
		return rotatableFunny.adjustRotation(angle);
	}
	
	@Override
	protected boolean acceptRotation(Item rotatable, float angle) {
		RotatableFunny rotatableFunny = getRotatableFunny(rotatable);
		AcceptRotationQuestion question = new AcceptRotationQuestion(rotatableFunny, angle);
		ring.notify(question);
		return question.isAccepted();
	}

}
