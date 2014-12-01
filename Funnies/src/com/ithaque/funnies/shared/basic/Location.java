package com.ithaque.funnies.shared.basic;

public class Location {
	float x, y;
	
	public static final Location ORIGIN = new Location(0, 0);
	
	public Location(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return "Location["+x+","+y+"]";
	}
}
