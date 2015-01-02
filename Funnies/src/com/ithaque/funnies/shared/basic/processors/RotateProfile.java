package com.ithaque.funnies.shared.basic.processors;

import com.ithaque.funnies.shared.Geometric;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.AnimationContext;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.processors.DragProcessor.DragProfile;

public class RotateProfile implements DragProfile {
	public static final AnimationContext.Key ROTATABLE_ITEM_KEY = new AnimationContext.Key("ROTATABLE_ITEM_KEY");
	public static final AnimationContext.Key ROTATION_KEY = new AnimationContext.Key("ROTATION_KEY");

	Item rotatable = null;
	float rotatableAngle = 0.0f;
	float anchorAngle = 0.0f;
	
	public RotateProfile() {
	}
	
	@Override
	public boolean beginDrag(MouseEvent event, Board board) {
		Item rotatable = board.getMouseTarget(event);
		Location anchor = DragProcessor.getAnchor(event, rotatable);
		if (rotatable!=null && acceptRotatable(rotatable, new Location(event.getX(), event.getY()))) {
			this.rotatable = rotatable;
			this.rotatableAngle = this.rotatable.getRotation();
			this.anchorAngle = Geometric.computeAngle(Location.ORIGIN, anchor);
		}
		return this.rotatable!=null;
	}
	
	protected boolean acceptRotatable(Item rotatable, Location mouseLocation) {
		return true;
	}

	@Override
	public void drag(MouseEvent event, Board board) {
		Location anchor = DragProcessor.getAnchor(event, rotatable);
		float angle = Geometric.computeAngle(Location.ORIGIN, anchor);
		rotatable.setRotation(rotatableAngle+angle-anchorAngle);
	}

	@Override
	public void drop(MouseEvent event, Board board) {
		Location anchor = DragProcessor.getAnchor(event, rotatable);
		Animation animation = null;
		float angle = rotatableAngle+Geometric.computeAngle(Location.ORIGIN, anchor)-anchorAngle;
		Float adjustedAngle = resolveRotation(rotatable, angle);
		Float finalAngle = null;
		if (adjustedAngle==null || !acceptRotation(rotatable, adjustedAngle)) {
			finalAngle = this.rotatableAngle;
		}
		else {
			if (adjustedAngle != angle) {
				finalAngle = adjustedAngle;
			}
		}
		if (getFinishRotateAnimation(rotatable)!=null) {
			animation = getFinishRotateAnimation(rotatable).create();
			animation.setContext(retrieveAnimationContext(rotatable, finalAngle));
			board.launchAnimation(animation);
		}
		else {
			rotatable.setRotation(finalAngle);
		}
		rotatable = null;
	}

	protected boolean acceptRotation(Item rotatable, float angle) {
		return true;
	}

	protected Float resolveRotation(Item rotatable, float angle) {
		return angle;
	}
	
	protected Animation.Factory getFinishRotateAnimation(Item rotatable) {
		return null;
	}

	public AnimationContext retrieveAnimationContext(Item rotatable, float finalAngle) {
		return new DragAnimationContext(rotatable, finalAngle);
	}
	
	public static class DragAnimationContext implements AnimationContext {
		Item rotatable;
		float finalAngle;
	
		public DragAnimationContext(Item rotatable, float finalAngle) {
			this.rotatable = rotatable;
			this.finalAngle = finalAngle;
		}
		
		public Location getLocation(Key locationKey) {
			return null;
		}
		
		public Item getItem(Key itemKey) {
			if (itemKey==ROTATABLE_ITEM_KEY) {
				return rotatable;
			}
			return null;		}
		
		public Float getFactor(Key itemKey) {
			if (itemKey==ROTATION_KEY) {
				return finalAngle;
			}
			return null;
		}
	}

}

