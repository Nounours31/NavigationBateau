/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.ui.menus;

import java.util.Arrays;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.voile.nav.astro.methodes.CalculsAstro;
import sfa.voile.nav.astro.methodes.eCalculAstroConstantes;
import sfa.voile.nav.astro.modele.Angle;
import sfa.voile.nav.astro.modele.Declinaison;
import sfa.voile.nav.astro.modele.Heure;
import sfa.voile.nav.astro.modele.Latitude;
import sfa.voile.nav.astro.tools.NavAstroError;
import sfa.voile.nav.astro.tools.NavAstroException;
import sfa.voile.nav.astro.ui.tools.DataReaders;

/**
 *
 * @author pierr
 */
public abstract class MenuAdapteur {
	private Logger _logger = LoggerFactory.getLogger(MenuAdapteur.class);
	
	protected NavAstroMenuItem[] _Item = null;
	protected HashMap<eCalculAstroConstantes, Object> _args = null;

	protected static CalculsAstro _calculAstro = new CalculsAstro();
	
	public abstract boolean affichageItemsDuMenu();
	
	public eUINavAstroAllMenuItems getUserChoiceAndValidedIt(NavAstroError err) {
		eUINavAstroAllMenuItems retour = eUINavAstroAllMenuItems.Sortir;
		while (true) {
			try {
				String s = DataReaders.readInput();
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
	
	protected Declinaison getDeclinaisonFromArgsList(eCalculAstroConstantes key) throws NavAstroException {
		if ((_args != null) && (_args.containsKey(key)) && (_args.get(key) != null)) {
			if (_args.get(key).getClass().isInstance(Declinaison.class)) {
				return (Declinaison)_args.get(key);
			}
		}
		throw new NavAstroException("Cannot read "+ key.name());
	}

	protected Latitude getLatitudeFromArgsList(eCalculAstroConstantes key) throws NavAstroException {
		if ((_args != null) && (_args.containsKey(key)) && (_args.get(key) != null)) {
			if (_args.get(key).getClass().isInstance(Latitude.class)) {
				return (Latitude)_args.get(key);
			}
		}
		throw new NavAstroException("Cannot read "+ key.name());
	}

	protected Heure getHeureFromArgsList(eCalculAstroConstantes key) throws NavAstroException {
		if ((_args != null) && (_args.containsKey(key)) && (_args.get(key) != null)) {
			if (_args.get(key).getClass().isInstance(Heure.class)) {
				return (Heure)_args.get(key);
			}
		}
		throw new NavAstroException("Cannot read "+ key.name());
	}

	protected Angle getAngleFromArgsList(eCalculAstroConstantes key) throws NavAstroException {
		if ((_args != null) && (_args.containsKey(key)) && (_args.get(key) != null)) {
			if (_args.get(key).getClass().isInstance(Angle.class)) {
				return (Angle)_args.get(key);
			}
		}
		throw new NavAstroException("Cannot read "+ key.name());
	}
}
