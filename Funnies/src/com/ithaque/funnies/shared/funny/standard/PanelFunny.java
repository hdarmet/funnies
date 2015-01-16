package com.ithaque.funnies.shared.funny.standard;

import com.ithaque.funnies.shared.funny.AbstractFunny;
import com.ithaque.funnies.shared.funny.TooledFunny;

public class PanelFunny extends AbstractFunny implements TooledFunny {

	public PanelFunny(String id) {
		super(id);
	}

	Icon toolIcon;
	
	public PanelFunny setToolIcon(Icon toolIcon) {
		this.toolIcon = toolIcon;
		return this;
	}

	@Override
	public Icon[] getIcons() {
		return new Icon[] {toolIcon};
	}

	@Override
	public void activateTool(Icon icon) {
		System.out.println("Tool activated :)");	
	}
	
}
