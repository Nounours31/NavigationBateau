package sfa.test.nav.model;


import org.junit.Test;

import sfa.nav.model.Angle;
import sfa.nav.model.Cap;
import sfa.nav.tools.NavException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CapTest {

	@Test
	public void test001 () {
		int sum = 6;
		assertEquals(sum, 6);
	}
	
	@Test
	public void test002 () throws NavException {
		Cap c;
		Angle a = Angle.fromRadian(Math.PI /2.0);
		c = new Cap(a);		
		assertEquals(c.asDegre(), 90.0, 0.01);
	}
}
