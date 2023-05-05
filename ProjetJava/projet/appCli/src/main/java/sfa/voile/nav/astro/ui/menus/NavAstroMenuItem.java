/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.ui.menus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NavAstroMenuItem  {
	private static Logger _logger = LoggerFactory.getLogger(NavAstroMenuItem.class);

	private final eUINavAstroAllMenuItems _code;
	private final int _iRandClassentMenu;
	
	public NavAstroMenuItem(eUINavAstroAllMenuItems code, int iRandClassentMenu) {
		_code = code;
		_iRandClassentMenu = iRandClassentMenu;
	}


	public eUINavAstroAllMenuItems getCode() {
		return _code;
	}

	public int getIndice() {
		return _iRandClassentMenu;
	}

	public String getNom() {
		return _code.getDescriptif();
	}
}
