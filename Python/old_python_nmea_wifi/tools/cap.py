from __future__ import annotations
import math
from tools.myException import myException
from tools.angle import angle


class cap(angle):
    def __init__(self, valAsDeg : float = 0.0):
        super().__init__(valAsDeg)
        self._normalize()

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
        if isinstance(val, angle):
            self._angleEnDeg += val.valAsDeg
            self._normalize()
            return self
        raise myException("angle add: type error")

    def __add__(self, val : angle) -> cap:
        retour = cap()
        if isinstance(val, angle):
            retour._angleEnDeg = self._angleEnDeg + val.valAsDeg
            retour._normalize()
            return retour
        raise myException("angle add: type error")

    def __mul__(self, other):
        retour = cap()
        if isinstance(other, float):
            retour._angleEnDeg = float(other) * self._angleEnDeg
            retour._normalize()
            return retour
        raise myException("angle add: type error")

    def __rmul__(self, other):
        retour = cap()
        if isinstance(other, float):
            retour._angleEnDeg = float(other) * self._angleEnDeg
            retour._normalize()
            return retour
        raise myException("angle add: type error")

    def copy(self) -> cap:
        retour = cap(valAsDeg=self._angleEnDeg)
        return retour


    def _normalize(self) -> None:
        x = self._angleEnDeg
        while x < 0:
            x += 360.0

        while x > 360.0:
            x -= 360.0
        self._angleEnDeg = x

    
            