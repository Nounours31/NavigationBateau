/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.toolsUI.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NavAstroMenuItems  {
	private static Logger _logger = LoggerFactory.getLogger(NavAstroMenuItems.class);

	ArrayList<NavAstroMenuItem> _Item = null;
	
	

	private ArrayList<NavAstroMenuItem> valuesSorted() {
		_Item.sort(Comparator.comparingInt(v -> v.getIndice()));
		return _Item;
	}

	private void displayMenu() {
		System.out.println("Possibilitées:");
		for (NavAstroMenuItem uneMethode : _Item) {
			System.out.println(String.format("\t%1d. %s", uneMethode.getIndice(), uneMethode.getNom()));
		}
	}
	
	private eUINavAstroContante readAndCheck(String input) {
		try {
			final int iChoix = Integer.parseInt(input);
			return _Item
					.stream()
					.filter(v -> v.getIndice() == iChoix)
					.findFirst().get().getCode();
		}
		catch (Exception e) {
			_logger.error("Error ...", e);
		}
		return null;
	}
	
	public eUINavAstroContante dialogue() {
		eUINavAstroContante retour = eUINavAstroContante.Sortir;
		
		while (true) {
			displayMenu();
			try {
				String s = UtilitairesUI.readInput();
				retour = readAndCheck(s);
				return retour;
			}
			catch (Exception e) {}
		}
	}
}
