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

from pSfaNavigation.mVecteurEtat import cVecteurEtat, cVecteurEtatKeys, cTrajet
from pSfaNavigation.mNavigationBateau import cNavigationBateau
from mSimulateurNavigation import cSimulateurNav

from pSfaNmea.mNmea0183Lib import nmea0183lib

import env as myEnv
trajet: dict[str, object] = myEnv.trajet
data: dict[str, object] = myEnv.data
        



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

        self._isReady = False
        self._tcp_connection = None

    
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
    def _receiver_thread(self, protocol : str, host: str, port : int) -> None:
        cApp._logger.info("start receiver")
        while self._socketServer is None:
            print(f"Wait for server")
            sleep(1) 


        if protocol == "UDP":
            while not cApp.stop_event.is_set() and self._socketServer is not None :    
                data, addr = self._socketServer.recvfrom(1024)
                nmea : nmea0183lib = nmea0183lib ()
                nmea.DumpTZBoatTrames(data)

        if protocol == "TCP":
            while self._socketServer is None or self._tcp_connection is None:        
                sleep(1)

            while not cApp.stop_event.is_set() and self._socketServer is not None and self._tcp_connection:        
                data = self._tcp_connection.recv(1024)
                print("Client a reçu:", data.decode())
                nmea : nmea0183lib = nmea0183lib ()
                nmea.DumpTZBoatTrames(data)
            
            print ("the end")
        
    

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
        self._socketServer = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self._socketServer.bind((host, port))
        self._socketServer.listen(2)
        print("Serveur: en attente de connexion...")
        self._tcp_connection, addr = self._socketServer.accept()
        print(f"Serveur: connecte avec {self._tcp_connection} - {addr}")
        self._isReady = True

    
    def serverSendData(self, data: str | None = None,  trames : List[str] | None = None) -> None:
        cApp._logger.info("serverSendData")
        databytes : bytes | None = None
        if data is not None:
            databytes = data.encode(encoding="utf-8")

        tramesBytes : List[bytes] | None = None
        if trames is not None:
            tramesBytes = [ t.encode(encoding="utf-8") for t in trames]

        self.serverSendBytesData(databytes, tramesBytes)

    def serverSendBytesData(self, data: bytes | None = None,  trames : List[bytes] | None = None) -> None:
        cApp._logger.info("serverSendData")
        if trames is None:
            trames = []
        
        if data is not None:
            trames.append(data)

        if not cApp.stop_event.is_set() and self._socketServer is not None:
            if self._protocolServer == "UDP":
                for x in trames:
                    self._socketServer.sendto(x, (self._hostServer, self._portServer))
                    self._socketServer.sendto('\x0d\x0a'.encode(encoding="utf-8"), (self._hostServer, self._portServer))

            if self._protocolServer == "TCP":
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
        
    def _client_thread_UDP(self, host: str, port : int):
        cApp._logger.info("start socket client - UDP")
        with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
            s.bind((host, port))
            self._socketClient = s
            try:
                while not cApp.stop_event.is_set() and self._socketClient is not None :    
                    data, addr = self._socketClient.recvfrom(1024)
                    nmea : nmea0183lib = nmea0183lib ()
                    nmea.DumpTZBoatTrames(data)

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

    def startServer (self, protocol : str, host: str, port : int, receiver : bool) -> None:
        cApp._logger.info("start SERVER")
        self._serverThread = threading.Thread(target=self._server_thread, kwargs={
            "protocol" : protocol, 
            "host": host, 
            "port" : port})
        
        self._serverThread.start()

        self._receiverThread = threading.Thread(target=self._receiver_thread, kwargs={
            "protocol" : protocol, 
            "host": host, 
            "port" : port})
        
        self._receiverThread.start()

    def startClient (self, protocol : str, host: str, port : int) -> None:
        cApp._logger.info("start CLIENT")
        self._clientThread = threading.Thread(target=self._client_thread, kwargs={
            "protocol" : protocol, 
            "host": host, 
            "port" : port})
        
        self._clientThread.start()

    def isReady(self) -> bool:
        return self._socketServer is not None and self._isReady
    
    
    def startNavigation (self) -> None:
        cApp._logger.info("start NAV")

        while (self._socketServer is None) :
            print(f"Wait for server")
            sleep(1)

        now : float = datetime.now(tz=timezone.utc).timestamp()
        v: cVecteurEtat = cVecteurEtat.fromDict(data)
        t: cTrajet = cTrajet.fromDict(trajet)
        
        nav : cNavigationBateau = cNavigationBateau (etat = v, trajet= t)  
        sim : cSimulateurNav = cSimulateurNav(now, navigationBateau = nav)

        while not cApp.stop_event.is_set():
            trames : List[bytes]
            trames = sim.evaluateMaintenatNavigation()
            
            cApp._logger.info(v.toString())
            self.serverSendBytesData(trames=trames)
            sleep(2)


        # arreter les sockets ...
        if self._clientThread is not None:
             self._clientThread.join()  
        if self._serverThread is not None:  
            self._serverThread.join()
