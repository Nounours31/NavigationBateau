package sfa.nav.model;

public class CapFactory {
	
	static public Cap fromDegre (double x)  {
		Cap retour = new Cap();
		retour.set(x);
		return retour;
	}

	public static Cap fromAngle(Angle a)  {
		Cap retour = new Cap();
		retour.set(a.asDegre());
		return retour;
	}

	public static Cap fromRadian(double r)  {
		return CapFactory.fromDegre(Angle.RadianToDegre(r));
	}
}
