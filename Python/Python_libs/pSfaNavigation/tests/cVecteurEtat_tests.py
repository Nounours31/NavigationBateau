
import datetime
import time

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


# =========================
# Fixtures
# =========================


class cVecteurEtat_tests:
    def test_init(self):
        data: dict[str, object] = {
            # cVecteurEtatKeys.HEURE : datetime.datetime.now(tz=datetime.timezone.utc).timestamp() ,
            cVecteurEtatKeys.HEURE : 1772564924,
            cVecteurEtatKeys.BATEAU : {
                cVecteurEtatKeys.SOG: cVelocite(vitesse=15.0, sens=75),
                cVecteurEtatKeys.POSITION: cPosition.fromDict({cLatitude.NOM: "N 12°", cLongitude.NOM: "W 10°"}),
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
            cVecteurEtatKeys.SATELLITE : {}
        }

        v: cVecteurEtat = cVecteurEtat.fromDict(data)
        ref : str = "[vecteurEtat heure=03/03/26 19:08:44.000000 bateau=[bateau sog=15.000Kt 075.0000°,position=N 012.0000°, W 010.0000°, varMagnetique=358.8000°]" + \
            " air=[air temperature=20.0 vent=15.000Kt 075.0000° derive=002.5000°] eau=[eau profondeur=17.0 temperature=12.0 courant=15.000Kt 075.0000°]]"

        print(v)
        assert v.toString() == ref

    def test_init2(self):
        trajet: dict[str, object] = {
            cVecteurEtatKeys.DEPART : cPosition.fromDict({cLatitude.NOM: "N 12°", cLongitude.NOM: "W 10°"}),
            cVecteurEtatKeys.ARRIVEE: cPosition.fromDict({cLatitude.NOM: "N 15°", cLongitude.NOM: "W 10°"}),
            cVecteurEtatKeys.WAYPOINTS : [
                cPosition.fromDict({cLatitude.NOM: "N 13°", cLongitude.NOM: "W 10°"}),
                cPosition.fromDict({cLatitude.NOM: "N 14°", cLongitude.NOM: "W 10°"})
            ]
        }

        v: cTrajet = cTrajet.fromDict(trajet)
        ref : str = "[Trajet depart=N 012.0000°, W 010.0000°, arrivee=N 015.0000°, W 010.0000°, wpt=[[cPosition: N 013.0000°, W 010.0000°], [cPosition: N 014.0000°, W 010.0000°]]]"
        print(v)
        assert v.toString() == ref

    def test_nav(self):
        depart : cPosition = cPosition.fromDict({cLatitude.NOM: "N 12°", cLongitude.NOM: "W 10°"})
        arrivee : cPosition = cPosition.fromDict({cLatitude.NOM: "N 15°", cLongitude.NOM: "W 10°"})
        trajet: dict[str, object] = {
            cVecteurEtatKeys.DEPART : depart,
            cVecteurEtatKeys.ARRIVEE: arrivee,
            cVecteurEtatKeys.WAYPOINTS : [
                cPosition.fromDict({cLatitude.NOM: "N 13°", cLongitude.NOM: "W 10°"}),
                cPosition.fromDict({cLatitude.NOM: "N 14°", cLongitude.NOM: "W 10°"})
            ]
        }

        data: dict[str, object] = {
            # cVecteurEtatKeys.HEURE : datetime.datetime.now(tz=datetime.timezone.utc).timestamp() ,
            cVecteurEtatKeys.HEURE : 1772564924,
            cVecteurEtatKeys.BATEAU : {
                cVecteurEtatKeys.SOG: cVelocite(vitesse=15.0, sens=75),
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
            cVecteurEtatKeys.SATELLITE : {}
        }
        
        v: cVecteurEtat = cVecteurEtat.fromDict(data)
        t: cTrajet = cTrajet.fromDict(trajet)

        b : cNavigationBateau = cNavigationBateau (etat = v, trajet= t)
        now : float = datetime.datetime.now(tz=datetime.timezone.utc).timestamp()
        theEnd : float = now + 100
        dixSecondes  : float = 10  
        
        # time.sleep(dixSecondes)
        currentTime : float = datetime.datetime.now(tz=datetime.timezone.utc).timestamp()
        dT = currentTime - now
        b.navigate (dT)
        print(b.toString())
        now = currentTime



