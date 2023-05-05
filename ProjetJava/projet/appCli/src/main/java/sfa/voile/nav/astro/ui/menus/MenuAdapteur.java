/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.ui.menus;

import java.util.Arrays;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.CalculsAstro;
import sfa.nav.astro.calculs.eCalculAstroConstantes;
import sfa.nav.lib.tools.NavAstroError;
import sfa.nav.lib.tools.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.Declinaison;
import sfa.nav.model.Heure;
import sfa.nav.model.Latitude;


/**
 *
 * @author pierr
 */
public abstract class MenuAdapteur {
	private Logger _logger = LoggerFactory.getLogger(MenuAdapteur.class);
	
	protected NavAstroMenuItem[] _Item = null;
	
	protected HashMap<eCalculAstroConstantes, Object> _args = null;
	protected static CalculsAstro _calculAstro = new CalculsAstro();
	
	private static Object _lastComputedObject = null;
	public Object getLastComputedObject() { return _lastComputedObject; }
	protected void setLastComputedObject(Object x) { _lastComputedObject = x; }
	
	public abstract boolean affichageItemsDuMenu();
	
	public eUINavAstroAllMenuItems getUserChoiceAndValidedIt(NavAstroError err) {
		eUINavAstroAllMenuItems retour = eUINavAstroAllMenuItems.Sortir;
		while (true) {
			try {
				String s = null;
				retour = readAndCheck(s);
				
				err.setMessage("Choix user:" + s);
				
				if (retour != null) 
					return retour;
			}
			catch (Exception e) {
				err.setMessage(e.getMessage());
			}
			err.setMessage("Mauvais choix");
		}
	}
	
	protected eUINavAstroAllMenuItems readAndCheck(String input) {
		try {
			final int iChoix = Integer.parseInt(input);
			return Arrays.asList(_Item)
					.stream()
					.filter(v -> v.getIndice() == iChoix)
					.findFirst().get().getCode();
		}
		catch (Exception e) {
			_logger.error("Err: {}", e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	protected void displayMenu() {
		System.out.println("Possibilitées:");
		for (NavAstroMenuItem uneMethode : _Item) {
			System.out.println(String.format("\t%1d. %s", uneMethode.getIndice(), uneMethode.getNom()));
		}
		System.out.print("Choix :");
	}

	protected void displayErreur(NavAstroError x) {
		System.out.println("Erreur de choix:" + x.getMessage() );
	}
	
	protected Declinaison getDeclinaisonFromArgsList(eCalculAstroConstantes key) throws NavException {
		if ((_args != null) && (_args.containsKey(key)) && (_args.get(key) != null)) {
			Object o = _args.get(key);
			if ((o != null) && (o instanceof Declinaison)) {
				return (Declinaison)o;
			}
			else {
				_logger.debug("valeur type: {}", _args.get(key).getClass());
			}
		}
		throw new NavException("Cannot read "+ key.name());
	}

	protected Latitude getLatitudeFromArgsList(eCalculAstroConstantes key) throws NavException {
		if ((_args != null) && (_args.containsKey(key)) && (_args.get(key) != null)) {
			Object o = _args.get(key);
			if ((o != null) && (o instanceof Latitude)) {
				return (Latitude)o;
			}
			else {
				_logger.debug("valeur type: {}", _args.get(key).getClass());
			}
		}
		throw new NavException("Cannot read "+ key.name());
	}

	protected Heure getHeureFromArgsList(eCalculAstroConstantes key) throws NavException {
		if ((_args != null) && (_args.containsKey(key)) && (_args.get(key) != null)) {
			Object o = _args.get(key);
			if ((o != null) && (o instanceof Heure)) {
				return (Heure)o;
			}
			else {
				_logger.debug("valeur type: {}", _args.get(key).getClass());
			}
		}
		throw new NavException("Cannot read "+ key.name());
	}

	protected Angle getAngleFromArgsList(eCalculAstroConstantes key) throws NavException {
		if ((_args != null) && (_args.containsKey(key)) && (_args.get(key) != null)) {
			Object o = _args.get(key);
			if ((o != null) && (o instanceof Angle)) {
				return (Angle)o;
			}
			else {
				_logger.debug("valeur type: {}", _args.get(key).getClass());
			}
		}
		throw new NavException("Cannot read "+ key.name());
	}
	
	
    protected void ResumeDeLaSaisie(HashMap<eCalculAstroConstantes, Object> args, eUINavAstroAllMenuItems iCas) {
    	System.out.println("---------------------------------------------------");
    	System.out.println("Arguments saisis - cas : "+ iCas.name());
    	for (eCalculAstroConstantes key : args.keySet()) {
    		System.out.println("\t - "+key.name()+" : ->"+args.get(key)+"<-");			
		}
    	System.out.println("---------------------------------------------------");
    }

    protected void Resultat(Object x) {
    	System.out.println("***************************************************");
    	System.out.println("Resultat:  ->"+ x.toString()+"<-");
    	System.out.println("***************************************************");
    	setLastComputedObject(x);
    }

}
