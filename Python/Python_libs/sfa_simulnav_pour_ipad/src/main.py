import logging
from datetime import datetime
import signal
import sys

from typing import List, Any
from colorist import green, Color

from time import sleep, time



from sfa_tools import myLogger

from mySocket import mySocket
from mySocketConf import eSocketConfig, TCP_CONFIG, UDP_CONFIG
from env import sleepTimeInSec, postionTrinitee, postionStQuay, data, depart, trajet

from sfa_navigation import cLatitude, cLongitude, cNavigationBateau, cVelocite, cPosition, cVecteurEtat
from sfa_navigation.cVecteurEtat import cTrajet, cVecteurEtatKeys


xx : mySocket 
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

    nmeaTools : nmea0183Lib = nmea0183Lib.nmea0183lib()

    v: cVecteurEtat = cVecteurEtat.fromDict(data)
    t: cTrajet = cTrajet.fromDict(trajet)

    b : cNavigationBateau = cNavigationBateau (etat = v, trajet= t)
    now : float = datetime.datetime.now(tz=datetime.timezone.utc).timestamp()
    theEnd : float = now + 100
    dixSecondes  : float = 10  
    
    currentTime : float = datetime.datetime.now(tz=datetime.timezone.utc).timestamp()


    iLoop : int = 1  
    while True:
        logger.info (f"-- {iLoop:03d} {datetime.now()}--------------------------------------------------")

        dT = currentTime - now
        b.navigate (dT)
        print(b.toString())
        now = currentTime

        trames : List[bytes] = nmeaTools.computeTrames(v)
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




