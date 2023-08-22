package sfa.nav.model;

public class CapFactory {
	
	static public Cap fromDegre (double x)  {
		Cap retour = new Cap();
		retour._internalSetInDegre(x);
		return retour;
	}

	public static Cap fromAngle(Angle a)  {
		Cap retour = new Cap();
		retour._internalSetInDegre(a.asDegre());
		return retour;
	}

	public static Cap fromRadian(double r)  {
		return CapFactory.fromDegre(Angle.RadianToDegre(r));
	}
}
