package sfa.nav.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Maree {
	static private final Logger logger = LoggerFactory.getLogger(Maree.class); 
	
	public enum eEtatMaree {
		PM, BM, X;
	}

	public enum eSensMaree {
		Etale, Montante, Descendante, X;
	}
	
	private final Heure _heure;
	private final Hauteur _hauteur;
	private  	eEtatMaree _etat;
	
	public Maree (Heure H, Hauteur h) {
		_heure = H;
		_hauteur = h;
		_etat = eEtatMaree.X;
	}

	public Heure heure() {
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

	public boolean avant(Heure h) {
		return this._heure.avant(h);
	}

	public boolean apres(Heure h) {
		return !avant(h);
	}

	@Override
	public String toString() {
		return "Maree [_heure=" + _heure + ", _hauteur=" + _hauteur + ", _etat=" + _etat + "]";
	}
}
