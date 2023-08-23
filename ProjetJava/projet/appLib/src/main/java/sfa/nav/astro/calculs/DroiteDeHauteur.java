package sfa.nav.astro.calculs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.CorrectionDeVisee.ErreurSextan;
import sfa.nav.astro.calculs.CorrectionDeVisee.eTypeCorrection;
import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;
import sfa.nav.model.AngleOriente;
import sfa.nav.model.AngleOrienteFactory;
import sfa.nav.model.Declinaison;
import sfa.nav.model.Distance;
import sfa.nav.model.DistanceFactory;
import sfa.nav.model.NavDateHeure;
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

	



	public DroiteHauteurPositionnee droitedeHauteur (
			PointGeographique 	positionEstimee, 
			Angle 				HauteurInstruentale_Hi, 
			NavDateHeure 		heureObservation,
			double 				hauteurOeil,
			ErreurSextan 		sextanErr,
			Ephemerides 		ephe) throws NavException {
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
		CorrectionDeVisee cv = new CorrectionDeVisee(eTypeCorrection.soleil, sextanErr);
		AngleOriente correction = AngleOrienteFactory.fromDegre(cv.correctionEnDegre(HauteurInstruentale_Hi, hauteurOeil, eBordSoleil.inf));
		Angle  hauteurVraie_Hv = HauteurInstruentale_Hi.plus(correction);
		double interceptEnDegre = hauteurVraie_Hv.asDegre() - aHauteurCalculee_Hc.asDegre();
		Distance intercept = DistanceFactory.fromMn(interceptEnDegre * 60.00);
		eSensIntercept sens =  (intercept.distanceInMilleNautique() > 0 ? eSensIntercept.versPg : eSensIntercept.opposePg);
		
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


}
