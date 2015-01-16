package com.ithaque.funnies.shared.funny.boardgame;

import com.ithaque.funnies.shared.Geometric;
import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Color;
import com.ithaque.funnies.shared.basic.Font;
import com.ithaque.funnies.shared.basic.LayoutDevice;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.TransformUtil;
import com.ithaque.funnies.shared.basic.items.TextItem;
import com.ithaque.funnies.shared.basic.items.animations.FadingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.MoveAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ParallelAnimation;
import com.ithaque.funnies.shared.basic.items.animations.SequenceAnimation;
import com.ithaque.funnies.shared.basic.items.animations.easing.LinearEasing;
import com.ithaque.funnies.shared.funny.AbstractFunny;
import com.ithaque.funnies.shared.funny.Funny;
import com.ithaque.funnies.shared.funny.IncompatibleRingException;
import com.ithaque.funnies.shared.funny.AbstractRing;
import com.ithaque.funnies.shared.funny.TrackableFunny;

public class MessageFunny extends AbstractFunny implements Funny {

	TextItem item;
		
	public MessageFunny(String id, TextItem item) {
		super(id);
		this.item = item;
		this.item.setOpacity(0.0f);
	}
		
	public MessageFunny(String id, String message, Color color, Font font) {
		this(id, new TextItem(message, color, font));
	}
		
	@Override
	public GameBoardRing getRing() {
		return (GameBoardRing) super.getRing();
	}
	
	public void setMessage(String message) {
		item.setText(message);
	}

	public void setStyle(Color color, Font font) {
		item.setTextStyle(color, font);
	}
	
	@Override
	public void enterRing(AbstractRing ring) {
		if (!(ring instanceof GameBoardRing)) {
			throw new IncompatibleRingException();
		}
		super.enterRing(ring);
		if (item!=null) {
			getMessageSupport().addItem(item);
		}
	}

	@Override
	public void exitRing(AbstractRing ring) {
		if (ring != getRing()) {
			throw new IllegalInvokeException();
		}
		if (item!=null) {
			getMessageSupport().removeItem(item);
		}
		super.exitRing(ring);
	}
	
	public Animation play(float x, float y, float displacement, long duration) {
		item.setLocation(new Location(x, y));
		ParallelAnimation animation = new ParallelAnimation();
		animation.addAnimation(new MoveAnimation(new LinearEasing(duration), new Location(x, y-displacement)).setItem(item));
		SequenceAnimation fading = new SequenceAnimation();
		fading.addAnimation(new FadingAnimation((long)(duration*0.1f), 1.0f).setItem(item));
		fading.addAnimation(new FadingAnimation((long)(duration*0.9f), 0.0f).setItem(item));
		animation.addAnimation(fading);
		return animation;
	}

	public Animation play(TrackableFunny target, float displacement, long duration) {
		Location[] area = Geometric.getArea(
				TransformUtil.invertTransformShape(getMessageSupport(), item.getShape()));
		Location targetLocation = TransformUtil.invertTransformLocation(
				getMessageSupport(), target.getLocation());
		return play(targetLocation.getX(), targetLocation.getY()-(area[1].getY()-area[0].getY())/2.0f, displacement, duration);
	}
	
	protected LayoutDevice getMessageSupport() {
		return getRing().infoLayer;
	}
}
