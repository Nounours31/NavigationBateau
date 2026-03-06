from __future__ import annotations
from typing import Pattern

import copy
import math
import re

from sfa_tools import cMyException

from .cDistance import cDistance
from .cAngle import cAngle
from .cCap import cCap
from .cAngle import eAngleFormat
from .cDistance import eDistanceFormat


class cVitesse:
    NOEUD2KMH: float = cDistance.MN2KM
    KMH2NOEUD: float = cDistance.KM2MN

    EQUAL_TOLERANCE_IN_KT: float = 0.01

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
            return f"{self.asNoeud:.3f}Kt"

        elif format == eDistanceFormat.FULL:
            return f"{self.asNoeud:.3f}Kt ({self.asKmH:.3f} kmh)"

    def __str__(self):
        return self.toString(eDistanceFormat.STD)

    def __repr__(self):
        return f"[Vitesse: {self.asNoeud:.3f} Kt]"

    def __ne__(self, other):
        return not self.__eq__(other)

    def __eq__(self, other):
        if isinstance(other, cVitesse):
            return (math.fabs(self.asNoeud - other.asNoeud) < cVitesse.EQUAL_TOLERANCE_IN_KT)
        return False

    def __imul__(self, other):
        if isinstance(other, (int, float)):
            self.asNoeud = self.asNoeud * other
            return self
        return NotImplemented

    def __mul__(self, other):
        if isinstance(other, (int, float)):
            x : cVitesse = cVitesse (valAsNoeud=self.asNoeud)
            x *= other
            return x
        return NotImplemented

    def __rmul__(self, other):
        return self.__mul__(other)

    def __truediv__(self, other):
        if isinstance(other, (int, float)):
            v: cVitesse = cVitesse(valAsNoeud=self.asNoeud)
            v /= other
            return v
        return NotImplemented

    def __itruediv__(self, other):
        if isinstance(other, (int, float)):
            self.asNoeud = self.asNoeud / other
            return self
        return NotImplemented

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
        new_obj._valAsNoeud = copy.deepcopy(self._valAsNoeud, memodict)

        memodict[id(self)] = new_obj

        return new_obj

    @classmethod
    def fromString(cls, s : str):
        r: Pattern[str] = re.compile(pattern=r"\s*([0-9]*)?[\.\,]*([0-9]*)?\s*(kt|kmh)?\s*", flags=re.IGNORECASE)

        d: float = 0.0
        m: re.Match[str] | None = re.match(r, s, re.ASCII)
        if m is not None:
            match len(m.groups()):
                case 3:
                    d = float(m.group(1)) if len(m.group(1)) > 0 else 0.0
                    d2: float = float(m.group(2)) if len(m.group(2)) > 0 else 0.0
                    while d2 > 1:
                        d2 /= 10.0
                    d += d2
                    if m.group(3) is not None and m.group(3).lower() == "kmh":
                        d *= cDistance.KM2MN

                case _:
                    raise cMyException("Not a normeVitesse: >" + s + "<")
        return cls(valAsNoeud=d)


class cVelocite:
    def __init__(self, vitesse: cVitesse | float | None = None, sens: cCap | float | None = None):
        self._distance: cVitesse = cVitesse(valAsNoeud=0.0)
        if vitesse is not None:
            if isinstance(vitesse, (float, int)):
                self._distance = cVitesse(valAsNoeud=vitesse)
            else:
                self._distance = vitesse

        self._sens: cCap = cCap(valAsDeg=0.0)
        if sens is not None:
            if isinstance(sens, (float, int)):
                self._sens = cCap(valAsDeg=sens)
            else:
                self._sens = sens

    @property
    def vitesse(self) -> cVitesse:
        return self._distance

    @vitesse.setter
    def vitesse(self, d: cVitesse) -> None:
        if not isinstance(d, cVitesse):
            raise cMyException("normeVitesse is not a cDistance ...")
        self._distance.asNoeud = d.asNoeud

    @property
    def sens(self) -> cCap:
        return self._sens

    @sens.setter
    def sens(self, c: cCap) -> None:
        if not isinstance(c, cCap):
            raise cMyException("sens is not a Cap ...")
        self._sens.angleAsDeg = c.capAsDeg

    @classmethod
    def fromDict(cls, data: dict) -> cVelocite:
        distance_data : object | None = data.get("norme")
        sens_data : object | None = data.get("sens")

        distance: cVitesse = cVitesse(valAsNoeud=0.0)
        sens: cCap = cCap(valAsDeg=0.0)
        if distance_data is not None and isinstance(distance_data, str):
            distance = cVitesse.fromString(distance_data)

        if sens_data is not None and isinstance(sens_data, str):
            sens = cCap.fromString(sens_data)
    
        return cls(vitesse=distance, sens=sens)

    @classmethod
    def fromString(cls, data: str) -> cVelocite:
        distance: cVitesse = cVitesse.fromString(data)
        sens: cCap = cCap.fromString(data)
        return cls(vitesse=distance, sens=sens)

    @classmethod
    def fromObject(cls, data: object) -> cVelocite:
        if isinstance(data, cVelocite) :
            return data.__deepcopy__({})
        if isinstance(data, str) :
            return cVelocite.fromString(data)
        if isinstance(data, dict) :
            return cVelocite.fromDict(data)

        return NotImplemented


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
        return f"[cVitesse({self.toString(eDistanceFormat.STD, eAngleFormat.DD)})]"

    def __add__(self, other):
        if isinstance(other, cVelocite):
            x: float = self.vitesse.asNoeud * math.cos(
                self.sens.asAngleTrigonometriqueEnRad
            ) + other.vitesse.asNoeud * math.cos(other.sens.asAngleTrigonometriqueEnRad)
            y: float = self.vitesse.asNoeud * math.sin(
                self.sens.asAngleTrigonometriqueEnRad
            ) + other.vitesse.asNoeud * math.sin(other.sens.asAngleTrigonometriqueEnRad)
            isYNull: bool = math.fabs(y) < cAngle.EQUAL_TOLERANCE_IN_DEG
            valAsAngleTrigonometriqueEnRad: float = 0.0
            if isYNull:
                valAsAngleTrigonometriqueEnRad = math.pi / 2.0 if x >= 0.0 else math.pi / (-2.0)
            else:
                valAsAngleTrigonometriqueEnRad = math.atan2(y, x)
            return cVelocite(
                vitesse=cVitesse(valAsNoeud=math.sqrt(x * x + y * y)),
                sens=cCap(valAsAngleTrigonometriqueEnRad=valAsAngleTrigonometriqueEnRad),
            )
        return NotImplemented

    def __iadd__(self, other):
        if isinstance(other, cVelocite):
            v: cVelocite = self.__add__(other)
            self.vitesse = v.vitesse
            self.sens = v.sens
            return self
        return NotImplemented

    def __sub__(self, other):
        if isinstance(other, cVelocite):
            x: float = self.vitesse.asNoeud * math.cos(
                self.sens.asAngleTrigonometriqueEnRad
            ) - other.vitesse.asNoeud * math.cos(other.sens.asAngleTrigonometriqueEnRad)

            y: float = self.vitesse.asNoeud * math.sin(
                self.sens.asAngleTrigonometriqueEnRad
            ) - other.vitesse.asNoeud * math.sin(other.sens.asAngleTrigonometriqueEnRad)
            
            isYNull: bool = math.fabs(y) < cAngle.EQUAL_TOLERANCE_IN_DEG
            valAsAngleTrigonometriqueEnRad: float = 0.0
            if isYNull:
                valAsAngleTrigonometriqueEnRad = math.pi / 2.0 if x >= 0.0 else math.pi / (-2.0)
            else:
                valAsAngleTrigonometriqueEnRad = math.atan2(y, x)
            return cVelocite(
                vitesse=cVitesse(valAsNoeud=math.sqrt(x * x + y * y)),
                sens=cCap(valAsAngleTrigonometriqueEnRad=valAsAngleTrigonometriqueEnRad),
            )
        return NotImplemented

    def __isub__(self, other):
        if isinstance(other, cVelocite):
            v: cVelocite = self.__sub__(other)
            self.vitesse = v.vitesse
            self.sens = v.sens
            return self
        return NotImplemented

    def __mul__(self, other):
        if isinstance(other, (int, float)):
            v: cVelocite = cVelocite(vitesse=cVitesse(self.vitesse.asNoeud), sens=self.sens)
            v *= other
            return v
        return NotImplemented

    def __imul__(self, other):
        if isinstance(other, (int, float)):
            self.vitesse.asNoeud = self.vitesse.asNoeud * other
            return self
        return NotImplemented

    def __rmul__(self, other):
        return self.__mul__(other)

    def __truediv__(self, other):
        if isinstance(other, (int, float)):
            v: cVelocite = cVelocite(vitesse=cVitesse(self.vitesse.asNoeud), sens=self.sens)
            v /= other
            return v
        return NotImplemented

    def __itruediv__(self, other):
        if isinstance(other, (int, float)):
            self.vitesse.asNoeud = self.vitesse.asNoeud / other
            return self
        return NotImplemented

    # ========================
    # Comparaisons
    # ========================

    def __ne__(self, other):
        return not self.__eq__(other)

    def __eq__(self, other):
        if isinstance(other, cVelocite):
            return (self.vitesse == other.vitesse) and (self.sens == other.sens)
        return False

    def __copy__(self) -> cVelocite:
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

    def __deepcopy__(self, memodict={}) -> cVelocite:
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
        new_obj._distance = copy.deepcopy(self._distance)
        new_obj._sens = copy.deepcopy(self._sens)

        memodict[id(self)] = new_obj

        return new_obj
