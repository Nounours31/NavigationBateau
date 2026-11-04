package sfa.nav.model;

import sfa.nav.infra.tools.error.NavException;

public class VitesseFactory extends Distance {

	public static Vitesse fromNoeud (double noeud) throws NavException {
		Vitesse retour = new Vitesse();
		retour.vitesseInNoeud(noeud);
		return retour;
	}

}
