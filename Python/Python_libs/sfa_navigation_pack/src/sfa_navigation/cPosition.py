from __future__ import annotations

import math

from . import cLatitude, cAngle, cLongitude, cCap, cDistance

from sfa_tools import cMyException


class cPosition (cAngle):

    def __init__(self, lat: latitude, lon: longitude):
        self.__lat = lat
        self.__long = lon
    
    @property
    def latitude(self) -> latitude:
        return self.__lat

    @latitude.setter
    def latitude(self,l:latitude) -> None:
        self.__lat = l

    @property
    def longitude(self) -> longitude:
        return self.__long

    @longitude.setter
    def longitude(self,l:longitude) -> None:
        self.__long = l
    
    def copy(self) -> cPosition:
        retour : cPosition = cPosition(self.__lat.copy(), self.__long.copy())
        return retour 

    def toString(self, base :int = cAngle.STR_AsMin, detail : int = cAngle.DISPLAY_LONG):
        if detail == cAngle.DISPLAY_LONG:
            return f"Position: \t[latitude:{self.latitude.toString(base, detail)}  --- longitude:{self.longitude.toString(base, detail)}]"
        elif detail == cAngle.DISPLAY_SHORT:
            return f"{self.latitude.toString(base, detail)},{self.longitude.toString(base, detail)}"

    # format lat, long
    @staticmethod
    def fromString(s : str = "") -> cPosition:
        retour : cPosition | None = None
        try:
            allInfo : list[str] = s.split(",")
            lat : cLatitude = cLatitude.fromString(allInfo[0])
            longi : cLongitude = cLongitude.fromString(allInfo[1])
            retour = cPosition(lat, longi)
        except Exception as e:
            raise cMyException (str(e))

        return retour
    

    def gudermannInverse(self):
        latitudeEnRad : float = self.__lat.valAsRad
        return math.log2(math.tan((math.pi/4.0) + (latitudeEnRad / 2.0)))

    @staticmethod
    def positionementRelatif(depart : cPosition, arrivee : cPosition) -> (cCap, cDistance) :
        varLat : float = arrivee.latitude.valAsDeg - depart.latitude.valAsDeg
        varLong : float = arrivee.longitude.valAsDeg - depart.longitude.valAsDeg

        if math.fabs(varLat) > 1.0:
            tangentRouteQuartFond : float = math.fabs(arrivee.longitude.valAsRad - depart.longitude.valAsRad)
            tangentRouteQuartFond = tangentRouteQuartFond / math.fabs(arrivee.gudermannInverse() - depart.gudermannInverse())
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
