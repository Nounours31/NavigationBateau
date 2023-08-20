package sfa.nav.astro.calculs;

import sfa.nav.model.Angle;
import sfa.nav.model.Declinaison;
import sfa.nav.model.VitesseAngulaire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ephemerides {
	private static final Logger logger = LoggerFactory.getLogger(Ephemerides.class);
	
	public enum EphemerideType {
		ParGradiant, parInterval;
	}
	
	private Angle GHA; 
	private Declinaison declinaison; 
	
	private double heure;
	
	private VitesseAngulaire varHA;
	private VitesseAngulaire varDecli; 
	
	final private EphemerideType type; 
	
	public Ephemerides(Angle _GHA, Declinaison _dec, double _heureRef) {
		GHA = _GHA;
		declinaison = _dec;
		heure = _heureRef;
		varHA = null;
		varDecli = null;
		type = EphemerideType.parInterval;
	}

	public Ephemerides(Angle _GHA, VitesseAngulaire _varHA, Declinaison _dec, VitesseAngulaire _varDecli, double _heureRef) {
		GHA = _GHA;
		declinaison = _dec;
		heure = _heureRef;
		varHA = _varHA;
		varDecli = _varDecli;
		type = EphemerideType.ParGradiant;
	}

	public Angle GHA() {
		return GHA;
	}

	public Angle declinaison() {
		return declinaison;
	}


	@Override
	public String toString() {
		return "Ephemerides [GHA=" + GHA + ", declinaison=" + declinaison + ", heure=" + heure + ", varHA=" + varHA
				+ ", varDecli=" + varDecli + ", type=" + type + "]";
	}

	public double heure() {
		return heure;
	}

	public VitesseAngulaire variationDeclinaison() {
		return varDecli;
	}

	public VitesseAngulaire variationHA() {
		return varHA;
	}

	public EphemerideType type() {
		return type;
	}
	

}
