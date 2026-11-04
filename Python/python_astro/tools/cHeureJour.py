from datetime import datetime
from datetime import timezone
from pprint import pprint
import re
import logging
import logging.config
import math

from cAstroError import cAstroError
from cHeure import cHeure
from cJour import cJour

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("cHeureJour")


class cHeureJour:
    """gestion des heures"""

    def __init__(self):
        self.__jour = None
        self.__heure = None
        
    def __str__(self) -> str:
        return str(self.__jour) + " " + str(self.__heure)

    def heure(self, h : cHeure):
        self.__heure = h

    def getHeure(self):
        return self.__heure

    def jour(self, j : cJour):
        self.__jour = j
        
    def toString(self) -> str:
        ts = self.asTS()
        x : datetime = datetime.fromtimestamp (ts, tz = timezone.utc) 
        return x.strftime("%Y/%m/%d %H:%M:%S [%Z]")

    def asTS(self) -> float:
        jourTS = 0
        if not self.__jour is None:
            jourTS = self.__jour.val()
        iseconde = 0
        if not self.__heure is None:
            iseconde = self.__heure.val()

        ts = jourTS + iseconde
        return ts
    