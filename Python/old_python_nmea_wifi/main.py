# import socketserver
# import tools.MyServer as MyServer
import multiprocessing
import threading
from datetime import datetime
import signal
import sys
import socket as socket

from typing import List
from colorist import green, Color

from time import sleep

from simulateurNav.simulateurNav import simulateurNav as Nav
from tools.angle import angle
from tools.mySocket import mySocket as mySocket
from nmea.nmea0183Lib import nmea0183lib as Nmea
from myEnv import myEnv
from tools.position import position

xx : mySocket = None


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



def listenNavInfo(s : mySocket):
    if s is None:
        print('La socket est vide')
        return

    if myEnv.ModeReseau == myEnv.UDP:
        return

    while True:
        print('Reception...')
        donnees = s.recv(1024)
        print('Recu :', donnees)

def sendNavInfo(s : mySocket):
    if s is None:
        print('La socket est vide')
        return

    navconfig : dict[str, float | object] = {
        "vitesseEnNoeudMoyenne" : 15.0,
        "variationMagnetique" : -1.2,
        "nav": {
            "positionDepart": myEnv.postionTrinitee.toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT),
            "positionWayPoints" : [
                position.fromString("2, 2").toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT),
                position.fromString("2, 2").toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT)
            ],
            "positionArrivee": myEnv.postionTrinitee.toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT)
        },
        "vent": {
            "vitesseEnNd" : 15,
            "directionEnDeg": 75, # sens du vent attention !!!
            "temperature" : 20
        },
        "courant": {
            "vitesseEnNd" : 1.5,
            "directionEnDeg": 2.5,
        },
        "eau": {
            "profondeur" : 17,
            "temperature" : 12,
        },
    }
    nav : Nav = Nav(navconfig)
    nmea : Nmea = Nmea()

    iLoop : int = 1  
    while True:
        myEnv.logger.info (f"-- {iLoop:03d} {datetime.now()}--------------------------------------------------")

        nav.nav()
        trames : List[bytes] = nmea.computeTrames(nav)
        s.send(trames)

        sleep(myEnv.sleepTimeInSec)
        iLoop += 1



def main():
    # Creation socket TCP
    global xx
    s : mySocket = mySocket(myEnv.ModeReseau, debug=False)
    xx = s

    process1 = threading.Thread(target=listenNavInfo,args=(s,))
    process1.start()
    process2 = multiprocessing.Process(target=sendNavInfo,args=(s,))
    process2.start()
    process1.join()
    process2.join()



if __name__ == '__main__':
    global original_sigint
    original_sigint = signal.getsignal(signal.SIGINT)
    signal.signal(signal.SIGINT, exit_gracefully)
    main()
    print ("The end ...")




