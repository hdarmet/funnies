package com.ithaque.funnies.shared.basic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.ithaque.funnies.shared.IllegalInvokeException;


public class MultiLayered extends GroupItem implements BaseDevice {

	float minX;
	float maxX;
	float minY;
	float maxY;

	public MultiLayered(float minX, float minY, float maxX, float maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	class MultiLayer extends Layer {
		
		MultiLayer(String id, float minX, float minY, float maxX, float maxY) {
			super(id, minX, minY, maxX, maxY);
		}
		
		@Override
		public void setScale(float scale, long serial) {
			for (int i=0; i<MultiLayered.this.getItemCount(); i++) {
				Item brother = MultiLayered.this.getItem(i);
				if (brother instanceof MultiLayer) {
					((MultiLayer)brother).doSetScale(scale, serial);
				}
			}
		}

		@Override
		public void setRotation(float angle, long serial) {
			for (int i=0; i<MultiLayered.this.getItemCount(); i++) {
				Item brother = MultiLayered.this.getItem(i);
				if (brother instanceof MultiLayer) {
					((MultiLayer)brother).doSetRotation(angle, serial);
				}
			}
		}

		@Override
		public void setLocation(Location location, long serial) {
			for (int i=0; i<MultiLayered.this.getItemCount(); i++) {
				Item brother = MultiLayered.this.getItem(i);
				if (brother instanceof MultiLayer) {
					((MultiLayer)brother).doSetLocation(location, serial);
				}
			}
		}
		
		@Override
		public void doSetParent(ItemHolder itemHolder) {
			if (!(itemHolder instanceof BaseDevice)) {
				throw new IllegalInvokeException();
			}
			super.doSetParent(itemHolder);
		}
		
		void doSetScale(float scale, long serial) {
			super.setScale(scale, serial);
		}

		void doSetRotation(float angle, long serial) {
			super.setRotation(angle, serial);
		}
		
		void doSetLocation(Location location, long serial) {
			super.setLocation(location, serial);
		}
	};
	
	@Override
	public Item getMouseTarget(MouseEvent event) {
		for (int index=getItemCount()-1; index>=0; index--) {
			Item child = getItem(index);
			if (child instanceof Device) {
				Item target = ((Device)child).getMouseTarget(event);
				if (target!=null) {
					return target;
				}
			}
		}
		return null;
	}

	@Override
	public Collection<Item> getMouseTargets(MouseEvent event) {
		Set<Item> targets = new HashSet<Item>();
		for (int index=getItemCount()-1; index>=0; index--) {
			Item child = getItem(index);
			if (child instanceof Device) {
				Collection<Item> childTargets = ((Device)child).getMouseTargets(event);
				targets.addAll(childTargets);
			}
		}
		return targets;
	}
	
	public Layer createAttachedLayer(String id) {
		Layer layer = new MultiLayer(id, minX, minY, maxX, maxY);
		addItem(layer);
		return layer;
	}
	
	public void addDevice(Device device) {
		addItem((Item)device);
	}

}
