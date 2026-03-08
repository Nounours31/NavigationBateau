from __future__ import annotations
from enum import Enum
from typing import List, Tuple

from .mCap import cCap
from .mVelocite import cVelocite
from .mDistance import cDistance
from .mNavigationFormules import cNavigationFormules 
from .mPosition import cPosition
from .mVecteurEtat import cVecteurEtat, cVecteurEtatKeys, cTrajet, cWayPoint

import numpy as np
import math

RAYON_CERCLE_WPT_EN_MILLE_NAUTIQUE : float = 0.1

class eModePassageDesWPT(Enum):
    CERCLE = 1
    PERPENDICULAIRE = 2
    CERCLE_ET_PERPENDICULAIRE = 3


class cNavigationBateau:
    def __init__(self, etat: cVecteurEtat, trajet: cTrajet):
        self._etat : cVecteurEtat  = etat
        self._trajet : cTrajet = trajet
        self._WptDejaPasses : List[cWayPoint] = []
    
    @property
    def etat(self) -> cVecteurEtat:
        return self._etat
    
    @etat.setter
    def etat(self, v : cVecteurEtat) -> None:
        self._etat = v  


    @property
    def positionCourante(self) -> cPosition:
        return self._etat.bateau.position

    @positionCourante.setter
    def positionCourante(self, p : cPosition) -> None:
         self._etat.bateau.position = p


    @property
    def vitesse(self) -> cVelocite:
        return self._etat.bateau.sog

    
    def toString (self) -> str :
        retour : str = f"{self._etat.toString()}"
        return retour
        
    def navigate (self, dT : float) -> cPosition :
        # recherche du waypoint le plus proche
        p : cPosition = self.positionCourante

        # je cherche le waypoint le plus adapte ma position / trajectoire
        trouve : bool = False
        NewWpt : cWayPoint 
        NewPosition : cPosition 
        (NewWpt, trouve) = self.getProchainWayPoint(p)
        NewPosition = NewWpt.position
        
        if not trouve:
            NewPosition = self._trajet.arrivee.position
            print ("Navigation finie")
        else: 
            
            # navigate de position courante vers new position
            v : cVelocite = self.vitesse
            nav: cNavigationFormules = cNavigationFormules(position=p)
            NewPosition  = nav.navACapVitesseCourantVentDonnes(tempsDeNavEnSeconde=dT, 
                                                                        v = v, 
                                                                        courant = self._etat.eau.courant,
                                                                        vent = self._etat.air.vent,
                                                                        derive = self._etat.air.derive)
        return NewPosition


    # je trie selon l'ordre de passge PAS la proximitée
    @staticmethod
    def quick_sort(arr : List[Tuple[cWayPoint, cCap, cDistance]]) -> List[Tuple[cWayPoint, cCap, cDistance]]:
        if len(arr) <= 1:
            return arr
        (wpt, c, d) = arr[0]
        pivot : int = wpt.ordreDePassage
        
        left : List[Tuple[cWayPoint, cCap, cDistance]] = [(wpt, c, d) for (wpt, c, d) in arr[1:] if wpt.ordreDePassage < pivot]
        right: List[Tuple[cWayPoint, cCap, cDistance]]= [(wpt, c, d) for (wpt, c, d) in arr[1:] if wpt.ordreDePassage >= pivot]
        return cNavigationBateau.quick_sort(left) + [(wpt, c, d)] + cNavigationBateau.quick_sort(right)


    def getProchainWayPoint(self, positionCourante: cPosition, strategiePassageDesWpt: eModePassageDesWPT = eModePassageDesWPT.CERCLE) -> Tuple[cWayPoint, bool]:
        # quel est le prochain point de la trajectoire a rejoindre a partir de la position courante
        # en fonction de la tolerance choisie
        navCalcul : cNavigationFormules = cNavigationFormules(positionCourante)

        # recup de tous les WPT
        allWPT : List[cWayPoint] = []    
        allWPT.append(self._trajet.depart)
        for p in self._trajet.waypoints:
            allWPT.append(p)
        allWPT.append(self._trajet.arrivee)


        # les WPT a parcourir et la distance au point courant
        allWptTries : List[Tuple[cWayPoint, cCap, cDistance]] = []
        for wpt in allWPT:
            if wpt not in self._WptDejaPasses: 
                d : cDistance 
                c : cCap
                (c, d) = navCalcul.routeLoxodromique(wpt.position)
                allWptTries.append((wpt, c, d))

        allWptTries = cNavigationBateau.quick_sort(allWptTries) # je trie selon l'ordre de passage PAS la proximitée


        # je dois aller vers le WPT tant que la distance n'est pas < RAYON_CERCLE_WPT_EN_MILLE_NAUTIQUE
        if strategiePassageDesWpt == eModePassageDesWPT.CERCLE:
            allWptPossible :  List[Tuple[cWayPoint, cCap, cDistance]] = []
            for (wpt, c, d) in allWptTries:
                if d > RAYON_CERCLE_WPT_EN_MILLE_NAUTIQUE:
                    allWptPossible.append((wpt, c, d))  
                else:
                    self._WptDejaPasses.append(wpt) # je marque le WPT comme deja passe                    
                    

            if len(allWptPossible) > 0:
                return (allWptPossible[0][0], True) 
            else:
                return (cWayPoint(self._trajet.arrivee.position, len(self._trajet.waypoints) + 1), False)


        # je dois aller vers le WPT devant moi 
        if strategiePassageDesWpt == eModePassageDesWPT.PERPENDICULAIRE:
            allWptPossible :  List[Tuple[cWayPoint, cCap, cDistance]] = []
            c : cCap
            d : cDistance
            for (wpt, c, d) in allWptTries:
                v1 = np.array([math.sin(c.capAsRad), math.cos(c.capAsRad)])
                v2 = np.array([math.sin(self.etat.bateau.sog.sens.capAsRad), math.cos(self.etat.bateau.sog.sens.capAsRad)])
                if np.dot(v1, v2) > 0:
                    allWptPossible.append((wpt, c, d))    
                else:
                    self._WptDejaPasses.append(wpt) # je marque le WPT comme deja passe                    

            if len(allWptPossible) > 0:
                return (allWptPossible[0][0], True) 
            else:
                return (cWayPoint(self._trajet.arrivee.position, len(self._trajet.waypoints) + 1), False)

        # je dois aller vers le WPT devant moi 
        if strategiePassageDesWpt == eModePassageDesWPT.CERCLE_ET_PERPENDICULAIRE:
            allWptPossible :  List[Tuple[cWayPoint, cCap, cDistance]] = []
            c : cCap
            d : cDistance
            for (wpt, c, d) in allWptTries:
                v1 = np.array([math.sin(c.capAsRad), math.cos(c.capAsRad)])
                v2 = np.array([math.sin(self.etat.bateau.sog.sens.capAsRad), math.cos(self.etat.bateau.sog.sens.capAsRad)])
                if np.dot(v1, v2) <= 0 and d <= RAYON_CERCLE_WPT_EN_MILLE_NAUTIQUE:
                    self._WptDejaPasses.append(wpt) # je marque le WPT comme deja passe                    
                else:
                    allWptPossible.append((wpt, c, d))    

            if len(allWptPossible) > 0:
                return (allWptPossible[0][0], True) 
            else:
                return (cWayPoint(self._trajet.arrivee.position, len(self._trajet.waypoints) + 1), False)

        
        # aucune solution - je vise l'arrivee
        return (cWayPoint(self._trajet.arrivee.position, len(self._trajet.waypoints) + 1), False)    
    