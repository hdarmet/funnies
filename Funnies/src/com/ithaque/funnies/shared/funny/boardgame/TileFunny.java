package com.ithaque.funnies.shared.funny.boardgame;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.GroupItem;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemHolder;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.items.BaseItem;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.funny.DropTargetFunny;
import com.ithaque.funnies.shared.funny.IncompatibleRingException;
import com.ithaque.funnies.shared.funny.Ring;

public class TileFunny implements DropTargetFunny {

	String id;
	GameBoardRing gbRing;
	
	Item tileItem;
	Item hilightItem;
	Item activableItem;
	GroupItem holderItem;
	
	Animation.Factory targetDropAnimation;
	Animation.Factory enterTargetAnimation;
	Animation.Factory showAllowedTargetAnimation;
	Animation.Factory hideAllowedTargetAnimation;
	Animation.Factory exitTargetAnimation;
	
	public TileFunny(String id, Item tileItem, Item hilightItem, Item activableItem) {
		this.id = id;
		this.tileItem = tileItem;
		this.hilightItem = hilightItem;
		this.activableItem = activableItem;
		this.holderItem = createHolderItem();
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
	
	protected GroupItem createHolderItem() {
		return new BaseItem();
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
		holderItem.setLocation(location);
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
		holderItem.setRotation(rotation);
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
		holderItem.setScale(scale);
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
	public Animation.Factory getTargetDropAnimation() {
		return targetDropAnimation;
	}

	@Override
	public Animation.Factory getEnterTargetAnimation() {
		return enterTargetAnimation;
	}

	@Override
	public Animation.Factory getExitTargetAnimation() {
		return exitTargetAnimation;
	}

	public void setTargetDropAnimation(Animation.Factory targetDropAnimation) {
		this.targetDropAnimation = targetDropAnimation;
	}

	public void setEnterTargetAnimation(Animation.Factory enterTargetAnimation) {
		this.enterTargetAnimation = enterTargetAnimation;
	}

	public void setExitTargetAnimation(Animation.Factory exitTargetAnimation) {
		this.exitTargetAnimation = exitTargetAnimation;
	}

	@Override
	public Animation.Factory getShowAllowedTargetAnimation() {
		return showAllowedTargetAnimation;
	}

	@Override
	public Animation.Factory getHideAllowedTargetAnimation() {
		return hideAllowedTargetAnimation;
	}
	
	public void setShowAllowedTargetAnimation(
			Animation.Factory showAllowedTargetAnimation) {
		this.showAllowedTargetAnimation = showAllowedTargetAnimation;
	}

	public void setHideAllowedTargetAnimation(
			Animation.Factory hideAllowedTargetAnimation) {
		this.hideAllowedTargetAnimation = hideAllowedTargetAnimation;
	}

	@Override
	public Item getHilightItem(Item target) {
		return hilightItem;
	}
	
	@Override
	public void enterRing(Ring ring) {
		if (ring instanceof GameBoardRing) {
			gbRing = (GameBoardRing)ring;
			if (tileItem!=null) {
				gbRing.boardLayer.addItem(tileItem);
			}
			if (hilightItem!=null) {
				gbRing.hilightLayer.addItem(hilightItem);
			}
			if (activableItem!=null) {
				gbRing.tilesetLayer.addItem(activableItem);
			}
			gbRing.piecesLayer.addItem(holderItem);
		}
		else {
			throw new IncompatibleRingException();
		}
	}

	@Override
	public Location getDropLocation(Item dragged, Item target) {
		return ((BaseItem)holderItem).getTargetLocation(dragged);
	}

	@Override
	public ItemHolder getDropHolder(Item dragged, Item target) {
		return holderItem;
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
