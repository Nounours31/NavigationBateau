import logging
import logging.config

import math

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("cAstro")

__DEG_2_RAD__: float = math.pi / 180.0
__RAD_2_DEG__: float = 180.0 / math.pi


class cAstro:
    def __init__(self):
        super.__init__()

    def dip(hauteurOeilEnMetre: float) -> float:
        return 0.0293 * math.sqrt(hauteurOeilEnMetre)

    def hauteurObservationEnDeg(hauteurInstrumentalEnDeg: float, collimationEnDeg: float) -> float:
        return hauteurInstrumentalEnDeg + collimationEnDeg

    def paralaxe(horizontaleParalaxeEnDeg: float, hauteurObservationEnDeg: float) -> float:
        return (
            math.asin(
                math.sin(horizontaleParalaxeEnDeg * __DEG_2_RAD__)
                * math.cos(hauteurObservationEnDeg * __DEG_2_RAD__)
            )
            * __RAD_2_DEG__
        )

    def refraction(
        hauteurObservationEnDeg: float,
        *,
        temperatureEnDeg: float = 10.0,
        pressionEnHPa: float = 1010.0,
    ) -> float:
        r0: float = 1 / math.tan(
            (hauteurObservationEnDeg + (7.31 / (hauteurObservationEnDeg + 4.4))) * __DEG_2_RAD__
        )
        f: float = 0.28 * pressionEnHPa / (273 + temperatureEnDeg)
        return r0 * f

    def diametre(visee: float, diamatreAstre: float) -> float:
        return visee * diamatreAstre

    def correction(dip: float, R: float, P: float, SD: float) -> float:
        return P - R - dip + SD


if __name__ == "__main__":
    print("cAstro")
