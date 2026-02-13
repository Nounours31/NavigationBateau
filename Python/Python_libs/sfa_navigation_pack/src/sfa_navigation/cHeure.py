from datetime import datetime


class cHeure:
    def __init__(self, uid: str) -> None:
        self.uid = uid

    def __str__(self) -> str:
        return f"uid {self.uid}"

    # ----------------------------------------------------------------------------------
    # doit retourner l'heure GPS au format
    #   hhmmss.ssss
    # ----------------------------------------------------------------------------------
    @staticmethod
    def heure2GPSDecimale(timestamp: float) -> float:
        now = datetime.fromtimestamp(timestamp)
        return ((now.hour * 100 + now.minute) * 100) + now.second + (now.microsecond / 1000000)
