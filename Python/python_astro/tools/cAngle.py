from pprint import pprint
import re
import logging
import logging.config
import math
from cAstroError  import cAstroError

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("cAngle")


class cAngle:
    """gestion des heures"""

    __regexDegreeCas0: str = r"^([\+\-])?\s*([\d\.°'\"]+)$"
    __regexDegreeCas1: str = r"^(\d+)°(\d{2})'(\d{2}(\.\d+)?)\"?$"
    __regexDegreeCas2: str = r"^(\d+)°(\d{2}(\.\d+)?)'?$"
    __regexDegreeCas3: str = r"^(\d+(\.\d+)?)°?$"

    __regexMinCas1: str    = r"^(\d+)'(\d{2}(\.\d+)?)\"?$"
    __regexMinCas2: str    = r"^(\d+(\.\d+)?)'$"

    __regexSec: str        = r"^(\d+(\.\d+)?)\"$"
    

    def __init__(self):
        self._secondeDecimale: float = 0.0

    def __str__(self) -> str:
        return self.toString(0)


    def val(self) -> float:
        return self._secondeDecimale

    def asDeg(self) -> float:
        logger.debug (f"self._secondeDecimale {self._secondeDecimale}")
        return self._secondeDecimale / 3600.0

    def _parseAnglePositive(self, sVal: str) -> float:   
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

            return signe * self._parseAnglePositive(match.group(2)) 
        raise cAstroError(f"String is not an angle {sVal}")

    def parse(self, sVal : str) :
        self._secondeDecimale = 0.0

        if not isinstance(sVal, str):
            raise cAstroError("No string to parse")

        if sVal is None:
            raise cAstroError("String is none")

        if len(sVal) == 0:
            raise cAstroError("String is empty")

        self._secondeDecimale = self.__parseAngleSigned(sVal)
        return self
        

    @staticmethod
    def _toString_deg(toParse, withSign : bool = False) -> str:
        sign: str = ""
        if withSign:
            sign: str = "+"
            
        if toParse < 0.0:
            sign = "-"
            toParse = -1.0 * toParse
        
        return  f"{sign:s}{(toParse / 3600.0):5.5f}°"

    @staticmethod
    def _toString_min(toParse, withSign : bool = False) -> str:
        sign: str = ""
        if withSign:
            sign: str = "+"
            
        if toParse < 0.0:
            sign = "-"
            toParse = -1.0 * toParse
        
        iHeure: int = int(math.floor(toParse / 3600.0))
        toParse = toParse - iHeure * 3600.0
        fminute: float = toParse / 60.0
        return f"{sign:s}{iHeure:01d}°{fminute:06.3f}'"
        
    @staticmethod
    def _toString_sec(toParse, withSign : bool = False) -> str:
        sign: str = ""
        if withSign:
            sign: str = "+"
            
        if toParse < 0.0:
            sign = "-"
            toParse = -1.0 * toParse
        
        iHeure: int = int(math.floor(toParse / 3600.0))
        toParse = (toParse - iHeure * 3600.0) 
        iminute: int = int(toParse / 60.0)
        toParse = (toParse - iminute * 60.0) 
        fseconde: float = toParse
        return f"{sign:s}{iHeure:01d}°{iminute:02d}'{fseconde:05.2f}\""

    def toString(self, format : int = 1, withSign : bool = True) -> str:
        toParse: float = self._secondeDecimale

        sign: str = ""
        if withSign:
            sign: str = "+"
            
        if toParse < 0.0:
            sign = "-"
            toParse = -1.0 * toParse

        if format == 0:
            return f"{sign:s}{cAngle._toString_sec(toParse, withSign= False)}"

        else:
            return f"{sign:s}{cAngle._toString_deg(toParse, withSign= False):s} [{sign:s}{cAngle._toString_min(toParse, withSign= False):s} # {sign:s}{cAngle._toString_sec(toParse, withSign= False):s}]"

    @staticmethod
    def fromDeg(f: float):
        c : cAngle = cAngle()
        c._secondeDecimale = f * 3600.0
        return c
