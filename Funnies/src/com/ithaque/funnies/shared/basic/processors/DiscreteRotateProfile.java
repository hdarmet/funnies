package com.ithaque.funnies.shared.basic.processors;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.Geometric;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.TransformUtil;

public class DiscreteRotateProfile extends RotateProfile {

	private static final float DEFAULT_ANCHOR_DISTANCE = 10.0f;
	private static final float BIG_FLOAT = 10000f;

	Animation.Factory finishRotationAnimation = null;

	List<Item> rotatables = new ArrayList<Item>();
	float distance = DEFAULT_ANCHOR_DISTANCE;
	
	public DiscreteRotateProfile() {
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

	@Override
	protected Float resolveRotation(Item rotatable, float angle) {
		angle = Geometric.adjustAngle(angle);
		Float bestAngle = null;
		float diff = BIG_FLOAT;
		for (Float aAngle : getAllowedAngles(rotatable)) {
			float aDiff = aAngle-angle;
			if (aDiff<0) {
				aDiff=-aDiff;
			}
			if (aDiff>Math.PI) {
				aDiff=(float)(2.0f*Math.PI-aDiff);
				aAngle = (float)(2.0f*Math.PI)-aAngle;
			}
			if (aDiff<diff) {
				bestAngle = aAngle;
				diff = aDiff;
			}
		}
		return bestAngle;
	}

	protected float[] getAllowedAngles(Item rotatable) {
		return new float[] {
			(float)(Math.PI/3.0f),
			(float)(Math.PI/3.0f*2.0f),
			(float)(Math.PI),
			(float)(Math.PI/3.0f*4.0f),
			(float)(Math.PI/3.0f*5.0f),
			(float)(Math.PI/3.0f)
		};
	}
	
}
