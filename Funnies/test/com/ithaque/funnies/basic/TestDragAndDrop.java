package com.ithaque.funnies.basic;

import org.junit.Test;

import com.ithaque.funnies.client.platform.gwt.test.TestImage;
import com.ithaque.funnies.client.platform.gwt.test.TestPlatform;
import com.ithaque.funnies.client.platform.gwt.test.TestRegistry;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.MouseEvent.Button;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.processors.DragProcessor;
import com.ithaque.funnies.shared.basic.processors.SimpleTargetedDragProfile;

public class TestDragAndDrop extends AbstractTestCase {
	TestPlatform platform;
	Layer layer;
	ImageItem dragged;
	ImageItem target;
	SimpleTargetedDragProfile dragProfile;
	
	public void setup() {
		super.setup();
		platform = new TestPlatform();
		Board board = new Board(platform);
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
		TestRegistry.reset();
	}
	
	@Test
	public void testBasicDrag() {
		dragged.addEventType(Type.MOUSE_DOWN);
		platform.mouseDown(500, 250, Button.LEFT, false, false, false);
		expect("0:Platform[0].sendEvent(MOUSE_DOWN, 500, 250, LEFT, false, false, false)");
		expect("1:Canvas[2].getCoordinateSpaceWidth()");
		expect("2:Canvas[2].getCoordinateSpaceHeight()");
		expect("3:Canvas[2].getCoordinateSpaceWidth()");
		expect("4:Canvas[2].getCoordinateSpaceHeight()");
		nothingExpected();
		TestRegistry.reset();
		platform.process(80);
		nothingExpected();
		platform.mouseDrag(510, 255, Button.LEFT, false, false, false);
		expect("0:Platform[0].sendEvent(MOUSE_DRAG, 510, 255, LEFT, false, false, false)");
		expect("1:Canvas[2].getCoordinateSpaceWidth()");
		expect("2:Canvas[2].getCoordinateSpaceHeight()");
		expect("3:Canvas[2].getCoordinateSpaceWidth()");
		expect("4:Canvas[2].getCoordinateSpaceHeight()");
		expect("5:Canvas[2].getCoordinateSpaceWidth()");
		expect("6:Canvas[2].getCoordinateSpaceHeight()");
		nothingExpected();
		platform.process(120);
		expect("7:Canvas[0].setVisible(true)");
		expect("8:Canvas[1].setVisible(false)");
		expect("9:Canvas[1].restoreContext2D()");
		expect("10:Canvas[1].saveContext2D()");
		expect("11:Context2D[1].beginPath()");
		expect("12:Canvas[1].getCoordinateSpaceWidth()");
		expect("13:Canvas[1].getCoordinateSpaceHeight()");
		expect("14:Context2D[1].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("15:Canvas[2].setVisible(true)");
		expect("16:Canvas[3].setVisible(false)");
		expect("17:Canvas[3].restoreContext2D()");
		expect("18:Canvas[3].saveContext2D()");
		expect("19:Context2D[3].beginPath()");
		expect("20:Canvas[3].getCoordinateSpaceWidth()");
		expect("21:Canvas[3].getCoordinateSpaceHeight()");
		expect("22:Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("23:Canvas[3].getCoordinateSpaceWidth()");
		expect("24:Canvas[3].getCoordinateSpaceHeight()");
		expect("25:Context2D[3].setTrandform(1.0, 0.0, 0.0, 1.0, 510.0, 255.0)");
		expect("26:Context2D[3].setGlobalAlpha(1.0)");
		expect("27:Context2D[3].translate(-35.0, -35.0)");
		expect("28:Context2D[3].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");
		expect("29:Canvas[2].setVisible(false)");
		expect("30:Canvas[3].setVisible(true)");
		expect("31:Canvas[0].setVisible(false)");
		expect("32:Canvas[1].setVisible(true)");
		nothingExpected();
	}
	
	@Test
	public void testThatItemNotDraggeableAreNotDragged() {
		platform.mouseDown(500, 250, Button.LEFT, false, false, false);
		expect("0:Platform[0].sendEvent(MOUSE_DOWN, 500, 250, LEFT, false, false, false)");
		nothingExpected();
		TestRegistry.reset();
		platform.process(80);
		nothingExpected();
		platform.mouseDrag(510, 255, Button.LEFT, false, false, false);
		expect("0:Platform[0].sendEvent(MOUSE_DRAG, 510, 255, LEFT, false, false, false)");
		platform.process(120);
		nothingExpected();
	}

	@Test
	public void testThatIfMouseIsNotOnItemNotDragTakesPlace() {
		dragged.addEventType(Type.MOUSE_DOWN);
		platform.mouseDown(0, 0, Button.LEFT, false, false, false);
		expect("0:Platform[0].sendEvent(MOUSE_DOWN, 0, 0, LEFT, false, false, false)");
		expect("1:Canvas[2].getCoordinateSpaceWidth()");
		expect("2:Canvas[2].getCoordinateSpaceHeight()");
		nothingExpected();
		TestRegistry.reset();
		platform.process(80);
		nothingExpected();
		platform.mouseDrag(10, 5, Button.LEFT, false, false, false);
		expect("0:Platform[0].sendEvent(MOUSE_DRAG, 10, 5, LEFT, false, false, false)");
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
		TestRegistry.reset();
		platform.mouseUp(600, 350, Button.LEFT, false, false, false);
		platform.process(160);
		find("20:Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("21:Canvas[3].getCoordinateSpaceWidth()");
		expect("22:Canvas[3].getCoordinateSpaceHeight()");
		expect("23:Context2D[3].setTrandform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		expect("24:Context2D[3].setGlobalAlpha(1.0)");
		expect("25:Context2D[3].translate(-35.0, -35.0)");
		expect("26:Context2D[3].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");
		expect("27:Canvas[3].getCoordinateSpaceWidth()");
		expect("28:Canvas[3].getCoordinateSpaceHeight()");
		expect("29:Context2D[3].setTrandform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		expect("30:Context2D[3].setGlobalAlpha(1.0)");
		expect("31:Context2D[3].translate(-35.0, -35.0)");
		expect("32:Context2D[3].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");
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
		TestRegistry.reset();
		platform.mouseUp(300, 350, Button.LEFT, false, false, false);
		platform.process(160);
		find("19:Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("20:Canvas[3].getCoordinateSpaceWidth()");
		expect("21:Canvas[3].getCoordinateSpaceHeight()");
		expect("22:Context2D[3].setTrandform(1.0, 0.0, 0.0, 1.0, 500.0, 250.0)");
		expect("23:Context2D[3].setGlobalAlpha(1.0)");
		expect("24:Context2D[3].translate(-35.0, -35.0)");
		expect("25:Context2D[3].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");
		expect("26:Canvas[3].getCoordinateSpaceWidth()");
		expect("27:Canvas[3].getCoordinateSpaceHeight()");
		expect("28:Context2D[3].setTrandform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		expect("29:Context2D[3].setGlobalAlpha(1.0)");
		expect("30:Context2D[3].translate(-35.0, -35.0)");
		expect("31:Context2D[3].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");	}
}
