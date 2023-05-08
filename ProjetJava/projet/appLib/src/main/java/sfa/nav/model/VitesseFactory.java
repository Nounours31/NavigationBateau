package sfa.nav.model;

import sfa.nav.infra.tools.error.NavException;

public class VitesseFactory extends Distance {
	static public Distance fromKm (double km) throws NavException {
		Distance retour = new Distance();
		retour.distanceInkm (km);
		return retour;
	}

	public static Distance fromMn (double mn) throws NavException {
		Distance retour = new Distance();
		retour.distanceInMille (mn);
		return retour;
	}

}
