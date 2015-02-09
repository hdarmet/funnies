package com.ithaque.funnies.shared;

public class Shape {
	public static final Shape EMPTY_SHAPE = new Shape(new Location[0]);
	
	Location[] locations;
	
	public Shape(Location ... locations) {
		this.locations = locations;
	}
	
	public Shape(float width, float height) {
		this(
			new Location(-width/2.0f, -height/2.0f), 
			new Location(width/2.0f, -height/2.0f), 
			new Location(width/2.0f, height/2.0f), 
			new Location(-width/2.0f, height/2.0f));
	}
	
	public Location getLocation(int index) {
		return locations[index];
	}

	public float getX(int index) {
		return locations[index].getX();
	}

	public float getY(int index) {
		return locations[index].getY();
	}

	public int size() {
		return locations.length;
	}

	public Location[] getLocations() {
		return locations;
	}
	
	public String toString() {
		StringBuilder result=new StringBuilder();
		for (Location location : locations) {
			if (result.length()>0) {
				result.append(",");
			}
			result.append(location.toString());
		}
		return result.toString();
	}
}
