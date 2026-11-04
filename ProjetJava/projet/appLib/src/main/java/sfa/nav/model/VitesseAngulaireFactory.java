package sfa.nav.model;

import sfa.nav.infra.tools.error.NavException;

public class VitesseAngulaireFactory extends Distance {
	static public VitesseAngulaire fromDegreParHeure (double DegParHeure) throws NavException {
		VitesseAngulaire retour = new VitesseAngulaire();
		retour.vitesseInDegParHeure(DegParHeure);
		return retour;
	}


}
