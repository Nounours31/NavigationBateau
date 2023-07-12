package sfa.nav.infra.tools.error;

public class NavAstroError extends Error {
	private static final long serialVersionUID = 2228105365799579025L;
	String _monMessage = "";

	public NavAstroError() {
	}

	public NavAstroError(String message) {
		super(message);
	}

	public NavAstroError(Throwable cause) {
		super(cause);
	}

	public NavAstroError(String message, Throwable cause) {
		super(message, cause);
	}

	public NavAstroError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	@Override
	public String getMessage() {
		String s = _monMessage;
		if (super.getMessage() != null)
			s = s + "\n" + super.getMessage();
		return s;
	}

	public void setMessage(String message) {
		_monMessage = _monMessage + "[" + message + "]";
	}
}
