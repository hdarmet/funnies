package com.ithaque.funnies.shared;

import com.ithaque.funnies.shared.basic.Location;

public class Trace {
	public static boolean debug = false;
	
	public static void debug(String message) {
		System.out.print(message);
	}
	
	public static String shapeToString(Location[] shape) {
		StringBuilder shapeString = new StringBuilder("{");
		for (int index=0; index<shape.length-1; index++) {
			shapeString.append(shape[index]);
		}
		shapeString.append(shape[shape.length-1]);
		return shapeString.toString();
	}
}
