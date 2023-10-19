package sfa.nav.astro.calculs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.correctionvisee.internal.Ephemeride;
import sfa.nav.astro.calculs.correctionvisee.internal.EphemerideFactory;
import sfa.nav.astro.calculs.correctionvisee.internal.EphemerideParGradiant;
import sfa.nav.astro.calculs.correctionvisee.internal.EphemerideParInterval;
import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleOriente;
import sfa.nav.model.Declinaison;
import sfa.nav.model.NavDateHeure;
import sfa.nav.model.VitesseAngulaire;

public class Ephemerides {
	private static final Logger logger = LoggerFactory.getLogger(Ephemerides.class);
	
	public enum EphemeridesType {
		ParGradiant, parInterval;
	}
	
	// --------------------------------------------
	// Une point des ephemerides
	// --------------------------------------------
	final private ArrayList<Ephemeride> _lEphe;
	final private EphemeridesType type; 
	
	public Ephemerides(String astre, Angle _GHA, Declinaison _dec, double piLune, NavDateHeure _heureRef, Angle _GHA2, Declinaison _dec2, double piLune2, NavDateHeure _heureRef2) {
		type = EphemeridesType.parInterval;
		_lEphe = new  ArrayList<Ephemeride>();
		
		EphemerideParInterval ephe = (EphemerideParInterval)EphemerideFactory.getEphereride(type);
		ephe.astre(astre).GHA(_GHA).declinaison(_dec).HP_PI(piLune).heureDeRef(_heureRef);
		_lEphe.add(ephe);
		
		ephe = (EphemerideParInterval)EphemerideFactory.getEphereride(type);
		ephe.astre(astre).GHA(_GHA2).declinaison(_dec2).HP_PI(piLune2).heureDeRef(_heureRef2);
		_lEphe.add(ephe);
	}

	public Ephemerides(String astre, Angle _GHA, VitesseAngulaire _varHA, Declinaison _dec, VitesseAngulaire _varDecli, NavDateHeure _heureRef) {
		type = EphemeridesType.ParGradiant;
		_lEphe = new  ArrayList<Ephemeride>();
		
		EphemerideParGradiant ephe = (EphemerideParGradiant)EphemerideFactory.getEphereride(type);
		ephe.astre(astre).GHA(_GHA).variationHA(_varHA).declinaison(_dec).variationDeclinaison(_varDecli).heureDeRef(_heureRef);
		_lEphe.add(ephe);
	}

	
	
	public Declinaison declinaison(NavDateHeure heureObservation)  {
		ArrayList<Ephemeride> lEphe = new ArrayList<Ephemeride>(List.of(_lEphe.get(0), (_lEphe.size() > 1 ? _lEphe.get(1) : _lEphe.get(0))));
		Ephemeride self = _lEphe.get(0);
		return self.declinaison(lEphe, heureObservation);
	}
	
	public AngleOriente AngleHoraireAHeureObservation(NavDateHeure heureObservation)  {
		ArrayList<Ephemeride> lEphe = new ArrayList<Ephemeride>(List.of(_lEphe.get(0), (_lEphe.size() > 1 ? _lEphe.get(1) : _lEphe.get(0))));
		Ephemeride self = _lEphe.get(0);
		return self.AngleHoraireLocal_AHL_LHA(lEphe, heureObservation);
	}
	
	public NavDateHeure getHeureGMTPassageGreenwich() {
		Ephemeride self = _lEphe.get(0);
		return self.getHeureGMTPassageGreenwich(_lEphe);
	}


	
	@Override
	public String toString() {
		Ephemeride self = _lEphe.get(0);
		return "Ephemerides [ephe1=" + _lEphe.get(0) + ", ephe2=" + (_lEphe.size() > 1 ? _lEphe.get(1).toString() : "null") + ", type=" + type + "]";
	}

	public double pi(NavDateHeure heureObservation) throws NavException {
		ArrayList<Ephemeride> lEphe = new ArrayList<Ephemeride>(List.of(_lEphe.get(0), (_lEphe.size() > 1 ? _lEphe.get(1) : _lEphe.get(0))));
		Ephemeride self = _lEphe.get(0);
		return self.Pi(lEphe, heureObservation);
	}


	public String toCanevas(NavDateHeure dateObservation, boolean isDeclinaison, boolean isLongitude, String offset, DecimalFormat fmt) {
		StringBuffer sb = new StringBuffer();
		Ephemeride self = _lEphe.get(0);

		sb.append(offset + "Astre....................... = " + _lEphe.get(0).astre() + "\n");
		ArrayList<Ephemeride> lEphe = new ArrayList<Ephemeride>(List.of(_lEphe.get(0), (_lEphe.size() > 1 ? _lEphe.get(1) : _lEphe.get(0))));
		sb.append(self.toCanevas(lEphe, dateObservation,  isDeclinaison,  isLongitude,  offset, fmt));
		return sb.toString();
	}

}
