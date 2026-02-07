from __future__ import annotations

import math

from .cLongitude import cLongitude
from .cLatitude import cLatitude
from .cDistance import cDistance
from .cAngle import cAngle
from .cAngle import eAngleFormat
from .cCap import cCap
from .cVecteur import cVecteur
from sfa_tools import cMyException


class cPosition:
    def __init__(self, lat: cLatitude, lon: cLongitude):
        self._lat = lat
        self._long = lon
    
    @property
    def lat(self) -> cLatitude:
        return self._lat

    @lat.setter
    def lat(self,l:cLatitude) -> None:
        self._lat = l

    @property
    def longi(self) -> cLongitude:
        return self._long

    @longi.setter
    def longi(self,l:cLongitude) -> None:
        self._long = l


    def gudermannInverse(self):
        latitudeEnRad : float = self.lat.latitudeEnRad
        return math.log(math.tan((math.pi/4.0) + (latitudeEnRad / 2.0)))

    # see https://fr.wikipedia.org/wiki/Loxodromie
    def positionementRelatif(self, arrivee : cPosition) -> cVecteur :
        depart : cPosition = self
        varLat : float = arrivee.lat.latitudeEnDeg - depart.lat.latitudeEnDeg
        varLong : float = arrivee.longi.longitudeEnDeg - depart.longi.longitudeEnDeg

        if math.fabs(varLat) > (1 / 3600):
            tangentRouteQuartFond : float = math.fabs(varLong)
            tangentRouteQuartFond =  (math.fabs(arrivee.longi.longitudeEnRad - depart.longi.longitudeEnRad)
                                      / math.fabs(arrivee.gudermannInverse() - depart.gudermannInverse()))
            RouteQuartFond = math.atan(tangentRouteQuartFond)

        else:
            RouteQuartFond = math.pi / 2.0

        """
        Cette route notée Rfq a un équivalent Rf compris entre 0° et 360°. 
        Par exemple :
            si Rfq = N60°E alors Rf = 060° (= 000°+060°)
            si Rfq = N60°W alors Rf = 300° (= 360°-060°)
            si Rfq = S60°E alors Rf = 120° (= 180°-060°)
            si Rfq = S60°W alors Rf = 240° (= 180°+060°)
        """
        RouteQuartFond = RouteQuartFond * cAngle.RAD2DEG
        routeFond : float = 0.0
        if varLat >= 0.0 and varLong >= 0.0:
            # route = "NE"
            routeFond = RouteQuartFond
        if varLat >= 0.0 and varLong < 0.0:
            # route = "NW"
            routeFond = 360 - RouteQuartFond
        if varLat < 0.0 and varLong >= 0.0:
            # route = "SE"
            routeFond = 180 - RouteQuartFond
        if varLat < 0.0 and varLong < 0.0:
            # route = "SW"
            routeFond = 180 + RouteQuartFond

        latitudeMoyenne : cLatitude = (arrivee.lat / 2.0 + depart.lat / 2.0)
        c: cCap = cCap(valAsDeg=routeFond)
        d: float = (math.fabs(arrivee.longi.longitudeEnDeg - depart.longi.longitudeEnDeg) * 60.0 * math.cos (latitudeMoyenne.latitudeEnRad) /
                    math.sin (RouteQuartFond * cAngle.DEG2RAD))

        dist : cDistance = cDistance(valAsMilleNautique=d)
        v: cVecteur = cVecteur(distance=dist, sens=c)
        return v

    # format lat, long
    @staticmethod
    def fromString(angleAsString : str = "") -> cPosition:
        retour : cPosition | None = None
        try:
            allInfo : list[str] = angleAsString.split(",")
            lat : cLatitude = cLatitude.fromString(allInfo[0])
            longi : cLongitude = cLongitude.fromString(allInfo[1])
            retour = cPosition(lat, longi)
        except Exception as e:
            raise cMyException (str(e))

        return retour

    # format lat, long
    def __str__(self) -> str:
        return self.toString()

    def __repr__(self) -> str:
        return "[cPosition: " + self.toString() + "]"

    def toString(self, format: eAngleFormat = eAngleFormat.DD) -> str:
        return self.lat.toString(format) + ", " + self.longi.toString(format)
