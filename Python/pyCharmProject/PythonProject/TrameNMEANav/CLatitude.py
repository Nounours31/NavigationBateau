from math import floor

from TrameNMEANav.CAngle import CAngle


class CLatitude(CAngle) :
    def __init__(self, uid: str):
        super().__init__(uid)

    @staticmethod
    def fromLatDecimalToStr(positionLatitudeDecimale: float) -> str:
        signe = "N" if positionLatitudeDecimale > 0 else "S"
        angle = abs(positionLatitudeDecimale)
        degre = floor(angle)
        minute = (angle - degre) * 60
        return "{degre:02d}°{minute:07.4f}' {signe:s}".format(degre=degre, minute=minute, signe=signe)