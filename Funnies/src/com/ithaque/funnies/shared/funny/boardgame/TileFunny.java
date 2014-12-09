package com.ithaque.funnies.shared.funny.boardgame;

import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.animations.ItemAnimation;
import com.ithaque.funnies.shared.funny.DropTargetFunny;
import com.ithaque.funnies.shared.funny.IncompatibleRingException;
import com.ithaque.funnies.shared.funny.Ring;

public class TileFunny implements DropTargetFunny {

	String id;
	
	Item tileItem;
	Item hilightItem;
	Item activableItem;
	
	ItemAnimation targetDropAnimation;
	ItemAnimation enterTargetAnimation;
	ItemAnimation showAllowedTargetAnimation;
	ItemAnimation hideAllowedTargetAnimation;
	ItemAnimation exitTargetAnimation;
	
	public TileFunny(String id, Item tileItem, Item hilightItem, Item activableItem) {
		this.id = id;
		this.tileItem = tileItem;
		this.hilightItem = hilightItem;
		this.activableItem = activableItem;
	}
	
	public TileFunny(String id, String tileImageUrl, String activableImageUrl, String targetImageUrl, Location[] shape) {
		this(id,
			new ImageItem(tileImageUrl),
			new ImageItem(activableImageUrl),
			new ImageItem(targetImageUrl)
		);
		activableItem.setShape(shape);
		((ImageItem)activableItem).setOpacity(0, 0.0f);
		((ImageItem)hilightItem).setOpacity(0, 0.0f);
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	public void setLocation(Location location) {
		if (tileItem!=null) {
			tileItem.setLocation(location);
		}
		if (hilightItem != null) {
			hilightItem.setLocation(location);
		}
		if (activableItem != null) {
			activableItem.setLocation(location);
		}
	}
	
	public void setRotation(float rotation) {
		if (tileItem!=null) {
			tileItem.setRotation(rotation);
		}
		if (hilightItem != null) {
			hilightItem.setRotation(rotation);
		}
		if (activableItem != null) {
			activableItem.setRotation(rotation);
		}
	}
	
	public void setScale(float scale) {
		if (tileItem!=null) {
			tileItem.setScale(scale);
		}
		if (hilightItem != null) {
			hilightItem.setScale(scale);
		}
		if (activableItem != null) {
			activableItem.setScale(scale);
		}
	}

	@Override
	public Item[] getDropTargetItems() {
		if (activableItem!=null) {
			return new Item[] {activableItem};
		}
		else {
			return new Item[0]; 
		}
	}

	@Override
	public ItemAnimation getTargetDropAnimation() {
		return targetDropAnimation;
	}

	@Override
	public ItemAnimation getEnterTargetAnimation() {
		return enterTargetAnimation;
	}

	@Override
	public ItemAnimation getExitTargetAnimation() {
		return exitTargetAnimation;
	}

	public void setTargetDropAnimation(ItemAnimation targetDropAnimation) {
		this.targetDropAnimation = targetDropAnimation;
	}

	public void setEnterTargetAnimation(ItemAnimation enterTargetAnimation) {
		this.enterTargetAnimation = enterTargetAnimation;
	}

	public void setExitTargetAnimation(ItemAnimation exitTargetAnimation) {
		this.exitTargetAnimation = exitTargetAnimation;
	}

	@Override
	public ItemAnimation getShowAllowedTargetAnimation() {
		return showAllowedTargetAnimation;
	}

	@Override
	public ItemAnimation getHideAllowedTargetAnimation() {
		return hideAllowedTargetAnimation;
	}
	
	public void setShowAllowedTargetAnimation(
			ItemAnimation showAllowedTargetAnimation) {
		this.showAllowedTargetAnimation = showAllowedTargetAnimation;
	}

	public void setHideAllowedTargetAnimation(
			ItemAnimation hideAllowedTargetAnimation) {
		this.hideAllowedTargetAnimation = hideAllowedTargetAnimation;
	}

	@Override
	public Item getHilightItem(Item target) {
		return hilightItem;
	}
	
	@Override
	public void enterRing(Ring ring) {
		if (ring instanceof GameBoardRing) {
			GameBoardRing gbRing = (GameBoardRing)ring;
			if (tileItem!=null) {
				gbRing.boardLayer.addItem(tileItem);
			}
			if (hilightItem!=null) {
				gbRing.hilightLayer.addItem(hilightItem);
			}
			if (activableItem!=null) {
				gbRing.tilesetLayer.addItem(activableItem);
			}
		}
		else {
			throw new IncompatibleRingException();
		}
	}

	public static class HHexFunny extends TileFunny {

		public HHexFunny(
			String id, 
			String tileImageUrl,
			String activableImageUrl,
			String targetImageUrl,
			float radius,
			float x,
			float y) 
		{
			super(id, tileImageUrl, activableImageUrl, targetImageUrl, buildHHexShape(radius));
			setLocation(new Location(x, y));
		}

		static Location[] buildHHexShape(float radius) {
			float hradius = radius/2.0f;
			float sqr3radius = (float)(hradius*Math.sqrt(3.0));
			return new Location[] {
				new Location(-radius, 0.0f),
				new Location(-hradius, -sqr3radius),
				new Location(hradius, -sqr3radius),
				new Location(radius, 0.0f),
				new Location(hradius, sqr3radius),
				new Location(-hradius, sqr3radius),
			};
		}

	}
	
	public static class VHexFunny extends TileFunny {

		public VHexFunny(
			String id, 
			String tileImageUrl,
			String activableImageUrl,
			String targetImageUrl,
			float radius,
			float x,
			float y) 
		{
			super(id, tileImageUrl, activableImageUrl, targetImageUrl, buildVHexShape(radius));
			setLocation(new Location(x, y));
		}

		static Location[] buildVHexShape(float radius) {
			float hradius = radius/2.0f;
			float sqr3radius = (float)(hradius*Math.sqrt(3.0));
			return new Location[] {
				new Location(0.0f, -radius),
				new Location(sqr3radius, -hradius),
				new Location(sqr3radius, hradius),
				new Location(0.0f, radius),
				new Location(-sqr3radius, hradius),
				new Location(-sqr3radius, -hradius),
			};
		}

	}

}
