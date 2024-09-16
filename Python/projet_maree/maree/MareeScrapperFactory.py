from maree.MareeScrapper import MareeScrapper
from maree.MareeScrapperMareeInfo import MareeScrapperMareeInfo
from maree.MareeScrapperShom import MareeScrapperShom

import os
import logging
import logging.config
import json

loggingInitPath = os.path.join(os.path.dirname(__file__), ".\\logging.json")
with open(loggingInitPath, 'r') as f:
    CONFIG = f.read()
logging.config.dictConfig(json.loads(CONFIG))
logger = logging.getLogger("MareeScrapperFactory")
logger.info("Configured the logger!")


class MareeScrapperFactory ():
    def __init__(self) -> None:
        pass

    @staticmethod
    def getOrigines() -> dict : 
        retour : dict = {}
        retour ["Shom"] = 0
        retour ["MareeInfo"] = 1
        return retour

    @staticmethod
    def getScrapper(i : int = 0) -> MareeScrapper : 
        logger.info("creation source de type: " + str(i))
        if i == 1:
            return MareeScrapperMareeInfo()
        return MareeScrapperShom()
        