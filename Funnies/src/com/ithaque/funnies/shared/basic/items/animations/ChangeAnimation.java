package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.Geometric;
import com.ithaque.funnies.shared.basic.AnimationContext.FactorFinder;
import com.ithaque.funnies.shared.basic.AnimationContext.LocationFinder;
import com.ithaque.funnies.shared.basic.AnimationContext.MoveableFinder;
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
	
	MoveableFinder destinationHolderFinder;
	LocationFinder locationFinder;
	FactorFinder rotationFinder;
	FactorFinder scaleFinder;
	
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

	public void setLocation(LocationFinder locationFinder) {
		this.locationFinder = locationFinder;
	}

	public void setRotation(FactorFinder rotationFinder) {
		this.rotationFinder = rotationFinder;
	}

	public void setScale(FactorFinder scaleFinder) {
		this.scaleFinder = scaleFinder;
	}

	public void setDestinationHolder(MoveableFinder destinationHolderFinder) {
		this.destinationHolderFinder = destinationHolderFinder;
	}

	@Override
	public boolean executeAnimation(long time) {
		//computeTargetMetrics();
		if (getLocation()!=null) {
			getItem().setLocation(
				getEasing().getValue(baseLocation.getX(), targetLocation.getX()),
				getEasing().getValue(baseLocation.getY(), targetLocation.getY()));
		}
		if (getRotation()!=null) {
			System.out.println("rotate : "+(getEasing().getValue(baseRotation, targetRotation)));
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
			this.baseRotation = getItem().getRotation();
			this.baseScale = getItem().getScale();
			computeTargetMetrics();
		}
		return result;
	}

	void computeTargetMetrics() {
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
		targetRotation = Geometric.optimizeRotation(baseRotation, targetRotation);
	}

	public Location getLocation() {
		return location==null ? locationFinder.find(getContext()) : location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public ItemHolder getDestinationHolder() {
		if (destinationHolder==null) {
			if (destinationHolderFinder!=null) {
				return (ItemHolder)destinationHolderFinder.find(getContext());
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
		return rotation==null ? rotationFinder.find(getContext()) : rotation;
	}

	public void setRotation(Float rotation) {
		this.rotation = rotation;
	}
	
	public Float getScale() {
		return scale==null ? scaleFinder.find(getContext()) : scale;
	}

	public void setScale(Float scale) {
		this.scale = scale;
	}
	
	public static class Builder extends SoftenAnimation.Builder {
		Location location;
		LocationFinder locationFinder;
		Float rotation;
		FactorFinder rotationFinder;
		Float scale;
		FactorFinder scaleFinder;
		ItemHolder destinationHolder;
		MoveableFinder destinationHolderFinder;

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
		public Builder setItem(MoveableFinder itemFinder) {
			super.setItem(itemFinder);
			return this;
		}
		
		public Builder setLocation(LocationFinder locationFinder) {
			this.locationFinder = locationFinder;
			return this;
		}
		
		public Builder setRotation(FactorFinder rotationFinder) {
			this.rotationFinder = rotationFinder;
			return this;
		}

		public Builder setScale(FactorFinder scaleFinder) {
			this.scaleFinder = scaleFinder;
			return this;
		}

		public Builder setDestinationHolder(MoveableFinder destinationHolderFinder) {
			this.destinationHolderFinder = destinationHolderFinder;
			return this;
		}

		@Override
		protected void prepare(SoftenAnimation animation) {
			super.prepare(animation);
			ChangeAnimation changeAnim = (ChangeAnimation)animation;
			if (location!=null) {
				changeAnim.setLocation(location);
			}
			else if (locationFinder!=null) {
				changeAnim.setLocation(locationFinder);
			}

			if (rotation!=null) {
				changeAnim.setRotation(rotation);
			}
			else if (rotationFinder!=null) {
				changeAnim.setRotation(rotationFinder);
			}
			
			if (scale!=null) {
				changeAnim.setScale(scale);
			}
			else if (scaleFinder!=null) {
				changeAnim.setScale(scaleFinder);
			}

			if (destinationHolder!=null) {
				changeAnim.setDestinationHolder(destinationHolder);
			}
			else if (destinationHolderFinder!=null) {
				changeAnim.setDestinationHolder(destinationHolderFinder);
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
