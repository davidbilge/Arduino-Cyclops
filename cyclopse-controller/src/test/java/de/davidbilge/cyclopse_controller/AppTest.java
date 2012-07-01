package de.davidbilge.cyclopse_controller;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}

	public void testCalculations() {
		Assert.assertEquals(Math.PI / 4, App.getAngleAlpha(180, 180), 0.001);
		Assert.assertEquals(Math.PI / 4, App.getAngleBeta(180, 180), 0.001);

		Assert.assertEquals(0.0, App.getAngleM1(180, 180));
		Assert.assertEquals(0.0, App.getAngleM2(180, 180));
	}
}
