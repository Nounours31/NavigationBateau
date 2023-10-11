package sfa.nav.astro.calculs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.correctionvisee.internal.CorrectionDeVisee.eTypeVisee;
import sfa.nav.astro.calculs.correctionvisee.internal.ICorrectionDeViseeFactory;
import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;
import sfa.nav.model.AngleOriente;
import sfa.nav.model.AngleOrienteFactory;
import sfa.nav.model.Declinaison;
import sfa.nav.model.Distance;
import sfa.nav.model.DistanceFactory;
import sfa.nav.model.Latitude;
import sfa.nav.model.NavDateHeure;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.tools.Constantes;
import sfa.nav.model.tools.ePointsCardinaux;


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
		return droitedeHauteur("Lune", positionEstimee, HauteurInstruentale_Hi, heureObservation, hauteurOeil, sextanErr, null, epheLune, visee, eTypeDroiteHauteur.lune);
	}

	public DroiteHauteurPositionnee droitedeHauteurEtoile (
			String 				astre,
			PointGeographique 	positionEstimee, 
			Angle 				HauteurInstruentale_Hi, 
			NavDateHeure 		heureObservation,
			double 				hauteurOeil,
			eTypeVisee			visee,
			ErreurSextan 		sextanErr,
			Ephemerides 		ephePointVernal, // GHA Aries, Point vernal, AHso (Angle Horaire Sideral Origine)
			Ephemerides 		epheEtoile) throws NavException {
		internalCanevas = new Canevas(eTypeDroiteHauteur.etoile);
		return this.droitedeHauteur(astre, positionEstimee, HauteurInstruentale_Hi, heureObservation, hauteurOeil, sextanErr, ephePointVernal, epheEtoile, eTypeVisee.etoile, eTypeDroiteHauteur.etoile);
	}

	public DroiteHauteurPositionnee droitedeHauteurPlanete (
			String 				astre,
			PointGeographique 	positionEstimee, 
			Angle 				HauteurInstruentale_Hi, 
			NavDateHeure 		heureObservation,
			double 				hauteurOeil,
			eTypeVisee			visee,
			ErreurSextan 		sextanErr,
			Ephemerides 		ephePointVernal, // GHA Aries, Point vernal, AHso (Angle Horaire Sideral Origine)
			Ephemerides 		epheEtoile) throws NavException {
		internalCanevas = new Canevas(eTypeDroiteHauteur.planete);
		return this.droitedeHauteur(astre, positionEstimee, HauteurInstruentale_Hi, heureObservation, hauteurOeil, sextanErr, ephePointVernal, epheEtoile, eTypeVisee.etoile, eTypeDroiteHauteur.etoile);
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
		return this.droitedeHauteur("Soleil", positionEstimee, HauteurInstruentale_Hi, heureObservation, hauteurOeil, sextanErr, null, ephe, visee, eTypeDroiteHauteur.soleil);
	}

	private DroiteHauteurPositionnee droitedeHauteur (
			String 				astre,
			PointGeographique 	positionEstimee, 
			Angle 				HauteurInstruentale_Hi, 
			NavDateHeure 		heureObservation,
			double 				hauteurOeil,
			ErreurSextan 		sextanErr,
			Ephemerides 		ephePointVernal, // GHA Aries, Point vernal, AHso (Angle Horaire Sideral Origine)
			Ephemerides 		epheAstre,
			eTypeVisee			visee,
			eTypeDroiteHauteur	typeDroiteHauteur) throws NavException {

		internalCanevas.droiteDeHauteur(this);
		internalCanevas.astre(astre);
		internalCanevas.heureVisee(heureObservation);
		internalCanevas.hauteurOeil(hauteurOeil);
		internalCanevas.positionEstimee(positionEstimee);
		internalCanevas.epheAstre(epheAstre);
		internalCanevas.ephePointVernal(ephePointVernal);
		internalCanevas.Hi(HauteurInstruentale_Hi);

		// Etape 1: Declinaison astre
		Declinaison declinaisonAstreHeureObservation = epheAstre.declinaison(heureObservation);
		internalCanevas.declinaisonAstre(declinaisonAstreHeureObservation);
		
		// Etape 2: Latitude POintVernal
		Angle LHA_LocalHoraireAngle = null;
		
		if((typeDroiteHauteur == eTypeDroiteHauteur.etoile) || (typeDroiteHauteur == eTypeDroiteHauteur.planete)) {
			Angle AngleHoraireAHeureObservation_PointVernal = ephePointVernal.AngleHoraireAHeureObservation(heureObservation);
			Angle AngleHoraireAHeureObservation_AstreVsPOintVernal = epheAstre.AngleHoraireAHeureObservation(heureObservation);
			Angle AngleHoraireAHeureObservation_Astre = AngleHoraireAHeureObservation_PointVernal.plus(AngleHoraireAHeureObservation_AstreVsPOintVernal);
		
			// Etape 3: position estimee
			LHA_LocalHoraireAngle = AngleHoraireAHeureObservation_Astre.plus(positionEstimee.longitude());
			internalCanevas.angleHoraireLocalAstre(LHA_LocalHoraireAngle);
		}
		else if((typeDroiteHauteur == eTypeDroiteHauteur.soleil) || (typeDroiteHauteur == eTypeDroiteHauteur.lune)) {
			Angle AngleHoraireAHeureObservation_Astre = epheAstre.AngleHoraireAHeureObservation(heureObservation);
		
			// Etape 3: position estimee
			LHA_LocalHoraireAngle = AngleHoraireAHeureObservation_Astre.plus(positionEstimee.longitude());
			internalCanevas.angleHoraireLocalAstre(LHA_LocalHoraireAngle);
		}
		else {
			throw (new NavException("Cas non prevu " + typeDroiteHauteur));
		}
		

		
		// Etape 4: Hauteur calculee
		double hauteurCalculee_Hc = Constantes.RAD2DEG * Math.asin(
				(Math.sin(declinaisonAstreHeureObservation.asRadian()) * Math.sin (positionEstimee.latitude().asRadian())) +
				(Math.cos(declinaisonAstreHeureObservation.asRadian()) * Math.cos (positionEstimee.latitude().asRadian()) * Math.cos (LHA_LocalHoraireAngle.asRadian())));
		Angle aHauteurCalculee_Hc = AngleFactory.fromDegre(hauteurCalculee_Hc);
		internalCanevas.Hc(aHauteurCalculee_Hc);
		
		
		// Etape 5: Intercept
		ICorrectionDeVisee cv = null;
		internalCanevas.typeVisee(visee);
		AngleOriente correction = null;
		double indiceRefraction_PI = epheAstre.pi(heureObservation);	
		if(typeDroiteHauteur == eTypeDroiteHauteur.lune) {
			cv = ICorrectionDeViseeFactory.getCorrectionVisse(visee, true, sextanErr);
			correction = AngleOrienteFactory.fromDegre(cv.correctionTotale_EnDegre(HauteurInstruentale_Hi, hauteurOeil, heureObservation, indiceRefraction_PI, visee));		
		}
		else if((typeDroiteHauteur == eTypeDroiteHauteur.soleil) || (typeDroiteHauteur == eTypeDroiteHauteur.etoile)) {
			cv = ICorrectionDeViseeFactory.getCorrectionVisse(visee, true, sextanErr); 
			correction = AngleOrienteFactory.fromDegre(cv.correctionTotale_EnDegre(HauteurInstruentale_Hi, hauteurOeil, heureObservation, indiceRefraction_PI, visee));
		}
		else {
			throw (new NavException("Cas non prevu " + typeDroiteHauteur));
		}
		if (logger.isDebugEnabled()) {
			double Ha = cv.HaFromHi_EnDegre(HauteurInstruentale_Hi.asDegre(), hauteurOeil);
			double corrOeil = cv.correctionHauteurOeilMetre_EnMinuteArc(hauteurOeil);
			double corrParalaxe = cv.correctionParallaxe_EnMinuteArc(Ha, indiceRefraction_PI);
			double corrRefrac = cv.correctionRefraction_EnMinuteArc(Ha);
			double corrSextan = cv.correctionSextan_EnMinuteArc();
			double corrDiam = cv.correctionAjoutSemiDiametreAstre_EnMinuteArc(heureObservation, indiceRefraction_PI);
			double corrTypeVisee = cv.corrrectionTypeVisee_EnMinuteArc(visee, heureObservation, indiceRefraction_PI);
			double diametre = cv.diametre_EnMinuteArc(heureObservation, indiceRefraction_PI);
			
			double correctioncalculee = -corrSextan - corrOeil - corrRefrac + corrParalaxe + corrDiam - corrTypeVisee;
			double correction1 = -corrOeil - corrRefrac + corrParalaxe + corrDiam;
			double correction2 = corrTypeVisee;
			
			logger.debug("Correction debug");
			logger.debug("	Correction totale: {}°  // {}'", 
					cv.correctionTotale_EnDegre(HauteurInstruentale_Hi, hauteurOeil, heureObservation, indiceRefraction_PI, visee),
					cv.correctionTotale_EnDegre(HauteurInstruentale_Hi, hauteurOeil, heureObservation, indiceRefraction_PI, visee) * 60.0);
			logger.debug("  HP - PI: {}", indiceRefraction_PI);
			logger.debug("  --------------------------------------------------");
			logger.debug("  Ha           : {}°", Ha);
			logger.debug("  corrOeil     : {}'", corrOeil);
			logger.debug("  corrParalaxe : {}'", corrParalaxe);
			logger.debug("  corrRefrac   : {}'", corrRefrac);
			logger.debug("  corrSextan   : {}'", corrSextan);
			logger.debug("  corrDiam     : {}'", corrDiam);
			logger.debug("  corrTypeVisee: {}'", corrTypeVisee);
			logger.debug("  diametre     : {}'", diametre);
			logger.debug("  --------------------------------------------------");
			logger.debug("  Correction totale [I -d -R + P +SD - typeVisee] : {}", correctioncalculee);
			logger.debug("  --------------------------------------------------");
			logger.debug("  Correction 1 [-d -R +P +SD] : {}'", correction1);
			logger.debug("  Correction 2 [typevisee] : {}'", correction2);
		}
		internalCanevas.cv(cv);

		Angle  hauteurVraie_Hv = HauteurInstruentale_Hi.plus(correction);
		internalCanevas.Hv(hauteurVraie_Hv);

		double interceptEnDegre = hauteurVraie_Hv.asDegre() - aHauteurCalculee_Hc.asDegre();
		Distance intercept = DistanceFactory.fromMn(interceptEnDegre * 60.00);
		eSensIntercept sens =  (intercept.distanceInMilleNautique() > 0 ? eSensIntercept.versPg : eSensIntercept.opposePg);
		
		// Etape 6: Azimut
		double azimut_Z = azimutCalculed(positionEstimee, declinaisonAstreHeureObservation, aHauteurCalculee_Hc);
		azimut_Z = correctionAzimutale(azimut_Z, LHA_LocalHoraireAngle, positionEstimee.latitude());
		Angle Z = AngleFactory.fromDegre(azimut_Z);

		internalCanevas.sensIntercept(sens);
		internalCanevas.azimut(Z);
		internalCanevas.intercept(intercept);
		
		System.out.println(internalCanevas.toString());
		return new DroiteHauteurPositionnee (intercept, sens, Z);
	}

	public double azimutCalculed(PointGeographique positionEstimee, Declinaison declinaisonAstreHeureObservation,
			Angle aHauteurCalculee_Hc) {
		double azimut_Z = Constantes.RAD2DEG * Math.acos(
				(Math.sin(declinaisonAstreHeureObservation.asRadian()) - (Math.sin (positionEstimee.latitude().asRadian()) * Math.sin (aHauteurCalculee_Hc.asRadian()))) /
				(Math.cos (positionEstimee.latitude().asRadian()) * Math.cos (aHauteurCalculee_Hc.asRadian())));
		return azimut_Z;
	}

	
	
	public double correctionAzimutale(double azimut_Z, Angle LHA, Latitude latEstimee) {	
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
		return z;
	}


}
