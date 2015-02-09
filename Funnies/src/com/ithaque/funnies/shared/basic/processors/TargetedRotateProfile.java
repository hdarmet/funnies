package com.ithaque.funnies.shared.basic.processors;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.Location;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Item;

public class TargetedRotateProfile extends RotateProfile {

	Animation.Factory finishRotationAnimation = null;

	List<Item> rotatables = new ArrayList<Item>();
	
	public TargetedRotateProfile() {
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
		if (!rotatables.contains(rotatable)) {
			return false;
		}
		return super.acceptRotatable(rotatable, mouseLocation);
	}
	
}
