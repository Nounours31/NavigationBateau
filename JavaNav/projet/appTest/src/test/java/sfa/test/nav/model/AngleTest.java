package sfa.test.nav.model;

import sfa.nav.model.Angle;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AngleTest {
	
	@Test
	public void test001 () {
		int sum = 6;
		assertEquals(sum, 6);
	}

	@Test
	public void test002 () {
		Angle a;
		a = Angle.fromDegre(361.00);
		assertEquals(a.asDegre(), 1.0, 0.01);
	}
}
