from pprint import pprint
import re
import logging
import logging.config
import math

logging.config.fileConfig('logging.conf')
logger = logging.getLogger("cAngle")

class cAngle:
    """ gestion des heures """
    __regexSecondes : str = r"^(\d+)°(\d{2})'(\d{2}(\.\d+)?)\"$"
    __regexMinutes : str = r"^(\d+)°(\d{2}(\.\d+)?)'$"
    __regexHeureDec : str = r"^(\d+(\.\d+)?)°?$"

    def __init__(self, *, sVal: str = ""):
        self.__secondeDecimale : float = 0.0
        logger.debug(f"Create cHeure:{sVal:s}")
        if isinstance(sVal, str) and len(sVal) > 0:
            try:
                self.__secondeDecimale = self.parse (sVal)
                logger.debug(f"cHeure - __secondeDecimale:{self.__secondeDecimale:10.5f}")
            except:
                self.__secondeDecimale = -1.0
                logger.critical (f"Unable to parse heure {sVal:s}")

    def __str__(self) -> str :
        return f"{self.__secondeDecimale:10.5f}"

    @staticmethod
    def usage (sVal: str) -> str:
        return f""

    def get_val(self) -> float:
        pprint(vars(self))
        return self.__secondeDecimale

    def parse (self, sVal: str) -> float:
        self.__secondeDecimale = -1.0

        if not isinstance(sVal, str):
            return self.get_val()
        
        if sVal == None:
            return self.get_val()
        
        if len(sVal) == 0:
            return self.get_val()
        
        if re.search(cAngle.__regexSecondes, sVal):
            return self.__parseSeconde(sVal)

        if re.search(cAngle.__regexMinutes, sVal):
            return self.__parseMinutes(sVal)

        if re.search(cAngle.__regexHeureDec, sVal):
            return self.__parseHeureDec(sVal)
        
        return self.get_val()
    
    def __parseSeconde (self, sVal: str) -> float:
        self.__secondeDecimale = -1.0
        return self.get_val()

    def __parseMinutes (self, sVal: str) -> float:
        self.__secondeDecimale = -1.0
        return self.get_val()

    def __parseHeureDec (self, sVal: str) -> float:
        self.__secondeDecimale = -1.0

        matches : list[re.Match[str]] = re.finditer(cAngle.__regexHeureDec, sVal, re.NOFLAG)
        for matchNum, match in enumerate(matches, start=1):    
            for groupNum in range(0, len(match.groups())):
                groupNum : int = groupNum + 1
        
                val : str = match.group(groupNum)
                match groupNum:
                    case 1:
                        self.__secondeDecimale += float (val) * 3600.0
                    case _:
                        self.__secondeDecimale += 0.0
                logger.debug(f"groupNum:{groupNum:2d} Value:{val:10s} - __secondeDecimale:{self.__secondeDecimale:10.5f}")

        return self.get_val()
        return self.get_val()
        



    def toString (self) -> str:
        toParse : float = self.__secondeDecimale; 

        iHeure : int = int(math.floor(toParse / 3600.0))

        toParse = toParse - iHeure * 3600.0
        iMinute : int = int(math.floor(toParse / 60.0))

        toParse = toParse - iMinute * 60

        retour : str = f"{iHeure:d}°{iMinute:2d}'{toParse:5.3f}\""
        return retour
