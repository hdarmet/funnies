package com.ithaque.funnies.shared.funny.standard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.AnimationContext;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.GroupItem;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.items.animations.CancelableAnimation;
import com.ithaque.funnies.shared.basic.items.animations.FadingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.MoveAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ParallelAnimation;
import com.ithaque.funnies.shared.basic.items.animations.RepeatableAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ScalingAnimation;
import com.ithaque.funnies.shared.funny.AbstractFunny;
import com.ithaque.funnies.shared.funny.AbstractRing;
import com.ithaque.funnies.shared.funny.Funny;
import com.ithaque.funnies.shared.funny.FunnyObserver;
import com.ithaque.funnies.shared.funny.Icon;
import com.ithaque.funnies.shared.funny.TooledFunny;
import com.ithaque.funnies.shared.funny.TooledRing;

public class ToolbarFunny extends AbstractFunny implements FunnyObserver {

	static final float MIN_SCALE = 0.8f;
	static final float MAX_SCALE = 1.2f;
	static final long DURATION = 2000;
	
	float minimize = MIN_SCALE;
	float maximize = MAX_SCALE;
	boolean hover = false;
	CancelableAnimation hoverAnimation = null;
	boolean activated = false;
	GroupItem toolbarItem;
	List<ToolRecord> tools = new ArrayList<ToolRecord>();

	class ToolRecord {
		TooledFunny tool;
		Icon icon;
		RepeatableAnimation hoverAnimation = null;
		
		ToolRecord(TooledFunny tool, Icon icon) {
			this.tool = tool;
			this.icon = icon;
			this.hoverAnimation = null;
		}
	}

	public ToolbarFunny(String toolbarId) {
		super(toolbarId);
		toolbarItem = new GroupItem();
		toolbarItem.setScale(minimize);
		toolbarItem.addEventType(Type.MOUSE_MOVE);
	}

	public ToolbarFunny setMetrics(float minimize, float maximize) {
		this.minimize = minimize;
		this.maximize = maximize;
		return this;
	}
		
	public boolean addTool(TooledFunny tool, Icon icon) {
		if (icon.getToolSupportId().equals(getId())) {
			icon.setToolbar(this);
			if (tool.isEnabled()) {
				icon.getIconItem().addEventType(Type.MOUSE_CLICK);
			}
			icon.getIconItem().addEventType(Type.MOUSE_MOVE);
			tools.add(new ToolRecord(tool, icon));
			Location location = new Location(-getWidth()/2.0f, getHeight(icon));
			icon.getIconItem().setLocation(location);
			tool.addObserver(this);
			toolbarItem.addItem(icon.getIconItem());
			return true;
		}
		return false;
	}
	
	public boolean removeTool(TooledFunny tool, Icon icon) {
		for (ToolRecord record : new ArrayList<ToolRecord>(tools)) {
			if (record.tool == tool && record.icon == icon) {
				icon.setToolbar(null);
				icon.getIconItem().removeEventType(Type.MOUSE_CLICK);
				icon.getIconItem().removeEventType(Type.MOUSE_MOVE);
				tools.remove(record);
				record.tool.removeObserver(this);
				toolbarItem.removeItem(record.icon.getIconItem());
				return true;
			}
		}
		return false;
	}
	
	public List<Icon> getTools() {
		List<Icon> icons = new ArrayList<Icon>();
		for (ToolRecord record : tools) {
			icons.add(record.icon);
		}
		return icons;
	}
	
	protected float getHeight(Icon icon) {
		float height = 0.0f;
		for (ToolRecord record : tools) {
			if (record.tool.isEnabled()) {
				if (record.icon==icon) {
					return height+icon.getHeight()/2.0f;
				}
				else {
					height+=icon.getHeight();
				}
			}
		}
		return 0.0f;
	}
	
	float getHeight() {
		float height = 0.0f;
		for (ToolRecord record : tools) {
			if (record.tool.isEnabled()) {
				height += record.icon.getHeight();
			}
		}
		return height;
	}

	float getWidth() {
		float width = 0.0f;
		for (ToolRecord record : tools) {
			if (record.tool.isEnabled() && record.icon.getWidth()>width) {
				width = record.icon.getWidth();
			}
		}
		return width;
	}
	
	public Item getToolbarItem() {
		return toolbarItem;
	}

	public void setLocation(float width, float height) {
		toolbarItem.setLocation(width, height);
	}
	
	@Override
	public void enterRing(AbstractRing ring) {
		if (!(ring instanceof TooledRing)) {
			throw new IllegalInvokeException();
		}
		((TooledRing)ring).getToolSupport().addItem(toolbarItem);
		super.enterRing(ring);
	}
	
	@Override
	public void exitRing(AbstractRing ring) {
		if (!(ring instanceof TooledRing)) {
			throw new IllegalInvokeException();
		}
		((TooledRing)ring).getToolSupport().removeItem(toolbarItem);
		super.exitRing(ring);
	}

	public void hover(Collection<Item> hoveredItems) {
		hoverToolbar(hoveredItems);
		for (ToolRecord toolRecord : tools) {
			if (toolRecord.tool.isEnabled()) {
				hoverTool(toolRecord, hoveredItems);
			}
		}
	}

	void hoverTool(ToolRecord toolRecord, Collection<Item> hoveredItems) {
		if (hoveredItems.contains(toolRecord.icon.getIconItem())) {
			if (toolRecord.hoverAnimation==null) {
				Animation animation = createToolAnimation(toolRecord);
				if (animation!=null) {
					toolRecord.hoverAnimation = new RepeatableAnimation(animation);
					getBoard().launchAnimation(toolRecord.hoverAnimation);
				}
			}
			else if (toolRecord.hoverAnimation.isFinished()) {
				toolRecord.hoverAnimation.reset();
				if (toolRecord.hoverAnimation!=null) {
					getBoard().launchAnimation(toolRecord.hoverAnimation);
				}
			}
		}
		else {
			if (toolRecord.hoverAnimation!=null && !toolRecord.hoverAnimation.isFinished()) {
				toolRecord.hoverAnimation.stop();
			}
		}
	}

	public void activateTool(TooledFunny funny, Icon icon) {
		if (!activated) {
			this.activated = true;
			minimizeToolbar();
			funny.activateTool(icon);
		}
	}

	Animation createToolAnimation(ToolRecord record) {
		Animation.Factory factory = record.icon.getHoverAnimation();
		if (factory!=null) {
			AnimationContext context = new Icon.IconAnimationContext(record.icon.getIconItem().getWrapped());
			Animation animation = factory.create();
			animation.setContext(context);
			return animation;
		}
		return null;
	}

	void hoverToolbar(Collection<Item> hoveredItems) {
		boolean hover = hoveredItems.contains(this.toolbarItem);
		if (this.hover!=hover && (this.hoverAnimation==null || this.hoverAnimation.isFinished()) && !activated) {
			this.hover = hover;
			if (hover) {
				maximizeToolbar();
			}
			else {
				minimizeToolbar();
			}
		}
	}

	void maximizeToolbar() {
		if (this.hoverAnimation!=null && !(this.hoverAnimation.isFinished())) {
			this.hoverAnimation.cancel();
		}
		this.hoverAnimation =  new CancelableAnimation(new ScalingAnimation(1000, maximize).setItem(toolbarItem));
		getBoard().launchAnimation(this.hoverAnimation);
	}

	void minimizeToolbar() {
		if (this.hoverAnimation!=null && !(this.hoverAnimation.isFinished())) {
			this.hoverAnimation.cancel();
		}
		this.hoverAnimation =  new CancelableAnimation(new ScalingAnimation(1000, minimize).setItem(toolbarItem)) {
			public void finish(long time) {
				super.finish(time);
				activated = false;
			}
		};
		getBoard().launchAnimation(this.hoverAnimation);
	}

	Board getBoard() {
		return this.toolbarItem.getBoard();
	}

	@Override
	public void change(ChangeType type, Funny funny) {
		if (type==ChangeType.ENABLING) {
			for (ToolRecord record : tools) {
				if (record.tool==funny) {
					Item item = record.icon.getIconItem();
					if (record.tool.isEnabled()) {
						item.addEventType(Type.MOUSE_CLICK);
					}
					else {
						item.removeEventType(Type.MOUSE_CLICK);
					}
				}
			}
			adjustLayout();
		}
	}

	void adjustLayout() {
		ParallelAnimation animation = new ParallelAnimation();
		float height = 0.0f;
		float width = getWidth();
		for (ToolRecord record : tools) {
			Item item = record.icon.getIconItem();
			if (record.tool.isEnabled()) {
				Location newLocation = new Location(-width/2.0f, height+record.icon.getHeight()/2.0f);
				Location iconLocation = item.getLocation();
				if (!iconLocation.equals(newLocation)) {
					if (item.getOpacity()==0.0f) {
						item.setLocation(newLocation);
					}
					else {
						animation.addAnimation(new MoveAnimation(DURATION).setLocation(newLocation).setItem(item));
					}
				}
				if (item.getOpacity()<1.0f) {
					animation.addAnimation(new FadingAnimation(DURATION, 1.0f).setItem(item));
				}
				height+=record.icon.getHeight();
			}
			else {
				if (item.getOpacity()>0.0f) {
					animation.addAnimation(new FadingAnimation(DURATION, 0.0f).setItem(item));
				}
			}
		}
		getBoard().launchAnimation(animation);
	}

}
