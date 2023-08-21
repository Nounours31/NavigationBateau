package sfa.nav.astro.calculs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;
import sfa.nav.model.Declinaison;
import sfa.nav.model.Distance;
import sfa.nav.model.DistanceFactory;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.tools.Constantes;


public class DroiteDeHauteur {
	private static final Logger logger = LoggerFactory.getLogger(DroiteDeHauteur.class);
	
	public enum eSensIntercept {
		versPg, opposePg;
	}

	public enum eBordSoleil {
		inf, sup, etoile;
	}
	
	public class DroiteHauteurPositionnee {
		public Distance getIntercept() { return intercept; }
		public eSensIntercept getSens() { return sens; }
		public Angle getZ() { return Z; }

		private Distance intercept;
		private eSensIntercept sens;
		private Angle Z;

		public DroiteHauteurPositionnee(Distance _intercept, eSensIntercept _sens, Angle _Z) {
			intercept = _intercept;
			sens = _sens;
			Z = _Z;
		}
	}

	



	public DroiteHauteurPositionnee droitedeHauteur (PointGeographique positionEstimee, 
			Angle HauteurInstruentale_Hi, 
			double heureObservation,
			double hauteurOeil,
			Angle sextan_collimasson,
			Ephemerides ephe) throws NavException {
		// Etape 1: Declinaison astre
		Declinaison declinaisonAstreHeureObservation = ephe.declinaison(heureObservation);
		
		// Etape 2: Latitude astre
		Angle AHL_LHA = ephe.AngleHoraireLocal_AHL_LHA(heureObservation);
		
		// Etape 3: position estimee
		
		// Etape 4: Hauteur calculee
		double hauteurCalculee_Hc = Constantes.RAD2DEG * Math.asin(
				(Math.sin(declinaisonAstreHeureObservation.asRadian()) * Math.sin (positionEstimee.latitude().asRadian())) +
				(Math.cos(declinaisonAstreHeureObservation.asRadian()) * Math.cos (positionEstimee.latitude().asRadian()) * Math.cos (AHL_LHA.asRadian())));
		Angle aHauteurCalculee_Hc = AngleFactory.fromDegre(hauteurCalculee_Hc);
		
		
		// Etape 5: Intercept
		double hauteurVraie_Hv = HauteurInstruentale_Hi.asDegre() - sextan_collimasson.asDegre() + correctionEnDegre(HauteurInstruentale_Hi.asDegre(), hauteurOeil, eBordSoleil.inf);
		double interceptEnDegre = hauteurVraie_Hv - hauteurCalculee_Hc;
		Distance intercept = DistanceFactory.fromMn(interceptEnDegre * 60.00);
		eSensIntercept sens =  (intercept.distanceInKm() > 0 ? eSensIntercept.versPg : eSensIntercept.opposePg);
		
		// Etape 6: Azimut
		double azimut_Z = Constantes.RAD2DEG * Math.acos(
				(Math.sin(declinaisonAstreHeureObservation.asRadian()) - (Math.sin (positionEstimee.latitude().asRadian()) * Math.sin (aHauteurCalculee_Hc.asRadian()))) /
				(Math.cos (positionEstimee.latitude().asRadian()) * Math.cos (aHauteurCalculee_Hc.asRadian())));

		if (AHL_LHA.asDegre() < 180.0)
			azimut_Z = azimut_Z - 360.0;
		azimut_Z = Math.abs(azimut_Z);
		
		Angle Z = AngleFactory.fromDegre(azimut_Z);
		
		return new DroiteHauteurPositionnee (intercept, sens, Z);
	}

	private double correctionEnDegre (double hauteurCalculee_Hc, double hauteurOeil, eBordSoleil bord) {
		boolean noInterval = false;
		double correction = 0.0;
		final int Hi = 0;
		final double[] _hauteurOeil = { 0, 	2, 	3, 	4, 	5};		
		final double[][] _correctionEnMinuteDeArc = {
				// Hi, 	0m, 	2m, 	3m, 	4m, 	5m		
				{	6, 	7.5,	5,		4.5,	4,		3.5},
				{	7, 	8.7,	6,		5.5, 	5, 		4.5},
				{	8,	9.6,	7, 		6.5, 	6, 		5.5},
				{	9, 	10.3,	8,		7,		6.5, 	6},
				{	10, 10.8,	8.5, 	8,		7,		7},
				{	12, 11.7,	9,		8.5, 	8, 		7.5},
				{	15,	12.6, 	10,		9.5, 	9, 		8.5},
				{	20,	13.5,	11, 	10.5, 	10, 	9.5},
				{	30,	14.5,	12, 	11, 	11, 	10.5},
				{	50, 15.3, 	13, 	12, 	12, 	11},
				{	90,	16, 	13.5, 	13, 	12, 	12},
		};
		
		int i = 0;
		int iLigne = 0, jColone = 0;
		

		i = 0;
		while ((i < _correctionEnMinuteDeArc.length) && (_correctionEnMinuteDeArc[i][Hi] < hauteurCalculee_Hc)) {
			i++;
		}
		iLigne = i;
		if (iLigne == _correctionEnMinuteDeArc.length) {
			noInterval = true;
			iLigne--;
		}
			
		i = 0;
		while ((i < _hauteurOeil.length) && (_hauteurOeil[i] <= hauteurOeil)) {
			i++;
		}
		jColone = i;
		if (jColone == _hauteurOeil.length) {
			jColone--;
			noInterval = true;
		}
		
		if (noInterval)
			correction = _correctionEnMinuteDeArc[iLigne][jColone];
		else {
			double penteHauteurOeilInferieur = ((_correctionEnMinuteDeArc[iLigne][jColone -1] - _correctionEnMinuteDeArc[iLigne-1][jColone -1]) / (_correctionEnMinuteDeArc[iLigne][Hi] - _correctionEnMinuteDeArc[iLigne-1][Hi]));
			double correctionHauteurOeilInferieur =  penteHauteurOeilInferieur * hauteurCalculee_Hc + _correctionEnMinuteDeArc[iLigne-1][jColone -1]; 

			double penteHauteurOeilSuperieur = ((_correctionEnMinuteDeArc[iLigne][jColone] - _correctionEnMinuteDeArc[iLigne-1][jColone]) / (_correctionEnMinuteDeArc[iLigne][Hi] - _correctionEnMinuteDeArc[iLigne-1][Hi]));
			double correctionHauteurOeilSuperieur =  penteHauteurOeilSuperieur * hauteurCalculee_Hc + _correctionEnMinuteDeArc[iLigne-1][jColone]; 

			double pente = ((correctionHauteurOeilSuperieur - correctionHauteurOeilInferieur) / (_hauteurOeil[jColone] - _hauteurOeil[jColone-1]));
			correction =  pente * hauteurOeil + correctionHauteurOeilInferieur; 

		}
		
		
		if (bord == eBordSoleil.etoile)
			correction -= 16.0;
		
		if (bord == eBordSoleil.sup)
			correction -= 32.0;
		
		correction = correction / 60.0;
		return correction;
	}
}
