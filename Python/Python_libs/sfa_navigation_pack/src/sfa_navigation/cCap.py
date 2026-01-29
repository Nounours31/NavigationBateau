from __future__ import annotations
import math

from sfa_tools import cMyException
from .cAngle import cAngle

"""
Classe de base des angles
"""
class cCap(cAngle):
    def __init__(self, valAsDeg : float = 0.0):
        super().__init__(valAsDeg)
        self._normalize()

    @property
    def valAsDeg(self) -> float:
        """
        :return:
        """
        return self._angleEnDeg

    @property
    def valAsRad(self) -> float:
        """
        :return:
        """
        return (math.pi / 180.0) * self._angleEnDeg

    @valAsDeg.setter
    def valAsDeg(self, x:float) -> None:
        self._angleEnDeg = x

    def __iadd__(self, val : cAngle) -> cAngle:
        if isinstance(val, cAngle):
            self._angleEnDeg += val.valAsDeg
            self._normalize()
            return self
        raise cMyException("angle add: type error")

    def __add__(self, val : cAngle) -> cCap:
        retour = cCap()
        if isinstance(val, cAngle):
            retour._angleEnDeg = self._angleEnDeg + val.valAsDeg
            retour._normalize()
            return retour
        raise cMyException("angle add: type error")

    def __mul__(self, other):
        retour = cCap()
        if isinstance(other, float):
            retour._angleEnDeg = float(other) * self._angleEnDeg
            retour._normalize()
            return retour
        raise cMyException("angle add: type error")

    def __rmul__(self, other):
        retour = cCap()
        if isinstance(other, float):
            retour._angleEnDeg = float(other) * self._angleEnDeg
            retour._normalize()
            return retour
        raise cMyException("angle add: type error")

    def copy(self) -> cCap:
        retour = cCap(valAsDeg=self._angleEnDeg)
        return retour


    def _normalize(self) -> None:
        x = self._angleEnDeg
        while x < 0:
            x += 360.0

        while x > 360.0:
            x -= 360.0
        self._angleEnDeg = x

    
            