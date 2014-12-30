package com.ithaque.funnies.shared.basic;

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
	
	public Layer addLayer(String id) {
		Layer layer = new MultiLayer(id, minX, minY, maxX, maxY);
		addItem(layer);
		return layer;
	}
}
