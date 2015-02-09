package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.Location;
import com.ithaque.funnies.shared.basic.AnimationContext.LocationFinder;
import com.ithaque.funnies.shared.basic.AnimationContext.MoveableFinder;
import com.ithaque.funnies.shared.basic.ItemHolder;
import com.ithaque.funnies.shared.basic.TransformUtil;
import com.ithaque.funnies.shared.basic.items.animations.easing.LinearEasing;

public class DropAnimation extends SoftenAnimation {

	Location location;	
	Location destLocation;
	Location baseLocation;
	ItemHolder destinationHolder;
	LocationFinder locationFinder;
	MoveableFinder destinationHolderFinder;
	
	public DropAnimation(Easing easing) {
		super(easing);
	}
	
	public DropAnimation(Easing easing, ItemHolder destinationHolder, Location location) {
		super(easing);
		this.destinationHolder = destinationHolder;
		this.location = location;
	}

	public void setLocation(LocationFinder locationFinder) {
		this.locationFinder = locationFinder;
	}

	public void setDestinationHolder(MoveableFinder destinationHolderFinder) {
		this.destinationHolderFinder = destinationHolderFinder;
	}

	@Override
	public boolean executeAnimation(long time) {
		Location location = getItem().getLocation();
		if (location!=null) { 
			if (getLocation()!=null) {
				getItem().setLocation(
					getEasing().getValue(baseLocation.getX(), destLocation.getX()),
					getEasing().getValue(baseLocation.getY(), destLocation.getY()), 
					getUpdateSerial());
			}
		}
		return true;
	}

	@Override
	public void finish(long time) {
		if (getDestinationHolder() != null && getDestinationHolder()!=getItem().getParent()) {
			float rotation = getRotation();
			getItem().setRotation(rotation, getUpdateSerial());
			float scale = getScale();
			getItem().setScale(scale, getUpdateSerial());
			getItem().setParent(getDestinationHolder(), getUpdateSerial());
		}
		getItem().setLocation(getLocation(), getUpdateSerial());
		super.finish(time);
	}
	
	Float getRotation() {
		return TransformUtil.transformRotation(getItem().getParent(), getDestinationHolder(), getItem().getRotation());
	}
	
	Float getScale() {
		return TransformUtil.transformScale(getItem().getParent(), getDestinationHolder(), getItem().getScale());
	}
	
	@Override
	public boolean start(long time) {
		boolean result = super.start(time);
		if (result) {
			this.baseLocation = getItem().getLocation();
			if (getDestinationHolder()!=null && getDestinationHolder() != getItem().getParent()) {
				destLocation = TransformUtil.transformLocation(getDestinationHolder(), getItem().getParent(), getLocation());
			}
			else {
				destLocation = getLocation();
			}
		}
		return result;
	}
	
	@Override
	public void reset() {
		super.reset();
		this.baseLocation = null;
		this.destLocation = null;
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

	public static class Builder extends SoftenAnimation.Builder {
		Location location = null;
		LocationFinder locationFinder = null;
		ItemHolder destinationHolder = null;
		MoveableFinder destinationHolderFinder = null;

		public Builder(Easing.Factory easing) {
			super(easing);
		}

		public Builder(long duration) {
			this(new LinearEasing.Builder(duration));
		}
		
		@Override
		public DropAnimation create() {
			DropAnimation animation =  new DropAnimation(getEasing().create());
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

		public Builder setDestinationHolder(MoveableFinder destinationHolderFinder) {
			this.destinationHolderFinder = destinationHolderFinder;
			return this;
		}

		@Override
		protected void prepare(ItemAnimation animation) {
			DropAnimation dropAnimation = (DropAnimation)animation;
			super.prepare(animation);
			if (location!=null) {
				dropAnimation.setLocation(location);
			}
			else if (locationFinder!=null) {
				dropAnimation.setLocation(locationFinder);
			}
			if (destinationHolder!=null) {
				dropAnimation.setDestinationHolder(destinationHolder);
			}
			else if (destinationHolderFinder!=null) {
				dropAnimation.setDestinationHolder(destinationHolderFinder);
			}
		}

		public Builder setLocation(Location location) {
			this.location = location;
			return this;
		}	

		public Builder setDestinationHolder(ItemHolder destinationHolder) {
			this.destinationHolder = destinationHolder;
			return this;
		}	
	}
	
}
