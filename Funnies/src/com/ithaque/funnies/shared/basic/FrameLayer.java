package com.ithaque.funnies.shared.basic;

import com.ithaque.funnies.shared.Location;



public class FrameLayer extends AbstractLayer {
	
	public FrameLayer(String id, float minX, float minY, float maxX, float maxY) {
		super(id, minX, minY, maxX, maxY);
	}
	
	public String toString() {
		return "frame layer : \""+getId()+"\"";
	}
	
	@Override
	public void setLocation(Location location, long serial) {
	}
	
	@Override
	public Token getLayerToken() {
		return token;
	}

	@Override
	public void setRotation(float rotation, long serial) {	
	}
	
	@Override
	public void setScale(float scale, long serial) {	
	}
		
}
