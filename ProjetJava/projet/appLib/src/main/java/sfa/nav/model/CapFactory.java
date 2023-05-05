package sfa.nav.model;

import sfa.nav.lib.tools.NavException;

public class CapFactory {
	
	static public Cap fromDegre (double x) throws NavException {
		Cap retour = new Cap();
		retour.set(x);
		return retour;
	}

	public static Cap fromAngle(Angle a) throws NavException {
		Cap retour = new Cap();
		retour.set(a.asDegre());
		return retour;
	}

	public static Cap fromRadian(double r) throws NavException {
		return CapFactory.fromDegre(Angle.RadianToDegre(r));
	}
}
