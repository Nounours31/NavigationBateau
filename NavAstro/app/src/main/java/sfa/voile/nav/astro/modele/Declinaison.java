/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.modele;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 * @author pierr
 */
public class Declinaison extends Latitude {
    private Logger _logger = LoggerFactory.getLogger(Declinaison.class);;
    
    public Declinaison() {
        super();
    }

    public Declinaison(Latitude l) {
        super();
        setSens(l._Sens);
        setLatitude(l._val);
    }

	public double getDeclinaison() {
		return getLatitude();
	}

}
