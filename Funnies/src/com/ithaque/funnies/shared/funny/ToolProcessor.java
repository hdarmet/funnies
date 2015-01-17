package com.ithaque.funnies.shared.funny;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.MouseEvent;
import com.ithaque.funnies.shared.basic.Processor;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.funny.standard.Icon;
import com.ithaque.funnies.shared.funny.standard.ToolbarFunny;

public class ToolProcessor implements Processor {

	TooledRing ring;
	List<ToolbarFunny> toolbars = new ArrayList<ToolbarFunny>();
	Map<Item, TooledFunnyRecord> tooledFunnies = new HashMap<Item, TooledFunnyRecord>();
	
	public ToolProcessor(TooledRing ring) {
		this.ring = ring;
	}
	
	public void registerToolbar(ToolbarFunny toolbar) {
		toolbars.add(toolbar);
		toolbar.setLocation(ring.getWidth()/2.0f, -ring.getHeight()/2.0f);
		toolbar.enterRing((AbstractRing)ring);
	}

	public void unregisterToolbar(ToolbarFunny toolbar) {
		toolbars.remove(toolbar);
		toolbar.exitRing((AbstractRing)ring);
	}
	
	@Override
	public boolean process(Event event, Board board) {
		if (event.getType()==Type.MOUSE_CLICK) {
			Item selected = board.getMouseTarget((MouseEvent)event);
			TooledFunnyRecord record = tooledFunnies.get(selected);
			if (record!=null) {
				record.funny.activateTool(record.icon);
				return true;
			}
		}
		else if (event.getType()==Type.MOUSE_MOVE) {
			Collection<Item> hoveredItems = 
				board.getMouseTargets((MouseEvent)event);
			for (ToolbarFunny toolbar : toolbars) {
				toolbar.hover(hoveredItems);
			}
		}
		return false;
	}

	public void registerToolFunny(TooledFunny funny) {
		for (Icon icon : ((TooledFunny)funny).getIcons()) {
			boolean placed = false;
			tooledFunnies.put(icon.getIconItem(), new TooledFunnyRecord(funny, icon, icon.getIconItem()));
			for (ToolbarFunny toolbar : toolbars) {
				if (toolbar.addTool(icon)) {
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
			tooledFunnies.remove(icon.getIconItem());
			for (ToolbarFunny toolbar : toolbars) {
				if (toolbar.removeTool(icon)) {
					found = true;
					break;
				}
			}
			if (!found) {
				throw new IllegalInvokeException();
			}
		}
	}

	class TooledFunnyRecord {
		TooledFunnyRecord(TooledFunny funny, Icon icon, Item item) {
			this.item = item;
			this.icon = icon;
			this.funny = funny;
		}
		
		Item item;
		Icon icon;
		TooledFunny funny;
	}
}
