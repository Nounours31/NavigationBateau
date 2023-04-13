package sfa.nav.model;

public class Cap extends Angle {
	public Cap() {
	}
	
	
	public static Cap fromAngle(Angle a) {
		Cap l = new Cap();
		l.internalFromAngle(a);
		return l;
	}
}
