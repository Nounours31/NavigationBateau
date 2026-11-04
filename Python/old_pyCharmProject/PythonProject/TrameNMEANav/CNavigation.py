import logging
from logging import Logger
from math import pi, cos, sin, atan2, atan

from TrameNMEANav.CAngle import CAngle
from TrameNMEANav.CLatitude import CLatitude
from TrameNMEANav.CLogger import CLogger
from TrameNMEANav.CLongitude import CLongitude
from TrameNMEANav.CPosition import CPosition


class CNavigation:
    def __init__(self):
        self.__logger: Logger = CLogger.getLogger("CNavigation")

    # ----------------------------------------------------------------------------------
    # Navigation a :
    #   - cap       constant
    #   - vitesse   constante
    #   - lat moyenne
    #   - a une position de depart (positionDepartLatitude / positionDepartLongitude)
    # ----------------------------------------------------------------------------------
    def nav(self, iSecondeDepuisDepart: float, vitesseEnNoeud: float, cap: float, positionDepart: CPosition) -> CPosition:
        retour : CPosition = CPosition()

        capRad = cap * CAngle.DEG2RAD
        latitudeEstimeeRad = positionDepart.latitude().value() * CAngle.DEG2RAD

        pasEnLatitude = cos(capRad) * vitesseEnNoeud / 60  # noeud = mille/h - 1 mille = 1 minute d'arc
        pasEnLongitude = sin(capRad) * vitesseEnNoeud / (60 * cos(latitudeEstimeeRad))

        positionLatitude : float = positionDepart.latitude().value() + pasEnLatitude * iSecondeDepuisDepart / 3600
        positionLongitude : float = positionDepart.longitude().value() + pasEnLongitude * iSecondeDepuisDepart / 3600
        retour.latitude(CLatitude(positionLatitude))
        retour.longitude(CLongitude(positionLongitude))

        return retour

    def navLoxodromique(self, positionDepart: CPosition, positionArrivee: CPosition) -> (float, float):
        latMoyenne = (positionArrivee.latitude().value() + positionDepart.latitude().value()) / 2.0
        variationLat = (positionArrivee.latitude().value() - positionDepart.latitude().value())
        variationLon = (positionArrivee.longitude().value() - positionDepart.longitude().value())

        latMoyenne *= CAngle.DEG2RAD
        variationLat *= CAngle.DEG2RAD
        variationLon *= CAngle.DEG2RAD

        if abs(variationLat) < 0.00000001:
            RouteQuartFond = 90.0
        else:
            tanRouteQuartFond = abs(variationLon / variationLat) * cos (latMoyenne)
            RouteQuartFond = atan(tanRouteQuartFond) / CAngle.DEG2RAD

        SensRoute = "N" if variationLat > 0 else "S"
        SensRoute = SensRoute + ("E" if variationLon > 0 else "W")

        routeFond = RouteQuartFond
        if SensRoute == "NE":
            routeFond = RouteQuartFond + 0
        if SensRoute == "NW":
            routeFond = 360.0 - RouteQuartFond
        if SensRoute == "SE":
            routeFond = 180.0 - RouteQuartFond
        if SensRoute == "SW":
           routeFond = 180.0 + RouteQuartFond

        distanceMille = (variationLat * 60.0 / CAngle.DEG2RAD) / cos (routeFond * CAngle.DEG2RAD)

        return distanceMille, routeFond
