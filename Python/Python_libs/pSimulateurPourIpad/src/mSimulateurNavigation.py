import copy
import logging
from math import cos, sin, sqrt, atan2

from typing import Dict, Any, List

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

    def updateVecteurEtat(self, w : cVecteurEtat) -> cVecteurEtat:    
        v : cVecteurEtat = copy.deepcopy(w)   
        positionCourante : cPosition = v.bateau.position
        objectif : cPosition = None
        objectifSecondaire : cPosition = None

        # facteur perturbant
        facteur = (self._heure - self._heuredepart) * cAngle.DEG2RAD
        
        # deplacement
        v.bateau.sog.vitesse.asNoeud = v.bateau.sog.vitesse.asNoeud * (1 + 0.25 * cos(facteur))
        v.bateau.sog.sens.capAsDeg = v.bateau.sog.sens.capAsDeg * (1.0 + 0.25 * cos(facteur))

        # vent
        v.air.vent.vitesse.asNoeud = v.air.vent.vitesse.asNoeud * (1 + cos(facteur))
        v.air.vent.sens.capAsDeg = v.air.vent.sens.capAsDeg * (1 + cos(facteur))
        v.air.derive.angleAsDeg = v.air.derive.angleAsDeg * (1 + cos(facteur))
        v.air.temperature = v.air.temperature + (5.0 * (cos(facteur)))
        
        """
        ventAppX = (self._ventReel.val * cos(self._ventReel.dir.valAsRad) - self._vitesse.val * cos(self._vitesse.dir.valAsRad))
        ventAppY = (self._ventReel.val * sin(self._ventReel.dir.valAsRad) - self._vitesse.val * sin(self._vitesse.dir.valAsRad))
        ventAppForce = sqrt (ventAppX ** 2 + ventAppY **2)
        ventAppDir : cAngle = cAngle(valAsRad=(atan2 (ventAppY, ventAppX)))
        """     

        # Eau (profondeur + temp)
        v.eau.courant.vitesse.asNoeud = v.eau.courant.vitesse.asNoeud * (1 + cos(facteur))
        v.eau.courant.sens.capAsDeg = v.eau.courant.sens.capAsDeg * (1 + cos(facteur))
        v.eau.temperature = v.eau.temperature + (2.0 * (cos(facteur)))
        v.eau.profondeur = v.eau.profondeur + (15.0 * (1+cos(facteur)))

        return v


    def nav(self, timestamp : float, v : cVecteurEtat, t: cTrajet) -> List[bytes] :
        # updateVecteurEtat a l'heure courante
        v = self.updateVecteurEtat(v)

        # calcul de la durre de la nav en secondes
        intervalDeNavEnSec = (timestamp - self._heure) / 1000.0
        self._heure = timestamp

        # navigation
        b : cNavigationBateau = cNavigationBateau (etat = v, trajet= t)        
        b.navigate (dT=intervalDeNavEnSec)

        n : nmea0183lib = nmea0183lib ()
        return n.computeTrames(nav=v, heure=timestamp)

        
        

