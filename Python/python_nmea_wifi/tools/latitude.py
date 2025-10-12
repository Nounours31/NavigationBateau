from __future__ import annotations

from tools import myException
from tools.angle import angle as angle
from tools.myException import MyException


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


    def toString(self, base :int = 0) -> str:
        return f"{self.sensAsString()} {super().toString(base=angle.STR_AsREAL)} - {self.sensAsString()} {super().toString(base=angle.STR_AsMin)} [{self.sensAsString()} {super().toString(base=angle.STR_AsSec)}]"

    
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
        self._angleEnDeg = x
