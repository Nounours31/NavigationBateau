from __future__ import annotations

import math

from . import cLatitude, cAngle, cLongitude, cCap, cDistance, cVecteur, eAngleFormat

from sfa_tools import cMyException


class cPosition ():

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
        latitudeEnRad : float = self._lat.valAsRad
        return math.log2(math.tan((math.pi/4.0) + (latitudeEnRad / 2.0)))

    def positionementRelatif(self, arrivee : cPosition) -> cVecteur :
        varLat : float = arrivee.lat.val - self.lat.val
        varLong : float = arrivee.longi.val - self.longi.val

        if math.fabs(varLat) > 1.0:
            tangentRouteQuartFond : float = math.fabs(arrivee.longi.val - self.longi.val)
            tangentRouteQuartFond = tangentRouteQuartFond / math.fabs(arrivee.gudermannInverse() - self.gudermannInverse())
            RouteQuartFond = math.atan(tangentRouteQuartFond)

        else:
            RouteQuartFond = math.pi / 2.0

        """
        Cette route notée R f q {displaystyle Rf_{q}} a un équivalent R f {displaystyle Rf} compris entre 0° et 360°. 
        Par exemple :
            si Rfq = N60°E alors Rf = 060° (= 000°+060°)
            si Rfq = N60°W alors Rf = 300° (= 360°-060°)
            si Rfq = S60°E alors Rf = 120° (= 180°-060°)
            si Rfq = S60°W alors Rf = 240° (= 180°+060°)
        """
        RouteQuartFond = RouteQuartFond * 180.0 / math.pi
        route : str = ""
        if varLat >= 0.0 and varLong >= 0.0:
            route = "NE"
            routeFond = RouteQuartFond
        if varLat >= 0.0 and varLong < 0.0:
            route = "NW"
            routeFond = 360 - RouteQuartFond
        if varLat < 0.0 and varLong >= 0.0:
            route = "SE"
            routeFond = 180 - RouteQuartFond
        if varLat < 0.0 and varLong < 0.0:
            route = "SW"
            routeFond = 180 + RouteQuartFond

        c : cCap = cCap(valAsDeg=routeFond)

        dist = varLat * 60.0 / math.cos (c.valAsRad)
        d : cDistance = cDistance(dist)

        return (c, d)

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
