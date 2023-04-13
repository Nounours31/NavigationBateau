package sfa.nav.model;

public class Longitude extends Angle {
	private Longitude() {}

	public static Longitude fromAngle(Angle a) {
		Longitude L = new Longitude();
		L.internalFromAngle(a);
		return L;
	}
}
