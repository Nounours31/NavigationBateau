package sfa.nav.mylittlemath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.model.Angle;
import sfa.nav.model.Cap;
import sfa.nav.model.Distance;
import sfa.nav.model.Latitude;
import sfa.nav.model.PointGeographique;


public class CalculsAngulaires {
	private static Logger logger = LoggerFactory.getLogger(CalculsAngulaires.class);
	private final static double RayonTerrestreEnKm = 6378.0;

	public CalculsAngulaires() {
	}

	public HandlerOnCapDistance getOrthoDromieCapDistanceEntreDeuxPoints (PointGeographique A, PointGeographique B) {
		HandlerOnCapDistance retour = new HandlerOnCapDistance();
		retour._distance = new Distance();
		retour._cap = new Cap();

		// Orthodromie = la route a cap non constant
		capOrthodromique(A, B, retour);

		return retour;
	}

	public HandlerOnCapDistance getLoxoDromieCapDistanceEntreDeuxPoints (PointGeographique A, PointGeographique B) {
		HandlerOnCapDistance retour = new HandlerOnCapDistance();
		retour._distance = new Distance();
		retour._cap = new Cap();

		capLoxodromique(A, B, retour);
		return retour;
	}

	private void capOrthodromique(PointGeographique A, PointGeographique B, HandlerOnCapDistance retour) {
		/*
		=== Distance orthodromique ===
		Soit ''M'' la longueur de l'orthodromie exprimée en milles marins entre A (LatA ,LongiA) et B (LatB ,LongiB), où 

		Attention la valeur de l'arc cosinus est en degrés 

		M = 60 * arccos (sin(Lat_A) * sin(Lat_B) + cos(Lat_A) * cos(Lat_B) * cos (Longi_B - Longi_A))
		 */
		double etape = Math.sin(A.latitude().asRadian()) * Math.sin(B.latitude().asRadian());
		etape = etape +  Math.cos(A.latitude().asRadian()) * Math.cos(B.latitude().asRadian()) * Math.cos(B.longitude().asRadian() - A.longitude().asRadian());
		etape = Math.acos(etape) * 180.0 / Math.PI;
		retour._distance.distanceInMille(60.0 * etape);
		
		double capInitial = Math.cos (A.latitude().asRadian()) * Math.tan(B.latitude().asRadian()) / Math.sin (B.longitude().asRadian() - A.longitude().asRadian()); 
		capInitial = capInitial - ( Math.sin(A.latitude().asRadian()) / Math.tan(B.longitude().asRadian() - A.longitude().asRadian()));
		retour._cap = Cap.fromAngle(Angle.fromRadian(capInitial));
		
	}

	private void capLoxodromique(PointGeographique A, PointGeographique B, HandlerOnCapDistance retour) {

		double GuderMannA = Math.PI / 4.0 + A.latitude().asRadian() / 2; 
		double GuderMannB = Math.PI / 4.0 + B.latitude().asRadian() / 2; 

		double LatitudeCroissanteA = Math.log(Math.tan(GuderMannA));
		double LatitudeCroissanteB = Math.log(Math.tan(GuderMannB));


		double cap = (B.longitude().asRadian() - A.longitude().asRadian()) / (LatitudeCroissanteB - LatitudeCroissanteA);
		cap = Math.atan(cap);
		retour._cap = Cap.fromAngle(Angle.fromRadian(cap));

		boolean angleTropFaible = (Math.abs(retour._cap.asRadian() - Math.PI/2) < Math.PI / 180.0);
		angleTropFaible = angleTropFaible && (Math.abs(retour._cap.asRadian() - 3 * Math.PI/2) < Math.PI / 180.0);

		if (angleTropFaible) { // 1 degre
			Latitude LatitudeMoyenne = Latitude.fromAngle (Angle.fromRadian( (A.latitude().asRadian() + B.latitude().asRadian()) / 2.0));
			Angle RouteFondParQuart;
			/*
			 	Dans ce cas la route est exprimée en degrés de 0° à 90° en partant du nord ou du sud puis en tournant de l'angle indiqué vers l'est ou vers l'ouest
				si Rfq = N60°E alors Rf = 060° (= 000°+060°)
				si Rfq = N60°W alors Rf = 300° (= 360°-060°)
				si Rfq = S60°E alors Rf = 120° (= 180°-060°)
				si Rfq = S60°W alors Rf = 240° (= 180°+060°)
			 */
			if (retour._cap.asDegre() < 90) 
				RouteFondParQuart = Angle.fromRadian(retour._cap.asRadian());
			else if (retour._cap.asDegre() < 180) 
				RouteFondParQuart = Angle.fromRadian(-(retour._cap.asRadian() - Math.PI/2.0));
			else if (retour._cap.asDegre() < 270) 
				RouteFondParQuart = Angle.fromRadian(retour._cap.asRadian() - 2.0 * Math.PI/2.0);
			else  
				RouteFondParQuart = Angle.fromRadian(-(retour._cap.asRadian() - 3.0 * Math.PI/2.0));

			double distance = (B.longitude().asDegre() - A.longitude().asDegre()) * Math.cos(LatitudeMoyenne.asRadian());
			distance = distance / Math.sin(RouteFondParQuart.asRadian());
			retour._distance.distanceInMille(distance);
		}
		else {
			double distance = (B.latitude().asDegre() - A.latitude().asDegre()) / Math.cos(retour._cap.asRadian());
			retour._distance.distanceInMille(distance);
		}
	}

}
