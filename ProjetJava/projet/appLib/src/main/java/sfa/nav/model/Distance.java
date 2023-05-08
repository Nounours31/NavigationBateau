package sfa.nav.model;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.tools.Constantes;
import sfa.nav.model.tools.ToStringOptions;
import sfa.nav.model.tools.ToStringOptions.eToStringMode;

public class Distance  {
	double _distanceInMille = 0.0;
	
	protected Distance() {
		_distanceInMille = 0.0;
	}

	public double distanceInKm() {
		return _distanceInMille * Constantes.milleMarinEnMetre / 1000.0;
	}

	public double distanceInMilleNautique() {
		return _distanceInMille;
	}


	@Override
	public String toString() {
		ToStringOptions opts = new ToStringOptions(ToStringOptions.or(eToStringMode.full, eToStringMode.KM, eToStringMode.MilleNautique));
		return myToString(opts);
	}

	public String myToString(ToStringOptions opts) {
		StringBuffer sb = new StringBuffer();

		if (opts.isA(eToStringMode.light)) {
			sb.append(String.format("%02.1f mn", distanceInMilleNautique()));
		}
		else if (opts.isA( eToStringMode.full)) {
			if (opts.isA(eToStringMode.MilleNautique))
				sb.append(String.format("[%02.1f Mn]", distanceInMilleNautique()));			

			if (opts.isA( eToStringMode.KM)) {
				sb.append(String.format("[%02.1f Km]", distanceInKm()));			
			}
		}
		else {
			sb.append("xxxxxxxxxxx");
		}
		return sb.toString();
	}

	
	protected void distanceInkm(double km) {
		_distanceInMille = km * 1000.0 / Constantes.milleMarinEnMetre;
	}


	public void distanceInMille(double mn) {
		_distanceInMille  = mn;
	}

}
