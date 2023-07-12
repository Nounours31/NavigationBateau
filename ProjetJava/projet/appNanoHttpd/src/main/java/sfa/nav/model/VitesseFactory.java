package sfa.nav.model;

import sfa.nav.infra.tools.error.NavException;

public class VitesseFactory extends Distance {
	static public Vitesse fromKmParHeure (double kmParHeure) throws NavException {
		Vitesse retour = new Vitesse();
		retour.vitesseInKmH(kmParHeure);
		return retour;
	}

	public static Vitesse fromNoeud (double noeud) throws NavException {
		Vitesse retour = new Vitesse();
		retour.vitesseInNoeud(noeud);
		return retour;
	}

}
