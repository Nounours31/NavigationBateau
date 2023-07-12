package sfa.nav.model.tools;

import sfa.nav.model.Cap;
import sfa.nav.model.Distance;

public class DataLoxodromieCapDistance {
	private Cap _cap;
	private Distance _distance;
	
	public Cap cap() {
		return _cap;
	}
	public Distance distance() {
		return _distance;
	}
	
	public void cap(Cap c) {
		_cap = c;
	}
	public void distance(Distance d) {
		_distance = d;
	}

	
	@Override
	public String toString() {
		return "c:"+_cap.toString()+" - D:"+_distance.toString();
	}
}
