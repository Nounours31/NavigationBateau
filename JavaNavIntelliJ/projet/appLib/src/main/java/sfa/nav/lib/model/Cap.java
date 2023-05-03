package sfa.nav.lib.model;

import sfa.nav.lib.tools.NavException;

public class Cap extends Angle {
	public Cap() {
		super ();
	}	
	
	@Override
	public double asRadian() throws NavException {
		return Angle.DegreToRadian (asDegre());
	}
	
	public static Cap fromAngle(Angle d) throws NavException {
		double a = d.asDegre();
		Cap c = Cap.fromDegre(a);
		return c;
	}
	
	
	
	@Override
	public double asDegre() throws NavException {
		return super.asDegre();
	}
	
	static public Cap fromDegre (double x) throws NavException {
		Cap retour = new Cap();
		retour.set(x);
		return retour;
	}
	
	static public Cap fromRadian (double x) throws NavException {
		double a = Angle.RadianToDegre(x);
		Cap retour = Cap.fromDegre(a);
		return retour;
	}	
}
