package sfa.nav.astro.calculs.correctionvisee.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.astro.calculs.ICorrectionDeVisee;
import sfa.nav.model.NavDateHeure;

public abstract class CorrectionDeVisee implements ICorrectionDeVisee {
	final Logger logger = LoggerFactory.getLogger(getClass()) ;
	
	public enum eTypeVisee {
		soleilBordSup, soleilBordInf, luneBordSup, luneBordInf, venus, mars, planete, etoile;

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


	public double DIP_ParLaTable(double hauteurOeil) {
		int i = 0;
		int iInf = 0, iSup = 0;
		int borneSup = CorrectionDeViseeTablesDeNavigation.DIP_HauteurOeilEnMetre.length;
		while ( (i < borneSup) && (CorrectionDeViseeTablesDeNavigation.DIP_HauteurOeilEnMetre[i] < hauteurOeil)) {
			i++;
		}
		if (i == 0) {
			iInf = iSup = 0;
		}
		else if (i == borneSup) {
			iSup = borneSup -1;
			iInf = iSup;
		}
		else {
			iInf = i -1;
			iSup = i;
		}
		double depressionApparenteHorizon = Interpolation(
				CorrectionDeViseeTablesDeNavigation.DIP_HauteurOeilEnMetre[iInf],
				CorrectionDeViseeTablesDeNavigation.DIP_CorrectionDepression_EnMinuteDeArc[iInf], 
				CorrectionDeViseeTablesDeNavigation.DIP_HauteurOeilEnMetre[iSup],
				CorrectionDeViseeTablesDeNavigation.DIP_CorrectionDepression_EnMinuteDeArc[iSup], 
				hauteurOeil);

		logger.debug("DIP_ParLaTable {}", depressionApparenteHorizon);
		return depressionApparenteHorizon;
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
}
