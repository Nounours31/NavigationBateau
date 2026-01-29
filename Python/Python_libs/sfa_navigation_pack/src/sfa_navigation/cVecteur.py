from __future__ import annotations

from sfa_tools import cMyException
from .cAngle import cAngle, eAngleFormat

from .cCap import cCap


class cVecteur:
    def __init__(self, unit: str):
        self.__norme : float = 0.0        
        self.__sens : cCap = cCap(0.0)
        self.__unit : str = unit

    @property
    def val(self) -> float:
        return self.__norme
    
    @val.setter
    def val(self, v: float) -> None:
        self.__norme = v

    @property
    def dir(self) -> cCap:
        return self.__sens
    
    @dir.setter
    def dir(self, c: cCap) -> None:
        if not isinstance(c, cCap):
            raise cMyException("dir is not a Cap ...")
        self.__sens = c.copy()
    
    def copy(self) -> cVecteur:
        retour : cVecteur = cVecteur(self.__unit)
        retour.__norme = self.__norme
        retour.__sens = self.__sens
        retour.__unit = self.__unit
        return retour
    
    def toString(self) -> str :
        return f"[{self.__norme:06.3f} {self.__unit} | {self.__sens.toString(eAngleFormat.DD)}]"