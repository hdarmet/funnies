package com.ithaque.funnies.shared.funny.standard;

import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.items.ImageItem;

public class Icon {
	Item iconItem;
	String toolSupportId;
	float width;
	float height;
	
	public Icon(String toolSupportId, float width, float height, Item iconItem) {
		this.iconItem = iconItem;
		this.toolSupportId = toolSupportId;
		this.width = width;
		this.height = height;
	}

	public Icon(String toolSupportId, float width, float height, String ... urls) {
		this(toolSupportId, width, height, new ImageItem(0, urls));
	}
	
	public Item getIconItem() {
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
}
