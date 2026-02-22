from __future__ import annotations

import copy
import math

from sfa_tools import cMyException

from .cDistance import cDistance
from .cAngle import cAngle
from .cCap import cCap
from .cAngle import eAngleFormat
from .cDistance import eDistanceFormat


class cNorme:
    NOEUD2KMH: float = cDistance.MN2KM
    KMH2NOEUD: float = cDistance.KM2MN

    DISPLAY_SHORT: int = 0
    DISPLAY_LONG: int = 1

    def __init__(self, valAsNoeud: float | None = None, valAsKmH: float | None = None):
        if isinstance(valAsNoeud, (int, float)):
            self._valAsNoeud = valAsNoeud

        elif isinstance(valAsKmH, (int, float)):
            self._valAsNoeud = valAsKmH * cDistance.KM2MN

        else:
            self._valAsNoeud = 0.0

    @property
    def asNoeud(self) -> float:
        return self._valAsNoeud

    @asNoeud.setter
    def asNoeud(self, x: float) -> None:
        self._valAsNoeud = x

    @property
    def asKmH(self) -> float:
        return self._valAsNoeud * cDistance.MN2KM

    # ========================
    # Conversions & affichage
    # ========================
    def toString(self, format: eDistanceFormat = eDistanceFormat.STD):
        if format == eDistanceFormat.STD:
            return f"{self.asNoeud:.3f}Mn"

        elif format == eDistanceFormat.FULL:
            return f"{self.asNoeud:.3f}Mn ({self.asKmH:.3f} km)"

    def __str__(self):
        return self.toString(eDistanceFormat.STD)

    def __repr__(self):
        return f"[cDistance: {self.asNoeud:.3f} Nd]"

    def __copy__(self) -> cVitesse:
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
        new_obj._valAsNoeud = copy.copy(self._valAsNoeud)
        return new_obj

    def __deepcopy__(self, memodict={}) -> cVitesse:
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

        new_obj._valAsMilleNautique = copy.deepcopy(self._valAsNoeud, memodict)
        return new_obj


class cVitesse:
    def __init__(self, norme: cNorme | float | None = None, sens: cCap | float | None = None):
        self._distance: cNorme = cNorme(valAsNoeud=0.0)
        if norme is not None:
            if isinstance(norme, (float, int)):
                self._distance = cNorme(valAsNoeud=norme)
            else:
                self._distance = norme

        self._sens: cCap = cCap(valAsDeg=0.0)
        if sens is not None:
            if isinstance(sens, (float, int)):
                self._sens = cCap(valAsDeg=sens)
            else:
                self._sens = sens

    @property
    def distance(self) -> cNorme:
        return self._distance

    @distance.setter
    def distance(self, d: cNorme) -> None:
        if not isinstance(d, cNorme):
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

    @classmethod
    def fromDict(cls, data: dict) -> cVitesse:
        distance_data = data.get("distance")
        sens_data = data.get("sens")

        distance: cNorme = cNorme.fromString(distance_data)
        sens: cCap = cCap.fromString(sens_data)
        return cls(distance=distance, sens=sens)

    # ========================
    # Conversions & affichage
    # ========================
    def toString(
        self,
        formatDistance: eDistanceFormat = eDistanceFormat.STD,
        formatAngle: eAngleFormat = eAngleFormat.DD,
    ):
        return f"{self._distance.toString(formatDistance)} {self._sens.toString(formatAngle)}"

    def __str__(self):
        return self.toString(eDistanceFormat.STD, eAngleFormat.DD)

    def __repr__(self):
        return f"[cVecteur({self.toString(eDistanceFormat.STD, eAngleFormat.DD)})]"

    def __add__(self, other):
        if isinstance(other, cVitesse):
            x: float = self.distance.asMn * math.cos(
                self.sens.asAngleTrigonometriqueEnRad
            ) + other.distance.asMn * math.cos(other.sens.asAngleTrigonometriqueEnRad)
            y: float = self.distance.asMn * math.sin(
                self.sens.asAngleTrigonometriqueEnRad
            ) + other.distance.asMn * math.sin(other.sens.asAngleTrigonometriqueEnRad)
            isYNull: bool = math.fabs(y) < cAngle.EQUAL_TOLERANCE_IN_DEG
            valAsAngleTrigonometriqueEnRad: float = 0.0
            if isYNull:
                valAsAngleTrigonometriqueEnRad = math.pi / 2.0 if x >= 0.0 else math.pi / (-2.0)
            else:
                valAsAngleTrigonometriqueEnRad = math.atan2(y, x)
            return cVitesse(
                distance=cDistance(valAsMilleNautique=math.sqrt(x * x + y * y)),
                sens=cCap(valAsAngleTrigonometriqueEnRad=valAsAngleTrigonometriqueEnRad),
            )
        return NotImplemented

    def __iadd__(self, other):
        if isinstance(other, cVitesse):
            v: cVitesse = self.__add__(other)
            self.distance = v.distance
            self.sens = v.sens
            return self
        return NotImplemented

    def __sub__(self, other):
        if isinstance(other, cVitesse):
            x: float = self.distance.asMn * math.cos(
                self.sens.asAngleTrigonometriqueEnRad
            ) - other.distance.asMn * math.cos(other.sens.asAngleTrigonometriqueEnRad)
            y: float = self.distance.asMn * math.sin(
                self.sens.asAngleTrigonometriqueEnRad
            ) - other.distance.asMn * math.sin(other.sens.asAngleTrigonometriqueEnRad)
            isYNull: bool = math.fabs(y) < cAngle.EQUAL_TOLERANCE_IN_DEG
            valAsAngleTrigonometriqueEnRad: float = 0.0
            if isYNull:
                valAsAngleTrigonometriqueEnRad = math.pi / 2.0 if x >= 0.0 else math.pi / (-2.0)
            else:
                valAsAngleTrigonometriqueEnRad = math.atan2(y, x)
            return cVitesse(
                distance=cDistance(valAsMilleNautique=math.sqrt(x * x + y * y)),
                sens=cCap(valAsAngleTrigonometriqueEnRad=valAsAngleTrigonometriqueEnRad),
            )
        return NotImplemented

    def __isub__(self, other):
        if isinstance(other, cVitesse):
            v: cVitesse = self.__sub__(other)
            self.distance = v.distance
            self.sens = v.sens
            return self
        return NotImplemented

    def __mul__(self, other):
        if isinstance(other, (int, float)):
            v: cVitesse = cVitesse(distance=cDistance(self.distance.asMn), sens=self.sens)
            v.distance *= other
            return v
        return NotImplemented

    def __imul__(self, other):
        if isinstance(other, (int, float)):
            self.distance *= other
            return self
        return NotImplemented

    def __rmul__(self, other):
        return self.__mul__(other)

    def __truediv__(self, other):
        if isinstance(other, (int, float)):
            v: cVitesse = cVitesse(distance=cDistance(self.distance.asMn), sens=self.sens)
            v.distance /= other
            return v
        return NotImplemented

    def __itruediv__(self, other):
        if isinstance(other, (int, float)):
            self.distance /= other
            return self
        return NotImplemented

    # ========================
    # Comparaisons
    # ========================

    def __ne__(self, other):
        return not self.__eq__(other)

    def __eq__(self, other):
        if isinstance(other, cVitesse):
            return (self.distance == other.distance) and (self.sens == other.sens)
        return False

    def __copy__(self) -> cVitesse:
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

    def __deepcopy__(self, memodict={}) -> cVitesse:
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
