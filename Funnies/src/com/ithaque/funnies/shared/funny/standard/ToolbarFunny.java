package com.ithaque.funnies.shared.funny.standard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.GroupItem;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.items.animations.CancelableAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ChangeFaceAnimation;
import com.ithaque.funnies.shared.basic.items.animations.RepeatableAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ScalingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.SequenceAnimation;
import com.ithaque.funnies.shared.funny.AbstractFunny;
import com.ithaque.funnies.shared.funny.AbstractRing;
import com.ithaque.funnies.shared.funny.TooledRing;

public class ToolbarFunny extends AbstractFunny {

	static final float MIN_SCALE = 0.3f;
	static final float MAX_SCALE = 1.0f;
	
	float minimize = MIN_SCALE;
	float maximize = MAX_SCALE;
	boolean hover = false;
	CancelableAnimation hoverAnimation = null;
	boolean activated = false;
	
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
	
	GroupItem toolbarItem;
	List<ToolRecord> tools = new ArrayList<ToolRecord>();
	
	public boolean addTool(Icon icon) {
		if (icon.getToolSupportId().equals(getId())) {
			icon.getIconItem().addEventType(Type.MOUSE_CLICK);
			icon.getIconItem().addEventType(Type.MOUSE_MOVE);
			tools.add(new ToolRecord(icon, true));
			icon.iconItem.setLocation(-getWidth()/2.0f, getHeight(icon));
			toolbarItem.addItem(icon.iconItem);
			return true;
		}
		return false;
	}
	
	public boolean removeTool(Icon icon) {
		for (ToolRecord record : new ArrayList<ToolRecord>(tools)) {
			if (record.tool == icon) {
				icon.getIconItem().removeEventType(Type.MOUSE_CLICK);
				icon.getIconItem().removeEventType(Type.MOUSE_MOVE);
				tools.remove(record);
				toolbarItem.removeItem(icon.iconItem);
				return true;
			}
		}
		return false;
	}

	public List<Icon> getTools() {
		List<Icon> icons = new ArrayList<Icon>();
		for (ToolRecord record : tools) {
			icons.add(record.tool);
		}
		return icons;
	}
	
	protected float getHeight(Icon icon) {
		float height = 0.0f;
		for (ToolRecord record : tools) {
			if (record.activated) {
				if (record.tool==icon) {
					return height+icon.height/2.0f;
				}
				else {
					height+=icon.height;
				}
			}
		}
		return 0.0f;
	}
	
	float getHeight() {
		float height = 0.0f;
		for (ToolRecord record : tools) {
			if (record.activated) {
				height += record.tool.height;
			}
		}
		return height;
	}

	float getWidth() {
		float width = 0.0f;
		for (ToolRecord record : tools) {
			if (record.activated && record.tool.width>width) {
				width = record.tool.width;
			}
		}
		return width;
	}

	class ToolRecord {
		Icon tool;
		boolean activated;
		RepeatableAnimation hoverAnimation = null;
		
		ToolRecord(Icon tool, boolean activated) {
			this.tool = tool;
			this.activated = activated;
			this.hoverAnimation = null;
		}
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
			if (toolRecord.activated) {
				hoverTool(toolRecord, hoveredItems);
			}
		}
	}

	void hoverTool(ToolRecord tool, Collection<Item> hoveredItems) {
		if (hoveredItems.contains(tool.tool.getIconItem())) {
			if (tool.hoverAnimation==null) {
				tool.hoverAnimation = new RepeatableAnimation(createToolAnimation(tool.tool.iconItem));
				getBoard().launchAnimation(tool.hoverAnimation);
			}
			else if (tool.hoverAnimation.isFinished()) {
				tool.hoverAnimation.reset();
				getBoard().launchAnimation(tool.hoverAnimation);
			}
		}
		else {
			if (tool.hoverAnimation!=null && !tool.hoverAnimation.isFinished()) {
				tool.hoverAnimation.stop();
			}
		}
	}

	Animation createToolAnimation(Item iconItem) {
		SequenceAnimation sequence = new SequenceAnimation();
		sequence.addAnimation(new ChangeFaceAnimation(2000, 1).setItem(iconItem));
		sequence.addAnimation(new ChangeFaceAnimation(2000, 0).setItem(iconItem));
		return sequence;
	}

	void hoverToolbar(Collection<Item> hoveredItems) {
		boolean hover = hoveredItems.contains(this.toolbarItem);
		if (this.hover!=hover && (this.hoverAnimation==null || this.hoverAnimation.isFinished()|| !activated)) {
			activated = false;
			this.hover = hover;
			if (hover) {
				maximizeToolbar();
			}
			else {
				minimizeToolbar();
			}
			this.toolbarItem.getBoard().launchAnimation(this.hoverAnimation);
		}
	}

	void maximizeToolbar() {
		if (this.hoverAnimation!=null && !(this.hoverAnimation.isFinished())) {
			this.hoverAnimation.cancel();
		}
		this.hoverAnimation =  new CancelableAnimation(new ScalingAnimation(1000, maximize).setItem(toolbarItem));
	}

	void minimizeToolbar() {
		if (this.hoverAnimation!=null && !(this.hoverAnimation.isFinished())) {
			this.hoverAnimation.cancel();
		}
		this.hoverAnimation =  new CancelableAnimation(new ScalingAnimation(1000, minimize).setItem(toolbarItem));
	}

	Board getBoard() {
		return this.toolbarItem.getBoard();
	}
}
