package com.ithaque.funnies.basic;

import org.junit.Test;

import com.ithaque.funnies.client.platform.gwt.test.TestImage;
import com.ithaque.funnies.client.platform.gwt.test.TestPlatform;
import com.ithaque.funnies.client.platform.gwt.test.TestRegistry;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.animations.FadingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.MoveAnimation;
import com.ithaque.funnies.shared.basic.items.animations.RotateAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ScalingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.easing.LinearEasing;

public class TestAnimation extends AbstractTestCase {
	TestPlatform platform;
	ImageItem item;
	Board board;
	
	public void setup() {
		super.setup();
		platform = new TestPlatform();
		board = new Board(platform);
		board.start();
		Layer layer = new Layer("layer", -500, -300, 500, 300);
		board.addItem(layer);
		item = new ImageItem("hexagon.png");
		layer.addItem(item);
		platform.process(0);
		TestImage.getImage("hexagon.png").loaded(70, 70);
		platform.process(40);
		TestRegistry.reset();
	}
	
	@Test
	public void testMoveAnimation() {
		MoveAnimation animation = new MoveAnimation(new LinearEasing(1000));
		animation.setLocation(new Location(100, 100));
		animation.setItem(item);
		board.launchAnimation(animation);	
		platform.process(80);
		find("19:Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("20:Canvas[3].getCoordinateSpaceWidth()");
		expect("21:Canvas[3].getCoordinateSpaceHeight()");
		expect("22:Context2D[3].setTrandform(1.0, 0.0, 0.0, 1.0, 504.0, 254.0)");
		expect("23:Context2D[3].setGlobalAlpha(1.0)");
		expect("24:Context2D[3].translate(-35.0, -35.0)");
		expect("25:Context2D[3].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");
		TestRegistry.reset();
		platform.process(540);
		find("17:Context2D[2].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("18:Canvas[2].getCoordinateSpaceWidth()");
		expect("19:Canvas[2].getCoordinateSpaceHeight()");
		expect("20:Context2D[2].setTrandform(1.0, 0.0, 0.0, 1.0, 550.0, 300.0)");
		expect("21:Context2D[2].setGlobalAlpha(1.0)");
		expect("22:Context2D[2].translate(-35.0, -35.0)");
		expect("23:Context2D[2].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");		TestRegistry.reset();
		TestRegistry.reset();
		platform.process(1040);
		find("15:Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("16:Canvas[3].getCoordinateSpaceWidth()");
		expect("17:Canvas[3].getCoordinateSpaceHeight()");
		expect("18:Context2D[3].setTrandform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		expect("19:Context2D[3].setGlobalAlpha(1.0)");
		expect("20:Context2D[3].translate(-35.0, -35.0)");
		expect("21:Context2D[3].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");
		TestRegistry.reset();
		platform.process(1080);
		nothingExpected();
	}
	
	@Test
	public void testRotateAnimation() {
		RotateAnimation animation = new RotateAnimation(new LinearEasing(1000));
		animation.setRotation((float)(Math.PI/2.0));
		animation.setItem(item);
		board.launchAnimation(animation);	
		platform.process(80);
		find("18:Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("19:Canvas[3].getCoordinateSpaceWidth()");
		expect("20:Canvas[3].getCoordinateSpaceHeight()");
		expect("21:Context2D[3].setTrandform(0.9980267, 0.06279052, -0.06279052, 0.9980267, 500.0, 250.0)");
		expect("22:Context2D[3].setGlobalAlpha(1.0)");
		expect("23:Context2D[3].translate(-35.0, -35.0)");
		expect("24:Context2D[3].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");
		TestRegistry.reset();
		platform.process(540);
		find("16:Context2D[2].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("17:Canvas[2].getCoordinateSpaceWidth()");
		expect("18:Canvas[2].getCoordinateSpaceHeight()");
		expect("19:Context2D[2].setTrandform(0.70710677, 0.70710677, -0.70710677, 0.70710677, 500.0, 250.0)");
		expect("20:Context2D[2].setGlobalAlpha(1.0)");
		expect("21:Context2D[2].translate(-35.0, -35.0)");
		expect("22:Context2D[2].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");
		TestRegistry.reset();
		platform.process(1040);
		find("15:Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("16:Canvas[3].getCoordinateSpaceWidth()");
		expect("17:Canvas[3].getCoordinateSpaceHeight()");
		expect("18:Context2D[3].setTrandform(-4.371139E-8, 1.0, -1.0, -4.371139E-8, 500.0, 250.0)");
		expect("19:Context2D[3].setGlobalAlpha(1.0)");
		expect("20:Context2D[3].translate(-35.0, -35.0)");
		expect("21:Context2D[3].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");		TestRegistry.reset();
		platform.process(1080);
		nothingExpected();
	}

	@Test
	public void testScalingAnimation() {
		ScalingAnimation animation = new ScalingAnimation(new LinearEasing(1000));
		animation.setScaling(2.0f);
		animation.setItem(item);
		board.launchAnimation(animation);	
		platform.process(80);
		find("18:Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("19:Canvas[3].getCoordinateSpaceWidth()");
		expect("20:Canvas[3].getCoordinateSpaceHeight()");
		expect("21:Context2D[3].setTrandform(1.04, 0.0, 0.0, 1.04, 500.0, 250.0)");
		expect("22:Context2D[3].setGlobalAlpha(1.0)");
		expect("23:Context2D[3].translate(-35.0, -35.0)");
		expect("24:Context2D[3].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");
		TestRegistry.reset();
		platform.process(540);
		find("16:Context2D[2].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("17:Canvas[2].getCoordinateSpaceWidth()");
		expect("18:Canvas[2].getCoordinateSpaceHeight()");
		expect("19:Context2D[2].setTrandform(1.5, 0.0, 0.0, 1.5, 500.0, 250.0)");
		expect("20:Context2D[2].setGlobalAlpha(1.0)");
		expect("21:Context2D[2].translate(-35.0, -35.0)");
		expect("22:Context2D[2].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");
		TestRegistry.reset();
		platform.process(1040);
		find("15:Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("16:Canvas[3].getCoordinateSpaceWidth()");
		expect("17:Canvas[3].getCoordinateSpaceHeight()");
		expect("18:Context2D[3].setTrandform(2.0, 0.0, 0.0, 2.0, 500.0, 250.0)");
		expect("19:Context2D[3].setGlobalAlpha(1.0)");
		expect("20:Context2D[3].translate(-35.0, -35.0)");
		expect("21:Context2D[3].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");
		TestRegistry.reset();
		platform.process(1080);
		nothingExpected();
	}

	@Test
	public void testFadingAnimation() {
		FadingAnimation animation = new FadingAnimation(new LinearEasing(1000), 0.1f);
		animation.setItem(item);
		board.launchAnimation(animation);	
		platform.process(80);
		find("18:Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("19:Canvas[3].getCoordinateSpaceWidth()");
		expect("20:Canvas[3].getCoordinateSpaceHeight()");
		expect("21:Context2D[3].setTrandform(1.0, 0.0, 0.0, 1.0, 500.0, 250.0)");
		expect("22:Context2D[3].setGlobalAlpha(0.964)");
		expect("23:Context2D[3].translate(-35.0, -35.0)");
		expect("24:Context2D[3].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");		
		TestRegistry.reset();
		platform.process(540);
		find("16:Context2D[2].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("17:Canvas[2].getCoordinateSpaceWidth()");
		expect("18:Canvas[2].getCoordinateSpaceHeight()");
		expect("19:Context2D[2].setTrandform(1.0, 0.0, 0.0, 1.0, 500.0, 250.0)");
		expect("20:Context2D[2].setGlobalAlpha(0.55)");
		expect("21:Context2D[2].translate(-35.0, -35.0)");
		expect("22:Context2D[2].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");		
		TestRegistry.reset();
		platform.process(1040);
		find("15:Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("16:Canvas[3].getCoordinateSpaceWidth()");
		expect("17:Canvas[3].getCoordinateSpaceHeight()");
		expect("18:Context2D[3].setTrandform(1.0, 0.0, 0.0, 1.0, 500.0, 250.0)");
		expect("19:Context2D[3].setGlobalAlpha(0.1)");
		expect("20:Context2D[3].translate(-35.0, -35.0)");
		expect("21:Context2D[3].drawImage(0, 0, 70, 70, 0, 0, 70, 70)");
		TestRegistry.reset();
		platform.process(1080);
		nothingExpected();
	}
}
