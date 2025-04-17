import re
from logging import Logger
from math import floor

from TrameNMEANav.CAngle import CAngle
from TrameNMEANav.CLogger import CLogger


class CLongitude(CAngle):
    _logger = None
    def __init__(self, val: float):
        super().__init__(angleDegreDouble=val)
        self._logger: Logger = CLogger.getLogger("CLongitude")
        CLongitude._logger = self._logger

    def __str__(self) -> str:
        sens = "E"
        asDouble, signe = self.toDecimal(True)
        if signe < 0:
            sens = "W"
        asMinuteDecimale, signe =  self.toDegMinDecimal(True)
        asMinuteSexaDecimale, signe =  self.toDegMinSecDecimal(True)
        return f"{asDouble} {sens} [{asMinuteDecimale}{sens} - {asMinuteSexaDecimale}{sens}]"

    def __format__(self, format_spec):
        return str(self)


    @staticmethod
    def fromString(s: str):
        retour: CLongitude = CLongitude(0.0)
        regexLatitude = r"([\-\+\.0-9°'\"]+)\s*([EeWwOo])"

        parseError: bool = False
        m: re.Match[str] = re.match(regexLatitude, s, re.MULTILINE)
        if m:
            maxGroup = len(m.groups())
            if maxGroup != 2:
                parseError = True
            else:
                signe: float = 1.0
                if m.group(2).lower() == "w" or m.group(2).lower() == "o":
                    signe = -1.0

                ang: CAngle = CAngle.fromString(m.group(1))
                retour.value(signe * abs(ang.value()))
                CLongitude._logger.debug(f"{retour}")

        else:
            parseError = True

        if parseError:
            CLongitude._logger.error(f"Ce n'est pas une latitude {s}s")

        return retour

    def sens(self) -> str:
        return "E" if self.value() > 0 else "W"
