from __future__ import annotations
import math
from tools.myException import myException
import re

class angle:
    STR_AsREAL : int = 3
    STR_AsMin : int = 1
    STR_AsSec : int = 2
    STR_AsALL : int = 0
    STR_Echec : int = 4

    DISPLAY_SHORT : int = 0
    DISPLAY_LONG : int = 1

    def __init__(self, valAsDeg : float = 0.0):
        self._angleEnDeg : float = valAsDeg

    def toString(self, base=STR_Echec) -> str :
        """
        :param base: 0 en reel, 10 en base 10, 60 en sexagedecimal
        :return: angle as string
        """
        if base == angle.STR_Echec:
            raise myException ("toSting cas non prevu")

        sign : str = "" if self._angleEnDeg >= 0.0 else "-"
        abs : float = math.fabs(self._angleEnDeg)
        d : int = math.floor (abs)
        min : float = (abs - d) * 60.0
        dmin : int = math.floor(min)
        sec : float = (min - math.floor (min)) * 60.0
        if base == angle.STR_AsREAL:
            return f"{sign}{abs:08.4f}°"
        elif base == angle.STR_AsMin:
            return f"{sign}{d:03d}°{min:06.3f}'"
        elif base == angle.STR_AsSec:
            return f"{sign}{d:03d}°{dmin:02d}'{sec:06.3f}\""
        else:
            return f"{sign}{abs:08.4f}° [{sign}{d:03d}°{min:06.3f}' # {sign}{d:03d}°{dmin:02d}'{sec:06.3f}\"]"

    @staticmethod
    def fromString (s : str = "") -> angle :
        regex_dd = r"\s*([\+|\-])?([0-9]*(\.[0-9]*)?)°?\s*" # 12.12°
        regex_ddmm = r"\s*([\+|\-])?([0-9]+)°([0-9]{1,2}(\.[0-9]*)?)'?\s*" # 12°12.25'
        regex_ddmmss = r"\s*([\+|\-])?([0-9]+)°([0-9]{1,2})'([0-9]{1,2}\.[0-9]*)\"?\s*" # 12°45'56.12"

        try:
            sign : float = 1.0
            a : angle = angle(0.0)
            x =  re.fullmatch(regex_dd, s)
            if x:
                sign = 1.0 if ((x.group(1) is None) or (x.group(1) == "+")) else -1.0
                deg = x.group(2)
                a = angle (sign * float(deg))

            else:
                x = re.fullmatch(regex_ddmm, s)
                if x:
                    sign = 1.0 if ((x.group(1) is None) or (x.group(1) == "+")) else -1.0
                    deg = x.group(2)
                    min = x.group(3)
                    a = angle (sign * (float(deg) + float(min) / 60.0))

                else:
                    x = re.fullmatch(regex_ddmmss, s)
                    if x:
                        sign = 1.0 if ((x.group(1) is None) or (x.group(1) == "+")) else -1.0
                        deg = x.group(2)
                        min = x.group(3)
                        sec = x.group(4)
                        a = angle (sign * (float(deg) + float(min) / 60.0 + float(sec) / 3600.0))
                    else:
                        raise myException("Ce n'est pas un angle")
        except Exception as e:
            raise myException (str(e))
        return a

    @property
    def valAsDeg(self) -> float:
        return self._angleEnDeg

    @property
    def valAsRad(self) -> float:
        return (math.pi / 180.0) * self._angleEnDeg

    @valAsDeg.setter
    def valAsDeg(self, x:float) -> None:
        self._angleEnDeg = x

    def __iadd__(self, val : angle) -> angle:
        if val is angle:
            self._angleEnDeg += val.valAsDeg
            return self
        raise MyException("angle add: type error")

    def __add__(self, val : angle) -> angle:
        if val is angle:
            retour = angle()
            retour._angleEnDeg = self._angleEnDeg + val._angleEnDeg
            return retour
        raise MyException("angle add: type error")

    def _normalize(self) -> None:
        pass

    
            