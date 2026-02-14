import logging
from datetime import datetime
import signal
import sys

from typing import List, Any
from colorist import green, Color

from time import sleep, time

from sfa_navigation import cVitesse, cCap, cPosition
from sfa_nmea import nmea0183Lib
from sfa_tools import myLogger

from mySocket import mySocket
from mySocketConf import eSocketConfig, TCP_CONFIG, UDP_CONFIG
from env import sleepTimeInSec, postionTrinitee, postionStQuay
from mySimulateurNav import simulateurNav as Nav

xx : mySocket = None
logger: logging.Logger = myLogger.getLogger("main", logging.DEBUG)

def exit_gracefully(signum, frame):
    signal.signal(signal.SIGINT, original_sigint)

    try:
        if input("\nReally quit? (y/n)> ").lower().startswith('y'):
            mySocket.closeAll()
            sys.exit(0)

    except KeyboardInterrupt:
        print("Ok ok, quitting")
        xx.closeAll()
        sys.exit(0)

    # restore the exit gracefully handler here    
    signal.signal(signal.SIGINT, exit_gracefully)




def sendNavInfo(s : mySocket):
    if s is None:
        print('La socket est vide')
        return

    navconfig : dict[str, Any] = {
        "vitesseEnNoeudMoyenne" : cVitesse(valAsNoeud=15.0),
        "variationMagnetique" : cCap(valAsDeg=-1.2),
        "nav": {
            "positionDepart": postionTrinitee,
            "positionWayPoints" : [
                cPosition.fromString("2, 2"),
                cPosition.fromString("2, 2")
            ],
            "positionArrivee": postionStQuay,
        },
        "vent": {
            "vitesseEnNd" : cVitesse(valAsNoeud=15.0),
            "directionEnDeg": cCap(valAsDeg=75.0), # sens du vent attention !!!
            "temperature" : 20
        },
        "courant": {
            "vitesseEnNd" : cVitesse(valAsNoeud=1.50),
            "directionEnDeg": cCap(valAsDeg=1.5),
        },
        "eau": {
            "profondeur" : 17,
            "temperature" : 12,
        },
    }
    nav : Nav = Nav(navconfig)
    nmeaTools : nmea0183Lib = nmea0183Lib.nmea0183lib()

    iLoop : int = 1  
    while True:
        logger.info (f"-- {iLoop:03d} {datetime.now()}--------------------------------------------------")

        nav.nav(time())
        trames : List[bytes] = nmeaTools.computeTrames(nav)
        s.send(trames)

        sleep(sleepTimeInSec)
        iLoop += 1



def main():
    global xx
    # Creation socket TCP
    s : mySocket = mySocket(mode=eSocketConfig.TCP, debug=False)
    xx = s
    sendNavInfo(s)



if __name__ == '__main__':
    global original_sigint
    original_sigint = signal.getsignal(signal.SIGINT)
    signal.signal(signal.SIGINT, exit_gracefully)
    main()
    print ("The end ...")




