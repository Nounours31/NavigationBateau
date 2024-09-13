import MareeScrapper
import MareeScrapperMareeInfo
import MareeScrapperShom

class MareeScrapperFactory ():
    def __init__(self) -> None:
        pass

    @staticmethod
    def getScrapper(i : int = 0) -> MareeScrapper : 
        if i == 1:
            return MareeScrapperMareeInfo()
        return MareeScrapperShom()
        