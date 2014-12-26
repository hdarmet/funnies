package com.ithaque.funnies.shared.funny.boardgame;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.items.AbstractImageItem;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.SpriteImageItem;
import com.ithaque.funnies.shared.basic.items.animations.ChangeFaceAnimation;
import com.ithaque.funnies.shared.basic.items.animations.FaceFadingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.SequenceItemAnimation;
import com.ithaque.funnies.shared.funny.Funny;
import com.ithaque.funnies.shared.funny.IncompatibleRingException;
import com.ithaque.funnies.shared.funny.Ring;

public class EphemeralFunny implements Funny {

	String id;
	AbstractImageItem item;
	
	public EphemeralFunny(String id, AbstractImageItem item) {
		this.item = item;
		for (int index=0; index<this.item.getImageCount(); index++) {
			this.item.setOpacity(index, 0.0f);
		}
	}
	
	public EphemeralFunny(String id, String ... urls) {
		this(id, new ImageItem(urls));
	}
	
	public EphemeralFunny(String id, String url, int colCount, int rowCount, float imageWidth, float imageHeight) {
		this(id, new SpriteImageItem(url, colCount, rowCount, imageWidth, imageHeight));
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void enterRing(Ring ring) {
		if (ring instanceof GameBoardRing) {
			GameBoardRing gbRing = (GameBoardRing)ring;
			gbRing.infoLayer.addItem(item);
		}
		else {
			throw new IncompatibleRingException();
		}
	}

	public Animation play(float x, float y, long duration) {
		item.setLocation(new Location(x, y));
		SequenceItemAnimation animation = new SequenceItemAnimation();
		long stepDuration = duration/(item.getImageCount()+1);
		for (int index=0; index<item.getImageCount(); index++) {
			animation.addAnimation(new ChangeFaceAnimation(stepDuration, index).setItem(item));
		}
		animation.addAnimation(new FaceFadingAnimation(stepDuration, 0.0f).setItem(item));
		return animation;
	}
}
