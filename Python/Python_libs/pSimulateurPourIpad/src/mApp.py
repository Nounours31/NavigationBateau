import logging
from datetime import datetime, timezone
import socket
import sys

import threading
from typing import List

from time import sleep

from pSfaTools.mLogger import getLogger

from pSfaNavigation.mCap import cCap
from pSfaNavigation.mVelocite import cVelocite, cVitesse
from pSfaNavigation.mVecteurEtat import cVecteurEtat, cTrajet
from pSfaNavigation.mNavigationBateau import cNavigationBateau
from mSimulateurNavigation import cSimulateurNav

from pSfaNmea.mNmea0183Lib import nmea0183lib

import env as myEnv
from mGUI import cMyGUI

trajet: dict[str, object] = myEnv.trajet
data: dict[str, object] = myEnv.data
        



class cApp:
    _logger: logging.Logger = getLogger("cApp", logging.INFO)
    stop_event : threading.Event = threading.Event()

    def __init__(self):
        self._serverThread : threading.Thread | None = None
        self._clientThread : threading.Thread | None = None

        self._protocolServer: str = 'TCP'
        self._socketServer : socket.socket | None = None

        self._socketClient : socket.socket | None = None
        self._hostServer : str | None = None
        self._portServer : int | None = None

        self._isReady = False
        self._tcp_connection : socket.socket | None = None

    
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
    # Thread server/receiver - utilise la socket server pour recevoir les info de l'Ipad notament les infos du pilote auto
    # -------------------------
    def _receiver_thread(self, protocol : str, host: str, port : int) -> None:
        cApp._logger.info("start receiver")
        while self._socketServer is None:
            cApp._logger.info("Wait for server")
            sleep(1) 


        if protocol == "UDP":
            while not cApp.stop_event.is_set() and self._socketServer is not None :    
                try :
                    data, addr = self._socketServer.recvfrom(1024)
                    nmea : nmea0183lib = nmea0183lib ()
                    nmea.DumpTZBoatTrames(data)
                except Exception as e:
                    self._logger.error(f"Socket not established - need to wait more ... {e}")
                    sleep(5)

        if protocol == "TCP":
            while self._socketServer is None or self._tcp_connection is None:        
                sleep(1)

            nbTrame : int = 0
            while not cApp.stop_event.is_set() and self._socketServer is not None and self._tcp_connection:        
                try:
                    data = self._tcp_connection.recv(1024)
                    cApp._logger.info("Client a reçu de l'iPad:", data.decode())
                    nmea : nmea0183lib = nmea0183lib ()
                    retour : List[tuple[str, str]] = []
                    retour = nmea.DumpTZBoatTrames(data)

                    cApp._logger.debug(f"Message from {host}:{port} - {data.decode()}")       
                    cUI : cMyGUI = cMyGUI.getInstance()
                    if nbTrame % 5 == 0:
                        cUI.addIPadInfo(f"----------------------------------------------------", True)
                    nbTrame += 1
                    for uneInfo in retour:
                        cUI.addIPadInfo(f"_receiver_thread: {nbTrame}\n\t - {uneInfo[0]}\n\t - {uneInfo[1]}")
                except Exception as e: 
                    self._logger.error(f"Socket not established - need to wait more ... {e}")
                    sleep(5)
                    sys.exit(5)

            print ("the end")
        
    

    # -------------------------
    # Thread server - utilise la socket server pour envoyer a l'iPad les info de nav
    # -------------------------
    def _server_thread(self, protocol : str, host: str, port : int) -> None:
        cApp._logger.info("start socket server")
        self._protocolServer = protocol
        if protocol == "UDP":
            self._server_thread_UDP(host = host, port = port)
        else:
            self._server_thread_TCP(host = host, port = port)
    
    # creation UDP    
    def _server_thread_UDP(self, host: str, port : int) -> None:
        cApp._logger.info("start socket server - UDP")
        self._hostServer = host
        self._portServer = port
        try:
            self._socketServer = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            self._isReady = True
        except socket.error as e:
            self._logger.error (f"Failed to create socket - {e}")
            sys.exit (1)

    # creation TCP
    def _server_thread_TCP(self, host: str, port : int) -> None:
        cApp._logger.info("start socket server - TCP")
        self._socketServer = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self._socketServer.bind((socket.gethostname(), port))
        self._socketServer.listen(1024)
        self._logger.info("Serveur: en attente de connexion...")
        self._tcp_connection, addr = self._socketServer.accept()
        self._logger.info(f"Serveur: connecte avec {self._tcp_connection} - {addr}")
        self._isReady = True

    
    # -------------------------
    # Envoyer a l'iPad les info de nav dans la socket server
    # -------------------------
    def serverSendData(self, data: str | None = None,  trames : List[str] | None = None) -> None:
        cApp._logger.info("serverSendData")
        databytes : bytes | None = None
        if data is not None:
            databytes = data.encode(encoding="utf-8")

        tramesBytes : List[bytes] | None = None
        if trames is not None:
            tramesBytes = [ t.encode(encoding="utf-8") for t in trames]

        self.serverSendBytesData(databytes, tramesBytes)

    # interne send data en UDP ou TCP
    def serverSendBytesData(self, data: bytes | None = None,  trames : List[bytes] | None = None) -> None:
        cApp._logger.debug("serverSendData")
        if trames is None:
            trames = []
        
        if data is not None:
            trames.append(data)

        if not cApp.stop_event.is_set() and self._socketServer is not None:
            if self._protocolServer == "UDP":
                for x in trames:
                    self._logger.debug(f"UDP Socket sendto host:{self._hostServer} port:{self._portServer}")
                    self._socketServer.sendto(x, (self._hostServer, self._portServer))
                    self._socketServer.sendto('\x0d\x0a'.encode(encoding="utf-8"), (self._hostServer, self._portServer))

            if self._protocolServer == "TCP" and self._tcp_connection is not None:
                for x in trames:
                    self._tcp_connection.sendall(x)
                    self._tcp_connection.sendall('\x0d\x0a'.encode(encoding="utf-8"))




    # -------------------------
    # Thread client - se connect a une socket
    # -------------------------
    def _client_thread(self, protocol : str, host: str, port : int) -> None:
        cApp._logger.info("start socket client ")
        if protocol == "UDP":
            self._client_thread_UDP(host = host, port = port)
        else:
            self._client_thread_TCP(host = host, port = port)
        
    # en UDP
    def _client_thread_UDP(self, host: str, port : int):
        cApp._logger.info("start socket client - UDP")
        with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
            s.bind((host, port))
            self._socketClient = s
            try:
                while not cApp.stop_event.is_set() and self._socketClient is not None :    
                    data, addr = self._socketClient.recvfrom(1024)
                    nmea : nmea0183lib = nmea0183lib ()
                    retour : List[tuple[str, str]] = []
                    retour = nmea.DumpTZBoatTrames(data)

                    cApp._logger.debug(f"Message from {addr}: {data.decode()}")       
                    cUI : cMyGUI = cMyGUI.getInstance()
                    for uneInfo in retour:
                        cUI.addIPadInfo(uneInfo[1])

            except Exception as err:
                cApp._logger.error("socket closed ... " + str(err))
                
            cApp._logger.error("Stop client thread")

    # en TCP
    def _client_thread_TCP(self, host: str, port : int):
        cApp._logger.info("start socket client - TCP")
        sleep(1)  # attendre que le serveur démarre
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((host, port))
            self._socketClient = s
            try:
                nbTrame : int = 0
                while not cApp.stop_event.is_set() and self._socketClient is not None:        
                    data = self._socketClient.recv(1024)
                    cApp._logger.debug("Client a reçu du serveur:" + data.decode())
                    cUI : cMyGUI = cMyGUI.getInstance()
                    nmea : nmea0183lib = nmea0183lib ()
                    trames : List[tuple[str, str]] = nmea.DumpTZBoatTrames(data)
                    if nbTrame % 2 == 0:
                        cUI.addIPadInfo(f"=========================================================", True)
                    nbTrame += 1
                    for t in trames:
                        cApp._logger.debug(f"Trame reçue {nbTrame}: \n\t{t[0]} \n\t{t[1]}")
                        cUI.addIPadInfo(f"_client_thread_TCP {nbTrame} \n\t - {t[0]} \n\t - {t[1]}")

            except Exception as err:
                cApp._logger.error("socket closed ... " + str(err))

            self._logger.error("Stop client thread")

    # --------------------------------------------------------------
    # Start the server qui emet les trames NMEA
    # --------------------------------------------------------------
    def startServer (self, protocol : str, host: str, port : int, receiver : bool) -> None:
        # emission vers l'iPad
        cApp._logger.info("start SERVER")
        self._serverThread = threading.Thread(target=self._server_thread, daemon=True, kwargs={
            "protocol" : protocol, 
            "host": host, 
            "port" : port})
        
        self._serverThread.start()

        # reception de l'iPad
        self._receiverThread = threading.Thread(target=self._receiver_thread, daemon=True, kwargs={
            "protocol" : protocol, 
            "host": host, 
            "port" : port})
        self._receiverThread.start()
        
    # --------------------------------------------------------------
    # Start the client qui consomme  qui emet les trames NMEA
    # --------------------------------------------------------------
    def startClient (self, protocol : str, host: str, port : int) -> None:
        cApp._logger.info("start CLIENT")
        self._clientThread = threading.Thread(target=self._client_thread, daemon=True, kwargs={
            "protocol" : protocol, 
            "host": host, 
            "port" : port})
        
        self._clientThread.start()


    # ---------------------------------------------------------------
    # est ce que la socket est ouverte et connectée ?
    # ---------------------------------------------------------------
    def isReady(self) -> bool:
        return self._socketServer is not None and self._isReady
    
    
    # ---------------------------------------------------------------
    # On simul la nav et on pousse les donnees dans la socket
    # ---------------------------------------------------------------
    def startNavigation (self) -> None:
        cApp._logger.info("start NAV")

        while (self._socketServer is None) :
            self._logger.info("Wait for server")
            sleep(1)

        now : float = datetime.now(tz=timezone.utc).timestamp()
        v: cVecteurEtat = cVecteurEtat.fromDict(data)
        t: cTrajet = cTrajet.fromDict(trajet)
        
        nav : cNavigationBateau = cNavigationBateau (etat = v, trajet= t)  
        sim : cSimulateurNav = cSimulateurNav(now, navigationBateau = nav)

        cUI : cMyGUI = cMyGUI.getInstance()

        while not cApp.stop_event.is_set():
            trames : List[bytes]
            newCap : float | None = cUI.getCap()
            newVitesse : float | None = cUI.getVitesse()

            forceCap : bool = newCap is not None
            forceVitesse : bool = newVitesse is not None

            if forceCap or forceVitesse :
                sog_imposed: cVelocite = v.bateau.sog
                if forceCap:
                    sog_imposed.sens = cCap(valAsDeg=newCap)
                if forceVitesse:
                    sog_imposed.vitesse = cVitesse(valAsNoeud=newVitesse)
                v.bateau.sog_imposed = sog_imposed
                self._logger.info("Nouveau cap demande: " + str(sog_imposed) + " - on le prend en compte dans la nav")
            else:
                v.bateau.sog_imposed = None # pas de cap impose - on laisse la nav faire son travail

            trames = sim.evaluateMaintenatNavigation()
            
            cApp._logger.info(v.toString())
            self.serverSendBytesData(trames=trames)
            cUI.addVecteurEtatInfo(v.toString(), clear=True)
            sleep(2)


        # arreter les sockets ...
        if self._clientThread is not None:
             self._clientThread.join()  
        if self._serverThread is not None:  
            self._serverThread.join()
