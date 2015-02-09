package com.ithaque.funnies.shared.basic.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ithaque.funnies.shared.Location;
import com.ithaque.funnies.shared.Trace;
import com.ithaque.funnies.shared.basic.MouseEvent;

public class GestureRecognition {
	
	public static final int NO_MATCH = 1000000;
	public static final int DEFAULT_NB_SECTORS = 8;
	public static final long DEFAULT_TIME_STEP = 20;
	public static final int DEFAULT_PRECISION = 8;
	public static final int DEFAULT_FIABILITY = 30;

	public class GestureDefinition {
		
		public GestureDefinition(String id, MatchHandler matchHandler,
				Integer[] moves) {
			super();
			this.id = id;
			this.matchHandler = matchHandler;
			this.moves = moves;
		}
		
		String id;
		MatchHandler matchHandler;
		Integer[] moves;
		
		public String getId() {
			return id;
		}
	}
	
	public class Gesture {
		Location lastPoint = null;
		List<Location> points = new ArrayList<Location>();
		float minX = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE;
		float minY = Float.MAX_VALUE;
		float maxY = Float.MIN_VALUE;
		List<Integer> moves = new ArrayList<Integer>();
		int cost;
		GestureDefinition match;
		int fiability;
		
		public Location getLastPoint() {
			return lastPoint;
		}
		
		public List<Location> getPoints() {
			return points;
		}

		public float getMinX() {
			return minX;
		}

		public float getMaxX() {
			return maxX;
		}

		public float getMinY() {
			return minY;
		}

		public float getMaxY() {
			return maxY;
		}

		public List<Integer> getMoves() {
			return moves;
		}

		public int getCost() {
			return cost;
		}

		public GestureDefinition getMatch() {
			return match;
		}

		public int getFiability() {
			return fiability;
		}

		void addPoint(Location lastPoint) {
			points.add(lastPoint);
			this.lastPoint = lastPoint;
			if (lastPoint.getX()<minX) minX=lastPoint.getX();
			if (lastPoint.getX()>maxX) maxX=lastPoint.getX();
			if (lastPoint.getY()<minY) minY=lastPoint.getY();
			if (lastPoint.getY()>maxY) maxY=lastPoint.getY();
		}

		void addMove(int direction) {
			moves.add(direction);
		}

		public int indexOf(Integer ... moves) {
			for (int index=0; index<this.moves.size()-moves.length; index++) {
				if (this.moves.get(index)==moves[0]) {
					boolean found = true;
					for (int j=1; j<moves.length; j++) {
						if (this.moves.get(index+j)!=moves[j]) {
							found = false;
							break;
						}
					}
					if (found) {
						return index;
					}
				}
			}
			return -1;
		}
	}
	
	public interface MatchHandler {
		int matches(Gesture gesture);
	}
	
	float sqPrec = DEFAULT_PRECISION*DEFAULT_PRECISION;
	Map<String, GestureDefinition> gestures = new HashMap<String, GestureDefinition>();
	float[] anglesMap = null;
	int sectorCount = DEFAULT_NB_SECTORS;
	int fiability=DEFAULT_FIABILITY;
	
	public void addGesture(String id, MatchHandler matchHandler, Integer ... moves) {
		gestures.put(id, new GestureDefinition(id, matchHandler, moves));	
	}
	
	public Gesture startCapture(long time, MouseEvent event) {
		Gesture gesture = new Gesture();
		Location lastPoint = new Location(event.getX(), event.getY());
		gesture.addPoint(lastPoint);
		return gesture;
	}
	
	public void capture(long time, MouseEvent event, Gesture gesture) {
		float difx = event.getX()-gesture.getLastPoint().getX();
		float dify = event.getY()-gesture.getLastPoint().getY();
		float sqDist = difx*difx+dify*dify;
		if (sqDist>sqPrec){
			Location lastPoint = new Location(event.getX(), event.getY());
			gesture.addPoint(lastPoint);
			addMove(difx, dify, gesture);
		}
	}
	
	protected void addMove(float dx, float dy, Gesture gesture) {
		float sectorRad=(float)Math.PI*2/sectorCount;
		float angle = (float)Math.atan2(dy,dx);
		if (angle<0) {
			angle+=Math.PI*2;
		}
		gesture.addMove(getDirection(angle, sectorRad));
	}

	protected int getDirection(float angle, float sectorRad) {
		return (int)((angle+sectorRad/2)/sectorRad);
	}

	protected int computeCost(Integer[] definition, List<Integer> gesture) {		
		// point
		if (definition[0]==-1){
			return gesture.size()==0 ? 0 : NO_MATCH;
		}	
		// precalc difangles
		int[][] diff = createTable(definition.length+1, gesture.size()+1);
		int[][] result = createTable(definition.length+1, gesture.size()+1);
		
		for (int x=1;x<=definition.length;x++){
			for (int y=1;y<gesture.size();y++){
				diff[x][y]=difAngle(definition[x-1],gesture.get(y-1));
			}
		}
		// max cost
		for (int y=1;y<=gesture.size();y++) {
			result[0][y]=NO_MATCH;
		}
		for (int x=1;x<=definition.length;x++) {
			result[x][0]=NO_MATCH;
		}
		result[0][0]=0;
		// levensthein application
		int cost = 0;
		
		for (int x=1;x<=definition.length;x++){
			for (int y=1;y<gesture.size();y++){
				cost=diff[x][y];
				int pa=result[x-1][y]+cost;
				int pb=result[x][y-1]+cost;
				int pc=result[x-1][y-1]+cost;
				result[x][y]=Math.min(Math.min(pa,pb),pc);
			}
		}
		return result[definition.length][gesture.size()-1];
	}
	
	protected int difAngle(int definitionAngle, int gestureAngle) {
		int dif = definitionAngle - gestureAngle;
		if (dif<0) dif=-dif;
		if (dif>sectorCount/2) dif=sectorCount-dif;
		return dif;
	}
	
	protected int[][] createTable(int width, int height) {
		int[][] result = new int[width][];
		for (int x=0; x<width; x++) {
			result[x]=new int[height];
			for (int y=0; y<height; y++) {
				result[x][y]=0;
			}
		}
		return result;
	}
	
	public void matchGesture(Gesture gesture) {
		
		int bestCost = NO_MATCH;
		int cost = 0;
		GestureDefinition bestGesture = null;
		for (GestureDefinition definition : gestures.values()){		
			cost=computeCost(definition.moves, gesture.moves);
			if (Trace.debug) {
				Trace.debug("Gesture cost : "+cost+" for "+definition.id+" "+definition.moves+" "+gesture.moves+"\n");
			}
			if (cost<=fiability){
				if (definition.matchHandler!=null){
					gesture.cost=cost;
					cost=definition.matchHandler.matches(gesture);
				}
				if (cost<bestCost){
					bestCost=cost;
					bestGesture=definition;
				}
			}
		}
		if (bestGesture!=null) {
			gesture.match = bestGesture;
			gesture.fiability=bestCost;
		} else {
			gesture.match = null;
		}
	}

}

