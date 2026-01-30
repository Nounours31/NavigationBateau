from __future__ import annotations

import re

from sfa_tools import cMyException
from .cAngle import cAngle, eAngleFormat


class cLongitude (cAngle):
    OUEST = -1.0
    EST = +1.0

    def __init__(self, valAsDeg: float = 0.0):
        super().__init__(valAsDeg)
        self.__sens: float = cLongitude.EST

    def sensAsString(self) -> str:
        return "E" if self.__sens == cLongitude.EST else "W"

    def __signe(self) -> float:
        if self.__sens == cLongitude.EST:
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
        self.__sens = cLongitude.EST
        self._angleEnDeg = val
        self._normalize()

    @staticmethod
    def fromString(angleAsString : str = "") -> cLongitude:
        retour : cLongitude = cLongitude()
        regex_dd1 = r"\s*([W|E])\s*([0-9\.°'\"]+)\s*" # N 12.12°
        regex_dd2 = r"\s*([0-9\.°'\"]+)\s*([W|E])\s*" # N 12.12°

        try:
            a: cAngle = None
            try:
                a : cAngle = cAngle.fromString(angleAsString)
                retour.valAsDeg = a.valAsDeg
                retour._normalize()
            except Exception as e:
                a = None

            if a is None:
                x : float = 0.0
                y = re.fullmatch(regex_dd1, angleAsString)
                if y:
                    sign = 1.0 if y.group(1) == "E" else -1.0
                    ang = y.group(2)
                    x = sign * cAngle.fromString(ang).valAsDeg

                else :
                    y = re.fullmatch(regex_dd2, angleAsString)
                    if y:
                        ang = y.group(1)
                        sign = 1.0 if y.group(1) == "E" else -1.0
                        x = sign * cAngle.fromString(ang).valAsDeg

                    else:
                        raise cMyException(f"Ce n'est pas une longitude >{angleAsString}<")

                retour.valAsDeg = x
                retour._normalize()

        except Exception as e:
            raise cMyException (str(e))
        return retour

    def toString(self, base :int = 0, detail : int = cAngle.DISPLAY_LONG) -> str:
        if detail == cAngle.DISPLAY_LONG:
            return f"{self.sensAsString()} {super().toString(eAngleFormat.DD)} - {self.sensAsString()} {super().toString(eAngleFormat.DMM)} [{self.sensAsString()} {super().toString(eAngleFormat.DMS)}]"
        if detail == cAngle.DISPLAY_SHORT:
            return f"{self.sensAsString()} {super().toString(eAngleFormat.DD)}"
        return f""


    def __iadd__(self, val: cLongitude) -> cLongitude:
        if not isinstance(val, cAngle):
            raise cMyException("longitude iadd type error")

        asDeg = self.valAsDeg + val.valAsDeg
        self.__sens = cLongitude.EST
        self._angleEnDeg = asDeg
        self._normalize()

        return self

    def __add__(self, val: cAngle) -> cLongitude:
        if not isinstance(val, cAngle):
            raise cMyException("latitude iadd type error")

        asDeg = self.valAsDeg + val.valAsDeg
        self.__sens = cLongitude.EST
        self._angleEnDeg = asDeg
        self._normalize()

        return self

    def copy(self) -> cLongitude:
        retour: cLongitude = cLongitude()
        retour.__sens = self.__sens
        retour._angleEnDeg = self._angleEnDeg
        return retour

    def _normalize(self) -> None:
        x = self._angleEnDeg * self.__signe()
        if abs(x) > 180.0:
            raise cMyException("Longitude invalide")

        self.__sens = cLongitude.EST
        if x < 0:
            self.__sens = cLongitude.OUEST
            x = -1.0 * x
        self._angleEnDeg = x
    
            