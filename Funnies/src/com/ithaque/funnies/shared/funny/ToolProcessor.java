package com.ithaque.funnies.shared.funny;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.Processor;
import com.ithaque.funnies.shared.funny.Icon.IconItem;
import com.ithaque.funnies.shared.funny.standard.ToolbarFunny;

public class ToolProcessor implements Processor {

	TooledRing ring;
	List<ToolbarFunny> toolbars = new ArrayList<ToolbarFunny>();
	
	public ToolProcessor(TooledRing ring) {
		this.ring = ring;
	}
	
	public void registerToolbar(ToolbarFunny toolbar) {
		toolbars.add(toolbar);
		ring.enterRing(toolbar);
	}

	public void unregisterToolbar(ToolbarFunny toolbar) {
		toolbars.remove(toolbar);
		ring.exitRing(toolbar);
	}
	
	@Override
	public boolean process(Event event, Board board) {
		if (event.getType()==Type.MOUSE_CLICK) {
			Item selected = board.getMouseTarget((MouseEvent)event);
			if (selected!=null && selected instanceof IconItem) {
				Icon icon = ((IconItem)selected).getIcon();
				TooledFunny funny = icon.getOwner();
				ToolbarFunny toolbar = icon.getToolbar();
				toolbar.activateTool(funny, icon);
				return true;
			}
		}
		return false;
	}

	public void registerToolFunny(TooledFunny funny) {
		for (Icon icon : ((TooledFunny)funny).getIcons()) {
			boolean placed = false;
			for (ToolbarFunny toolbar : toolbars) {
				if (toolbar.addTool(funny, icon)) {
					placed = true;
					break;
				}
			}
			if (!placed) {
				throw new IllegalInvokeException();
			}
		}
	}

	public void unregisterToolFunny(TooledFunny funny) {
		for (Icon icon : ((TooledFunny)funny).getIcons()) {
			boolean found = false;
			for (ToolbarFunny toolbar : toolbars) {
				if (toolbar.removeTool(funny, icon)) {
					found = true;
					break;
				}
			}
			if (!found) {
				throw new IllegalInvokeException();
			}
		}
	}

}
