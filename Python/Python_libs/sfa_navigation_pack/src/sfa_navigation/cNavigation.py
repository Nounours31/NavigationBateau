from __future__ import annotations

import math
from math import cos, dist, sin

from . import cVelocite, cDistance, cCap
from .cPosition import cPosition
from .cLongitude import cLongitude
from .cLatitude import cLatitude
from .cAngle import eAngleFormat, cAngle
from .cVelocite import cVelocite


class cNavigation:
    def __init__(self, position: cPosition):
        self._position = position

    @property
    def position(self) -> cPosition:
        return self._position

    @position.setter
    def position(self, p: cPosition) -> None:
        self._position = p

    # format latitude, long
    def __str__(self) -> str:
        return self._position.toString()

    def __repr__(self) -> str:
        return "[cNavigation: " + self._position.toString() + "]"

    def toString(self, format: eAngleFormat = eAngleFormat.DD) -> str:
        return self._position.toString(format)

    # see https://fr.wikipedia.org/wiki/Loxodromie
    def routeLoxodromique(self, arrivee: cPosition) -> tuple[cCap, cDistance]:
        depart: cPosition = self._position
        varLatEnDeg: float = arrivee.latitude.latitudeEnDeg - depart.latitude.latitudeEnDeg
        varLongEnDeg: float = arrivee.longitude.longitudeEnDeg - depart.longitude.longitudeEnDeg
        latMoyenneEnRad: float = (arrivee.latitude.latitudeEnRad + depart.latitude.latitudeEnRad) / 2.0

        # calcul de la Rv
        LatitudeCroissanteRadArrivee: float = arrivee.gudermannInverse()
        LatitudeCroissanteRadDepart: float = depart.gudermannInverse()
        
        RouteQuartFond : float = 0.0
        RouteQuartFondEnDeg : float = 0.0
        routeFondEnDeg: float = 0.0  

        if math.fabs(varLatEnDeg) > (1 / 3600):
            tangentRv = arrivee.longitude.angleAsRad - depart.longitude.angleAsRad
            tangentRv = tangentRv / (LatitudeCroissanteRadArrivee - LatitudeCroissanteRadDepart)
            RouteQuartFond = math.atan(math.fabs(tangentRv))    

        else:
            RouteQuartFond = math.pi / 2.0

        RouteQuartFondEnDeg = RouteQuartFond * cAngle.RAD2DEG
        routeFondEnDeg = cNavigation.RouteQuartFond2RouteFond(varLatEnDeg, varLongEnDeg, RouteQuartFondEnDeg)
        
        # calcul de la distance
        distanceEnMille: float = 0.0
        if RouteQuartFondEnDeg > 89.0:
            distanceEnMille = math.cos(latMoyenneEnRad) * varLongEnDeg * 60.0 # noeud = mille/h - 1 mille = 1 minute d'arc
            distanceEnMille /= math.sin(RouteQuartFond )
        else:
            distanceEnMille = varLatEnDeg * 60.0 # noeud = mille/h - 1 mille = 1 minute d'arc
            distanceEnMille /= math.cos (routeFondEnDeg * cAngle.DEG2RAD)

        c: cCap = cCap(valAsDeg=routeFondEnDeg)
        dist: cDistance = cDistance(valAsMilleNautique=distanceEnMille)
        return (c, dist)


    @staticmethod
    def RouteQuartFond2RouteFond(varLat: float, varLong: float, RouteQuartFond: float) -> float:
        """
        Cette route notée Rfq a un équivalent Rf compris entre 0° et 360°. 
        Par exemple :
            si Rfq = N60°E alors Rf = 060° (= 000°+060°)
            si Rfq = N60°W alors Rf = 300° (= 360°-060°)
            si Rfq = S60°E alors Rf = 120° (= 180°-060°)
            si Rfq = S60°W alors Rf = 240° (= 180°+060°)
        """
        routeFond: float = 0.0
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
        return routeFond
    

    # https://fr.wikipedia.org/wiki/Orthodromie#Distance_orthodromique
    def routeOrthodromique(self, arrivee: cPosition) -> tuple[cCap, cDistance, cPosition]:
        depart: cPosition = self._position
        varLatEnDeg: float = arrivee.latitude.latitudeEnDeg - depart.latitude.latitudeEnDeg
        varLongEnDeg: float = arrivee.longitude.longitudeEnDeg - depart.longitude.longitudeEnDeg

        # distance en Mn
        distanceEnMn: float = 0.0
        distanceEnMn = math.sin(depart.latitude.latitudeEnRad) * math.sin(arrivee.latitude.latitudeEnRad) 
        distanceEnMn += math.cos(depart.latitude.latitudeEnRad) * math.cos(arrivee.latitude.latitudeEnRad) * math.cos(arrivee.longitude.longitudeEnRad - depart.longitude.longitudeEnRad)
        distanceEnMn = math.acos(distanceEnMn) * cAngle.RAD2DEG * 60.0 # noeud = mille/h - 1 mille = 1 minute d'arc  

        coTangRouteInitaile: float = 0.0
        coTangRouteInitaile = math.cos(depart.latitude.latitudeEnRad) * math.sin(arrivee.latitude.latitudeEnRad)
        coTangRouteInitaile -= math.sin(depart.latitude.latitudeEnRad) * math.cos(arrivee.latitude.latitudeEnRad) * math.cos(arrivee.longitude.longitudeEnRad - depart.longitude.longitudeEnRad)
        coTangRouteInitaile /= (math.cos(arrivee.latitude.latitudeEnRad) * math.sin(arrivee.longitude.longitudeEnRad - depart.longitude.longitudeEnRad))

        tangRouteInitiale = 1.0 / coTangRouteInitaile
        routeInitiale = math.atan(tangRouteInitiale)
        routeQuartFondInitialeEnDeg : float = math.atan(tangRouteInitiale) * cAngle.RAD2DEG
        #routeInitialeEnDeg = cNavigation.RouteQuartFond2RouteFond(varLat=varLatEnDeg, varLong = varLongEnDeg, RouteQuartFond =routeQuartFondInitialeEnDeg)
        routeInitialeEnDeg = routeQuartFondInitialeEnDeg
        

        cosLatitudeVertex = math.fabs(math.sin(routeInitiale)) * math.cos(depart.latitude.latitudeEnRad)
        latitudeVertexEnRad = math.acos(cosLatitudeVertex)

        cosDeltaLongitudeVertex = math.tan(depart.latitude.latitudeEnRad) / math.tan(latitudeVertexEnRad)
        longitudeVertexEnRad = math.acos(cosDeltaLongitudeVertex) 
        if varLongEnDeg < 0.0:
            longitudeVertexEnRad = depart.longitude.longitudeEnRad - math.fabs(longitudeVertexEnRad)
        else:
            longitudeVertexEnRad = depart.longitude.longitudeEnRad + math.fabs(longitudeVertexEnRad)
        Vertex : cPosition = cPosition(lat=cLatitude(valAsDeg=latitudeVertexEnRad * cAngle.RAD2DEG), lon=cLongitude(valAsDeg=longitudeVertexEnRad * cAngle.RAD2DEG))

        return (cCap(valAsDeg=routeInitialeEnDeg), cDistance(valAsMilleNautique=distanceEnMn), Vertex)




    def navLoxodromiqueCapEtVitesseDonnes(self, tempsDeNavEnSeconde: float, v: cVelocite) -> cPosition:

        vitesseEnNoeud : float = v.vitesse.asNoeud
        distanceEnMille : float = vitesseEnNoeud * (tempsDeNavEnSeconde / 3600.0)

        capRad: float = v.sens.asAngleTrigonometriqueEnRad
        latitudeEstimeeRad: float = self.position.latitude.latitudeEnRad
        
        pasEnLatitudeEnDeg: float =  ( sin(capRad) * distanceEnMille / 60.0 )  # noeud = mille/h - 1 mille = 1 minute d'arc
        pasEnLongitudeEnDeg: float = ( cos(capRad) * distanceEnMille / 60.0) * cos(latitudeEstimeeRad)

        positionLatitudeDeg: float = self.position.latitude.latitudeEnDeg + pasEnLatitudeEnDeg
        positionLongitudeDeg: float = self.position.longitude.longitudeEnDeg + pasEnLongitudeEnDeg
        
        return cPosition(lat=cLatitude(valAsDeg=positionLatitudeDeg), lon=cLongitude(valAsDeg=positionLongitudeDeg))
