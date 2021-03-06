package com.ithaque.funnies.shared;


public class Geometric {
	public static final float TWO_PI = (float)(Math.PI*2);
	
	static float ccw(Location a, Location b, Location c)
	{ 
		return (b.getX()-a.getX())*(c.getY()-a.getY()) - (b.getY()-a.getY())*(c.getX()-a.getX()); 
	}
	
	public static boolean intersect(Location l1p1, Location l1p2, Location l2p1, Location l2p2) {
		return (((ccw(l1p1, l1p2, l2p1) * ccw(l1p1, l1p2, l2p2)) <= 0)
			&& ((ccw(l2p1, l2p2, l1p1) * ccw(l2p1, l2p2, l1p2)) <= 0));
	}
		
	public static boolean inside(Location t, Shape shape) {
		int i, count = 0;
		Location[] poly = new Location[shape.size()+2];
		for (i=0; i<shape.size(); i++) {
			poly[i] = shape.getLocation(i);
		}
		poly[shape.size()] = poly[0];
		
		Location farAway = new Location(Integer.MAX_VALUE, t.getY());
		for (i = 0; i < shape.size(); i++) {
			if (!intersect(poly[i], poly[i], t, farAway) && !intersect(poly[i+1], poly[i+1], t, farAway)) {
				if (intersect(poly[i+1], poly[i], t, farAway)) {
					count++;
				}
			}
		}
		if (Trace.debug) {
			Trace.debug("Inside ?\n");
			Trace.debug("Point : "+t+"\n");
			Trace.debug("Shape : ");
			for (Location l : shape.getLocations()) {
				Trace.debug(l+", ");
			} 
			Trace.debug("\n");
			Trace.debug("Count : "+count+"\n");
			Trace.debug("Inside : "+((count & 1)!=0)+"\n");
		}
		return (count & 1)!=0;
	}

	public static Location[] getArea(Shape shape) {
		float minX = shape.getX(0);
		float maxX = shape.getX(0);
		float minY = shape.getY(0);
		float maxY = shape.getY(0);
		for (int i=1; i<shape.size(); i++) {
			if (minX>shape.getX(i)) {
				minX=shape.getX(i);
			}
			if (maxX<shape.getX(i)) {
				maxX=shape.getX(i);
			}
			if (minY>shape.getY(i)) {
				minY=shape.getY(i);
			}
			if (maxY<shape.getY(i)) {
				maxY=shape.getY(i);
			}
		}
		return new Location[] {new Location(minX, minY), new Location(maxX, maxY)};
	}

	public static float pow (float t, int i) {
		if (i==0) {
			return 1;
		}
		float r = t;
		for (int j=1; j<i; j++) {
			r*=t;
		}
		return r;
	}
	
	public static int fact(int k) {
	    if(k==0 || k==1) {
	    	return 1;
	    }
	    else{
	    	return k * fact(k-1);
	    }
	}

	public static float bernstain(int i, int n, float t) {
	    return fact(n) / (fact(i) * fact(n-i))* pow(t, i) * pow(1-t, n-i);
	}                            

	public static Location getBezier(float t, Location ... points) {
	    float x = 0;
	    float y = 0;
	    int n = points.length-1;
	    for(int i=0; i <= n; i++){
			x += points[i].getX() * bernstain(i, n, t);
			y += points[i].getY() * bernstain(i, n, t);
	    }                
	    return new Location(x, y);
	}
	
	public static float computeDistance(Location p1, Location p2) {
		float deltaX = p2.getX() - p1.getX();
		float deltaY = p2.getY() - p1.getY();
		return (float)Math.sqrt(deltaX*deltaX+deltaY*deltaY);
	}
	
	public static float computeAngle(Location p1, Location p2) {
		float deltaX = p2.getX() - p1.getX();
		float deltaY = p2.getY() - p1.getY();
		return (float)Math.atan2(deltaX, -deltaY);
	}

	public static float adjustAngle(float angle) {
		while (angle<0) {
			angle+=TWO_PI;
		}
		while (angle>TWO_PI) {
			angle-=TWO_PI;
		}
		return angle;
	}

	public static Float optimizeRotation(Float currentAngle, Float targetAngle) {
		float diff = targetAngle - currentAngle;
		while (diff<-Math.PI) {
			targetAngle+=(float)Math.PI*2.0f;
			diff+=(float)Math.PI*2.0f;
		}
		while (diff>Math.PI) {
			targetAngle-=(float)Math.PI*2.0f;
			diff-=(float)Math.PI*2.0f;
		}
		return targetAngle;
	}

	public static boolean compareShape(Shape shape1, Shape shape2) {
		if (shape1==shape2) {
			return true;
		}
		if (shape1==null || shape2==null) {
			return false;
		}
		if (shape1.size() != shape2.size()) {
			return false;
		}
		for (int index=0; index<shape1.size(); index++) {
			if (!shape1.getLocation(index).equals(shape2.getLocation(index))) {
				return false;
			}
		}
		return true;
	}
	
}
