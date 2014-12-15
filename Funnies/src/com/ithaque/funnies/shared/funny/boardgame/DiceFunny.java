package com.ithaque.funnies.shared.funny.boardgame;

import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.animations.ChangeFaceAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ItemChangeAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ItemJumpAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ParallelItemAnimation;
import com.ithaque.funnies.shared.basic.items.animations.SequenceItemAnimation;
import com.ithaque.funnies.shared.funny.ActivableFunny;
import com.ithaque.funnies.shared.funny.IncompatibleRingException;
import com.ithaque.funnies.shared.funny.Ring;

public class DiceFunny implements ActivableFunny {

	String id;
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
		this.id = id;
		this.faces = faces;
		this.location = location;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void enterRing(Ring ring) {
		diceItem = buildDiceItem();
		if (ring instanceof GameBoardRing) {
			GameBoardRing gbRing = (GameBoardRing)ring;
			gbRing.piecesLayer.addItem(diceItem);
		}
		else {
			throw new IncompatibleRingException();
		}
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
	
	public void rollFor(Integer number) {
		Location actualLocation = diceItem.getLocation();
		float actualRotation = diceItem.getRotation();
		
		int lastFace = getFaceIndex(number);
		SequenceItemAnimation animation = new SequenceItemAnimation();
		float factor = 1.0f;
		while (factor>0.6f) {
			addJump(actualLocation, (float)(actualRotation+Math.PI*2*factor), animation, 25f+25f*factor, factor, randomFace());
			addJump(actualLocation, (float)(actualRotation), animation, 0.0f, factor*0.85f, randomFace());
			factor*=0.7f;
		}
		addJump(actualLocation, (float)(actualRotation+Math.PI*2*factor), animation, 25f+25f*factor, factor, randomFace());
		addJump(actualLocation, (float)(actualRotation), animation, 0.0f, factor*0.85f, lastFace);
		animation.setItem(diceItem);
		animation.launchFor();
	}

	private void addJump(
			Location actualLocation, 
			float actualRotation,
			SequenceItemAnimation animation, 
			float xOffset, float jumpFactor,
			int secondIndex) 
	{
		ParallelItemAnimation aggregate = new ParallelItemAnimation();
		ItemChangeAnimation turn = new ItemChangeAnimation(500, actualRotation, null);
		aggregate.addAnimation(turn);
		ItemJumpAnimation jump = new ItemJumpAnimation(500, jumpFactor);
		jump.setLocation(new Location(actualLocation.getX()+xOffset, actualLocation.getY()));
		aggregate.addAnimation(jump);
		ChangeFaceAnimation changeFace = new ChangeFaceAnimation(500, secondIndex);
		aggregate.addAnimation(changeFace);
		animation.addAnimation(aggregate);
	}

}
