package com.ithaque.funnies.shared.funny;



public interface TooledFunny extends Funny {

	Icon[] getIcons();

	void activateTool(Icon icon);

	boolean isEnabled();
	
	void setEnabled(boolean enabled);
		
}
