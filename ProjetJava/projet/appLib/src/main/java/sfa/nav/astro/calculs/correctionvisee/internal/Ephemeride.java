package sfa.nav.astro.calculs.correctionvisee.internal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import sfa.nav.astro.calculs.Ephemerides.EphemeridesType;
import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleOriente;
import sfa.nav.model.Declinaison;
import sfa.nav.model.NavDateHeure;

public abstract class Ephemeride {
	protected Angle GHA; 
	protected Declinaison declinaison; 	
	protected NavDateHeure heureDeRef;
		
	protected String astre;
	protected final EphemeridesType type;

	
	public Ephemeride(EphemeridesType _type) {
		type = _type;
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

	public String astre() {
		return astre;
	}

	protected double getDureeEnHeure(NavDateHeure dateObservation, Ephemeride eph) {
		double dureeEnHeure = (dateObservation.asHeureDecimaleEpoch() - eph.heureDeRef().asHeureDecimaleEpoch());
		return dureeEnHeure;
	}

	
	public abstract Declinaison declinaison(List<Ephemeride> lEphe, NavDateHeure heureObservation);
	public abstract AngleOriente AngleHoraireLocal_AHL_LHA(List<Ephemeride> lEphe, NavDateHeure dateObservation);
	public abstract double Pi(ArrayList<Ephemeride> lEphe, NavDateHeure heureObservation) throws NavException;
	public abstract String toCanevas(ArrayList<Ephemeride> lEphe, NavDateHeure dateObservation, boolean isDeclinaison, boolean isLongitude, String offset, DecimalFormat fmt);
	public abstract NavDateHeure getHeureGMTPassageGreenwich(ArrayList<Ephemeride> lEphe);

	public abstract double getLongitudeVitesseDegreParHeure(ArrayList<Ephemeride> lEphe);

}