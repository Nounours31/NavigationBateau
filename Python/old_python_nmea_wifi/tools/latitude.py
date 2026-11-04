from __future__ import annotations

from tools import myException
from tools.angle import angle as angle
from tools.myException import myException

import re

class latitude (angle):
    SUD = -1.0
    NORD = +1.0

    def __init__(self, valAsDeg : float = 0.0):
        super().__init__(valAsDeg)
        self.__sens : float = latitude.NORD

    def sensAsString(self) -> str:
        return "N" if self.__sens == latitude.NORD else "S"

    def __signe(self) -> float:
        if self.__sens == latitude.NORD:
            return 1.0
        return -1.0

    @property
    def valAsRad(self) -> float:
        return self.__signe() * super().valAsRad

    @property
    def valAsDeg(self) -> float:
        return self.__signe() * super().valAsDeg

    @valAsDeg.setter
    def valAsDeg(self,val:float) -> None:
        self.__sens = latitude.NORD
        self._angleEnDeg = val
        self._normalize()

    @staticmethod
    def fromString(s : str = "") -> latitude:
        retour : latitude = latitude()

        regex_dd1 = r"\s*([N|S])\s*([0-9\.°'\"]+)\s*" # N 12.12°
        regex_dd2 = r"\s*([0-9\.°'\"]+)\s*([N|S])\s*" # N 12.12°

        try:
            a: angle = None
            try:
                a : angle = angle.fromString(s)
                retour.valAsDeg = a.valAsDeg
                retour._normalize()
            except Exception as e:
                a = None

            if a is None:
                x : float = 0.0
                y = re.fullmatch(regex_dd1, s)
                if y:
                    sign = 1.0 if y.group(1) == "N" else -1.0
                    ang = y.group(2)
                    x = sign * angle.fromString(ang).valAsDeg

                else :
                    y = re.fullmatch(regex_dd2, s)
                    if y:
                        ang = y.group(1)
                        sign = 1.0 if y.group(1) == "N" else -1.0
                        x = sign * angle.fromString(ang).valAsDeg

                    else:
                        raise myException(f"Ce n'est pas une latitude >{s}<")

                retour.valAsDeg = x
                retour._normalize()

        except Exception as e:
            raise myException (str(e))
        return retour




    def toString(self, base :int = angle.STR_AsMin, detail : int = angle.DISPLAY_LONG) -> str:
        if detail == angle.DISPLAY_LONG:
            return f"{self.sensAsString()} {super().toString(base=angle.STR_AsREAL)} - {self.sensAsString()} {super().toString(base=angle.STR_AsMin)} [{self.sensAsString()} {super().toString(base=angle.STR_AsSec)}]"
        if detail == angle.DISPLAY_SHORT:
            return f"{self.sensAsString()} {super().toString(base=base)}"
        return ""
    
    def __iadd__(self, val: angle) -> latitude:
        if not isinstance(val, angle):
            raise MyException("latitude iadd type error")

        asDeg = self.valAsDeg + val.valAsDeg
        self.__sens = latitude.NORD
        self._angleEnDeg = asDeg
        self._normalize()

        return self

    def __add__(self, val : angle) -> latitude:
        if not isinstance(val, angle):
            raise MyException("latitude iadd type error")

        asDeg = self.valAsDeg + val.valAsDeg
        self.__sens = latitude.NORD
        self._angleEnDeg = asDeg
        self._normalize()

        return self

    def copy(self) -> latitude:
        retour : latitude = latitude()
        retour.__sens = self.__sens
        retour._angleEnDeg = self._angleEnDeg
        return retour


    def _normalize(self) -> None:
        x = self._angleEnDeg * self.__signe()
        if abs(x) > 90.0:
            raise myException("Latitude invalide")

        self.__sens = latitude.NORD
        if x < 0:
            self.__sens = latitude.SUD
            x = -1.0 * x
        self._angleEnDeg = x
