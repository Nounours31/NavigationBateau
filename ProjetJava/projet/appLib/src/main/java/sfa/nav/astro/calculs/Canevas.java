package sfa.nav.astro.calculs;

import java.text.DecimalFormat;

import sfa.nav.astro.calculs.CorrectionDeVisee.eTypeVisee;
import sfa.nav.astro.calculs.CorrectionDeViseeLune.correctionLunaireDeViseeHandler;
import sfa.nav.astro.calculs.DroiteDeHauteur.eSensIntercept;
import sfa.nav.astro.calculs.DroiteDeHauteur.eTypeDroiteHauteur;
import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.Declinaison;
import sfa.nav.model.Distance;
import sfa.nav.model.NavDateHeure;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.tools.ePointsCardinaux;

public class Canevas {
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

	private CorrectionDeVisee _cv = null;
	public CorrectionDeVisee cv() {return _cv; }
	public CorrectionDeViseeSoleil cvSolaire() {if (_cv instanceof CorrectionDeViseeSoleil) return (CorrectionDeViseeSoleil)_cv; return null;}
	public CorrectionDeViseeLune cvLunaire() {if (_cv instanceof CorrectionDeViseeLune) return (CorrectionDeViseeLune)_cv; return null;}
	public void cv(CorrectionDeVisee x) { _cv = x;}

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
		if (typeDroiteHauteur() == eTypeDroiteHauteur.etoile) 
			sb.append(myToString());
		else if (typeDroiteHauteur() == eTypeDroiteHauteur.lune) 
			sb.append(myToString());
		else if (typeDroiteHauteur() == eTypeDroiteHauteur.planete) 
			sb.append(myToString());
		else if (typeDroiteHauteur() == eTypeDroiteHauteur.soleil) 
			sb.append(myToString());
		else 
			sb.append("type inconnu " + typeDroiteHauteur());
		return sb.toString();
	}

	private String myToString() {
		DecimalFormat fmt = new DecimalFormat("+##00.0000;-#");
		StringBuffer canevas = new StringBuffer(); 
		canevas.append(introductionEtInfoVisee());
		canevas.append(infoCalculDeclinaison(fmt));
		canevas.append(infoCalculAngleHoraireLocal(fmt));
		canevas.append(infoCalculHauteurLatitudeEstimee());
		canevas.append(infoCalculHauteurCalculee());
		canevas.append(infoCalculIntercept(fmt));
		canevas.append(infoCalculAzimut(fmt));
		return canevas.toString();
	}
	
	

	private String introductionEtInfoVisee() {
		StringBuffer canevas = new StringBuffer();
		canevas.append("-----------------------------------------" + "\n");
		canevas.append("|    CANEVAS DROITE DE HAUTEUR   "+ typeDroiteHauteur()  + "\n");
		canevas.append("-----------------------------------------" + "\n");
		canevas.append("0- Info " + "\n");
		canevas.append("---------" + "\n");
		canevas.append("	a - Date Corrigee" + "\n");
		canevas.append("		" + (heureVisee() == null? "NULL" : heureVisee()) + "\n");
		canevas.append("	b - Astre" + "\n");
		canevas.append("		"+ astre() + "\n");
		canevas.append("		Hi: " + (Hi() == null? "NULL" : Hi().toCanevas()) + "\n");
		canevas.append("	c - Position estimee" + "\n");
		canevas.append("		" + (positionEstimee() == null ? "NULL" : positionEstimee().toCanevas())+ "\n");
		canevas.append("	d - Erreur sextan et Oeil" + "\n");
		canevas.append("		" + (cv() == null ? "NULL" : cv().forCanevas()) + "\n");
		canevas.append("		Hauteur oeil: " + hauteurOeil() +" metre" + "\n\n");
		return canevas.toString();
	}

	private String infoCalculDeclinaison(DecimalFormat fmt) {
		StringBuffer canevas = new StringBuffer();
		canevas.append("1- Declinaison\n");
		canevas.append("---------------\n");
		canevas.append("	Astre LHA =======>> " + 
				(declinaisonAstre() == null ? "NULL" : declinaisonAstre().toCanevas() + "       ["+ fmt.format(declinaisonAstre().asDegre())+"°]") + "\n");
		canevas.append("	Element de calculs: \n" + 
				(epheAstre() == null ? "NULL" : epheAstre().toCanevas(heureVisee(), true, false, "\t\t")) + "\n");
		return canevas.toString();
	}
	

	private String infoCalculAngleHoraireLocal(DecimalFormat fmt) {
		StringBuffer canevas = new StringBuffer(); 
		canevas.append("2- Angle Horaire Local" + "\n");
		canevas.append("-----------------------" + "\n");
		canevas.append("	LHA / GHA =======>> " +  (angleHoraireLocalAstre() == null ? "NULL" : angleHoraireLocalAstre().toCanevas() + "       ["+fmt.format(angleHoraireLocalAstre().asDegre())+"°]") + "\n");
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
		canevas.append("	Point vernal: " + (ephePointVernal() == null ? "NULL" : ephePointVernal().AngleHoraireAHeureObservation(heureVisee()).toCanevas()) + "\n");
		canevas.append("	   Element de calculs: \n" + 
				(ephePointVernal() == null ? "NULL" : ephePointVernal().toCanevas(heureVisee(), false, true,"\t\t")) + "\n");

		canevas.append("	Astre LHA: " + (epheAstre() == null ? "NULL" : epheAstre().AngleHoraireAHeureObservation(heureVisee()).toCanevas()) + "\n");
		canevas.append("	   Element de calculs: \n" + 
				(epheAstre() == null ? "NULL" : epheAstre().toCanevas(heureVisee(), false, true,"\t\t")) + "\n");
		canevas.append("	Longitude Estimée: " + 
				(positionEstimee() == null ? "NULL" : positionEstimee().longitude().toCanevasLong()) + "\n");
		canevas.append("	Calcul =  Vernal + Astre + Longi = " + 
				(ephePointVernal() == null ? "NULL" : ephePointVernal().AngleHoraireAHeureObservation(heureVisee()).toCanevas()) + " + " +
				(epheAstre() == null ? "NULL" : epheAstre().AngleHoraireAHeureObservation(heureVisee()).toCanevas()) + " + " +
				(positionEstimee() == null ? "NULL" : positionEstimee().longitude().toCanevas()) +  
				" = " + (angleHoraireLocalAstre() == null ? "NULL" : angleHoraireLocalAstre().toCanevas()) + 
				"        "+angleHoraireLocalAstre().toString()+"\n\n");
		return canevas.toString();
	}
	
	private String infoCalculAngleHoraireLocal_Autre(DecimalFormat fmt) {
		StringBuffer canevas = new StringBuffer(); 
		canevas.append("	Astre LHA: " + (epheAstre() == null ? "NULL" : epheAstre().AngleHoraireAHeureObservation(heureVisee()).toCanevas()) + "\n");
		canevas.append("	   Element de calculs: \n" + 
				(epheAstre() == null ? "NULL" : epheAstre().toCanevas(heureVisee(), false, true,"\t\t\t")) + "\n");
		canevas.append("	Longitude Estimée: " + (positionEstimee() == null ? "NULL" : positionEstimee().longitude().toCanevasLong()) + "\n");
		canevas.append("	Calcul = Astre + Longi = " + 
				(epheAstre() == null ? "NULL" : epheAstre().AngleHoraireAHeureObservation(heureVisee()).toCanevas()) + " + " +
				(positionEstimee() == null ? "NULL" : positionEstimee().longitude().toCanevas()) +  
				" = " + (angleHoraireLocalAstre() == null ? "NULL" : angleHoraireLocalAstre().toCanevas()) + "        "+angleHoraireLocalAstre().toString()+"\n\n");
		return canevas.toString();
	}

	private String infoCalculHauteurLatitudeEstimee() {
		StringBuffer canevas = new StringBuffer(); 
		canevas.append("3- Latitude Estimee" + "\n");
		canevas.append("--------------------" + "\n");
		canevas.append("	=======>> Lat. estimee :" + (positionEstimee() == null ? "NULL" : positionEstimee().latitude().toCanevas()) + "\n\n");
		return canevas.toString();
	}

	private String infoCalculHauteurCalculee() {
		StringBuffer canevas = new StringBuffer();
		canevas.append("4- Hauteur calculee" + "\n");
		canevas.append("--------------------" + "\n");
		canevas.append("	=======>> Hc: " + Hc() + "\n\n");
		return canevas.toString();
	}

	private String infoCalculIntercept(DecimalFormat fmt) {
		StringBuffer canevas = new StringBuffer();
		canevas.append("5- Intercept" + "\n");
		canevas.append("-------------" + "\n");
		canevas.append("	=======>> I: " + (intercept() == null ? "NULL" : intercept()) + "\n\n");
		canevas.append("	Hi............................................. : " +  (Hi() == null? "NULL" : Hi().toCanevas()) + "       [" + fmt.format (Hi().asDegre())+ "°]\n");
		if (typeDroiteHauteur() == eTypeDroiteHauteur.lune)
			canevas.append(infoCalculIntercept_CorrectionLune(fmt));
		else if ((typeDroiteHauteur() == eTypeDroiteHauteur.soleil) ||
				(typeDroiteHauteur() == eTypeDroiteHauteur.etoile) ||
				(typeDroiteHauteur() == eTypeDroiteHauteur.planete))
			canevas.append(infoCalculIntercept_CorrectionSoleil(fmt));
		else 
			canevas.append("##################################################################");
			
		canevas.append("	Intercept: "+ intercept() +" \n" );
		canevas.append("	   intercept: (Hv - Hc) * 60.0 = ("+ fmt.format(Hv().asDegre()) +" - " + fmt.format(Hc().asDegre()) + ") * 60.0 = ("+ fmt.format(Hv().asDegre() - Hc().asDegre()) + ") * 60.0  = "+ fmt.format((Hv().asDegre() - Hc().asDegre()) * 60.0) + "\n\n" );
		return canevas.toString();
	}

	private String infoCalculIntercept_CorrectionSoleil(DecimalFormat fmt) {
		StringBuffer canevas = new StringBuffer();
		canevas.append("	Tables corrections (°) ........................ : " + fmt.format(cvSolaire().correctionEnDegre(Hi(), hauteurOeil(), heureVisee(), typeVisee()))+"\n" );
		canevas.append("	  Correction  sextan  (Collimation) ........... : " +  fmt.format(cvSolaire().correctionEnMinuteArcPourLeSextan ()/60.0)+"°"+
				"        # " + fmt.format(cvSolaire().correctionEnMinuteArcPourLeSextan ())+"'\n");
		canevas.append("	  Correction1 refraction hauteur oeil ......... : " + fmt.format((cvSolaire().correctionEnMinuteArcPourReflexionHauteurOeil (Hi(), hauteurOeil())/60.0))+"°"+
										"        # " + fmt.format(cvSolaire().correctionEnMinuteArcPourReflexionHauteurOeil (Hi(), hauteurOeil()))+"'\n");
		canevas.append("	  Correction2 par type de visee ["+String.format("%1$14s", typeVisee())+"]: " + fmt.format((cvSolaire().correctionEnMinuteArcTypeVisee (heureVisee(), typeVisee())/60.0))+"°"+
				"        # " + fmt.format(cvSolaire().correctionEnMinuteArcTypeVisee (heureVisee(), typeVisee()))+"'\n");
		canevas.append("	  Correction Totale = -Collimat + Corr1 + Corr2 = " + fmt.format((cvSolaire().correctionEnDegre (Hi(), hauteurOeil(), heureVisee(), typeVisee())))+"°"+
				"        # " + fmt.format(cvSolaire().correctionEnDegre (Hi(), hauteurOeil(), heureVisee(), typeVisee()) * 60.0)+"'\n");
		
		canevas.append("\n	Hv         : " + Hv().toCanevas() +  "       [" + fmt.format (Hv().asDegre())+ "°]\n");
		canevas.append("	   Hv = Hi + Correction Totale = " + 
											fmt.format(Hi().asDegre()) + "°  +  " + 
											fmt.format(cvSolaire().correctionEnDegre(Hi(), hauteurOeil(), heureVisee(), typeVisee())) + "°  =  " +
											Hv().toCanevas() + "       [" + fmt.format (Hv().asDegre())+ "°]\n\n"  );
		return canevas.toString();
	}

	private String infoCalculIntercept_CorrectionLune(DecimalFormat fmt) {
		StringBuffer canevas = new StringBuffer();
		double indiceRefraction_PI = 0.0;
		try { indiceRefraction_PI = epheAstre().pi(heureVisee()); } catch (NavException e) {}
		canevas.append("	Pi ............................................ : " + indiceRefraction_PI +"\n" );
		canevas.append("	Tables corrections Lune (°) ................... : " + fmt.format(cvLunaire().correctionEnDegreLune(Hi(), 
				hauteurOeil(), 
				typeVisee(),
				indiceRefraction_PI))+"\n" );
		canevas.append("	  Correction Collimation sextan ............... : " +  fmt.format(cvLunaire().correctionEnMinuteArcPourLeSextan ()/60.0)+"°"+
				"        # " + fmt.format(cvLunaire().correctionEnMinuteArcPourLeSextan ())+"'\n");
		canevas.append("	  Correction1 depression apparente hauteur oeil : " + 
				fmt.format((cvLunaire().deppressionApparante (hauteurOeil())/60.0))+"°"+"        # " + 
				fmt.format(cvLunaire().deppressionApparante (hauteurOeil()))+"'\n");
		
		correctionLunaireDeViseeHandler parallaxe = cvLunaire().correctionParallaxeEtTypeVisee(Hi(), indiceRefraction_PI, typeVisee());
		canevas.append("	  Correction2 parallaxe ....................... : " + fmt.format(parallaxe.parallaxe / 60.0) +"°"+
				"        # " + fmt.format(parallaxe.parallaxe)+"'\n");
		canevas.append("	  Correction3 par type de visee ["+String.format("%1$10s", typeVisee())+"]   : " + fmt.format(parallaxe.visee/60.0)+"°"+
				"        # " + fmt.format(parallaxe.visee)+"'\n");
		canevas.append("	  Correc. Tot. = -Colli + Corr1 + Corr2 + Corr3 = " + 
				fmt.format((-1.0) * cvLunaire().correctionEnMinuteArcPourLeSextan () + 
						cvLunaire().deppressionApparante (hauteurOeil()) +
						parallaxe.parallaxe +
						parallaxe.visee )+"'"+"        # " + 
				fmt.format(((-1.0) * cvLunaire().correctionEnMinuteArcPourLeSextan () + 
						cvLunaire().deppressionApparante (hauteurOeil()) +
						parallaxe.parallaxe +
						parallaxe.visee) / 60.0)+"°\n");
		
		canevas.append("\n	Hv         : " + Hv().toCanevas() +  "       [" + fmt.format (Hv().asDegre())+ "°]\n");
		canevas.append("	   Hv = Hi + Correction Totale = " + 
											fmt.format(Hi().asDegre()) + "°  +  " + 
											fmt.format(cvLunaire().correctionEnDegreLune(Hi(), 
													hauteurOeil(), 
													typeVisee(),
													indiceRefraction_PI)) + "°  =  " +
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
