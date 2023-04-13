package sfa.nav.model;

public class Latitude extends Angle {
	private Latitude() {}

	public static Latitude fromAngle(Angle a) {
		Latitude l = new Latitude();
		l.internalFromAngle(a);
		return l;
	}
}
