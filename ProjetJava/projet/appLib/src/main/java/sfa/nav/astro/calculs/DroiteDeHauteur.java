package sfa.nav.astro.calculs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.CorrectionDeVisee.ErreurSextan;
import sfa.nav.astro.calculs.CorrectionDeVisee_TableDeNavigation.eTypeVisee;
import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;
import sfa.nav.model.AngleOriente;
import sfa.nav.model.AngleOrienteFactory;
import sfa.nav.model.Cap;
import sfa.nav.model.Declinaison;
import sfa.nav.model.Distance;
import sfa.nav.model.DistanceFactory;
import sfa.nav.model.Latitude;
import sfa.nav.model.LatitudeFactory;
import sfa.nav.model.LongitudeFactory;
import sfa.nav.model.NavDateHeure;
import sfa.nav.model.NavDateHeure.NavMoisDeAnnee;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.PointGeographiqueFactory;
import sfa.nav.model.tools.Constantes;
import sfa.nav.model.tools.DataLoxodromieCapDistance;
import sfa.nav.model.tools.DataOrthoDromie;
import sfa.nav.model.tools.ePointsCardinaux;
import sfa.nav.nav.calculs.CalculsDeNavigation;


public class DroiteDeHauteur {
	private static final Logger logger = LoggerFactory.getLogger(DroiteDeHauteur.class);
	private Canevas internalCanevas = null;
	
	public enum eTypeDroiteHauteur {
		soleil, lune, etoile, planete
	}

	public enum eSensIntercept {
		versPg, opposePg;
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
		@Override
		public String toString() {
			return "DroiteHauteurPositionnee [intercept=" + intercept + ", sens=" + sens + ", Z=" + Z
					+ "]";
		}
	}


	
	
	

	public DroiteHauteurPositionnee droitedeHauteurLune (
			PointGeographique 	positionEstimee, 
			Angle 				HauteurInstruentale_Hi, 
			NavDateHeure 		heureObservation,
			double 				hauteurOeil,
			eTypeVisee			visee,
			ErreurSextan 		sextanErr,
			Ephemerides 		epheLune) throws NavException {
		internalCanevas = new Canevas(eTypeDroiteHauteur.lune);
		return droitedeHauteur(positionEstimee, HauteurInstruentale_Hi, heureObservation, hauteurOeil, sextanErr, null, epheLune, visee, eTypeDroiteHauteur.lune);
	}

	public DroiteHauteurPositionnee droitedeHauteurEtoile (
			PointGeographique 	positionEstimee, 
			Angle 				HauteurInstruentale_Hi, 
			NavDateHeure 		heureObservation,
			double 				hauteurOeil,
			ErreurSextan 		sextanErr,
			Ephemerides 		ephePointVernal, // GHA Aries, Point vernal, AHso (Angle Horaire Sideral Origine)
			Ephemerides 		epheEtoile) throws NavException {
		internalCanevas = new Canevas(eTypeDroiteHauteur.etoile);
		return this.droitedeHauteur(positionEstimee, HauteurInstruentale_Hi, heureObservation, hauteurOeil, sextanErr, ephePointVernal, epheEtoile, eTypeVisee.etoile, eTypeDroiteHauteur.etoile);
	}

	public DroiteHauteurPositionnee droitedeHauteurSoleil (
			PointGeographique 	positionEstimee, 
			Angle 				HauteurInstruentale_Hi, 
			NavDateHeure 		heureObservation,
			double 				hauteurOeil,
			eTypeVisee			visee,
			ErreurSextan 		sextanErr,
			Ephemerides 		ephe) throws NavException {
		internalCanevas = new Canevas(eTypeDroiteHauteur.soleil);
		return this.droitedeHauteur(positionEstimee, HauteurInstruentale_Hi, heureObservation, hauteurOeil, sextanErr, null, ephe, visee, eTypeDroiteHauteur.soleil);
	}

	private DroiteHauteurPositionnee droitedeHauteur (
			PointGeographique 	positionEstimee, 
			Angle 				HauteurInstruentale_Hi, 
			NavDateHeure 		heureObservation,
			double 				hauteurOeil,
			ErreurSextan 		sextanErr,
			Ephemerides 		ephePointVernal, // GHA Aries, Point vernal, AHso (Angle Horaire Sideral Origine)
			Ephemerides 		epheAstre,
			eTypeVisee			visee,
			eTypeDroiteHauteur	typeDroiteHauteur) throws NavException {

		internalCanevas.setHeurevisee(heureObservation);
		internalCanevas.setHauteurOeil(hauteurOeil);
		internalCanevas.setPositionEstimee(positionEstimee);
		internalCanevas.setEpheAstre(epheAstre);
		internalCanevas.setEphePointVernal(ephePointVernal);
		internalCanevas.setHi(HauteurInstruentale_Hi);

		// Etape 1: Declinaison astre
		Declinaison declinaisonAstreHeureObservation = epheAstre.declinaison(heureObservation);
		internalCanevas.setDeclianaisonAstre(declinaisonAstreHeureObservation);
		
		// Etape 2: Latitude POintVernal
		Angle LHA_LocalHoraireAngle = null;
		
		if(typeDroiteHauteur == eTypeDroiteHauteur.etoile) {
			Angle AngleHoraireAHeureObservation_PointVernal = ephePointVernal.AngleHoraireAHeureObservation(heureObservation);
			Angle AngleHoraireAHeureObservation_AstreVsPOintVernal = epheAstre.AngleHoraireAHeureObservation(heureObservation);
			Angle AngleHoraireAHeureObservation_Astre = AngleHoraireAHeureObservation_PointVernal.plus(AngleHoraireAHeureObservation_AstreVsPOintVernal);
		
			// Etape 3: position estimee
			LHA_LocalHoraireAngle = AngleHoraireAHeureObservation_Astre.plus(positionEstimee.longitude());
			internalCanevas.setLHAAstre(LHA_LocalHoraireAngle);
		}
		else if((typeDroiteHauteur == eTypeDroiteHauteur.soleil) || (typeDroiteHauteur == eTypeDroiteHauteur.lune)) {
			Angle AngleHoraireAHeureObservation_Astre = epheAstre.AngleHoraireAHeureObservation(heureObservation);
		
			// Etape 3: position estimee
			LHA_LocalHoraireAngle = AngleHoraireAHeureObservation_Astre.plus(positionEstimee.longitude());
			internalCanevas.setLHAAstre(LHA_LocalHoraireAngle);
		}
		else {
			throw (new NavException("Cas non prevu " + typeDroiteHauteur));
		}
		

		
		// Etape 4: Hauteur calculee
		double hauteurCalculee_Hc = Constantes.RAD2DEG * Math.asin(
				(Math.sin(declinaisonAstreHeureObservation.asRadian()) * Math.sin (positionEstimee.latitude().asRadian())) +
				(Math.cos(declinaisonAstreHeureObservation.asRadian()) * Math.cos (positionEstimee.latitude().asRadian()) * Math.cos (LHA_LocalHoraireAngle.asRadian())));
		Angle aHauteurCalculee_Hc = AngleFactory.fromDegre(hauteurCalculee_Hc);
		internalCanevas.setHc(aHauteurCalculee_Hc);
		
		
		// Etape 5: Intercept
		CorrectionDeVisee cv = new CorrectionDeVisee(sextanErr);
		AngleOriente correction = null;
		if(typeDroiteHauteur == eTypeDroiteHauteur.lune) {
			double indiceRefraction_PI = epheAstre.pi(heureObservation);	
			correction = AngleOrienteFactory.fromDegre(cv.correctionEnDegreLune(HauteurInstruentale_Hi, hauteurOeil, visee, indiceRefraction_PI));		
		}
		else if((typeDroiteHauteur == eTypeDroiteHauteur.soleil) || (typeDroiteHauteur == eTypeDroiteHauteur.etoile)) {
			correction = AngleOrienteFactory.fromDegre(cv.correctionEnDegre(HauteurInstruentale_Hi, hauteurOeil, NavMoisDeAnnee.FromNavDateHeure(heureObservation) ,visee));
		}
		else {
			throw (new NavException("Cas non prevu " + typeDroiteHauteur));
		}
		internalCanevas.setCv(cv);

		Angle  hauteurVraie_Hv = HauteurInstruentale_Hi.plus(correction);
		internalCanevas.setHv(hauteurVraie_Hv);

		double interceptEnDegre = hauteurVraie_Hv.asDegre() - aHauteurCalculee_Hc.asDegre();
		Distance intercept = DistanceFactory.fromMn(interceptEnDegre * 60.00);
		eSensIntercept sens =  (intercept.distanceInMilleNautique() > 0 ? eSensIntercept.versPg : eSensIntercept.opposePg);
		
		// Etape 6: Azimut
		double azimut_Z = Constantes.RAD2DEG * Math.acos(
				(Math.sin(declinaisonAstreHeureObservation.asRadian()) - (Math.sin (positionEstimee.latitude().asRadian()) * Math.sin (aHauteurCalculee_Hc.asRadian()))) /
				(Math.cos (positionEstimee.latitude().asRadian()) * Math.cos (aHauteurCalculee_Hc.asRadian())));

		azimut_Z = correctionAzimutale(azimut_Z, LHA_LocalHoraireAngle, positionEstimee.latitude());
		Angle Z = AngleFactory.fromDegre(azimut_Z);

		internalCanevas.setSensIntercept(sens);
		internalCanevas.setAzimut(Z);
		internalCanevas.setIntercept(intercept);
		
		System.out.println(internalCanevas.toString());
		return new DroiteHauteurPositionnee (intercept, sens, Z);
	}

	
	
	private double correctionAzimutale(double azimut_Z, Angle LHA, Latitude latEstimee) {	
		double z = azimut_Z; 
		
		double x = LHA.asDegre();
		while (x < 0.0) x += 360.0;
		while (x >= 360.0) x -= 360.0;

		if (latEstimee.getSens() == ePointsCardinaux.Nord) {
			if (LHA.asDegre() < 180.0)
				z =  360.0 - z;
			else
				z = z;
		}
		else {
			if (LHA.asDegre() > 180.0)
				z =  180.0 - z;
			else
				z = 180.0 + z;
				
		}
		z = Math.abs(z);
		internalCanevas.setAzimutCorrection(latEstimee.getSens(), LHA.asDegre(), azimut_Z, z);
		return z;
	}


}
