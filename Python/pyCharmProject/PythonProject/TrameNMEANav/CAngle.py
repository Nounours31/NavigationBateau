from datetime import datetime
from math import cos, floor


class CAngle:
    def __init__(self, uid : str) -> None:
        self.uid = uid

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
