from __future__ import annotations

import math
from typing import Dict

from .cLongitude import cLongitude
from .cLatitude import cLatitude
from .cAngle import eAngleFormat
from sfa_tools import cMyException


class cPosition:
    def __init__(self, lat: cLatitude, lon: cLongitude):
        self._lat = lat
        self._long = lon

    @property
    def latitude(self) -> cLatitude:
        return self._lat

    @latitude.setter
    def latitude(self, ll: cLatitude) -> None:
        self._lat = ll

    @property
    def longitude(self) -> cLongitude:
        return self._long

    @longitude.setter
    def longitude(self, ll: cLongitude) -> None:
        self._long = ll

    # format latitude, long
    @classmethod
    def fromString(cls, angleAsString: str = "") -> cPosition:
        """ """
        try:
            allInfo: list[str] = angleAsString.split(",")
            lat: cLatitude = cLatitude.fromString(allInfo[0])
            longi: cLongitude = cLongitude.fromString(allInfo[1])
            retour: cPosition = cls(lat, longi)
            return retour
        except Exception as e:
            raise cMyException(str(e)) from e

    @classmethod
    def fromDict(cls, data: Dict = {}) -> cPosition:
        """ """
        try:
            lat: cLatitude = cLatitude.fromString(data["latitude"])
            longi: cLongitude = cLongitude.fromString(data["longitude"])
            retour: cPosition = cls(lat, longi)
            return retour
        except Exception as e:
            raise cMyException(str(e)) from e

    # format latitude, long
    def __str__(self) -> str:
        return self.toString()

    def __repr__(self) -> str:
        return "[cPosition: " + self.toString() + "]"

    def toString(self, format: eAngleFormat = eAngleFormat.DD) -> str:
        return self.latitude.toString(format) + ", " + self.longitude.toString(format)

    def gudermannInverse(self):
        latitudeEnRad: float = self.latitude.latitudeEnRad
        return math.log(math.tan((math.pi / 4.0) + (latitudeEnRad / 2.0)))
