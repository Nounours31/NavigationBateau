package sfa.nav.model;

import sfa.nav.model.tools.Constantes;

public class Vitesse {
	double vitesseInNoeud = 0.0;

	public double vitesseInNoeud() {
		return vitesseInNoeud;
	}

	public void vitesseInNoeud(double d) {
		this.vitesseInNoeud = d;
	}

	public double vitesseInKmH() {
		return vitesseInNoeud * Constantes.milleMarinEnMetre / 1000.0;
	}

	public void vitesseInKmH(double d) {
		this.vitesseInNoeud = d / (Constantes.milleMarinEnMetre / 1000.0);
	}

	@Override
	public String toString() {
		return "Vitesse [" + vitesseInNoeud() + " Nx / "+ vitesseInKmH() + "KmH]";
	}
}
