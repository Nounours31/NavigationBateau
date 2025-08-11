from datetime import datetime
from datetime import timezone
from pprint import pprint
import re
import logging
import logging.config
import math

from cAstroError import cAstroError

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("cHeureJour")


class cHeureJour:
    """gestion des heures"""

    

    def __init__(self, *, sVal: str = ""):
        self.__secondeDecimale: float = 0.0
        logger.debug(f"Create cHeureJour:{sVal:s}")
        if isinstance(sVal, str) and len(sVal) > 0:
            try:
                self.__secondeDecimale = self.parse(sVal)
                logger.debug(f"cHeureJour - __secondeDecimale:{self.__secondeDecimale:10.5f}")
            except ValueError:
                self.__secondeDecimale = -1.0
                logger.critical(f"Unable to parse heure {sVal:s}")

    def __str__(self) -> str:
        return f"{self.__secondeDecimale:10.5f}"

    @staticmethod
    def usage(sVal: str) -> str:
        return f"Format invalide (Ex: 10:20:30.566) >{sVal:s}< {cHeureJour.__regex:s}"

    def get_val(self) -> float:
        pprint(vars(self))
        return self.__secondeDecimale

    def parse(self, sVal: str) -> float:
        self.__secondeDecimale = cHeureJour.parseHeure2Seconde(sVal)

    @staticmethod
    def parseHeure2Seconde(sVal: str) -> float:
        regex: str = r"^(\d+)(:\d{2})?(:\d{2}(\.\d+)?)?$"

        e : cAstroError = cAstroError(f"Heure incorrecte. Format h+:mm:ss.sss / h+:mm.mm - {sVal}")

        if not isinstance(sVal, str):
            return -1.0

        if sVal is None:
            return -1.0

        if len(sVal) == 0:
            return -1.0

        if not re.search(regex, sVal):
            return -1.0

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

        e : cAstroError = cAstroError(f"Chronos. Format [m+:ss / m+:ss.ss] - >{sVal}<")

        if not isinstance(sVal, str):
            return -1.0

        if sVal is None:
            return -1.0

        if len(sVal) == 0:
            return -1.0

        if not re.search(regex, sVal):
            return -1.0

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
    def parseJour(sVal: str) -> float:
        # epoch est le 1er janvier 1970 à 00:00:00 (UTC)
        regexp = r"^(\d{4})/(\d{2})/(\d{2})$"

        e : cAstroError = cAstroError(f"Date incorrecte. Format yyyy/mm/dd - {sVal}")
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
            year = int(match.group(1))
            month = int(match.group(2))
            day = int(match.group(2))

            try:
                x : datetime = datetime(year=year, month=month,day=day,hour=0, minute=0, second=0,microsecond=0,tzinfo=timezone.utc)
                ts = x.timestamp()
                return ts
            except Exception as err:
                e : cAstroError = cAstroError(repr(err))
                raise e
        
        raise e

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



    def toString(self) -> str:
        toParse: float = self.__secondeDecimale
        iHeure: int = int(math.floor(toParse / 3600.0))

        toParse = toParse - iHeure * 3600.0
        iMinute: int = int(math.floor(toParse / 60.0))

        toParse = toParse - iMinute * 60

        retour: str = f"{iHeure:d}:{iMinute:2d}:{toParse:5.3f}"
        return retour
