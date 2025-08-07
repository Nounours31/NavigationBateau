from pprint import pprint
import re
import logging
import logging.config
import math

logging.config.fileConfig('logging.conf')
logger = logging.getLogger("cHeure")

class cHeure:
    """ gestion des heures """
    __regex : str = r"^(\d{2})(:\d{2})?(:\d{2}(\.\d+)?)?$"

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
        return f"Format invalide (Ex: 10:20:30.566) >{sVal:s}< {cHeure.__regex:s}"

    def get_val(self) -> float:
        pprint(vars(self))
        return self.__secondeDecimale

    def parse (self, sVal: str) -> float:
        regex : str = cHeure.__regex

        if not isinstance(sVal, str):
            return -1.0
        
        if sVal == None:
            return -1.0
        
        if len(sVal) == 0:
            return -1.0
        
        if not re.search(regex, sVal):
            return -1.0
        
        self.__secondeDecimale = 0.0
        matches : list[re.Match[str]] = re.finditer(regex, sVal, re.NOFLAG)
        for matchNum, match in enumerate(matches, start=1):    
            for groupNum in range(0, len(match.groups())):
                groupNum : int = groupNum + 1
        
                val : str = match.group(groupNum)

                """
                    groupNum: 1 Value:10         - __secondeDecimale:36000.00000
                    groupNum: 2 Value::11        - __secondeDecimale:36660.00000
                    groupNum: 3 Value::12.13     - __secondeDecimale:36672.13000
                    groupNum: 4 Value:.13        - __secondeDecimale:36672.13000
                """

                match groupNum:
                    case 1:
                        self.__secondeDecimale += float (val) * 3600.0
                    case 2:
                        self.__secondeDecimale += float (val.replace(":","")) * 60.0
                    case 3:
                        self.__secondeDecimale += float (val.replace(":","")) 
                    case 4:
                        self.__secondeDecimale += 0.0
                    case _:
                        self.__secondeDecimale += 0.0
                logger.debug(f"groupNum:{groupNum:2d} Value:{val:10s} - __secondeDecimale:{self.__secondeDecimale:10.5f}")

        return self.get_val()
    
    def toString (self) -> str:
        toParse : float = self.__secondeDecimale; 

        iHeure : int = int(math.floor(toParse / 3600.0))

        toParse = toParse - iHeure * 3600.0
        iMinute : int = int(math.floor(toParse / 60.0))

        toParse = toParse - iMinute * 60

        retour : str = f"{iHeure:d}:{iMinute:2d}:{toParse:5.3f}"
        return retour
