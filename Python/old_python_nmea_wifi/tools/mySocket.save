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

'''


# ----------------------------------------------------------------------------------
# main function
# ----------------------------------------------------------------------------------
def main():
    myEnv.UDP_IP = "127.0.0.1"
    myEnv.UDP_PORT = 5005

    global sock
    sock = socket.socket(socket.AF_INET, # Internet
                            socket.SOCK_DGRAM) # UDP

    logger.debug("UDP target IP: %s" % myEnv.UDP_IP)
    logger.debug("UDP target port: %s" % myEnv.UDP_PORT)

'''


class mySocket:
    __sock : socket = None
    __client : socket = None
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

        if mySocket.__sock is None and not debug:
            if mode == myEnv.TCP:
                server: socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                server.bind(('', myEnv.TCP_PORT))
                server.listen()
                mySocket.__sock = server
                mySocket.__client, adresseClient = server.accept()
                print (f"bind done with: {adresseClient}")

            elif mode == myEnv.UDP:
                server: socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
                server.bind(('', myEnv.UDP_PORT))


            # mySocket.__sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM) # UDP

    @staticmethod
    def closeAll():
        if not mySocket.__debug:
            mySocket.__sock.close()

    def send(self, trames : List[bytes]):
        if not mySocket.__debug:
            if mySocket.__mode == myEnv.TCP:
                client = mySocket.__client
                for t in trames:
                    client.sendall(t)
                    client.sendall(bytes("\x0d\x0a".encode(encoding="utf-8")))

            elif mySocket.__mode  == myEnv.UDP:
                sock = mySocket.__sock
                for t in trames :
                    sock.sendto(t, (myEnv.UDP_IP, myEnv.UDP_PORT))
                    sock.sendto('\x0d\x0a'.encode(encoding="utf-8"), (myEnv.UDP_IP, myEnv.UDP_PORT))

    def getClient(self):
        pass

    def recv(self, param):
        if not mySocket.__debug:
            if mySocket.__mode == myEnv.TCP:
                client : socket = mySocket.__client
                return client.recv(param)

