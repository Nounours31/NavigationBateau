import math
from datetime import datetime
from enum import Enum

from sfa_navigation import cLatitude, cLongitude

class cConstanteForNMEA(Enum):
    FORMAT_LAT = "FORMAT_LAT"
    FORMAT_LONGI = "FORMAT_LONGI"

class cNmeaTools:
    def __init__(self):
        pass

    # ----------------------------------------------------------------------------------
    # This is a simple calculator to compute the checksum field for the NMEA protocol.
    # The checksum is simple, just an XOR of all the bytes between the $ and the * (not including the delimiters themselves), and written in hexadecimal.
    # ----------------------------------------------------------------------------------
    @staticmethod
    def _NMEAChecksum(s : str) -> str :
        xor = ord(s[0]) ^ ord(s[1])
        for i in range(2, len(s)):
            xor = xor ^ ord(s[i])

        retour = hex(xor)[2:].upper()
        if len(retour) < 2:
            retour = "0" + retour
        return retour

    @staticmethod
    def _nmeaLatitudeFormat(l: cLatitude) -> str :
        return cNmeaTools._nmeaLatLongFormat (l.latitudeEnDeg, cConstanteForNMEA.FORMAT_LAT)

    @staticmethod
    def _nmeaLongitudeFormat(l: cLongitude) -> str :
        return cNmeaTools._nmeaLatLongFormat (l.longitudeEnDeg, cConstanteForNMEA.FORMAT_LONGI)

    @staticmethod
    def _nmeaLatLongFormat(x: float, f : cConstanteForNMEA) -> str :
        # E 7,682288°  -> 7° 40' 56.238" Est --> 00740.9373,E
        x = abs(x)
        deg : int = int(math.floor(x))
        min : float = (x - deg) * 60.0

        if f == cConstanteForNMEA.FORMAT_LONGI:
            return f"{deg:03d}{min:07.4f}"
        if f == cConstanteForNMEA.FORMAT_LAT:
            return f"{deg:02d}{min:07.4f}"
        return "xxxxxx"

    @staticmethod
    def date2GPSDecimale(d : datetime) -> int :
        # 100106       : date exprimée en format « jjmmaa » : 10 janvier 2006
        return int(d.day * 10000 + d.month * 100 + (d.year - 100 * math.floor (d.year / 100)))

    @staticmethod
    def heure2GPSDecimale(h : datetime) -> float :
        #  064036.289   : Trame envoyée à 06 h 40 min 36 s 289 (heure UTC)
        return ((h.hour * 100 + h.minute) * 100) + h.second + (h.microsecond / 1000000)

    # ----------------------------------------------------------------------------------
    # doit retourner un angle en degre decimal
    # ----------------------------------------------------------------------------------
    @staticmethod
    def angleSexaToDecimal(degre : int, minute: float) -> float :
        x = 1 if degre >= 0 else -1
        return x * (abs(degre) + abs(minute) / 60)

    # ----------------------------------------------------------------------------------
    # doit retourner un angle en degre decimal / minute sagedecimal
    # ----------------------------------------------------------------------------------
    @staticmethod
    def angleDecimalToMinuteSexa(degreDecimal : float) -> float :
        x = 1 if degreDecimal >= 0 else -1
        y = abs(degreDecimal)
        degre = math.floor(y)
        minute = (y - degre) * 60 / 100 
        return x * (degre + minute)