/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.nav.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.lib.tools.NavException;


/**
 *
 * @author pierr
 */
public class Heure {

    private static Logger _logger = LoggerFactory.getLogger(Heure.class);
    private double _val = 0.0;

    public Heure(double v) {
        _val = v;
    }

	public Heure() {
		_val = 0.0;
	}

    public Heure(Heure h) {
    	_val = h.getVal();
	}




	public String toHeureMinuteSeconde() {
        double x = _val;
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

    public double getVal() {
        return _val;
    }

    public void setVal(double d) {
        _val = d;
    }

    public Heure plus(double dz) {
        return new Heure(this._val + dz);
    }

    public Heure plus(Heure Ei) {
        return new Heure(this._val + Ei._val);
    }

    @Override
    public String toString() {
        return "Heure: " + this.toHeureMinuteSeconde() + "  [" + _val + "]";
    }

    public boolean apres(Heure H) {
        return (_val > H._val);
    }

    public boolean avant(Heure H) {
        return !apres(H);
    }

    public double moins(Heure H) {
        return _val - H._val;
    }

    @Deprecated
	public static Heure fromString(Object object) throws NavException {
		throw new NavException("Not impl");
	}

}
