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
		public void setScale(float scale) {
			for (int i=0; i<MultiLayered.this.getItemCount(); i++) {
				MultiLayer layer = (MultiLayer)MultiLayered.this.getItem(i);
				layer.doSetScale(scale);
			}
		}

		@Override
		public void setRotation(float angle) {
			for (int i=0; i<MultiLayered.this.getItemCount(); i++) {
				MultiLayer layer = (MultiLayer)MultiLayered.this.getItem(i);
				layer.doSetRotation(angle);
			}
		}

		@Override
		public void setLocation(Location location) {
			for (int i=0; i<MultiLayered.this.getItemCount(); i++) {
				MultiLayer layer = (MultiLayer)MultiLayered.this.getItem(i);
				layer.doSetLocation(location);
			}
		}
		
		@Override
		public void setParent(ItemHolder itemHolder) {
			if (!(itemHolder instanceof BaseDevice)) {
				throw new IllegalInvokeException();
			}
			super.setParent(itemHolder);
		}
		
		void doSetScale(float scale) {
			super.setScale(scale);
		}

		void doSetRotation(float angle) {
			super.setRotation(angle);
		}
		
		void doSetLocation(Location location) {
			super.setLocation(location);
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
