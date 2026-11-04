from abc import ABC, abstractmethod
from datetime import datetime

import pytz

import os
import logging
import logging.config
import json

loggingInitPath = os.path.join(os.path.dirname(__file__), ".\\logging.json")
with open(loggingInitPath, 'r') as f:
    CONFIG = f.read()
logging.config.dictConfig(json.loads(CONFIG))
logger = logging.getLogger("MareeScrapper")
logger.info("Configured the logger!")

class MareeScrapper (ABC):
    def __init__(self) -> None:
        super().__init__()

    @abstractmethod
    def __str__(self) -> str:
        return super().__str__()

    @abstractmethod
    def getListPort(self) -> list:
        pass

    @abstractmethod
    def getPortMaree(self, port : str, dateDebut : str, dureeEnJour: int, outputName: str = ".\\maree.defaut.csv", content4Odt: str = None) -> list:
        pass

    @abstractmethod
    def getType(self) -> str:
        pass

    # --------------------------------------------------------------
    # test le format des dates
    # --------------------------------------------------------------
    def isDateValide (self, debut: str) -> bool: 
        retour : bool = False
        if len(debut) != 8: 
            return retour
        
        try:
            jourdebut: datetime = datetime(int(debut[:4]), int(debut[4:6]), int(debut[6:8]), 0, 0, 0, 0, pytz.utc)
        except Exception as e:
            logger.critical("Parse de la date KO. Date >>" + debut + "<<")
            logger.critical(e)
            return retour

        retour = True
        return retour
