from math import floor

from TrameNMEANav.CAngle import CAngle


class CLongitude(CAngle) :
    def __init__(self, uid: str):
        super().__init__(uid)

    @staticmethod
    def fromLongDecimalToStr(positionLongitudeDecimale: float) -> str:
        signe = "E" if positionLongitudeDecimale > 0 else "W"
        angle = abs(positionLongitudeDecimale)
        degre = floor(angle)
        minute = (angle - degre) * 60
        return "{degre:02d}°{minute:07.4f}' {signe:s}".format(degre=degre, minute=minute, signe=signe)