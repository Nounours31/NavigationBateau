package sfa.nav.astro.calculs.correctionvisee.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.astro.calculs.ICorrectionDeVisee;
import sfa.nav.astro.calculs.correctionvisee.internal.CorrectionDeVisee.eTypeVisee;
import sfa.nav.model.Angle;
import sfa.nav.model.NavDateHeure;

public abstract class CorrectionDeVisee implements ICorrectionDeVisee {
	final Logger logger = LoggerFactory.getLogger(getClass()) ;
	
	public enum eTypeVisee {
		soleilBordSup, soleilBordInf, luneBordSup, luneBordInf, venus, mars, planete, etoile,
		// meridienne
		meridienneSoleil, meridiennePolaris;

		public static String getTypeDroiteHauteur(eTypeVisee e) {
			switch (e) {
			case soleilBordSup, soleilBordInf: return "Soleil";
			case luneBordSup, luneBordInf: return "Lune";
			case venus: return "Planete(Venus)";
			case mars: return "Planete(Mars)";
			case planete: return "Planete";
			case etoile: return "Etoile";
			default: return "Inconnu";
			}
		}
	}

	
	public class correctionDeViseeHandler {
		public double correctionRefractionParallaxeEventuellementSDetDIP;
		public double correctionSemiDiametre;
		public double correctionDIP;
	}
	
	
	private final ErreurSextan err;	
	protected final eTypeVisee visee;	
	protected final boolean isParCalcul = false;	

	protected CorrectionDeVisee (ErreurSextan _err, eTypeVisee _visee) {
		err = _err;
		visee = _visee;
	}
	
	public final ErreurSextan getErr() {
		return err;
	}


	public double correctionSextan_EnMinuteArc() {
		double correction = +(err.collimacon().asDegre() * 60.0);
		correction += (err.exentricite().asDegre() * 60.0);
		logger.debug("correctionSextan_EnMinuteArc {}", correction);
		return correction;
	}



	
	protected double Interpolation (double xDebut, double yDebut, double xFin, double yFin, double x) {
		if (Math.abs(xFin - xDebut) < 0.001) {
			return (yFin + yDebut) / 2.0;
		}

		double pente = (yFin - yDebut) / (xFin - xDebut);
		double interpolation = pente * (x - xDebut) + yDebut;
		return interpolation;
	}
	


	@Override
	public String toString() {
		return "CorrectionDeVisee [" + 
				", ErreurSextan = " + err +" ]";
	}



	@Override
	public double correctionHauteurOeilMetre_EnMinuteArc(double hauteurOeil)  {
		double d = 1.760 * Math.sqrt(hauteurOeil);
		logger.debug("correctionHauteurOeilMetre_EnMinuteArc {}", d);
		return d;
	}

	@Override
	public double HaFromHi_EnDegre (double Hi, double HauteurOeilEnMetre) {
		double Ha = Hi + (correctionSextan_EnMinuteArc() - correctionHauteurOeilMetre_EnMinuteArc(HauteurOeilEnMetre)) / 60.0; 
		logger.debug("HaFromHi_EnDegre {}", Ha);
		return Ha;
	}
	
	@Override
	public double correctionRefraction_EnMinuteArc(double Ha)  {
		return correctionRefraction_EnMinuteArc(Ha, 1010, 10);
	}


	@Override
	public double correctionRefraction_EnMinuteArc(double Ha, double pression_hectopascal, double temperatureCelcius)  {
		double R =  1.0 / Math.tan ((Ha + (7.31 / (Ha + 4.4))) * Math.PI / 180.0);
		double F = 0.28 * pression_hectopascal / (temperatureCelcius + 273);
		logger.debug("correctionRefraction_EnMinuteArc {}", R * F);
		return R * F;
	}


	@Override
	public double correctionParallaxe_EnMinuteArc(double Ha, double HP_HorizontalParallaxe)  {
		double d = HP_HorizontalParallaxe * Math.cos(Ha * Math.PI / 180.0); 
		logger.debug("correctionParallaxe_EnMinuteArc {}", d);
		return d;
	}

	@Override
	public double correctionTotale_EnDegre(Angle hauteurInstruentale_Hi, 
			double hauteurOeil,
			NavDateHeure heureObservation, 
			double indiceRefraction_PI,
			eTypeVisee t) {

		// [ -I -d -R +P + 1/2 Diametre] -Visee
		double Ha = HaFromHi_EnDegre(hauteurInstruentale_Hi.asDegre(),hauteurOeil);
		
		double correction = 
				- correctionSextan_EnMinuteArc() 
				- correctionHauteurOeilMetre_EnMinuteArc(hauteurOeil)
				- correctionRefraction_EnMinuteArc(Ha)
				+ correctionParallaxe_EnMinuteArc(Ha, indiceRefraction_PI)
				+ correctionAjoutSemiDiametreAstre_EnMinuteArc(heureObservation, indiceRefraction_PI)
				- corrrectionTypeVisee_EnMinuteArc (t, heureObservation, indiceRefraction_PI);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Ha                 : {}", Ha);
			logger.debug("Visee              : {}", t);
			logger.debug("Diametre           : {}", diametre_EnMinuteArc(heureObservation, indiceRefraction_PI));
			logger.debug("HP_PI              : {}", indiceRefraction_PI);
			logger.debug("Correction complete: {}", correction);
			logger.debug("--------------------------------------------");
			logger.debug("Correction correctionSextan_EnMinuteArc                 : {}", correctionSextan_EnMinuteArc());
			logger.debug("Correction correctionHauteurOeilMetre_EnMinuteArc       : {}", correctionHauteurOeilMetre_EnMinuteArc(hauteurOeil));
			logger.debug("Correction correctionRefraction_EnMinuteArc             : {}", correctionRefraction_EnMinuteArc(Ha));
			logger.debug("Correction correctionParallaxe_EnMinuteArc              : {}", correctionParallaxe_EnMinuteArc(Ha, indiceRefraction_PI));
			logger.debug("Correction correctionAjoutSemiDiametreAstre_EnMinuteArc : {}", correctionAjoutSemiDiametreAstre_EnMinuteArc(heureObservation, indiceRefraction_PI));
			logger.debug("Correction corrrectionTypeVisee_EnMinuteArc             : {}", corrrectionTypeVisee_EnMinuteArc(t, heureObservation, indiceRefraction_PI));
			logger.debug("--------------------------------------------");
			if ((t == eTypeVisee.luneBordInf) || (t == eTypeVisee.luneBordSup)) {
				logger.debug("Correction Sextan: [-I]                 {}", -1.0 * correctionSextan_EnMinuteArc());
				logger.debug("Correction1 :      [-d]                 {}", -1.0 * correctionHauteurOeilMetre_EnMinuteArc(hauteurOeil));
				logger.debug("Correction2 :      [-R + P + 1/2 Diam]  {}", -1.0 * correctionRefraction_EnMinuteArc(Ha)
						+ correctionParallaxe_EnMinuteArc(Ha, indiceRefraction_PI)
						+ correctionAjoutSemiDiametreAstre_EnMinuteArc(heureObservation, indiceRefraction_PI));
				logger.debug("Correction2 :      [Visee]              {} - {}", -1.0 * corrrectionTypeVisee_EnMinuteArc (t, heureObservation, indiceRefraction_PI), t);
			}
			else {
				logger.debug("Correction Sextan: [-I]                   {}", -1.0 * correctionSextan_EnMinuteArc());
				logger.debug("Correction1 :      [-d -R +P + 1/2 Diam]  {}", -1.0 * correctionHauteurOeilMetre_EnMinuteArc(hauteurOeil)
						- correctionRefraction_EnMinuteArc(Ha)
						+ correctionParallaxe_EnMinuteArc(Ha, indiceRefraction_PI)
						+ correctionAjoutSemiDiametreAstre_EnMinuteArc(heureObservation, indiceRefraction_PI));
				logger.debug("Correction2 :      [Visee]                {} - {}", -1.0 * corrrectionTypeVisee_EnMinuteArc (t, heureObservation, indiceRefraction_PI), t);
			}
		}
		
		// correction en degre
		correction = correction / 60.0;
		return correction;
	}
	
	@Override
	public double correctionAjoutSemiDiametreAstre_EnMinuteArc (NavDateHeure heureObservation, double HP) {
		double d = diametre_EnMinuteArc(heureObservation, HP) / 2.0;
		logger.debug("corrrectionDiametre_EnMinuteArc {}", d);
		return d;
	}

	@Override
	public double corrrectionTypeVisee_EnMinuteArc (eTypeVisee t, NavDateHeure heureObservation, double HP) {
		double d = 0.0;
		if ((t == eTypeVisee.soleilBordSup) || (t == eTypeVisee.luneBordSup))
			d = 1.0 * diametre_EnMinuteArc(heureObservation, HP);
		
		logger.debug("corrrectionVisee_EnMinuteArc {}", d);
		return d;
	}
}
