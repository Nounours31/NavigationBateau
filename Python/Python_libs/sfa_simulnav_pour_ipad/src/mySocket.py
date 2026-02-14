import logging
import socket
from enum import Enum
from typing import List, Dict, Any

from logging import Logger
from sfa_tools.myLogger import getLogger

from mySocketConf import eSocketConfig, TCP_CONFIG, UDP_CONFIG


class mySocket:
    _logger : Logger = getLogger("mySocket", logging.DEBUG)

    def __init__(self, mode: eSocketConfig, debug = False):
        self._debug = debug
        self._mode = mode

        if not debug:
            if mode == eSocketConfig.TCP:
                server: socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                server.bind(('', TCP_CONFIG["PORT"]))
                server.listen()
                self._sock = server
                self._client, adresseClient = server.accept()
                mySocket._logger.info (f"bind done with: {adresseClient}")

            elif mode == eSocketConfig.UDP:
                server: socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
                server.bind(('', UDP_CONFIG["PORT"]))


            # mySocket.__sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM) # UDP

    def closeAll(self):
        if not self._debug:
            self._sock.close()

    def send(self, trames : List[bytes]):
        if not self._debug:
            if self._mode == eSocketConfig.TCP:
                client = self._client
                for t in trames:
                    client.sendall(t)
                    client.sendall(bytes("\x0d\x0a".encode(encoding="utf-8")))

            elif self._mode  == eSocketConfig.UDP:
                sock = self._sock
                for t in trames :
                    sock.sendto(t, (UDP_CONFIG["IP"], UDP_CONFIG["PORT"]))
                    sock.sendto('\x0d\x0a'.encode(encoding="utf-8"), (UDP_CONFIG["IP"], UDP_CONFIG["PORT"]))


    def recv(self, param):
        if not self._debug:
            if self._mode == eSocketConfig.TCP:
                client : socket = self._client
                return client.recv(param)

