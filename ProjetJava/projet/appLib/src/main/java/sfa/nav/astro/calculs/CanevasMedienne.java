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

public class CanevasMedienne {
	final static Logger logger = LoggerFactory.getLogger(CanevasMedienne.class);
	
	final private eTypeVisee _type;
	eTypeVisee typeVisee() {return _type;}

	private Ephemerides _epheAstre = null;
	public Ephemerides epheAstre() {return _epheAstre;}
	public void epheAstre(Ephemerides x) { _epheAstre = x;}



	public CanevasMedienne(eTypeVisee t) { _type = t; }

/*
	private String myToString(boolean withPDF) throws NoSuchMethodException {
		PDF4DroiteDeHauteur pdf = null;
		if (withPDF) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss.SSSZ");
			String id = sdf.format( new Date());
			pdf = new PDF4DroiteDeHauteur(id);
		}
		DecimalFormat fmt = new DecimalFormat("+##00.0000;-#");
		StringBuffer canevas = new StringBuffer(); 
		canevas.append(predeterminationHeureMeridienne(fmt));
		
		pdf.addTextSansTitre(0, canevas.toString());
		pdf.generePDF();
		
		return canevas.toString();
	}
	
	

	private String introductionEtInfoVisee(DecimalFormat fmt) {
		StringBuffer canevas = new StringBuffer();
		canevas.append(
		"----------------------------------------- \n" + 
		"|    CANEVAS Meridienne   "+ typeDroiteHauteur()  + "\n" +
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
	}*/

}
