from __future__ import annotations

import math
from math import cos, sin

from . import cVitesse, cDistance, cCap
from .cPosition import cPosition
from .cLongitude import cLongitude
from .cLatitude import cLatitude
from .cAngle import eAngleFormat, cAngle
from .cVitesse import cVitesse


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
    def positionementRelatif(self, arrivee: cPosition) -> cVitesse:
        depart: cPosition = self._position
        varLat: float = arrivee.latitude.latitudeEnDeg - depart.latitude.latitudeEnDeg
        varLong: float = arrivee.longitude.longitudeEnDeg - depart.longitude.longitudeEnDeg

        if math.fabs(varLat) > (1 / 3600):
            tangentRouteQuartFond: float = math.fabs(varLong)
            tangentRouteQuartFond = math.fabs(
                arrivee.longitude.longitudeEnRad - depart.longitude.longitudeEnRad
            ) / math.fabs(arrivee.gudermannInverse() - depart.gudermannInverse())
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

        latitudeMoyenne: cLatitude = arrivee.latitude / 2.0 + depart.latitude / 2.0
        c: cCap = cCap(valAsDeg=routeFond)
        d: float = (
            math.fabs(arrivee.longitude.longitudeEnDeg - depart.longitude.longitudeEnDeg)
            * 60.0
            * math.cos(latitudeMoyenne.latitudeEnRad)
            / math.sin(RouteQuartFond * cAngle.DEG2RAD)
        )

        dist: cDistance = cDistance(valAsMilleNautique=d)
        v: cVitesse = cVitesse(distance=dist, sens=c)
        return v

    def navACapEtVitesseDonnes(
        self,
        tempsDeNavEnSeconde: float,
        vitesseEnNoeud: cVitesse,
        cap: cCap,
        positionDepart: cPosition,
    ) -> cPosition:
        retour: cPosition = cPosition(lat=cLatitude(valAsDeg=0.0), lon=cLongitude(valAsDeg=0.0))

        capRad: float = cap.capAsRad
        latitudeEstimeeRad: float = positionDepart.latitude.latitudeEnRad

        tempsDeNavEnHeure: float = tempsDeNavEnSeconde / 3600.0
        pasEnLatitudeEnDeg: float = tempsDeNavEnHeure * (
            cos(capRad) * vitesseEnNoeud / 60.0
        )  # noeud = mille/h - 1 mille = 1 minute d'arc
        pasEnLongitudeEnDeg: float = tempsDeNavEnHeure * (
            sin(capRad) * vitesseEnNoeud / (60.0 * cos(latitudeEstimeeRad))
        )

        positionLatitudeDeg: float = positionDepart.latitude.latitudeEnDeg + pasEnLatitudeEnDeg
        positionLongitudeDeg: float = positionDepart.longitude.longitudeEnDeg + pasEnLongitudeEnDeg
        retour.latitude = cLatitude(valAsDeg=positionLatitudeDeg)
        retour.longitude = cLongitude(valAsDeg=positionLongitudeDeg)

        return retour
