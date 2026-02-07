from __future__ import annotations
import copy

from sfa_tools import cMyException
from .cAngle import cAngle, eAngleFormat

"""
Classe de base de cap
"""
class cCap(cAngle):
    def __init__(self, valAsDeg : float | None = None, valAsAngleTrigonometriqueEnDeg : float | None = None, valAsAngleTrigonometriqueEnRad : float | None = None):
        super().__init__(valAsDeg=0.0)
        if not valAsDeg is None:
            self._angleEnDeg = valAsDeg
        elif not valAsAngleTrigonometriqueEnDeg is None:
            self.asAngleTrigonometriqueEnDeg = valAsAngleTrigonometriqueEnDeg
        elif not valAsAngleTrigonometriqueEnRad is None:
            self.asAngleTrigonometriqueEnRad = valAsAngleTrigonometriqueEnRad
        self.inner_normalise()

    @property
    def capAsDeg(self) -> float :
        return self._angleEnDeg
    @capAsDeg.setter
    def capAsDeg(self, cap: float) -> None :
        self._angleEnDeg = cap

    @property
    def asAngleTrigonometriqueEnRad(self) -> float :
        return self.asAngleTrigonometriqueEnDeg * cAngle.DEG2RAD

    @asAngleTrigonometriqueEnRad.setter
    def asAngleTrigonometriqueEnRad(self, rad : float) -> None :
        self.asAngleTrigonometriqueEnDeg = rad * cAngle.RAD2DEG

    @property
    def asAngleTrigonometriqueEnDeg(self) -> float :
        return (90.0 - self._angleEnDeg)

    @asAngleTrigonometriqueEnDeg.setter
    def asAngleTrigonometriqueEnDeg(self, deg : float) -> None :
        self._angleEnDeg = 90.0 - deg


    def __repr__(self) -> str:
        return "[cCap: " + self.__str__() + "]"

    def __str__(self) -> str:
        """
        toString par defaut
        """
        return super().__str__()

    def toString(self, format: eAngleFormat = eAngleFormat.DD) -> str:
        return  super().toString(format=format)


    def __add__(self, other) -> cCap:
        a : cAngle = super().__add__(other)
        return cCap(a.angleAsDeg)

    def __iadd__(self, other) -> cCap:
        super().__iadd__(other)
        self.inner_normalise()
        return self

    def __mul__(self, other) -> cCap:
        a = super().__mul__(other)
        return cCap(a.angleAsDeg)

    def __imul__(self, other) -> cCap:
        super().__imul__(other)
        self.inner_normalise()
        return self

    def __sub__(self, other) -> cCap:
        a = super().__sub__(other)
        return cCap(a.angleAsDeg)

    def __isub__(self, other) -> cCap:
        super().__isub__(other)
        self.inner_normalise()
        return self

    def __truediv__(self, other) -> cCap:
        a = super().__truediv__(other)
        return cCap(a.angleAsDeg)

    def __itruediv__(self, other) -> cCap:
        super().__itruediv__(other)
        self.inner_normalise()
        return self

    def __ne__(self, other):
        return not self.__eq__(other)

    def __eq__(self, other):
        if isinstance(other, cCap):
            return abs(self.angleAsDeg - other.angleAsDeg) <= cAngle.EQUAL_TOLERANCE_IN_DEG
        if isinstance(other, cAngle):
            return abs(self.angleAsDeg - other.angleAsDeg) <= cAngle.EQUAL_TOLERANCE_IN_DEG
        if isinstance(other, (int, float)):
            return abs(self.angleAsDeg - other) <= cAngle.EQUAL_TOLERANCE_IN_DEG
        return False

    def __copy__(self) -> cCap:
        """
        normalise renvoie un angle compris entre 0 et 360
        Args:
            aucun
        Returns:
            self
        Raises:
            aucun
        """
        c = super().__copy__()
        return c

    def __deepcopy__(self, memodict={}) -> cCap:
        """
        normalise renvoie un angle compris entre 0 et 360
        Args:
            aucun
        Returns:
            self
        Raises:
            aucun
        """
        c = super().__deepcopy__()
        return c