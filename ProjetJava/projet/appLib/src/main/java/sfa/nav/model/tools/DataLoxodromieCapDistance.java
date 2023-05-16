package sfa.nav.model.tools;

import sfa.nav.model.Cap;
import sfa.nav.model.Distance;

public class DataLoxodromieCapDistance {
	public Cap _cap;
	public Distance _distance;
	
	@Override
	public String toString() {
		return "c:"+_cap.toString()+" - D:"+_distance.toString();
	}
}
