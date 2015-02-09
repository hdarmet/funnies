package com.ithaque.funnies.shared.funny.standard;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.funny.AbstractFunny;
import com.ithaque.funnies.shared.funny.FunnyObserver.ChangeType;
import com.ithaque.funnies.shared.funny.FunnySpy;
import com.ithaque.funnies.shared.funny.Icon;
import com.ithaque.funnies.shared.funny.TooledFunny;

public class CommandFunny extends AbstractFunny implements TooledFunny {

	public CommandFunny(String id) {
		super(id);
	}

	Icon toolIcon;
	boolean enabled = true;
	Animation.Factory hoverAnimation;
	
	public CommandFunny setToolIcon(Icon toolIcon) {
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
		System.out.println("Command activated :)");	
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
	public void addSpy(FunnySpy spy) {
		toolIcon.getIconItem().addObserver(spy);
	}

	@Override
	public void removeSpy(FunnySpy spy) {
		toolIcon.getIconItem().removeObserver(spy);
	}
}
