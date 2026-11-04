package sfa.nav.maree.calculs;

import java.util.List;

import sfa.nav.model.NavDateHeure;
import sfa.nav.model.tools.eSensIntervalMaree;

public class IntervalHoraire {
	private NavDateHeure start = null;
	private NavDateHeure end = null;
	private eSensIntervalMaree sens = eSensIntervalMaree.X;
	
	public IntervalHoraire() {
		start = null;
		end = null;
		sens = eSensIntervalMaree.X;
	}
	
	public boolean hasStart() {
		return (start != null);
	}

	public NavDateHeure start() {
		return start;
	}

	public NavDateHeure end() {
		return end;
	}
	
	public void start(NavDateHeure h) {
		start = h;
	}

	public void end(NavDateHeure h) {
		end = h;
	}

	public void setMareePosition(eSensIntervalMaree x) {
		sens  = x;
	}
	
	@Override
	public String toString() {
		if (sens == eSensIntervalMaree.YaDeLeau)
			return "[" + start.toShortString() + "+++++++" +  end.toShortString() + "]";
		if (sens == eSensIntervalMaree.YaPasDeau)
			return "[" + start.toShortString() + "-------" +  end.toShortString() + "]";
		return "[" + start.toShortString() + "???????" +  end.toShortString() + "]";		
	}

	public static String toString(List<IntervalHoraire> x) {
		StringBuffer sb = new StringBuffer();
		for (IntervalHoraire y : x) {
			sb.append(y.toString());
		}
		return sb.toString();		
	}
}
