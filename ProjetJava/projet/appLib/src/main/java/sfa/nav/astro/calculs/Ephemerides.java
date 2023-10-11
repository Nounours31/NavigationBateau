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
		
		private double HP_PI;
		final private String astre;

		public Ephemeride(String _astre, Angle _GHA, Declinaison _dec, double _piLune, NavDateHeure _heureRef) {
			GHA = _GHA;
			declinaison = _dec;
			heureDeRef = _heureRef;
			varHA = null;
			varDecli = null;			
			HP_PI = _piLune;
			astre = _astre;
		}
		
		public Ephemeride(Angle _GHA, Declinaison _dec, NavDateHeure _heureRef) {
			this ("Soleil", _GHA, _dec, 0.0, _heureRef);
		}
		
		public Ephemeride(Angle _GHA, Declinaison _dec, double _piLune, NavDateHeure _heureRef) {
			this ("Lune", _GHA, _dec, _piLune, _heureRef);
		}

		public Ephemeride(String _astre, Angle _GHA, VitesseAngulaire _varHA, Declinaison _dec, VitesseAngulaire _varDecli, NavDateHeure _heureRef) {
			astre = _astre;
			GHA = _GHA;
			declinaison = _dec;
			heureDeRef = _heureRef;
			varHA = _varHA;
			varDecli = _varDecli;			
			HP_PI = 0.0;
		}

		public Ephemeride(Angle _GHA, VitesseAngulaire _varHA, Declinaison _dec, VitesseAngulaire _varDecli, NavDateHeure _heureRef) {
			this ("Soleil", _GHA, _varHA, _dec, _varDecli, _heureRef);
		}

		

		@Override
		public String toString() {
			return "Ephemerides [Astre= " + astre +", GHA=" + GHA + ", declinaison=" + declinaison + ", heureEnSeconde=" + heureDeRef + ", varHA=" + varHA
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
		public double HP_PI() {
			return HP_PI;
		}
		public String astre() {
			return astre;
		}
	}

	
	final private Ephemeride ephe1;
	final private Ephemeride ephe2;
	final private EphemeridesType type; 
	
	private Ephemerides(Angle _GHA, Declinaison _dec, double piLune, NavDateHeure _heureRef, Angle _GHA2, Declinaison _dec2, double piLune2, NavDateHeure _heureRef2) {
		this("Soleil", _GHA,  _dec,  piLune,  _heureRef,  _GHA2,  _dec2,  piLune2,  _heureRef2);
	}
	public Ephemerides(String astre, Angle _GHA, Declinaison _dec, double piLune, NavDateHeure _heureRef, Angle _GHA2, Declinaison _dec2, double piLune2, NavDateHeure _heureRef2) {
		ephe1 = new Ephemeride(astre, _GHA, _dec, piLune, _heureRef);
		ephe2 = new Ephemeride(astre, _GHA2, _dec2, piLune2, _heureRef2);
		type = EphemeridesType.parInterval;
	}

	private Ephemerides(Angle _GHA, VitesseAngulaire _varHA, Declinaison _dec, VitesseAngulaire _varDecli, NavDateHeure _heureRef) {
		this("Soleil",  _GHA, _varHA,  _dec,  _varDecli,  _heureRef);
	}

	public Ephemerides(String astre, Angle _GHA, VitesseAngulaire _varHA, Declinaison _dec, VitesseAngulaire _varDecli, NavDateHeure _heureRef) {
		ephe1 = new Ephemeride(astre, _GHA, _varHA, _dec, _varDecli, _heureRef);
		ephe2 = null;
		type = EphemeridesType.ParGradiant;
	}

	
	
	public Declinaison declinaison(NavDateHeure heureObservation) throws NavException {
		if (type == EphemeridesType.parInterval)  
				return declinaisonParInterval(heureObservation); 
		return declinaisonParVitesse(heureObservation);
	}
	
	public AngleOriente AngleHoraireAHeureObservation(NavDateHeure heureObservation)  {
		if (type == EphemeridesType.parInterval)  
				return AngleHoraireLocal_AHL_LHA_ParInterval(heureObservation);
		return AngleHoraireLocal_AHL_LHA_ParVitesse(heureObservation);
	}
	

	private Declinaison declinaisonParVitesse(NavDateHeure dateObservation) throws NavException {
		if (logger.isDebugEnabled()) {
			logger.debug("Observation: {} - Heure de ref {}", dateObservation, ephe1.heureDeRef());
			logger.debug("Declinaison de ref: {}° - Variation {}°/h", ephe1.declinaison().asDegre(), ephe1.variationDeclinaison().asDegreParHeure());
		}
		
		double dureeEnHeure = getDureeEnHeure(dateObservation, ephe1);
		double variationTotale = ephe1.variationDeclinaison().asDegreParHeure() * dureeEnHeure;
		double interpolation = ephe1.declinaison().asDegre() + variationTotale;
		if (logger.isDebugEnabled()) {
			logger.debug("duree en heure: {} - variation totale {} - interpolation {}", dureeEnHeure, variationTotale, interpolation);
		}
		Declinaison retour = DeclinaisonFactory.fromDegre(interpolation);
		return retour;
	}

	private AngleOriente AngleHoraireLocal_AHL_LHA_ParVitesse(NavDateHeure dateObservation) {
		if (logger.isDebugEnabled()) {
			logger.debug("Observation: {} - Heure de ref {}", dateObservation, ephe1.heureDeRef());
		}
		
		
		double dureeEnHeure = getDureeEnHeure(dateObservation, ephe1);
		double interpolation = ephe1.GHA().asDegre() + ephe1.variationHA().asDegreParHeure() * dureeEnHeure ;
		AngleOriente retour = AngleOrienteFactory.fromDegre(interpolation);
		return retour;
	}

	private Declinaison declinaisonParInterval(NavDateHeure dateObservation)  {
		boolean isValid = ((ephe1.heureDeRef().avant(dateObservation)) && (dateObservation.avant(ephe2.heureDeRef())));
		if (!isValid)
			return null;
		
		double gradiantDegreParHeure = getGradiantDeclinaisonEnDegreParHeure(ephe1, ephe2);		
		double dureeEnHeure = getDureeEnHeure(dateObservation, ephe1);
		
		double interpolation = gradiantDegreParHeure * dureeEnHeure + ephe1.declinaison().asDegre();
		Declinaison retour = DeclinaisonFactory.fromDegreSafe (interpolation);
		
		return retour;
	}


	private AngleOriente AngleHoraireLocal_AHL_LHA_ParInterval(NavDateHeure dateObservationEnSecondes)  {
		boolean isValid = ((ephe1.heureDeRef().avant(dateObservationEnSecondes)) && (dateObservationEnSecondes.avant(ephe2.heureDeRef())));
		if (!isValid)
			return null;
		
		double gradiantDegreParHeure = getGradiantLongitudeEnDegreParHeure(ephe1, ephe2);	
		double dureeEnHeure = getDureeEnHeure(dateObservationEnSecondes, ephe1);
		
		double interpolation = gradiantDegreParHeure * (dureeEnHeure) + ephe1.GHA().asDegre();
		AngleOriente retour = AngleOrienteFactory.fromDegre(interpolation);
		return retour;
	}

	private double getGradiantLongitudeEnDegreParHeure(Ephemeride d, Ephemeride f) {
		double gradiantDegreParHeure = (f.GHA().asDegre() - d.GHA().asDegre()) /  (f.heureDeRef().asHeureDecimaleEpoch() - d.heureDeRef().asHeureDecimaleEpoch());
		return gradiantDegreParHeure;
	}

	private double getGradiantDeclinaisonEnDegreParHeure(Ephemeride d, Ephemeride f) {
		double gradiantDegreParHeure = (f.declinaison().asDegre() - d.declinaison().asDegre()) /  (f.heureDeRef().asHeureDecimaleEpoch() - d.heureDeRef().asHeureDecimaleEpoch());
		return gradiantDegreParHeure;
	}
	
	private double getDureeEnHeure(NavDateHeure dateObservation, Ephemeride eph) {
		double dureeEnHeure = (dateObservation.asHeureDecimaleEpoch() - eph.heureDeRef().asHeureDecimaleEpoch());
		return dureeEnHeure;
	}



	
	@Override
	public String toString() {
		return "Ephemerides [ephe1=" + ephe1 + ", ephe2=" + ephe2 + ", type=" + type + "]";
	}

	public double pi(NavDateHeure dateObservation) throws NavException {
		if ((ephe1 != null) && (ephe1.HP_PI() == 0.0))
			return 0.0;
		
		boolean isValid = ((ephe1.heureDeRef().avant(dateObservation)) && (dateObservation.avant(ephe2.heureDeRef())));
		if (!isValid)
			throw new NavException("Date observation pas entre les deux bornes");
		
		double gradiantParHeure = (ephe2.HP_PI() - ephe1.HP_PI()) / (ephe2.heureDeRef().asHeureDecimaleEpoch() - ephe1.heureDeRef().asHeureDecimaleEpoch());
	
		double dureeEnHeure = getDureeEnHeure(dateObservation, ephe1);
		double interpolation = gradiantParHeure * dureeEnHeure + ephe1.HP_PI();
		return interpolation;
	}


	public String toCanevas(NavDateHeure dateObservation, boolean isDeclinaison, boolean isLongitude, String offset) {
		StringBuffer sb = new StringBuffer();
		sb.append(offset + "Astre....................... = " + this.ephe1.astre() + "\n");
		sb.append(offset + "Interval de temps (heure)... : " + getDureeEnHeure(dateObservation, ephe1) + "\n\n");			
		if (type == EphemeridesType.ParGradiant) {
			if (isDeclinaison) {
				sb.append(offset + "DECLINAISON par gradiant :" + "\n");
				sb.append(offset + "    Origine................. = " + this.ephe1.declinaison.toCanevas() + " à " + this.ephe1.heureDeRef + "\n");
				sb.append(offset + "    Vitesse angulaire....... = " + this.ephe1.varDecli.toCanevas() + "\n");
				sb.append(offset + "    ==> variation (°) Dt * V = " + getDureeEnHeure(dateObservation, ephe1) * this.ephe1.varDecli.asDegreParHeure() + "\n");
			}
			if (isLongitude) {
				sb.append(offset + "LONGITUDE par gradiant :" + "\n");
				sb.append(offset + "    Origine................. = " + this.ephe1.GHA.toCanevas() + " à " + this.ephe1.heureDeRef + "\n");
				sb.append(offset + "    Vitesse angulaire....... = " + this.ephe1.varHA.toCanevas() + "\n");
				sb.append(offset + "    ==> variation (°) Dt * V = " + getDureeEnHeure(dateObservation, ephe1) * this.ephe1.varHA.asDegreParHeure() + "\n");
			}
		}
		else if (type == EphemeridesType.parInterval) {
			if (isDeclinaison) {
				sb.append(offset + "DECLINAISON par interval" + "\n");
				sb.append(offset + "   from .................... : " + this.ephe1.declinaison.toCanevas() + " à " + this.ephe1.heureDeRef + "\n");
				sb.append(offset + "   to ...................... : " + this.ephe2.declinaison.toCanevas() + " à " + this.ephe2.heureDeRef + "\n");	
				sb.append(offset + "   gradiant (Deg par Heure)  : " + getGradiantDeclinaisonEnDegreParHeure(ephe1, ephe2) + "\n");
				sb.append(offset + "   ==> variation (°) Dt * V  = " + getDureeEnHeure(dateObservation, ephe1) * getGradiantDeclinaisonEnDegreParHeure(ephe1, ephe2) + "\n");			
			}
			if (isLongitude) {
				sb.append(offset + "LONGITUDE par interval :" + "\n");
				sb.append(offset + "   from .................... : " + this.ephe1.GHA.toCanevas() + " à " + this.ephe1.heureDeRef + "\n");
				sb.append(offset + "   to ...................... : " + this.ephe2.GHA.toCanevas() + " à " + this.ephe2.heureDeRef + "\n");
				sb.append(offset + "   gradiant (Deg par Heure)  : " + getGradiantLongitudeEnDegreParHeure(ephe1, ephe2) + "\n");
				sb.append(offset + "   ==> variation (°) Dt * V  = " + getDureeEnHeure(dateObservation, ephe1) * getGradiantLongitudeEnDegreParHeure(ephe1, ephe2) + "\n");			
			}
		}
		else {
			sb.append("type inconnu");
		}
		return sb.toString();
	}

}
