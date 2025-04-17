import logging
from logging import Logger
from math import pi, cos, sin, atan2, atan

from TrameNMEANav.CLatitude import CLatitude
from TrameNMEANav.CLogger import CLogger
from TrameNMEANav.CLongitude import CLongitude
from TrameNMEANav.CPosition import CPosition


class CNavigation:
    DEG_2_PI: float = pi / 180.0

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

        capRad = cap * CNavigation.DEG_2_PI
        latitudeEstimeeRad = positionDepart.latitude().value() * CNavigation.DEG_2_PI

        pasEnLatitude = cos(capRad) * vitesseEnNoeud / 60  # noeud = mille/h - 1 mille = 1 minute d'arc
        pasEnLongitude = sin(capRad) * vitesseEnNoeud / (60 * cos(latitudeEstimeeRad))

        positionLatitude : float = positionDepart.latitude().value() + pasEnLatitude * iSecondeDepuisDepart / 3600
        positionLongitude : float = positionDepart.longitude().value() + pasEnLongitude * iSecondeDepuisDepart / 3600
        retour.latitude(CLatitude(positionLatitude))
        retour.longitude(CLongitude(positionLongitude))

        if self.__logger.isEnabledFor(logging.INFO):
            msg: str = f">>> iSecondeDepuisDepart: {iSecondeDepuisDepart:05.10f}, vitesseEnNoeud: {vitesseEnNoeud:09.2f}, cap: {cap:09.2f}, positionDepart {positionDepart:s}, position: {retour:s}"
            self.__logger.info(msg)

            msg = f"<<< position : {retour:s}"
            self.__logger.info(msg)

        return retour

    def navLoxodromique(self, positionDepart: CPosition, positionArrivee: CPosition) -> (float, float):
        latMoyenne = (positionArrivee.latitude().value() + positionDepart.latitude().value()) / 2.0
        variationLat = (positionArrivee.latitude().value() - positionDepart.latitude().value())
        variationLon = (positionArrivee.longitude().value() - positionDepart.longitude().value())

        latMoyenne *= CNavigation.DEG_2_PI
        variationLat *= CNavigation.DEG_2_PI
        variationLon *= CNavigation.DEG_2_PI

        if abs(variationLat) < 0.00000001:
            RouteQuartFond = 90.0
        else:
            tanRouteQuartFond = abs(variationLon / variationLat) * cos (latMoyenne)
            RouteQuartFond = atan(tanRouteQuartFond) / CNavigation.DEG_2_PI

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

        distanceMille = (variationLat * 60.0 / CNavigation.DEG_2_PI) / cos (routeFond * CNavigation.DEG_2_PI)

        return distanceMille, routeFond
