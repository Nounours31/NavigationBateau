import re
from logging import Logger
from math import floor
from re import Match

from TrameNMEANav.CAngle import CAngle
from TrameNMEANav.CLogger import CLogger


class CLatitude(CAngle):
    _logger : Logger = CLogger.getLogger("CLatitude")
    def __init__(self, val: float):
        super().__init__(angleDegreDouble=val)
        self._logger: Logger = CLogger.getLogger("CLatitude")
        CLatitude._logger = self._logger

    def __str__(self) -> str:
        sens = "N"
        asDouble, signe = self.toDecimal(True)
        if signe < 0:
            sens = "S"
        asMinuteDecimale, signe =  self.toDegMinDecimal(True)
        asMinuteSexaDecimale, signe =  self.toDegMinSecDecimal(True)
        return f"{asDouble} {sens} [{asMinuteDecimale}{sens} - {asMinuteSexaDecimale}{sens}]"

    def __format__(self, format_spec):
        return str(self)

    @staticmethod
    def fromString(s: str):
        retour: CLatitude = CLatitude(0.0)
        regexLatitude = r"([\-\+\.0-9°'\"]+)\s*([NnSs])"

        parseError: bool = False
        m: Match[str] = re.match(regexLatitude, s, re.MULTILINE)
        if m:
            maxGroup = len(m.groups())
            if maxGroup != 2:
                parseError = True
            else:
                signe: float = 1.0
                if m.group(2).lower() == "s":
                    signe = -1.0

                ang: CAngle = CAngle.fromString(m.group(1))
                retour.value(signe * abs(ang.value()))
                CLatitude._logger.debug(f"{retour}")

        else:
            parseError = True

        if parseError:
            CLatitude._logger.error(f"Ce n'est pas une latitude {s}s")

        return retour

    def sens(self) -> str:
        return "N" if self.value() > 0 else "S"
