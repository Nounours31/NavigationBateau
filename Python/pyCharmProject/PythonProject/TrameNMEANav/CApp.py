import calendar
import time
from datetime import datetime, timezone
from logging import Logger
from math import cos
from time import sleep

from TrameNMEANav.CLogger import CLogger
from TrameNMEANav.CNMEA import CNMEA
from TrameNMEANav.CPosition import CPosition
from TrameNMEANav.CSocketNetWorkForNMEA import CSocketNetWorkForNMEA
from TrameNMEANav.CNavigation import CNavigation


class CApp:

    MAX_TIME = 3000

    def __init__(self):
        self.myNMEANetwork: CSocketNetWorkForNMEA = CSocketNetWorkForNMEA()
        self._logger: Logger = CLogger.getLogger("CApp")

    def close(self):
        self.myNMEANetwork.close()

    def start(self):
        self.myNMEANetwork.openSocket()

        sleepTimeInSec = 2
        now = datetime.now(tz=timezone.utc)
        vitesseEnNoeudMoyenne = 5.0
        variationMagnetique = -1.2



        positionDepart : CPosition = CPosition.SAINT_QUAY()
        positionArrivee : CPosition = CPosition.SAINT_HELIER()

        position = positionDepart

        nmeaTool: CNMEA = CNMEA("x")
        cnav = CNavigation()

        heureDepart = time.time()
        while True:
            distance, bearing = cnav.navLoxodromique(position, positionArrivee)

            now = time.time()
            nbSecondes = now - heureDepart
            cap = bearing + 5.0 * cos(10 * nbSecondes)  # +/- 5 degre
            vitesseEnNoeud = vitesseEnNoeudMoyenne + (vitesseEnNoeudMoyenne / 10) * cos(100 * nbSecondes)  # +/- 10%
            self._logger.info(f"nbSecondes:{nbSecondes:5.8f}s - cap: {cap:3.6f}° - vitesse: {vitesseEnNoeud:3.6f}Nd")


            prevPosition = position
            position = cnav.nav(nbSecondes, vitesseEnNoeud, cap, prevPosition)
            distanceX, bearingX = cnav.navLoxodromique(position, prevPosition)
            self._logger.info(f"Position\n\tAvant:{prevPosition:s}\n\tApres: {position:s}\n\tDistance parcourue: {distanceX:02.6f}")

            gga = nmeaTool.getGGA(now, position)
            rmc = nmeaTool.getRMC(now, position, vitesseEnNoeud, cap)
            vtg = nmeaTool.getVTG(vitesseEnNoeud, cap)
            gsa = nmeaTool.getGSA()
            gsv = nmeaTool.getGSV()
            depth = nmeaTool.getDepth(nbSecondes)
            hdg = nmeaTool.getHDG(cap, variationMagnetique)
            trueWind = nmeaTool.getWindInfo("True", 150.0, 12.2)
            relativeWind = nmeaTool.getWindInfo("Relative", 150.0, 12.2)

            bwr = nmeaTool.getWayPointInfoBWR(now=now,
                                              positionWayPoint=positionArrivee,
                                              bearing=bearing,
                                              variation=variationMagnetique,
                                              distance=distance,
                                              uid="WP1")
            bwc = nmeaTool.getWayPointInfoBWC(now=now,
                                              positionWayPoint=positionArrivee,
                                              bearing=bearing,
                                              variation=variationMagnetique,
                                              distance=distance,
                                              uid="WP1")
            self._logger.debug(gga)
            #self._logger.debug(gsv)
            #self._logger.debug(gsa)
            #self._logger.debug(vtg)
            #self._logger.debug(rmc)
            #self._logger.debug(depth)
            #self._logger.debug(hdg)
            #self._logger.debug(trueWind)
            #self._logger.debug(relativeWind)
            #self._logger.debug(bwr)
            #self._logger.debug(bwc)

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
            self._logger.debug("------------------------------------------------------------------------------------------")

            sleep(sleepTimeInSec)
            nbSecondes = nbSecondes + sleepTimeInSec

            if nbSecondes > CApp.MAX_TIME:
                self.myNMEANetwork.close()
                break
