from __future__ import annotations
import copy

from .cDistance import cDistance
from .cDistance import eDistanceFormat


class cVitesse:
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
    # Opérateurs arithmétiques
    # ========================

    def __add__(self, other):
        return NotImplemented

    def __iadd__(self, other):
        return NotImplemented

    def __sub__(self, other):
        return NotImplemented

    def __isub__(self, other):
        return NotImplemented

    def __mul__(self, other):
        return NotImplemented

    def __imul__(self, other):
        return NotImplemented

    def __rmul__(self, other):
        return self.__mul__(other)

    def __truediv__(self, other):
        return NotImplemented

    def __itruediv__(self, other):
        return NotImplemented

    def __ne__(self, other):
        return not self.__eq__(other)

    def __eq__(self, other):
        return NotImplemented

    def __lt__(self, other):
        return NotImplemented

    def __le__(self, other):
        return self < other or self == other

    def __gt__(self, other):
        return NotImplemented

    def __ge__(self, other):
        return self > other or self == other

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
