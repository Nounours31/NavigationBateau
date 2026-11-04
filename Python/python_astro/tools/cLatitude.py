import logging
import logging.config
import math
import re

from cAngle import cAngle
from cAstroError import cAstroError

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("cLatitude")


class cLatitude(cAngle):
    
    N = +1.0
    S = -1.0

    def __init__(self):
        super().__init__()
        self.__sens = cLatitude.N

    def parse(self, sVal : str):
        self._secondeDecimale = 0.0
        regexp = r"^([NS])\s*(.+)$"
        regexp2 = r"^([0-9°'\"\.]+)\s*([NS])$"

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
            if match.group(1) == "S" :
                signe = -1.0

            self._secondeDecimale = signe * self._parseAnglePositive(match.group(2))
            if abs(self._secondeDecimale) > (90.0 * 3600.0):
                raise cAstroError("latitude <= 90°")
            
            bMatch = True
            break
        if not bMatch:
            matches: list[re.Match[str]] = re.finditer(regexp2, sVal, re.NOFLAG)
            for _, match in enumerate(matches, start=1):
                signe : float = +1.0
                if match.group(2) == "S" :
                    signe = -1.0

                self._secondeDecimale = signe * self._parseAnglePositive(match.group(1))
                if abs(self._secondeDecimale) > (90.0 * 3600.0):
                    raise cAstroError("latitude <= 90°")

                bMatch = True
                break

        if not bMatch:
            raise cAstroError(f"String is not an latitude >{sVal}< attendu [N 10°23.12']")

        return self

    def toString(self, format : int = 1) -> str:
        toParse: float = self._secondeDecimale

        sign: str = "N "
        if toParse < 0.0:
            sign = "S "
            toParse = -1.0 * toParse

        if format == 0:
            return f"{sign:s}{cAngle._toString_sec(toParse, withSign= False)}"

        else:
            return f"{sign:s}{cAngle._toString_deg(toParse, withSign= False):s} [{sign:s}{cAngle._toString_min(toParse, withSign= False):s} # {sign:s}{cAngle._toString_sec(toParse, withSign= False):s}]"

if __name__ == "__main__":
    print("cLatitude")
    x : cLatitude = cLatitude()
