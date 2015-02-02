package com.ithaque.funnies.basic;

import org.junit.Test;

import com.ithaque.funnies.client.platform.gwt.test.TestImage;
import com.ithaque.funnies.client.platform.gwt.test.TestPlatform;
import com.ithaque.funnies.client.platform.gwt.test.TestRegistry;
import com.ithaque.funnies.shared.basic.AnimationContext;
import com.ithaque.funnies.shared.basic.AnimationContext.FactorFinder;
import com.ithaque.funnies.shared.basic.AnimationContext.LocationFinder;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.Location;
import com.ithaque.funnies.shared.basic.AnimationContext.MoveableFinder;
import com.ithaque.funnies.shared.basic.Moveable;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.animations.ChangeAnimation;
import com.ithaque.funnies.shared.basic.items.animations.FadingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.MoveAnimation;
import com.ithaque.funnies.shared.basic.items.animations.RepeatableAnimation;
import com.ithaque.funnies.shared.basic.items.animations.RotateAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ScalingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.SequenceAnimation;
import com.ithaque.funnies.shared.basic.items.animations.easing.LinearEasing;

public class TestRepeatableAnimation extends AbstractTestCase {
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
	public void testRepeatedAnimationIsDone() {
		ScalingAnimation grow = new ScalingAnimation(500, 1.5f).setItem(item);
		ScalingAnimation shrink = new ScalingAnimation(500, 1.0f).setItem(item);
		SequenceAnimation sequence = new SequenceAnimation();
		sequence.addAnimation(grow);
		sequence.addAnimation(shrink);
		RepeatableAnimation repeatable = new RepeatableAnimation(sequence);
		board.launchAnimation(repeatable);
		reset();
		platform.process(40+40);
		find("Context2D[3].setTransform(1.0078542, 0.0, 0.0, 1.0078542, 500.0, 250.0)");
		reset();
		platform.process(40+520);
		find("Context2D[2].setTransform(1.5, 0.0, 0.0, 1.5, 500.0, 250.0)");
		reset();
		platform.process(40+1000);
		find("Context2D[3].setTransform(1.0, 0.0, 0.0, 1.0, 500.0, 250.0)");
		reset();
		platform.process(40+1040);
		find("Context2D[2].setTransform(1.0078542, 0.0, 0.0, 1.0078542, 500.0, 250.0)");
	}
}
