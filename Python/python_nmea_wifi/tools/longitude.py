from __future__ import annotations
from tools.angle import angle as angle

class longitude (angle):
    def __init__(self):
        super().__init__()
        self.__sens : chr = 'E'

    def __signe(self) -> float:
        if self.__sens == 'N':
            return 1.0
        return -1.0

    @property
    def val(self) -> float:
        return self.__signe() * self._angleEnDeg
    

    def toString(self) -> str:
        return f"{self.__sens} {super().toString(base=10)} [{self.__sens} {super().toString(base=60)}]"

    def __iadd__(self, val) -> longitude:
        self._angleEnDeg += val
        return self

    def __add__(self, val : longitude | float) -> longitude:
        retour = longitude()
        if val is longitude:
            retour._angleEnDeg = self._angleEnDeg + val._angleEnDeg
        else:
            retour._angleEnDeg = self._angleEnDeg + val
        return retour
    
    def copy(self) -> longitude:
        retour : longitude = longitude()
        retour.__sens = self.__sens
        retour._angleEnDeg = self._angleEnDeg
        return retour

    @staticmethod
    def fromFloatEnDeg(x:float) -> longitude:
        l : longitude = longitude()
        
        if x < 0: 
            l.__sens = "W"
            x = -1.0 * x
        
        if x > 180.0:
            x = 180.0
        
        l._angleEnDeg = x
        return l
    
            