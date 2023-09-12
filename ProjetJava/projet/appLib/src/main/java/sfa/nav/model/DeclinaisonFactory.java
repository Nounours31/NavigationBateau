package sfa.nav.model;

import sfa.nav.infra.tools.error.NavException;

public class DeclinaisonFactory {
	
	static public Declinaison fromDegre (double x) throws NavException {
		Declinaison retour = new Declinaison();
		retour._internalSetInDegre(x);
		return retour;
	}

	static public Declinaison fromDegreSafe (double x) {
		Declinaison retour = new Declinaison();
		retour._internalSetInDegre(x);
		return retour;
	}

	public static Declinaison fromString(String s) throws NavException {
		Latitude l = LatitudeFactory.fromString(s);
		return fromDegre (l.asDegre());
	}
}
