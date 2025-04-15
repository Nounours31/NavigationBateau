from logging import Logger
from math import pi, cos, sin

from TrameNMEANav.CLogger import CLogger


class CNavigation:
    logger: Logger = CLogger.getLogger("CNavigation", None, None)
    DEG_2_PI : float = pi / 180.0

    def __init__(self):
        pass

    # ----------------------------------------------------------------------------------
    # Navigation a :
    #   - cap       constant
    #   - vitesse   constante
    #   - lat moyenne
    #   - a une position de depart (positionDepartLatitude / positionDepartLongitude)
    # ----------------------------------------------------------------------------------
    @staticmethod
    def nav(iSecondeDepuisDepart: int, vitesseEnNoeud: float, cap: float, latitudeEstimee: float,
            positionDepartLatitude: float, positionDepartLongitude: float) -> tuple[float, float]:
        capRad = cap * CNavigation.DEG_2_PI
        latitudeEstimeeRad = latitudeEstimee * CNavigation.DEG_2_PI

        pasEnLatitude = cos(capRad) * vitesseEnNoeud / 60  # noeud = mille/h - 1 mille = 1 minute d'arc
        pasEnLongitude = sin(capRad) * vitesseEnNoeud / (60 * cos(latitudeEstimeeRad))

        positionLatitude = positionDepartLatitude + pasEnLatitude * iSecondeDepuisDepart / 3600
        positionLongitude = positionDepartLongitude + pasEnLongitude * iSecondeDepuisDepart / 3600

        CNavigation.logger.info(
            ">>> iSecondeDepuisDepart: {:03d}, vitesseEnNoeud: {:09.2f}, cap: {:09.2f}, latitudeEstimee: {:011.4f}, positionDepartLatitude: {:011.4f}, positionDepartLongitude: {:011.4f}".format(
                iSecondeDepuisDepart, vitesseEnNoeud, cap, latitudeEstimee, positionDepartLatitude,
                positionDepartLongitude))
        CNavigation.logger.info(
            "<<< positionLatitude: {:011.4f}, positionLongitude: {:011.4f}".format(positionLatitude, positionLongitude))

        return positionLatitude, positionLongitude


