from datetime import datetime
from datetime import timezone
from pprint import pprint
import re
import logging
import logging.config

from cAstroError import cAstroError

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("cJour")


class cJour:
    """gestion des heures"""

    

    def __init__(self):
        self.__timestamp: float = 0.0

    def __str__(self) -> str:
        raise NotImplementedError

    @staticmethod
    def usage(sVal: str) -> str:
        raise NotImplementedError

    def val(self) -> float:
        return self.__timestamp
    
    def parse(self, sVal: str) :
        self.__timestamp = cJour.__parseJour(sVal)
        return self


    @staticmethod
    def __parseJour(sVal: str) -> float:
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




    def toString(self) -> str:
        raise NotImplementedError
