/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.ui.menus;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.voile.nav.astro.methodes.CalculsAstro;
import sfa.voile.nav.astro.methodes.eCalculAstroConstantes;
import sfa.voile.nav.astro.modele.Declinaison;
import sfa.voile.nav.astro.modele.Heure;
import sfa.voile.nav.astro.tools.NavAstroError;
import sfa.voile.nav.astro.ui.dialogues.DialogueForCalculDeclinaisonParInterval;
import sfa.voile.nav.astro.ui.tools.DataReaders;

import java.nio.charset.StandardCharsets;

/**
 *
 * @author pierr
 */
public class MenuDeclainaisonSolaire extends MenuAdapteur {
	private static Logger _logger = LoggerFactory.getLogger(MenuDeclainaisonSolaire.class);


	public MenuDeclainaisonSolaire () {
		_Item = new NavAstroMenuItem[] {
				new NavAstroMenuItem (eUINavAstroAllMenuItems.Sortir, 1),
				new NavAstroMenuItem (eUINavAstroAllMenuItems.DeclainaisonSolaireParInterval, 2),		
				new NavAstroMenuItem (eUINavAstroAllMenuItems.DeclainaisonSolaireParGradiant, 3)	
		};
	}

	@Override
	public boolean affichageItemsDuMenu() {
		boolean retour = false;
		try {
			NavAstroError err = new NavAstroError();
			while (retour == false) {
				displayMenu();
				eUINavAstroAllMenuItems methode = getUserChoiceAndValidedIt(err);
				if (methode == null)
					continue;

				switch (methode) {
                case Sortir: 
                	retour = true;
                	break;
                case DeclainaisonSolaireParInterval: 
                	DialogueForCalculDeclinaisonParInterval x = new DialogueForCalculDeclinaisonParInterval();
                	_args = new HashMap<eCalculAstroConstantes, Object>();
					x.demandeDesArguments(_args, methode);
					
					Heure hDebutInterval = getHeureFromArgsList (eCalculAstroConstantes.HeureUT1);
					Heure hFinInterval = getHeureFromArgsList (eCalculAstroConstantes.HeureUT2);
					Heure HMeusure = getHeureFromArgsList (eCalculAstroConstantes.HeureUTRef);					
					Declinaison dDebutInterval = getDeclinaisonFromArgsList (eCalculAstroConstantes.Declinaison1);
					Declinaison dFinInterval = getDeclinaisonFromArgsList (eCalculAstroConstantes.Declinaison2);
					
					_calculAstro.DeclinaisonSoleilParInterval(hDebutInterval, hFinInterval, dDebutInterval, dFinInterval, HMeusure);
                default:
                	displayErreur (err);
                	break;
            }
			}
		}
		catch (Exception e) {}
		return retour;
	}


}
