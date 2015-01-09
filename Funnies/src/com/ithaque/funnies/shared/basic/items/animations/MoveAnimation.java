package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.AnimationContext.Key;
import com.ithaque.funnies.shared.basic.ItemHolder;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.TransformUtil;
import com.ithaque.funnies.shared.basic.items.animations.easing.LinearEasing;

public class MoveAnimation extends SoftenAnimation {

	Location location;
	Location destLocation;
	Location baseLocation;
	ItemHolder destinationHolder;
	Key locationKey;
	Key destinationHolderKey;
	
	public MoveAnimation(Easing easing) {
		super(easing);
	}
	
	public MoveAnimation(Easing easing, ItemHolder destinationHolder, Location location) {
		super(easing);
		this.destinationHolder = destinationHolder;
		this.location = location;
	}

	public void setLocationKey(Key locationKey) {
		this.locationKey = locationKey;
	}

	public void setDestinationHolderKey(Key destinationHolderKey) {
		this.destinationHolderKey = destinationHolderKey;
	}

	@Override
	public boolean executeAnimation(long time) {
		Location location = getItem().getLocation();
		if (location!=null && getLocation()!=null) {
			getItem().setLocation(
				getEasing().getValue(baseLocation.getX(), destLocation.getX()),
				getEasing().getValue(baseLocation.getY(), destLocation.getY()));
		}
		return true;
	}

	@Override
	public void finish(long time) {
		if (getDestinationHolder() != null && getDestinationHolder()!=getItem().getParent()) {
			float rotation = TransformUtil.transformRotation(getItem().getParent(), getDestinationHolder(), getItem().getRotation());
			getItem().setRotation(rotation);
			float scale = TransformUtil.transformScale(getItem().getParent(), getDestinationHolder(), getItem().getScale());
			getItem().setScale(scale);
			getItem().changeParent(getDestinationHolder());
		}
		getItem().setLocation(getLocation());
		super.finish(time);
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

	public static class Builder extends SoftenAnimation.Builder {
		Location location;
		Key locationKey;
		ItemHolder destinationHolder;
		Key destinationHolderKey;

		public Builder(Easing.Factory easing) {
			super(easing);
		}

		public Builder(long duration) {
			this(new LinearEasing.Builder(duration));
		}
		
		@Override
		public MoveAnimation create() {
			MoveAnimation animation =  new MoveAnimation(getEasing().create());
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
		
		public Builder setDestinationHolderKey(Key destinationHolderKey) {
			this.destinationHolderKey = destinationHolderKey;
			return this;
		}

		@Override
		protected void prepare(SoftenAnimation animation) {
			super.prepare(animation);
			if (location!=null) {
				((MoveAnimation)animation).setLocation(location);
			}
			else if (locationKey!=null) {
				((MoveAnimation)animation).setLocationKey(locationKey);
			}
			if (destinationHolder!=null) {
				((MoveAnimation)animation).setDestinationHolder(destinationHolder);
			}
			else if (destinationHolderKey!=null) {
				((MoveAnimation)animation).setDestinationHolderKey(destinationHolderKey);
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
