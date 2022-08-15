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
public class Longitude extends Angle {
    protected Logger _logger = LoggerFactory.getLogger(Longitude.class);;
    
    public enum SensLongitude {
        Est, West;

        public static SensLongitude inverse(SensLongitude sens) {
            if (sens == Est) return West;
            return Est;
        }
    }

    protected SensLongitude _Sens = SensLongitude.West;

    
    public Longitude() {
        super (0.0);
    }

    public double getLongitude() {
        return getVal();
    }

    public void setLongitude(double x) {
        setVal(x);
    }


	public SensLongitude getSens() {
        return _Sens;
    }

    public void setSens(SensLongitude _Sens) {
        this._Sens = _Sens;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Longitude) {
            if ((((Longitude)o)._val == _val) && (((Longitude)o)._Sens == _Sens)) 
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format ("v %s %s [Semidecimale: %s %s] [decimale: %s %f]", _Sens, this.toDegreMinuteSeconde(), _Sens, this.toDegreMinuteDecimale(), _Sens, this._val);
    }
    

}
