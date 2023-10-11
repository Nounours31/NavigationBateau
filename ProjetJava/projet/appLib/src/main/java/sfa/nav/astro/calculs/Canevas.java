package sfa.nav.astro.calculs;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.DroiteDeHauteur.eSensIntercept;
import sfa.nav.astro.calculs.DroiteDeHauteur.eTypeDroiteHauteur;
import sfa.nav.astro.calculs.correctionvisee.internal.CorrectionDeVisee.correctionDeViseeHandler;
import sfa.nav.astro.calculs.correctionvisee.internal.CorrectionDeVisee.eTypeVisee;
import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.Declinaison;
import sfa.nav.model.Distance;
import sfa.nav.model.NavDateHeure;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.tools.ePointsCardinaux;
import sfa.nav.pdf.PDF4DroiteDeHauteur;

public class Canevas {
	final static Logger logger = LoggerFactory.getLogger(Canevas.class);
	
	final private eTypeDroiteHauteur _type;
	eTypeDroiteHauteur typeDroiteHauteur() {return _type;}

	private String  _astre = "Soleil";
	String astre() {return _astre;}
	void astre(String x) { _astre = x;}
	
	private NavDateHeure _heureVisee = null;
	public NavDateHeure heureVisee() {return _heureVisee;}
	public void heureVisee(NavDateHeure x) { _heureVisee = x;}

	private double hauteurOeil = -9999.9;
	public double hauteurOeil() {return hauteurOeil;}
	public void hauteurOeil(double x) { hauteurOeil = x;}

	private PointGeographique _positionEstimee = null;
	public PointGeographique positionEstimee() {return _positionEstimee;}
	public void positionEstimee(PointGeographique x) { _positionEstimee = x;}

	private Ephemerides _epheAstre = null;
	public Ephemerides epheAstre() {return _epheAstre;}
	public void epheAstre(Ephemerides x) { _epheAstre = x;}

	private Ephemerides _ephePointVernal = null;
	public Ephemerides ephePointVernal() {return _ephePointVernal;}
	public void ephePointVernal(Ephemerides x) { _ephePointVernal = x;}

	private Angle _Hi = null;
	public Angle Hi() {return _Hi;}
	public void Hi(Angle x) { _Hi = x;}

	private Declinaison _declinaisonAstre = null;
	public Declinaison declinaisonAstre() {return _declinaisonAstre;}
	public void declinaisonAstre(Declinaison x) { _declinaisonAstre = x;}
	
	private Angle _angleHoraireLocalAstre = null;
	public Angle angleHoraireLocalAstre() {return _angleHoraireLocalAstre;}
	public void angleHoraireLocalAstre(Angle x) { _angleHoraireLocalAstre = x;}
	
	private Angle _Hc = null;
	public Angle Hc() {return _Hc;}
	public void Hc(Angle x) { _Hc = x;}

	private ICorrectionDeVisee _cv = null;
	public ICorrectionDeVisee cv() {return _cv; }
	public void cv(ICorrectionDeVisee x) { _cv = x;}

	private eTypeVisee _typeVisee = null;
	public eTypeVisee typeVisee() {return _typeVisee;}
	public void typeVisee(eTypeVisee x) { _typeVisee = x;}
	
	private Angle _Hv = null;
	public Angle Hv() {return _Hv;}
	public void Hv(Angle x) { _Hv = x;}

	private Angle _azimut = null;
	public Angle azimut() {return _azimut;}
	public void azimut(Angle x) { _azimut = x;}

	private Distance _intercept = null;
	public Distance intercept() {return _intercept;}
	public void intercept(Distance x) { _intercept = x;}

	private eSensIntercept _sensIntercept = null;
	public eSensIntercept sensIntercept() {return _sensIntercept;}
	public void sensIntercept(eSensIntercept x) { _sensIntercept = x;}

	private DroiteDeHauteur _droiteDeHauteur = null;
	public DroiteDeHauteur droiteDeHauteur() {return _droiteDeHauteur;}
	public void droiteDeHauteur(DroiteDeHauteur x) { _droiteDeHauteur = x;}




	public Canevas(eTypeDroiteHauteur t) { _type = t; }


	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("CanevasDroiteHauteur : \n");
		try {
			sb.append(myToString(true));
		}
		catch (Exception e) {
			logger.error("toString KO ...", e);
			sb.append(e.getMessage());
		}
		return sb.toString();
	}

	private String myToString(boolean withPDF) throws NoSuchMethodException {
		PDF4DroiteDeHauteur pdf = null;
		if (withPDF) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss.SSSZ");
			String id = sdf.format( new Date());
			pdf = new PDF4DroiteDeHauteur(id);
		}
		DecimalFormat fmt = new DecimalFormat("+##00.0000;-#");
		StringBuffer canevas = new StringBuffer(); 
		canevas.append(introductionEtInfoVisee(fmt));
		canevas.append(infoCalculDeclinaison(fmt));
		canevas.append(infoCalculAngleHoraireLocal(fmt));
		canevas.append(infoCalculHauteurLatitudeEstimee());
		canevas.append(infoCalculHauteurCalculee());
		canevas.append(infoCalculIntercept(fmt));
		canevas.append(infoCalculAzimut(fmt));
		
		pdf.addTextSansTitre(0, canevas.toString());
		pdf.generePDF();
		
		return canevas.toString();
	}
	
	

	private String introductionEtInfoVisee(DecimalFormat fmt) {
		StringBuffer canevas = new StringBuffer();
		canevas.append(
		"----------------------------------------- \n" + 
		"|    CANEVAS DROITE DE HAUTEUR   "+ typeDroiteHauteur()  + "\n" +
		"----------------------------------------- \n"+
		"0- Info \n"+
		"	a - Date / heure de la visee \n"+
		"		  " + heureVisee() + "\n"+
		"	b - Astre \n"+
		"		  "+ astre() + "\n"+
		"		   Hi .................: " + fmt.format(Hi().asDegre()) + "° \n"+
		"		   Ha [Hi + I - dip]: " + fmt.format(cv().HaFromHi_EnDegre(Hi().asDegre(), hauteurOeil)) + "° \n"+
		"	c - Position estimee \n"+
		"		  " + positionEstimee().toCanevas() + "\n"+
		"	d - Erreur sextan et Oeil \n"+
		"		  collimation : " + fmt.format(cv().correctionSextan_EnMinuteArc()) + "' \n"+
		"		  Dip ........: " + fmt.format(cv().correctionHauteurOeilMetre_EnMinuteArc(hauteurOeil())) + "' \n"+
		"		  Hauteur oeil: " + fmt.format(hauteurOeil()) + " metre \n");
		return canevas.toString();
	}

	private String infoCalculDeclinaison(DecimalFormat fmt) {
		StringBuffer canevas = new StringBuffer();
		boolean dumpDeclinaison = true;
		boolean dumpLongitude = false;
		canevas.append(
				"\n"
				+ "1- Declinaison \n" 
				+ "	   Astre LHA =======>> " + fmt.format(declinaisonAstre().asDegre())+"° <<====\n"
				+ "	   Element de calculs: \n" + 
				epheAstre().toCanevas(heureVisee(), dumpDeclinaison, dumpLongitude, "\t\t", fmt) + 
				"\n");
		return canevas.toString();
	}
	

	private String infoCalculAngleHoraireLocal(DecimalFormat fmt) {
		StringBuffer canevas = new StringBuffer(); 
		String mode = "";
		if (typeDroiteHauteur() == eTypeDroiteHauteur.etoile) 
			mode = ">> ETOILE <<";
		
			canevas.append("\n"
				+ "2- Angle Horaire Local "+ mode +" \n"
				+ "		LHA  =======>> " +  angleHoraireLocalAstre().toCanevas() + "      ["+fmt.format(angleHoraireLocalAstre().asDegre())+"°]" + "\n");
		if (typeDroiteHauteur() == eTypeDroiteHauteur.etoile) {
			canevas.append(infoCalculAngleHoraireLocal_Etoile(fmt)); 
		}
		else {
			canevas.append(infoCalculAngleHoraireLocal_Autre(fmt)); 			
		}
		return canevas.toString();
	}
	
	private String infoCalculAngleHoraireLocal_Etoile(DecimalFormat fmt) {
		StringBuffer canevas = new StringBuffer(); 
		boolean dumpDeclinaison = false;
		boolean dumpLongitude = true;
		canevas.append("\n	a- Point vernal: \n");
		canevas.append("	   Element de calculs: \n" 
				+ "\t\tGHA 'Aries': =====>" + ephePointVernal().AngleHoraireAHeureObservation(heureVisee()).toCanevas() + "<==\n"
				+ ephePointVernal().toCanevas(heureVisee(), dumpDeclinaison, dumpLongitude,"\t\t", fmt) + "\n");

		canevas.append("\t\tAstre SHA:  =====> " + epheAstre().AngleHoraireAHeureObservation(heureVisee()).toCanevas() + "<==\n"
				+ epheAstre().toCanevas(heureVisee(), dumpDeclinaison, dumpLongitude,"\t\t", fmt) + "\n");
		
		canevas.append("\t\tLongitude Estimee: " + 
				positionEstimee().longitude().toCanevasLong() + "\n");

		canevas.append("\t\tCalcul =  Vernal + Astre + Longi \n"
				+ "\t\t\t =  " 
				+ "Verval[" + fmt.format(ephePointVernal().AngleHoraireAHeureObservation(heureVisee()).asDegre()) + "] "
				+ "+  Astre[" + fmt.format(epheAstre().AngleHoraireAHeureObservation(heureVisee()).asDegre()) + "] "
				+ "+  Longi[" + fmt.format(positionEstimee().longitude().asDegre()) + "]\n");
		canevas.append("\t\t\t  =  " + angleHoraireLocalAstre().toCanevas() + "\n\n");
		return canevas.toString();
	}
	
	private String infoCalculAngleHoraireLocal_Autre(DecimalFormat fmt) {
		StringBuffer canevas = new StringBuffer(); 
		canevas.append("	Astre GHA: " + (epheAstre() == null ? "NULL" : epheAstre().AngleHoraireAHeureObservation(heureVisee()).toCanevas()) + "\n");
		canevas.append("	   Element de calculs: \n" + 
				(epheAstre() == null ? "NULL" : epheAstre().toCanevas(heureVisee(), false, true,"\t\t\t", fmt)) + "\n");
		canevas.append("	Longitude Estimée: " + (positionEstimee() == null ? "NULL" : positionEstimee().longitude().toCanevasLong()) + "\n");
		canevas.append("	Calcul = Astre + Longi  \n"); 
		canevas.append("	       = " + 
				"Astre[ " + (epheAstre() == null ? "NULL" : epheAstre().AngleHoraireAHeureObservation(heureVisee()).toCanevas()) + "] + Longi[" +
				(positionEstimee() == null ? "NULL" : positionEstimee().longitude().toCanevas()) + "]\n"); 
		canevas.append("	       = " + 
				(angleHoraireLocalAstre() == null ? "NULL" : angleHoraireLocalAstre().toCanevas()) +"\n\n");
		return canevas.toString();
	}

	private String infoCalculHauteurLatitudeEstimee() {
		StringBuffer canevas = new StringBuffer(); 
		canevas.append("3- Latitude Estimee" + "\n");
		canevas.append("	=======>> Lat. estimee :" + (positionEstimee() == null ? "NULL" : positionEstimee().latitude().toCanevas()) + "\n\n");
		return canevas.toString();
	}

	private String infoCalculHauteurCalculee() {
		StringBuffer canevas = new StringBuffer();
		canevas.append("4- Hauteur calculee" + "\n");
		canevas.append("	=======>> Hc: " + Hc() + "\n\n");
		return canevas.toString();
	}

	private String infoCalculIntercept(DecimalFormat fmt) throws NoSuchMethodException {
		StringBuffer canevas = new StringBuffer();
		canevas.append("5- Intercept" + "\n");
		canevas.append("\t=======>> I: " + (intercept() == null ? "NULL" : intercept()) + "\n\n");
		canevas.append("\tHi............................................. : " +  (Hi() == null? "NULL" : Hi().toCanevas()) + "       [" + fmt.format (Hi().asDegre())+ "°]\n");
		canevas.append(infoCalculIntercept_Correction(fmt));
			
		canevas.append("\tIntercept: "+ intercept() +" \n" );
		canevas.append("\t\tintercept: (Hv - Hc) * 60.0 = ("+ fmt.format(Hv().asDegre()) +" - (" + fmt.format(Hc().asDegre()) + ")) * 60.0 = ("+ fmt.format(Hv().asDegre() - Hc().asDegre()) + ") * 60.0  = "+ fmt.format((Hv().asDegre() - Hc().asDegre()) * 60.0) + " Mn \n" );
		canevas.append("\t\t\t" + (intercept().distanceInMilleNautique() > 0 ? " I > 0 " : "I < 0 ") +
				"  sens: " +  sensIntercept()  + "\n\n");
		return canevas.toString();
	}




	private String infoCalculIntercept_Correction(DecimalFormat fmt) {
		StringBuffer canevas = new StringBuffer();
		double indiceRefraction_PI = 0.0;
		ICorrectionDeVisee cv = cv();
		Angle Hi = Hi();
		double hauteurOeil = hauteurOeil();
		double Ha = cv.HaFromHi_EnDegre(Hi.asDegre(), hauteurOeil);
		NavDateHeure	heureObservation = heureVisee();
		eTypeVisee t = typeVisee();
				
		try { 
			indiceRefraction_PI = epheAstre().pi(heureVisee()); 
		} catch (NavException e) {}
		
		double correction = cv.correctionTotale_EnDegre(Hi,hauteurOeil,heureObservation,indiceRefraction_PI, t);

		canevas.append("\t\tHa               : " + fmt.format(cv.HaFromHi_EnDegre(Hi().asDegre(), hauteurOeil())) + "°\n"
				+ "\t\tVisee              : " + typeVisee()+ "\n"
				+ "\t\tDiametre           : "+ fmt.format(cv.diametre_EnMinuteArc(heureObservation, indiceRefraction_PI)) + "'\n"
				+ "\t\tHP_PI              : "+ indiceRefraction_PI+ "'\n"
		+ "\t\tCorrection totale: "+ fmt.format(correction)+ "°   ["+fmt.format(correction*60.0)+"']\n"
		+ "\t\t--------------------------------------------"+ "'\n"
		+ "\t\tI: Correction correctionSextan_EnMinuteArc                 : "+ cv.correctionSextan_EnMinuteArc()+ "'\n"
		+ "\t\td: Correction correctionHauteurOeilMetre_EnMinuteArc       : "+ cv.correctionHauteurOeilMetre_EnMinuteArc(hauteurOeil)+ "'\n"
		+ "\t\tR: Correction correctionRefraction_EnMinuteArc             : "+ cv.correctionRefraction_EnMinuteArc(Ha)+ "'\n"
		+ "\t\tP: Correction correctionParallaxe_EnMinuteArc              : "+ cv.correctionParallaxe_EnMinuteArc(Ha, indiceRefraction_PI) + "'\n"
		+ "\t\t1/2D: Correction correctionAjoutSemiDiametreAstre_EnMinuteArc : "+ cv.correctionAjoutSemiDiametreAstre_EnMinuteArc(heureObservation, indiceRefraction_PI)+ "'\n"
		+ "\t\tVisee: Correction corrrectionTypeVisee_EnMinuteArc             : [ "+ t +" ]"+ cv.corrrectionTypeVisee_EnMinuteArc(t, heureObservation, indiceRefraction_PI)+ "'\n"
		+ "\t\t--------------------------------------------"+ "'\n");
		
		if ((t == eTypeVisee.luneBordInf) || (t == eTypeVisee.luneBordSup)) {
			canevas.append ("\t\t\tCorrection Sextan: [-I]                 "+ (-1.0 * cv.correctionSextan_EnMinuteArc())+ "'\n"
			+ "\t\t\tCorrection1 :      [-d]                 " + (-1.0 * cv.correctionHauteurOeilMetre_EnMinuteArc(hauteurOeil))+ "'\n"
			+ "\t\t\tCorrection2 :      [-R + P + 1/2 Diam]  " + fmt.format((-1.0 * cv.correctionRefraction_EnMinuteArc(Ha)
					+ cv.correctionParallaxe_EnMinuteArc(Ha, indiceRefraction_PI)
					+ cv.correctionAjoutSemiDiametreAstre_EnMinuteArc(heureObservation, indiceRefraction_PI)))+ "'\n" 
					+ "\t\t\tCorrection2 :      [Visee]              " + (cv.corrrectionTypeVisee_EnMinuteArc (t, heureObservation, indiceRefraction_PI))+ "'\n"
					+ "\t\t\t----------------------------------------\n" 
			+ "\t\t\tCorrection totale : [-I -d -R +P + 1/2 Diam - Visee] " + (
					(-1.0 * cv.correctionSextan_EnMinuteArc() 
							- cv.correctionHauteurOeilMetre_EnMinuteArc(hauteurOeil)
							- cv.correctionRefraction_EnMinuteArc(Ha)
							+ cv.correctionParallaxe_EnMinuteArc(Ha, indiceRefraction_PI)
							+ cv.correctionAjoutSemiDiametreAstre_EnMinuteArc(heureObservation, indiceRefraction_PI)
							- cv.corrrectionTypeVisee_EnMinuteArc (t, heureObservation, indiceRefraction_PI) ) )+ "'\n");
		}
		else {
			canevas.append( "\t\t\tCorrection Sextan: [-I]                   "+ (-1.0 * cv.correctionSextan_EnMinuteArc())+ "'\n"
			+ "\t\t\tCorrection1 :      [-d -R +P + 1/2 Diam]  " + fmt.format((-1.0 * cv.correctionHauteurOeilMetre_EnMinuteArc(hauteurOeil)
					- cv.correctionRefraction_EnMinuteArc(Ha)
					+ cv.correctionParallaxe_EnMinuteArc(Ha, indiceRefraction_PI)
					+ cv.correctionAjoutSemiDiametreAstre_EnMinuteArc(heureObservation, indiceRefraction_PI))) + "'\n"
			+ "\t\t\tCorrection2 :      [Visee]                " + (-1.0 * cv.corrrectionTypeVisee_EnMinuteArc (t, heureObservation, indiceRefraction_PI))+ "'\n"
			+ "\t\t\t----------------------------------------\n" 
			+ "\t\t\tCorrection totale : [-I -d -R +P + 1/2 Diam - Visee] " + (
					(-1.0 * cv.correctionSextan_EnMinuteArc() 
						- cv.correctionHauteurOeilMetre_EnMinuteArc(hauteurOeil)
						- cv.correctionRefraction_EnMinuteArc(Ha)
						+ cv.correctionParallaxe_EnMinuteArc(Ha, indiceRefraction_PI)
						+ cv.correctionAjoutSemiDiametreAstre_EnMinuteArc(heureObservation, indiceRefraction_PI)
						- cv.corrrectionTypeVisee_EnMinuteArc (t, heureObservation, indiceRefraction_PI) ) )+ "'\n");
		}
	
		
		canevas.append("\n	Hv         : " + Hv().toCanevas() +  "       [" + fmt.format (Hv().asDegre())+ "°]\n");
		canevas.append("	   Hv = Hi + Correction Totale = " + 
											fmt.format(Hi().asDegre()) + "°  +  (" + 
											fmt.format(cv().correctionTotale_EnDegre(Hi(), 
													hauteurOeil(), 
													heureVisee(),
													indiceRefraction_PI,
													typeVisee())) + "°)  =  " +
											Hv().toCanevas() + "       [" + fmt.format (Hv().asDegre())+ "°]\n\n"  );
		return canevas.toString();
	}
	
	private String infoCalculAzimut(DecimalFormat fmt) {
		StringBuffer canevas = new StringBuffer();
		canevas.append("6- Azimut" + "\n");
		canevas.append("----------" + "\n");
		canevas.append("	=======>> Z   : " + (azimut() == null ? "NULL" : azimut()) + "\n");
		canevas.append("	          Sens: " + (sensIntercept() == null ? "NULL" : sensIntercept()) + "\n\n");
		double azimutCalcule = droiteDeHauteur().azimutCalculed(positionEstimee(),
				declinaisonAstre(),
				Hc());
		canevas.append("	Azimut calculé :  "+ azimutCalcule +"\n");
		canevas.append("	   Element de calcul : \n");
		canevas.append("	     - Angle horaire local : "+ angleHoraireLocalAstre() +"\n");
		canevas.append("	     - Sens latitude       : "+ positionEstimee().latitude().getSens() +"\n");
		double x = azimutCalcule;
		if (positionEstimee().latitude().getSens() == ePointsCardinaux.Nord) {
			if (angleHoraireLocalAstre().asDegre() < 180.0) {
				x = 360.0 - azimutCalcule;
				canevas.append("                 [AHL < 180]	=> z : z = 360.0 - z = 360.0 - "+azimutCalcule+"   = "+ fmt.format(x)+"\n");
			}
			else
				canevas.append("                 [AHL >= 180] => z : z = z = "+azimutCalcule+"\n");
		}
		else {
			if (angleHoraireLocalAstre().asDegre() > 180.0) {
				x = 180.0 - azimutCalcule;
				canevas.append("                 [AHL > 180]	=> z : z = 180.0 - z = 180.0 - "+azimutCalcule+"   = "+ fmt.format(x)+"\n");
			}
			else {
				x = 180 + azimutCalcule;
				canevas.append("                 [AHL <= 180]	=> z : z = 180 + z = 180.0 - "+azimutCalcule+"   = "+ fmt.format(x)+"\n");			
			}
		}
		canevas.append("	     - z = abs(z)       :  abs("+ x +")\n");
		canevas.append("	Azimut corrige :  "+ droiteDeHauteur().correctionAzimutale(azimutCalcule,
				angleHoraireLocalAstre(),
				positionEstimee().latitude()) +"\n");
		return canevas.toString();
	}
	
	
}
