import copy
from datetime import datetime, timezone
import logging
from math import cos, sin, sqrt, atan2

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

from pSfaNmea.mNmea0183Lib import nmea0183lib



class cSimulateurNav:
    _logger : logging.Logger = getLogger("simulateurNav", logging.DEBUG)

    def __init__(self, timestamp : float):
        self._heuredepart : float = timestamp
        self._heure : float = timestamp

    def updateVecteurEtat(self, w : cVecteurEtat, t : cTrajet) -> cVecteurEtat:    
        v : cVecteurEtat = copy.deepcopy(w)   

        # quel est le prochain point de la trajectoire a rejoindre a partir de la position courante
        c : cCap = t.getTrajectoireInfo (v.bateau)

        # facteur perturbant
        nombre = (2.0 * random.random()) - 1.0
        facteur = nombre
        
        
        # deplacement
        v.bateau.sog.vitesse.asNoeud = v.bateau.sog.vitesse.asNoeud * (1 + facteur / 10.0)
        v.bateau.sog.sens.capAsDeg = c.capAsDeg + (facteur * 5.0)

        # vent
        v.air.vent.vitesse.asNoeud = v.air.vent.vitesse.asNoeud * (1 + facteur / 10.0)
        v.air.vent.sens.capAsDeg = v.air.vent.sens.capAsDeg + (facteur * 5.0)
        v.air.derive.angleAsDeg = v.air.derive.angleAsDeg + (facteur * 5.0)
        v.air.temperature = v.air.temperature + (1 + facteur * 5.0)
        
        """
        ventAppX = (self._ventReel.val * cos(self._ventReel.dir.valAsRad) - self._vitesse.val * cos(self._vitesse.dir.valAsRad))
        ventAppY = (self._ventReel.val * sin(self._ventReel.dir.valAsRad) - self._vitesse.val * sin(self._vitesse.dir.valAsRad))
        ventAppForce = sqrt (ventAppX ** 2 + ventAppY **2)
        ventAppDir : cAngle = cAngle(valAsRad=(atan2 (ventAppY, ventAppX)))
        """     

        # Eau (profondeur + temp)
        v.eau.courant.vitesse.asNoeud = v.eau.courant.vitesse.asNoeud *(1 + facteur / 10.0)
        v.eau.courant.sens.capAsDeg = v.eau.courant.sens.capAsDeg  +(facteur * 5.0)
        v.eau.temperature = v.eau.temperature + (1 + 2.0 * facteur)
        v.eau.profondeur = v.eau.profondeur + (1 + 15.0 * (1 + facteur / 10.0))

        return v


    def nav(self, v : cVecteurEtat, t: cTrajet) -> Tuple[cVecteurEtat, List[bytes]] :
        # maintenant
        timestamp = datetime.now(tz=timezone.utc).timestamp()

        # updateVecteurEtat a l'heure courante
        # le long de la trajectoire demandee
        v = self.updateVecteurEtat(v, t)

        # calcul de la durre de la nav en secondes
        intervalDeNavEnSec = (timestamp - self._heure) 
        self._heure = timestamp

        # navigation avec ce sinfos
        b : cNavigationBateau = cNavigationBateau (etat = v, trajet= t)        
        newPosition : cPosition = b.navigate (dT=intervalDeNavEnSec)
        v.bateau.position = newPosition 

        n : nmea0183lib = nmea0183lib ()
        return (v, n.computeTrames(nav=v, heure=datetime.fromtimestamp(timestamp, tz=timezone.utc)))

        
        

