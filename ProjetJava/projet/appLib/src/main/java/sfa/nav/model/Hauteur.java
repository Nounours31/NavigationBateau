package sfa.nav.model;

public class Hauteur {
	public double _d = 0.0;

	public Hauteur() {
		_d = 0.0;
	}

	public Hauteur(Hauteur h) {
		_d = h._d;
	}

	public Hauteur(double hEnMetre) {
		_d = hEnMetre;
	}

	public boolean plusHaut(Hauteur h) {
		return (_d >= h._d);
	}

	public boolean plusHaut(double hEnMetre) {
		return (_d >= hEnMetre);
	}

	public boolean plusBasse(double hEnMetre) {
		return (_d <= hEnMetre);
	}

	public boolean plusBasse(Hauteur h) {
		return (_d <= h._d);
	}

	public Hauteur moins(Hauteur h) {
		return new Hauteur(_d - h._d);
	}

	public double getValInMetre() {
		return _d;
	}

	@Override
	public String toString() {
		return "Hauteur [_d=" + _d + "]";
	}

}
