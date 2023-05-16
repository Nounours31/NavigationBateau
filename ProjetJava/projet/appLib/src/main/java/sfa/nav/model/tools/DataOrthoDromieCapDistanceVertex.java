package sfa.nav.model.tools;

import sfa.nav.model.Cap;
import sfa.nav.model.Distance;
import sfa.nav.model.PointGeographique;

public class DataOrthoDromieCapDistanceVertex {
	public Cap _cap;
	public Distance _distance;
	public PointGeographique _vertex;
	
	@Override
	public String toString() {
		return "C:"+_cap.toString()+" - D:"+_distance.toString() + " - P:" + _vertex.toString();
	}
}
