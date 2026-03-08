from __future__ import annotations
from typing import List

from .mCap import cCap
from .mVelocite import cVelocite
from .mDistance import cDistance
from .mNavigationFormules import cNavigationFormules 
from .mPosition import cPosition
from .mVecteurEtat import cVecteurEtat, cVecteurEtatKeys, cTrajet


RAYON_CERCLE_WPT_EN_MILLE_NAUTIQUE : float = 0.1

class eModePassageDesWPT(Enum):
    CERCLE = 1
    PERPENDICULAIRE = 2
    CERCLE_ET_PERPENDICULAIRE = 3

class 

class cNavigationBateau:
    def __init__(self, etat: cVecteurEtat, trajet: cTrajet):
        self._etat : cVecteurEtat  = etat
        self._trajet : cTrajet = trajet
        self._WptDejaPasses : List[cPosition] = []
    
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
    
    def toString (self) -> str :
        retour : str = f"{self._etat.toString()}"
        return retour
        
    def navigate (self, dT : float) -> cPosition :
        # recherche du waypoint le plus proche
        p : cPosition = self.positionCourante

        # je cherche le waypoint le plus adapte ma position / trajectoire
        trouve : bool = False
        NewPosition : cPosition 
        (NewPosition, trouve) = self.getProchainWayPoint(self, p)
        
        if not trouve:
            NewPosition = self.arrivee
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


    @staticmethod
    def quick_sort(arr : List[(cPosition, cDistance)]) -> List[(cPosition, cDistance)]:
        if len(arr) <= 1:
            return arr
        (pivot, dpivot) = arr[0]
        left = [(x, y) for (x, y) in arr[1:] if y < dpivot]
        right = [(x, y) for (x, y) in arr[1:] if y >= dpivot]
        return cTrajet.quick_sort(left) + [(pivot, dpivot)] + cTrajet.quick_sort(right)


    def getProchainWayPoint(self, positionCourante: cPosition, strategiePassageDesWpt: eModePassageDesWPT) -> cPosition:
        # quel est le prochain point de la trajectoire a rejoindre a partir de la position courante
        # en fonction de la tolerance choisie
        
        # recup de tous les WPT
        allWPT : List[cPosition] = []    
        allWPT.append(self.depart)
        for p in self.waypoints:
            allWPT.append(p)
        allWPT.append(self.arrivee)


        # classement des WPT par distance au Pt courant
        allWptTries : List[cPosition] = cTrajet.quick_sort(allWPT)

        # je dois aller vers le WPT tant que la distance n'est pas < RAYON_CERCLE_WPT_EN_MILLE_NAUTIQUE
        if strategiePassageDesWpt == eModePassageDesWPT.CERCLE:
            for p in allWPT:
                d : cDistance = cNavigationFormules.distance(positionCourante, p)
                if d.asMilleNautique < RAYON_CERCLE_WPT_EN_MILLE_NAUTIQUE:
                    return p

        # liste des points devant moi et pas trop pret  
        x : cNavigationFormules = cNavigationFormules(positionCourante)
        c : cCap
        d : cDistance
        wptDevant : List[(cPosition, cDistance)] = []    
        for p in allWPT:
            (c, d) = x.routeLoxodromique(arrivee=p)
            if d > 0.1:
                v1 = np.array([math.sin(c.capAsRad), math.cos(c.capAsRad)])
                v2 = np.array([math.sin(b.sog.sens.capAsRad), math.cos(b.sog.sens.capAsRad)])
                if np.dot(v1, v2) > 0:
                    wptDevant.append((p, d)) 
        
        # aucune solution - je vise l'arrivee
        wptDevantTrie : List[(cPosition, cDistance)] = cTrajet.quick_sort(wptDevant)
        if len(wptDevantTrie) == 0 :
            (c, d) = x.routeLoxodromique(arrivee=self.arrivee)
            return c
        
        # 1 solution - j'y vais
        if len(wptDevantTrie) == 1 :
            (c, d) = x.routeLoxodromique(arrivee=wptDevantTrie[0][0])
            return c
        

        # plusieur solution - prendre la meilleure des 2 premieres
        # je ne garde que les point assez loing (je n'impose pas de passer par le WPT)
        wptDevantTrieUtile : List[(cPosition, cDistance)] = []    
        for (p, d) in wptDevantTrie:
            if d > 0.5:
                wptDevantTrieUtile.append((p,d))

        # pour les deux premiers prendre l'angle avec la route le plus petit
        if len(wptDevantTrieUtile) == 0 or len(wptDevantTrieUtile) == 1:
            (u, v) = wptDevantTrie[0]
            (c, d) = x.routeLoxodromique(arrivee=u)
            return c
        
        (c1, d1) = x.routeLoxodromique(arrivee=wptDevantTrieUtile[0][0])
        (c2, d2) = x.routeLoxodromique(arrivee=wptDevantTrieUtile[1][0])
        if math.fabs(c1.capAsDeg - b.sog.sens.capAsDeg) < math.fabs(c2.capAsDeg - b.sog.sens.capAsDeg):
            return c1
        else:
            return c2
