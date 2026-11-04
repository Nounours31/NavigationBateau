package sfa.nav.astro.calculs;

import sfa.nav.model.AngleOriente;

public class ErreurSextan {
	final AngleOriente collimacon;
	final AngleOriente exentricite;

	public ErreurSextan(AngleOriente sextan_collimasson, AngleOriente sextan_exentricite) {
		collimacon = sextan_collimasson;
		exentricite = sextan_exentricite;
	}

	@Override
	public String toString() {
		return "ErreurSextan [collimacon=" + collimacon 
				+ ", exentricite=" + exentricite + "]";
	}

	public String toCanevas() {
		return "Sextan [collimacon=[" + collimacon.toCanevas() + "], exentricite=[" + exentricite.toCanevas() + "]]";
	}

	public final AngleOriente collimacon() {
		return collimacon;
	}

	public final AngleOriente exentricite() {
		return exentricite;
	}
}
