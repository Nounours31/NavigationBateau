import logging
import time
from math import cos, sin, sqrt, atan2

from typing import Dict, Any

from sfa_navigation import cVitesse, cPosition, cAngle, cCap, cVecteur
from sfa_tools import myLogger




class simulateurNav:
    _logger : logging.Logger = myLogger.getLogger("simulateurNav", logging.DEBUG)
    def __init__(self, config: dict[str, Any]):

        self._heuredepart : float = time.time()
        self._heure : float = time.time()

        '''
        {
            "vitesseEnNoeudMoyenne": 15.0,
            "variationMagnetique": -1.2,
            "nav": {
                "positionDepart": myEnv.postionTrinitee.toString(),
                "positionWayPoints": [
                    cPosition.fromString("2, 2").toString(base=cAngle.STR_AsMin, detail=cAngle.DISPLAY_SHORT),
                    cPosition.fromString("2, 2").toString(base=cAngle.STR_AsMin, detail=cAngle.DISPLAY_SHORT)
                ],
                "positionArrivee": myEnv.postionTrinitee.toString()
            },
            "vent": {
                "vitesseEnNd": 15,
                "directionEnDeg": 75,  # sens du vent attention !!!
                "temperature": 20
            },
            "courant": {
                "vitesseEnNd": 1.5,
                "directionEnDeg": 2.5,
            },
            "eau": {
                "profondeur": 17,
                "temperature": 12,
            }
        }
        '''
        self._vitesseMoyenne : cVitesse = config["vitesseEnNoeudMoyenne"]
        self._variationMagnetiqueEnDeg : float  = config["variationMagnetique"]

        self._positionDepart : cPosition = cPosition.fromString(config["nav"]["positionDepart"])
        self._positionArrivee : cPosition = cPosition.fromString(config["nav"]["positionArrivee"])
        self._positionWaypoints : list[cPosition] = []
        self._positionWaypointsAtteint : int = -1
        for p in config["nav"]["positionWayPoints"]:
            self._positionWaypoints.append(cPosition.fromString(p))
        self._positionCourante : cPosition = self._positionDepart.copy()


        self._ventReelDepart : cVecteur = cVecteur('Kt')
        self._ventReelDepart.val = config["vent"]["vitesseEnNd"]
        self._ventReelDepart.dir = cCap(valAsDeg=config["vent"]["directionEnDeg"])

        self._ventReel : cVecteur = self._ventReelDepart.copy()
        self._ventApp : cVecteur = self._ventReelDepart.copy()

        self._temperatureAirDepart : float = config["vent"]["temperature"]
        self._temperatureAir : float = config["vent"]["temperature"]

        self._profondeur : float = config["eau"]["profondeur"]
        self._profondeurDepart : float = config["eau"]["profondeur"]
        self._temperatureEau : float = config["eau"]["temperature"]
        self._temperatureEauDepart : float = config["eau"]["temperature"]

        self._courant : cVecteur = cVecteur('Kt')
        self._courantDepart : cVecteur = self._courant.copy()

        self._satellites : Dict[str, object] = {
            "IDs": [80, 71, 73, 79, 10, 1, 68],
            "PDOP": 1.83,
            "HDOP": 1.09,
            "VDOP": 1.47
        }

        self._logger.info("Creation simulateurNav")
        if self._logger.isEnabledFor(level = logging.DEBUG):
            self._logger.debug(self.toString())
    
    def toString(self) -> str:
        retour : str = ""
        retour += "Etape de Nav:\n"
        retour += f"\tHeure depart:     {self._heuredepart.isoformat()}\n"
        retour += f"\tVitesse moyenne:  {self._vitesseMoyenne.toString()}\n"
        retour += f"\tW:           {self._variationMagnetiqueEnDeg:6.2f} °\n"
        retour += f"\tDepart:      {self._positionDepart.toString()}\n"

        retour += f"\tHeure:       {self._heure.isoformat()}\n"
        retour += f"\tV:           {self._vitesse.toString()}\n"
        retour += f"\tcPosition:    {self._positionCourante.toString()}\n"

        retour += f"\tCourant:     {self._courant.toString()}\n"

        retour += f"\tVent reel:   {self._ventReel.toString()}\n"
        retour += f"\tVent app:    {self._ventApp.toString()}\n"
        retour += f"\tT air:       {self._temperatureAir:6.2f} °\n"

        retour += f"\tProfondeur:  {self._profondeur:6.2f} m\n"
        retour += f"\tT eau:       {self._temperatureEau:6.2f} °\n"
        retour += f"\tSatellites:  {self._satellites} °\n"

        return retour

    @property
    def heure(self) -> float :
        return self._heure
     
    @property
    def positionCourante(self) -> cPosition:
        return self._positionCourante

    @property
    def variationMagnetiqueEnDeg(self) -> float :
        return self._variationMagnetiqueEnDeg

    @property
    def vitesse(self) -> cVecteur :
        return self._vitesse

    @property
    def ventReel(self) -> cVecteur :
        return self._ventReel

    @property
    def ventApparent(self) -> cVecteur :
        return self._ventApp

    @property
    def satellite (self) -> Dict[str,object]:
        return self._satellites

    @property
    def profondeur (self) -> float:
        return self._profondeur

    @property
    def eauTemp (self) -> float:
        return self._temperatureEau

    @property
    def airTemp (self) -> float:
        return self._temperatureAir

    # ----------------------------------------------------------------------------------
    # Navigation a :
    #   - cCap       constant 
    #   - vitesse   constante 
    #   - lat moyenne
    #   - a une cPosition de depart (positionDepartLatitude / positionDepartLongitude) 
    # ----------------------------------------------------------------------------------
    def nav(self, timestamp : float) -> None :
        # calcul de la durre de la nav en secondes
        maintenant = time.time()
        intervalDeNavEnSec = (maintenant - self._heure) / 1000.0
        self._heure = maintenant

        # ----------------------------------
        # vers ou dois je aller waypoint ?
        # ----------------------------------
        # waypoint ou arrivee ?
        positionCourante : cPosition = self._positionCourante
        objectif : cPosition = None
        objectifSecondaire : cPosition = None
        if ((len(self._positionWaypoints) > 0) and (len(self._positionWaypoints) > 1+self._positionWaypointsAtteint)):
            objectif = self._positionWaypoints[1+self._positionWaypointsAtteint]
            if ((len(self._positionWaypoints) > 0) and (len(self._positionWaypoints) > 2 + self._positionWaypointsAtteint)):
                objectifSecondaire = self._positionWaypoints[2 + self._positionWaypointsAtteint]
            else:
                objectifSecondaire = self._positionArrivee
        else:
            objectif = self._positionArrivee
            objectifSecondaire = self._positionArrivee

        # ma route
        capNextWaypoint, distanceNextWaypoint = cPosition.positionementRelatif(positionCourante, objectif)


        # ai je atteint le waypoint
        if distanceNextWaypoint.val < 0.5:
            objectif = objectifSecondaire
            self._positionWaypointsAtteint = self._positionWaypointsAtteint + 1

        # ma nouvelle route
        capNextWaypoint, distanceNextWaypoint = cPosition.positionementRelatif(positionCourante, objectif)

        # facteur perturbant
        facteur = (self._heure.timestamp() - self._heuredepart.timestamp()) * cAngle.DEG2RAD
        
        # deplacement
        self._vitesse.val = self._vitesseMoyenne.val * (1 + 0.25 * cos(facteur))
        self._vitesse.dir = capNextWaypoint * (1.0 + 0.25 * cos(facteur))

        capRad = self._vitesse.dir.valAsRad
        latitudeEstimeeRad = self._positionCourante.latitude.valAsRad

        pasEnLatitude = cos (capRad) * self._vitesse.val / 60 # noeud = mille/h - 1 mille = 1 minute d'arc
        pasEnLongitude = sin (capRad) * self._vitesse.val / (60 * cos (latitudeEstimeeRad))
        
        self._positionCourante.latitude += cAngle(valAsDeg=(pasEnLatitude * intervalDeNavEnSec / 3600))
        self._positionCourante.longitude += cAngle(valAsDeg=(pasEnLongitude * intervalDeNavEnSec / 3600))

        # vent
        self._ventReel.val = self._ventReelDepart.val * (1 + cos(facteur))
        self._ventReel.dir = cCap(self._ventReelDepart.dir.valAsDeg + (180 * cos(facteur)))

        ventAppX = (self._ventReel.val * cos(self._ventReel.dir.valAsRad) - self._vitesse.val * cos(self._vitesse.dir.valAsRad))
        ventAppY = (self._ventReel.val * sin(self._ventReel.dir.valAsRad) - self._vitesse.val * sin(self._vitesse.dir.valAsRad))
        ventAppForce = sqrt (ventAppX ** 2 + ventAppY **2)
        ventAppDir : cAngle = cAngle(valAsRad=(atan2 (ventAppY, ventAppX)))
        
        self._ventApp.val = ventAppForce
        self._ventApp.dir = cCap(valAsDeg=ventAppDir.valAsDeg)
        self._temperatureAir = self._temperatureAirDepart + (5.0 * (cos(facteur)))

        # Eau (profondeur + temp)
        self._profondeur = self._profondeurDepart + (10 * (1+cos(facteur)))
        self._temperatureEau = self._temperatureEauDepart + (2.0 * (cos(facteur)))

        self._courant.val = self._courantDepart.val * (1 + cos(facteur))
        self._courant.dir.valAsDeg = self._courantDepart.dir.valAsDeg + (180 * cos(facteur))

        if self._logger.isEnabledFor(level = logging.DEBUG):
            self._logger.debug(self.toString())

        return
        

