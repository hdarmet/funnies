package com.ithaque.funnies.shared.basic.items.animations;

import com.ithaque.funnies.shared.basic.AnimationContext.LocationFinder;
import com.ithaque.funnies.shared.basic.AnimationContext.MoveableFinder;
import com.ithaque.funnies.shared.basic.ItemHolder;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.TransformUtil;
import com.ithaque.funnies.shared.basic.items.animations.easing.LinearEasing;

public class DragAnimation extends SoftenAnimation {

	Location location;
	Location destLocation;
	Location baseLocation;
	ItemHolder dragHolder;
	LocationFinder locationFinder;
	MoveableFinder dragHolderFinder;
	
	public DragAnimation(Easing easing) {
		super(easing);
	}
	
	public DragAnimation(Easing easing, ItemHolder destinationHolder, Location location) {
		super(easing);
		this.dragHolder = destinationHolder;
		this.location = location;
	}

	public void setLocation(LocationFinder locationFinder) {
		this.locationFinder = locationFinder;
	}

	public void setDragHolder(MoveableFinder dragHolderFinder) {
		this.dragHolderFinder = dragHolderFinder;
	}

	@Override
	public boolean start(long time) {
		boolean result = super.start(time);
		if (result) {
			if (getDragHolder() != null && getDragHolder()!=getItem().getParent()) {
				float rotation = TransformUtil.transformRotation(getItem().getParent(), getDragHolder(), getItem().getRotation());
				getItem().setRotation(rotation, getUpdateSerial());
				float scale = TransformUtil.transformScale(getItem().getParent(), getDragHolder(), getItem().getScale());
				getItem().setScale(scale, getUpdateSerial());
				getItem().setParent(getDragHolder(), getUpdateSerial());
			}
			this.baseLocation = TransformUtil.transformLocation(getItem().getParent(), getDragHolder(), getItem().getLocation());
			getItem().setLocation(baseLocation, getUpdateSerial());		
			destLocation = getLocation();
		}
		return result;
	}
	
	@Override
	public void reset() {
		super.reset();
		this.baseLocation = null;
		this.destLocation = null;
	}
	
	@Override
	public boolean executeAnimation(long time) {
		Location location = getItem().getLocation();
		if (location!=null && destLocation!=null) {
			getItem().setLocation(
				getEasing().getValue(baseLocation.getX(), destLocation.getX()),
				getEasing().getValue(baseLocation.getY(), destLocation.getY()),
				getUpdateSerial());
		}
		return true;
	}

	@Override
	public void finish(long time) {
		getItem().setLocation(getLocation(), getUpdateSerial());
		super.finish(time);
	}
		
	public Location getLocation() {
		return location==null ? locationFinder.find(getContext()) : location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public ItemHolder getDragHolder() {
		if (dragHolder==null) {
			if (dragHolderFinder!=null) {
				return (ItemHolder)dragHolderFinder.find(getContext());
			}
			else {
				return null;
			}
		}
		else {
			return dragHolder;
		}
	}
	
	public void setDragHolder(ItemHolder dragHolder) {
		this.dragHolder = dragHolder;
	}

	public static class Builder extends SoftenAnimation.Builder {
		Location location;
		LocationFinder locationFinder;
		ItemHolder dragHolder;
		MoveableFinder dragHolderFinder;

		public Builder(Easing.Factory easing) {
			super(easing);
		}

		public Builder(long duration) {
			this(new LinearEasing.Builder(duration));
		}
		
		@Override
		public DragAnimation create() {
			DragAnimation animation =  new DragAnimation(getEasing().create());
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
		
		public Builder setDragHolder(MoveableFinder dragHolderFinder) {
			this.dragHolderFinder = dragHolderFinder;
			return this;
		}

		@Override
		protected void prepare(ItemAnimation animation) {
			super.prepare(animation);
			if (location!=null) {
				((DragAnimation)animation).setLocation(location);
			}
			else if (locationFinder!=null) {
				((DragAnimation)animation).setLocation(locationFinder);
			}
			if (dragHolder!=null) {
				((DragAnimation)animation).setDragHolder(dragHolder);
			}
			else if (dragHolderFinder!=null) {
				((DragAnimation)animation).setDragHolder(dragHolderFinder);
			}
		}

		public Builder setLocation(Location location) {
			this.location = location;
			return this;
		}	

		public Builder setDragHolder(ItemHolder dragHolder) {
			this.dragHolder = dragHolder;
			return this;
		}	
	}
	
}
