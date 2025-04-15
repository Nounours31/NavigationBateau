import socket


class CSocketNetWorkForNMEA() :
    __UDP_IP = "127.0.0.1"
    __UDP_PORT = 5005

    def __init__(self):
        self._socket = None

    def close(self) -> None:
        if not self._socket is None:
            self._socket.close()


    def openSocket(self) -> None:
        if self._socket is None:
            self._socket = socket.socket(socket.AF_INET,  # Internet
                                 socket.SOCK_DGRAM)  # UDP

    def sendto(self, x):
        self._socket.sendto(x, (CSocketNetWorkForNMEA.__UDP_IP, CSocketNetWorkForNMEA.__UDP_PORT))
        self._socket.sendto('\x0d\x0a'.encode(encoding="utf-8"), (CSocketNetWorkForNMEA.__UDP_IP, CSocketNetWorkForNMEA.__UDP_PORT))

    @classmethod
    def getIP(cls) -> str:
        return cls.__UDP_IP

    @classmethod
    def getPort(cls) -> int:
        return cls.__UDP_PORT

