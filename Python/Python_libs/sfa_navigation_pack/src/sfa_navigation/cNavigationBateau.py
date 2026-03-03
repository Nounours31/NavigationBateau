from __future__ import annotations
from typing import List

from . import cVelocite
from . import cCap, cDistance
from . import cNavigationFormules
from . import cPosition
from . import cVecteurEtat
from .cVecteurEtat import cTrajet



class cNavigationBateau:
    DISTANCE_JE_SUIS_ARRIVE : float = 0.1

    def __init__(self, etat: cVecteurEtat, trajet: cTrajet):
        self._etat = etat
        self._trajet = trajet
    
    @property
    def positionCourante(self) -> cPosition:
        return self._etat.cBateau.position

    @property
    def allWPT(self) -> List[cPosition]:
        retour : List[cPosition] = []
        retour.append(self._trajet.depart)
        for x in self._trajet.waypoints:
            retour.append(x)
        retour.append(self._trajet.arrivee)
        return retour
    
    def navigate (self, dT : float) -> None :
        # recherche du waypoint le plus proche
        p : cPosition = self.positionCourante
        wpt : List[cPosition] = self.allWPT

        lePlusProche : cPosition
        cap : cCap
        minDist : cDistance = cDistance(valAsMilleNautique=0.0)
        dist : cDistance = cDistance(valAsMilleNautique=0.0)
        newWPT : cPosition
        trouve : bool = False
        for x in wpt:
            (cap, dist) = cNavigationFormules(position=p).routeLoxodromique(arrivee=x)
            if dist <= minDist and dist.asMn > cNavigationBateau.DISTANCE_JE_SUIS_ARRIVE:
                minDist = dist
                newWPT = x
                trouve = True
        
        if not trouve:
            print ("Navigation finie")
            return
        
        # navigate de position courante vers new position
        v : cVelocite = self.vitesse
        dP: cPosition = v * dT
        NewPosition : cPosition = p + dP
        
        self.positionCourante = NewPosition



    def toString (self) -> str :
        retour : str = f"{self._etat.toString()}"
        return retour