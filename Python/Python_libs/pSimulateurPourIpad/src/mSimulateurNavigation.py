import copy
from datetime import datetime, timezone
import logging
from math import cos, sin, sqrt, atan2

import math
import random
from typing import Dict, Any, List, Tuple

from pSfaTools.mMyException import cMyException
from pSfaTools.mLogger import getLogger

from pSfaNavigation.mNavigationFormules import cNavigationFormules, cMethodeCalcul
from pSfaNavigation.mCap import cCap
from pSfaNavigation.mDistance import cDistance
from pSfaNavigation.mVelocite import cVelocite, cVitesse
from pSfaNavigation.mPosition import cPosition
from pSfaNavigation.mLatitude import cLatitude, eLatitudeSens
from pSfaNavigation.mLongitude import cLongitude, eLongitudeSens
from pSfaNavigation.mAngle import cAngle, eAngleFormat
from pSfaNavigation.mVecteurEtat import cVecteurEtat, cVecteurEtatKeys, cTrajet
from pSfaNavigation.mNavigationBateau import cNavigationBateau
from pSfaNavigation.mNavigationFormules import cNavigationFormules


from pSfaNmea.mNmea0183Lib import nmea0183lib



class cSimulateurNav:
    _logger : logging.Logger = getLogger("simulateurNav", logging.DEBUG)

    def __init__(self, timestamp : float, navigationBateau : cNavigationBateau):
        self._heuredepart : float = timestamp
        self._heure : float = timestamp
        self._navigationBateau : cNavigationBateau = navigationBateau


    @property
    def heureDepart(self) -> float:
        return self._heuredepart    
    
    


    def updateVecteurEtat(self, w : cVecteurEtat) -> cVecteurEtat:    
        v : cVecteurEtat = copy.deepcopy(w)   

        # facteur perturbant
        dureeNavEnSec : float = (datetime.now(tz=timezone.utc).timestamp() - self.heureDepart)
        facteur = math.cos(dureeNavEnSec * cAngle.DEG2RAD)         
        
        
        # deplacement
        v.bateau.sog.vitesse.asNoeud = v.bateau.sog.vitesse.asNoeud * (1 + 5 * facteur / 100.0) # 5% de variation de la vitesse
        v.bateau.sog.sens.capAsDeg = v.bateau.sog.sens.capAsDeg * (1 + 5 * facteur / 100.0) # 5% de variation du cap

        # vent
        v.air.vent.vitesse.asNoeud = v.air.vent.vitesse.asNoeud * (1 + 2 * facteur / 100.0)
        v.air.vent.sens.capAsDeg = v.air.vent.sens.capAsDeg * (1 + 2 * facteur / 100.0)
        v.air.derive.angleAsDeg = v.air.derive.angleAsDeg * (1 + 1 * facteur / 100.0)
        v.air.temperature = v.air.temperature * (1 + 5 * facteur / 100.0)
        
        """
        ventAppX = (self._ventReel.val * cos(self._ventReel.dir.valAsRad) - self._vitesse.val * cos(self._vitesse.dir.valAsRad))
        ventAppY = (self._ventReel.val * sin(self._ventReel.dir.valAsRad) - self._vitesse.val * sin(self._vitesse.dir.valAsRad))
        ventAppForce = sqrt (ventAppX ** 2 + ventAppY **2)
        ventAppDir : cAngle = cAngle(valAsRad=(atan2 (ventAppY, ventAppX)))
        """     

        # Eau (profondeur + temp)
        v.eau.courant.vitesse.asNoeud = v.eau.courant.vitesse.asNoeud * (1 + 1 * facteur / 100.0)
        v.eau.courant.sens.capAsDeg = v.eau.courant.sens.capAsDeg  * (1 + 1 * facteur / 100.0)
        v.eau.temperature = v.eau.temperature * (1 + 5 * facteur / 100.0)
        v.eau.profondeur = v.eau.profondeur * (1 + 5 * facteur / 100.0)

        return v


    def evaluateMaintenatNavigation(self) -> List[bytes] :
        # maintenant
        timestamp = datetime.now(tz=timezone.utc).timestamp()

        # calcul de la durre de la nav en secondes
        intervalDeNavEnSec = (timestamp - self._heure) 
        self._heure = timestamp

        # navigation avec ce sinfos
        # attention on ne veut pas tenir compte de la derive du a vent car on veut un cap vers un point, pas un cap subit
        v : cVecteurEtat | None = self._navigationBateau.etat

        deriveAConserver : cAngle = v.air.derive
        courantAConserver : cVelocite = v.eau.courant
        v.air.derive = cAngle(valAsDeg=0) 
        v.eau.courant = cVelocite(vitesse=cVitesse(0), sens=cCap(valAsDeg=0))  

        newPosition : cPosition = self._navigationBateau.navigate(dT=intervalDeNavEnSec)
        v.bateau.position = newPosition 
        v.air.derive = deriveAConserver
        v.eau.courant = courantAConserver

        # je perturbe un peu le VecteurEtat pour simuler une nav plus réaliste et moins linéaire
        # le long de la trajectoire demandee
        v = self.updateVecteurEtat(v)
        NavDeLaPerturbation : cNavigationFormules = cNavigationFormules(position=v.bateau.position) 
        v.bateau.position = NavDeLaPerturbation.navACapEtVitesseDonnes(tempsDeNavEnSeconde = intervalDeNavEnSec, v = v.bateau.sog)

        n : nmea0183lib = nmea0183lib ()
        return n.computeTrames(nav=v, heure=datetime.fromtimestamp(timestamp, tz=timezone.utc))

        
        

