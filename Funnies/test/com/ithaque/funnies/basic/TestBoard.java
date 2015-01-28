package com.ithaque.funnies.basic;

import org.junit.Test;

import com.ithaque.funnies.client.platform.gwt.test.TestImage;
import com.ithaque.funnies.client.platform.gwt.test.TestPlatform;
import com.ithaque.funnies.client.platform.gwt.test.TestRegistry;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.items.ImageItem;

public class TestBoard extends AbstractTestCase {

	@Test
	public void testLaunchBoard() {
		Board board = new Board(new TestPlatform());
		board.start();
		expect("0:Platform[0].startTimer()");
		expect("1:Platform[0].createCanvas()");
		expect("2:Canvas[0].setAttribute(position:absolute;left:0px;top:0px;)");
		expect("3:Canvas[0].setVisible(true)");
		expect("4:Canvas[0].setCoordinateSpaceWidth(1000)");
		expect("5:Canvas[0].setCoordinateSpaceHeight(500)");
		expect("6:Canvas[0].saveContext2D()");
		expect("7:Canvas[0].addToRootPanel()");
		expect("8:Platform[0].createCanvas()");
		expect("9:Canvas[1].setAttribute(position:absolute;left:0px;top:0px;)");
		expect("10:Canvas[1].setVisible(false)");
		expect("11:Canvas[1].setCoordinateSpaceWidth(1000)");
		expect("12:Canvas[1].setCoordinateSpaceHeight(500)");
		expect("13:Canvas[1].saveContext2D()");
		expect("14:Canvas[1].addToRootPanel()");
		nothingExpected();
	}
	
	@Test
	public void testDefineOneImageItem() {
		TestPlatform platform = new TestPlatform();
		Board board = new Board(platform);
		board.start();
		TestRegistry.reset();
		ImageItem iitem = new ImageItem("hexagon.png");
		board.addItem(iitem);
		platform.process(0);
		expect("0:Canvas[0].setVisible(true)");
		expect("1:Canvas[1].setVisible(false)");
		expect("2:Canvas[1].restoreContext2D()");
		expect("3:Canvas[1].saveContext2D()");
		expect("4:Context2D[1].beginPath()");
		expect("5:Canvas[1].getCoordinateSpaceWidth()");
		expect("6:Canvas[1].getCoordinateSpaceHeight()");
		expect("7:Context2D[1].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("8:Platform[0].createImage(hexagon.png)");
		expect("9:Image[hexagon.png].Image()");
		expect("10:Canvas[0].setVisible(false)");
		expect("11:Canvas[1].setVisible(true)");
		nothingExpected();
	}
	
	@Test
	public void testImageItemNotLoaded() {
		TestPlatform platform = new TestPlatform();
		Board board = new Board(platform);
		board.start();
		ImageItem iitem = new ImageItem("hexagon.png");
		board.addItem(iitem);
		platform.process(0);
		TestRegistry.reset();
		platform.process(40);
		nothingExpected();
	}
	
	@Test
	public void testLoadOneImageItem() {
		TestPlatform platform = new TestPlatform();
		Board board = new Board(platform);
		board.start();
		ImageItem iitem = new ImageItem("hexagon.png");
		board.addItem(iitem);
		platform.process(0);
		TestRegistry.reset();
		TestImage.getImage("hexagon.png").loaded(70, 70);
		platform.process(40);
		expect("0:Canvas[0].setVisible(false)");
		expect("1:Canvas[1].setVisible(true)");
		expect("2:Canvas[0].restoreContext2D()");
		expect("3:Canvas[0].saveContext2D()");
		expect("4:Context2D[0].beginPath()");
		expect("5:Canvas[0].getCoordinateSpaceWidth()");
		expect("6:Canvas[0].getCoordinateSpaceHeight()");
		expect("7:Context2D[0].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("8:Image[hexagon.png].getWidth()");
		expect("9:Image[hexagon.png].getHeight()");
		expect("10:Canvas[0].getCoordinateSpaceWidth()");
		expect("11:Canvas[0].getCoordinateSpaceHeight()");
		expect("12:Context2D[0].setTrandform(1.0, 0.0, 0.0, 1.0, 500.0, 250.0)");
		expect("13:Context2D[0].setGlobalAlpha(1.0)");
		expect("14:Context2D[0].translate(-35.0, -35.0)");
		expect("15:Context2D[0].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");
		expect("16:Canvas[0].setVisible(true)");
		expect("17:Canvas[1].setVisible(false)");
		nothingExpected();
	}

	@Test
	public void testImageItemRotationScalingAndLocationChange() {
		TestPlatform platform = new TestPlatform();
		Board board = new Board(platform);
		board.start();
		ImageItem iitem = new ImageItem("hexagon.png");
		board.addItem(iitem);
		platform.process(0);
		TestImage.getImage("hexagon.png").loaded(70, 70);
		platform.process(40);
		TestRegistry.reset();
		iitem.setLocation(20, 30);
		iitem.setRotation(1.0f);
		iitem.setScale(0.5f);
		platform.process(80);
		expect("0:Canvas[0].setVisible(true)");
		expect("1:Canvas[1].setVisible(false)");
		expect("2:Canvas[1].restoreContext2D()");
		expect("3:Canvas[1].saveContext2D()");
		expect("4:Context2D[1].beginPath()");
		expect("5:Canvas[1].getCoordinateSpaceWidth()");
		expect("6:Canvas[1].getCoordinateSpaceHeight()");
		expect("7:Context2D[1].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("8:Canvas[1].getCoordinateSpaceWidth()");
		expect("9:Canvas[1].getCoordinateSpaceHeight()");
		expect("10:Context2D[1].setTrandform(0.27015114, 0.42073548, -0.42073548, 0.27015114, 520.0, 280.0)");
		expect("11:Context2D[1].setGlobalAlpha(1.0)");
		expect("12:Context2D[1].translate(-35.0, -35.0)");
		expect("13:Context2D[1].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");
		expect("14:Canvas[0].setVisible(false)");
		expect("15:Canvas[1].setVisible(true)");
		nothingExpected();
	}
		
	@Test
	public void testLayerCreation() {
		TestPlatform platform = new TestPlatform();
		Board board = new Board(platform);
		board.start();
		platform.process(0);
		TestRegistry.reset();
		Layer layer = new Layer("layer", -500, -300, 500, 300);
		board.addItem(layer);
		platform.process(40);
		expect("0:Canvas[0].getCoordinateSpaceWidth()");
		expect("1:Canvas[0].getCoordinateSpaceHeight()");
		expect("2:Canvas[0].getCoordinateSpaceWidth()");
		expect("3:Canvas[0].getCoordinateSpaceHeight()");
		expect("4:Canvas[0].setVisible(true)");
		expect("5:Canvas[1].setVisible(false)");
		expect("6:Canvas[1].restoreContext2D()");
		expect("7:Canvas[1].saveContext2D()");
		expect("8:Context2D[1].beginPath()");
		expect("9:Canvas[1].getCoordinateSpaceWidth()");
		expect("10:Canvas[1].getCoordinateSpaceHeight()");
		expect("11:Context2D[1].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("12:Platform[0].createCanvas()");
		expect("13:Canvas[2].setAttribute(position:absolute;left:0px;top:0px;)");
		expect("14:Canvas[2].setVisible(true)");
		expect("15:Canvas[2].setCoordinateSpaceWidth(1000)");
		expect("16:Canvas[2].setCoordinateSpaceHeight(500)");
		expect("17:Canvas[2].saveContext2D()");
		expect("18:Canvas[2].addToRootPanel()");
		expect("19:Platform[0].createCanvas()");
		expect("20:Canvas[3].setAttribute(position:absolute;left:0px;top:0px;)");
		expect("21:Canvas[3].setVisible(false)");
		expect("22:Canvas[3].setCoordinateSpaceWidth(1000)");
		expect("23:Canvas[3].setCoordinateSpaceHeight(500)");
		expect("24:Canvas[3].saveContext2D()");
		expect("25:Canvas[3].addToRootPanel()");
		expect("26:Canvas[2].setVisible(true)");
		expect("27:Canvas[3].setVisible(false)");
		expect("28:Canvas[3].restoreContext2D()");
		expect("29:Canvas[3].saveContext2D()");
		expect("30:Context2D[3].beginPath()");
		expect("31:Canvas[3].getCoordinateSpaceWidth()");
		expect("32:Canvas[3].getCoordinateSpaceHeight()");
		expect("33:Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("34:Canvas[2].setVisible(false)");
		expect("35:Canvas[3].setVisible(true)");
		expect("36:Canvas[0].setVisible(false)");
		expect("37:Canvas[1].setVisible(true)");
		nothingExpected();
	}
	
	@Test
	public void testPutItemOnLayer() {
		TestPlatform platform = new TestPlatform();
		Board board = new Board(platform);
		board.start();
		Layer layer = new Layer("layer", -500, -300, 500, 300);
		board.addItem(layer);
		platform.process(0);
		TestRegistry.reset();
		ImageItem iitem = new ImageItem("hexagon.png");
		board.addItem(iitem);
		TestImage.getImage("hexagon.png").loaded(70, 70);
		platform.process(40);
		expect("0:Canvas[0].setVisible(false)");
		expect("1:Canvas[1].setVisible(true)");
		expect("2:Canvas[0].restoreContext2D()");
		expect("3:Canvas[0].saveContext2D()");
		expect("4:Context2D[0].beginPath()");
		expect("5:Canvas[0].getCoordinateSpaceWidth()");
		expect("6:Canvas[0].getCoordinateSpaceHeight()");
		expect("7:Context2D[0].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("8:Platform[0].createImage(hexagon.png)");
		expect("9:Image[hexagon.png].Image()");
		expect("10:Canvas[2].setVisible(false)");
		expect("11:Canvas[3].setVisible(true)");
		expect("12:Canvas[0].setVisible(true)");
		expect("13:Canvas[1].setVisible(false)");
		nothingExpected();
	}

	@Test
	public void testChangeLayerAttributes() {
		TestPlatform platform = new TestPlatform();
		Board board = new Board(platform);
		board.start();
		Layer layer = new Layer("layer", -500, -300, 500, 300);
		board.addItem(layer);
		platform.process(0);
		TestRegistry.reset();
		layer.setLocation(20, 30);
		layer.setRotation(1.0f);
		layer.setScale(0.5f);
		ImageItem iitem = new ImageItem("hexagon.png");
		layer.addItem(iitem);
		TestImage.getImage("hexagon.png").loaded(70, 70);
		platform.process(40);
		expect("0:Canvas[3].getCoordinateSpaceWidth()");
		expect("1:Canvas[3].getCoordinateSpaceHeight()");
		expect("2:Canvas[3].getCoordinateSpaceWidth()");
		expect("3:Canvas[3].getCoordinateSpaceHeight()");
		expect("4:Canvas[3].getCoordinateSpaceWidth()");
		expect("5:Canvas[3].getCoordinateSpaceHeight()");
		expect("6:Canvas[3].getCoordinateSpaceWidth()");
		expect("7:Canvas[3].getCoordinateSpaceHeight()");
		expect("8:Canvas[0].setVisible(false)");
		expect("9:Canvas[1].setVisible(true)");
		expect("10:Canvas[0].restoreContext2D()");
		expect("11:Canvas[0].saveContext2D()");
		expect("12:Context2D[0].beginPath()");
		expect("13:Canvas[0].getCoordinateSpaceWidth()");
		expect("14:Canvas[0].getCoordinateSpaceHeight()");
		expect("15:Context2D[0].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("16:Canvas[2].setVisible(false)");
		expect("17:Canvas[3].setVisible(true)");
		expect("18:Canvas[2].restoreContext2D()");
		expect("19:Canvas[2].saveContext2D()");
		expect("20:Context2D[2].beginPath()");
		expect("21:Canvas[2].getCoordinateSpaceWidth()");
		expect("22:Canvas[2].getCoordinateSpaceHeight()");
		expect("23:Context2D[2].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("24:Platform[0].createImage(hexagon.png)");
		expect("25:Image[hexagon.png].Image()");
		expect("26:Canvas[2].setVisible(true)");
		expect("27:Canvas[3].setVisible(false)");
		expect("28:Canvas[0].setVisible(true)");
		expect("29:Canvas[1].setVisible(false)");
		nothingExpected();
	}
	
	@Test
	public void testThatLayerAndItemAttributesAreCombined() {
		TestPlatform platform = new TestPlatform();
		Board board = new Board(platform);
		board.start();
		Layer layer = new Layer("layer", -500, -300, 500, 300);
		board.addItem(layer);
		layer.setLocation(20, 30);
		layer.setRotation(1.0f);
		layer.setScale(0.5f);
		ImageItem iitem = new ImageItem("hexagon.png");
		iitem.setLocation(10, 20);
		iitem.setRotation(1.5f);
		iitem.setScale(2.2f);
		layer.addItem(iitem);
		platform.process(0);
		TestRegistry.reset();
		TestImage.getImage("hexagon.png").loaded(70, 70);
		platform.process(40);
		find("20:Context2D[2].setTrandform(-0.88125795, 0.65831935, -0.65831935, -0.88125795, 255.58301, 289.61038)");
	}

	@Test
	public void testNothingIsDoneIfNothingHasChanged() {
		TestPlatform platform = new TestPlatform();
		Board board = new Board(platform);
		board.start();
		Layer layer = new Layer("layer", -500, -300, 500, 300);
		board.addItem(layer);
		ImageItem iitem = new ImageItem("hexagon.png");
		board.addItem(iitem);
		TestImage.getImage("hexagon.png").loaded(70, 70);
		platform.process(0);
		TestRegistry.reset();
		platform.process(40);
		nothingExpected();
	}
	
}
