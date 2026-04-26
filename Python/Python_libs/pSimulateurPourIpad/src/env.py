from pSfaTools.mMyException import cMyException

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

from datetime import datetime, timezone

sleepTimeInSec=2

# Port de la trinitee
latTrinitee = 47.565102  # angleSexaToDecimal(degre = 2, minute = 56.23)
longTrinitee = -3.011115  # angleSexaToDecimal(degre = 2, minute = 56.23)
postionTrinitee: cPosition = cPosition.fromString(f"{latTrinitee:.6f},{longTrinitee:.6f}")

# La teignouse
latTeignouse = 47.457243477695584
longTeignouse = -3.0455449857339927  # angleSexaToDecimal(degre = 2, minute = 56.23)
postionTeignouse: cPosition = cPosition.fromString(f"{latTeignouse:.6f},{longTeignouse:.6f}")

# Buisson de Meaban
latMeaban = 47.52807297570114
longMeaban = -2.9486584578059367  # angleSexaToDecimal(degre = 2, minute = 56.23)
positionMeaban: cPosition = cPosition.fromString(f"{latMeaban:.6f},{longMeaban:.6f}")

# Port de St Quay
latPortStQuay = 48.649665  # angleSexaToDecimal(degre = 2, minute = 56.23)
longPortStQuay = -2.813217  # angleSexaToDecimal(degre = 2, minute = 56.23)
postionStQuay: cPosition = cPosition.fromString(f"{latPortStQuay:.6f},{longPortStQuay:.6f}")

# Port de
latSamoa = -14.2456  # angleSexaToDecimal(degre = 2, minute = 56.23)
longSamoa = -169.6100  # angleSexaToDecimal(degre = 2, minute = 56.23)

depart : cPosition = postionTrinitee
arrivee : cPosition = postionTeignouse

trajet: dict[str, object] = {
    cVecteurEtatKeys.DEPART : depart,
    cVecteurEtatKeys.ARRIVEE: arrivee,
    cVecteurEtatKeys.WAYPOINTS : [
        positionMeaban,
    ]
}

data: dict[str, object] = {
    cVecteurEtatKeys.HEURE : datetime.now(tz=timezone.utc).timestamp() ,
    # cVecteurEtatKeys.HEURE : 1772564924,
    cVecteurEtatKeys.BATEAU : {
        cVecteurEtatKeys.SOG: cVelocite(vitesse=5.0, sens=75),
        cVecteurEtatKeys.POSITION: depart,
        cVecteurEtatKeys.VARIATION_MAGNETIQUE: cCap(valAsDeg=-1.2),
    },
    cVecteurEtatKeys.EAU : {
        cVecteurEtatKeys.COURANT: cVelocite(vitesse=15.0, sens=75),
        cVecteurEtatKeys.PROFONDEUR: 17,
        cVecteurEtatKeys.TEMPERATURE: 12,
    },
    cVecteurEtatKeys.AIR : {
        cVecteurEtatKeys.VENT: cVelocite(vitesse=15.0, sens=75),
        cVecteurEtatKeys.TEMPERATURE: 20,
        cVecteurEtatKeys.DERIVE: cAngle(valAsDeg=2.5),
    },
    cVecteurEtatKeys.SATELLITE : {},
}

v: cVecteurEtat = cVecteurEtat.fromDict(data)
t: cTrajet = cTrajet.fromDict(trajet)
