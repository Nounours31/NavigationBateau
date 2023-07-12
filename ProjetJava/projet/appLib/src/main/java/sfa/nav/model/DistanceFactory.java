package sfa.nav.model;

public class DistanceFactory extends Distance {
	static public Distance fromKm (double km)  {
		Distance retour = new Distance();
		retour.distanceInkm (km);
		return retour;
	}

	public static Distance fromMn (double mn)  {
		Distance retour = new Distance();
		retour.distanceInMille (mn);
		return retour;
	}

}
