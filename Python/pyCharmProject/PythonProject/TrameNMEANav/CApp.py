import calendar
import time
from datetime import datetime, timezone
from logging import Logger
from math import cos
from time import sleep

from TrameNMEANav.CAngle import CAngle
from TrameNMEANav.CLatitude import CLatitude
from TrameNMEANav.CLogger import CLogger
from TrameNMEANav.CLongitude import CLongitude
from TrameNMEANav.CNMEA import CNMEA
from TrameNMEANav.CPosition import CPosition
from TrameNMEANav.CSocketNetWorkForNMEA import CSocketNetWorkForNMEA
from TrameNMEANav.CNavigation import CNavigation


class CApp:

    intervalMesureNMEAEnSecondes = 2
    vitesseMoyenneEnNoeud = 5.0
    variationMagnetiqueEnDegre = -1.2

    MAX_TIME_IN_SECONDES = 3600
    DEBUG_NAV = True
    DEBUG_NMEA = False

    @staticmethod
    def SAOMA() -> CPosition:
        return CPosition(lat=CLatitude(-14.2456), lon=CLongitude(-169.6100))


    @staticmethod
    def SAINT_QUAY() -> CPosition:
        # Port de St Quay
        return CPosition(lat=CLatitude(48.649665), lon=CLongitude(-2.813217))


    @staticmethod
    def SAINT_HELIER() -> CPosition:
        # Port de St helier
        return CPosition(lat=CLatitude(49.17911688235086), lon=CLongitude(-2.113543749854877))


    @staticmethod
    def IleViergesUS() -> CPosition:
        return CPosition(lat=CLatitude(17.726768560744713), lon=CLongitude(-64.57080286354567))

    @staticmethod
    def PuertoRico() -> CPosition:
        return CPosition(lat=CLatitude(17.90563819856933), lon=CLongitude(-67.19948124361858))


    @staticmethod
    def navInfo() -> (float, float, float):
        return CApp.intervalMesureNMEAEnSecondes, CApp.vitesseMoyenneEnNoeud, CApp.variationMagnetiqueEnDegre

    def __init__(self):
        self.myNMEANetwork: CSocketNetWorkForNMEA = CSocketNetWorkForNMEA()
        self._logger: Logger = CLogger.getLogger("CApp")

    def close(self):
        self.myNMEANetwork.close()

    def start(self):
        self.myNMEANetwork.openSocket()

        # Info de la nav
        positionDepart : CPosition = CApp.IleViergesUS()
        positionArrivee : CPosition =CApp.PuertoRico()
        position = positionDepart
        intervalMesureNMEAEnSecondes, vitesseMoyenneEnNoeud, variationMagnetiqueEnDegre = CApp.navInfo()

        # Outils de Nav
        nmeaTool: CNMEA = CNMEA("x")
        cnav = CNavigation()

        heureDepart = time.time()
        ts = 0
        while True:
            # on demarre la nav ...
            sleep(intervalMesureNMEAEnSecondes)

            # maintenant
            now = time.time()
            tsPrevious = ts
            ts = now - heureDepart
            nbSecondesDeNav = ts - tsPrevious

            # distance et cap vers l'arrivee
            distance, bearing = cnav.navLoxodromique(position, positionArrivee)
            cap = bearing + 10.0 * cos((10 * nbSecondesDeNav) * CAngle.DEG2RAD)  # +/- 10 degre
            vitesseEnNoeud = vitesseMoyenneEnNoeud * (1  +  0.1 * cos((10 * nbSecondesDeNav) * CAngle.DEG2RAD))  # +/- 10%


            # j'avance
            prevPosition = position
            position = cnav.nav(nbSecondesDeNav, vitesseEnNoeud, cap, prevPosition)
            if CApp.DEBUG_NAV:
                debugDParcourueReel, debugCapReel = cnav.navLoxodromique(prevPosition, position)
                self._logger.info(f"SAUT\n\tPosition avant:{prevPosition:s}\n\tPosition apres: {position:s}\n\t\tcap: {debugCapReel:03.6f}° Distance parcourue: {debugDParcourueReel:03.6f}Mn - Vitesse: {(debugDParcourueReel/ (nbSecondesDeNav / 3600)):03.6f}Mn")

                debugDistanceParcourue, debugCapParcouru = cnav.navLoxodromique(positionDepart, position)
                debugVitesse = debugDistanceParcourue / (ts / 3600)
                self._logger.info(f"STAT DE NAV")
                self._logger.info(f"\tDuree nav:{ts:5.8f}s \n\tcap moyen: {debugCapParcouru:3.6f}° - vitesse moyenne: {debugVitesse:03.6f}Nd")
                self._logger.info(f"\tdistance a parcourir:{distance:03.6f}Mn \n\tdistance a parcourue:{debugDistanceParcourue:03.6f}Mn  - a la vitesse {debugVitesse:03.6f}Nd")

                self._logger.debug("------------------------------------------------------------------------------------------")


            # info NMEA de ma position
            gga = nmeaTool.getGGA(now, position)
            rmc = nmeaTool.getRMC(now, position, vitesseEnNoeud, cap)
            vtg = nmeaTool.getVTG(vitesseEnNoeud, cap)
            gsa = nmeaTool.getGSA()
            gsv = nmeaTool.getGSV()
            depth = nmeaTool.getDepth(nbSecondesDeNav)
            hdg = nmeaTool.getHDG(cap, variationMagnetiqueEnDegre)
            trueWind = nmeaTool.getWindInfo("True", 150.0, 12.2)
            relativeWind = nmeaTool.getWindInfo("Relative", 150.0, 12.2)

            bwr = nmeaTool.getWayPointInfoBWR(now=now,
                                              positionWayPoint=positionArrivee,
                                              bearing=bearing,
                                              variation=variationMagnetiqueEnDegre,
                                              distance=distance,
                                              uid="WP1")
            bwc = nmeaTool.getWayPointInfoBWC(now=now,
                                              positionWayPoint=positionArrivee,
                                              bearing=bearing,
                                              variation=variationMagnetiqueEnDegre,
                                              distance=distance,
                                              uid="WP1")
            if CApp.DEBUG_NMEA:
                self._logger.debug(gga)
                self._logger.debug(gsv)
                self._logger.debug(gsa)
                self._logger.debug(vtg)
                self._logger.debug(rmc)
                self._logger.debug(depth)
                self._logger.debug(hdg)
                self._logger.debug(trueWind)
                self._logger.debug(relativeWind)
                self._logger.debug(bwr)
                self._logger.debug(bwc)
                self._logger.debug("------------------------------------------------------------------------------------------")

            self.myNMEANetwork.sendto(gga)
            for x in gsv:
                self.myNMEANetwork.sendto(x)

            self.myNMEANetwork.sendto(gsa)
            self.myNMEANetwork.sendto(vtg)
            self.myNMEANetwork.sendto(rmc)
            self.myNMEANetwork.sendto(depth)
            self.myNMEANetwork.sendto(hdg)
            self.myNMEANetwork.sendto(relativeWind)
            self.myNMEANetwork.sendto(trueWind)
            self.myNMEANetwork.sendto(bwr)
            self.myNMEANetwork.sendto(bwc)

            if nbSecondesDeNav > CApp.MAX_TIME_IN_SECONDES:
                self.myNMEANetwork.close()
                break
