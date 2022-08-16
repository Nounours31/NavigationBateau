/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.modele;

/**
 *
 * @author pierr
 */
public class DeclinaisonParser extends LatitudeParser {    

	public DeclinaisonParser() {
		super();
	}

	private static DeclinaisonParser _instance = null;

	public static DeclinaisonParser getInstance() {
		if (_instance  == null) 
			_instance = new DeclinaisonParser();
		return _instance;
	}
}
