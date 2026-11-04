import re
from logging import Logger
from math import floor, pi
from re import Match

from TrameNMEANav.CLogger import CLogger


class CAngle:
    DEG2RAD=(pi / 180.0)
    RAD2DEG=(180.0/pi)
    CAS_DEGRE_DOUBLE = 0
    CAS_DEGRE_INT_MIN_DOUBLE = 1
    CAS_DEGRE_MIN_SEC = 2
    _logger = None

    def __init__(self, *,
                 angleDegreDouble: float = None,
                 angleDegreInt: int = None,
                 angleMinuteInt: int = None,
                 angleSecondeInt: int = None,
                 angleMinuteDouble: float = None):

        self._logger = CLogger.getLogger("CAngle")
        CAngle._logger = self._logger

        test: [bool] = [False, False, False]
        test[CAngle.CAS_DEGRE_DOUBLE] = not angleDegreDouble is None
        test[1] = (not angleDegreInt is None) and (not angleMinuteDouble is None)
        test[2] = (not angleDegreInt is None) and (not angleMinuteInt is None) and (not angleSecondeInt is None)

        # un des cas exist
        if not (test[CAngle.CAS_DEGRE_DOUBLE] or test[CAngle.CAS_DEGRE_INT_MIN_DOUBLE] or test[CAngle.CAS_DEGRE_MIN_SEC]):
            self._logger.error("Invalide init d'un angle ")
            self._angle = 0.0

        # cas par cas
        if test[CAngle.CAS_DEGRE_DOUBLE]:
            self._angle = angleDegreDouble

        if (not test[CAngle.CAS_DEGRE_DOUBLE]) and test[CAngle.CAS_DEGRE_INT_MIN_DOUBLE]:
            self._angle = angleDegreInt + angleMinuteDouble / 60.0

        if (not test[CAngle.CAS_DEGRE_DOUBLE]) and (not test[CAngle.CAS_DEGRE_INT_MIN_DOUBLE]) and test[CAngle.CAS_DEGRE_MIN_SEC]:
            self._angle = angleDegreInt + angleMinuteDouble / 60.0 + angleSecondeInt / 3600.0


    def __str__(self) -> str:
        asDouble, signe = self.toDecimal(False)
        asMinuteDecimale, signe =  self.toDegMinDecimal(False)
        asMinuteSexaDecimale, signe =  self.toDegMinSecDecimal(False)
        return f"{asDouble} [{asMinuteDecimale} - {asMinuteSexaDecimale}]"

    def __format__(self, format_spec):
        return str(self)

    def toDecimal(self, asUnSigned: bool) -> (str, int):
        val = self._angle
        signe: int = +1
        if val < 0:
            signe = -1
            val = -1.0 * val

        sSigne: str = "+"
        if val < 0:
            sSigne = "-"
            val = -1.0 * val

        if asUnSigned:
            sSigne = ""

        asDouble: str = f"{sSigne}{val:1.4f}°"
        return asDouble, signe

    def toDegMinDecimal(self, asUnSigned: bool) -> (str, int):
        val = self._angle
        signe: int = +1
        if val < 0:
            signe = -1
            val = -1.0 * val

        sSigne: str = "+"
        if val < 0:
            sSigne = "-"
            val = -1.0 * val

        if asUnSigned:
            sSigne = ""

        deg = floor(val)
        min = ((val - floor(val)) * 60.0)
        asMinuteDecimale = f"{sSigne}{deg:2d}°{min:2.4f}'"
        return asMinuteDecimale, signe

    def toDegMinSexaDecimalNMEA(self, asUnSigned: bool) -> str:
        val = self._angle
        signe: int = +1
        if val < 0:
            signe = -1
            val = -1.0 * val

        sSigne: str = "+"
        if val < 0:
            sSigne = "-"
            val = -1.0 * val

        if asUnSigned:
            sSigne = ""

        deg = floor(val)
        min = ((val - floor(val)) * 60.0)
        asMinuteDecimale = f"{sSigne}{deg:2d}.{min:2.4f}'"
        return asMinuteDecimale, signe

    def toDegMinSecDecimal(self, asUnSigned: bool) -> (str, int):
        val = self._angle
        signe: int = +1
        if val < 0:
            signe = -1
            val = -1.0 * val

        sSigne: str = "+"
        if val < 0:
            sSigne = "-"
            val = -1.0 * val

        if asUnSigned:
            sSigne = ""

        deg = floor(val)
        min = floor ((val - floor(val)) * 60.0)
        sec = val * 3600 - deg * 3600 - min * 60

        asMinuteSexaDecimale = f"{sSigne}{deg:2d}°{min:2d}'{sec:2.4f}\""
        return asMinuteSexaDecimale, signe


    def value(self, val: float = None):
        if val is None:
            return self._angle
        else:
            self._angle = val

    @staticmethod
    def fromString(s: str):
        retour: CAngle = CAngle(angleDegreDouble=0.0)

        regexAngle = r"([\-\+])?([0-9]{1,3})°([0-9]{1,2}(\.[0-9]+)?)[']?(([0-9]{1,2}(\.[0-9]+)?)[\"]?)?"

        parseError: bool = False
        m: Match[str] = re.match(regexAngle, s, re.MULTILINE)
        if m:
            maxGroup = len(m.groups())
            if maxGroup != 7:
                parseError = True

            signe: float = 1.0
            SIGNE = 1
            DEGRE = 2
            MIN = 3
            MINDEC = 4
            SECQUOTE = 5
            SEC = 6
            SECDEC = 7
            if m.group(SIGNE) == "-":
                signe = -1.0

            a: float = 0.0
            b : float = 0.0

            b = 0.0
            if not m.group(DEGRE) is None and m.group(DEGRE) != "":
                b = float(m.group(DEGRE))
            a += b

            b = 0.0
            if not m.group(MIN) is None and m.group(MIN) != "":
                b = float(m.group(MIN))
            a += b / 60.0

            b = 0.0
            if not m.group(SEC) is None and m.group(SEC) != "":
                b = float(m.group(SEC))
            a += b / 3600.0

            a = a * signe
            retour.value(a)

        else:
            parseError = True

        if parseError:
            self._logger.error(f"Ce n'est pas une latitude {s}s")

        return retour
