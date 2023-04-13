package sfa.nav.model;

public class Vitesse {
	double vitesseInNoeud = 0.0;
	final static double NoeudToKmH = 1.852;

	public double vitesseInNoeud() {
		return vitesseInNoeud;
	}

	public void vitesseInNoeud(double d) {
		this.vitesseInNoeud = d;
	}

	public double vitesseInKmH() {
		return vitesseInNoeud * NoeudToKmH;
	}

	public void vitesseInKmH(double d) {
		this.vitesseInNoeud = d / NoeudToKmH;
	}

	@Override
	public String toString() {
		return "Vitesse [" + vitesseInNoeud() + " Nx / "+ vitesseInKmH() + "KmH]";
	}
}
