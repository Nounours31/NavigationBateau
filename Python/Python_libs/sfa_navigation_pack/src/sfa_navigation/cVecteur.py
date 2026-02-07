from __future__ import annotations

import copy
import math

from sfa_tools import cMyException

from .cDistance import cDistance
from .cAngle import cAngle
from .cCap import cCap
from .cAngle import eAngleFormat
from .cDistance import eDistanceFormat


class cVecteur:
    def __init__(self, distance : cDistance | float | None = None, sens : cCap | float | None = None):
        self._distance : cDistance = cDistance(valAsMilleNautique=0.0)
        if not distance is None:
            if isinstance (distance, (float, int)):
                self._distance = cDistance(valAsMilleNautique=distance)
            else:
                self._distance = distance

        self._sens : cCap = cCap(valAsDeg=0.0)
        if not sens is None:
            if isinstance (sens, (float, int)):
                self._sens = cCap(valAsDeg=sens)
            else:
                self._sens = sens

    @property
    def distance(self) -> cDistance:
        return self._distance
    
    @distance.setter
    def distance(self, d : cDistance) -> None:
        if not isinstance(d, cDistance):
            raise cMyException("distance is not a cDistance ...")
        self._distance.asMn = d.asMn

    @property
    def sens(self) -> cCap:
        return self._sens
    
    @sens.setter
    def sens(self, c: cCap) -> None:
        if not isinstance(c, cCap):
            raise cMyException("sens is not a Cap ...")
        self._sens.angleAsDeg = c.capAsDeg
    


    # ========================
    # Conversions & affichage
    # ========================
    def toString(self, formatDistance: eDistanceFormat = eDistanceFormat.STD, formatAngle : eAngleFormat = eAngleFormat.DD):
        return f"{self._distance.toString(formatDistance)} {self._sens.toString(formatAngle)}"

    def __str__(self):
        return self.toString(eDistanceFormat.STD, eAngleFormat.DD)

    def __repr__(self):
        return f"[cVecteur({self.toString(eDistanceFormat.STD,eAngleFormat.DD)})]"

    def __add__(self, other):
        if isinstance(other, cVecteur):
            x : float = self.distance.asMn * math.cos (self.sens.asAngleTrigonometriqueEnRad) + other.distance.asMn * math.cos (other.sens.asAngleTrigonometriqueEnRad)
            y : float = self.distance.asMn * math.sin (self.sens.asAngleTrigonometriqueEnRad) + other.distance.asMn * math.sin (other.sens.asAngleTrigonometriqueEnRad)
            isYNull : bool = (math.fabs(y) < cAngle.EQUAL_TOLERANCE_IN_DEG)
            valAsAngleTrigonometriqueEnRad : float = 0.0
            if isYNull:
                valAsAngleTrigonometriqueEnRad = math.pi / 2.0 if x >= 0.0 else math.pi / (-2.0)
            else:
                valAsAngleTrigonometriqueEnRad = math.atan2(y, x)
            return cVecteur(distance=cDistance(valAsMilleNautique=math.sqrt(x * x + y * y)),
                            sens=cCap(valAsAngleTrigonometriqueEnRad=valAsAngleTrigonometriqueEnRad))
        return NotImplemented

    def __iadd__(self, other):
        if isinstance(other, cVecteur):
            v : cVecteur = self.__add__(other)
            self.distance = v.distance
            self.sens = v.sens
            return self
        return NotImplemented

    def __sub__(self, other):
        if isinstance(other, cVecteur):
            x : float = self.distance.asMn * math.cos (self.sens.asAngleTrigonometriqueEnRad) - other.distance.asMn * math.cos (other.sens.asAngleTrigonometriqueEnRad)
            y : float = self.distance.asMn * math.sin (self.sens.asAngleTrigonometriqueEnRad) - other.distance.asMn * math.sin (other.sens.asAngleTrigonometriqueEnRad)
            isYNull : bool = (math.fabs(y) < cAngle.EQUAL_TOLERANCE_IN_DEG)
            valAsAngleTrigonometriqueEnRad : float = 0.0
            if isYNull:
                valAsAngleTrigonometriqueEnRad = math.pi / 2.0 if x >= 0.0 else math.pi / (-2.0)
            else:
                valAsAngleTrigonometriqueEnRad = math.atan2(y, x)
            return cVecteur(distance=cDistance(valAsMilleNautique=math.sqrt(x * x + y * y)),
                            sens=cCap(valAsAngleTrigonometriqueEnRad=valAsAngleTrigonometriqueEnRad))
        return NotImplemented

    def __isub__(self, other):
        if isinstance(other, cVecteur):
            v : cVecteur = self.__sub__(other)
            self.distance = v.distance
            self.sens = v.sens
            return self
        return NotImplemented

    def __mul__(self, other):
        if isinstance(other, (int, float)):
            v : cVecteur = cVecteur(distance=cDistance(self.distance.asMn), sens=self.sens)
            v.distance *=  other
            return v
        return NotImplemented

    def __imul__(self, other):
        if isinstance(other, (int, float)):
            self.distance *=  other
            return self
        return NotImplemented

    def __rmul__(self, other):
        return self.__mul__(other)

    def __truediv__(self, other):
        if isinstance(other, (int, float)):
            v : cVecteur = cVecteur(distance=cDistance(self.distance.asMn), sens=self.sens)
            v.distance /= other
            return v
        return NotImplemented

    def __itruediv__(self, other):
        if isinstance(other, (int, float)):
            self.distance /=  other
            return self
        return NotImplemented

    # ========================
    # Comparaisons
    # ========================

    def __ne__(self, other):
        return not self.__eq__(other)

    def __eq__(self, other):
        if isinstance(other, cVecteur):
            return ((self.distance == other.distance) and (self.sens == other.sens))
        return False


    def __copy__(self) -> cVecteur:
        """
        normalise renvoie un angle compris entre 0 et 360
        Args:
            aucun
        Returns:
            self
        Raises:
            aucun
        """
        cls = self.__class__
        new_obj = cls.__new__(cls)
        new_obj._distance = copy.copy(self._distance)
        new_obj._sens = copy.copy(self._sens)
        return new_obj

    def __deepcopy__(self, memodict={}) -> cVecteur:
        """
        normalise renvoie un angle compris entre 0 et 360
        Args:
            aucun
        Returns:
            self
        Raises:
            aucun
        """
        cls = self.__class__
        new_obj = cls.__new__(cls)

        memodict[id(self)] = new_obj

        new_obj._distance = copy.deepcopy(self._distance)
        new_obj._sens = copy.deepcopy(self._sens)
        return new_obj
