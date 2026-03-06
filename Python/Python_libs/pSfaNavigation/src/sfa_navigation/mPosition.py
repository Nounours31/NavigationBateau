from __future__ import annotations

import copy
from typing import Dict

from .mLongitude import cLongitude
from .mLatitude import cLatitude
from .mAngle import eAngleFormat

from pSfaTools.mMyException import cMyException


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
    def fromObject(cls, o: object) -> cPosition:
        if isinstance(o, cPosition):
            return o.__deepcopy__()
        if isinstance(o, str):
            return cPosition.fromString(o)
        if isinstance(o, dict):
            return cPosition.fromDict(o)

        raise cMyException("Impossible de contruire une cPosition")

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
            lat: cLatitude = cLatitude.fromString(data[cLatitude.NOM])
            longi: cLongitude = cLongitude.fromString(data[cLongitude.NOM])
            retour: cPosition = cls(lat, longi)
            return retour
        except Exception as e:
            raise cMyException(str(e)) from e


    def toString(self, format: eAngleFormat = eAngleFormat.DD) -> str:
        return self.latitude.toString(format) + ", " + self.longitude.toString(format)

    def gudermannInverse(self):
        return self.latitude.gudermannInverse()



    # format latitude, long
    def __str__(self) -> str:
        return self.toString()

    def __repr__(self) -> str:
        return "[cPosition: " + self.toString() + "]"


    def __copy__(self) -> cPosition:
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
        new_obj._lat = copy.copy(self._lat)
        new_obj._long = copy.copy(self._long)
        return new_obj

    def __deepcopy__(self, memodict={}) -> cPosition:
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
        new_obj._lat = copy.deepcopy(self._lat)
        new_obj._long = copy.deepcopy(self._long)

        memodict[id(self)] = new_obj

        return new_obj


