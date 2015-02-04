package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.AnimationContext.MoveableFinder;
import com.ithaque.funnies.shared.basic.AnimationContext;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Moveable;
import com.ithaque.funnies.shared.basic.WrapperItem;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.SpriteImageItem;
import com.ithaque.funnies.shared.basic.items.animations.ChangeFaceAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ScalingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.SequenceAnimation;
import com.ithaque.funnies.shared.funny.standard.ToolbarFunny;

public class Icon {
	IconItem iconItem;
	String toolSupportId;
	float width;
	float height;
	TooledFunny owner = null;
	ToolbarFunny toolbar = null;
	Animation.Factory hoverAnimation = null;
	
	public class IconItem extends WrapperItem {

		public IconItem(Item wrapped) {
			super(wrapped);
		}

		public Icon getIcon() {
			return Icon.this;
		}
	}
	
	public Icon(String toolSupportId, float width, float height, Item iconItem) {
		this.iconItem = new IconItem(iconItem);
		this.toolSupportId = toolSupportId;
		this.width = width;
		this.height = height;
	}

	public Icon(String toolSupportId, float width, float height, String ... urls) {
		this(toolSupportId, width, height, new ImageItem(0, urls));
	}
	
	public IconItem getIconItem() {
		return iconItem;
	}
	
	public String getToolSupportId() {
		return toolSupportId;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}

	public TooledFunny getOwner() {
		return owner;
	}

	public void setOwner(TooledFunny owner) {
		this.owner = owner;
	}

	public ToolbarFunny getToolbar() {
		return toolbar;
	}

	public void setToolbar(ToolbarFunny toolbar) {
		this.toolbar = toolbar;
	}
	
	public Animation.Factory getHoverAnimation() {
		return hoverAnimation;
	}

	public void setHoverAnimation(Animation.Factory hoverAnimation) {
		this.hoverAnimation = hoverAnimation;
	}

	public static class SingleImageIcon extends Icon {
		public SingleImageIcon(String toolSupportId, float width, float height, String url) {
			super(toolSupportId, width, height, url);
			setHoverAnimation(
				new SequenceAnimation.Builder()
					.addAnimation(new ScalingAnimation.Builder(2000, 1.2f).setItem(item()))
					.addAnimation(new ScalingAnimation.Builder(2000, 1.0f).setItem(item()))
				);
		}
	}

	public static class DoubleImageIcon extends Icon {
		public DoubleImageIcon(String toolSupportId, float width, float height, String url1, String url2) {
			super(toolSupportId, width, height, url1, url2);
			setHoverAnimation(
				new SequenceAnimation.Builder()
					.addAnimation(new ChangeFaceAnimation.Builder(2000, 1).setItem(item()))
					.addAnimation(new ChangeFaceAnimation.Builder(2000, 0).setItem(item()))
				);
		}
		
	}

	public static class SpriteIcon extends Icon {
		public SpriteIcon(String toolSupportId, float width, float height, String url, int colCount, int rowCount, int imageWidth, int imageHeight) {
			super(toolSupportId, width, height, new SpriteImageItem(url, colCount, rowCount, imageWidth, imageHeight));
			setHoverAnimation(
				SpriteImageItem.play(4000, colCount*rowCount, item())
			);
		}
		
	}

	public static MoveableFinder item() {
		return new MoveableFinder() {
			MoveableFinder finder = HoverProcessor.item();
			@Override
			public Moveable find(AnimationContext context) {
				IconItem iconItem = (IconItem)finder.find(context);
				return iconItem==null ? null : iconItem.getWrapped();
			}
		};
	}
}
