/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.ui.menus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NavAstroMenuItem  {
	private static Logger _logger = LoggerFactory.getLogger(NavAstroMenuItem.class);

	private final eUINavAstroContante _code;
	private final int _indice;
	private final String _nom;
	
	protected NavAstroMenuItem(eUINavAstroContante code, int i, String txt) {
		_code = code;
		_indice = i;
		_nom = txt;
	}


	eUINavAstroContante getCode() {
		return _code;
	}

	private int getIndice() {
		return _indice;
	}

	String getNom() {
		return _nom;
	}
}
