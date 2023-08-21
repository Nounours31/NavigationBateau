package sfa.nav.astro.calculs;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;
import sfa.nav.model.Declinaison;
import sfa.nav.model.DeclinaisonFactory;
import sfa.nav.model.VitesseAngulaire;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ephemerides {
	private static final Logger logger = LoggerFactory.getLogger(Ephemerides.class);
	
	public enum EphemeridesType {
		ParGradiant, parInterval;
	}
	
	private class Ephemeride {
		private Angle GHA; 
		private Declinaison declinaison; 
		
		private double heureEnSeconde;
		
		private VitesseAngulaire varHA;
		private VitesseAngulaire varDecli; 

		public Ephemeride(Angle _GHA, Declinaison _dec, double _heureRef) {
			GHA = _GHA;
			declinaison = _dec;
			heureEnSeconde = _heureRef;
			varHA = null;
			varDecli = null;			
		}
		public Ephemeride(Angle _GHA, VitesseAngulaire _varHA, Declinaison _dec, VitesseAngulaire _varDecli, double _heureRef) {
			GHA = _GHA;
			declinaison = _dec;
			heureEnSeconde = _heureRef;
			varHA = _varHA;
			varDecli = _varDecli;			
		}

		@Override
		public String toString() {
			return "Ephemerides [GHA=" + GHA + ", declinaison=" + declinaison + ", heureEnSeconde=" + heureEnSeconde + ", varHA=" + varHA
					+ ", varDecli=" + varDecli + ", type=" + type + "]";
		}

		public double heureEnSeconde() {
			return heureEnSeconde;
		}
		public Declinaison declinaison() {
			return declinaison;
		}
		public Angle GHA() {
			return GHA;
		}
		public VitesseAngulaire variationHA() {
			return varHA;
		}
		public VitesseAngulaire variationDeclinaison() {
			return varDecli;
		}
	}
	
	final private Ephemeride ephe1;
	final private Ephemeride ephe2;
	final private EphemeridesType type; 
	
	public Ephemerides(Angle _GHA, Declinaison _dec, double _heureRef, Angle _GHA2, Declinaison _dec2, double _heureRef2) {
		ephe1 = new Ephemeride(_GHA, _dec, _heureRef);
		ephe2 = new Ephemeride(_GHA2, _dec2, _heureRef2);
		type = EphemeridesType.parInterval;
	}

	public Ephemerides(Angle _GHA, VitesseAngulaire _varHA, Declinaison _dec, VitesseAngulaire _varDecli, double _heureRef) {
		ephe1 = new Ephemeride(_GHA, _varHA, _dec, _varDecli, _heureRef);
		ephe2 = null;
		type = EphemeridesType.ParGradiant;
	}

	
	
	public Declinaison declinaison(double heureObservation) throws NavException {
		if (type == EphemeridesType.parInterval)  
				return declinaisonParInterval(heureObservation); 
		return declinaisonParVitesse(heureObservation);
	}
	
	public Angle AngleHoraireLocal_AHL_LHA(double heureObservation) throws NavException {
		if (type == EphemeridesType.parInterval)  
				return AngleHoraireLocal_AHL_LHA_ParInterval(heureObservation);
		return AngleHoraireLocal_AHL_LHA_ParVitesse(heureObservation);
	}
	

	private Declinaison declinaisonParVitesse(double dateObservationEnSecondes) throws NavException {
		if (logger.isDebugEnabled()) {
			logger.debug("Observation: {} - Heure de ref {}", new Date((long)dateObservationEnSecondes), new Date((long)ephe1.heureEnSeconde()));
		}
		
		double dureeEnSeconde = (dateObservationEnSecondes - ephe1.heureEnSeconde());
		double interpolation = ephe1.declinaison().asDegre() + ephe1.variationDeclinaison().asDegreParSeconde() * dureeEnSeconde;
		Declinaison retour = DeclinaisonFactory.fromDegre(interpolation);
		return retour;
	}

	private Angle AngleHoraireLocal_AHL_LHA_ParVitesse(double dateObservationEnSecondes) {
		if (logger.isDebugEnabled()) {
			logger.debug("Observation: {} - Heure de ref {}", new Date((long)dateObservationEnSecondes), new Date((long)ephe1.heureEnSeconde()));
		}
		
		double dureeEnSeconde = (dateObservationEnSecondes - ephe1.heureEnSeconde());
		double interpolation = ephe1.GHA().asDegre() + ephe1.variationHA().asDegreParSeconde() * dureeEnSeconde ;
		Angle retour = AngleFactory.fromDegre(interpolation);
		return retour;
	}


	private Declinaison declinaisonParInterval(double dateObservationEnSecondes) throws NavException {
		boolean isValid = ((ephe1.heureEnSeconde() <= dateObservationEnSecondes) && (dateObservationEnSecondes <= ephe2.heureEnSeconde()));
		if (!isValid)
			throw new NavException("Date observation pas entre les deux bornes");
		
		double gradiant = (ephe2.declinaison().asDegre() - ephe1.declinaison().asDegre()) / (ephe2.heureEnSeconde() - ephe1.heureEnSeconde());
	
		double interpolation = gradiant * dateObservationEnSecondes + ephe1.declinaison().asDegre();
		Declinaison retour = DeclinaisonFactory.fromDegre(interpolation);
		
		return retour;
	}


	private Angle AngleHoraireLocal_AHL_LHA_ParInterval(double dateObservationEnSecondes) throws NavException {
		boolean isValid = ((ephe1.heureEnSeconde() <= dateObservationEnSecondes) && (dateObservationEnSecondes <= ephe2.heureEnSeconde()));
		if (!isValid)
			throw new NavException("Date observation pas entre les deux bornes");
		
		double gradiant = (ephe2.GHA().asDegre() - ephe1.GHA().asDegre()) /  (ephe2.heureEnSeconde() - ephe1.heureEnSeconde());
	
		double interpolation = gradiant * dateObservationEnSecondes + ephe1.GHA().asDegre();
		Angle retour = AngleFactory.fromDegre(interpolation);
		return retour;
	}

	
	@Override
	public String toString() {
		return "Ephemerides [ephe1=" + ephe1 + ", ephe2=" + ephe2 + ", type=" + type + "]";
	}
}
