from __future__ import annotations
import math

class angle:
    def __init__(self, val : float = 0.0):
        self._angleEnDeg : float = val

    def toString(self, base=0) -> str :
        d : int = math.floor (self._angleEnDeg)
        min : float = (self._angleEnDeg - d) * 60.0
        dmin : int = math.floor(min)
        sec : float = (min - math.floor (min)) * 60.0
        if base == 10:
            return f"{d:03d}°{min:06.3f}'"
        elif base == 60:
            return f"{d:03d}°{dmin:02d}'{sec:06.3f}\""
        else:
            return f"{d:03d}°{min:06.3f}' [{d:03d}°{dmin:02d}'{sec:06.3f}\"]"

    @property
    def val(self) -> float:
        return self._angleEnDeg
    
    @val.setter
    def val(self, x:float) -> None:
        while x < 0: 
            x += 360.0
        
        while x > 360.0: 
            x -= 360.0
        
        self._angleEnDeg = x

    @staticmethod
    def fromFloatEnDeg(x:float) -> angle:
        l : angle = angle()
        l.val = x
        return l
    
            