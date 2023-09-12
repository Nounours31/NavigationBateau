package sfa.nav.astro.calculs;

import java.nio.charset.StandardCharsets;

import sfa.nav.astro.calculs.CorrectionDeVisee.ErreurSextan;
import sfa.nav.astro.calculs.CorrectionDeVisee_TableDeNavigation.eTypeVisee;
import sfa.nav.astro.calculs.DroiteDeHauteur.eSensIntercept;
import sfa.nav.astro.calculs.DroiteDeHauteur.eTypeDroiteHauteur;
import sfa.nav.model.Angle;
import sfa.nav.model.Declinaison;
import sfa.nav.model.Distance;
import sfa.nav.model.NavDateHeure;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.tools.ePointsCardinaux;

public class Canevas {
	final private eTypeDroiteHauteur type;

	private String  astre = "Soleil";
	private NavDateHeure heureVisee = null;

	private Ephemerides epheAstre = null;
	private Ephemerides ephePointVernal = null;

	private Declinaison declianaisonAstre = null;
	private PointGeographique positionEstimee = null;

	private Angle GHAPointVernal = null;
	private Angle GHA_AstreVersusPOintVernal = null;
	private Angle GHAAstre = null;
	private Angle LHAAstre = null;

	private Angle Hc = null;
	private Angle Hi = null;
	private Angle Hv = null;
	private CorrectionDeVisee cv = null;
	private eTypeVisee typeVisee = eTypeVisee.inconnues;
	public final eTypeVisee getTypeVisee() {
		return typeVisee;
	}


	public final void setTypeVisee(eTypeVisee typeVisee) {
		this.typeVisee = typeVisee;
	}


	private double hOeil = -9999.9;

	private Angle azimut = null;
	private ePointsCardinaux azimut_correction_latitudeestime = null;
	private double azimut_correction_LHA = -9999.9;
	private double azimut_correction_azimutBase = -9999.9;
	private double azimut_correction_azimutCorrige = -9999.9;

	private eSensIntercept sensIntercept = null;
	private Distance intercept = null;




	public Canevas(eTypeDroiteHauteur t) { type = t; }


	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("CanevasDroiteHauteur [\n");
		if (type == eTypeDroiteHauteur.etoile) 
			sb.append(toStringForDroiteEtoile());
		else if (type == eTypeDroiteHauteur.lune) 
			sb.append(toStringForDroiteLune());
		if (type == eTypeDroiteHauteur.planete) 
			sb.append(toStringForDroitePlanete());
		if (type == eTypeDroiteHauteur.soleil) 
			sb.append(toStringForDroiteSoleil());
		else 
			sb.append("type inconnu " + type);
		sb.append("\n]");
		return sb.toString();
	}

	private String toStringForDroiteSoleil() {
		StringBuffer canevas = new StringBuffer(); 
		canevas.append("-----------------------------------------" + "\n");
		canevas.append("|    CANEVAS DROITE DE HAUTEUR           |" + "\n");
		canevas.append("-----------------------------------------" + "\n");
		canevas.append("0- Info " + "\n");
		canevas.append("---------" + "\n");
		canevas.append("	a - Date Corrigee" + "\n");
		canevas.append("		" + (heureVisee == null? "NULL" : heureVisee) + "\n");
		canevas.append("	b - Astre" + "\n");
		canevas.append("		"+ astre + "\n");
		canevas.append("		Hi: " + (Hi == null? "NULL" : Hi.toCanevas()) + "\n");
		canevas.append("	c - Position estimee" + "\n");
		canevas.append("		" + (positionEstimee == null ? "NULL" : positionEstimee.toCanevas())+ "\n");
		canevas.append("	d - Erreur sextan et Oeil" + "\n");
		canevas.append("		" + (cv == null ? "NULL" : cv.forCanevas()) + "\n");
		canevas.append("		" + hOeil +" metre" + "\n\n");
		
		canevas.append("1- Declinaison\n");
		canevas.append("---------------\n");
		canevas.append("	Astre LHA =======>> " + (declianaisonAstre == null ? "NULL" : declianaisonAstre.toCanevas()) + "\n");
		canevas.append("	Element de calculs: \n" + 
				(epheAstre == null ? "NULL" : epheAstre.toCanevas(heureVisee, true, false, "\t\t")) + "\n");
		
		canevas.append("2- Angle Horaire Local" + "\n");
		canevas.append("-----------------------" + "\n");
		canevas.append("	LHA / GHA =======>> " +  (getLHAAstre() == null ? "NULL" : getLHAAstre().toCanevas()) + "\n");
		canevas.append("	Point vernal: " + (ephePointVernal == null ? "NULL" : ephePointVernal.AngleHoraireAHeureObservation(heureVisee).toCanevas()) + "\n");
		canevas.append("	   Element de calculs: \n" + 
				(ephePointVernal == null ? "NULL" : ephePointVernal.toCanevas(heureVisee, false, true,"\t\t\t")) + "\n");
		canevas.append("	Astre LHA: " + (epheAstre == null ? "NULL" : epheAstre.AngleHoraireAHeureObservation(heureVisee).toCanevas()) + "\n");
		canevas.append("	   Element de calculs: \n" + 
				(epheAstre == null ? "NULL" : epheAstre.toCanevas(heureVisee, false, true,"\t\t\t")) + "\n");
		canevas.append("	Longitude Estimée: " + (positionEstimee == null ? "NULL" : positionEstimee.longitude().toCanevas()) + " [+E / -W]\n");
		canevas.append("	Calcul =  Vernal + Astre + Longi = " + 
				(ephePointVernal == null ? "NULL" : ephePointVernal.AngleHoraireAHeureObservation(heureVisee).toCanevas()) + " + " +
				(epheAstre == null ? "NULL" : epheAstre.AngleHoraireAHeureObservation(heureVisee).toCanevas()) + " + " +
				(positionEstimee == null ? "NULL" : positionEstimee.longitude().toCanevas()) +  
				" = " + (getLHAAstre() == null ? "NULL" : getLHAAstre().toCanevas()) + "        "+getLHAAstre().toString()+"\n\n");
		
		canevas.append("3- Latitude Estimee" + "\n");
		canevas.append("--------------------" + "\n");
		canevas.append("	=======>> " + (positionEstimee == null ? "NULL" : positionEstimee.latitude().toCanevas()) + "\n\n");
		
		canevas.append("4- Hauteur calculee" + "\n");
		canevas.append("--------------------" + "\n");
		canevas.append("	=======>> " + Hc + "\n\n");
		
		canevas.append("5- Intercept" + "\n");
		canevas.append("-------------" + "\n");
		canevas.append("	=======>> " + (intercept == null ? "NULL" : intercept) + "\n\n");
		canevas.append("	Hi         : " +  (Hi == null? "NULL" : Hi.toCanevas()) + "\n");
		canevas.append("	Collimation: " + cv.getErr().toCanevas() + "\n");
		canevas.append("	Tables corrections: " + cv.correctionEnDegre(Hi, hOeil, heureVisee, typeVisee)+"\n" );
		canevas.append("	  Refraction hauteur oeil: " + cv.correctionReflexionHauteurOeil (Hi, hOeil)+"\n");
		canevas.append("	  Par type de visee : " + cv.correctionEnDegreTypeVisee (heureVisee, typeVisee)+"\n");
		canevas.append("	===>> Hv: " + (Hi.asDegre() + 
											cv.getErr().collimacon.asDegre() +  
											cv.correctionEnDegre(Hi, hOeil, heureVisee, typeVisee)) + "\n");
		canevas.append("	          Hv = Hi + Coll + Correction 1 + Correction 2 = " + 
											Hi.toCanevas() + " " + 
											cv.getErr().collimacon.toCanevas() + " " + 
											+ cv.correctionReflexionHauteurOeil (Hi, hOeil) + " " +
											+ cv.correctionEnDegreTypeVisee (heureVisee, typeVisee) + " " +
											"\n" );
		canevas.append("	===>> intercept: "+ intercept +" \n" );
		canevas.append("	===>> intercept: (Hv - Hc) * 60.0 = ("+ Hv.toCanevas() +" - " + Hc.toCanevas() + ") * 60.0 \n\n" );
		
		canevas.append("6- Azimut" + "\n");
		canevas.append("----------" + "\n");
		canevas.append("	=======>> " + (azimut == null ? "NULL" : azimut) + "\n");
		canevas.append("	AHL = xxx < 360° \n");
		
		return canevas.toString();
	}



	public final void setDeclianaisonAstre(Declinaison declianaisonAstre) {
		this.declianaisonAstre = declianaisonAstre;
	}

	public final void setHauteurOeil(double h) {
		this.hOeil = h;
	}

	public final void setHeurevisee(NavDateHeure dateVisee) {
		this.heureVisee = dateVisee;
	}


	public final void setPositionEstimee(PointGeographique positionEstimee) {
		this.positionEstimee = positionEstimee;
	}





	private String toStringForDroitePlanete() {
		return toStringForDroiteSoleil();
	}


	private String toStringForDroiteLune() {
		return toStringForDroiteSoleil();
	}


	private String toStringForDroiteEtoile() {
		return toStringForDroiteSoleil();
	}



	public final void setEpheAstre(Ephemerides epheAstre) {
		this.epheAstre = epheAstre;
	}




	public final void setEphePointVernal(Ephemerides ephePointVernal) {
		this.ephePointVernal = ephePointVernal;
	}




	public final void setGHAAstre(Angle lHAAstre) {
		LHAAstre = lHAAstre;
	}




	public final void setGHAPointVernal(Angle lHAPointVernal) {
		GHAPointVernal = lHAPointVernal;
	}


	public void setGHAAstreVsPointVernal(Angle aHL_LHA_AstreVersusPOintVernal) {
		GHA_AstreVersusPOintVernal = aHL_LHA_AstreVersusPOintVernal;
	}


	public void setLHAAstre(Angle lHA_LocalHoraireAngle) {
		LHAAstre = lHA_LocalHoraireAngle;
	}
	public Angle getLHAAstre() {
		return LHAAstre;
	}



	public final void setHc(Angle hc) {
		Hc = hc;
	}




	public final void setHi(Angle hi) {
		Hi = hi;
	}




	public final void setHv(Angle hv) {
		Hv = hv;
	}




	public final void setCv(CorrectionDeVisee cv2) {
		this.cv = cv2;
	}




	public final void setAzimut(Angle azimut) {
		this.azimut = azimut;
	}
	public void setAzimutCorrection(ePointsCardinaux sens, double LHA, double azimut_Z, double z) {
		this.azimut_correction_latitudeestime = sens;
		this.azimut_correction_LHA = LHA;
		this.azimut_correction_azimutBase = azimut_Z;		
		this.azimut_correction_azimutCorrige = z;		
	}




	public final void setSensIntercept(eSensIntercept sensIntercept) {
		this.sensIntercept = sensIntercept;
	}




	public final void setIntercept(Distance intercept) {
		this.intercept = intercept;
	}


	public void setAstre(String _astre) {
		astre = _astre;		
	}

}
