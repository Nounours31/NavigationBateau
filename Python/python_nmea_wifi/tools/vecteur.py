from __future__ import annotations
from tools.angle import angle
from tools.cap import cap
from tools.myException import MyException

class vecteur:
    def __init__(self, unit: str):
        self.__norme : float = 0.0        
        self.__sens : cap = cap(0.0)
        self.__unit : str = unit

    @property
    def val(self) -> float:
        return self.__norme
    
    @val.setter
    def val(self, v: float) -> None:
        self.__norme = v

    @property
    def dir(self) -> cap:
        return self.__sens
    
    @dir.setter
    def dir(self, c: cap) -> None:
        if not isinstance(c, cap):
            raise MyException("dir is not a Cap ...")
        self.__sens = c.copy()
    
    def copy(self) -> vecteur:
        retour : vecteur = vecteur(self.__unit)
        retour.__norme = self.__norme
        retour.__sens = self.__sens
        retour.__unit = self.__unit
        return retour
    
    def toString(self) -> str :
        return f"[{self.__norme:06.3f} {self.__unit} | {self.__sens.toString(base=angle.STR_AsALL)}]"