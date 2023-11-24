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
import sfa.nav.model.VitesseAngulaire;

public class EphemerideParGradiant extends Ephemeride {
	private static final Logger logger = LoggerFactory.getLogger(EphemerideParGradiant.class);
	private VitesseAngulaire varHA;
	private VitesseAngulaire varDecli; 

	public EphemerideParGradiant() {
		super(EphemeridesType.ParGradiant);
	}
	

	@Override
	public String toString() {
		return "Ephemerides [Astre= " + astre +", GHA=" + GHA + ", declinaison=" + declinaison + ", heureEnSeconde=" + heureDeRef + ", varHA=" + varHA
				+ ", varDecli=" + varDecli + ", type=" + type + "]";
	}

	public VitesseAngulaire variationHA() {
		return varHA;
	}
	public VitesseAngulaire variationDeclinaison() {
		return varDecli;
	}

	
	public EphemerideParGradiant heureDeRef(NavDateHeure _heureDeRef) {
		heureDeRef = _heureDeRef;
		return this;
	}
	public EphemerideParGradiant  declinaison(Declinaison _declinaison) {
		declinaison = _declinaison;
		return this;
	}
	public EphemerideParGradiant  GHA(Angle _GHA) {
		GHA = _GHA;
		return this;
	}
	public EphemerideParGradiant  variationHA(VitesseAngulaire _varHA) {
		varHA = _varHA;
		return this;
	}
	public EphemerideParGradiant  variationDeclinaison(VitesseAngulaire _varDecli) {
		varDecli = _varDecli;
		return this;
	}
	public EphemerideParGradiant  astre(String _astre) {
		astre = _astre;
		return this;
	}
	
	@Override
	public Declinaison declinaison(List<Ephemeride> ephe, NavDateHeure dateObservation)  {
		if (logger.isDebugEnabled()) {
			logger.debug("Observation: {} - Heure de ref {}", dateObservation, this.heureDeRef());
			logger.debug("Declinaison de ref: {}° - Variation {}°/h", this.declinaison().asDegre(), this.variationDeclinaison().asDegreParHeure());
		}
		
		double dureeEnHeure = getDureeEnHeure(dateObservation, this);
		double variationTotale = this.variationDeclinaison().asDegreParHeure() * dureeEnHeure;
		double interpolation = this.declinaison().asDegre() + variationTotale;
		if (logger.isDebugEnabled()) {
			logger.debug("duree en heure: {} - variation totale {} - interpolation {}", dureeEnHeure, variationTotale, interpolation);
		}
		
		Declinaison retour = null;
		try {
			retour = DeclinaisonFactory.fromDegre(interpolation);
		}
		catch(Exception e) {
			logger.error("declinaisonParVitesse ", e);
		}
		return retour;
	}

	@Override
	public AngleOriente AngleHoraireLocal_AHL_LHA(List<Ephemeride> ephe, NavDateHeure dateObservation) {
		if (logger.isDebugEnabled()) {
			logger.debug("Observation: {} - Heure de ref {}", dateObservation, this.heureDeRef());
		}
		
		
		double dureeEnHeure = getDureeEnHeure(dateObservation, this);
		double interpolation = this.GHA().asDegre() + this.variationHA().asDegreParHeure() * dureeEnHeure ;
		AngleOriente retour = AngleOrienteFactory.fromDegre(interpolation);
		return retour;
	}


	@Override
	public double Pi(ArrayList<Ephemeride> lEphe, NavDateHeure heureObservation) throws NavException {
		// throw new NavException("not implemented");
		// soyons generique ...
		return 0.0;
	}


	@Override
	public String toCanevas(ArrayList<Ephemeride> lEphe, NavDateHeure dateObservation, boolean isDeclinaison, boolean isLongitude, String offset, DecimalFormat fmt) {
		StringBuffer sb = new StringBuffer();
		if (isDeclinaison) {
			sb.append(offset + "DECLINAISON par gradiant :" + "\n");
			sb.append(offset + "    Origine................. = " + this.declinaison().toCanevas() + " à " + this.heureDeRef() + "\n");
			sb.append(offset + "    Vitesse angulaire....... = " + this.variationDeclinaison().toCanevas() + "\n");
			sb.append(offset + "    Interval de temps (Dt en heure)... : " + fmt.format(getDureeEnHeure(dateObservation, this)) + " h\n");			
			sb.append(offset + "    ==> variation (°) Dt * V = " + fmt.format(getDureeEnHeure(dateObservation, this) * this.variationDeclinaison().asDegreParHeure()) + "°\n");
			sb.append(offset + "    ==> resultat             : " + fmt.format(this.declinaison(null, dateObservation).asDegre()) + "°\n");
		}
		if (isLongitude) {
			sb.append(offset + "LONGITUDE par gradiant :" + "\n");
			sb.append(offset + "    Origine................. = " + this.GHA().toCanevas() + " à " + this.heureDeRef() + "\n");
			sb.append(offset + "    Vitesse angulaire....... = " + this.variationHA().toCanevas() + "\n");
			sb.append(offset + "    Interval de temps (Dt en heure)... : " + fmt.format(getDureeEnHeure(dateObservation, this)) + " h\n");			
			sb.append(offset + "    ==> variation (°) Dt * V = " + fmt.format(getDureeEnHeure(dateObservation, this) * this.variationHA().asDegreParHeure()) + "°\n");
			sb.append(offset + "    ==> resultat             : " + fmt.format(this.AngleHoraireLocal_AHL_LHA(null, dateObservation).asDegre()) + "°\n");
		}
		return sb.toString();
	}


	@Override
	public NavDateHeure getHeureGMTPassageGreenwich(ArrayList<Ephemeride> lEphe) {
		EphemerideParGradiant self = (EphemerideParGradiant)lEphe.get(0);
		double variationAngulaireEnDegre = 360.0 - self.GHA.asDegre();
		double intervalHoraire = variationAngulaireEnDegre / self.varHA.asDegreParHeure();

		NavDateHeure heureGMTPassageGreenwich = self.heureDeRef.plusHeureDecimale(intervalHoraire);
		return (heureGMTPassageGreenwich);		
	}


	@Override
	public double getLongitudeVitesseDegreParHeure(ArrayList<Ephemeride> lEphe) {
		EphemerideParGradiant self = (EphemerideParGradiant)lEphe.get(0);
		return self.varHA.asDegreParHeure();
	}
}