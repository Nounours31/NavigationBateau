package sfa.nav.astro.calculs.correctionvisee.internal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.Ephemerides.EphemeridesType;
import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleOriente;
import sfa.nav.model.AngleOrienteFactory;
import sfa.nav.model.Declinaison;
import sfa.nav.model.DeclinaisonFactory;
import sfa.nav.model.NavDateHeure;
import sfa.nav.model.NavDateHeureFactory;

public class EphemerideParInterval extends Ephemeride {
	private static final Logger logger = LoggerFactory.getLogger(EphemerideParInterval.class);

	protected double HP_PI;

	public double HP_PI() {
		return HP_PI;
	}
		
	public EphemerideParInterval() {
		super(EphemeridesType.parInterval);
	}

	@Override
	public String toString() {
		return "Ephemerides [Astre= " + astre +", GHA=" + GHA + ", declinaison=" + declinaison + ", heureEnSeconde=" + heureDeRef + ",  type=" + type + "]";
	}


	public EphemerideParInterval heureDeRef(NavDateHeure _heureDeRef) {
		heureDeRef = _heureDeRef;
		return this;
	}
	public EphemerideParInterval  declinaison(Declinaison _declinaison) {
		declinaison = _declinaison;
		return this;
	}
	public EphemerideParInterval  GHA(Angle _GHA) {
		GHA = _GHA;
		return this;
	}

	public EphemerideParInterval  HP_PI(double _HP_PI) {
		HP_PI = _HP_PI;
		return this;
	}
	public EphemerideParInterval  astre(String _astre) {
		astre = _astre;
		return this;
	}


	@Override
	public Declinaison declinaison(List<Ephemeride> lEphe, NavDateHeure heureObservation) {
		EphemerideParInterval ephe1 = (EphemerideParInterval) lEphe.get(0); 
		EphemerideParInterval ephe2 = (EphemerideParInterval) lEphe.get(1); 

		boolean isValid = ((ephe1.heureDeRef().avant(heureObservation)) && (heureObservation.avant(ephe2.heureDeRef())));
		if (!isValid)
			return null;
		
		double gradiantDegreParHeure = getGradiantDeclinaisonEnDegreParHeure(ephe1, ephe2);		
		double dureeEnHeure = getDureeEnHeure(heureObservation, ephe1);
		
		double interpolation = gradiantDegreParHeure * dureeEnHeure + ephe1.declinaison().asDegre();
		Declinaison retour = DeclinaisonFactory.fromDegreSafe (interpolation);
		
		return retour;
	}

	@Override
	public AngleOriente AngleHoraireLocal_AHL_LHA(List<Ephemeride> lEphe, NavDateHeure dateObservation) {
		EphemerideParInterval ephe1 = (EphemerideParInterval) lEphe.get(0); 
		EphemerideParInterval ephe2 = (EphemerideParInterval) lEphe.get(1); 

		boolean isValid = ((ephe1.heureDeRef().avant(dateObservation)) && (dateObservation.avant(ephe2.heureDeRef())));
		if (!isValid)
			return null;
		
		double gradiantDegreParHeure = getGradiantLongitudeEnDegreParHeure(ephe1, ephe2);	
		double dureeEnHeure = getDureeEnHeure(dateObservation, ephe1);
		
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
	



	@Override
	public double Pi(ArrayList<Ephemeride> lEphe, NavDateHeure heureObservation) throws NavException {
		EphemerideParInterval ephe1 = (EphemerideParInterval) lEphe.get(0); 
		EphemerideParInterval ephe2 = (EphemerideParInterval) lEphe.get(1); 
		if ((ephe1 != null) && (ephe1.HP_PI() == 0.0))
			return 0.0;
		
		boolean isValid = ((ephe1.heureDeRef().avant(heureObservation)) && (heureObservation.avant(ephe2.heureDeRef())));
		if (!isValid)
			throw new NavException("Interval invalide");

		
		double gradiantParHeure = (ephe2.HP_PI() - ephe1.HP_PI()) / (ephe2.heureDeRef().asHeureDecimaleEpoch() - ephe1.heureDeRef().asHeureDecimaleEpoch());

		double dureeEnHeure = getDureeEnHeure(heureObservation, ephe1);
		double interpolation = gradiantParHeure * dureeEnHeure + ephe1.HP_PI();	
		return interpolation;
	}

	@Override
	public String toCanevas(ArrayList<Ephemeride> lEphe, NavDateHeure dateObservation, boolean isDeclinaison, boolean isLongitude, String offset,  DecimalFormat fmt) {
		EphemerideParInterval ephe1 = (EphemerideParInterval) lEphe.get(0); 
		EphemerideParInterval ephe2 = (EphemerideParInterval) lEphe.get(1); 
		StringBuffer sb = new StringBuffer();
		if (isDeclinaison) {
				sb.append(offset + "DECLINAISON par interval" + "\n");
				sb.append(offset + "   from .................... : " + ephe1.declinaison().toCanevas() + " à " + ephe1.heureDeRef() + "\n");
				sb.append(offset + "   to ...................... : " + ephe2.declinaison().toCanevas() + " à " + ephe2.heureDeRef() + "\n");	
				sb.append(offset + "   gradiant (Deg par Heure)  : " + getGradiantDeclinaisonEnDegreParHeure(ephe1, ephe2) + " °/h\n");
				sb.append(offset + "   Interval de temps (Dt en heure) : " + fmt.format(getDureeEnHeure(dateObservation, ephe1)) + " h\n");			
				sb.append(offset + "   ==> variation (°) Dt * Gradian  = " + getDureeEnHeure(dateObservation, ephe1) * getGradiantDeclinaisonEnDegreParHeure(ephe1, ephe2) + "°\n");			
				sb.append(offset + "   ==> resultat             : " + fmt.format(this.declinaison(lEphe, dateObservation).asDegre()) + "°\n");
			}
			if (isLongitude) {
				sb.append(offset + "LONGITUDE par interval :" + "\n");
				sb.append(offset + "   from .................... : " + ephe1.GHA().toCanevas() + " à " + ephe1.heureDeRef() + "\n");
				sb.append(offset + "   to ...................... : " + ephe2.GHA().toCanevas() + " à " + ephe2.heureDeRef() + "\n");
				sb.append(offset + "   gradiant (Deg par Heure)  : " + getGradiantLongitudeEnDegreParHeure(ephe1, ephe2) + "°/h\n");
				sb.append(offset + "   Interval de temps (Dt en heure) : " + fmt.format(getDureeEnHeure(dateObservation, ephe1)) + " h\n");			
				sb.append(offset + "   ==> variation (°) Dt * Gradian  = " + getDureeEnHeure(dateObservation, ephe1) * getGradiantLongitudeEnDegreParHeure(ephe1, ephe2) + "°\n");			
				sb.append(offset + "   ==> resultat             : " + fmt.format(this.AngleHoraireLocal_AHL_LHA(lEphe, dateObservation).asDegre()) + "°\n");
			}
			return sb.toString();
	}

	@Override
	public NavDateHeure getHeureGMTPassageGreenwich(ArrayList<Ephemeride> lEphe) {
		if (lEphe.size() < 2)
			return null;
		
		Ephemeride dernierAvant360 = lEphe.get(0);
		int indiceDernierAvant360 = 0;
		
		int i = 0;
		for (Ephemeride e : lEphe) {
			if ((e.GHA.asDegre() > dernierAvant360.GHA.asDegre()) && (e.GHA.asDegre() <= 360.0)) {
				dernierAvant360 = e;
				indiceDernierAvant360 = i;
			}
			i++;
		}
		
		// rien apres c'est foutu
		if (indiceDernierAvant360 >=  (lEphe.size() - 1))
			return null;
		
		double gradiantParHeure = 
				(360.0 -                                             lEphe.get(indiceDernierAvant360).GHA.asDegre()) / 
				(lEphe.get(indiceDernierAvant360+1).GHA.asDegre() -  lEphe.get(indiceDernierAvant360).GHA.asDegre());
		double dureeEnHeureDecimale = (lEphe.get(indiceDernierAvant360+1).heureDeRef.asHeureDecimaleEpoch() - lEphe.get(indiceDernierAvant360).heureDeRef.asHeureDecimaleEpoch());

		double interpolation = gradiantParHeure * dureeEnHeureDecimale + lEphe.get(indiceDernierAvant360).heureDeRef.asHeureDecimaleEpoch();	
		return NavDateHeureFactory.fromHeureDecimale(interpolation);
	}


	@Override
	public double getLongitudeVitesseDegreParHeure(ArrayList<Ephemeride> lEphe) {
		double gradiantParHeure = (lEphe.get(1).GHA.asDegre() - lEphe.get(0).GHA.asDegre()) /(lEphe.get(1).heureDeRef.asHeureDecimaleEpoch() - lEphe.get(0).heureDeRef.asHeureDecimaleEpoch());
		return gradiantParHeure;
	}
}