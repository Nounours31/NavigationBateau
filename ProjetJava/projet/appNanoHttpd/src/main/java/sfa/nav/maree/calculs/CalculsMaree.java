package sfa.nav.maree.calculs;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.model.Hauteur;
import sfa.nav.model.Maree;
import sfa.nav.model.NavDateHeure;
import sfa.nav.model.NavDateHeureFactory;
import sfa.nav.model.tools.eEtatMaree;
import sfa.nav.model.tools.eSensIntervalMaree;
import sfa.nav.model.tools.eSensMaree;

public class CalculsMaree {
	private static Logger logger = LoggerFactory.getLogger(CalculsMaree.class);
	private final double[] nbDouziemmeParHeureMaree = { 1, 2, 3, 3, 2, 1, 0 };
	private final double[] nbDouziemmeCumuleParHeureMaree = { 0, 1, 3, 6, 9, 11, 12 };

	public CalculsMaree() {
	}

	public Hauteur CalculHauteur(Maree m1, Maree m2, NavDateHeure h) {
		return CalculHauteurDouzieme(m1, m2, h);
	}

	public NavDateHeure CalculHeure(Maree m1, Maree m2, Hauteur piedPilote, Hauteur HauteurEau) {
		return CalculHeureDouzieme(m1, m2, piedPilote, HauteurEau);
	}

	public Hauteur CalculHauteurV2(Maree m1, Maree m2, NavDateHeure h) {
		return CalculHauteurHarmonique(m1, m2, h);
	}

	public NavDateHeure CalculHeureV2(Maree m1, Maree m2, Hauteur piedPilote, Hauteur HauteurEau) {
		return CalculHeureHarmonique(m1, m2, piedPilote, HauteurEau);
	}

	// -----------------------------------------------------------
	// Calcul par sinusoide
	// -----------------------------------------------------------
	//
	// DeltaH : variation de hauteur par rapport au point de repere choisi
	// ma : marnage de la maree concernee
	// DeltaT : temps ecoule depuis le point de repere choisi
	// DureeMaree : durée de la marée
	//
	// DeltaH = ma * sin ((pi/2) * DeltaT / DureeMaree)) **2
	// DeltaT = (DureeMaree / (pi/2)) * asin( sqrt (DeltaH / ma))
	// -----------------------------------------------------------
	public NavDateHeure CalculHeureHarmonique(Maree m1, Maree m2, Hauteur piedPilote, Hauteur HauteurEau) {
		Maree debut = (m1.avant(m2) ? m1 : m2);
		Maree fin = (m1.avant(m2) ? m2 : m1);

		Hauteur hauteurRecherchee = new Hauteur(HauteurEau.getValInMetre() + Math.abs(piedPilote.getValInMetre()));

		eSensMaree sens = eSensMaree.X;
		if (debut.hauteur().plusHaut(fin.hauteur())) {
			debut.etat(eEtatMaree.PM);
			fin.etat(eEtatMaree.BM);
			sens = eSensMaree.Descendante;
		} else {
			debut.etat(eEtatMaree.BM);
			fin.etat(eEtatMaree.PM);
			sens = eSensMaree.Montante;
		}

		boolean isValid = false;
		if (sens == eSensMaree.Montante) {
			isValid = ((debut.hauteur().plusBasse(hauteurRecherchee)) && (fin.hauteur().plusHaut(hauteurRecherchee)));
		} else {
			isValid = ((debut.hauteur().plusHaut(hauteurRecherchee)) && (fin.hauteur().plusBasse(hauteurRecherchee)));
		}
		if (!isValid)
			return null;

		double DureeMaree = fin.heure().asHeureDecimale() - debut.heure().asHeureDecimale();
		double DeltaH = hauteurRecherchee.getValInMetre() - debut.hauteur().getValInMetre();
		double marnage = Math.abs(fin.hauteur().moins(debut.hauteur()).getValInMetre());
		double DeltaT = (DureeMaree / (Math.PI / 2)) * Math.asin(Math.sqrt(Math.abs(DeltaH / marnage)));

		NavDateHeure retour = NavDateHeureFactory.fromZonedDateTime(debut.heure().add(DeltaT));
		return retour;
	}

	public Hauteur CalculHauteurHarmonique(Maree m1, Maree m2, NavDateHeure h) {
		Maree debut = (m1.avant(m2) ? m1 : m2);
		Maree fin = (m1.avant(m2) ? m2 : m1);

		boolean isValid = ((debut.avantOrEqual(h)) && (fin.apresOrEqual(h)));

		if (!isValid) {
			logger.error("Calcul invalides");
			return null;
		}
		eSensMaree sens = eSensMaree.X;
		if (debut.hauteur().plusHaut(fin.hauteur())) {
			debut.etat(eEtatMaree.PM);
			fin.etat(eEtatMaree.BM);
			sens = eSensMaree.Descendante;
		} else {
			debut.etat(eEtatMaree.BM);
			fin.etat(eEtatMaree.PM);
			sens = eSensMaree.Montante;
		}

		double dureeMaree = (fin.heure().getHeureDecimaleFromEpoch() - (debut.heure().getHeureDecimaleFromEpoch()));
		double marnage = Math.abs(fin.hauteur().moins(debut.hauteur()).getValInMetre())
				* ((sens == eSensMaree.Montante) ? 1.0 : -1.0);
		double DeltaT = h.asHeureDecimale() - debut.heure().asHeureDecimale();
		double DeltaH = marnage * Math.pow(Math.sin((Math.PI / 2.0) * DeltaT / dureeMaree), 2.0);

		Hauteur retour = new Hauteur(debut.hauteur());
		retour._d = retour._d + DeltaH;
		return retour;
	}

	// -----------------------------------------------------------
	// Calcul par douzieme
	// -----------------------------------------------------------
	public Hauteur CalculHauteurDouzieme(Maree m1, Maree m2, NavDateHeure h) {
		Maree debut = (m1.avant(m2) ? m1 : m2);
		Maree fin = (m1.avant(m2) ? m2 : m1);

		boolean isValid = ((debut.avantOrEqual(h)) && (fin.apresOrEqual(h)));

		if (!isValid) {
			logger.error("Calcul invalides");
			return null;
		}

		eSensMaree sens = eSensMaree.X;
		if (debut.hauteur().plusHaut(fin.hauteur())) {
			debut.etat(eEtatMaree.PM);
			fin.etat(eEtatMaree.BM);
			sens = eSensMaree.Descendante;
		} else {
			debut.etat(eEtatMaree.BM);
			fin.etat(eEtatMaree.PM);
			sens = eSensMaree.Montante;
		}

		double HeureMaree = (fin.heure().getHeureDecimaleFromEpoch() - (debut.heure().getHeureDecimaleFromEpoch()))
				/ 6.0;
		double Marnage = Math.abs(fin.hauteur().moins(debut.hauteur()).getValInMetre());
		double Douzieme = Marnage / 12.0;

		double nbHeureMaree = h.getHeureDecimaleFromEpoch() - debut.heure().getHeureDecimaleFromEpoch();
		nbHeureMaree = nbHeureMaree / HeureMaree;

		int nbHeureEntiere = (int) Math.floor(nbHeureMaree);
		double nbHeuredecimale = nbHeureMaree - Math.floor(nbHeureMaree);

		double nbDouziemeHeureEntiere = nbDouziemmeCumuleParHeureMaree[nbHeureEntiere];
		double nbDouziemeRestant = nbDouziemmeParHeureMaree[nbHeureEntiere];

		double deltaHauteur = (nbDouziemeHeureEntiere + nbDouziemeRestant * nbHeuredecimale) * Douzieme;
		Hauteur retour = new Hauteur(debut.hauteur());
		retour._d = retour._d + ((sens == eSensMaree.Montante) ? 1.0 : -1.0) * deltaHauteur;
		return retour;
	}

	public NavDateHeure CalculHeureDouzieme(Maree m1, Maree m2, Hauteur piedPilote, Hauteur HauteurEau) {
		Maree debut = (m1.avant(m2) ? m1 : m2);
		Maree fin = (m1.avant(m2) ? m2 : m1);

		double HauteurTotale = HauteurEau.getValInMetre() + Math.abs(piedPilote.getValInMetre());

		eSensMaree sens = eSensMaree.X;
		if (debut.hauteur().plusHaut(fin.hauteur())) {
			debut.etat(eEtatMaree.PM);
			fin.etat(eEtatMaree.BM);
			sens = eSensMaree.Descendante;
		} else {
			debut.etat(eEtatMaree.BM);
			fin.etat(eEtatMaree.PM);
			sens = eSensMaree.Montante;
		}

		boolean isValid = false;
		if (sens == eSensMaree.Montante)
			isValid = ((debut.hauteur().plusBasse(HauteurTotale)) && (fin.hauteur().plusHaut(HauteurTotale)));
		else
			isValid = ((debut.hauteur().plusHaut(HauteurTotale)) && (fin.hauteur().plusBasse(HauteurTotale)));

		if (!isValid)
			return null;

		double HeureMaree = (fin.heure().getHeureDecimaleFromEpoch() - debut.heure().getHeureDecimaleFromEpoch()) / 6.0;
		double Marnage = Math.abs(fin.hauteur().moins(debut.hauteur()).getValInMetre());
		double Douzieme = Marnage / 12.0;

		double variationDeHauteurEau = Math.abs(debut.hauteur().getValInMetre() - HauteurTotale);
		double nbDouzieme = variationDeHauteurEau / Douzieme;

		// -------------------------
		// recherche du nb heure maree
		// -------------------------
		double nbHeureMaree = 0.0;

		// interval precedent
		int i = 0;
		while ((i < nbDouziemmeCumuleParHeureMaree.length) && (nbDouziemmeCumuleParHeureMaree[i] <= nbDouzieme)) {
			i++;
		}
		i = i - 1;
		nbHeureMaree = (double) i;

		// reste a parcourir sur cet interval
		double nbDouziemeRestant = nbDouzieme - nbDouziemmeCumuleParHeureMaree[i];
		double nbDouziemesurcetInterval = nbDouziemmeParHeureMaree[i];
		double heuremareerestante = ((nbDouziemesurcetInterval != 0) ? nbDouziemeRestant / nbDouziemesurcetInterval
				: 0.0);
		nbHeureMaree = nbHeureMaree + heuremareerestante;

		double nbHeureDecimale = nbHeureMaree * HeureMaree;

		NavDateHeure retour = NavDateHeureFactory.fromZonedDateTime(debut.heure().add(nbHeureDecimale));
		return retour;
	}

	public List<IntervalHoraire> CalculIntervalPassageParJour(List<Maree> lM, Hauteur piedPilote, Hauteur HauteurEau) {
		List<IntervalHoraire> retour = new ArrayList<>();
		if (lM.size() < 3)
			return retour;

		int i = 0;
		IntervalHoraire interval = new IntervalHoraire();
		while ((i + 1) < lM.size()) {
			Maree m1 = lM.get(i);
			Maree m2 = lM.get(i + 1);

			CalculsMaree cm = new CalculsMaree();
			NavDateHeure h = cm.CalculHeure(m1, m2, piedPilote, HauteurEau);

			if (h == null)
				continue;

			if (!interval.hasStart())
				interval.start(h);
			else {
				interval.end(h);
				interval.setMareePosition(
						(m1.auDessus(m2) ? eSensIntervalMaree.YaDeLeau : eSensIntervalMaree.YaPasDeau));
				retour.add(interval);
				interval = new IntervalHoraire();
				interval.start(h);
			}
			i++;
		}
		return retour;

	}
}
