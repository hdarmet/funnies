package com.ithaque.funnies.shared.basic.processors;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.Geometric;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.TransformUtil;

public class TargetedRotateProfile extends RotateProfile {

	private static final float DEFAULT_ANCHOR_DISTANCE = 10.0f;

	Animation.Factory finishRotationAnimation = null;

	List<Item> rotatables = new ArrayList<Item>();
	float distance = DEFAULT_ANCHOR_DISTANCE;
	
	public TargetedRotateProfile() {
	}
	
	public void setAnchorDistance(float distance) {
		this.distance = distance;
	}
	
	public void setFinishRotationAnimation(Animation.Factory finishRotationAnimation) {
		this.finishRotationAnimation = finishRotationAnimation;
	}
	
	@Override
	protected Animation.Factory getFinishRotateAnimation(Item rotatable) {
		return this.finishRotationAnimation;
	}
	
	public void addRotatable(Item rotatable) {
		rotatables.add(rotatable);
	}
	
	public void removeRotatable(Item rotatable) {
		rotatables.remove(rotatable);
	}
	
	@Override
	protected boolean acceptRotatable(Item rotatable, Location mouseLocation) {
		Location anchor = TransformUtil.invertTransformLocation(rotatable, mouseLocation);
		if (!rotatables.contains(rotatable)) {
			return false;
		}
		for (Location location : rotatable.getShape()) {
			if (Geometric.computeDistance(location, anchor)<distance) {
				return true;
			}
		}
		return false;
	}
	
}
