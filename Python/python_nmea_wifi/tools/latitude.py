from __future__ import annotations
from tools.angle import angle as angle

class latitude (angle):
    def __init__(self):
        super().__init__()
        self.__sens : chr = 'N'

    def __signe(self) -> float:
        if self.__sens == 'N':
            return 1.0
        return -1.0

    @property
    def val(self) -> float:
        return self.__signe() * self._angleEnDeg
    

    def toString(self) -> str:
        return f"{self.__sens} {super().toString(base=10)} [{self.__sens} {super().toString(base=60)}]"

    
    def __iadd__(self, val) -> latitude:
        self._angleEnDeg += val
        return self

    def __add__(self, val : latitude | float) -> latitude:
        retour = latitude()
        if val is latitude:
            retour._angleEnDeg = self._angleEnDeg + val._angleEnDeg
        else:
            retour._angleEnDeg = self._angleEnDeg + val
        return retour

    def copy(self) -> latitude:
        retour : latitude = latitude()
        retour.__sens = self.__sens
        retour._angleEnDeg = self._angleEnDeg
        return retour
 
    @staticmethod
    def fromFloatEnDeg(x:float) -> latitude:
        l : latitude = latitude()
        
        if x < 0: 
            l.__sens = "S"
            x = -1.0 * x
        
        if x > 90.0:
            x = 90.0
        
        l._angleEnDeg = x
        return l
    
            