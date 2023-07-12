/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.ui.menus;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.eCalculAstroConstantes;
import sfa.nav.infra.tools.error.NavAstroError;
import sfa.nav.model.Declinaison;
import sfa.nav.model.NavDateHeure;
import sfa.voile.nav.astro.ui.dialogues.DialogueForCalculDeclinaisonSolaire;

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
					
					NavDateHeure hDebutInterval = getHeureFromArgsList (eCalculAstroConstantes.HeureUT1);
					NavDateHeure hFinInterval = getHeureFromArgsList (eCalculAstroConstantes.HeureUT2);
					NavDateHeure HMeusure = getHeureFromArgsList (eCalculAstroConstantes.HeureMeusure);					
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
