package com.ithaque.funnies.basic;

import org.junit.Test;

import com.ithaque.funnies.client.platform.gwt.test.TestImage;
import com.ithaque.funnies.client.platform.gwt.test.TestPlatform;
import com.ithaque.funnies.client.platform.gwt.test.TestRegistry;
import com.ithaque.funnies.shared.Location;
import com.ithaque.funnies.shared.basic.AnimationContext;
import com.ithaque.funnies.shared.basic.AnimationContext.FactorFinder;
import com.ithaque.funnies.shared.basic.AnimationContext.LocationFinder;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.AnimationContext.MoveableFinder;
import com.ithaque.funnies.shared.basic.Moveable;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.animations.ChangeAnimation;
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
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 504.0, 254.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		TestRegistry.reset();
		platform.process(540);
		find("Context2D[2].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[2].getCoordinateSpaceWidth()");
		expect("Canvas[2].getCoordinateSpaceHeight()");
		expect("Context2D[2].setTransform(1.0, 0.0, 0.0, 1.0, 550.0, 300.0)");
		expect("Context2D[2].setGlobalAlpha(1.0)");
		expect("Context2D[2].translate(-35.0, -35.0)");
		expect("Context2D[2].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		reset();
		platform.process(1040);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 600.0, 350.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		reset();
		platform.process(1080);
		nothingExpected();
	}
	
	@Test
	public void testFinderUsageOnMoveApplication() {
		MoveableFinder itemFinder = new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return item;
			}
		};
		LocationFinder locationFinder = new LocationFinder() {
			@Override
			public Location find(AnimationContext context) {
				return new Location(100, 100);
			}		
		};
		MoveAnimation animation = new MoveAnimation(new LinearEasing(1000));
		animation.setLocation(locationFinder);
		animation.setItem(itemFinder);
		board.launchAnimation(animation);	
		platform.process(80);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 504.0, 254.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
	}
	
	@Test
	public void testMoveApplicationBuilderWithValues() {
		MoveableFinder itemFinder = new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return item;
			}
		};
		MoveAnimation.Builder animationBuilder = new MoveAnimation.Builder(1000);
		animationBuilder.setLocation(new Location(100, 100));
		animationBuilder.setItem(itemFinder);
		board.launchAnimation(animationBuilder.create());	
		platform.process(80);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 504.0, 254.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
	}

	@Test
	public void testMoveApplicationBuilder() {
		MoveableFinder itemFinder = new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return item;
			}
		};
		LocationFinder locationFinder = new LocationFinder() {
			@Override
			public Location find(AnimationContext context) {
				return new Location(100, 100);
			}		
		};
		MoveAnimation.Builder animationBuilder = new MoveAnimation.Builder(new LinearEasing.Builder(1000));
		animationBuilder.setLocation(locationFinder);
		animationBuilder.setItem(itemFinder);
		board.launchAnimation(animationBuilder.create());	
		platform.process(80);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 504.0, 254.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
	}

	@Test
	public void testRotateAnimation() {
		RotateAnimation animation = new RotateAnimation(new LinearEasing(1000));
		animation.setRotation((float)(Math.PI/2.0));
		animation.setItem(item);
		board.launchAnimation(animation);	
		platform.process(80);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(0.9980267, 0.06279052, -0.06279052, 0.9980267, 500.0, 250.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		reset();
		platform.process(540);
		find("Context2D[2].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[2].getCoordinateSpaceWidth()");
		expect("Canvas[2].getCoordinateSpaceHeight()");
		expect("Context2D[2].setTransform(0.70710677, 0.70710677, -0.70710677, 0.70710677, 500.0, 250.0)");
		expect("Context2D[2].setGlobalAlpha(1.0)");
		expect("Context2D[2].translate(-35.0, -35.0)");
		expect("Context2D[2].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		reset();
		platform.process(1040);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(-4.371139E-8, 1.0, -1.0, -4.371139E-8, 500.0, 250.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");		TestRegistry.reset();
		platform.process(1080);
		nothingExpected();
	}

	@Test
	public void testFinderUsageOnRotateAnimation() {
		MoveableFinder itemFinder = new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return item;
			}
		};
		FactorFinder rotateFinder = new FactorFinder() {
			@Override
			public Float find(AnimationContext context) {
				return (float)(Math.PI/2.0);
			}		
		};
		RotateAnimation animation = new RotateAnimation(new LinearEasing(1000));
		animation.setRotation(rotateFinder);
		animation.setItem(itemFinder);
		board.launchAnimation(animation);	
		platform.process(80);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(0.9980267, 0.06279052, -0.06279052, 0.9980267, 500.0, 250.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
	}
	
	@Test
	public void testRotateAnimationBuilder() {
		MoveableFinder itemFinder = new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return item;
			}
		};
		FactorFinder rotateFinder = new FactorFinder() {
			@Override
			public Float find(AnimationContext context) {
				return (float)(Math.PI/2.0);
			}		
		};
		RotateAnimation.Builder animationBuilder = new RotateAnimation.Builder(new LinearEasing.Builder(1000));
		animationBuilder.setRotation(rotateFinder);
		animationBuilder.setItem(itemFinder);
		board.launchAnimation(animationBuilder.create());	
		platform.process(80);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(0.9980267, 0.06279052, -0.06279052, 0.9980267, 500.0, 250.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
	}

	@Test
	public void testRotateAnimationBuilderWithValues() {
		MoveableFinder itemFinder = new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return item;
			}
		};
		RotateAnimation.Builder animationBuilder = new RotateAnimation.Builder(1000, (float)(Math.PI/2.0));
		animationBuilder.setItem(itemFinder);
		board.launchAnimation(animationBuilder.create());	
		platform.process(80);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(0.9999808, 0.0061930595, -0.0061930595, 0.9999808, 500.0, 250.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
	}

	@Test
	public void testScalingAnimation() {
		ScalingAnimation animation = new ScalingAnimation(new LinearEasing(1000));
		animation.setScaling(2.0f);
		animation.setItem(item);
		board.launchAnimation(animation);	
		platform.process(80);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(1.04, 0.0, 0.0, 1.04, 500.0, 250.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		reset();
		platform.process(540);
		find("Context2D[2].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[2].getCoordinateSpaceWidth()");
		expect("Canvas[2].getCoordinateSpaceHeight()");
		expect("Context2D[2].setTransform(1.5, 0.0, 0.0, 1.5, 500.0, 250.0)");
		expect("Context2D[2].setGlobalAlpha(1.0)");
		expect("Context2D[2].translate(-35.0, -35.0)");
		expect("Context2D[2].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		reset();
		platform.process(1040);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(2.0, 0.0, 0.0, 2.0, 500.0, 250.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		reset();
		platform.process(1080);
		nothingExpected();
	}

	@Test
	public void testFinderUsageOnScalingAnimation() {
		MoveableFinder itemFinder = new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return item;
			}
		};
		FactorFinder scalingFinder = new FactorFinder() {
			@Override
			public Float find(AnimationContext context) {
				return 2.0f;
			}		
		};
		ScalingAnimation animation = new ScalingAnimation(new LinearEasing(1000));
		animation.setScaling(scalingFinder);
		animation.setItem(itemFinder);
		board.launchAnimation(animation);	
		platform.process(80);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(1.04, 0.0, 0.0, 1.04, 500.0, 250.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
	}
	
	@Test
	public void testScalingAnimationBuilder() {
		MoveableFinder itemFinder = new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return item;
			}
		};
		FactorFinder scalingFinder = new FactorFinder() {
			@Override
			public Float find(AnimationContext context) {
				return 2.0f;
			}		
		};
		ScalingAnimation.Builder animationBuilder = new ScalingAnimation.Builder(new LinearEasing.Builder(1000));
		animationBuilder.setScaling(scalingFinder);
		animationBuilder.setItem(itemFinder);
		board.launchAnimation(animationBuilder.create());	
		platform.process(80);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(1.04, 0.0, 0.0, 1.04, 500.0, 250.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
	}

	@Test
	public void testScalingAnimationBuilderWithValues() {
		MoveableFinder itemFinder = new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return item;
			}
		};
		ScalingAnimation.Builder animationBuilder = new ScalingAnimation.Builder(1000, 2.0f);
		animationBuilder.setItem(itemFinder);
		board.launchAnimation(animationBuilder.create());	
		platform.process(80);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(1.0039426, 0.0, 0.0, 1.0039426, 500.0, 250.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
	}

	@Test
	public void testFadingAnimation() {
		FadingAnimation animation = new FadingAnimation(new LinearEasing(1000), 0.1f);
		animation.setItem(item);
		board.launchAnimation(animation);	
		platform.process(80);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 500.0, 250.0)");
		expect("Context2D[3].setGlobalAlpha(0.964)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");		
		reset();
		platform.process(540);
		find("Context2D[2].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[2].getCoordinateSpaceWidth()");
		expect("Canvas[2].getCoordinateSpaceHeight()");
		expect("Context2D[2].setTransform(1.0, 0.0, 0.0, 1.0, 500.0, 250.0)");
		expect("Context2D[2].setGlobalAlpha(0.55)");
		expect("Context2D[2].translate(-35.0, -35.0)");
		expect("Context2D[2].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");		
		reset();
		platform.process(1040);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 500.0, 250.0)");
		expect("Context2D[3].setGlobalAlpha(0.1)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		reset();
		platform.process(1080);
		nothingExpected();
	}
	
	@Test
	public void testFinderUsageOnFadingAnimation() {
		MoveableFinder itemFinder = new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return item;
			}
		};
		FadingAnimation animation = new FadingAnimation(new LinearEasing(1000), 0.1f);
		animation.setItem(itemFinder);
		board.launchAnimation(animation);	
		platform.process(80);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 500.0, 250.0)");
		expect("Context2D[3].setGlobalAlpha(0.964)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");		
	}

	@Test
	public void testFadingAnimationBuilder() {
		MoveableFinder itemFinder = new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return item;
			}
		};
		FadingAnimation.Builder animationBuilder = new FadingAnimation.Builder(new LinearEasing.Builder(1000), 0.1f);
		animationBuilder.setItem(itemFinder);
		board.launchAnimation(animationBuilder.create());	
		platform.process(80);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 500.0, 250.0)");
		expect("Context2D[3].setGlobalAlpha(0.964)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");		
	}
	
	//--------------------------------------
	
	@Test
	public void testChangeAnimation() {
		ChangeAnimation animation = new ChangeAnimation(new LinearEasing(1000));
		animation.setLocation(new Location(100, 100));
		animation.setScale(0.5f);
		animation.setRotation((float)Math.PI/3.0f);
		animation.setItem(item);
		board.launchAnimation(animation);	
		platform.process(80);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(0.9791404, 0.041038144, -0.041038144, 0.9791404, 504.0, 254.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");		
		reset();
		platform.process(540);
		find("Context2D[2].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[2].getCoordinateSpaceWidth()");
		expect("Canvas[2].getCoordinateSpaceHeight()");
		expect("Context2D[2].setTransform(0.649519, 0.375, -0.375, 0.649519, 550.0, 300.0)");
		expect("Context2D[2].setGlobalAlpha(1.0)");
		expect("Context2D[2].translate(-35.0, -35.0)");
		expect("Context2D[2].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		reset();
		platform.process(1040);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(0.24999999, 0.43301272, -0.43301272, 0.24999999, 600.0, 350.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
		reset();
		platform.process(1080);
		nothingExpected();
	}
	
	@Test
	public void testFinderUsageOnChnageApplication() {
		MoveableFinder itemFinder = new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return item;
			}
		};
		LocationFinder locationFinder = new LocationFinder() {
			@Override
			public Location find(AnimationContext context) {
				return new Location(100, 100);
			}		
		};
		FactorFinder scalingFinder = new FactorFinder() {
			@Override
			public Float find(AnimationContext context) {
				return 0.5f;
			}		
		};
		FactorFinder rotationFinder = new FactorFinder() {
			@Override
			public Float find(AnimationContext context) {
				return (float)Math.PI/3.0f;
			}		
		};
		ChangeAnimation animation = new ChangeAnimation(new LinearEasing(1000));
		animation.setLocation(locationFinder);
		animation.setRotation(rotationFinder);
		animation.setScale(scalingFinder);
		animation.setItem(itemFinder);
		board.launchAnimation(animation);	
		platform.process(80);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(0.9791404, 0.041038144, -0.041038144, 0.9791404, 504.0, 254.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
	}

	@Test
	public void testChangeApplicationBuilderWithValues() {
		MoveableFinder itemFinder = new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return item;
			}
		};
		ChangeAnimation.Builder animationBuilder = new ChangeAnimation.Builder(new LinearEasing.Builder(1000));
		animationBuilder.setLocation(new Location(100.0f, 100.0f));
		animationBuilder.setRotation((float)Math.PI/3.0f);
		animationBuilder.setScale(0.5f);
		animationBuilder.setItem(itemFinder);
		board.launchAnimation(animationBuilder.create());	
		platform.process(80);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(0.9791404, 0.041038144, -0.041038144, 0.9791404, 504.0, 254.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
	}

	@Test
	public void testChangeApplicationBuilder() {
		MoveableFinder itemFinder = new MoveableFinder() {
			@Override
			public Moveable find(AnimationContext context) {
				return item;
			}
		};
		LocationFinder locationFinder = new LocationFinder() {
			@Override
			public Location find(AnimationContext context) {
				return new Location(100, 100);
			}		
		};
		FactorFinder scalingFinder = new FactorFinder() {
			@Override
			public Float find(AnimationContext context) {
				return 0.5f;
			}		
		};
		FactorFinder rotationFinder = new FactorFinder() {
			@Override
			public Float find(AnimationContext context) {
				return (float)Math.PI/3.0f;
			}		
		};
		ChangeAnimation.Builder animationBuilder = new ChangeAnimation.Builder(new LinearEasing.Builder(1000));
		animationBuilder.setLocation(locationFinder);
		animationBuilder.setRotation(rotationFinder);
		animationBuilder.setScale(scalingFinder);
		animationBuilder.setItem(itemFinder);
		board.launchAnimation(animationBuilder.create());	
		platform.process(80);
		find("Context2D[3].clearRect(0.0, 0.0, 1000.0, 500.0)");
		expect("Canvas[3].getCoordinateSpaceWidth()");
		expect("Canvas[3].getCoordinateSpaceHeight()");
		expect("Context2D[3].setTransform(0.9791404, 0.041038144, -0.041038144, 0.9791404, 504.0, 254.0)");
		expect("Context2D[3].setGlobalAlpha(1.0)");
		expect("Context2D[3].translate(-35.0, -35.0)");
		expect("Context2D[3].drawImage(hexagon.png, 0, 0, 70, 70, 0, 0, 70, 70)");
	}
}
