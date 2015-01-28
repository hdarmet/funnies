package com.ithaque.funnies.shared.funny.boardgame;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.GroupItem;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemHolder;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.items.BaseItem;
import com.ithaque.funnies.shared.basic.items.DecoratedItem;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.animations.FaceFadingAnimation;
import com.ithaque.funnies.shared.basic.processors.AbstractTargetedDragProfile;
import com.ithaque.funnies.shared.funny.DecoratedFunny;
import com.ithaque.funnies.shared.funny.DropTargetFunny;
import com.ithaque.funnies.shared.funny.IncompatibleRingException;
import com.ithaque.funnies.shared.funny.AbstractRing;
import com.ithaque.funnies.shared.funny.TargetFunny;
import com.ithaque.funnies.shared.funny.TrackableFunny;

public class TileFunny extends DecoratedFunny implements DropTargetFunny, TrackableFunny, TargetFunny {

	public static final Animation.Factory DEFAULT_ENTER_TARGET_ANIMATION = 
		new FaceFadingAnimation.Builder(100, 1.0f)
			.setItem(AbstractTargetedDragProfile.newDropTarget());
	public static final Animation.Factory DEFAULT_EXIT_TARGET_ANIMATION = 
		new FaceFadingAnimation.Builder(100, 0.0f)
			.setItem(AbstractTargetedDragProfile.previousDropTarget());
	public static final Animation.Factory DEFAULT_SHOW_ALLOWED_TARGET_ANIMATION = 
		new FaceFadingAnimation.Builder(100, 0.2f)
			.setItem(AbstractTargetedDragProfile.possibleDropTarget());
	public static final Animation.Factory DEFAULT_HIDE_ALLOWED_TARGET_ANIMATION =
		new FaceFadingAnimation.Builder(100, 0.0f)
			.setItem(AbstractTargetedDragProfile.possibleDropTarget());

	DecoratedItem tileItem;
	Item hilightItem;
	Item activableItem;
	GroupItem holderItem;
	
	Animation.Factory targetDropAnimation = null;
	Animation.Factory enterTargetAnimation = DEFAULT_ENTER_TARGET_ANIMATION;
	Animation.Factory showAllowedTargetAnimation = DEFAULT_SHOW_ALLOWED_TARGET_ANIMATION;
	Animation.Factory hideAllowedTargetAnimation = DEFAULT_HIDE_ALLOWED_TARGET_ANIMATION;
	Animation.Factory exitTargetAnimation = DEFAULT_EXIT_TARGET_ANIMATION;
	
	public TileFunny(String id, DecoratedItem tileItem, Item hilightItem, Item activableItem) {
		super(id);
		this.tileItem = tileItem;
		this.hilightItem = hilightItem;
		this.activableItem = activableItem;
		this.holderItem = createHolderItem();
	}

	public TileFunny(String id, String tileImageUrl, String activableImageUrl, String targetImageUrl, Location[] shape) {
		this(id,
			new DecoratedItem(new ImageItem(tileImageUrl)),
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
	public void enterRing(AbstractRing ring) {
		if (!(ring instanceof GameBoardRing)) {
			throw new IncompatibleRingException();
		}
		super.enterRing(ring);
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
		gbRing.piecesLayer.addItem(holderItem);
	}

	@Override
	public void exitRing(AbstractRing ring) {
		if (ring != getRing()) {
			throw new IllegalInvokeException();
		}
		GameBoardRing gbRing = (GameBoardRing)ring;
		if (tileItem!=null) {
			gbRing.boardLayer.removeItem(tileItem);
		}
		if (hilightItem!=null) {
			gbRing.hilightLayer.removeItem(hilightItem);
		}
		if (activableItem!=null) {
			gbRing.tilesetLayer.removeItem(activableItem);
		}
		gbRing.piecesLayer.removeItem(holderItem);
		super.exitRing(ring);
	}

	@Override
	public Location getDropLocation(Item dragged, Item target) {
		return ((BaseItem)holderItem).getTargetLocation(dragged);
	}

	@Override
	public ItemHolder getDropHolder(Item dragged, Item target) {
		return ((BaseItem)holderItem).getStackTop();
	}
	
	@Override
	public Location getTargetLocation(Item dragged) {
		return ((BaseItem)holderItem).getTargetLocation(dragged);
	}

	@Override
	public ItemHolder getTargetHolder() {
		return ((BaseItem)holderItem).getStackTop();
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
	
	@Override
	public GameBoardRing getRing() {
		return (GameBoardRing) super.getRing();
	}

	@Override
	public Location getLocation() {
		return tileItem.getDisplayLocation();
	}

	@Override
	public DecoratedItem getDecorationSupport() {
		return tileItem;
	}

}
