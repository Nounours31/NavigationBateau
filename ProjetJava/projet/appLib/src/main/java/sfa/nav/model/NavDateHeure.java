/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.nav.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Formatter.BigDecimalLayoutForm;

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

	private ZonedDateTime _value = null;

	protected NavDateHeure() {
	}

	private enum NavMoisDeAnnee {
		Janvier (0), Fevrier(1), Mars(2), Avril(3), Mai(4), Juin(5), Juillet(6), Aout(7), Septembre(8), Octobre(9), Novembre(10), Decembre(11);
		
		public static NavMoisDeAnnee FromNavDateHeure(NavDateHeure x) {
			int i = x._value.getMonthValue();
			switch (i) {
			case 1: return Janvier;
			case 2: return Fevrier;
			case 3: return Mars;
			case 4: return Avril;
			case 5: return Mai;
			case 6: return Juin;
			case 7: return Juillet;
			case 8: return Aout;
			case 9: return Septembre;
			case 10: return Octobre;
			case 11: return Novembre;
			case 12: return Decembre;
			default: return Janvier;
			}
		}
		
		private final int ordinal;
		private NavMoisDeAnnee(int i) { ordinal = i; }
		public int indice() { return ordinal; }
	}
	
	public int getMois() {
		 return NavMoisDeAnnee.FromNavDateHeure(this).indice();
	}
	
	
	static public ZoneId myZone() {
		return _myZone;
	}

	// Europe/Paris
	// 'GMT+2' or 'UTC+01:00'
	static public void myZone(String s) {
		_myZone = ZoneId.of(s);
	}

	public double asHeureDecimaleFromMidnight() {
		LocalTime heureMinuit = LocalTime.of(0, 0, 0);
		ZonedDateTime aujourdhuiMinuit = ZonedDateTime.of(_value.toLocalDate(), heureMinuit , _myZone);
		return (_value.toEpochSecond() - aujourdhuiMinuit.toEpochSecond()) / (60.0 * 60.0);
	}

	
	public double asHeureDecimaleEpoch() {
		return _value.toEpochSecond() / (60.0 * 60.0);
	}

	public void setTodaysecondes(long s) {
		LocalTime heureMinuit = LocalTime.of(0, 0, 0);
		ZonedDateTime aujourdhuiMinuit = ZonedDateTime.of(LocalDate.now(), heureMinuit , _myZone);
		
		Instant i = Instant.ofEpochSecond(aujourdhuiMinuit.toEpochSecond() + s);
		_value = ZonedDateTime.ofInstant(i, _myZone);
	}

	public void setTodayHeureDecimale(double d) {
		LocalTime heureMinuit = LocalTime.of(0, 0, 0);
		ZonedDateTime aujourdhuiMinuit = ZonedDateTime.of(LocalDate.now(), heureMinuit , _myZone);
		
		Instant i = Instant.ofEpochSecond(aujourdhuiMinuit.toEpochSecond() + (long) (d * 60.0 * 60.0));
		_value = ZonedDateTime.ofInstant(i, _myZone);
	}
	
	public void setEpochsecondes(long s) {
		Instant i = Instant.ofEpochSecond(s);
		_value = ZonedDateTime.ofInstant(i, _myZone);
	}

	public void setEpochHeureDecimale(double d) {
		Instant i = Instant.ofEpochSecond((long) (d * 60.0 * 60.0));
		_value = ZonedDateTime.ofInstant(i, _myZone);
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
		return Double.toString(asHeureDecimaleEpoch());
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

	public NavDateHeure plusHeureDecimale(double nbHeureDecimale) {
		return NavDateHeureFactory.fromHeureDecimale(this.asHeureDecimaleEpoch() + nbHeureDecimale);
	}

	public NavDateHeure add(NavDateHeure h) {
		return this.plusHeureDecimale(h.asHeureDecimaleEpoch());
	}

	public NavDateHeure moins(NavDateHeure h) throws NavException {
		return this.moinsHeureDecimale(h.asHeureDecimaleEpoch());
	}

	public NavDateHeure moinsHeureDecimale(double nbHeureDecimale) throws NavException {
		return NavDateHeureFactory.fromHeureDecimale(this.asHeureDecimaleEpoch() - nbHeureDecimale);
	}

	public boolean apres(NavDateHeure hFinInterval) throws NavException {
		return !this.avantOrEqual(hFinInterval);
	}

	public static NavDateHeure moyenne(NavDateHeure debut, NavDateHeure fin) throws NavException {
		return NavDateHeureFactory.fromHeureDecimale((fin.asHeureDecimaleEpoch() + debut.asHeureDecimaleEpoch())/ 2.0);
	}
}
