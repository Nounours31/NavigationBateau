from datetime import datetime
from math import cos, floor


class CHeure:
    def __init__(self, uid : str) -> None:
        self.uid = uid

    def __str__(self) -> str :
        return f"uid {self.uid}"

    # ----------------------------------------------------------------------------------
    # doit retourner l'heure GPS au format
    #   hhmmss.ssss
    # ----------------------------------------------------------------------------------
    @staticmethod
    def heure2GPSDecimale(now: datetime) -> float:
        return ((now.hour * 100 + now.minute) * 100) + now.second + (now.microsecond / 1000000)
