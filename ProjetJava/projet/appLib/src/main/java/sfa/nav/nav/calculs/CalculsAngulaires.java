package sfa.nav.nav.calculs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;
import sfa.nav.model.Cap;
import sfa.nav.model.CapFactory;
import sfa.nav.model.Distance;
import sfa.nav.model.DistanceFactory;
import sfa.nav.model.Latitude;
import sfa.nav.model.LatitudeFactory;
import sfa.nav.model.Longitude;
import sfa.nav.model.LongitudeFactory;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.PointGeographiqueFactory;
import sfa.nav.model.tools.Constantes;
import sfa.nav.model.tools.DataLoxodromieCapDistance;
import sfa.nav.model.tools.DataOrthoDromieCapDistanceVertex;
import sfa.nav.model.tools.ePointsCardinaux;


/*
================================================================================
http://ressources.univ-lemans.fr/AccesLibre/UM/Pedago/physique/02/divers/ortholoxo.html


L'orthodromie (du grec orthos : droit et dromos : course) désigne le chemin le plus court entre deux points d'une sphère, c'est-à-dire le plus petit des deux arcs 
du grand cercle passant par ces deux points. La route orthodromique entre deux points A et B du globe terrestre correspond à la route la plus courte entre eux.

Calcul de la distance orthodromique :
Pour calculer cette distance, on utilise la formule des cosinus de la trigonométrie sphérique.
Soient la et lb les latitudes de A et B et La et Lb leurs longitudes.
Pour cela on considère le triangle ABC tel que 
	C soit le pôle nord. 
	Les cotés du triangle sont a (complémentaire de la pour A), b (complémentaire de lb pour B), et l'angle γ du triangle en C vaut LB − LA (différence des longitudes). 
	
	L'arc AB vaut :

c = arccos[sin(la).sin(lb) + cos(la).cos(lb).cos(LB − LB)]

Si on exprime c en radian, la distance entre A et B est dAB = R.c km (R = 6371 km est le rayon terrestre moyen).
Un mille nautique correspondant à une minute de grand cercle, la distance entre A et B est dAB = 60.c milles nautiques si on exprime c en degrés.
Pour suivre rigoureusement la route orthodromique (tracée en vert), il faut modifier le cap en permanence. En fait, on calcule une série de caps moyens qui sont suivi pendant un certain temps.
Sur un planisphère la route orthodromique décrit une courbe complexe fonction du type de la projection utilisée. Dans le programme elle est tracée point par point.

La loxodromie (du grec loxos : oblique et dromos : course) désigne le chemin à cap constant entre deux points d'une sphère.
Sur un planisphère c'est une droite qui coupe les méridiens avec un angle constant.
Soient lx et Lx les coordonnées d'un point de la trajectoire. On a : Lx = a.lx + b avec a = (Lb − La) / (lb − la) et b = Lb − a.lb.
L'angle entre la trajectoire et les méridiens est donc α avec a = tan(α).
La route loxodromique perd tout intérêt au voisinage des pôles car elle devient alors une spirale qui s'enroule autour du pôle.
Calcul de la distance loxodromique :
On considère un point C de la trajectoire (L et l), le point voisin D (L + dL, l + dl) et le point E situé sur le même méridien que C (L, l + dl). Soit α l'angle ECD. On a ED = dl.tan(α) = cos(l).dL et dl / cos(l) = dL / tan(α).
Par hypothèse l'angle α est constant.
Par intégration, on tire : ln|tan(l/2 + π/4)| = L / tan(α) + Constante
Finalement, on obtient :


Si A et B sont sur un même parallèle (la = lb) cette relation conduit à une forme indéterminée 0 / 0. La distance est égale à la longueur de l'arc du parallèle soit Arc(AB) = (Lb − La) / cos(la).
Pour des points assez voisins, on peut utiliser la formule approchée :
Arc(AB) = (Lb − La) / cos(α) avec tan(α) = (lb − la). cos[(la + lb) /2] / (Lb − La)
Si on exprime l'arc en radian, la distance entre A et B est DAB = R.Arc(AB) km (R = 6371 km est le rayon terrestre moyen).
La valeur en milles nautiques est égale à la longueur de l'arc exprimée en minutes.
Remarque :
Cette formule n'est valable que si la différence des longitudes est inférieure à 180°. Dans le cas contraire, la trajectoire loxodromique est constituée de deux segments de droite qui joignent les points A et B aux points C (latitude lc et longitude 180°) et D (latitude lc et longitude −180°).




================================================================================
https://www.nauticalalmanac.it/fr/orthodromie-loxodromie-calculs
formules utilisées dans les calculs
e (Hayford) 	e = 0,081991890…
φ c 	φ c = (7915,7 * log tan(45°+ φ /2))-(3437,7 *e*e*sin φ)
Route Vraire 	Rv = = Δ Länge (λb-λa) / Δφc (φcb-φca)
Distance Loxodromique 	

Rv < 45°      dist. = Δφ (φb-φa)  * 1/cos Rv

45° < Rv  < 87°     dist. =  Δφ (φb-φa)  * tan Rv * 1/sin Rv

Rv >87°     dist. = Δ Longitude (λb-λa) * cos ((φa+φb)/2) * 1/sin Rv

Distance Orthodromique 	cos dist. = (sin φa * sin φb) + (cos φa * cos φb * cos Δ Longitude (λb-λa))
Angle de route au depart (Ard) 	ctg (Ard) * 1/cos φa  = 1/sin Δ Longitude * tan φb - (ctg Δ Longitude * tan φa)



================================================================================
https://fr.wikipedia.org/wiki/Orthodromie


================================================================================
https://coordinates-converter.com/fr/decimal/48.850000,2.350000?karte=OpenStreetMap&zoom=8


================================================================================
http://serge.mehl.free.fr/anx/loxodromie.html


================================================================================
https://www.aero-training.fr/calculer-une-orthodromie.html
 */

public class CalculsAngulaires {
	private static Logger logger = LoggerFactory.getLogger(CalculsAngulaires.class);

	public CalculsAngulaires() {
	}

	// ------------------------------------------------------------------------------------------------
	// A un point, direction, distance -> calcul de la future position
	// ------------------------------------------------------------------------------------------------
	public PointGeographique estimeLoxodromique (PointGeographique Depart, Cap routeFond, Distance d) {
		PointGeographique pg = null;
		if (d.distanceInMilleNautique() < 300) {
			double l = d.distanceInMilleNautique() * Math.cos(routeFond.asRadian()) / 60.0;
			Latitude latArrivee = LatitudeFactory.fromDegre(Depart.latitude().asDegre() + l);
			
			
			double latitudeMoyenne = (Depart.latitude().asRadian() + latArrivee.asRadian()) / 2.0;
			double variationLongitudeEnDegre = 1.0 *  d.distanceInMilleNautique() * Math.sin(routeFond.asRadian()) / Math.abs((60.0 * Math.cos(latitudeMoyenne)));
			Longitude longitudeArrivee = LongitudeFactory.fromDegre(Depart.longitude().asDegre() + variationLongitudeEnDegre);
			
			pg = PointGeographiqueFactory.fromLatLong(latArrivee, longitudeArrivee);
		}		
		return pg;
	}
	
	
	// ------------------------------------------------------------------------------------------------
	// A deux points donnes, cap et distance orthodromique 
	//		--> la plus courte
	//		--> cap NON consant
	// ------------------------------------------------------------------------------------------------
	public DataOrthoDromieCapDistanceVertex capOrthodromique(PointGeographique Depart, PointGeographique Arrivee) throws NavException {
		DataOrthoDromieCapDistanceVertex retour = new DataOrthoDromieCapDistanceVertex();
		retour.distance ( DistanceFactory.fromKm(0.0));
		retour.cap (CapFactory.fromDegre(0.0));
		retour.vertex (null);
		
		logger.debug("Orthodromie - chemin le plus court entre Depart {} et Arrivee {}", Depart, Arrivee);
		/*
			CA c'est le PLUS court l'arc de l'orance
		*/
		
		/*
		=== Distance orthodromique ===
			l: latitude
			L: Longitude
			
			Distance = arccos [ sin(LatA) * sin(LatB) + cos(LatA) * cos(LatB) * cos(LongB − LongA)]
			
			Cap initial:
			
							Cos (LatA) * Tan (LatB)				Sin (LatA)
			CoTan (cap) = ----------------------------   -     -----------------------
			                Sin (LongB - LongA)					Tan (LongB - Long A)
			                
			
			si Meme longitude, on se deplace sur la latitude de bas en haut 
				Distance = arccos [ sin(LatA) * sin(LatB) + cos(LatA) * cos(LatB)] = arccos [ cos(LatA - LatB) ] = LatA - LatB
				cap: 0 ou 180°
		 */
		
		double LatDepart = Depart.latitude().asRadian();
		double LatArrivee = Arrivee.latitude().asRadian();
		double LongiDepart = Depart.longitude().asRadian();
		double LongiArrivee = Arrivee.longitude().asRadian();
		
		double l = LatArrivee - LatDepart;
		double g = LongiArrivee - LongiDepart;
		double gOptimum = g;
		
		// sens du deplacement
		ePointsCardinaux[] sens = {
				ePointsCardinaux.N,
				ePointsCardinaux.E
		};
		
		sens[0] = ePointsCardinaux.N;
		if (l < 0) sens[0] = ePointsCardinaux.S;

		sens[1] = ePointsCardinaux.E;
		if (gOptimum < 0) sens[1] = ePointsCardinaux.W;

		// ----------------------------------------
		// Si g > Pi alors je ne prend pas le plus court chemein, je dois me retourner
		// ----------------------------------------
		if (Math.abs(g) > Math.PI) {
			gOptimum = 2 * Math.PI - Math.abs(gOptimum);
			sens[1] = sens[1].sensInverse();
			logger.debug("Changement de sens: g {} gOptimun {} sens {}", AngleFactory.fromRadian(g), AngleFactory.fromRadian(gOptimum), sens[1].getTag());
		}

		
		// deplacement a Longitude constante
		if (Math.abs(gOptimum) <= Angle.DegreToRadian(1.0)) {
			logger.debug("Deplacement a longitude constante : delta longitude {}°", Math.abs(Angle.RadianToDegre(gOptimum)));

			retour.distance ( DistanceFactory.fromKm(Math.abs(Constantes.RayonTerrestreEnKm * (l))));
			retour.cap ( (l < 0) ? /* vers sud */ CapFactory.fromDegre(180.0) : /* vers le nord */ CapFactory.fromDegre(0.0));
			retour.vertex (null);
		}
		else {
			double cosM = Math.sin(LatDepart) * Math.sin(LatArrivee) +  Math.cos(LatDepart) * Math.cos(LatArrivee) * Math.cos(gOptimum);
			Angle M =  AngleFactory.fromRadian(Math.acos(cosM));
			double sinM = Math.sin(M.asRadian());

			double mOrtho = Math.abs(Constantes.RayonTerrestreEnKm * M.asRadian());
			retour.distance ( DistanceFactory.fromKm(mOrtho));
			
			// ---------------------------------------
			// Angle de route initiale est notee 
			//   - 0 a 180: du nord vers l'est
			//   - 0 a -180: vers l'ouest delon le signe de g
			// ---------------------------------------
			double cosAngleRouteInitiale = (Math.sin(LatArrivee) - Math.sin(LatDepart) * cosM) / (Math.cos(LatDepart) * sinM);
			double AngleRouteInitiale = Math.acos(cosAngleRouteInitiale);
			double routeOrthoAuDepart = AngleRouteInitiale;
			
			if (sens[1].equals(ePointsCardinaux.W)) {
				routeOrthoAuDepart = 2 * Math.PI - routeOrthoAuDepart;
			}			
			retour.cap ( CapFactory.fromAngle(AngleFactory.fromRadian(routeOrthoAuDepart)));

			// ---------------------------------------
			// Vertex Angle de route initiale est notee 
			// ---------------------------------------
			double cosLatVertex = Math.cos(LatDepart) * Math.sin(AngleRouteInitiale);
			double LatVertex = Math.acos(cosLatVertex);
			Latitude latitudeVertex;
			if (Math.abs(AngleRouteInitiale) >= (Math.PI / 2.0))
				latitudeVertex = LatitudeFactory.fromDegreAndSens(LatVertex * 180.0 /Math.PI, ePointsCardinaux.Sud);
			else 
				latitudeVertex = LatitudeFactory.fromDegreAndSens(LatVertex * 180.0 /Math.PI, ePointsCardinaux.Nord);
			
			
			double cos_gVertex = Math.tan(LatDepart) / Math.tan(LatVertex);
			double gVertex = Math.acos(cos_gVertex);
			double LongiVertex = 0.0;
			ePointsCardinaux directionVersLeVertex = sens[1];
			ePointsCardinaux sensLongiVertex = Depart.longitude().getSens();
			if (gOptimum < 0) { // deplacement vers l'ouest
				gVertex = Math.abs(gVertex) * (-1.0);
				directionVersLeVertex = ePointsCardinaux.W;			
			}
			else { 
				gVertex = Math.abs(gVertex);
				directionVersLeVertex = ePointsCardinaux.E;			
			}

			LongiVertex = LongiDepart + gVertex;
			if (Math.abs(LongiVertex) > Math.PI) {
				if (directionVersLeVertex.equals(ePointsCardinaux.E)) {
					LongiVertex = LongiVertex -Math.PI;
					LongiVertex = Math.PI - LongiVertex;
					sensLongiVertex = sensLongiVertex.sensInverse();
				}
				if (directionVersLeVertex.equals(ePointsCardinaux.W)) {
					LongiVertex = Math.abs(LongiVertex) -Math.PI;
					LongiVertex = Math.PI - LongiVertex;
					sensLongiVertex = sensLongiVertex.sensInverse();
				}
			}
			else {
				sensLongiVertex = ePointsCardinaux.E;
				if (LongiVertex < 0) sensLongiVertex = ePointsCardinaux.Ouest;
				LongiVertex = Math.abs(LongiVertex);
			}
			logger.debug("Variation de longitude du vertex: gVertex {} sens {} LongitudeVertex {}", 
					AngleFactory.fromRadian(gVertex), 
					directionVersLeVertex.getTag(),
					AngleFactory.fromRadian(LongiVertex));

			Longitude longitudeVertex = LongitudeFactory.fromDegreAndSens(LongiVertex * 180.0 / Math.PI, sensLongiVertex);
			retour.vertex ( PointGeographiqueFactory.fromLatLong(latitudeVertex, longitudeVertex));
		}
		
		return retour;
	}

	// ------------------------------------------------------------------------------------------------
	// A deux points donnes, cap et distance loxodromique 
	//		--> cap constant
	// ------------------------------------------------------------------------------------------------
	public DataLoxodromieCapDistance capLoxodromique(PointGeographique Depart, PointGeographique Arrivee) throws NavException {
		DataLoxodromieCapDistance retour = new DataLoxodromieCapDistance();
		retour.distance ( DistanceFactory.fromKm(0.0));
		retour.cap ( CapFactory.fromDegre(0.0));

		logger.debug("loxodromie - A cap constant entre Depart {} et Arrivee {}", Depart, Arrivee);
		/*
				CA c'est le cas de tous les jours, la vrai nav ....
				!! --> Cette formule n'est valable que si la différence des longitudes est inférieure à 180°
		 */
		// le mieux explique: 
		//			http://serge.mehl.free.fr/anx/loxodromie.html
		
		/*
		 				Pi	   x
		 Guderman (x) = -- +  --- 
		  				4	   2
		 
		 						       LongiArrivee           -         LongiDepart
		 Direction = ArcTan ( ------------------------------------------------------------ )
		 					ln( tan( Guderman( LatArrivee )) - ln( tan( Guderman( LatDepart ))
		 
		 				LatArrivee - LatDepart
		 Distance =   -----------------
		                cos ( Cap )
		 */

		double LatDepart = Depart.latitude().asRadian();
		double LatArrivee = Arrivee.latitude().asRadian();
		double LongiDepart = Depart.longitude().asRadian();
		double LongiArrivee = Arrivee.longitude().asRadian();

		
		double l = LatArrivee - LatDepart;
		double g = LongiArrivee - LongiDepart;
		double gOptimum = g;

		// sens du deplacement
		ePointsCardinaux[] sens = {
				ePointsCardinaux.N,
				ePointsCardinaux.E
		};
		
		sens[0] = ePointsCardinaux.N;
		if (l < 0) sens[0] = ePointsCardinaux.S;

		sens[1] = ePointsCardinaux.E;
		if (g < 0) sens[1] = ePointsCardinaux.W;

		// ----------------------------------------
		// Si g > Pi alors je ne prend pas le plus court chemein, je dois me retourner
		// ----------------------------------------
		if (Math.abs(g) > Math.PI) {
			gOptimum = 2 * Math.PI - Math.abs(g);
			sens[1] = sens[1].sensInverse();
		}

		
		// A Latitude constante --> on se deplace d'est en ouest ou inverse
		if (Math.abs(LatDepart - LatArrivee) < Angle.DegreToRadian(0.01)) {
			logger.debug("Deplacement a latitude constante : delta latitude {}°", Math.abs(Angle.RadianToDegre(LatDepart - LatArrivee)));

			retour.cap (CapFactory.fromDegre(90.0));
			// --------------------------------
			// Long > 0 =>  Ouest
			// Si g <0 alors on va vers l'ouest
			// --------------------------------
			if (g < 0)
				retour.cap (CapFactory.fromDegre(270.0));
			
			retour.distance (DistanceFactory.fromKm(Constantes.RayonTerrestreEnKm * Math.abs(gOptimum * Math.cos(LatDepart))));
		}
		else {
			logger.debug("Deplacement std");


			
			double GuderMannDepart = Math.PI / 4.0 + LatDepart / 2; 
			double GuderMannArrivee = Math.PI / 4.0 + LatArrivee / 2; 

			double LatitudeCroissanteDepart = Math.abs(Math.log(Math.tan(GuderMannDepart)));
			if (LatDepart < 0) LatitudeCroissanteDepart = LatitudeCroissanteDepart * (-1.0);
			
			double LatitudeCroissanteArrivee = Math.abs(Math.log(Math.tan(GuderMannArrivee)));
			if (LatArrivee < 0) LatitudeCroissanteArrivee = LatitudeCroissanteArrivee * (-1.0);
			
			double TanRFQ = Math.abs(gOptimum / (LatitudeCroissanteArrivee - LatitudeCroissanteDepart));
			double RFQ = Math.atan(Math.abs(TanRFQ));
			logger.debug("\tRFQ entre A et B: {}", AngleFactory.fromRadian(RFQ));
			
			
			
			//-------------------------------
			// Applicaion RFQ a RF
			// ---> WIKI: https://fr.wikipedia.org/wiki/Route_(navigation)#Domaine_maritime
			// Pour résoudre certains calculs particuliers (Routes et distances loxodromiques), on utilise la notion de route fond par quart. 
			// Dans ce cas la route est exprimée en degrés de 0° à 90° en partant du nord ou du sud puis 
			// en tournant de l'angle indiqué vers l'est ou vers l'ouest. Cette route notée 
			// 	RFQ
			// a un équivalent 
			// 	RF (Route fond)
			// compris entre 0° et 360°. Par exemple :
			// 	
			// si Rfq = N60°E alors Rf = 060° (= 000°+060°)
			// si Rfq = N60°W alors Rf = 300° (= 360°-060°)
			// si Rfq = S60°E alors Rf = 120° (= 180°-060°)
			// si Rfq = S60°W alors Rf = 240° (= 180°+060°)
			//-------------------------------
			double RF = 0.0;
			if (     (sens[0].equals(ePointsCardinaux.N)) && (sens[1].equals(ePointsCardinaux.E))) RF = 0.0 * Math.PI + RFQ;
			else if ((sens[0].equals(ePointsCardinaux.N)) && (sens[1].equals(ePointsCardinaux.W))) RF = 2.0 * Math.PI - RFQ;
			else if ((sens[0].equals(ePointsCardinaux.S)) && (sens[1].equals(ePointsCardinaux.E))) RF = 1.0 * Math.PI - RFQ;
			else if ((sens[0].equals(ePointsCardinaux.S)) && (sens[1].equals(ePointsCardinaux.W))) RF = 1.0 * Math.PI + RFQ;
			else
				throw new NavException(Constantes.ImpossibledeCalculerLaRFFromRFQ);

			retour.cap ( CapFactory.fromRadian(RF));

			retour.distance ( DistanceFactory.fromKm(Constantes.RayonTerrestreEnKm * (Math.abs(LatArrivee - LatDepart)) / Math.cos(RFQ)));
		}
		logger.debug("\tDistance {} Cap: {}", retour.distance().distanceInKm(), retour.cap().toString());
		
		return retour;
	}
}
