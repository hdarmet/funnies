package com.ithaque.funnies.shared.funny;

import com.ithaque.funnies.shared.funny.standard.Icon;

public interface TooledFunny extends Funny {

	Icon[] getIcons();

	void activateTool(Icon icon);

}
