package sfa.voile.nav.astro.tools;

public class NavAstroError extends Error {
	String _monMessage = "";
	
	public NavAstroError() {
		// TODO Auto-generated constructor stub
	}

	public NavAstroError(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NavAstroError(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public NavAstroError(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NavAstroError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getMessage() {
		String s =  _monMessage;
		if (super.getMessage() != null)
			s = s + "\n" + super.getMessage();
		return s;
	}

	public void setMessage(String message) {
		_monMessage = _monMessage + "[" + message + "]";
	}

}
