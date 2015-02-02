package com.ithaque.funnies.shared.funny.standard;

import com.ithaque.funnies.shared.basic.Animation.Factory;
import com.ithaque.funnies.shared.basic.Color;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.TransformUtil;
import com.ithaque.funnies.shared.basic.items.DecoratedItem;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.PolygonItem;
import com.ithaque.funnies.shared.basic.items.animations.DetachAnimation;
import com.ithaque.funnies.shared.basic.items.animations.FadingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.MoveAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ParallelAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ScalingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.SequenceAnimation;
import com.ithaque.funnies.shared.funny.AbstractFunny;
import com.ithaque.funnies.shared.funny.ActivableFunny;
import com.ithaque.funnies.shared.funny.FunnyObserver.ChangeType;
import com.ithaque.funnies.shared.funny.HoverFunny;
import com.ithaque.funnies.shared.funny.HoverProcessor;
import com.ithaque.funnies.shared.funny.Icon;
import com.ithaque.funnies.shared.funny.PanelRing;
import com.ithaque.funnies.shared.funny.TooledFunny;

public class PanelFunny extends AbstractFunny implements TooledFunny, ActivableFunny, HoverFunny {

	DecoratedItem panelItem;
	Item backgroundItem;
	ImageItem closeItem;
	Icon lastIcon;
	
	public PanelFunny(String id, float panelWidth, float panelHeight, Color fillColor, Color lineColor, float lineWidth) {
		super(id);
		backgroundItem = new PolygonItem(fillColor, lineColor, lineWidth, 1.0f, makeShape(panelWidth, panelHeight));
		closeItem = new ImageItem("close.png");
		closeItem.addEventType(Type.MOUSE_CLICK);
		closeItem.addEventType(Type.MOUSE_MOVE);
		panelItem = new DecoratedItem(backgroundItem);
		panelItem.addItem(closeItem, panelWidth/2.0f, -panelHeight/2.0f); 
		panelItem.setOpacity(0.0f);
		lastIcon = getIcons()[0];
	}

	Location[] makeShape(float panelWidth, float panelHeight) {
		return new Location[] {
			new Location(-panelWidth/2.0f, -panelHeight/2.0f),
			new Location(panelWidth/2.0f, -panelHeight/2.0f),
			new Location(panelWidth/2.0f, panelHeight/2.0f),
			new Location(-panelWidth/2.0f, panelHeight/2.0f)
		};
	}

	Icon toolIcon;
	boolean enabled = true;
	
	public PanelFunny setToolIcon(Icon toolIcon) {
		this.toolIcon = toolIcon;
		toolIcon.setOwner(this);
		return this;
	}

	@Override
	public Icon[] getIcons() {
		return new Icon[] {toolIcon};
	}

	@Override
	public void activateTool(Icon icon) {
		lastIcon = icon;
		((PanelRing)getRing()).getPanelSupport().addItem(panelItem);
		panelItem.setLocation(getHiddenLocation());
		panelItem.setScale(0.01f);
		ParallelAnimation animation = new ParallelAnimation();
		animation.addAnimation(new ScalingAnimation(1000, 1.0f).setItem(panelItem));
		animation.addAnimation(new FadingAnimation(1000, 1.0f).setItem(panelItem));
		animation.addAnimation(new MoveAnimation(1000, Location.ORIGIN).setItem(panelItem));
		((PanelRing)getRing()).enterModalMode(animation);
	}
	
	protected Location getHiddenLocation() {
		Location panelLocation = lastIcon.getIconItem().getDisplayLocation();
		return TransformUtil.invertTransformLocation(panelItem.getParent(), panelLocation);
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		fire(ChangeType.ENABLING);
	}

	@Override
	public Item[] getActivableItems() {
		return new Item[] {closeItem};
	}

	@Override
	public void activate(Item activated) {
		SequenceAnimation sequence = new SequenceAnimation(); 
		ParallelAnimation animation = new ParallelAnimation();
		animation.addAnimation(new ScalingAnimation(1000, 0.01f).setItem(panelItem));
		animation.addAnimation(new FadingAnimation(1000, 0.0f).setItem(panelItem));
		animation.addAnimation(new MoveAnimation(1000, getHiddenLocation()).setItem(panelItem));
		sequence.addAnimation(animation);
		sequence.addAnimation(new DetachAnimation().setItem(panelItem));
		((PanelRing)getRing()).exitModalMode(sequence);
	}

	@Override
	public Item[] getHoverables() {
		return new Item[] {closeItem};
	}

	@Override
	public Factory getHoverAnimation(Item item) {
		SequenceAnimation.Builder builder = new SequenceAnimation.Builder();
		builder.addAnimation(new ScalingAnimation.Builder(500, 1.2f).setItem(HoverProcessor.item()));
		builder.addAnimation(new ScalingAnimation.Builder(500, 1.0f).setItem(HoverProcessor.item()));
		return builder;
	}
	
}
