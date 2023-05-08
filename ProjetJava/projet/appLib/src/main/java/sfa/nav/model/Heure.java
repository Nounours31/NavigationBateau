/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.nav.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;


/**
 *
 * @author pierr
 */
public class Heure {

    private static Logger _logger = LoggerFactory.getLogger(Heure.class);
    private double _heuredecimale = 0.0;
    private boolean isValide = true;

    protected Heure() {
    }

    

	public String toHeureMinuteSeconde() {
        double x = _heuredecimale;
        StringBuffer sb = new StringBuffer();
        if (x < 0) {
            sb.append("-");
        }
        x = Math.abs(x);

        sb.append(String.format("%02d", (int) Math.floor(x)));
        sb.append(":");
        x -= Math.floor(x);
        x *= 60.0;

        sb.append(String.format("%02d", (int) Math.floor(x)));
        sb.append(":");
        x -= Math.floor(x);
        x *= 60.0;

        sb.append(String.format("%02.02f", x));

        return sb.toString();
    }

    public double getHeureDecimale() {
        return _heuredecimale;
    }


    public Heure plus(Heure h) {
        return HeureFactory.fromHeureDecimale(this._heuredecimale + h._heuredecimale);
    }

    @Override
    public String toString() {
        return "Heure: " + this.toHeureMinuteSeconde() + "  [" + _heuredecimale + "]";
    }

    public boolean apres(Heure H) {
        return (_heuredecimale > H._heuredecimale);
    }

    public boolean avant(Heure H) {
        return !apres(H);
    }

    public double moins(Heure H) {
        return _heuredecimale - H._heuredecimale;
    }



	public void set(double d) {
		_heuredecimale = d;
	}


}
