from __future__ import annotations
from datetime import datetime, timezone
from uuid import uuid4


class cHeure:
    def __init__(self, uid: str = "", timestampUTC : int = 0) -> None:
        if uid :
            self._uid = uid
        else:
            self._uid = uuid4().__str__()
        if timestampUTC > 0:
            self._dt = (datetime.fromtimestamp(timestampUTC,tz=timezone.utc))
        else:
            self._dt = datetime.now(tz=timezone.utc)

    @property
    def uid(self) -> str:
        return self._uid

    @property
    def ts(self) -> float:
        return self._dt.replace(tzinfo=timezone.utc).timestamp()

    @property
    def heure(self) -> datetime:
        return self._dt

    def __str__(self) -> str:
        return f"uid: {self._uid} - timeStamp: {self._dt}"

    def toString(self) -> str:
        return f"[cHeure {self.__str__()}]"

    @classmethod
    def fromTimeStamp(cls, timestampUTC: int) -> cHeure:
        x : cHeure = cHeure(timestampUTC=timestampUTC)
        return x

    # ----------------------------------------------------------------------------------
    # doit retourner l'heure GPS au format
    #   hhmmss.ssss
    # ----------------------------------------------------------------------------------
    def toHeure2GPSDecimale(self) -> float:
        now = self._dt
        return ((now.hour * 100 + now.minute) * 100) + now.second + (now.microsecond / 1000000)

