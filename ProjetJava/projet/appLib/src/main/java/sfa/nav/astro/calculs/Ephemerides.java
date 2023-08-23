package sfa.nav.astro.calculs;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;
import sfa.nav.model.AngleOriente;
import sfa.nav.model.AngleOrienteFactory;
import sfa.nav.model.Declinaison;
import sfa.nav.model.DeclinaisonFactory;
import sfa.nav.model.NavDateHeure;
import sfa.nav.model.VitesseAngulaire;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ephemerides {
	private static final Logger logger = LoggerFactory.getLogger(Ephemerides.class);
	
	public enum EphemeridesType {
		ParGradiant, parInterval;
	}
	
	// --------------------------------------------
	// Une point des ephemerides
	// --------------------------------------------
	private class Ephemeride {
		private Angle GHA; 
		private Declinaison declinaison; 
		
		private NavDateHeure heureDeRef;
		
		private VitesseAngulaire varHA;
		private VitesseAngulaire varDecli; 

		public Ephemeride(Angle _GHA, Declinaison _dec, NavDateHeure _heureRef) {
			GHA = _GHA;
			declinaison = _dec;
			heureDeRef = _heureRef;
			varHA = null;
			varDecli = null;			
		}
		public Ephemeride(Angle _GHA, VitesseAngulaire _varHA, Declinaison _dec, VitesseAngulaire _varDecli, NavDateHeure _heureRef) {
			GHA = _GHA;
			declinaison = _dec;
			heureDeRef = _heureRef;
			varHA = _varHA;
			varDecli = _varDecli;			
		}

		@Override
		public String toString() {
			return "Ephemerides [GHA=" + GHA + ", declinaison=" + declinaison + ", heureEnSeconde=" + heureDeRef + ", varHA=" + varHA
					+ ", varDecli=" + varDecli + ", type=" + type + "]";
		}

		public NavDateHeure heureDeRef() {
			return heureDeRef;
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
	
	public Ephemerides(Angle _GHA, Declinaison _dec, NavDateHeure _heureRef, Angle _GHA2, Declinaison _dec2, NavDateHeure _heureRef2) {
		ephe1 = new Ephemeride(_GHA, _dec, _heureRef);
		ephe2 = new Ephemeride(_GHA2, _dec2, _heureRef2);
		type = EphemeridesType.parInterval;
	}

	public Ephemerides(Angle _GHA, VitesseAngulaire _varHA, Declinaison _dec, VitesseAngulaire _varDecli, NavDateHeure _heureRef) {
		ephe1 = new Ephemeride(_GHA, _varHA, _dec, _varDecli, _heureRef);
		ephe2 = null;
		type = EphemeridesType.ParGradiant;
	}

	
	
	public Declinaison declinaison(NavDateHeure heureObservation) throws NavException {
		if (type == EphemeridesType.parInterval)  
				return declinaisonParInterval(heureObservation); 
		return declinaisonParVitesse(heureObservation);
	}
	
	public AngleOriente AngleHoraireLocal_AHL_LHA(NavDateHeure heureObservation) throws NavException {
		if (type == EphemeridesType.parInterval)  
				return AngleHoraireLocal_AHL_LHA_ParInterval(heureObservation);
		return AngleHoraireLocal_AHL_LHA_ParVitesse(heureObservation);
	}
	

	private Declinaison declinaisonParVitesse(NavDateHeure dateObservation) throws NavException {
		if (logger.isDebugEnabled()) {
			logger.debug("Observation: {} - Heure de ref {}", dateObservation, ephe1.heureDeRef());
		}
		
		double dureeEnHeure = (dateObservation.asHeureDecimaleEpoch() - ephe1.heureDeRef().asHeureDecimaleEpoch());
		double interpolation = ephe1.declinaison().asDegre() + ephe1.variationDeclinaison().asDegreParHeure() * dureeEnHeure;
		Declinaison retour = DeclinaisonFactory.fromDegre(interpolation);
		return retour;
	}

	private AngleOriente AngleHoraireLocal_AHL_LHA_ParVitesse(NavDateHeure dateObservation) {
		if (logger.isDebugEnabled()) {
			logger.debug("Observation: {} - Heure de ref {}", dateObservation, ephe1.heureDeRef());
		}
		
		double dureeEnHeure = (dateObservation.asHeureDecimaleEpoch() - ephe1.heureDeRef().asHeureDecimaleEpoch());
		double interpolation = ephe1.GHA().asDegre() + ephe1.variationHA().asDegreParHeure() * dureeEnHeure ;
		AngleOriente retour = AngleOrienteFactory.fromDegre(interpolation);
		return retour;
	}


	private Declinaison declinaisonParInterval(NavDateHeure dateObservation) throws NavException {
		boolean isValid = ((ephe1.heureDeRef().avant(dateObservation)) && (dateObservation.avant(ephe2.heureDeRef())));
		if (!isValid)
			throw new NavException("Date observation pas entre les deux bornes");
		
		double gradiantDegreParHeure = (ephe2.declinaison().asDegre() - ephe1.declinaison().asDegre()) / (ephe2.heureDeRef().asHeureDecimaleEpoch() - ephe1.heureDeRef().asHeureDecimaleEpoch());
	
		double dureeEnHeure = (dateObservation.asHeureDecimaleEpoch() - ephe1.heureDeRef().asHeureDecimaleEpoch());
		double interpolation = gradiantDegreParHeure * dureeEnHeure + ephe1.declinaison().asDegre();
		Declinaison retour = DeclinaisonFactory.fromDegre(interpolation);
		
		return retour;
	}


	private AngleOriente AngleHoraireLocal_AHL_LHA_ParInterval(NavDateHeure dateObservationEnSecondes) throws NavException {
		boolean isValid = ((ephe1.heureDeRef().avant(dateObservationEnSecondes)) && (dateObservationEnSecondes.avant(ephe2.heureDeRef())));
		if (!isValid)
			throw new NavException("Date observation pas entre les deux bornes");
		
		double gradiantDegreParHeure = (ephe2.GHA().asDegre() - ephe1.GHA().asDegre()) /  (ephe2.heureDeRef().asHeureDecimaleEpoch() - ephe1.heureDeRef().asHeureDecimaleEpoch());
	
		double dureeEnHeure = (dateObservationEnSecondes.asHeureDecimaleEpoch() - ephe1.heureDeRef().asHeureDecimaleEpoch());
		double interpolation = gradiantDegreParHeure * (dureeEnHeure) + ephe1.GHA().asDegre();
		AngleOriente retour = AngleOrienteFactory.fromDegre(interpolation);
		return retour;
	}

	
	@Override
	public String toString() {
		return "Ephemerides [ephe1=" + ephe1 + ", ephe2=" + ephe2 + ", type=" + type + "]";
	}
}
