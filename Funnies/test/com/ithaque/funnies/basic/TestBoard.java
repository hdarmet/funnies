package com.ithaque.funnies.basic;

import org.junit.Test;

import com.ithaque.funnies.client.platform.gwt.test.TestImage;
import com.ithaque.funnies.client.platform.gwt.test.TestPlatform;
import com.ithaque.funnies.client.platform.gwt.test.TestRegistry;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.MultiLayered;
import com.ithaque.funnies.shared.basic.items.ImageItem;

public class TestBoard extends AbstractTestCase {

	@Test
	public void testLaunchBoard() {
		Board board = new Board(new TestPlatform());
		board.start();
		expect("Platform[0].startTimer()");
		expect("Platform[0].createCanvas()");
		expect("Canvas[0].setAttribute(position:absolute;left:0px;top:0px;)");
		expect("Canvas[0].setVisible(true)");
		expect("Canvas[0].setCoordinateSpaceWidth(1000)");
		expect("Canvas[0].setCoordinateSpaceHeight(500)");
		expect("Canvas[0].saveContext2D()");
		expect("Canvas[0].addToRootPanel()");
		expect("Platform[0].createCanvas()");
		expect("Canvas[1].setAttribute(position:absolute;left:0px;top:0px;)");
		expect("Canvas[1].setVisible(false)");
		expect("Canvas[1].setCoordinateSpaceWidth(1000)");
		expect("Canvas[1].setCoordinateSpaceHeight(500)");
		expect("Canvas[1].saveContext2D()");
		expect("Canvas[1].addToRootPanel()");
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
		expect("Canvas[0].setVisible(true)");
		expect("Canvas[1].setVisible(false)");
		expect("Canvas[1].restoreContext2D()");
		expect("Canvas[1].saveContext2D()");
		expect("Context2D[1].beginPath()");
		expect("Canvas[1].getCoordinateSpaceWidth()");
		expect("Canvas[1].getCoordinateSpaceHeight()");
		expect("Context2D[1].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Platform[0].createImage(hexagon.png)");
		expect("Image[hexagon.png].Image()");
		expect("Canvas[0].setVisible(false)");
		expect("Canvas[1].setVisible(true)");
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
		expect("Canvas[0].setVisible(false)");
		expect("Canvas[1].setVisible(true)");
		expect("Canvas[0].restoreContext2D()");
		expect("Canvas[0].saveContext2D()");
		expect("Context2D[0].beginPath()");
		expect("Canvas[0].getCoordinateSpaceWidth()");
		expect("Canvas[0].getCoordinateSpaceHeight()");
		expect("Context2D[0].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Image[hexagon.png].getWidth()");
		expect("Image[hexagon.png].getHeight()");
		expect("Canvas[0].getCoordinateSpaceWidth()");
		expect("Canvas[0].getCoordinateSpaceHeight()");
		expect("Context2D[0].setTransform(1.0, 0.0, 0.0, 1.0, 500.0, 250.0)");
		expect("Context2D[0].setGlobalAlpha(1.0)");
		expect("Context2D[0].translate(-35.0, -35.0)");
		expect("Context2D[0].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		expect("Canvas[0].setVisible(true)");
		expect("Canvas[1].setVisible(false)");
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
		expect("Canvas[0].setVisible(true)");
		expect("Canvas[1].setVisible(false)");
		expect("Canvas[1].restoreContext2D()");
		expect("Canvas[1].saveContext2D()");
		expect("Context2D[1].beginPath()");
		expect("Canvas[1].getCoordinateSpaceWidth()");
		expect("Canvas[1].getCoordinateSpaceHeight()");
		expect("Context2D[1].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[1].getCoordinateSpaceWidth()");
		expect("Canvas[1].getCoordinateSpaceHeight()");
		expect("Context2D[1].setTransform(0.27015114, 0.42073548, -0.42073548, 0.27015114, 520.0, 280.0)");
		expect("Context2D[1].setGlobalAlpha(1.0)");
		expect("Context2D[1].translate(-35.0, -35.0)");
		expect("Context2D[1].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		expect("Canvas[0].setVisible(false)");
		expect("Canvas[1].setVisible(true)");
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
		expect("Canvas[0].getCoordinateSpaceWidth()");
		expect("Canvas[0].getCoordinateSpaceHeight()");
		expect("Canvas[0].getCoordinateSpaceWidth()");
		expect("Canvas[0].getCoordinateSpaceHeight()");
		expect("Canvas[0].setVisible(true)");
		expect("Canvas[1].setVisible(false)");
		expect("Canvas[1].restoreContext2D()");
		expect("Canvas[1].saveContext2D()");
		expect("Context2D[1].beginPath()");
		expect("Canvas[1].getCoordinateSpaceWidth()");
		expect("Canvas[1].getCoordinateSpaceHeight()");
		expect("Context2D[1].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Platform[0].createCanvas()");
		expect("Canvas[2].setAttribute(position:absolute;left:0px;top:0px;)");
		expect("Canvas[2].setVisible(true)");
		expect("Canvas[2].setCoordinateSpaceWidth(1000)");
		expect("Canvas[2].setCoordinateSpaceHeight(500)");
		expect("Canvas[2].saveContext2D()");
		expect("Canvas[2].addToRootPanel()");
		expect("Platform[0].createCanvas()");
		expect("Canvas[3].setAttribute(position:absolute;left:0px;top:0px;)");
		expect("Canvas[3].setVisible(false)");
		expect("Canvas[3].setCoordinateSpaceWidth(1000)");
		expect("Canvas[3].setCoordinateSpaceHeight(500)");
		expect("Canvas[3].saveContext2D()");
		expect("Canvas[3].addToRootPanel()");
		expect("Canvas[2].setVisible(true)");
		expect("Canvas[3].setVisible(false)");
		expect("Canvas[3].restoreContext2D()");
		expect("Canvas[3].saveContext2D()");
		expect("Context2D[3].beginPath()");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[2].setVisible(false)");
		expect("Canvas[3].setVisible(true)");
		expect("Canvas[0].setVisible(false)");
		expect("Canvas[1].setVisible(true)");
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
		platform.process(40);
		TestImage.getImage("hexagon.png").loaded(70, 70);
		platform.process(80);
		find("Context2D[1].setTransform(1.0, 0.0, 0.0, 1.0, 500.0, 250.0)");
		expect("Context2D[1].setGlobalAlpha(1.0)");
		expect("Context2D[1].translate(-35.0, -35.0)");
		expect("Context2D[1].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
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
		platform.process(40);
		TestImage.getImage("hexagon.png").loaded(70, 70);
		platform.process(80);
		find("Context2D[3].setTransform(0.27015114, 0.42073548, -0.42073548, 0.27015114, 261.2962, 280.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
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
		find("Context2D[2].setTransform(-0.88125795, 0.65831935, -0.65831935, -0.88125795, 255.58301, 289.61038)");
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
		platform.process(0);
		TestImage.getImage("hexagon.png").loaded(70, 70);
		platform.process(40);
		TestRegistry.reset();
		platform.process(80);
		nothingExpected();
	}
	
	@Test
	public void testMultiLayeredInitialization() {
		TestPlatform platform = new TestPlatform();
		Board board = new Board(platform);
		board.start();
		MultiLayered layered = new MultiLayered(-500, -300, 500, 300);
		board.addItem(layered);
		layered.createAttachedLayer("l1");
		layered.createAttachedLayer("l2");
		platform.process(0);
		find("Platform[0].createCanvas()");
		expect("Canvas[0].setAttribute(position:absolute;left:0px;top:0px;)");
		find("Platform[0].createCanvas()");
		expect("Canvas[1].setAttribute(position:absolute;left:0px;top:0px;)");
		find("Platform[0].createCanvas()");
		expect("Canvas[2].setAttribute(position:absolute;left:0px;top:0px;)");
		expect("Canvas[2].setVisible(true)");
		expect("Canvas[2].setCoordinateSpaceWidth(1000)");
		expect("Canvas[2].setCoordinateSpaceHeight(500)");
		expect("Canvas[2].saveContext2D()");
		expect("Canvas[2].addToRootPanel()");
		expect("Platform[0].createCanvas()");
		expect("Canvas[3].setAttribute(position:absolute;left:0px;top:0px;)");
		expect("Canvas[3].setVisible(false)");
		expect("Canvas[3].setCoordinateSpaceWidth(1000)");
		expect("Canvas[3].setCoordinateSpaceHeight(500)");
		expect("Canvas[3].saveContext2D()");
		expect("Canvas[3].addToRootPanel()");
		expect("Canvas[2].setVisible(true)");
		expect("Canvas[3].setVisible(false)");
		expect("Canvas[3].restoreContext2D()");
		expect("Canvas[3].saveContext2D()");
		expect("Context2D[3].beginPath()");
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Platform[0].createCanvas()");
		expect("Canvas[4].setAttribute(position:absolute;left:0px;top:0px;)");
		expect("Canvas[4].setVisible(true)");
		expect("Canvas[4].setCoordinateSpaceWidth(1000)");
		expect("Canvas[4].setCoordinateSpaceHeight(500)");
		expect("Canvas[4].saveContext2D()");
		expect("Canvas[4].addToRootPanel()");
		expect("Platform[0].createCanvas()");
		expect("Canvas[5].setAttribute(position:absolute;left:0px;top:0px;)");
		expect("Canvas[5].setVisible(false)");
		expect("Canvas[5].setCoordinateSpaceWidth(1000)");
		expect("Canvas[5].setCoordinateSpaceHeight(500)");
		expect("Canvas[5].saveContext2D()");
		expect("Canvas[5].addToRootPanel()");
		expect("Canvas[4].setVisible(true)");
		expect("Canvas[5].setVisible(false)");
		expect("Canvas[5].restoreContext2D()");
		expect("Canvas[5].saveContext2D()");
		expect("Context2D[5].beginPath()");
		find("Context2D[5].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[2].setVisible(false)");
		expect("Canvas[3].setVisible(true)");
		expect("Canvas[4].setVisible(false)");
		expect("Canvas[5].setVisible(true)");
		expect("Canvas[0].setVisible(false)");
		expect("Canvas[1].setVisible(true)");
	}

	@Test
	public void testMultiLayeredSynchronization() {
		TestPlatform platform = new TestPlatform();
		Board board = new Board(platform);
		board.start();
		MultiLayered layered = new MultiLayered(-500, -300, 500, 300);
		board.addItem(layered);
		Layer firstLayer = layered.createAttachedLayer("l1");
		Layer secondLayer = layered.createAttachedLayer("l2");
		ImageItem item1 = new ImageItem("hexagon.png");
		firstLayer.addItem(item1);
		ImageItem item2 = new ImageItem("rhexagon.png");
		item2.setLocation(new Location(50.0f, 50.0f));
		secondLayer.addItem(item2);
		platform.process(0);
		TestImage.getImage("hexagon.png").loaded(70, 70);
		TestImage.getImage("rhexagon.png").loaded(70, 70);
		reset();
		platform.process(40);
		find("Context2D[2].setTransform(1.0, 0.0, 0.0, 1.0, 500.0, 250.0)");
		expect("Context2D[2].setGlobalAlpha(1.0)");
		expect("Context2D[2].translate(-35.0, -35.0)");
		expect("Context2D[2].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		find("Context2D[4].setTransform(1.0, 0.0, 0.0, 1.0, 550.0, 300.0)");
		expect("Context2D[4].setGlobalAlpha(1.0)");
		expect("Context2D[4].translate(-35.0, -35.0)");
		expect("Context2D[4].drawImage(rhexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		firstLayer.setScale(0.7f);
		firstLayer.setLocation(10.0f, 20.0f);
		secondLayer.setRotation((float)Math.PI/2.0f);
		reset();
		platform.process(80);
		find("Context2D[3].setTransform(-3.059797E-8, 0.7, -0.7, -3.059797E-8, 790.0, 270.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		find("Context2D[5].setTransform(-3.059797E-8, 0.7, -0.7, -3.059797E-8, 755.0, 305.0)");
		expect("Context2D[5].setGlobalAlpha(1.0)");
		expect("Context2D[5].translate(-35.0, -35.0)");
		expect("Context2D[5].drawImage(rhexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
	}

}
