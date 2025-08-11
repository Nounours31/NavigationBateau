import logging
import logging.config
import re

from cAngle import cAngle
from cAstroError import cAstroError

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("cLongitude")


class cLongitude(cAngle):
    W = -1.0
    E = 1.0
    
    def __init__(self):
        super().__init__()
        self.__sens = cLongitude.E

    def parse(self, sVal : str):
        self._secondeDecimale = 0.0
        regexp = r"^([WOE])\s*(.+)$"
        regexp2 = r"^([0-9°'\"\.]+)\s*([WOE])$"

        if not isinstance(sVal, str):
            raise cAstroError("No string to parse")

        if sVal is None:
            raise cAstroError("String is none")

        if len(sVal) == 0:
            raise cAstroError("String is empty")

        bMatch : bool = False
        matches: list[re.Match[str]] = re.finditer(regexp, sVal, re.NOFLAG)
        for _, match in enumerate(matches, start=1):
            signe : float = +1.0
            if match.group(1) == "W" or match.group(1) == "O" :
                signe = -1.0

            self._secondeDecimale = signe * self._parseAnglePositive(match.group(2))
            if abs(self._secondeDecimale) > (180.0 * 3600.0):
                raise cAstroError("longitude <= 180°")
            
            bMatch = True
            break
        if not bMatch:
            matches: list[re.Match[str]] = re.finditer(regexp2, sVal, re.NOFLAG)
            for _, match in enumerate(matches, start=1):
                signe : float = +1.0
                if match.group(2) == "W" or match.group(2) == "O" :
                    signe = -1.0

                self._secondeDecimale = signe * self._parseAnglePositive(match.group(1))
                if abs(self._secondeDecimale) > (180.0 * 3600.0):
                    raise cAstroError("longitude <= 180°")

                bMatch = True
                break

        if not bMatch:
            raise cAstroError(f"String is not an angle {sVal}")

        return self
 
    def toString(self, format : int = 1) -> str:
        toParse: float = self._secondeDecimale

        sign: str = "E "
        if toParse < 0.0:
            sign = "W "
            toParse = -1.0 * toParse

        if format == 0:
            return f"{sign:s}{cAngle._toString_sec(toParse, withSign= False)}"

        else:
            return f"{sign:s}{cAngle._toString_deg(toParse, withSign= False):s} [{sign:s}{cAngle._toString_min(toParse, withSign= False):s} # {sign:s}{cAngle._toString_sec(toParse, withSign= False):s}]"

if __name__ == "__main__":
    print("cLongitude")
