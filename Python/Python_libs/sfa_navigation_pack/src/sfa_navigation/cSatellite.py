from __future__ import annotations
from typing import Dict, List
from sfa_tools import cMyException


class cSatellite:
    def __init__(self):
        self._inRange : List[str] = [
            "80", "71", "73", "79"
        ]


    @property
    def visible(self) -> List[str] :
        return self._inRange

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
            return cls()
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
    def fromDict(cls, data: Dict = {}) -> cSatellite:
        return cls()


    def toString(self) -> str:
        return "[cSatellite]"
    
        