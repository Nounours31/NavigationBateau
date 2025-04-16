from math import cos, floor

from pkg_resources import non_empty_lines

from TrameNMEANav.CLogger import CLogger


class CAngle:
    logger : CLogger = None

    def __init__(self, *,
                 angleDegreDouble : float = None,
                 angleDegreInt : int = None,
                 angleMinuteInt : int = None,
                 angleSecondeInt : int = None,
                 angleMinuteDouble : float = None)  :

        logger = CLogger.getLogger(None, None, None)

        test : [bool] = [False, False, False]
        test[0] = not angleDegreDouble is None
        test[1] = (not angleDegreInt is None) and (not angleMinuteDouble is None)
        test[2] = (not angleDegreInt is None) and (not angleMinuteInt is None) and (not angleSecondeInt is None)

        if not (test[0] or test[1] or test[2]):
            logger.error("Invalide init d'un angle ")
            self.angle = 0.0

        if test[0]:
            self.angle = angleDegreDouble

        if (not test[0]) and test[1]:
            self.angle = angleDegreInt + angleMinuteDouble / 60.0

        if (not test[0]) and (not test[1]) and test[2]:
            self.angle = angleDegreInt + angleMinuteDouble / 60.0 + angleSecondeInt / 3600.0



    def __str__(self) -> str :
        return f"uid {self.uid}"

    # ----------------------------------------------------------------------------------
    # doit retourner un angle en degre decimal
    # ----------------------------------------------------------------------------------
    @staticmethod
    def angleSexaToDecimal(degre: int, minute: float) -> float:
        x = 1 if degre >= 0 else -1
        return x * (abs(degre) + abs(minute) / 60)


    # ----------------------------------------------------------------------------------
    # doit retourner un angle en degre decimal / minute sexagedecimal
    # ----------------------------------------------------------------------------------
    @staticmethod
    def angleDecimalToMinuteSexa(degreDecimal: float) -> float:
        x = 1 if degreDecimal >= 0 else -1
        y = abs(degreDecimal)
        degre = floor(y)
        minute = (y - degre) * 60 / 100
        return x * (degre + minute)
