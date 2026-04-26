import socket 
import signal
import sys
import logging
import logging.config

from math import floor, pi, cos, sin, tan
from time import sleep
from datetime import datetime, timezone
import calendar
from typing import List

from myEnv import myEnv

# Initialize the logger once as the application starts up.
logging.config.fileConfig('logging.conf')
 
# Get an instance of the logger and use it to write a log!
# Note: Do this AFTER the config is loaded above or it won't use the config.
logger = logging.getLogger(__name__)
logger.info("Configured the logger!")




class mySocket:
    __debug : bool = False
    __mode : int = myEnv.TCP
    
    def __init__(self, mode: int, debug = False):
        mySocket.__debug = debug
        mySocket.__mode = mode

        # TCP
        # handler_class = MyServer.MyServer
        # with socketserver.TCPServer(("", TCP_PORT), handler_class) as httpd:
        #     print("serving at port", TCP_PORT)
        #     httpd.serve_forever()

        if not debug:
            if mode == myEnv.TCP:
                server: socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                server.bind(('', myEnv.TCP_PORT))
                server.listen()
                self.__sock = server
                self.__client, adresseClient = server.accept()
                print (f"bind done with: {adresseClient}")

            elif mode == myEnv.UDP:
                server: socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
                server.bind(('', myEnv.UDP_PORT))


            # mySocket.__sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM) # UDP

    def closeAll(self):
        if not mySocket.__debug:
            self.__sock.close()

    def send(self, trames : List[bytes]):
        if not mySocket.__debug:
            if mySocket.__mode == myEnv.TCP:
                client = self.__client
                for t in trames:
                    client.sendall(t)
                    client.sendall(bytes("\x0d\x0a".encode(encoding="utf-8")))

            elif mySocket.__mode  == myEnv.UDP:
                sock = self.__sock
                for t in trames :
                    sock.sendto(t, (myEnv.UDP_IP, myEnv.UDP_PORT))
                    sock.sendto('\x0d\x0a'.encode(encoding="utf-8"), (myEnv.UDP_IP, myEnv.UDP_PORT))


    def recv(self, param):
        if not mySocket.__debug:
            if mySocket.__mode == myEnv.TCP:
                client : socket = self.__client
                return client.recv(param)

