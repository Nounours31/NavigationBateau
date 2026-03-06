from __future__ import annotations
from typing import List

from .mCap import cCap
from .mVelocite import cVelocite
from .mDistance import cDistance
from .mNavigationFormules import cNavigationFormules 
from .mPosition import cPosition
from .mVecteurEtat import cVecteurEtat, cVecteurEtatKeys, cTrajet



class cNavigationBateau:
    DISTANCE_JE_SUIS_ARRIVE : float = 0.1

    def __init__(self, etat: cVecteurEtat, trajet: cTrajet):
        self._etat = etat
        self._trajet = trajet
    
    @property
    def positionCourante(self) -> cPosition:
        return self._etat.bateau.position

    @positionCourante.setter
    def positionCourante(self, p : cPosition) -> None:
         self._etat.bateau.position = p


    @property
    def vitesse(self) -> cVelocite:
        return self._etat.bateau.sog

    @property
    def arrivee(self) -> cPosition:
        return self._trajet.arrivee

    @property
    def WptWithDepartArrivee(self) -> List[cPosition]:
        retour : List[cPosition] = []
        retour.append(self._trajet.depart)
        for x in self._trajet.waypoints:
            retour.append(x)
        retour.append(self._trajet.arrivee)
        return retour

    @property
    def Wpt(self) -> List[cPosition]:
        retour : List[cPosition] = []
        for x in self._trajet.waypoints:
            retour.append(x)
        return retour
    
    def navigate (self, dT : float) -> None :
        # recherche du waypoint le plus proche
        p : cPosition = self.positionCourante
        wpt : List[cPosition] = self.Wpt

        lePlusProche : cPosition
        cap : cCap
        minDist : cDistance = cDistance(valAsMilleNautique=60000.0)
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
            newWPT = self.arrivee
            print ("Navigation finie")
            return
        
        # navigate de position courante vers new position
        v : cVelocite = self.vitesse
        nav: cNavigationFormules = cNavigationFormules(position=p)
        NewPosition : cPosition = nav.navACapVitesseCourantVentDonnes(tempsDeNavEnSeconde=dT, 
                                                                      v = v, 
                                                                      courant = self._etat.eau.courant,
                                                                      vent = self._etat.air.vent,
                                                                      derive = self._etat.air.derive)
        self.positionCourante = NewPosition


    def toString (self) -> str :
        retour : str = f"{self._etat.toString()}"
        return retour