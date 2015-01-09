package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.AnimationContext.Key;
import com.ithaque.funnies.shared.basic.ItemHolder;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.TransformUtil;
import com.ithaque.funnies.shared.basic.items.animations.easing.LinearEasing;

public class ChangeAnimation extends SoftenAnimation {

	ItemHolder destinationHolder;
	Location location;
	Float rotation;
	Float scale;

	Location baseLocation;
	Float baseRotation;
	Float baseScale;
	
	Location targetLocation;
	Float targetRotation;
	Float targetScale;
	
	Key destinationHolderKey;
	Key locationKey;
	Key rotationKey;
	Key scaleKey;
	
	public ChangeAnimation(Easing easing) {
		super(easing);
	}
	
	public ChangeAnimation(
		Easing easing, 
		ItemHolder destinationHolder, 
		Location location,
		Float rotation,
		Float scale) 
	{
		super(easing);
		this.destinationHolder = destinationHolder;
		this.location = location;
		this.rotation = rotation;
		this.scale = scale;
	}

	public void setLocationKey(Key locationKey) {
		this.locationKey = locationKey;
	}

	public void setRotationKey(Key rotationKey) {
		this.rotationKey = rotationKey;
	}

	public void setScaleKey(Key scaleKey) {
		this.scaleKey = scaleKey;
	}

	public void setDestinationHolderKey(Key destinationHolderKey) {
		this.destinationHolderKey = destinationHolderKey;
	}

	@Override
	public boolean executeAnimation(long time) {
		if (getLocation()!=null) {
			getItem().setLocation(
				getEasing().getValue(baseLocation.getX(), targetLocation.getX()),
				getEasing().getValue(baseLocation.getY(), targetLocation.getY()));
		}
		if (getRotation()!=null) {
			getItem().setRotation(getEasing().getValue(baseRotation, targetRotation));
		}
		if (getScale()!=null) {
			getItem().setScale(getEasing().getValue(baseScale, targetScale));
		}
		return true;
	}

	@Override
	public void finish(long time) {
		if (getDestinationHolder() != null && getDestinationHolder()!=getItem().getParent()) {
			getItem().changeParent(getDestinationHolder());
		}
		if (getLocation()!=null) {
			getItem().setLocation(getLocation());
		}
		if (getRotation()!=null) {
			getItem().setRotation(getRotation());
		}
		if (getScale()!=null) {
			getItem().setScale(getScale());
		}
		super.finish(time);
	}
	
	@Override
	public boolean start(long time) {
		boolean result = super.start(time);
		if (result) {
			this.baseLocation = getItem().getLocation();
			if (getDestinationHolder()!=null && getDestinationHolder() != getItem().getParent()) {
				targetLocation = TransformUtil.transformLocation(getDestinationHolder(), getItem().getParent(), getLocation());
				targetRotation = TransformUtil.transformRotation(getDestinationHolder(), getItem().getParent(), getRotation());
				targetScale = TransformUtil.transformScale(getDestinationHolder(), getItem().getParent(), getScale());
			}
			else {
				targetLocation = getLocation();
				targetRotation = getRotation();
				targetScale = getScale();
			}
		}
		return result;
	}
	
	public Location getLocation() {
		return location==null ? getContext().getLocation(locationKey) : location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public ItemHolder getDestinationHolder() {
		if (destinationHolder==null) {
			if (destinationHolderKey!=null) {
				return (ItemHolder)getContext().getItem(destinationHolderKey);
			}
			else {
				return null;
			}
		}
		else {
			return destinationHolder;
		}
	}
	
	public void setDestinationHolder(ItemHolder destinationHolder) {
		this.destinationHolder = destinationHolder;
	}

	public Float getRotation() {
		return rotation==null ? getContext().getFactor(rotationKey) : rotation;
	}

	public void setRotation(Float rotation) {
		this.rotation = rotation;
	}
	
	public Float getScale() {
		return scale==null ? getContext().getFactor(scaleKey) : scale;
	}

	public void setScale(Float scale) {
		this.scale = scale;
	}
	
	public static class Builder extends SoftenAnimation.Builder {
		Location location;
		Key locationKey;
		Float rotation;
		Key rotationKey;
		Float scale;
		Key scaleKey;
		ItemHolder destinationHolder;
		Key destinationHolderKey;

		public Builder(Easing.Factory easing) {
			super(easing);
		}

		public Builder(long duration) {
			this(new LinearEasing.Builder(duration));
		}
		
		@Override
		public ChangeAnimation create() {
			ChangeAnimation animation =  new ChangeAnimation(getEasing().create());
			prepare(animation); 
			return animation;
		}

		@Override
		public Builder setItemKey(Key itemKey) {
			super.setItemKey(itemKey);
			return this;
		}
		
		public Builder setLocationKey(Key locationKey) {
			this.locationKey = locationKey;
			return this;
		}
		
		public Builder setRotationKey(Key rotationKey) {
			this.rotationKey = rotationKey;
			return this;
		}

		public Builder setScaleKey(Key scaleKey) {
			this.scaleKey = scaleKey;
			return this;
		}

		public Builder setDestinationHolderKey(Key destinationHolderKey) {
			this.destinationHolderKey = destinationHolderKey;
			return this;
		}

		@Override
		protected void prepare(SoftenAnimation animation) {
			super.prepare(animation);
			ChangeAnimation changeAnim = (ChangeAnimation)animation;
			if (location!=null) {
				changeAnim.setLocation(location);
			}
			else if (locationKey!=null) {
				changeAnim.setLocationKey(locationKey);
			}

			if (rotation!=null) {
				changeAnim.setRotation(rotation);
			}
			else if (rotationKey!=null) {
				changeAnim.setRotationKey(rotationKey);
			}
			
			if (scale!=null) {
				changeAnim.setScale(scale);
			}
			else if (scaleKey!=null) {
				changeAnim.setScaleKey(scaleKey);
			}

			if (destinationHolder!=null) {
				changeAnim.setDestinationHolder(destinationHolder);
			}
			else if (destinationHolderKey!=null) {
				changeAnim.setDestinationHolderKey(destinationHolderKey);
			}
		}

		public Builder setLocation(Location location) {
			this.location = location;
			return this;
		}	

		public Builder setRotation(Float rotation) {
			this.rotation = rotation;
			return this;
		}
		
		public Builder setScale(Float scale) {
			this.scale = scale;
			return this;
		}
		
		public Builder setDestinationHolder(ItemHolder destinationHolder) {
			this.destinationHolder = destinationHolder;
			return this;
		}	
	}
	
}
