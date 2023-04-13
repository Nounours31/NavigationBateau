package sfa.nav.model;

public class Distance {
	double distanceInMille = 0.0;
	final static double MilleToKm = 1.852;
	
	public Distance() {
		distanceInMille = 0.0;
	}

	public double distanceInMille() {
		return distanceInMille;
	}

	public void distanceInMille(double d) {
		this.distanceInMille = d;
	}

	public double distanceInkm() {
		return distanceInMille * MilleToKm;
	}

	public void distanceInKm(double d) {
		this.distanceInMille = d / MilleToKm;
	}

	@Override
	public String toString() {
		return "Distance [" + distanceInMille() + " Mn / "+ distanceInkm() + "Km]";
	}
}
