from __future__ import annotations
import copy
from typing import Dict, List

from pSfaTools.mMyException import cMyException





class cSatellite:
    def __init__(self):
        self._SatInRange : List[int] = [ 80, 71, 73, 79 ]


    @property
    def visible(self) -> List[int] :
        return self._SatInRange

    @property
    def PDOP(self) -> float :
        return 1.83
    
    @property
    def VDOP(self) -> float :
        return 1.09

    @property
    def HDOP(self) -> float :
        return 1.47



    # format latitude, long
    @classmethod
    def fromObject(cls, o: object) -> cSatellite:
        if isinstance(o, cSatellite):
            return copy.deepcopy(o)
        if isinstance(o, str):
            return cSatellite.fromString(o)
        if isinstance(o, dict):
            return cSatellite.fromDict(o)

        raise cMyException("Impossible de contruire une cPosition")

    # format latitude, long
    @classmethod
    def fromString(cls, angleAsString: str = "") -> cSatellite:
        return cls()

    @classmethod
    def fromDict(cls, data: Dict[str,object] | None = None) -> cSatellite:
        if data is None:
            data = {}
        return cls()


    def toString(self) -> str:
        return "[cSatellite]"
    
        