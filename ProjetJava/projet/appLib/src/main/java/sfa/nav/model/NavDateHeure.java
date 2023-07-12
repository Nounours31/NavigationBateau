/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.nav.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;

/**
 *
 * @author pierr
 */
public class NavDateHeure {
	private static final double toNano = 1_000_000_000.0;

	private static Logger _logger = LoggerFactory.getLogger(NavDateHeure.class);
	private static DateTimeFormatter formatterZ = DateTimeFormatter.ofPattern("yyyy/MM/dd kk:mm:ss z");
	private static ZoneId GMT = ZoneId.of("UTC");
	private static ZoneId _myZone = ZoneId.of("Europe/Paris");
	private static ZonedDateTime _ceMatinAMinuit = ZonedDateTime.of(LocalDate.now(), LocalTime.of(0, 0), _myZone);

	private ZonedDateTime _value = null;

	protected NavDateHeure() {
	}

	static private void initCeMatinAMinuit() {
		_ceMatinAMinuit = ZonedDateTime.of(LocalDate.now(), LocalTime.of(0, 0), _myZone);
	}

	static public ZoneId myZone() {
		return _myZone;
	}

	// Europe/Paris
	// 'GMT+2' or 'UTC+01:00'
	static public void myZone(String s) {
		_myZone = ZoneId.of(s);
		initCeMatinAMinuit();
	}

	public double getHeureDecimaleFromEpoch() {
		return _value.toEpochSecond() / (60.0 * 60.0);
	}

	public double asHeureDecimale() {
		return (_value.toEpochSecond() - _ceMatinAMinuit.toEpochSecond()) / (60.0 * 60.0);
	}

	public void setTodayHeureDecimale(double d) {
		int h = (int) Math.floor(d);
		double dMn = (d - (double) h) * 60.0;
		int iMn = (int) Math.floor(dMn);

		double dSec = (dMn - (double) iMn) * 60.0;
		int iSec = (int) Math.floor(dSec);

		double dNano = ((((d - (double) h) * 60.0 - (double) iMn) * 60.0) - iSec) * toNano;
		int iNano = (int) dNano;

		int nanoOffset = 0;
		if (iNano > 0) {
			nanoOffset = (int) (1.0 * toNano - iNano);
		}
		_value = ZonedDateTime.of(LocalDate.now(), LocalTime.of(h, iMn, iSec, iNano), _myZone);
		_value = _value.plusNanos(nanoOffset);
	}

	private ZonedDateTime getHeureGMT() {
		return _value.withZoneSameInstant(GMT);
	}

	public String toShortString() {
		return _value.withZoneSameInstant(_myZone).format(formatterZ);
	}

	@Override
	public String toString() {
		return _value.withZoneSameInstant(_myZone).format(formatterZ) + " [" + getHeureGMT().format(formatterZ) + "]";
	}

	public String toStringDecimale() {
		return Double.toString(getHeureDecimaleFromEpoch());
	}

	protected void setValeur(ZonedDateTime zdt) {
		_value = zdt;
	}

	public boolean avant(NavDateHeure heure) {
		return (this._value.isBefore(heure._value));
	}

	public boolean avantOrEqual(NavDateHeure h) {
		return (this._value.isBefore(h._value) || this._value.isEqual(h._value));
	}

	public boolean apresOrEqual(NavDateHeure h) {
		return (this._value.isAfter(h._value) || this._value.isEqual(h._value));
	}

	public ZonedDateTime add(double nbHeureDecimale) {
		long nanosecondes = (long) (nbHeureDecimale * 60 * 60 * NavDateHeure.toNano);
		return this._value.plusNanos(nanosecondes);
	}

	public double moins(NavDateHeure hReference) throws NavException {
		throw new NavException("Not implemented");
	}

	public boolean apres(NavDateHeure hFinInterval) throws NavException {
		throw new NavException("Not implemented");
	}

}
