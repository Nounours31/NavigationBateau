from __future__ import annotations
import math
from tools.myException import MyException

class angle:
    STR_AsREAL : int = 3
    STR_AsMin : int = 1
    STR_AsSec : int = 2
    STR_AsALL : int = 0
    STR_Echec : int = 4

    def __init__(self, valAsDeg : float = 0.0):
        self._angleEnDeg : float = valAsDeg

    def toString(self, base=STR_Echec) -> str :
        """
        :param base: 0 en reel, 10 en base 10, 60 en sexagedecimal
        :return: angle as string
        """
        if base == angle.STR_Echec:
            raise MyException ("toSting cas non prevu")

        d : int = math.floor (self._angleEnDeg)
        min : float = (self._angleEnDeg - d) * 60.0
        dmin : int = math.floor(min)
        sec : float = (min - math.floor (min)) * 60.0
        if base == angle.STR_AsREAL:
            return f"{self._angleEnDeg:08.4f}°"
        elif base == angle.STR_AsMin:
            return f"{d:03d}°{min:06.3f}'"
        elif base == angle.STR_AsSec:
            return f"{d:03d}°{dmin:02d}'{sec:06.3f}\""
        else:
            return f"{self._angleEnDeg:08.4f}° [{d:03d}°{min:06.3f}' # {d:03d}°{dmin:02d}'{sec:06.3f}\"]"

    @property
    def valAsDeg(self) -> float:
        return self._angleEnDeg

    @property
    def valAsRad(self) -> float:
        return (math.pi / 180.0) * self._angleEnDeg

    @valAsDeg.setter
    def valAsDeg(self, x:float) -> None:
        self._angleEnDeg = x

    def __iadd__(self, val : angle) -> angle:
        if val is angle:
            self._angleEnDeg += val.valAsDeg
            return self
        raise MyException("angle add: type error")

    def __add__(self, val : angle) -> angle:
        if val is angle:
            retour = angle()
            retour._angleEnDeg = self._angleEnDeg + val._angleEnDeg
            return retour
        raise MyException("angle add: type error")

    def _normalize(self) -> None:
        pass

    
            