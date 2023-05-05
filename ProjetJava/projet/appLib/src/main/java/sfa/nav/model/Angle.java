package sfa.nav.model;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.lib.tools.Constantes;
import sfa.nav.lib.tools.NavException;
import sfa.nav.lib.tools.eToStringMode;

public class Angle {
	private static Logger logger = LoggerFactory.getLogger(Angle.class);
	private double _angleInDegre;
	private boolean _status;

	final private static String NonInitMsg = Constantes.AngleNonInitialisedMsg;


	protected Angle() {
		set (0);
		valide(false);
	}

	protected Angle(Angle a) throws NavException {
		if (a == null) {
			set (0);
			valide(false);
		}
		else {
			set (a.asDegre());
			valide(a.valide());
		}
	}

	
	public double asRadian() throws NavException {
		if (!_status)
			throw new NavException(NonInitMsg);
		return Angle.DegreToRadian(_angleInDegre);
	}
	
	public double asDegre() throws NavException {
		if (!_status)
			throw new NavException(NonInitMsg);
		return _angleInDegre;
	}

	protected void valide(boolean b) {
		_status = b;
	}
	protected boolean valide() {
		return _status;
	}

	protected void set(double d) {
		while (d >= 360.0)
			d -= 360.0;

		while (d < 0.0)
			d += 360.0;
		
		_angleInDegre = d;
		
		valide(true);
	}
	
	
	
	



	
	@Override
	public String toString() {
		if (!valide())
			return NonInitMsg;
		
		return myToString(eToStringMode.or(eToStringMode.full, eToStringMode.deg, eToStringMode.rad));
	}

	public String myToString(int mode) {
		if (!valide())
			return NonInitMsg;
		
		double angle2Print = 0.0;
		try { angle2Print = this.asDegre();} catch (NavException e1) {}
				
		double h = Math.floor(   angle2Print);
		double m = Math.floor(  (angle2Print - h) * 60.0);
		double s = ((((angle2Print - h) * 60.0) - m ) * 60.0);
		
		DecimalFormat dfPosition = new DecimalFormat("000.000°");
		DecimalFormat dfSeconde = new DecimalFormat("00.00\"");
		DecimalFormat dfRad = new DecimalFormat("0.00 Rad");
		StringBuffer sb = new StringBuffer();

		if (eToStringMode.isA(mode, eToStringMode.light)) {
			sb.append(dfPosition.format(angle2Print));
		}
		else if (eToStringMode.isA(mode, eToStringMode.full)) {
			sb.append(dfPosition.format(angle2Print));
			
			if (eToStringMode.isA(mode, eToStringMode.deg))
				sb.append(String.format("[%02d°%02d'%s]", (int)Math.floor(h), (int)Math.floor(m), dfSeconde.format(s)));			

			if (eToStringMode.isA(mode, eToStringMode.rad)) {
				double asRad = 0.0;
				try { asRad = this.asRadian(); } catch (NavException e) {}
				sb.append(String.format("[%s]", dfRad.format(asRad)));		
			}
		}
		else {
			sb.append("xxxxxxxxxxx");
		}
		return sb.toString();
	}


	public static double DegreToRadian(double asDegre) {
		return asDegre * Constantes.DEG2RAD;
	}
	public static double RadianToDegre(double asRadian) {
		return asRadian * Constantes.RAD2DEG;
	}

	public Angle plus(Angle dz) throws NavException {
		Angle retour = new Angle(this);
		retour.set(this._angleInDegre + dz._angleInDegre);
		return retour;
	}
	
	@Deprecated
	public Angle plus(double dz) throws NavException {
		throw new NavException("deprecated");
	}

	@Deprecated
	public double multiply(double nbHeure) throws NavException {
		throw new NavException("deprecated");
	}
}
