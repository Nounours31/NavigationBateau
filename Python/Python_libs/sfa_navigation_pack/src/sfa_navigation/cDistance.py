from __future__ import annotations
import copy
from enum import Enum


class eDistanceFormat(Enum):
    FULL = "Full"
    STD = "Std"

class cDistance:
    MN2KM: float = 1.852
    KM2MN: float = 1 / MN2KM

    DISPLAY_SHORT : int = 0
    DISPLAY_LONG : int = 1

    EQUAL_TOLERANCE_IN_MN : float = 0.00001

    def __init__(self, valAsMilleNautique : float | None = None, valAsKm : float | None = None):
        self._toleranceEnMn = cDistance.EQUAL_TOLERANCE_IN_MN

        if isinstance(valAsMilleNautique, (int, float)) :
            self._valAsMilleNautique = valAsMilleNautique


        elif isinstance(valAsKm, (int, float)):
            self._valAsMilleNautique = valAsKm * cDistance.KM2MN

        else:
            self._valAsMilleNautique = 0.0


    @property
    def asMn(self) -> float:
        return self._valAsMilleNautique


    @asMn.setter
    def asMn(self, x:float) -> None:
        self._valAsMilleNautique = x


    @property
    def asKm(self) -> float:
        return self._valAsMilleNautique * cDistance.MN2KM

    # ========================
    # Opérateurs arithmétiques
    # ========================

    def __add__(self, other):
        if isinstance(other, cDistance):
            return cDistance(valAsMilleNautique=self.asMn + other.asMn)
        if isinstance(other, (int, float)):
            return cDistance(valAsMilleNautique=self.asMn + other)
        return NotImplemented

    def __iadd__(self, other):
        if isinstance(other, cDistance):
            self.asMn += other.asMn
            return self
        if isinstance(other, (int, float)):
            self.asMn += other
            return self
        return NotImplemented

    def __sub__(self, other):
        if isinstance(other, cDistance):
            return cDistance(valAsMilleNautique=self.asMn - other.asMn)
        if isinstance(other, (int, float)):
            return cDistance(valAsMilleNautique=self.asMn - other)
        return NotImplemented

    def __isub__(self, other):
        if isinstance(other, cDistance):
            self.asMn -= other.asMn
            return self
        if isinstance(other, (int, float)):
            self.asMn -= other
            return self
        return NotImplemented

    def __mul__(self, other):
        if isinstance(other, cDistance):
            return cDistance(valAsMilleNautique=self.asMn * other.asMn)

        if isinstance(other, (int, float)):
            return cDistance(valAsMilleNautique=self.asMn * other)
        return NotImplemented

    def __imul__(self, other):
        if isinstance(other, cDistance):
            self.asMn *= other.asMn
            return self

        if isinstance(other, (int, float)):
            self.asMn *= other
            return self
        return NotImplemented

    def __rmul__(self, other):
        return self.__mul__(other)

    def __truediv__(self, other):
        if isinstance(other, (int, float)):
            return cDistance(valAsMilleNautique=self.asMn / other)
        elif isinstance(other, cDistance):
            return cDistance(valAsMilleNautique=self.asMn / other.asMn)
        return NotImplemented

    def __itruediv__(self, other):
        if isinstance(other, (int, float)):
            self.asMn /= other
            return self
        elif isinstance(other, cDistance):
            self.asMn /= other.asMn
            return self

        return NotImplemented

    # ========================
    # Comparaisons
    # ========================

    def __ne__(self, other):
        return not self.__eq__(other)

    def __eq__(self, other):
        if isinstance(other, cDistance):
            return abs(self.asMn - other.asMn) <= self._toleranceEnMn
        if isinstance(other, (int, float)):
            return abs(self.asMn - other) <= self._toleranceEnMn
        return False

    def __lt__(self, other):
        if isinstance(other, cDistance):
            return self.asMn < other.asMn
        if isinstance(other, (int, float)):
            return self.asMn < other
        return NotImplemented

    def __le__(self, other):
        return self < other or self == other

    def __gt__(self, other):
        if isinstance(other, cDistance):
            return self.asMn > other.asMn
        if isinstance(other, (int, float)):
            return self.asMn > other
        return NotImplemented

    def __ge__(self, other):
        return self > other or self == other

    # ========================
    # Conversions & affichage
    # ========================
    def toString(self, format: eDistanceFormat = eDistanceFormat.STD):
        if format == eDistanceFormat.STD:
            return f"{self.asMn:.3f}Mn"

        elif format == eDistanceFormat.FULL:
            return f"{self.asMn:.3f}Mn ({self.asKm:.3f} km)"

    def __str__(self):
        return self.toString(eDistanceFormat.STD)

    def __repr__(self):
        return f"[cDistance({self.asMn:.3f})]"


    def __copy__(self) -> cDistance:
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
        new_obj._valAsMilleNautique = copy.copy(self._valAsMilleNautique)
        new_obj._toleranceEnMn = copy.copy(self._toleranceEnMn)
        return new_obj

    def __deepcopy__(self, memodict={}) -> cDistance:
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

        new_obj._valAsMilleNautique = copy.deepcopy(self._valAsMilleNautique, memodict)
        new_obj._toleranceEnMn = copy.deepcopy(self._toleranceEnMn, memodict)
        return new_obj
    
            