package com.ithaque.funnies.shared.funny.standard;

import java.util.ArrayList;
import java.util.List;

import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.Location;
import com.ithaque.funnies.shared.Shape;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Animation.Factory;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Color;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.GroupItem;
import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.items.PolygonItem;
import com.ithaque.funnies.shared.basic.items.animations.CancelableAnimation;
import com.ithaque.funnies.shared.basic.items.animations.FadingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.MoveAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ParallelAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ScalingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.SoftenAnimation;
import com.ithaque.funnies.shared.basic.items.animations.easing.SineInOutEasing;
import com.ithaque.funnies.shared.funny.AbstractFunny;
import com.ithaque.funnies.shared.funny.AbstractRing;
import com.ithaque.funnies.shared.funny.Funny;
import com.ithaque.funnies.shared.funny.FunnyObserver;
import com.ithaque.funnies.shared.funny.FunnySpy;
import com.ithaque.funnies.shared.funny.HoverFunny;
import com.ithaque.funnies.shared.funny.Icon;
import com.ithaque.funnies.shared.funny.Icon.IconItem;
import com.ithaque.funnies.shared.funny.TooledFunny;
import com.ithaque.funnies.shared.funny.TooledRing;

public class ToolbarFunny extends AbstractFunny implements HoverFunny, FunnyObserver {

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
	
	public static final BackgroundManager GLASS_BACKGROUND = new GlassBackgroundManager();
	public static final BackgroundManager RIBBON_BACKGROUND = new RibbonBackgroundManager();
	
	float minimize = MIN_SCALE;
	float maximize = MAX_SCALE;
	CancelableAnimation hoverAnimation = null;
	boolean activated = false;
	PolygonItem background;
	GroupItem toolbarItem;
	List<ToolRecord> tools = new ArrayList<ToolRecord>();
	PositionManager positionManager;
	BackgroundManager backgroundManager;
	float currentFactor = 0.0f;
	
	class ToolRecord {
		TooledFunny tool;
		Icon icon;
		
		ToolRecord(TooledFunny tool, Icon icon) {
			this.tool = tool;
			this.icon = icon;
		}
	}

	public ToolbarFunny(String toolbarId, PositionManager positionManager, BackgroundManager backgroundManager) {
		super(toolbarId);
		this.toolbarItem = new GroupItem();
		this.toolbarItem.setScale(minimize);
		this.background = new PolygonItem(Color.BLACK, Color.BLACK, 1, 0.0f, Shape.EMPTY_SHAPE);
		this.positionManager = positionManager;
		this.backgroundManager = backgroundManager;
		if (this.backgroundManager != null) {
			this.backgroundManager.initLayout(this);
		}
	}

	public ToolbarFunny setMetrics(float minimize, float maximize) {
		this.minimize = minimize;
		this.maximize = maximize;
		return this;
	}
		
	Location getBackgroundLocation(float factor) {
		if (backgroundManager!=null) {
			return backgroundManager.getBackgroundLocation(this, factor);
		}
		else {
			return null;
		}
	}

	Shape getBackgroundShape(float factor) {
		if (backgroundManager!=null) {
			return backgroundManager.getBackgroundShape(this, factor);
		}
		else {
			return null;
		}
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
			fire(ChangeType.REGISTERING);
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
				fire(ChangeType.REGISTERING);
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
		super.enterRing(ring);
		setLocation(
			positionManager.getAnchorX(this), 
			positionManager.getAnchorY(this));
		((TooledRing)ring).getToolSupport().addItem(background);
		((TooledRing)ring).getToolSupport().addItem(toolbarItem);
		setBackgroundLayout(currentFactor, Item.getUpdateSerial());
	}

	private void setBackgroundLayout(float factor, long serial) {
		background.setLocation(getBackgroundLocation(factor));
		background.setShape(serial, getBackgroundShape(factor));
	}
	
	@Override
	public void exitRing(AbstractRing ring) {
		if (!(ring instanceof TooledRing)) {
			throw new IllegalInvokeException();
		}
		((TooledRing)ring).getToolSupport().removeItem(background);
		((TooledRing)ring).getToolSupport().removeItem(toolbarItem);
		super.exitRing(ring);
	}

	public void activateTool(TooledFunny funny, Icon icon) {
		if (!activated) {
			this.activated = true;
			minimizeToolbar();
			funny.activateTool(icon);
		}
	}

	@Override
	public Item[] getHoverables() {
		Item[] hoverables = new Item[tools.size()];
		int index=0;
		for (ToolRecord tool : tools) {
			hoverables[index++] = tool.icon.getIconItem();
		}
		return hoverables;
	}

	@Override
	public Factory getHoverAnimation(Item item) {
		if (item instanceof IconItem) {
			Icon icon = ((IconItem)item).getIcon();
			return icon.getHoverAnimation();
		}
		return null;
	}
	
	@Override
	public void moveOn(Item item) {
		maximizeToolbar();
	}
	
	@Override
	public void moveOut(Item item) {
		minimizeToolbar();
	}

	void maximizeToolbar() {
		if (this.hoverAnimation!=null && !(this.hoverAnimation.isFinished())) {
			this.hoverAnimation.cancel();
		}
		this.hoverAnimation =  new CancelableAnimation(getMaximizeAnimation());
		getBoard().launchAnimation(this.hoverAnimation);
	}

	void minimizeToolbar() {
		if (this.hoverAnimation!=null && !(this.hoverAnimation.isFinished())) {
			this.hoverAnimation.cancel();
		}
		this.hoverAnimation =  new CancelableAnimation(getMinimizeAnimation()) {
			public void finish(long time) {
				super.finish(time);
				activated = false;
			}
		};
		getBoard().launchAnimation(this.hoverAnimation);
	}

	private Animation getMaximizeAnimation() {
		ParallelAnimation animation = new ParallelAnimation();
		animation.addAnimation(new ScalingAnimation(1000, maximize).setItem(toolbarItem));
		if (backgroundManager!=null) {
			animation.addAnimation(backgroundManager.getMinimizeAnimation(this));
		}
		return animation;
	}

	private Animation getMinimizeAnimation() {
		ParallelAnimation animation = new ParallelAnimation();
		animation.addAnimation(new ScalingAnimation(1000, minimize).setItem(toolbarItem));
		if (backgroundManager!=null) {
			animation.addAnimation(backgroundManager.getMaximizeAnimation(this));
		}
		return animation;
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
				Location location = new Location(positionManager.getX(record.icon, this), positionManager.getY(record.icon, this));
				item.setLocation(location);
			}
		}
		setBackgroundLayout(currentFactor, Item.getUpdateSerial());
	}
	
	void adjustLayout() {
		ParallelAnimation animation = new ParallelAnimation();
		for (ToolRecord record : tools) {
			Item item = record.icon.getIconItem();
			if (record.tool.isEnabled()) {
				Location newLocation = new Location(positionManager.getX(record.icon, this), positionManager.getY(record.icon, this));
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
		float getX(Icon icon, ToolbarFunny toolbar);
		float getAnchorX(ToolbarFunny toolbar);
		float getY(Icon icon, ToolbarFunny toolbar);
		float getAnchorY(ToolbarFunny toolbar);
		float getHeight(ToolbarFunny toolbar);
		float getWidth(ToolbarFunny toolbar);
		Orientation getOrientation();
	}

	public enum Orientation {
		LEFT,
		RIGHT,
		TOP,
		BOTTOM;
		
		boolean isVertical() {
			return this==Orientation.LEFT || this==Orientation.RIGHT;
		}

		boolean isHorizontal() {
			return this==Orientation.TOP || this==Orientation.BOTTOM;
		}
		
		boolean isBefore() {
			return this==Orientation.LEFT || this==Orientation.TOP;
		}

		boolean isAfter() {
			return this==Orientation.RIGHT || this==Orientation.BOTTOM;
		}
	}
	
	public static class LeftTopPositionManager implements PositionManager  {
		
		@Override
		public float getX(Icon icon, ToolbarFunny toolbar) {
			return getWidth(toolbar)/2.0f;
		}
		
		@Override
		public float getY(Icon icon, ToolbarFunny toolbar) {
			float height = 0.0f;
			for (ToolRecord record : toolbar.tools) {
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
		public float getHeight(ToolbarFunny toolbar) {
			float height = 0.0f;
			for (ToolRecord record : toolbar.tools) {
				if (record.tool.isEnabled()) {
					height += record.icon.getHeight();
				}
			}
			return height;
		}

		@Override
		public float getWidth(ToolbarFunny toolbar) {
			float width = 0.0f;
			for (ToolRecord record : toolbar.tools) {
				if (record.tool.isEnabled() && record.icon.getWidth()>width) {
					width = record.icon.getWidth();
				}
			}
			return width;
		}

		@Override
		public float getAnchorX(ToolbarFunny toolbar) {
			return -toolbar.getRing().getWidth()/2.0f;
		}		

		@Override
		public float getAnchorY(ToolbarFunny toolbar) {
			return -toolbar.getRing().getHeight()/2.0f;
		}
		
		@Override
		public Orientation getOrientation() {
			return Orientation.LEFT;
		}
	}
	
	public static class LeftBottomPositionManager extends LeftTopPositionManager  {
		
		@Override
		public float getY(Icon icon, ToolbarFunny toolbar) {
			return -getHeight(toolbar)+super.getY(icon, toolbar);
		}
		
		@Override
		public float getAnchorY(ToolbarFunny toolbar) {
			return toolbar.getRing().getHeight()/2.0f;
		}
	}

	public static class LeftMiddlePositionManager extends LeftTopPositionManager  {
		
		@Override
		public float getY(Icon icon, ToolbarFunny toolbar) {
			return -getHeight(toolbar)/2.0f+super.getY(icon, toolbar);
		}
		
		@Override
		public float getAnchorY(ToolbarFunny toolbar) {
			return 0.0f;
		}
	}
	
	public static class RightTopPositionManager extends LeftTopPositionManager  {
		
		@Override
		public float getX(Icon icon, ToolbarFunny toolbar) {
			return -getWidth(toolbar)/2.0f;
		}
		
		@Override
		public float getAnchorX(ToolbarFunny toolbar) {
			return toolbar.getRing().getWidth()/2.0f;
		}
		
		@Override
		public Orientation getOrientation() {
			return Orientation.RIGHT;
		}

	}

	public static class RightBottomPositionManager extends LeftBottomPositionManager  {
		
		@Override
		public float getX(Icon icon, ToolbarFunny toolbar) {
			return -getWidth(toolbar)/2.0f;
		}
		
		@Override
		public float getAnchorX(ToolbarFunny toolbar) {
			return toolbar.getRing().getWidth()/2.0f;
		}

		@Override
		public Orientation getOrientation() {
			return Orientation.RIGHT;
		}
	}

	public static class RightMiddlePositionManager extends LeftMiddlePositionManager  {
		
		@Override
		public float getX(Icon icon, ToolbarFunny toolbar) {
			return -getWidth(toolbar)/2.0f;
		}
		
		@Override
		public float getAnchorX(ToolbarFunny toolbar) {
			return toolbar.getRing().getWidth()/2.0f;
		}

		@Override
		public Orientation getOrientation() {
			return Orientation.RIGHT;
		}
	}
	
	public static class TopLeftPositionManager implements PositionManager  {
		
		@Override
		public float getY(Icon icon, ToolbarFunny toolbar) {
			return getHeight(toolbar)/2.0f;
		}
		
		@Override
		public float getX(Icon icon, ToolbarFunny toolbar) {
			float width = 0.0f;
			for (ToolRecord record : toolbar.tools) {
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
		public float getWidth(ToolbarFunny toolbar) {
			float width = 0.0f;
			for (ToolRecord record : toolbar.tools) {
				if (record.tool.isEnabled()) {
					width += record.icon.getWidth();
				}
			}
			return width;
		}

		@Override
		public float getHeight(ToolbarFunny toolbar) {
			float height = 0.0f;
			for (ToolRecord record : toolbar.tools) {
				if (record.tool.isEnabled() && record.icon.getHeight()>height) {
					height = record.icon.getHeight();
				}
			}
			return height;
		}

		@Override
		public float getAnchorX(ToolbarFunny toolbar) {
			return -toolbar.getRing().getWidth()/2.0f;
		}		

		@Override
		public float getAnchorY(ToolbarFunny toolbar) {
			return -toolbar.getRing().getHeight()/2.0f;
		}

		@Override
		public Orientation getOrientation() {
			return Orientation.TOP;
		}

	}

	public static class TopRightPositionManager extends TopLeftPositionManager  {
		
		@Override
		public float getX(Icon icon, ToolbarFunny toolbar) {
			return -getWidth(toolbar)+super.getX(icon, toolbar);
		}
		
		@Override
		public float getAnchorX(ToolbarFunny toolbar) {
			return toolbar.getRing().getWidth()/2.0f;
		}
	}

	public static class TopMiddlePositionManager extends TopLeftPositionManager  {
		
		@Override
		public float getX(Icon icon, ToolbarFunny toolbar) {
			return -getWidth(toolbar)/2.0f+super.getX(icon, toolbar);
		}
		
		@Override
		public float getAnchorX(ToolbarFunny toolbar) {
			return 0.0f;
		}
	}
	
	public static class BottomLeftPositionManager extends TopLeftPositionManager  {
		
		@Override
		public float getY(Icon icon, ToolbarFunny toolbar) {
			return -getHeight(toolbar)/2.0f;
		}
		
		@Override
		public float getAnchorY(ToolbarFunny toolbar) {
			return toolbar.getRing().getHeight()/2.0f;
		}
		
		@Override
		public Orientation getOrientation() {
			return Orientation.BOTTOM;
		}

	}

	public static class BottomRightPositionManager extends TopRightPositionManager  {
		
		@Override
		public float getY(Icon icon, ToolbarFunny toolbar) {
			return -getHeight(toolbar)/2.0f;
		}
		
		@Override
		public float getAnchorY(ToolbarFunny toolbar) {
			return toolbar.getRing().getHeight()/2.0f;
		}

		@Override
		public Orientation getOrientation() {
			return Orientation.BOTTOM;
		}
	}

	public static class BottomMiddlePositionManager extends TopMiddlePositionManager  {
		
		@Override
		public float getY(Icon icon, ToolbarFunny toolbar) {
			return -getHeight(toolbar)/2.0f;
		}
		
		@Override
		public float getAnchorY(ToolbarFunny toolbar) {
			return toolbar.getRing().getHeight()/2.0f;
		}

		@Override
		public Orientation getOrientation() {
			return Orientation.BOTTOM;
		}
	}
	
	static class BackgroundAnimation extends SoftenAnimation {

		float base;
		float target;
		ToolbarFunny toolbar;
		
		public BackgroundAnimation(long duration, ToolbarFunny toolbar, float target) {
			super(new SineInOutEasing(duration));
			base = toolbar.currentFactor;
			this.target = target;
			this.toolbar = toolbar;
		}

		@Override
		protected boolean executeAnimation(long time) {
			toolbar.currentFactor = getEasing().getValue(base, target);
			toolbar.setBackgroundLayout(toolbar.currentFactor, getUpdateSerial());
			return true;
		}
		
	}
	
	public interface BackgroundManager {
		
		void initLayout(ToolbarFunny toolbar);
	
		Shape getBackgroundShape(ToolbarFunny toolbar, float factor);

		Location getBackgroundLocation(ToolbarFunny toolbar, float factor);

		Animation getMinimizeAnimation(ToolbarFunny toolbar);
		Animation getMaximizeAnimation(ToolbarFunny toolbar);
	}
	
	public static class GlassBackgroundManager implements BackgroundManager {

		@Override
		public void initLayout(ToolbarFunny toolbar) {
			toolbar.background.setOpacity(0.0f);
		}

		@Override
		public Animation getMinimizeAnimation(ToolbarFunny toolbar) {
			return new FadingAnimation(1000, 0.8f).setItem(toolbar.background);
		}

		@Override
		public Animation getMaximizeAnimation(ToolbarFunny toolbar) {
			return new FadingAnimation(1000, 0.0f).setItem(toolbar.background);
		}

		@Override
		public Location getBackgroundLocation(ToolbarFunny toolbar, float factor) {
			return Location.ORIGIN;
		}

		@Override
		public Shape getBackgroundShape(ToolbarFunny toolbar, float factor) 
		{
			float width = toolbar.getRing().getWidth();
			float height = toolbar.getRing().getWidth();
			return new Shape(width, height);
		}

	}
	
	public static class RibbonBackgroundManager implements BackgroundManager {

		@Override
		public void initLayout(ToolbarFunny toolbar) {
			toolbar.background.setOpacity(0.8f);
		}

		@Override
		public Animation getMinimizeAnimation(ToolbarFunny toolbar) {
			return new BackgroundAnimation(1000, toolbar, 1.0f).setItem(toolbar.background);
		}

		@Override
		public Animation getMaximizeAnimation(ToolbarFunny toolbar) {
			return new BackgroundAnimation(1000, toolbar, 0.0f).setItem(toolbar.background);
		}


		@Override
		public Location getBackgroundLocation(ToolbarFunny toolbar, float factor) 
		{
			Orientation orientation = toolbar.positionManager.getOrientation();
			float x = orientation.isHorizontal() ? 0.0f : toolbar.getRing().getWidth()/2.0f - getRibbonThickness(toolbar, factor)/2.0f;
			float y = orientation.isVertical() ? 0.0f : toolbar.getRing().getHeight()/2.0f - getRibbonThickness(toolbar, factor)/2.0f;
			if (orientation.isBefore()) {
				return new Location(-x, -y);
			}
			else if (orientation.isAfter()) {
				return new Location(x, y);
			}
			return null;		
		}
		
		@Override
		public Shape getBackgroundShape(ToolbarFunny toolbar, float factor) 
		{
			Orientation orientation = toolbar.positionManager.getOrientation();
			float width = orientation.isHorizontal() ? toolbar.getRing().getWidth() : getRibbonThickness(toolbar, factor);
			float height = orientation.isHorizontal() ? getRibbonThickness(toolbar, factor) : toolbar.getRing().getHeight();
			Shape result = new Shape(width, height);
			return result;
		}
	
		float getRibbonThickness(ToolbarFunny toolbar, float factor) {
			if (toolbar.positionManager.getOrientation().isVertical()) {
				return (toolbar.minimize+(toolbar.maximize-toolbar.minimize)*factor)*toolbar.positionManager.getWidth(toolbar);
			}
			else if (toolbar.positionManager.getOrientation().isHorizontal()) {
				return (toolbar.minimize+(toolbar.maximize-toolbar.minimize)*factor)*toolbar.positionManager.getHeight(toolbar);
			}
			else {
				return 0.0f;
			}
		}

	}
	
	@Override
	public void addSpy(FunnySpy spy) {
		toolbarItem.addObserver(spy);
		background.addObserver(spy);
	}

	@Override
	public void removeSpy(FunnySpy spy) {
		toolbarItem.removeObserver(spy);
		background.removeObserver(spy);
	}
}
