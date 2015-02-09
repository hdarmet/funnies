package com.ithaque.funnies.geometry;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ithaque.funnies.basic.AbstractTestCase;
import com.ithaque.funnies.shared.Clipper;
import com.ithaque.funnies.shared.Location;
import com.ithaque.funnies.shared.Shape;

public class TestClipping extends AbstractTestCase {

	@Test
	public void testSimpleIntersect() {
		float deltaX= 0.0f;
		float deltaY = 0.0f;
		Shape subject = new Shape(
			new Location(0.0f+deltaX, -50.0f+deltaY),
			new Location(50.0f+deltaX, 0.0f+deltaY),
			new Location(0.0f+deltaX, 50.0f+deltaY),
			new Location(-50.0f+deltaX, 0.0f+deltaY));
		Shape clipping = new Shape(
			new Location(0.0f+deltaX, 0.0f+deltaY),
			new Location(50.0f+deltaX, 50.0f+deltaY),
			new Location(0.0f+deltaX, 100.0f+deltaY),
			new Location(-50.0f+deltaX, 50.0f+deltaY));
		List<Shape> shapes = new Clipper().clip(subject, clipping, true, true);
		Assert.assertEquals(1, shapes.size());
		Assert.assertEquals("Location[25.0,25.0],Location[0.0,50.0],Location[-25.0,25.0],Location[0.0,0.0]", shapes.get(0).toString());
	}
	
	@Test
	public void testDoublePlygon() {
		float deltaX= 0.0f;
		float deltaY = 0.0f;
		Shape subject = new Shape(
			new Location(-10.0f+deltaX, -50.0f+deltaY),
			new Location(10.0f+deltaX, -50.0f+deltaY),
			new Location(10.0f+deltaX, 50.0f+deltaY),
			new Location(-10.0f+deltaX, 50.0f+deltaY));
		Shape clipping = new Shape(
			new Location(-50.0f+deltaX, -10.0f+deltaY),
			new Location(50.0f+deltaX, -10.0f+deltaY),
			new Location(50.0f+deltaX, 10.0f+deltaY),
			new Location(-50.0f+deltaX, 10.0f+deltaY));
		List<Shape> shapes = new Clipper().clip(subject, clipping, true, false);
		Assert.assertEquals(2, shapes.size());
		Assert.assertEquals("Location[-50.0,10.0],Location[-50.0,-10.0],Location[-10.0,-10.000004],Location[-10.0,10.0]", shapes.get(0).toString());
		Assert.assertEquals("Location[50.0,-10.0],Location[50.0,10.0],Location[10.0,10.000004],Location[10.0,-10.0]", shapes.get(1).toString());
	}
	
}
