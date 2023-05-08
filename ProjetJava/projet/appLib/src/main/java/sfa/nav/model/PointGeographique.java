package sfa.nav.model;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.tools.ToStringOptions;
import sfa.nav.model.tools.ToStringOptions.eToStringMode;

public class PointGeographique {
	private Latitude _latitude = null;;
	private Longitude _longitude = null;
	
	public PointGeographique (Latitude lat, Longitude Longi) throws NavException {
			_latitude = LatitudeFactory.fromDegre(lat.asDegre());
			_longitude = LongitudeFactory.fromDegre(Longi.asDegre());
	}
	
	public Latitude latitude() {
		return _latitude;
	}
	public void latitude(Latitude _latitude) throws NavException {
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
	
	
}
