from pprint import pprint
import re
import logging
import logging.config
import math
from cAstroError import cAstroError

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("cAngle")


class cAngle:
    """gestion des heures"""

    __regexDegreeCas0: str = r"^([\+\-])?\s*([\d\.°'\"]+)$"
    __regexDegreeCas1: str = r"^(\d+)°(\d{2})'(\d{2}(\.\d+)?)\"?$"
    __regexDegreeCas2: str = r"^(\d+)°(\d{2}(\.\d+)?)'?$"
    __regexDegreeCas3: str = r"^(\d+(\.\d+)?)°?$"

    __regexMinCas1: str    = r"^(\d+)'(\d{2}(\.\d+)?)\"$"
    __regexMinCas2: str    = r"^(\d+(\.\d+)?)'$"

    __regexSec: str        = r"^(\d+(\.\d+)?)\"$"
    
    
    def __init__(self, *, sVal: str = ""):
        self.__secondeDecimale: float = 0.0
        logger.debug(f"Create cHeure:{sVal:s}")
        if isinstance(sVal, str) and len(sVal) > 0:
            try:
                self.__secondeDecimale = self.parse(sVal)
                logger.debug(f"cHeure - __secondeDecimale:{self.__secondeDecimale:10.5f}")
            except TypeError:
                self.__secondeDecimale = -1.0
                logger.critical(f"Unable to parse heure {sVal:s}")

    def __str__(self) -> str:
        return f"{self.__secondeDecimale:10.5f}"

    @staticmethod
    def usage(sVal: str) -> str:
        return "A faire"

    def get_val(self) -> float:
        pprint(vars(self))
        return self.__secondeDecimale

    def __parseAnglePositive(self, sVal: str) -> float:   
        if re.search(cAngle.__regexDegreeCas1, sVal, re.NOFLAG) :
            return self.__parseDegCas1(sVal)
        if re.search(cAngle.__regexDegreeCas2, sVal, re.NOFLAG) :
            return self.__parseDegCas2(sVal)
        if re.search(cAngle.__regexDegreeCas3, sVal, re.NOFLAG) :
            return self.__parseDegCas3(sVal)
        if re.search(cAngle.__regexMinCas1, sVal, re.NOFLAG) :
            return self.__parseMinCas1(sVal)
        if re.search(cAngle.__regexMinCas2, sVal, re.NOFLAG) :
            return self.__parseMinCas2(sVal)
        if re.search(cAngle.__regexSec, sVal, re.NOFLAG) :
            return self.__parseSeconde(sVal)
             
        raise cAstroError(f"String is not an angle {sVal}")

    def __parseDegCas1 (self, sVal: str) :
        matches: list[re.Match[str]] = re.finditer(cAngle.__regexDegreeCas1, sVal, re.NOFLAG)
        for matchNum, match in enumerate(matches, start=1):
            deg : float = float(match.group(1))
            min : float = float(match.group(2))
            sec : float = float(match.group(3))

            return sec + min * 60.0 + deg * 3600.0
        raise cAstroError(f"String is not an angle {sVal}")
            
    def __parseDegCas2 (self, sVal: str) :
        matches: list[re.Match[str]] = re.finditer(cAngle.__regexDegreeCas2, sVal, re.NOFLAG)
        for matchNum, match in enumerate(matches, start=1):
            deg : float = float(match.group(1))
            min : float = float(match.group(2))

            return min * 60.0 + deg * 3600.0
        raise cAstroError(f"String is not an angle {sVal}")

    def __parseDegCas3 (self, sVal: str) :
        matches: list[re.Match[str]] = re.finditer(cAngle.__regexDegreeCas3, sVal, re.NOFLAG)
        for matchNum, match in enumerate(matches, start=1):
            deg : float = float(match.group(1))

            return deg * 3600.0
        raise cAstroError(f"String is not an angle {sVal}")

    def __parseMinCas1 (self, sVal: str) :
        matches: list[re.Match[str]] = re.finditer(cAngle.__regexMinCas1, sVal, re.NOFLAG)
        for matchNum, match in enumerate(matches, start=1):
            min : float = float(match.group(1))
            sec : float = float(match.group(2))

            return min * 60.0 + sec
        raise cAstroError(f"String is not an angle {sVal}")

    def __parseMinCas2 (self, sVal: str) :
        matches: list[re.Match[str]] = re.finditer(cAngle.__regexMinCas2, sVal, re.NOFLAG)
        for matchNum, match in enumerate(matches, start=1):
            min : float = float(match.group(1))

            return min * 60.0 
        raise cAstroError(f"String is not an angle {sVal}")

    def __parseSeconde (self, sVal: str) :
        matches: list[re.Match[str]] = re.finditer(cAngle.__regexSec, sVal, re.NOFLAG)
        for matchNum, match in enumerate(matches, start=1):
            sec : float = float(match.group(1))

            return sec 
        raise cAstroError(f"String is not an angle {sVal}")
            
    def __parseAngleSigned(self, sVal : str) :
        matches: list[re.Match[str]] = re.finditer(cAngle.__regexDegreeCas0, sVal, re.NOFLAG)
        for matchNum, match in enumerate(matches, start=1):
            signe = +1.0
            if not match.group(1) is None and match.group(1) == "-":
                signe = -1.0

            return signe * self.__parseAnglePositive(match.group(2)) 
        raise cAstroError(f"String is not an angle {sVal}")

    def parse(self, sVal : str) :
        self.__secondeDecimale = 0.0

        if not isinstance(sVal, str):
            raise cAstroError("No string to parse")

        if sVal is None:
            raise cAstroError("String is none")

        if len(sVal) == 0:
            raise cAstroError("String is empty")

        return self.__parseAngleSigned(sVal)
        



    def toString(self) -> str:
        toParse: float = self.__secondeDecimale

        iHeure: int = int(math.floor(toParse / 3600.0))

        toParse = toParse - iHeure * 3600.0
        iMinute: int = int(math.floor(toParse / 60.0))

        toParse = toParse - iMinute * 60

        retour: str = f"{iHeure:d}°{iMinute:2d}'{toParse:5.3f}\""
        return retour

    @staticmethod
    def toStringDebug(fValInDegree: float) -> str:
        if math.isnan(fValInDegree):
            return "NaN"

        sign: str = ""
        if fValInDegree < 0.0:
            sign = "-"
            fValInDegree = -1.0 * fValInDegree
        # heuredec
        sHeureDec: str = f"{sign:1s}{fValInDegree:07.4f}°"

        # minute hexa
        toParse: float = fValInDegree
        iHeure: int = int(math.floor(toParse))
        toParse = toParse - iHeure
        fminute: float = toParse * 60.0
        sMinHexa: str = f"{sign:1s}{iHeure:03d}°{fminute:05.2f}'"

        # seconde decimale
        toParse: float = fValInDegree
        iHeure: int = int(math.floor(toParse))
        toParse = (toParse - iHeure) * 60.0
        iminute: int = int(toParse)
        fseconde: float = (toParse - int(toParse)) * 60.0
        sSecDec: str = f"{sign:1s}{iHeure:03d}°{iminute:02d}'{fseconde:05.2f}'"

        retour: str = f"{sHeureDec:s} [{sMinHexa:s} - {sSecDec:s}]"
        return retour
