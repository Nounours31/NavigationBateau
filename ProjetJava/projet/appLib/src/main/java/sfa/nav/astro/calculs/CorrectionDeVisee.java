package sfa.nav.astro.calculs;

import sfa.nav.astro.calculs.internal.CorrectionDeViseeTablesDeNavigation;

public abstract class CorrectionDeVisee {
	public enum eTypeVisee {
		soleilBordSup, soleilBordInf, luneBordSup, luneBordInf, venus, mars, planete, etoile;
	}

	
	private final ErreurSextan err;	
	public CorrectionDeVisee (ErreurSextan _err) {
		err = _err;
	}

	

	public final ErreurSextan getErr() {
		return err;
	}


	
	public double correctionEnMinuteArcPourLeSextan() {
		double correction = +(err.collimacon.asDegre() * 60.0);
		correction += (err.exentricite.asDegre() * 60.0);
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


	public String forCanevas() {
		StringBuffer sb = new StringBuffer();
		sb.append("CorrectionDeVisee [" + err.toCanevas() + " ]");
		return sb.toString();
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
		return depressionApparenteHorizon;
	}

}
