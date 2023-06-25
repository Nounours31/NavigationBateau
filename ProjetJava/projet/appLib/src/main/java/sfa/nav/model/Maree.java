package sfa.nav.model;

import java.util.ArrayList;
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
	private  	eEtatMaree _etat;
	
	public Maree (NavDateHeure H, Hauteur h, int coef) {
		_heure = H;
		_hauteur = h;
		_etat = eEtatMaree.X;
		_Coef = coef;
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
				String date = s.replaceAll("-",  "/");

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
					eEtatMaree etat = (typeMaree.equals("tide.low") ? eEtatMaree.BM : (typeMaree.equals("tide.hight") ? eEtatMaree.PM : eEtatMaree.X));

					String dateMaree = date + " " + horaireMaree.getAsString();
					NavDateHeure heureMaree = NavDateHeureFactory.fromString(dateMaree);

					
					double hauteur = hauteurMaree.getAsDouble();
					int coef  = 0;
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
