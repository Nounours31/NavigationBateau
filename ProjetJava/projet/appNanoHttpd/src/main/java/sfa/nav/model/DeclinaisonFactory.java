package sfa.nav.model;

import sfa.nav.infra.tools.error.NavException;

public class DeclinaisonFactory {
	
	static public Declinaison fromDegre (double x) throws NavException {
		Declinaison retour = new Declinaison();
		retour.set(x);
		return retour;
	}

	public static Declinaison fromString(Object object) {
		return null;
	}
}
