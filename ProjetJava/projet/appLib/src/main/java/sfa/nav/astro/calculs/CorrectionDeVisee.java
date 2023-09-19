package sfa.nav.astro.calculs;

import sfa.nav.model.Angle;
import sfa.nav.model.AngleOriente;
import sfa.nav.model.NavDateHeure;
import sfa.nav.model.NavDateHeure.NavMoisDeAnnee;

public class CorrectionDeVisee {
	public enum eTypeVisee {
		soleilBordSup, soleilBordInf, luneBordSup, luneBordInf, planete, etoile, inconnues;
	}
	
	
	

	public static class ErreurSextan {
		public ErreurSextan(AngleOriente sextan_collimasson, AngleOriente sextan_exentricite) {
			collimacon = sextan_collimasson;
			exentricite = sextan_exentricite;
		}
		final AngleOriente collimacon;
		final AngleOriente exentricite;
		
		@Override
		public String toString() {
			return "ErreurSextan [collimacon=" + collimacon 
					+ ", exentricite=" + exentricite + "]";
		}

		public String toCanevas() {
			return "Sextan [collimacon=[" + collimacon.toCanevas() + "], exentricite=[" + exentricite.toCanevas() + "]]";
		}
		
	}
	

	private final ErreurSextan err;
	public final ErreurSextan getErr() {
		return err;
	}


	public CorrectionDeVisee (ErreurSextan _err) {
		err = _err;
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



}
