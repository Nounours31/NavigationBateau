package sfa.nav.model;

import sfa.nav.lib.tools.Constantes;

public class Distance {
	double distanceInMille = 0.0;
	
	public Distance() {
		distanceInMille = 0.0;
	}

	public double distanceInMille() {
		return distanceInMille;
	}

	public void distanceInMille(double d) {
		this.distanceInMille = d;
	}

	public double distanceInKm() {
		return distanceInMille * Constantes.milleMarinEnMetre / 1000.0;
	}

	public void distanceInKm(double d) {
		this.distanceInMille = d / (Constantes.milleMarinEnMetre / 1000.0);
	}

	@Override
	public String toString() {
		return "Distance [" + distanceInMille() + " Mn / "+ distanceInKm() + "Km]";
	}
}
