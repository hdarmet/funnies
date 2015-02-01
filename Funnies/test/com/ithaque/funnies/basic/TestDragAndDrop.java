package com.ithaque.funnies.basic;

import org.junit.Test;

import com.ithaque.funnies.client.platform.gwt.test.TestImage;
import com.ithaque.funnies.client.platform.gwt.test.TestPlatform;
import com.ithaque.funnies.client.platform.gwt.test.TestRegistry;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.MouseEvent.Button;
import com.ithaque.funnies.shared.basic.MultiLayered;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.animations.DropAnimation;
import com.ithaque.funnies.shared.basic.items.animations.FaceFadingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ScalingAnimation;
import com.ithaque.funnies.shared.basic.processors.AbstractDragProfile;
import com.ithaque.funnies.shared.basic.processors.AbstractTargetedDragProfile;
import com.ithaque.funnies.shared.basic.processors.DragProcessor;
import com.ithaque.funnies.shared.basic.processors.SimpleTargetedDragProfile;

public class TestDragAndDrop extends AbstractTestCase {
	TestPlatform platform;
	Layer layer;
	Board board;
	ImageItem dragged;
	ImageItem target;
	SimpleTargetedDragProfile dragProfile;
	
	public void setup() {
		super.setup();
		platform = new TestPlatform();
		board = new Board(platform);
		DragProcessor dragProcessor = new DragProcessor();
		dragProfile = new SimpleTargetedDragProfile(board);
		dragProcessor.addDragProfile(dragProfile);
		board.addProcessor(dragProcessor);
		board.start();
		layer = new Layer("layer", -500, -300, 500, 300);
		board.addItem(layer);
		dragged = new ImageItem("hexagon.png");
		layer.addItem(dragged);
		dragProfile.addDraggeable(dragged);
		platform.process(0);
		TestImage.getImage("hexagon.png").loaded(70, 70);
		platform.process(40);
		reset();
	}
	
	@Test
	public void testBasicDrag() {
		dragged.addEventType(Type.MOUSE_DOWN);
		platform.mouseDown(500, 250, Button.LEFT, false, false, false);
		expect("Platform[0].sendEvent(MOUSE_DOWN, 500, 250, LEFT, false, false, false)");
		expect("Canvas[2].getCoordinateSpaceWidth()");
		expect("Canvas[2].getCoordinateSpaceHeight()");
		expect("Canvas[2].getCoordinateSpaceWidth()");
		expect("Canvas[2].getCoordinateSpaceHeight()");
		nothingExpected();
		reset();
		platform.process(80);
		nothingExpected();
		platform.mouseDrag(510, 255, Button.LEFT, false, false, false);
		expect("Platform[0].sendEvent(MOUSE_DRAG, 510, 255, LEFT, false, false, false)");
		expect("Canvas[2].getCoordinateSpaceWidth()");
		expect("Canvas[2].getCoordinateSpaceHeight()");
		expect("Canvas[2].getCoordinateSpaceWidth()");
		expect("Canvas[2].getCoordinateSpaceHeight()");
		expect("Canvas[2].getCoordinateSpaceWidth()");
		expect("Canvas[2].getCoordinateSpaceHeight()");
		nothingExpected();
		platform.process(120);
		expect("Canvas[0].setVisible(true)");
		expect("Canvas[1].setVisible(false)");
		expect("Canvas[1].restoreContext2D()");
		expect("Canvas[1].saveContext2D()");
		expect("Context2D[1].beginPath()");
		expect("Canvas[1].getCoordinateSpaceWidth()");
		expect("Canvas[1].getCoordinateSpaceHeight()");
		expect("Context2D[1].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[2].setVisible(true)");
		expect("Canvas[3].setVisible(false)");
		expect("Canvas[3].restoreContext2D()");
		expect("Canvas[3].saveContext2D()");
		expect("Context2D[3].beginPath()");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 510.0, 255.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		expect("Canvas[2].setVisible(false)");
		expect("Canvas[3].setVisible(true)");
		expect("Canvas[0].setVisible(false)");
		expect("Canvas[1].setVisible(true)");
		nothingExpected();
	}
	
	@Test
	public void testThatItemNotDraggeableAreNotDragged() {
		platform.mouseDown(500, 250, Button.LEFT, false, false, false);
		expect("Platform[0].sendEvent(MOUSE_DOWN, 500, 250, LEFT, false, false, false)");
		nothingExpected();
		reset();
		platform.process(80);
		nothingExpected();
		platform.mouseDrag(510, 255, Button.LEFT, false, false, false);
		expect("Platform[0].sendEvent(MOUSE_DRAG, 510, 255, LEFT, false, false, false)");
		platform.process(120);
		nothingExpected();
	}

	@Test
	public void testThatIfMouseIsNotOnItemNotDragTakesPlace() {
		dragged.addEventType(Type.MOUSE_DOWN);
		platform.mouseDown(0, 0, Button.LEFT, false, false, false);
		expect("Platform[0].sendEvent(MOUSE_DOWN, 0, 0, LEFT, false, false, false)");
		expect("Canvas[2].getCoordinateSpaceWidth()");
		expect("Canvas[2].getCoordinateSpaceHeight()");
		nothingExpected();
		reset();
		platform.process(80);
		nothingExpected();
		platform.mouseDrag(10, 5, Button.LEFT, false, false, false);
		expect("Platform[0].sendEvent(MOUSE_DRAG, 10, 5, LEFT, false, false, false)");
		platform.process(120);
		nothingExpected();
	}
	
	@Test
	public void testStandardDrop() {
		target = new ImageItem("rhexagon.png");
		target.setLocation(100, 100);
		layer.addItem(target);
		platform.process(80);
		TestImage.getImage("rhexagon.png").loaded(70, 70);		
		dragProfile.addTarget(target);
		dragged.addEventType(Type.MOUSE_DOWN);
		platform.mouseDown(500, 250, Button.LEFT, false, false, false);
		platform.mouseDrag(550, 300, Button.LEFT, false, false, false);
		platform.process(120);
		reset();
		platform.mouseUp(600, 350, Button.LEFT, false, false, false);
		platform.process(160);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(rhexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
	}
	
	@Test
	public void testDropOutsideTarget() {
		target = new ImageItem("rhexagon.png");
		target.setLocation(100, 100);
		layer.addItem(target);
		platform.process(80);
		TestImage.getImage("rhexagon.png").loaded(70, 70);		
		dragProfile.addTarget(target);
		dragged.addEventType(Type.MOUSE_DOWN);
		platform.mouseDown(500, 250, Button.LEFT, false, false, false);
		platform.mouseDrag(450, 300, Button.LEFT, false, false, false);
		platform.process(120);
		reset();
		platform.mouseUp(300, 350, Button.LEFT, false, false, false);
		platform.process(160);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 500.0, 250.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(rhexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
	}
	
	@Test
	public void testDragAnimationsForASuccessfulOperation() {
		dragProfile.setBeginDragAnimation(new ScalingAnimation.Builder(500, 1.2f).setItem(AbstractDragProfile.draggedItem()));
		dragProfile.setDraggedDropAnimation(new ScalingAnimation.Builder(500, 1.0f).setItem(AbstractDragProfile.draggedItem()));
		target = new ImageItem("rhexagon.png", "hhexagon.png");
		target.setOpacity(1, 0.0f);
		target.setLocation(100, 100);
		layer.addItem(target);
		dragProfile.addTarget(target);
		dragged.addEventType(Type.MOUSE_DOWN);
		platform.process(80);
		TestImage.getImage("rhexagon.png").loaded(70, 70);		
		TestImage.getImage("hhexagon.png").loaded(70, 70);		
		platform.process(120);
		reset();
		platform.mouseDown(500, 250, Button.LEFT, false, false, false);
		platform.process(160);
		// Drag begins : dragged item begins to grow
		find("Context2D[3].setTransform(1.0031416, 0.0, 0.0, 1.0031416, 500.0, 250.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		
		TestRegistry.reset();
		platform.mouseDrag(550, 300, Button.LEFT, false, false, false);
		platform.process(120+280);
		// Drag is on : dragged image continues to grow
		find("Context2D[2].setTransform(1.1187382, 0.0, 0.0, 1.1187382, 550.0, 300.0)");
		expect("Context2D[2].setGlobalAlpha(1.0)");
		expect("Context2D[2].translate(-35.0, -35.0)");
		expect("Context2D[2].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		
		// Drag is on (2) : animation continues even if mouse position haven't changed
		reset();
		platform.mouseDrag(550, 300, Button.LEFT, false, false, false);
		platform.process(120+520);
		find("Context2D[3].setTransform(1.2, 0.0, 0.0, 1.2, 550.0, 300.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		
		// Animation is finished : nothing happened
		reset();
		platform.mouseDrag(550, 300, Button.LEFT, false, false, false);
		platform.process(120+560);
		dontFind("drawImage");

		// Drop on target !
		reset();
		platform.mouseUp(620, 320, Button.LEFT, false, false, false);
		platform.process(120+600);
		// Dragged item begin to revert to normal size
		find("Context2D[2].setTransform(1.1968584, 0.0, 0.0, 1.1968584, 600.0, 350.0)");
		expect("Context2D[2].setGlobalAlpha(1.0)");
		expect("Context2D[2].translate(-35.0, -35.0)");
		expect("Context2D[2].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");

		// Dragged item continue to shrink (without any action from the user)
		reset();
		platform.process(120+560+240);
		find("Context2D[3].setTransform(1.1062791, 0.0, 0.0, 1.1062791, 600.0, 350.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
	}

	@Test
	public void testTargetActivationDuringDragOperation() {
		dragProfile.setShowAllowedTargetAnimation(new FaceFadingAnimation.Builder(500, 1, 0.2f).setItem(AbstractTargetedDragProfile.possibleDropTarget()));
		dragProfile.setHideAllowedTargetAnimation(new FaceFadingAnimation.Builder(500, 1, 0.0f).setItem(AbstractTargetedDragProfile.possibleDropTarget()));
		target = new ImageItem("rhexagon.png", "hhexagon.png");
		target.setOpacity(1, 0.0f);
		target.setLocation(100, 100);
		layer.addItem(target);
		dragProfile.addTarget(target);
		dragged.addEventType(Type.MOUSE_DOWN);
		platform.process(80);
		TestImage.getImage("rhexagon.png").loaded(70, 70);		
		TestImage.getImage("hhexagon.png").loaded(70, 70);		
		platform.process(120);
		TestRegistry.reset();
		platform.mouseDown(500, 250, Button.LEFT, false, false, false);
		platform.process(160);
		// base target image is shown because on same layer
		find("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(rhexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		
		// highlighted item begins to appear
		find("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		expect("Context2D[3].setGlobalAlpha(0.0031416838)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hhexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		reset();
		platform.mouseDrag(550, 300, Button.LEFT, false, false, false);
		platform.process(120+280);
		
		// Nothing interesting about target base image, just skip
		find("Context2D[2].setTransform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		// highlight image continues to appear
		find("Context2D[2].setTransform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		expect("Context2D[2].setGlobalAlpha(0.11873813)");
		expect("Context2D[2].translate(-35.0, -35.0)");
		expect("Context2D[2].drawImage(hhexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
	
		// Animation is over
		reset();
		platform.mouseDrag(580, 330, Button.LEFT, false, false, false);
		platform.process(120+520);
		find("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		find("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		expect("Context2D[3].setGlobalAlpha(0.2)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hhexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");	
	}
	
	@Test
	public void testDraggedAnimationDuringSuccessfulDrop() {
		dragProfile.setAdjustLocationAnimation(new DropAnimation.Builder(500).setItem(AbstractDragProfile.draggedItem()).setLocation(AbstractDragProfile.dropLocation()));
		target = new ImageItem("rhexagon.png");
		target.setLocation(100, 100);
		layer.addItem(target);
		dragProfile.addTarget(target);
		dragged.addEventType(Type.MOUSE_DOWN);
		platform.process(80);
		TestImage.getImage("rhexagon.png").loaded(70, 70);	
		platform.process(120);
		// Drag
		platform.mouseDown(500, 250, Button.LEFT, false, false, false);
		platform.process(160);
		// Drop (not exactly on the center of target)
		platform.mouseUp(580, 330, Button.LEFT, false, false, false);
		// Dragged item begin to move to target center by itself
		reset();
		platform.process(160+40);
		find("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 508.0, 258.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");

		// Dragged item continues to move to target center by itself
		reset();
		platform.process(160+160);
		find("Context2D[2].setTransform(1.0, 0.0, 0.0, 1.0, 532.0, 282.0)");
		expect("Context2D[2].setGlobalAlpha(1.0)");
		expect("Context2D[2].translate(-35.0, -35.0)");
		expect("Context2D[2].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		
		// Animation is over : dragged item is on target center
		reset();
		platform.process(160+480);
		find("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		
		// Animation is finished : nothing happen
		reset();
		platform.process(160+560);
		dontFind("drawImage");
	}

	@Test
	public void testDraggedAnimationForFailedDrop() {
		dragProfile.setAdjustLocationAnimation(new DropAnimation.Builder(500).setItem(AbstractDragProfile.draggedItem()).setLocation(AbstractDragProfile.dropLocation()));
		target = new ImageItem("rhexagon.png");
		target.setLocation(100, 100);
		layer.addItem(target);
		dragProfile.addTarget(target);
		dragged.addEventType(Type.MOUSE_DOWN);
		platform.process(80);
		TestImage.getImage("rhexagon.png").loaded(70, 70);	
		platform.process(120);
		// Drag
		platform.mouseDown(500, 250, Button.LEFT, false, false, false);
		platform.process(160);
		platform.mouseDrag(510, 330, Button.LEFT, false, false, false);
		platform.process(160+40);
		// Drop outside target
		platform.mouseUp(510, 330, Button.LEFT, false, false, false);
		// Dragged item begins to return to its original place
		reset();
		platform.process(160+80);
		find("Context2D[2].setTransform(1.0, 0.0, 0.0, 1.0, 509.2, 323.6)");
		expect("Context2D[2].setGlobalAlpha(1.0)");
		expect("Context2D[2].translate(-35.0, -35.0)");
		expect("Context2D[2].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		// Time has passed (almost half a second :) ) 
		reset();
		platform.process(160+480);
		find("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 501.2, 259.6)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		// Animation is finished
		reset();
		platform.process(160+520);
		find("Context2D[2].setTransform(1.0, 0.0, 0.0, 1.0, 500.0, 250.0)");
		expect("Context2D[2].setGlobalAlpha(1.0)");
		expect("Context2D[2].translate(-35.0, -35.0)");
		expect("Context2D[2].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		// If we continue to wait... nothing happens
		reset();
		platform.process(160+560);
		dontFind("drawImage");
	}
	
	@Test
	public void testTargetAnimationWhenDraggedObjectHovesOnIt() {
		dragProfile.setEnterTargetAnimation(new FaceFadingAnimation.Builder(500, 1, 1.0f).setItem(AbstractTargetedDragProfile.newDropTarget()));
		dragProfile.setExitTargetAnimation(new FaceFadingAnimation.Builder(500, 1, 0.0f).setItem(AbstractTargetedDragProfile.previousDropTarget()));
		target = new ImageItem("hhexagon.png", "rhexagon.png");
		target.setLocation(100, 100);
		target.setOpacity(1, 0.0f);
		layer.addItem(target);
		dragProfile.addTarget(target);
		dragged.addEventType(Type.MOUSE_DOWN);
		platform.process(80);
		TestImage.getImage("rhexagon.png").loaded(70, 70);	
		TestImage.getImage("hhexagon.png").loaded(70, 70);	
		platform.process(120);
		// Drag
		platform.mouseDown(500, 250, Button.LEFT, false, false, false);
		platform.process(160);
		// Hover on target
		reset();
		platform.mouseDrag(580, 320, Button.LEFT, false, false, false);
		platform.process(200);
		// Skip base image
		find("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		// Highlight image begins to appear...
		find("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		expect("Context2D[3].setGlobalAlpha(0.015708419)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(rhexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		// Wait half a second
		reset();
		platform.process(200+520);
		// Skip base image
		find("Context2D[2].setTransform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		// Highlight image is now opaque
		find("Context2D[2].setTransform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		expect("Context2D[2].setGlobalAlpha(1.0)");
		expect("Context2D[2].translate(-35.0, -35.0)");
		expect("Context2D[2].drawImage(rhexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		// Hover out of target
		reset();
		platform.mouseDrag(550, 300, Button.LEFT, false, false, false);
		platform.process(200+520+40);
		// Skip base image
		find("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		// Highlight image begins to deseapear
		find("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		expect("Context2D[3].setGlobalAlpha(0.98429155)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(rhexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		// Wait hald a second, 
		reset();
		platform.process(200+520+520);
		dontFind("drawImage(rhexagon.png,");
	}
	
	@Test
	public void testDragOnDragLayerWithoutAnimation_FailedDropCase() {
		layer.removeItem(dragged);
		board.removeItem(layer);
		MultiLayered layered = new MultiLayered(-500.0f, -300.0f, 500.0f, 300.0f);
		board.addItem(layered);
		layer = layered.createAttachedLayer("standard");
		Layer dragLayer = layered.createAttachedLayer("drag");
		layer.addItem(dragged);
		dragProfile.setDragLayer(dragLayer);
		dragged.addEventType(Type.MOUSE_DOWN);
		platform.process(80);
		// Drag
		reset();
		platform.mouseDown(500, 250, Button.LEFT, false, false, false);
		platform.process(120);
		// 6th context id drag layer's
		find("Context2D[6].setTransform(1.0, 0.0, 0.0, 1.0, 500.0, 250.0)");
		expect("Context2D[6].setGlobalAlpha(1.0)");
		expect("Context2D[6].translate(-35.0, -35.0)");
		expect("Context2D[6].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		reset();
		platform.mouseUp(500, 250, Button.LEFT, false, false, false);
		// Drop (failed) return to standard layer
		platform.process(160);
		find("Context2D[5].setTransform(1.0, 0.0, 0.0, 1.0, 500.0, 250.0)");
		expect("Context2D[5].setGlobalAlpha(1.0)");
		expect("Context2D[5].translate(-35.0, -35.0)");
		expect("Context2D[5].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
	}

}
