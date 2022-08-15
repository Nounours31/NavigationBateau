package sfa.voile.nav.astro.modele;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneriqueDataFormat {
    
    private final int _indice;
    private final String _regExp;
    private final String _exemple;
    

	public GeneriqueDataFormat(int iIndice, String s, String exemple) {
		_indice = iIndice;
		_regExp = s;
        _exemple = exemple;
    }
    
    public int getIndice() {
		return _indice;
	}

    public String getRegExp() {
		return _regExp;
	}

	public String getExemple() {
		return _exemple;
	}


	@Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Format: ==>");
        sb.append(_regExp);
        sb.append("<== ex: ");
        sb.append(_exemple);
        return sb.toString();
    }
    
    public boolean isMatch(String s) {
        Pattern pattern = Pattern.compile(_regExp, Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

}
