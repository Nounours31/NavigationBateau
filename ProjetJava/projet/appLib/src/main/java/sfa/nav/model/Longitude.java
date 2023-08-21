package sfa.nav.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.model.tools.ToStringOptions;
import sfa.nav.model.tools.ePointsCardinaux;

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

	protected Longitude() {
		super();
		super.setOriente();
	}

	@Override
	public double asRadian() {
		return Angle.DegreToRadian(asDegre());
	}

	@Override
	public double asDegre() {
		return super.asDegre();
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

	public ePointsCardinaux getSens() {
		if ((super.asDegre() >= 0.0) && (super.asDegre() <= 180.0))
			return ePointsCardinaux.Est;
		if ((super.asDegre() < 0.0) && (super.asDegre() >= -180.0))
			return ePointsCardinaux.Ouest;
		return ePointsCardinaux.Error;
	}


	public static boolean isValideAngleInDegre(double x) {
		if (Math.abs(x) <= 180.0)
			return true;
		return false;
	}
	
	public Longitude plusDegre(double variationLongitudeEnDegre) {
		double inDegre = this.asDegre();
		inDegre += variationLongitudeEnDegre;
		
		Angle a = AngleFactory.fromDegre(inDegre);
		if (a.asDegre() < 180.0)
			return LongitudeFactory.fromDegre(inDegre);
		
		inDegre = a.asDegre() - 360.0;
		return LongitudeFactory.fromDegre(inDegre);
	}
}
