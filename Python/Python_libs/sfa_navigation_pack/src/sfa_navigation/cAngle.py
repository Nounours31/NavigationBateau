from __future__ import annotations

import math
import re
from enum import Enum

from sfa_tools import cMyException

"""
Package des gestion des angles

"""
class eAngleFormat(Enum):
    DMS = "DMS"
    DMM = "DMM"
    DD = "DD"

class cAngle:
    """
    Gestion des angles

    Attention au formats:
        DMS - Degrees, Minutes, Seconds (DMS): e.g. "N 50° 7' 20.9122", "W 8° 39' 56.52"
        DMM - Degrees and Decimal Minutes (DMM): e.g. "50° 7.34854N", "8° 39.942W"
        DD  - Decimal Degrees (DD): e.g. "+50.12257", "-8.66570"
    """
    RAD2DEG: float = 180.0 / math.pi
    DEG2RAD: float = math.pi / 180.0


    STR_AsREAL : int = 3
    STR_AsMin : int = 1
    STR_AsSec : int = 2
    STR_AsALL : int = 0
    STR_Echec : int = 4

    DISPLAY_SHORT : int = 0
    DISPLAY_LONG : int = 1

    """
    constructeur avec priorite au degre
    """
    def __init__(self, valAsDeg : float | None = 0.0, valAsRad : float | None = 0.0):
        if valAsDeg is not None :
            self._angleEnDeg : float = valAsDeg
        elif valAsRad is not None :
            self._angleEnDeg : float = valAsRad * cAngle.RAD2DEG
        else:
            self._angleEnDeg = 0.0


    def toString(self, format : eAngleFormat = eAngleFormat.DD) -> str :
        """
        :param base: 0 en reel, 10 en base 10, 60 en sexagedecimal
        :return: angle as string
        """

        sign : str = "" if self._angleEnDeg >= 0.0 else "-"

        abs : float = math.fabs(self._angleEnDeg)
        d : int = math.floor (abs)
        minute : float = (abs - d) * 60.0
        minuteAsInt : int = math.floor(minute)
        seconde : float = (minuteAsInt - math.floor (minute)) * 60.0

        if format == eAngleFormat.DD:
            return f"{sign}{abs:08.4f}°"
        elif format == eAngleFormat.DMM:
            return f"{sign}{d:03d}°{minute:06.3f}'"
        elif format == eAngleFormat.DMS:
            return f"{sign}{d:03d}°{minuteAsInt:02d}'{seconde:06.3f}\""
        else:
            return f"{sign}{abs:08.4f}° [{sign}{d:03d}°{min:06.3f}' # {sign}{d:03d}°{minuteAsInt:02d}'{seconde:06.3f}\"]"

    @staticmethod
    def fromString (s : str = "") -> cAngle :
        regex_dd = r"\s*([\+|\-])?([0-9]*(\.[0-9]*)?)°?\s*" # 12.12°
        regex_ddmm = r"\s*([\+|\-])?([0-9]+)°([0-9]{1,2}(\.[0-9]*)?)'?\s*" # 12°12.25'
        regex_ddmmss = r"\s*([\+|\-])?([0-9]+)°([0-9]{1,2})'([0-9]{1,2}\.[0-9]*)\"?\s*" # 12°45'56.12"

        try:
            sign : float = 1.0
            a : cAngle = cAngle(0.0)
            x =  re.fullmatch(regex_dd, s)
            if x:
                sign = 1.0 if ((x.group(1) is None) or (x.group(1) == "+")) else -1.0
                deg = x.group(2)
                a = cAngle (sign * float(deg))

            else:
                x = re.fullmatch(regex_ddmm, s)
                if x:
                    sign = 1.0 if ((x.group(1) is None) or (x.group(1) == "+")) else -1.0
                    deg = x.group(2)
                    min = x.group(3)
                    a = cAngle (sign * (float(deg) + float(min) / 60.0))

                else:
                    x = re.fullmatch(regex_ddmmss, s)
                    if x:
                        sign = 1.0 if ((x.group(1) is None) or (x.group(1) == "+")) else -1.0
                        deg = x.group(2)
                        min = x.group(3)
                        sec = x.group(4)
                        a = cAngle (sign * (float(deg) + float(min) / 60.0 + float(sec) / 3600.0))
                    else:
                        raise cMyException("Ce n'est pas un angle")
        except Exception as e:
            raise cMyException (str(e))
        return a

    @property
    def valAsDeg(self) -> float:
        return self._angleEnDeg

    @property
    def valAsRad(self) -> float:
        return cAngle.DEG2RAD * self._angleEnDeg

    @valAsDeg.setter
    def valAsDeg(self, x:float) -> None:
        self._angleEnDeg = x

    def __iadd__(self, val : cAngle) -> cAngle:
        if val is cAngle:
            self._angleEnDeg += val.valAsDeg
            return self
        raise cMyException("angle add: type error")

    def __add__(self, val : cAngle) -> cAngle:
        if val is cAngle:
            retour = cAngle()
            retour._angleEnDeg = self._angleEnDeg + val._angleEnDeg
            return retour
        raise cMyException("angle add: type error")

    def _normalize(self) -> None:
        pass

    
            