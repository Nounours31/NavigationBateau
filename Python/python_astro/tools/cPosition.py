import logging
import logging.config

from cLatitude import cLatitude
from cLongitude import cLongitude

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("cLatitude")


class cPosition:
    def __init__(self):
        self._lat = None
        self._long = None

    def __str__(self):
        pass

    def setLatitude(self, l : cLatitude) :
        self._lat = l

    def getLatitude(self) :
        return self._lat

    def getLongitude(self) :
        return self._long 

    def setLongitude(self, l: cLatitude) :
        self._long = l

    def toString(self, format : int = 1) -> str:
        return f"{self._lat.toString(format)} / {self._long.toString(format)} "

if __name__ == "__main__":
    print("cPosition")
