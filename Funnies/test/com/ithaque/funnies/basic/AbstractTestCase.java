package com.ithaque.funnies.basic;

import org.junit.Assert;
import org.junit.Before;

import com.ithaque.funnies.client.platform.gwt.test.TestImage;
import com.ithaque.funnies.client.platform.gwt.test.TestPlatform;
import com.ithaque.funnies.client.platform.gwt.test.TestRegistry;

public class AbstractTestCase {

	protected TestPlatform platform = new TestPlatform();
	
	@Before
	public void setup() {
		TestRegistry.reset();
		TestImage.reset();
	}
		
	public void expect(String instruction) {
		Assert.assertEquals(instruction, TestRegistry.next());		
	}

	public void dontFind(String instruction) {
		while (!TestRegistry.isEmpty()) {
			if (TestRegistry.next().indexOf(instruction)!=-1) {
				Assert.fail();
			}
		}
	}

	public void find(String instruction) {
		while (!TestRegistry.isEmpty()) {
			if (TestRegistry.next().indexOf(instruction)!=-1) {
				return;
			}
		}
		Assert.fail();
	}
	
	public void dump() {
		TestRegistry.dump();
	}
	
	public void reset() {
		TestRegistry.reset();
	}
	
	public void process(long time) {
		TestRegistry.reset();
		platform.process(time);
	}
	
	public void nothingExpected() {
		Assert.assertTrue(TestRegistry.isEmpty());
	}
	
	public void load(String url, int w, int h) {
		TestImage.getImage(url).loaded(w, h);
	}

}
