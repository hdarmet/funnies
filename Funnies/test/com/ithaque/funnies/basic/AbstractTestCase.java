package com.ithaque.funnies.basic;

import org.junit.Assert;
import org.junit.Before;

import com.ithaque.funnies.client.platform.gwt.test.TestRegistry;

public class AbstractTestCase {

	@Before
	public void setup() {
		TestRegistry.reset();
	}
		
	public void expect(String instruction) {
		Assert.assertEquals(instruction, TestRegistry.next());		
	}

	public void find(String instruction) {
		while (!TestRegistry.isEmpty()) {
			if (TestRegistry.next().indexOf(instruction)!=-1) {
				return;
			}
		}
		Assert.fail();
	}
	
	public void nothingExpected() {
		Assert.assertTrue(TestRegistry.isEmpty());
	}
}
