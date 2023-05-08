package sfa.nav.model;


import org.junit.Test;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.Cap;

import static org.junit.Assert.assertEquals;

public class CapTest {

	@Test
	public void test001 () {
		int sum = 6;
		assertEquals(sum, 6);
	}
	
	@Test
	public void test002 () throws NavException {
		Cap c;
		Angle a = AngleFactory.fromRadian(Math.PI /2.0);
		c = CapFactory.fromAngle(a);		
		assertEquals(c.asDegre(), 90.0, 0.01);
	}
}
