import logging
from datetime import datetime, timezone
import signal
import socket
import sys

import threading
from typing import List, Any
from colorist import green, Color

from time import sleep, time

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

from .mSimulateurNavigation import cSimulateurNav

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
        



class cApp:
    _logger: logging.Logger = getLogger("cApp", logging.DEBUG)
    stop_event : threading.Event = threading.Event()

    def __init__(self):
        self._serverThread : threading.Thread | None = None
        self._clientThread : threading.Thread | None = None

        self._protocolServer: str = 'TCP'
        self._socketServer : socket.socket | None = None

        self._socketClient : socket.socket | None = None
        self._hostServer : str | None = None
        self._portServer : int | None = None

    
    def closeAll(self) -> None :
        cApp._logger.info("Close all Socket")
        cApp.stop_event.set

        if self._socketServer:
           self._socketServer.close()
           self._socketServer = None
        if self._socketClient:
           self._socketClient.close()
           self._socketClient = None

    # -------------------------
    # Thread serveur - Met a disposition une socket
    # -------------------------
    def _server_thread(self, protocol : str, host: str, port : int) -> None:
        cApp._logger.info("start socket server")
        self._protocolServer = protocol
        if protocol == "UDP":
            self._server_thread_UDP(host = host, port = port)
        else:
            self._server_thread_TCP(host = host, port = port)
        
    def _server_thread_UDP(self, host: str, port : int) -> None:
        cApp._logger.info("start socket server - UDP")
        self._hostServer = host
        self._portServer = port
        self._socketServer = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    def _server_thread_TCP(self, host: str, port : int) -> None:
        cApp._logger.info("start socket server - TCP")
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.bind((host, port))
            s.listen()
            print("Serveur: en attente de connexion...")
            self._socketServer = s
            conn, addr = s.accept()
            print(f"Serveur: connecte avec {conn} - {addr}")
    
    def serverSendData(self, data: str | None = None,  trames : List[str] | None = None) -> None:
        cApp._logger.info("serverSendData")
        databytes : bytes | None = None
        if data is not None:
            databytes = data.encode(encoding="utf-8")

        tramesBytes : List[bytes] | None = None
        if trames is not None:
            tramesBytes = [ t.encode(encoding="utf-8") for t in trames]

        self.serverSendBytesData(databytes, tramesBytes)

    def serverSendBytesData(self, data: bytes,  trames : List[bytes]) -> None:
        cApp._logger.info("serverSendData")
        if trames is None:
            trames = []
        
        trames.append(data)

        if not cApp.stop_event.is_set():
            if self._protocolServer == "UDP":
                for x in trames:
                    self._socketServer.sendto(x, (self._hostServer, self._portServer))
                    self._socketServer.sendto('\x0d\x0a'.encode(encoding="utf-8"), (self._hostServer, self._portServer))

            if self._protocolServer == "TCP":
                for x in trames:
                    self._socketServer.sendall(x)
                    self._socketServer.sendall('\x0d\x0a'.encode(encoding="utf-8"))




    # -------------------------
    # Thread client - se connect a une socket
    # -------------------------
    def _client_thread(self, protocol : str, host: str, port : int) -> None:
        cApp._logger.info("start socket client ")
        if protocol == "UDP":
            self._client_thread_UDP(host = host, port = port)
        else:
            self._client_thread_TCP(host = host, port = port)
        
    def _client_thread_UDP(self, host: str, port : int):
        cApp._logger.info("start socket client - UDP")
        with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
            s.bind((host, port))
            self._socketClient = s
            try:
                while not cApp.stop_event.is_set() and self._socketClient is not None :    
                    data, addr = self._socketClient.recvfrom(1024)
                    print(f"Message from {addr}: {data.decode()}")            
            except Exception as err:
                cApp._logger.error("socket closed ... " + str(err))
                
            cApp._logger.error("Stop client thread")

    def _client_thread_TCP(self, host: str, port : int):
        cApp._logger.info("start socket client - TCP")
        sleep(1)  # attendre que le serveur démarre
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((host, port))
            self._socketClient = s
            try:
                while not cApp.stop_event.is_set() and self._socketClient is not None:        
                    data = self._socketClient.recv(1024)
                    print("Client a reçu:", data.decode())
            except Exception as err:
                cApp._logger.error("socket closed ... " + str(err))

            self._logger.error("Stop client thread")

    def startServer (self, protocol : str, host: str, port : int) -> None:
        cApp._logger.info("start SERVER")
        self._serverThread = threading.Thread(target=self._server_thread, kwargs={
            "protocol" : protocol, 
            "host": host, 
            "port" : port})
        
        self._serverThread.start()

    def startClient (self, protocol : str, host: str, port : int) -> None:
        cApp._logger.info("start CLIENT")
        self._clientThread = threading.Thread(target=self._client_thread, kwargs={
            "protocol" : protocol, 
            "host": host, 
            "port" : port})
        
        self._clientThread.start()

    def startNavigation (self) -> None:
        cApp._logger.info("start NAV")

        while (self._socketServer is None) :
            print(f"Wait for server")
            sleep(1)

        v: cVecteurEtat = cVecteurEtat.fromDict(data)
        t: cTrajet = cTrajet.fromDict(trajet)
        now : float = datetime.now(tz=timezone.utc).timestamp()
        sim : cSimulateurNav = cSimulateurNav(now)

        while not cApp.stop_event.is_set():
            now = datetime.now(tz=timezone.utc).timestamp()
            trames : List[str] = sim.nav(now, v, t)
            self.serverSendData(trames=trames)
            sleep(2)


        # arreter les sockets ...
        self._clientThread.join()
        self._serverThread.join()


        """
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
        """
