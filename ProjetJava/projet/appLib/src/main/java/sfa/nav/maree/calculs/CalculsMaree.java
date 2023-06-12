package sfa.nav.maree.calculs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.Cap;
import sfa.nav.model.Distance;
import sfa.nav.model.Maree;
import sfa.nav.model.Maree.eEtatMaree;
import sfa.nav.model.Maree.eSensMaree;
import sfa.nav.model.Hauteur;
import sfa.nav.model.Heure;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.tools.Constantes;
import sfa.nav.model.tools.DataLoxodromieCapDistance;



public class CalculsMaree {
	private static Logger logger = LoggerFactory.getLogger(CalculsMaree.class);
	private final double[] nbDouziemmeParHeureMaree = {
			1, 2, 3, 3, 2, 1
	};
	private final double[] nbDouziemmeCumuleParHeureMaree = {
			0, 1, 3, 6, 9, 11, 12
	};
	
	private final double[][] nbHeureMareeParDouziemeDeMaree = {
			
	};
	
	public CalculsMaree() {
	}

	public Hauteur CalculHauteur(Maree m1, Maree m2, Heure h) {
		Maree debut = (m1.avant(m2) ? m1 : m2);
		Maree fin = (m1.avant(m2) ? m2 : m1);
		
		boolean isValid = ((debut.avant (h)) &&  (fin.apres (h)));

		if (!isValid)
			return null;

		eSensMaree sens = eSensMaree.X;
		if (m1.hauteur().plusHaut(m2.hauteur())) {
			m1.etat(eEtatMaree.PM);
			m2.etat(eEtatMaree.BM);
			sens = eSensMaree.Descendante;
		}
		else {
			m1.etat(eEtatMaree.BM);
			m2.etat(eEtatMaree.PM);
			sens = eSensMaree.Montante;
		}
		
		
		double HeureMaree = (fin.heure().moins (debut.heure())) / 6.0;
		double Marnage = Math.abs(fin.hauteur().moins (debut.hauteur()).getValInMetre());
		double Douzieme = Marnage / 12.0;
		
		double nbHeureMaree = Math.abs((debut.heure().moins (h)));
		nbHeureMaree = nbHeureMaree / HeureMaree;
		
		int nbHeureEntiere = (int)Math.floor(nbHeureMaree);
		double nbHeuredecimale = nbHeureMaree - Math.floor(nbHeureMaree);
		
		double nbDouziemeHeureEntiere = nbDouziemmeCumuleParHeureMaree[nbHeureEntiere];
		double nbDouziemeRestant = nbDouziemmeParHeureMaree[nbHeureEntiere];
		
		double deltaHauteur = (nbDouziemeHeureEntiere + nbDouziemeRestant * nbHeuredecimale)  * Douzieme;
		Hauteur retour = new Hauteur(m1.hauteur());
		retour._d = retour._d + ((sens == eSensMaree.Montante) ? 1.0: -1.0) * deltaHauteur;
		return retour;
	}

	public Heure CalculHeure(Maree m1, Maree m2, double piedPilote, double HauteurEau) {
		Maree debut = (m1.avant(m2) ? m1 : m2);
		Maree fin = (m1.avant(m2) ? m2 : m1);
		
		double HauteurTotale = HauteurEau + Math.abs(piedPilote);

		eSensMaree sens = eSensMaree.X;
		if (m1.hauteur().plusHaut(m2.hauteur())) {
			m1.etat(eEtatMaree.PM);
			m2.etat(eEtatMaree.BM);
			sens = eSensMaree.Descendante;
		}
		else {
			m1.etat(eEtatMaree.BM);
			m2.etat(eEtatMaree.PM);
			sens = eSensMaree.Montante;
		}

		boolean isValid = false;
		if (sens == eSensMaree.Montante)
			isValid = ((!debut.hauteur().plusHaut(HauteurTotale)) &&  (fin.hauteur().plusHaut(HauteurTotale)));
		else 
			isValid = ((debut.hauteur().plusHaut(HauteurTotale)) &&  (!fin.hauteur().plusHaut(HauteurTotale)));

		if (!isValid)
			return null;
		
		
		double HeureMaree = (fin.heure().moins (debut.heure())) / 6.0;
		double Marnage = Math.abs(fin.hauteur().moins (debut.hauteur()).getValInMetre());
		double Douzieme = Marnage / 12.0;

		double variationDeHauteurEau = Math.abs(debut.hauteur().getValInMetre() - HauteurTotale);
		double nbDouzieme = variationDeHauteurEau / Douzieme;
		
		// -------------------------
		// recherche du nb heure maree
		// -------------------------
		double nbHeureMaree = 0.0;

		// interval precedent
		int i = 0;
		while (nbDouziemmeCumuleParHeureMaree[i] < nbDouzieme) {
			i++;
		}
		i = i - 1;
		nbHeureMaree = (double)i;

		// reste a parcourir sur cet interval
		double nbDouziemeRestant = nbDouzieme - nbDouziemmeCumuleParHeureMaree[i];
		double nbDouziemesurcetInterval = nbDouziemmeParHeureMaree[i];
		double heuremareerestante = nbDouziemeRestant / nbDouziemesurcetInterval;
		nbHeureMaree = nbHeureMaree + heuremareerestante;

		double nbHeure = nbHeureMaree * HeureMaree;

		Heure retour = debut.heure().plus(nbHeure);
		return retour;		
	}
}
