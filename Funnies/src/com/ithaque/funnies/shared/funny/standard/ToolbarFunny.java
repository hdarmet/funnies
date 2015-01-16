package com.ithaque.funnies.shared.funny.standard;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.GroupItem;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.funny.AbstractFunny;
import com.ithaque.funnies.shared.funny.AbstractRing;
import com.ithaque.funnies.shared.funny.TooledRing;

public class ToolbarFunny extends AbstractFunny {

	public ToolbarFunny(String toolbarId) {
		super(toolbarId);
		toolbarItem = new GroupItem();
	}

	GroupItem toolbarItem;
	List<ToolRecord> tools = new ArrayList<ToolRecord>();
	
	public boolean addTool(Icon icon) {
		if (icon.getToolSupportId().equals(getId())) {
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
		
		ToolRecord(Icon tool, boolean activated) {
			this.tool = tool;
			this.activated = activated;
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

}
