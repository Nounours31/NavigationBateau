from __future__ import annotations

import copy

from sfa_tools import cMyException
from enum import Enum

from .cAngle import cAngle, eAngleFormat
from .cCap import cCap


class eVecteurUnit(Enum):
    KM = "km"
    MN = "MilleNautique"

class cVecteur:
    def __init__(self, norme : float | None = None, sens : cCap | None = None, unit: eVecteurUnit | None = None):
        self._norme : float = 0.0
        if not norme is None:
            self._norme = norme

        self._sens : cCap = cCap(0.0)
        if not sens is None:
            self._sens = sens

        self._unit : eVecteurUnit = eVecteurUnit.MN
        if not unit is None:
            self._unit = unit

    @property
    def norme(self) -> float:
        return self._norme
    
    @norme.setter
    def norme(self, v: float) -> None:
        self._norme = v

    @property
    def dir(self) -> cCap:
        return self._sens
    
    @dir.setter
    def dir(self, c: cCap) -> None:
        if not isinstance(c, cCap):
            raise cMyException("dir is not a Cap ...")
        self._sens = copy.deepcopy(c)
    

    def toString(self, format: eAngleFormat = eAngleFormat.DD) -> str:
        return f"[{self._norme:06.3f} {str(self._unit)} | {self._sens.toString(format=format)}]"


    def __copy__(self) -> cAngle:
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
        new_obj._norme = copy.copy(self._norme)
        new_obj._sens = copy.copy(self._sens)
        new_obj._unit = copy.copy(self._unit)
        return new_obj

    def __deepcopy__(self, memodict={}) -> cAngle:
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

        new_obj._norme = copy.deepcopy(self._norme)
        new_obj._sens = copy.deepcopy(self._sens)
        new_obj._unit = copy.deepcopy(self._unit)
        return new_obj
