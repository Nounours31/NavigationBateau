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
public class Latitude extends Angle {
    protected Logger _logger = LoggerFactory.getLogger(Latitude.class);;
    
    public enum SensLatitude {
        Nord, Sud;

        public static SensLatitude inverse(SensLatitude sens) {
            if (sens == Nord) return Sud;
            return Nord;
        }
    }

    protected SensLatitude _Sens = SensLatitude.Nord;

    
    public Latitude() {
        super (0.0);
    }

    public double getLatitude() {
        return getVal();
    }

    public void setLatitude(double x) {
        _val = x;
    }


    public SensLatitude getSens() {
        return _Sens;
    }

    public void setSens(SensLatitude _Sens) {
        this._Sens = _Sens;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Latitude) {
            if ((((Latitude)o)._val == _val) && (((Latitude)o)._Sens == _Sens)) 
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format ("Latitude %s %s [Semidecimale: %s %s] [decimale: %s %f]", _Sens, this.toDegreMinuteSeconde(), _Sens, this.toDegreMinuteDecimale(), _Sens, this._val);
    }
    

}
