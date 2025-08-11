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

    def lat(self, l : cLatitude) :
        self._lat = l

    def long(self, l: cLatitude) :
        self._long = l

    def toString(self, format : int = 1) -> str:
        return f"{self._lat.toString(format)} / {self._long.toString(format)} "

if __name__ == "__main__":
    print("cPosition")
