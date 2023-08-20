package sfa.nav.model;

import sfa.nav.model.tools.Constantes;

public class VitesseAngulaire {
	private double vitesseInDegreParSeconde = 0.0;

	public void vitesseInDegParHeure(double degParHeure) {
		vitesseInDegreParSeconde = degParHeure / 3600.0;
	}

	public double asDegreParSeconde() {
		return vitesseInDegreParSeconde;
	}

	@Override
	public String toString() {
		return "VitesseAngulaire [vitesseInDegreParSeconde=" + vitesseInDegreParSeconde + "]";
	}

	
}
