package sfa.nav.model;

public class AngleOriente extends Angle {

	protected AngleOriente() {
		set(0);
	}

	@Override
	protected void set(double d) {
		while (d >= 360.0)
			d -= 360.0;

		while (d < -360.0)
			d += 360.0;

		super.set(d, true);
	}
}
