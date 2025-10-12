# import socketserver
# import tools.MyServer as MyServer
from datetime import datetime
import signal
import sys


from typing import List
from colorist import green, Color

from time import sleep

from simulateurNav.simulateurNav import simulateurNav as Nav
from tools.mySocket import mySocket as mySocket
from nmea.nmea0183Lib import nmea0183lib as Nmea
from myEnv import myEnv 


def exit_gracefully(signum, frame):
    signal.signal(signal.SIGINT, original_sigint)

    try:
        if input("\nReally quit? (y/n)> ").lower().startswith('y'):
            mySocket.closeAll()
            sys.exit(0)

    except KeyboardInterrupt:
        print("Ok ok, quitting")
        mySocket.closeAll()
        sys.exit(0)

    # restore the exit gracefully handler here    
    signal.signal(signal.SIGINT, exit_gracefully)




def main():
    navconfig : dict[str, float | object] = {
        "vitesseEnNoeudMoyenne" : 15.0,
        "capMoyen" : 186.0,
        "variationMagnetique" : -1.2,
        "positionDepart" : {
            "LatitudeDecimale" : myEnv.latTrinitee,
            "LongitudeDecimale" : myEnv.longTrinitee
        },
        "vent": {
            "vitesseEnNd" : 15,
            "directionEnDeg": 75,
            "temperature" : 20
        },
        "eau": {
            "profondeur" : 17,
            "temperature" : 12,
            "courant": {
                "vitesseEnNd" : 1.5,
                "directionEnDeg": 2.5,
            }
        },
    }
    nav : Nav = Nav(navconfig)
    nmea : Nmea = Nmea()
    myNet : mySocket = mySocket(debug=False)

    iLoop : int = 1  
    while True:
        myEnv.logger.info (f"-- {iLoop:03d} {datetime.now()}--------------------------------------------------")

        nav.nav()
        trame : List[bytes] = nmea.computeTrames(nav)
        myNet.send(trame)

        sleep(myEnv.sleepTimeInSec)
        iLoop += 1


if __name__ == '__main__':
    global original_sigint
    original_sigint = signal.getsignal(signal.SIGINT)
    signal.signal(signal.SIGINT, exit_gracefully)
    main()
    print ("The end ...")




