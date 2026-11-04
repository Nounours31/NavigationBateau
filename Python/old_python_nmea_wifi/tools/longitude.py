from __future__ import annotations

import re

from tools.angle import angle as angle
from tools.myException import myException


class longitude (angle):
    OUEST = -1.0
    EST = +1.0

    def __init__(self, valAsDeg: float = 0.0):
        super().__init__(valAsDeg)
        self.__sens: float = longitude.EST

    def sensAsString(self) -> str:
        return "E" if self.__sens == longitude.EST else "W"

    def __signe(self) -> float:
        if self.__sens == longitude.EST:
            return 1.0
        return -1.0

    @property
    def valAsRad(self) -> float:
        return self.__signe() * super().valAsRad

    @property
    def valAsDeg(self) -> float:
        return self.__signe() * super().valAsDeg

    @valAsDeg.setter
    def valAsDeg(self, val: float) -> None:
        self.__sens = longitude.EST
        self._angleEnDeg = val
        self._normalize()

    @staticmethod
    def fromString(s : str = "") -> longitude:
        retour : longitude = longitude()
        regex_dd1 = r"\s*([W|E])\s*([0-9\.°'\"]+)\s*" # N 12.12°
        regex_dd2 = r"\s*([0-9\.°'\"]+)\s*([W|E])\s*" # N 12.12°

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
                    sign = 1.0 if y.group(1) == "E" else -1.0
                    ang = y.group(2)
                    x = sign * angle.fromString(ang).valAsDeg

                else :
                    y = re.fullmatch(regex_dd2, s)
                    if y:
                        ang = y.group(1)
                        sign = 1.0 if y.group(1) == "E" else -1.0
                        x = sign * angle.fromString(ang).valAsDeg

                    else:
                        raise myException(f"Ce n'est pas une longitude >{s}<")

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
        return f""


    def __iadd__(self, val: longitude) -> longitude:
        if not isinstance(val, angle):
            raise myException("longitude iadd type error")

        asDeg = self.valAsDeg + val.valAsDeg
        self.__sens = longitude.EST
        self._angleEnDeg = asDeg
        self._normalize()

        return self

    def __add__(self, val: angle) -> longitude:
        if not isinstance(val, angle):
            raise myException("latitude iadd type error")

        asDeg = self.valAsDeg + val.valAsDeg
        self.__sens = longitude.EST
        self._angleEnDeg = asDeg
        self._normalize()

        return self

    def copy(self) -> longitude:
        retour: longitude = longitude()
        retour.__sens = self.__sens
        retour._angleEnDeg = self._angleEnDeg
        return retour

    def _normalize(self) -> None:
        x = self._angleEnDeg * self.__signe()
        if abs(x) > 180.0:
            raise myException("Longitude invalide")

        self.__sens = longitude.EST
        if x < 0:
            self.__sens = longitude.OUEST
            x = -1.0 * x
        self._angleEnDeg = x
    
            