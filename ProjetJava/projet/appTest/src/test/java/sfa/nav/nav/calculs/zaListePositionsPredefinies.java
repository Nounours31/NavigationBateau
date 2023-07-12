package sfa.nav.nav.calculs;

import sfa.nav.model.LatitudeFactory;
import sfa.nav.model.LongitudeFactory;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.PointGeographiqueFactory;

public class zaListePositionsPredefinies {
	static public final PointGeographique Paris = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromDegre(48.85),
			LongitudeFactory.fromDegre(2.35));
	static public final PointGeographique EnDessousDeParis5degre = PointGeographiqueFactory
			.fromLatLong(LatitudeFactory.fromDegre(43.85), LongitudeFactory.fromDegre(2.35));
	static public final PointGeographique EnDessusDeParis5degre = PointGeographiqueFactory
			.fromLatLong(LatitudeFactory.fromDegre(53.85), LongitudeFactory.fromDegre(2.35));
	static public final PointGeographique AGaucheDeParis5degre = PointGeographiqueFactory
			.fromLatLong(LatitudeFactory.fromDegre(48.85), LongitudeFactory.fromDegre(-2.65));
	static public final PointGeographique ADroiteDeParis5degre = PointGeographiqueFactory
			.fromLatLong(LatitudeFactory.fromDegre(48.85), LongitudeFactory.fromDegre(7.35));

	static public final PointGeographique NewYork = PointGeographiqueFactory
			.fromLatLong(LatitudeFactory.fromDegre(40.705628), LongitudeFactory.fromDegre(-74.013519));
	static public final PointGeographique GreenWitch = PointGeographiqueFactory
			.fromLatLong(LatitudeFactory.fromDegre(0.0), LongitudeFactory.fromDegre(0.0));
	static public final PointGeographique MelhPointB = PointGeographiqueFactory
			.fromLatLong(LatitudeFactory.fromDegre(60.0), LongitudeFactory.fromDegre(120.0));
	static public final PointGeographique MelhMemeLatitudePointA = PointGeographiqueFactory
			.fromLatLong(LatitudeFactory.fromDegre(40.0), LongitudeFactory.fromDegre(-140.0));
	static public final PointGeographique MelhMemeLatitudePointB = PointGeographiqueFactory
			.fromLatLong(LatitudeFactory.fromDegre(40.0), LongitudeFactory.fromDegre(160.0));
	static public final PointGeographique MelhMemeLongitudePointA = PointGeographiqueFactory
			.fromLatLong(LatitudeFactory.fromDegre(78.0), LongitudeFactory.fromDegre(107.0));
	static public final PointGeographique MelhMemeLongitudePointB = PointGeographiqueFactory
			.fromLatLong(LatitudeFactory.fromDegre(-59.1), LongitudeFactory.fromDegre(107.0));

	static public final PointGeographique Equateur = PointGeographiqueFactory
			.fromLatLong(LatitudeFactory.fromDegre(0.0), LongitudeFactory.fromDegre(0.0));
	static public final PointGeographique EquateurDessus = PointGeographiqueFactory
			.fromLatLong(LatitudeFactory.fromDegre(5.0), LongitudeFactory.fromDegre(0.0));
	static public final PointGeographique EquateurDessous = PointGeographiqueFactory
			.fromLatLong(LatitudeFactory.fromDegre(-5.0), LongitudeFactory.fromDegre(0.0));
	static public final PointGeographique EquateurGauche = PointGeographiqueFactory
			.fromLatLong(LatitudeFactory.fromDegre(0.0), LongitudeFactory.fromDegre(-5.0));
	static public final PointGeographique EquateurDroite = PointGeographiqueFactory
			.fromLatLong(LatitudeFactory.fromDegre(0.0), LongitudeFactory.fromDegre(5.0));

}
