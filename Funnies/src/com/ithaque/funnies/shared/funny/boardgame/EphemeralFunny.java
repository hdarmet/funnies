package com.ithaque.funnies.shared.funny.boardgame;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.LayoutDevice;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.items.AbstractImageItem;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.SpriteImageItem;
import com.ithaque.funnies.shared.basic.items.animations.ChangeFaceAnimation;
import com.ithaque.funnies.shared.basic.items.animations.FaceFadingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.SequenceAnimation;
import com.ithaque.funnies.shared.funny.AbstractFunny;
import com.ithaque.funnies.shared.funny.Funny;
import com.ithaque.funnies.shared.funny.IncompatibleRingException;
import com.ithaque.funnies.shared.funny.AbstractRing;

public class EphemeralFunny extends AbstractFunny implements Funny {

	AbstractImageItem item;
	
	public EphemeralFunny(String id, AbstractImageItem item) {
		super(id);
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
	public GameBoardRing getRing() {
		return (GameBoardRing) super.getRing();
	}

	@Override
	public void enterRing(AbstractRing ring) {
		if (!(ring instanceof GameBoardRing)) {
			throw new IncompatibleRingException();
		}
		super.enterRing(ring);
		if (item!=null) {
			getEphemeralSupport().addItem(item);
		}
	}

	@Override
	public void exitRing(AbstractRing ring) {
		if (ring != getRing()) {
			throw new IllegalInvokeException();
		}
		if (item!=null) {
			getEphemeralSupport().removeItem(item);
		}
		super.exitRing(ring);
	}
	
	public Animation play(float x, float y, long duration) {
		item.setLocation(new Location(x, y));
		SequenceAnimation animation = new SequenceAnimation();
		long stepDuration = duration/(item.getImageCount()+1);
		for (int index=0; index<item.getImageCount(); index++) {
			animation.addAnimation(new ChangeFaceAnimation(stepDuration, index).setItem(item));
		}
		animation.addAnimation(new FaceFadingAnimation(stepDuration, 0.0f).setItem(item));
		return animation;
	}
	
	protected LayoutDevice getEphemeralSupport() {
		return getRing().infoLayer;
	}
}
