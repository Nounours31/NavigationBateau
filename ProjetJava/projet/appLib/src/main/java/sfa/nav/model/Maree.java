package sfa.nav.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.tools.eEtatMaree;

public class Maree {
	static private final Logger logger = LoggerFactory.getLogger(Maree.class);

	private final NavDateHeure _heure;
	private final Hauteur _hauteur;
	private final int _Coef;
	private eEtatMaree _etat;
	private ePortRef _portDeRef;

	private enum ePortRef {
		Brest, StMalo, aucun;
	}

	private enum ePort {
		StMalo, SaintQuay, Binic;
	}

	private static class CorrectionsPourLesPortsRattaches {
		ePort portRatache;
		NavDuree CorrectionHorairePM_VE;
		NavDuree CorrectionHorairePM_ME;
		NavDuree CorrectionHoraireBM_VE;
		NavDuree CorrectionHoraireBM_ME;

		Hauteur CorrectionHauteurPM_VE;
		Hauteur CorrectionHauteurPM_ME;
		Hauteur CorrectionHauteurBM_VE;
		Hauteur CorrectionHauteurBM_ME;
	}

	static private HashMap<ePortRef, ArrayList<CorrectionsPourLesPortsRattaches>> listeDesCorrections = initCorrectionsPourLesPortsRattaches();

	static private HashMap<ePortRef, ArrayList<CorrectionsPourLesPortsRattaches>> initCorrectionsPourLesPortsRattaches() {
		listeDesCorrections = new HashMap<>();
		try {
			ArrayList<CorrectionsPourLesPortsRattaches> allPort = new ArrayList<>();
			CorrectionsPourLesPortsRattaches StQuay = new CorrectionsPourLesPortsRattaches();
			StQuay.portRatache = ePort.SaintQuay;
			StQuay.CorrectionHorairePM_VE = NavDureeFactory.fromString("-0:05");
			StQuay.CorrectionHorairePM_ME = NavDureeFactory.fromString("-0:10");
			StQuay.CorrectionHoraireBM_VE = NavDureeFactory.fromString("-0:20");
			StQuay.CorrectionHoraireBM_ME = NavDureeFactory.fromString("-0:25");

			StQuay.CorrectionHauteurPM_VE = new Hauteur(-0.85);
			StQuay.CorrectionHauteurPM_ME = new Hauteur(-0.65);
			StQuay.CorrectionHauteurBM_VE = new Hauteur(-0.25);
			StQuay.CorrectionHauteurBM_ME = new Hauteur(-0.05);
			allPort.add(StQuay);

			CorrectionsPourLesPortsRattaches Binic = new CorrectionsPourLesPortsRattaches();
			StQuay.portRatache = ePort.Binic;
			Binic.CorrectionHorairePM_VE = NavDureeFactory.fromString("-0:05");
			Binic.CorrectionHorairePM_ME = NavDureeFactory.fromString("-0:10");
			Binic.CorrectionHoraireBM_VE = NavDureeFactory.fromString("-0:20");
			Binic.CorrectionHoraireBM_ME = NavDureeFactory.fromString("-0:25");

			Binic.CorrectionHauteurPM_VE = new Hauteur(-0.85);
			Binic.CorrectionHauteurPM_ME = new Hauteur(-0.65);
			Binic.CorrectionHauteurBM_VE = new Hauteur(-0.25);
			Binic.CorrectionHauteurBM_ME = new Hauteur(-0.05);
			allPort.add(Binic);

			CorrectionsPourLesPortsRattaches StMalo = new CorrectionsPourLesPortsRattaches();
			StQuay.portRatache = ePort.StMalo;
			Binic.CorrectionHorairePM_VE = NavDureeFactory.fromString("-0:0");
			Binic.CorrectionHorairePM_ME = NavDureeFactory.fromString("0:0");
			Binic.CorrectionHoraireBM_VE = NavDureeFactory.fromString("-0:0");
			Binic.CorrectionHoraireBM_ME = NavDureeFactory.fromString("-0:0");

			Binic.CorrectionHauteurPM_VE = new Hauteur(0.0);
			Binic.CorrectionHauteurPM_ME = new Hauteur(0.0);
			Binic.CorrectionHauteurBM_VE = new Hauteur(0.0);
			Binic.CorrectionHauteurBM_ME = new Hauteur(0.0);
			allPort.add(StMalo);

			listeDesCorrections.put(ePortRef.StMalo, allPort);
		} catch (NavException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listeDesCorrections;
	}

	public Maree(NavDateHeure H, Hauteur h, int coef, ePortRef x) {
		_heure = H;
		_hauteur = h;
		_etat = eEtatMaree.X;
		_Coef = coef;
		_portDeRef = x;
	}

	public Maree(NavDateHeure H, Hauteur h, int coef) {
		_heure = H;
		_hauteur = h;
		_etat = eEtatMaree.X;
		_Coef = coef;
		_portDeRef = ePortRef.aucun;
	}

	public Maree(NavDateHeure H, Hauteur h) {
		_heure = H;
		_hauteur = h;
		_etat = eEtatMaree.X;
		_Coef = -1;
		_portDeRef = ePortRef.aucun;
	}

	public NavDateHeure heure() {
		return _heure;
	}

	public Hauteur hauteur() {
		return _hauteur;
	}

	public eEtatMaree etat() {
		return _etat;
	}

	public void etat(eEtatMaree e) {
		_etat = e;
	}

	public boolean avant(Maree m2) {
		return this._heure.avant(m2.heure());
	}

	public boolean avant(NavDateHeure h) {
		return this._heure.avant(h);
	}

	public boolean avantOrEqual(NavDateHeure h) {
		return this._heure.avantOrEqual(h);
	}

	public boolean apresOrEqual(NavDateHeure h) {
		return this._heure.apresOrEqual(h);
	}

	public boolean apres(NavDateHeure h) {
		return !avant(h);
	}

	@Override
	public String toString() {
		return "Maree [_heure=" + _heure + ", _hauteur=" + _hauteur + ", _etat=" + _etat + "]";
	}

	public boolean auDessus(Maree m2) {
		return (this._hauteur._d > m2._hauteur._d);
	}

	public static ArrayList<Maree> fromListeShomToListeMaree(String reponse) throws NavException {
		ArrayList<Maree> retour = new ArrayList<>();

		JsonElement root = JsonParser.parseString(reponse);
		if (root.isJsonObject()) {
			JsonObject rootasobj = root.getAsJsonObject();
			// {\"2023-06-21\":[[\"tide.low\
			Set<String> keys = rootasobj.keySet();
			LinkedHashSet<String> sortedKeys = new LinkedHashSet<String>(keys);
			for (String s : sortedKeys) {
				String date = s.replaceAll("-", "/");

				// [[\"tide.low\",\"04:00\",\"2.56\",\"---\"],[\"tide.high\",\"
				JsonElement mareesDuJour = rootasobj.get(s);
				if (!mareesDuJour.isJsonArray())
					continue;

				JsonArray allMaree = mareesDuJour.getAsJsonArray();
				for (JsonElement unemaree : allMaree.asList()) {
					if (!unemaree.isJsonArray())
						continue;

					JsonArray uneMareeJson = unemaree.getAsJsonArray();
					JsonElement etatMaree = uneMareeJson.get(0);
					JsonElement horaireMaree = uneMareeJson.get(1);
					JsonElement hauteurMaree = uneMareeJson.get(2);
					JsonElement coefMaree = uneMareeJson.get(3);

					String typeMaree = etatMaree.getAsString();
					if (typeMaree.equals("tide.none"))
						continue;
					eEtatMaree etat = (typeMaree.equals("tide.low") ? eEtatMaree.BM
							: (typeMaree.equals("tide.hight") ? eEtatMaree.PM : eEtatMaree.X));

					String dateMaree = date + " " + horaireMaree.getAsString();
					NavDateHeure heureMaree = NavDateHeureFactory.fromString(dateMaree);

					double hauteur = hauteurMaree.getAsDouble();
					int coef = 0;
					if (etat == eEtatMaree.PM)
						coef = coefMaree.getAsInt();

					Maree m = new Maree(heureMaree, new Hauteur(hauteur), coef);
					retour.add(m);
				}
			}
		}
		return retour;
	}

}
