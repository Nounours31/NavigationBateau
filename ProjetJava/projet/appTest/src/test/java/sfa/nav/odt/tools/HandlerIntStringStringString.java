package sfa.nav.odt.tools;

import java.util.ArrayList;
import java.util.List;

public class HandlerIntStringStringString {
	public int 		_i;
	public String 	_u;
	public String 	_v;
	public String 	_w;
	public HandlerIntStringStringString(int i, String u, String v, String w) {_i = i; _u = u; _v = v; _w = w;}
	
	static public List<HandlerIntStringStringString> fromStringStringArray (int[] icas, String[][] allInfos) {
		List<HandlerIntStringStringString> retour = new ArrayList<>();
		int i = 0;
		for (String[] s : allInfos) {
			if ((s != null) && (s.length == 3) && (s[0] != null) && (s[1] != null) && (s[2] != null)) {
				HandlerIntStringStringString x = new HandlerIntStringStringString(icas[i++], s[0], s[1], s[2]);
				retour.add(x);
			}
		}
		return retour;
	}
}
