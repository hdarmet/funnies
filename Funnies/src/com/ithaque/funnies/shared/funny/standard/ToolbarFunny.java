package com.ithaque.funnies.shared.funny.standard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.AnimationContext;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.GroupItem;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.items.animations.CancelableAnimation;
import com.ithaque.funnies.shared.basic.items.animations.FadingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.MoveAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ParallelAnimation;
import com.ithaque.funnies.shared.basic.items.animations.RepeatableAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ScalingAnimation;
import com.ithaque.funnies.shared.funny.AbstractFunny;
import com.ithaque.funnies.shared.funny.AbstractRing;
import com.ithaque.funnies.shared.funny.Funny;
import com.ithaque.funnies.shared.funny.FunnyObserver;
import com.ithaque.funnies.shared.funny.Icon;
import com.ithaque.funnies.shared.funny.TooledFunny;
import com.ithaque.funnies.shared.funny.TooledRing;

public class ToolbarFunny extends AbstractFunny implements FunnyObserver {

	static final float MIN_SCALE = 0.8f;
	static final float MAX_SCALE = 1.2f;
	static final long DURATION = 2000;
	
	public static final PositionManager LEFT_TOP = new LeftTopPositionManager();
	public static final PositionManager LEFT_BOTTOM = new LeftBottomPositionManager();
	public static final PositionManager LEFT_MIDDLE = new LeftMiddlePositionManager();

	public static final PositionManager RIGHT_TOP = new RightTopPositionManager();
	public static final PositionManager RIGHT_BOTTOM = new RightBottomPositionManager();
	public static final PositionManager RIGHT_MIDDLE = new RightMiddlePositionManager();

	public static final PositionManager TOP_LEFT = new TopLeftPositionManager();
	public static final PositionManager TOP_RIGHT = new TopRightPositionManager();
	public static final PositionManager TOP_MIDDLE = new TopMiddlePositionManager();

	public static final PositionManager BOTTOM_LEFT = new BottomLeftPositionManager();
	public static final PositionManager BOTTOM_RIGHT = new BottomRightPositionManager();
	public static final PositionManager BOTTOM_MIDDLE = new BottomMiddlePositionManager();
	
	float minimize = MIN_SCALE;
	float maximize = MAX_SCALE;
	boolean hover = false;
	CancelableAnimation hoverAnimation = null;
	boolean activated = false;
	GroupItem toolbarItem;
	List<ToolRecord> tools = new ArrayList<ToolRecord>();
	PositionManager positionManager;

	class ToolRecord {
		TooledFunny tool;
		Icon icon;
		RepeatableAnimation hoverAnimation = null;
		
		ToolRecord(TooledFunny tool, Icon icon) {
			this.tool = tool;
			this.icon = icon;
			this.hoverAnimation = null;
		}
	}

	public ToolbarFunny(String toolbarId, PositionManager positionManager) {
		super(toolbarId);
		toolbarItem = new GroupItem();
		toolbarItem.setScale(minimize);
		toolbarItem.addEventType(Type.MOUSE_MOVE);
		this.positionManager = positionManager;
	}

	public ToolbarFunny setMetrics(float minimize, float maximize) {
		this.minimize = minimize;
		this.maximize = maximize;
		return this;
	}
		
	public boolean addTool(TooledFunny tool, Icon icon) {
		if (icon.getToolSupportId().equals(getId())) {
			icon.setToolbar(this);
			if (tool.isEnabled()) {
				icon.getIconItem().addEventType(Type.MOUSE_CLICK);
			}
			icon.getIconItem().addEventType(Type.MOUSE_MOVE);
			tools.add(new ToolRecord(tool, icon));
			setLayout();
			tool.addObserver(this);
			toolbarItem.addItem(icon.getIconItem());
			return true;
		}
		return false;
	}
	
	public boolean removeTool(TooledFunny tool, Icon icon) {
		for (ToolRecord record : new ArrayList<ToolRecord>(tools)) {
			if (record.tool == tool && record.icon == icon) {
				icon.setToolbar(null);
				icon.getIconItem().removeEventType(Type.MOUSE_CLICK);
				icon.getIconItem().removeEventType(Type.MOUSE_MOVE);
				tools.remove(record);
				record.tool.removeObserver(this);
				toolbarItem.removeItem(record.icon.getIconItem());
				return true;
			}
		}
		return false;
	}
	
	public List<Icon> getTools() {
		List<Icon> icons = new ArrayList<Icon>();
		for (ToolRecord record : tools) {
			icons.add(record.icon);
		}
		return icons;
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
		setLocation(
			positionManager.getAnchorX(ring.getWidth(), ring.getHeight(), tools), 
			positionManager.getAnchorY(ring.getWidth(), ring.getHeight(), tools));
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
			if (toolRecord.tool.isEnabled()) {
				hoverTool(toolRecord, hoveredItems);
			}
		}
	}

	void hoverTool(ToolRecord toolRecord, Collection<Item> hoveredItems) {
		if (hoveredItems.contains(toolRecord.icon.getIconItem())) {
			if (toolRecord.hoverAnimation==null) {
				Animation animation = createToolAnimation(toolRecord);
				if (animation!=null) {
					toolRecord.hoverAnimation = new RepeatableAnimation(animation);
					getBoard().launchAnimation(toolRecord.hoverAnimation);
				}
			}
			else if (toolRecord.hoverAnimation.isFinished()) {
				toolRecord.hoverAnimation.reset();
				if (toolRecord.hoverAnimation!=null) {
					getBoard().launchAnimation(toolRecord.hoverAnimation);
				}
			}
		}
		else {
			if (toolRecord.hoverAnimation!=null && !toolRecord.hoverAnimation.isFinished()) {
				toolRecord.hoverAnimation.stop();
			}
		}
	}

	public void activateTool(TooledFunny funny, Icon icon) {
		if (!activated) {
			this.activated = true;
			minimizeToolbar();
			funny.activateTool(icon);
		}
	}

	Animation createToolAnimation(ToolRecord record) {
		Animation.Factory factory = record.icon.getHoverAnimation();
		if (factory!=null) {
			AnimationContext context = new Icon.IconAnimationContext(record.icon.getIconItem().getWrapped());
			Animation animation = factory.create();
			animation.setContext(context);
			return animation;
		}
		return null;
	}

	void hoverToolbar(Collection<Item> hoveredItems) {
		boolean hover = hoveredItems.contains(this.toolbarItem);
		if (this.hover!=hover && (this.hoverAnimation==null || this.hoverAnimation.isFinished()) && !activated) {
			this.hover = hover;
			if (hover) {
				maximizeToolbar();
			}
			else {
				minimizeToolbar();
			}
		}
	}

	void maximizeToolbar() {
		if (this.hoverAnimation!=null && !(this.hoverAnimation.isFinished())) {
			this.hoverAnimation.cancel();
		}
		this.hoverAnimation =  new CancelableAnimation(new ScalingAnimation(1000, maximize).setItem(toolbarItem));
		getBoard().launchAnimation(this.hoverAnimation);
	}

	void minimizeToolbar() {
		if (this.hoverAnimation!=null && !(this.hoverAnimation.isFinished())) {
			this.hoverAnimation.cancel();
		}
		this.hoverAnimation =  new CancelableAnimation(new ScalingAnimation(1000, minimize).setItem(toolbarItem)) {
			public void finish(long time) {
				super.finish(time);
				activated = false;
				hover = false;
			}
		};
		getBoard().launchAnimation(this.hoverAnimation);
	}

	Board getBoard() {
		return this.toolbarItem.getBoard();
	}

	@Override
	public void change(ChangeType type, Funny funny) {
		if (type==ChangeType.ENABLING) {
			for (ToolRecord record : tools) {
				if (record.tool==funny) {
					Item item = record.icon.getIconItem();
					if (record.tool.isEnabled()) {
						item.addEventType(Type.MOUSE_CLICK);
					}
					else {
						item.removeEventType(Type.MOUSE_CLICK);
					}
				}
			}
			adjustLayout();
		}
	}

	void setLayout() {
		for (ToolRecord record : tools) {
			Item item = record.icon.getIconItem();
			if (record.tool.isEnabled()) {
				Location location = new Location(positionManager.getX(record.icon, tools), positionManager.getY(record.icon, tools));
				item.setLocation(location);
			}
		}
	}
	
	void adjustLayout() {
		ParallelAnimation animation = new ParallelAnimation();
		for (ToolRecord record : tools) {
			Item item = record.icon.getIconItem();
			if (record.tool.isEnabled()) {
				Location newLocation = new Location(positionManager.getX(record.icon, tools), positionManager.getY(record.icon, tools));
				Location iconLocation = item.getLocation();
				if (!iconLocation.equals(newLocation)) {
					if (item.getOpacity()==0.0f) {
						item.setLocation(newLocation);
					}
					else {
						animation.addAnimation(new MoveAnimation(DURATION).setLocation(newLocation).setItem(item));
					}
				}
				if (item.getOpacity()<1.0f) {
					animation.addAnimation(new FadingAnimation(DURATION, 1.0f).setItem(item));
				}
			}
			else {
				if (item.getOpacity()>0.0f) {
					animation.addAnimation(new FadingAnimation(DURATION, 0.0f).setItem(item));
				}
			}
		}
		getBoard().launchAnimation(animation);
	}
	
	public interface PositionManager {
		float getX(Icon icon, List<ToolRecord> tools);
		float getAnchorX(float width, float height, List<ToolRecord> tools);
		float getY(Icon icon, List<ToolRecord> tools);
		float getAnchorY(float width, float height, List<ToolRecord> tools);
		float getHeight(List<ToolRecord> tools);
		float getWidth(List<ToolRecord> tools);
	}

	public static class LeftTopPositionManager implements PositionManager  {
		
		@Override
		public float getX(Icon icon, List<ToolRecord> tools) {
			return -getWidth(tools)/2.0f;
		}
		
		@Override
		public float getY(Icon icon, List<ToolRecord> tools) {
			float height = 0.0f;
			for (ToolRecord record : tools) {
				if (record.tool.isEnabled()) {
					if (record.icon==icon) {
						return height+icon.getHeight()/2.0f;
					}
					else {
						height+=icon.getHeight();
					}
				}
			}
			return height;
		}
		
		@Override
		public float getHeight(List<ToolRecord> tools) {
			float height = 0.0f;
			for (ToolRecord record : tools) {
				if (record.tool.isEnabled()) {
					height += record.icon.getHeight();
				}
			}
			return height;
		}

		@Override
		public float getWidth(List<ToolRecord> tools) {
			float width = 0.0f;
			for (ToolRecord record : tools) {
				if (record.tool.isEnabled() && record.icon.getWidth()>width) {
					width = record.icon.getWidth();
				}
			}
			return width;
		}

		@Override
		public float getAnchorX(float width, float height,
				List<ToolRecord> tools) {
			return width/2.0f;
		}		

		@Override
		public float getAnchorY(float width, float height,
				List<ToolRecord> tools) {
			return -height/2.0f;
		}
	}
	
	public static class LeftBottomPositionManager extends LeftTopPositionManager  {
		
		@Override
		public float getY(Icon icon, List<ToolRecord> tools) {
			return -getHeight(tools)+super.getY(icon, tools);
		}
		
		@Override
		public float getAnchorY(float width, float height, List<ToolRecord> tools) {
			return height/2.0f;
		}
	}

	public static class LeftMiddlePositionManager extends LeftTopPositionManager  {
		
		@Override
		public float getY(Icon icon, List<ToolRecord> tools) {
			return -getHeight(tools)/2.0f+super.getY(icon, tools);
		}
		
		@Override
		public float getAnchorY(float width, float height, List<ToolRecord> tools) {
			return 0.0f;
		}
	}
	
	public static class RightTopPositionManager extends LeftTopPositionManager  {
		
		@Override
		public float getX(Icon icon, List<ToolRecord> tools) {
			return getWidth(tools)/2.0f;
		}
		
		@Override
		public float getAnchorX(float width, float height, List<ToolRecord> tools) {
			return -width/2.0f;
		}
	}

	public static class RightBottomPositionManager extends LeftBottomPositionManager  {
		
		@Override
		public float getX(Icon icon, List<ToolRecord> tools) {
			return getWidth(tools)/2.0f;
		}
		
		@Override
		public float getAnchorX(float width, float height, List<ToolRecord> tools) {
			return -width/2.0f;
		}
	}

	public static class RightMiddlePositionManager extends LeftMiddlePositionManager  {
		
		@Override
		public float getX(Icon icon, List<ToolRecord> tools) {
			return getWidth(tools)/2.0f;
		}
		
		@Override
		public float getAnchorX(float width, float height, List<ToolRecord> tools) {
			return -width/2.0f;
		}
	}
	
	public static class TopLeftPositionManager implements PositionManager  {
		
		@Override
		public float getY(Icon icon, List<ToolRecord> tools) {
			return getHeight(tools)/2.0f;
		}
		
		@Override
		public float getX(Icon icon, List<ToolRecord> tools) {
			float width = 0.0f;
			for (ToolRecord record : tools) {
				if (record.tool.isEnabled()) {
					if (record.icon==icon) {
						return width+icon.getWidth()/2.0f;
					}
					else {
						width+=icon.getWidth();
					}
				}
			}
			return width;
		}
		
		@Override
		public float getWidth(List<ToolRecord> tools) {
			float width = 0.0f;
			for (ToolRecord record : tools) {
				if (record.tool.isEnabled()) {
					width += record.icon.getWidth();
				}
			}
			return width;
		}

		@Override
		public float getHeight(List<ToolRecord> tools) {
			float height = 0.0f;
			for (ToolRecord record : tools) {
				if (record.tool.isEnabled() && record.icon.getHeight()>height) {
					height = record.icon.getHeight();
				}
			}
			return height;
		}

		@Override
		public float getAnchorX(float width, float height,
				List<ToolRecord> tools) {
			return -width/2.0f;
		}		

		@Override
		public float getAnchorY(float width, float height,
				List<ToolRecord> tools) {
			return -height/2.0f;
		}
	}

	public static class TopRightPositionManager extends TopLeftPositionManager  {
		
		@Override
		public float getX(Icon icon, List<ToolRecord> tools) {
			return -getWidth(tools)+super.getX(icon, tools);
		}
		
		@Override
		public float getAnchorX(float width, float height, List<ToolRecord> tools) {
			return width/2.0f;
		}
	}

	public static class TopMiddlePositionManager extends TopLeftPositionManager  {
		
		@Override
		public float getX(Icon icon, List<ToolRecord> tools) {
			return -getWidth(tools)/2.0f+super.getX(icon, tools);
		}
		
		@Override
		public float getAnchorX(float width, float height, List<ToolRecord> tools) {
			return 0.0f;
		}
	}
	
	public static class BottomLeftPositionManager extends TopLeftPositionManager  {
		
		@Override
		public float getY(Icon icon, List<ToolRecord> tools) {
			return -getHeight(tools)/2.0f;
		}
		
		@Override
		public float getAnchorY(float width, float height, List<ToolRecord> tools) {
			return height/2.0f;
		}
	}

	public static class BottomRightPositionManager extends TopRightPositionManager  {
		
		@Override
		public float getY(Icon icon, List<ToolRecord> tools) {
			return -getHeight(tools)/2.0f;
		}
		
		@Override
		public float getAnchorY(float width, float height, List<ToolRecord> tools) {
			return height/2.0f;
		}
	}

	public static class BottomMiddlePositionManager extends TopMiddlePositionManager  {
		
		@Override
		public float getY(Icon icon, List<ToolRecord> tools) {
			return -getHeight(tools)/2.0f;
		}
		
		@Override
		public float getAnchorY(float width, float height, List<ToolRecord> tools) {
			return height/2.0f;
		}
	}
	
}
