package sfa.nav.model;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.tools.Constantes;
import sfa.nav.model.tools.ToStringOptions;
import sfa.nav.model.tools.ToStringOptions.eToStringMode;

public class PointGeographique {
	private Latitude _latitude = null;;
	private Longitude _longitude = null;
	
	protected PointGeographique () {
	}
	
	public Latitude latitude() {
		return _latitude;
	}
	public void latitude(Latitude _latitude)  {
		this._latitude = LatitudeFactory.fromDegre(_latitude.asDegre());
	}
	public Longitude longitude() {
		return _longitude;
	}
	public void longitude(Longitude _longitude) {
		this._longitude = LongitudeFactory.fromDegre(_longitude.asDegre());
	}
	
	@Override
	public String toString() {
		ToStringOptions opts = new ToStringOptions(eToStringMode.light);
		return "PtGeo [" + _latitude.myToString(opts) + ", "+ _longitude.myToString(opts) + "]";
	}

	public boolean equals(PointGeographique p, double precisionDistanceMille) {
		double LatA = this.latitude().asRadian();
		double LatB = p.latitude().asRadian();
		double LongiA = this.longitude().asRadian();
		double LongiB = p.longitude().asRadian();
		double g = LongiA - LongiB;
		
		double cosM = Math.sin(LatA) * Math.sin(LatB) +  Math.cos(LatA) * Math.cos(LatB) * Math.cos(g);
		Angle M =  AngleFactory.fromRadian(Math.acos(cosM));
		double d = Math.abs(Constantes.RayonTerrestreEnKm * M.asRadian());
		
		if (d < precisionDistanceMille)
			return true;
		return false;	
	}
}
