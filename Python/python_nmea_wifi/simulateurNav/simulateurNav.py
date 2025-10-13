import logging
import logging.config

from math import floor, pi, cos, sin, tan, sqrt, atan2
from datetime import datetime, timezone
from typing import Dict

from myEnv import myEnv

from tools.vecteur import vecteur
from tools.latitude import latitude as latitude 
from tools.longitude import longitude as longitude 
from tools.position import position as position 
from tools.angle import angle as angle 
from tools.cap import cap


deg2rad = pi / 180.0
rad2deg = 180.0 / pi

class simulateurNav:
    def __init__(self, config: dict[str, float | object]):
        self.__logger : logging.Logger = myEnv.logger
        self.__heuredepart : datetime = datetime.now()
        self.__heure : datetime = datetime.now()
        self.__vitesseMoyenne : vecteur = vecteur('Kt')
        self.__vitesseMoyenne.val = config["vitesseEnNoeudMoyenne"]
        self.__vitesseMoyenne.dir  = cap(valAsDeg=config["capMoyen"])

        self.__vitesse : vecteur = vecteur('Kt')
        self.__vitesse.val = config["vitesseEnNoeudMoyenne"]
        self.__vitesse.dir = cap(valAsDeg=config["capMoyen"])

        self.__variationMagnetiqueEnDeg : float  = config["variationMagnetique"]
        self.__positionDepart : position = position (
            latitude.fromString(config["positionDepart"]["LatitudeDecimale"]),
            longitude.fromString(config["positionDepart"]["LongitudeDecimale"]))
        self.__positionCourante : position = self.__positionDepart.copy()

        self.__ventReelDepart : vecteur = vecteur('Kt')
        self.__ventReelDepart.val = config["vent"]["vitesseEnNd"]
        self.__ventReelDepart.dir = cap(valAsDeg=config["vent"]["directionEnDeg"])

        self.__ventReel : vecteur = self.__ventReelDepart.copy()
        self.__ventApp : vecteur = self.__ventReelDepart.copy()

        self.__temperatureAirDepart : float = config["vent"]["temperature"]
        self.__temperatureAir : float = config["vent"]["temperature"]

        self.__profondeur : float = config["eau"]["profondeur"]
        self.__profondeurDepart : float = config["eau"]["profondeur"]
        self.__temperatureEau : float = config["eau"]["temperature"]
        self.__temperatureEauDepart : float = config["eau"]["temperature"]

        self.__courant : vecteur = vecteur('Kt')
        self.__courant.val = config["eau"]["courant"]["vitesseEnNd"]
        self.__courant.dir = cap(valAsDeg=config["eau"]["courant"]["directionEnDeg"])

        self.__courantDepart : vecteur = self.__courant.copy()

        self.__satellites : Dict[str, object] = {
            "IDs": [80, 71, 73, 79, 10, 1, 68],
            "PDOP": 1.83,
            "HDOP": 1.09,
            "VDOP": 1.47
        }

        self.__logger.info("Creation simulateurNav")
        if self.__logger.isEnabledFor(level = logging.DEBUG):
            self.__logger.debug(self.toString())
    
    def toString(self) -> str:
        retour : str = ""
        retour += "Etape de Nav:\n"
        retour += f"\tHeure depart:     {self.__heuredepart.isoformat()}\n"
        retour += f"\tVitesse moyenne:  {self.__vitesseMoyenne.toString()}\n"
        retour += f"\tW:           {self.__variationMagnetiqueEnDeg:6.2f} °\n"
        retour += f"\tDepart:      {self.__positionDepart.toString()}\n"

        retour += f"\tHeure:       {self.__heure.isoformat()}\n"
        retour += f"\tV:           {self.__vitesse.toString()}\n"
        retour += f"\tPosition:    {self.__positionCourante.toString()}\n"

        retour += f"\tCourant:     {self.__courant.toString()}\n"

        retour += f"\tVent reel:   {self.__ventReel.toString()}\n"
        retour += f"\tVent app:    {self.__ventApp.toString()}\n"
        retour += f"\tT air:       {self.__temperatureAir:6.2f} °\n"

        retour += f"\tProfondeur:  {self.__profondeur:6.2f} m\n"
        retour += f"\tT eau:       {self.__temperatureEau:6.2f} °\n"
        retour += f"\tSatellites:  {self.__satellites} °\n"

        return retour

    @property
    def heure(self) -> datetime :
        return self.__heure
     
    @property
    def positionCourante(self) -> position:
        return self.__positionCourante

    @property
    def variationMagnetiqueEnDeg(self) -> float :
        return self.__variationMagnetiqueEnDeg

    @property
    def vitesse(self) -> vecteur :
        return self.__vitesse

    @property
    def ventReel(self) -> vecteur :
        return self.__ventReel

    @property
    def ventApparent(self) -> vecteur :
        return self.__ventApp

    @property
    def satellite (self) -> Dict[str,object]:
        return self.__satellites

    @property
    def profondeur (self) -> float:
        return self.__profondeur

    @property
    def eauTemp (self) -> float:
        return self.__temperatureEau

    @property
    def airTemp (self) -> float:
        return self.__temperatureAir

    # ----------------------------------------------------------------------------------
    # Navigation a :
    #   - cap       constant 
    #   - vitesse   constante 
    #   - lat moyenne
    #   - a une position de depart (positionDepartLatitude / positionDepartLongitude) 
    # ----------------------------------------------------------------------------------
    def nav(self) -> None :
        maintenant = datetime.now()
        iSeconde = maintenant.timestamp() - self.__heure.timestamp()
        self.__heure = maintenant

        # facteur perturbant
        facteur = (self.__heure.timestamp() - self.__heuredepart.timestamp()) * deg2rad 
        
        # deplacement
        self.__vitesse.val = self.__vitesseMoyenne.val * (1 + 0.25 * cos(facteur))
        self.__vitesse.dir = cap(self.__vitesseMoyenne.dir.valAsDeg * (1.0 + 0.25 * cos(facteur)))

        capRad = self.__vitesse.dir.valAsRad
        latitudeEstimeeRad = self.__positionCourante.latitude.valAsRad

        pasEnLatitude = cos (capRad) * self.__vitesse.val / 60 # noeud = mille/h - 1 mille = 1 minute d'arc
        pasEnLongitude = sin (capRad) * self.__vitesse.val / (60 * cos (latitudeEstimeeRad))
        
        self.__positionCourante.latitude += angle(valAsDeg=(pasEnLatitude * iSeconde / 3600))
        self.__positionCourante.longitude += angle(valAsDeg=(pasEnLongitude * iSeconde / 3600))

        # vent
        self.__ventReel.val = self.__ventReelDepart.val * (1 + cos(facteur))
        self.__ventReel.dir = cap(self.__ventReelDepart.dir.valAsDeg + (180 * cos(facteur)))

        ventAppX = (self.__ventReel.val * cos(self.__ventReel.dir.valAsRad) - self.__vitesse.val * cos(self.__vitesse.dir.valAsRad))
        ventAppY = (self.__ventReel.val * sin(self.__ventReel.dir.valAsRad) - self.__vitesse.val * sin(self.__vitesse.dir.valAsRad))
        ventAppForce = sqrt (ventAppX ** 2 + ventAppY **2)
        ventAppDir : angle = angle(valAsDeg=(atan2 (ventAppY, ventAppX) * rad2deg))
        
        self.__ventApp.val = ventAppForce
        self.__ventApp.dir = cap(valAsDeg=ventAppDir.valAsDeg)
        self.__temperatureAir = self.__temperatureAirDepart + (5.0 * (cos(facteur)))

        # Eau (profondeur + temp)
        self.__profondeur = self.__profondeurDepart + (100 * (1+cos(facteur)))
        self.__temperatureEau = self.__temperatureEauDepart + (2.0 * (cos(facteur)))

        self.__courant.val = self.__courantDepart.val * (1 + cos(facteur))
        self.__courant.dir.valAsDeg = self.__courantDepart.dir.valAsDeg + (180 * cos(facteur))

        if self.__logger.isEnabledFor(level = logging.DEBUG):
            self.__logger.debug(self.toString())

        return
        

