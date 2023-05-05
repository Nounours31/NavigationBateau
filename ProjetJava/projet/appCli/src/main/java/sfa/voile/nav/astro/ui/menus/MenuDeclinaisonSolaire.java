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

import sfa.nav.astro.calculs.eCalculAstroConstantes;
import sfa.nav.lib.tools.NavAstroError;
import sfa.nav.model.Declinaison;
import sfa.nav.model.Heure;
import sfa.voile.nav.astro.ui.dialogues.DialogueForCalculDeclinaisonSolaire;

import java.nio.charset.StandardCharsets;

/**
 *
 * @author pierr
 */
public class MenuDeclinaisonSolaire extends MenuAdapteur {
	private static Logger _logger = LoggerFactory.getLogger(MenuDeclinaisonSolaire.class);


	public MenuDeclinaisonSolaire () {
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
                	DialogueForCalculDeclinaisonSolaire x = new DialogueForCalculDeclinaisonSolaire();
                	_args = new HashMap<eCalculAstroConstantes, Object>();
					x.demandeDesArguments(_args, methode);
					ResumeDeLaSaisie(_args, methode);
					
					Heure hDebutInterval = getHeureFromArgsList (eCalculAstroConstantes.HeureUT1);
					Heure hFinInterval = getHeureFromArgsList (eCalculAstroConstantes.HeureUT2);
					Heure HMeusure = getHeureFromArgsList (eCalculAstroConstantes.HeureMeusure);					
					Declinaison dDebutInterval = getDeclinaisonFromArgsList (eCalculAstroConstantes.Declinaison1);
					Declinaison dFinInterval = getDeclinaisonFromArgsList (eCalculAstroConstantes.Declinaison2);
					
					Declinaison res = _calculAstro.DeclinaisonSoleilParInterval(hDebutInterval, hFinInterval, dDebutInterval, dFinInterval, HMeusure);
					Resultat(res);
					break;
					
                case DeclainaisonSolaireParGradiant: 
                	x = new DialogueForCalculDeclinaisonSolaire();
                	_args = new HashMap<eCalculAstroConstantes, Object>();
					x.demandeDesArguments(_args, methode);
					ResumeDeLaSaisie(_args, methode);
					
					hDebutInterval = getHeureFromArgsList (eCalculAstroConstantes.HeureUTRef);
					hFinInterval = getHeureFromArgsList (eCalculAstroConstantes.HeureMeusure);
					HMeusure = getHeureFromArgsList (eCalculAstroConstantes.HeureUTRef);					
					dDebutInterval = getDeclinaisonFromArgsList (eCalculAstroConstantes.Declinaison1);
					dFinInterval = getDeclinaisonFromArgsList (eCalculAstroConstantes.Declinaison2);
					
					res = _calculAstro.DeclinaisonSoleilParInterval(hDebutInterval, hFinInterval, dDebutInterval, dFinInterval, HMeusure);
					Resultat(res);
                default:
                	displayErreur (err);
                	break;
            }
			}
		}
		catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		return retour;
	}


}
