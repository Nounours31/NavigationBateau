package sfa.nav.model;

public class PointGeographiqueFactory extends PointGeographique{

	public static PointGeographique fromLatLong(Latitude lat, Longitude longi) {
		PointGeographique retour = new PointGeographique();
		retour.latitude(lat);
		retour.longitude(longi);
		return retour;
	}
}
