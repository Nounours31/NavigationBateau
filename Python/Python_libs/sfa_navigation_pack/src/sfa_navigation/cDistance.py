from __future__ import annotations
import math


class cDistance():
    def __init__(self, valAsMilleNautique : float = 0.0):
        self.__valAsMilleNautique = valAsMilleNautique
        self._normalize()

    @property
    def val(self) -> float:
        return self.__valAsMilleNautique


    @val.setter
    def val(self, x:float) -> None:
        self.__valAsMilleNautique = x




    def _normalize(self) -> None:
        x = self.__valAsMilleNautique
        if x < 0:
            x = math.fabs(x)

        self.__valAsMilleNautique = x

    
            