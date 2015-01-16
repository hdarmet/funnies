package com.ithaque.funnies.shared.funny.standard;

import com.ithaque.funnies.shared.funny.AbstractFunny;
import com.ithaque.funnies.shared.funny.TooledFunny;

public class CommandFunny extends AbstractFunny implements TooledFunny {

	public CommandFunny(String id) {
		super(id);
	}

	Icon toolIcon;
	
	public CommandFunny setToolIcon(Icon toolIcon) {
		this.toolIcon = toolIcon;
		return this;
	}

	@Override
	public Icon[] getIcons() {
		return new Icon[] {toolIcon};
	}

	@Override
	public void activateTool(Icon icon) {
		System.out.println("Command activated :)");	
	}
	
}
