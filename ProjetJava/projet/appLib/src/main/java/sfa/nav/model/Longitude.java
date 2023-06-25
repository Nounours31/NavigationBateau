package sfa.nav.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.tools.Constantes;
import sfa.nav.model.tools.ToStringOptions;
import sfa.nav.model.tools.ePointsCardinaux;
import sfa.nav.model.tools.ToStringOptions.eToStringMode;

// -----------------------------------------------------------------
// Une longitude, généralement notée λ, est donc une mesure angulaire sur 360° par rapport à un méridien de référence, 
// avec une étendue de 
//
//		−180°, vers l'ouest, à +180°, vers l'est. 
//
// Par convention, le méridien de référence, qui correspond aux points de longitude 0°, est le méridien de Greenwich.
//-----------------------------------------------------------------
public class Longitude extends Angle {
	private static Logger logger = LoggerFactory.getLogger(Longitude.class);
	
	
	protected Longitude()  {
		super ();
	}

	
	@Override
	public double asRadian()  {
		return Angle.DegreToRadian (asDegre());
	}
	
		

	
	@Override
	public double asDegre()  {
		if (getSens() == ePointsCardinaux.Est) {
			return super.asDegre();
		}
		else if (getSens() == ePointsCardinaux.Ouest) {
			return super.asDegre() - 360.0;
		}
		return 0.0;
	}



	@Override
	public String toString() {
		Angle a = AngleFactory.fromDegre(Math.abs(this.asDegre()));
		return "Long:" + a.toString() + " " + getSens().getTag();
	}

	public String myToString(ToStringOptions opts) {
		Angle a = AngleFactory.fromDegre(Math.abs(this.asDegre()));
		return "Long:" + a.myToString(opts) + " " + getSens().getTag();
	}


	public ePointsCardinaux getSens()  {
		if (super.asDegre() <= 180.0) return ePointsCardinaux.Est;
		if (super.asDegre() > 180.0) return ePointsCardinaux.Ouest;
		return ePointsCardinaux.Error;
	}


	public void inverseSens() {
		set(super.asDegre() * (-1.0));
	}

	public static boolean isValideAngleInDegre(double x) {
		Angle a = AngleFactory.fromDegre(x);
		if (a.asDegre() <= 180.0) return true;
		if (a.asDegre() > 180.0) return true;
		return false;		
	}
}
