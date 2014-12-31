package com.ithaque.funnies.shared.funny.boardgame;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.animations.BezierAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ChangeFaceAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ParallelAnimation;
import com.ithaque.funnies.shared.basic.items.animations.RotateAnimation;
import com.ithaque.funnies.shared.basic.items.animations.SequenceAnimation;
import com.ithaque.funnies.shared.funny.AbstractFunny;
import com.ithaque.funnies.shared.funny.ActivableFunny;
import com.ithaque.funnies.shared.funny.IncompatibleRingException;
import com.ithaque.funnies.shared.funny.Ring;

public class DiceFunny extends AbstractFunny implements ActivableFunny {

	ImageItem diceItem;
	DiceFace[] faces;
	Location location;
	int currentFace = 0;

	public DiceFunny(String id, float x, float y, String urlTemplate, int minFace, int maxFace) {
		this(id, new Location(x, y), buildFaces(urlTemplate, minFace, maxFace));
	}
	
	static DiceFace[] buildFaces(String urlTemplate, int minFace,int maxFace) {
		DiceFace[] faces = new DiceFace[maxFace-minFace+1];
		for (int faceValue = minFace; faceValue<=maxFace; faceValue++) {
			String faceUrl = urlTemplate.replace("{0}", ""+faceValue);
			faces[faceValue-minFace] = new DiceFace(faceUrl, faceValue);
		}
		return faces;
	}

	public DiceFunny(String id, Location location, DiceFace ... faces) {
		super(id);
		this.faces = faces;
		this.location = location;
		diceItem = buildDiceItem();
	}
	
	@Override
	public GameBoardRing getRing() {
		return (GameBoardRing) super.getRing();
	}
	
	@Override
	public void enterRing(Ring ring) {
		if (!(ring instanceof GameBoardRing)) {
			throw new IncompatibleRingException();
		}
		super.enterRing(ring);
		GameBoardRing gbRing = (GameBoardRing)ring;
		if (diceItem!=null) {
			gbRing.piecesLayer.addItem(diceItem);
		}
	}

	@Override
	public void exitRing(Ring ring) {
		if (ring != getRing()) {
			throw new IllegalInvokeException();
		}
		GameBoardRing gbRing = (GameBoardRing)ring;
		if (diceItem!=null) {
			gbRing.piecesLayer.removeItem(diceItem);
		}
		super.exitRing(ring);
	}

	ImageItem buildDiceItem() {
		String[] urls = new String[faces.length];
		for (int faceIndex=0; faceIndex<faces.length; faceIndex++) {
			urls[faceIndex] = faces[faceIndex].getFaceUrl();
		}
		ImageItem item = new ImageItem(urls);
		item.addEventType(Type.MOUSE_CLICK);
		item.setOpacity(currentFace, 1.0f);
		for (int faceIndex=0; faceIndex<faces.length; faceIndex++) {
			if (faceIndex!=currentFace) {
				item.setOpacity(faceIndex, 0.0f);
			}
		}
		item.setLocation(location);
		return item;
	}

	public static class DiceFace {
		
		public DiceFace(String faceUrl, String stringValue) {
			super();
			this.faceUrl = faceUrl;
			this.stringValue = stringValue;
		}

		public DiceFace(String faceUrl, int intValue) {
			super();
			this.faceUrl = faceUrl;
			this.intValue = intValue;
		}
		
		String faceUrl;
		String stringValue;
		Integer intValue;
		
		public String getFaceUrl() {
			return faceUrl;
		}
		public String getStringValue() {
			return stringValue;
		}
		public Integer getIntValue() {
			return intValue;
		}
		
	}

	int getFaceIndex(Integer number) {
		for (int index=0; index<faces.length; index++) {
			if (faces[index].getIntValue()==number) {
				return index;
			}
		}
		return -1;
	}
	
	@Override
	public Item[] getActivableItems() {
		return new Item[] {diceItem};
	}
	
	int randomFace() {
		float random = diceItem.getBoard().randomize();
		return (int) (random*faces.length);
	}
	
	public Animation rollFor(Integer number) {
		Location actualLocation = diceItem.getLocation();
		float actualRotation = diceItem.getRotation();
		
		int lastFace = getFaceIndex(number);
		SequenceAnimation animation = new SequenceAnimation();
		float factor = 1.0f;
		while (factor>0.6f) {
			float halfOffset = (25f+25f*factor)/2.0f;
			addJump(actualLocation, (float)(actualRotation+Math.PI*2*factor), animation, 25f+25f*factor, halfOffset, factor*100, randomFace());
			addJump(actualLocation, (float)(actualRotation), animation, 0.0f, halfOffset, factor*0.85f*100, randomFace());
			factor*=0.7f;
		}
		float halfOffset = (25f+25f*factor)/2.0f;
		addJump(actualLocation, (float)(actualRotation+Math.PI*2*factor), animation, 25f+25f*factor, halfOffset, factor*100, randomFace());
		addJump(actualLocation, (float)(actualRotation), animation, 0.0f, halfOffset, factor*0.85f*100, lastFace);
		return animation;
	}

	private void addJump(
			Location actualLocation, 
			float actualRotation,
			SequenceAnimation animation,
			float xOffset,
			float xHalfOffset,
			float yOffset,
			int secondIndex) 
	{
		ParallelAnimation aggregate = new ParallelAnimation();
		RotateAnimation turn = new RotateAnimation(500, actualRotation);
		turn.setItem(diceItem);
		aggregate.addAnimation(turn);
		
		BezierAnimation jump = new BezierAnimation(500, 
			new Location(actualLocation.getX()+xHalfOffset, actualLocation.getY()-yOffset));
		jump.setItem(diceItem);
		jump.setLocation(new Location(actualLocation.getX()+xOffset, actualLocation.getY()));
		aggregate.addAnimation(jump);
		
		ChangeFaceAnimation changeFace = new ChangeFaceAnimation(500, secondIndex);
		changeFace.setItem(diceItem);
		aggregate.addAnimation(changeFace);
		animation.addAnimation(aggregate);
	}

}
