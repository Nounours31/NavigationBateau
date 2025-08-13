from datetime import datetime
from datetime import timezone
from pprint import pprint
import re
import logging
import logging.config
import math

from cAstroError import cAstroError

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("cHeure")


class cHeure:
    """gestion des heures"""

    

    def __init__(self, sVal : str = None):
        self.__secondeDecimale: float = 0.0
        if not sVal is None:
            self.parse(sVal)


    def __str__(self) -> str:
        return self.toString(format=0)

    def val(self) -> float:
        return self.__secondeDecimale

    def parse(self, sVal: str) :
        self.__secondeDecimale = cHeure.__parseHeure2Seconde(sVal)
        return self

    def duplicate(self) :
        x : cHeure = cHeure()
        x.__secondeDecimale = self.__secondeDecimale
        return x


    def addSeconde (self, x : float): 
        self.__secondeDecimale += x 

    def moinsHeureFuseau (self, x : int): 
        self.__secondeDecimale = self.__secondeDecimale - x * 3600.0

    @staticmethod
    def __parseHeure2Seconde(sVal: str) -> float:
        regex: str = r"^(\d+)(:\d{2})?(:\d{2}(\.\d+)?)?$"

        e : cAstroError = cAstroError(f"Heure incorrecte >{sVal}< - Format attendu h+:mm:ss.sss / h+:mm.mm - ")

        if not isinstance(sVal, str):
            raise e

        if sVal is None:
            raise e

        if len(sVal) == 0:
            raise e

        if not re.search(regex, sVal):
            raise e

        secondeDecimale = 0.0
        heure = 0.0
        min = 0.0
        sec = 0.0
        matches: list[re.Match[str]] = re.finditer(regex, sVal, re.NOFLAG)
        for _, match in enumerate(matches, start=1):
            heure = float(match.group(1))
            
            if not match.group(2) is None:
                hasMin = True
                min = float(match.group(2).replace(':',''))
            
            if not match.group(3) is None:
                if hasMin:
                    sec = float(match.group(3).replace(':',''))
                else:
                    min = float(match.group(3).replace(':',''))

            secondeDecimale = heure * 3600.0 + min * 60.0 + sec
            logger.debug(f"secondeDecimale:{secondeDecimale:10.5f}")

        return secondeDecimale

    @staticmethod
    def parseChrono2Seconde(sVal: str) -> float:
        regex: str = r"^(\d+):(\d{2}(\.\d+)?)$"

        e : cAstroError = cAstroError(f"Chrono string ko >{sVal}<. Format attendu [m+:ss / m+:ss.ss]")

        if not isinstance(sVal, str):
            raise e

        if sVal is None:
            raise e

        if len(sVal) == 0:
            raise e

        if not re.search(regex, sVal):
            raise e

        secondeDecimale = 0.0
        min = 0.0
        sec = 0.0
        matches: list[re.Match[str]] = re.finditer(regex, sVal, re.NOFLAG)
        for _, match in enumerate(matches, start=1):
            min = float(match.group(1))
            sec = float(match.group(2))
            
            secondeDecimale = min * 60.0 + sec
            logger.debug(f"secondeDecimale:{secondeDecimale:10.5f}")

        return secondeDecimale


    @staticmethod
    def parseFuseau(sVal: str) -> float:
        # epoch est le 1er janvier 1970 à 00:00:00 (UTC)
        regexp = r"^([\+\-])?\s*(\d{1,2})$"

        e : cAstroError = cAstroError(f"Fuseau incorrecte. Format [+/- dd] - >{sVal}<")
        if not isinstance(sVal, str):
            raise e

        if sVal is None:
            raise e

        if len(sVal) == 0:
            raise e

        if not re.search(regexp, sVal):
            raise e

        matches = re.finditer(regexp, sVal, re.NOFLAG)
        for _, match in enumerate(matches, start=1):
            signe = +1.0  
            if not match.group(1) is None and match.group(1) == '-':
                signe = -1.0
            
            decalage = int (match.group(2))
            return signe * decalage
        
        raise e



    def toString(self, format: int = 1) -> str:
        if format == 0:
            return f"{self.__secondeDecimale:05.3f}"
        
        toParse: float = self.__secondeDecimale
        iHeure: int = int(math.floor(toParse / 3600.0))

        toParse = toParse - iHeure * 3600.0
        iMinute: int = int(math.floor(toParse / 60.0))

        toParse = toParse - iMinute * 60

        retour: str = f"{iHeure:02d}:{iMinute:02d}:{toParse:06.3f}"
        return retour
