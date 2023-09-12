package sfa.nav.model;

public class VitesseAngulaire {
	private double vitesseInDegreParSeconde = 0.0;

	public void vitesseInDegParHeure(double degParHeure) {
		vitesseInDegreParSeconde = degParHeure / 3600.0;
	}

	public double asDegreParSeconde() {
		return vitesseInDegreParSeconde;
	}

	public double asDegreParHeure() {
		return vitesseInDegreParSeconde * 3600.0;
	}

	@Override
	public String toString() {
		return "VitesseAngulaire [vitesseInDegreParSeconde=" + vitesseInDegreParSeconde + "]";
	}

	public String toCanevas() {
		return "[" + asDegreParHeure() + "°/h]";
	}

	
}
