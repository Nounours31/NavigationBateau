from __future__ import annotations
from tools.angle import angle as angle
from tools.myException import MyException


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

    def toString(self, base :int = 0) -> str:
        return f"{self.sensAsString()} {super().toString(base=angle.STR_AsREAL)} - {self.sensAsString()} {super().toString(base=angle.STR_AsMin)} [{self.sensAsString()} {super().toString(base=angle.STR_AsSec)}]"

    def __iadd__(self, val: longitude) -> longitude:
        if not isinstance(val, angle):
            raise MyException("longitude iadd type error")

        asDeg = self.valAsDeg + val.valAsDeg
        self.__sens = longitude.EST
        self._angleEnDeg = asDeg
        self._normalize()

        return self

    def __add__(self, val: angle) -> longitude:
        if not isinstance(val, angle):
            raise MyException("latitude iadd type error")

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
            raise MyException("Longitude invalide")

        self.__sens = longitude.EST
        if x < 0:
            self.__sens = longitude.OUEST
        self._angleEnDeg = x
    
            