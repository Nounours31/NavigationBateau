from __future__ import annotations

import math
import re
import copy

from enum import Enum, unique


from pSfaTools.mMyException import cMyException
from .mAngle import cAngle, eAngleFormat


@unique
class eLongitudeSens(Enum):
    E = "Est"
    W = "Ouest"

    def __repr__(self) -> str:
        return "[eLongitudeSens.E]" if self is eLongitudeSens.E else "[eLongitudeSens.W]"

    def __str__(self) -> str:
        return "E" if self is eLongitudeSens.E else "W"

    def __float__(self) -> float:
        return +1.0 if self is eLongitudeSens.E else -1.0


class cLongitude(cAngle):
    NOM : str = "Longitude"

    def __init__(self, valAsDeg: float = 0.0, sens: eLongitudeSens | None = None):
        if math.fabs(valAsDeg) > 180.0:
            raise cMyException("Longitude invalide " + str(valAsDeg))

        super().__init__(math.fabs(valAsDeg))

        self._sens: eLongitudeSens = eLongitudeSens.E
        if sens is None:
            self._sens: eLongitudeSens = eLongitudeSens.E if valAsDeg >= 0.0 else eLongitudeSens.W
        else:
            self._sens: eLongitudeSens = sens

    @property
    def longitudeEnRad(self) -> float:
        return self.longitudeEnDeg * cAngle.DEG2RAD

    # Attention KO ne marche pas: https://stackoverflow.com/questions/10810369/python-super-and-setting-parent-class-property
    # @cAngle.valAsDeg.getter

    @property
    def longitudeEnDeg(self) -> float:
        return float(self.sensLongitude) * self._angleEnDeg

    @longitudeEnDeg.setter
    def longitudeEnDeg(self, val: float) -> None:
        if math.fabs(val) > 180.0:
            raise cMyException("Invalide longitude " + str(val))

        self.sensLongitude = eLongitudeSens.E if val >= 0.0 else eLongitudeSens.W
        # Attention KO ne marche pas: https://stackoverflow.com/questions/10810369/python-super-and-setting-parent-class-property
        # super().valAsDeg = math.fabs(val)
        self._angleEnDeg = math.fabs(val)
        return

    @property
    def sensLongitude(self) -> eLongitudeSens:
        return self._sens

    @sensLongitude.setter
    def sensLongitude(self, val: eLongitudeSens) -> None:
        self._sens = val

    @classmethod
    def fromDict(cls, data: dict = {}) -> cLongitude:
        s: str = data[cLongitude.NOM]
        return cLongitude.fromString(s)

    @classmethod
    def fromString(cls, angleAsString: str = "") -> cLongitude:
        retour: cLongitude | None = None

        regex_dd1 : re.Pattern[str] = re.compile(pattern=r"\s*([E|W|O])\s*(.*)", flags=re.IGNORECASE)  # N 12.12°
        regex_dd2 : re.Pattern[str] = re.compile(pattern=r"\s*(.*)\s*([E|W|O])\s*", flags=re.IGNORECASE)  # 12.12° N

        try:
            try:
                a: cAngle = cAngle.fromString(angleAsString)
                sens: eLongitudeSens = eLongitudeSens.E if a.angleAsDeg >= 0 else eLongitudeSens.W
                retour = cls(valAsDeg=math.fabs(a.angleAsDeg), sens=sens)

            except Exception:
                retour = None

            if retour is None:
                y = re.fullmatch(regex_dd1, angleAsString)
                if y:
                    sens: eLongitudeSens = (
                        eLongitudeSens.E if y.group(1) == "E" else eLongitudeSens.W
                    )
                    a: cAngle = cAngle.fromString(y.group(2))
                    retour = cls(valAsDeg=math.fabs(a.angleAsDeg), sens=sens)

                else:
                    y = re.fullmatch(regex_dd2, angleAsString)
                    if y:
                        sens: eLongitudeSens = (
                            eLongitudeSens.E if y.group(2) == "E" else eLongitudeSens.W
                        )
                        a: cAngle = cAngle.fromString(y.group(1))
                        retour = cls(valAsDeg=math.fabs(a.angleAsDeg), sens=sens)
                    else:
                        raise cMyException(f"Ce n'est pas une longitude >{angleAsString}<") from None

        except Exception as e:
            raise cMyException(str(e)) from e
        return retour

    def normalise(self) -> None:
        x = self.longitudeEnDeg
        if abs(x) > 180.0:
            raise cMyException("Longitude invalide")

        self.sensLongitude = eLongitudeSens.E
        if x < 0:
            self.sensLongitude = eLongitudeSens.W
            self.angleAsDeg = math.fabs(x)

    def __str__(self) -> str:
        return self.toString()

    def __repr__(self) -> str:
        return "["+cLongitude.NOM+": " + self.toString() + "]"

    def toString(self, format: eAngleFormat = eAngleFormat.DD) -> str:
        match format:
            case eAngleFormat.Debug:
                return (
                    f"{str(self.sensLongitude)} {super().toString(eAngleFormat.DD)} - "
                    f"{str(self.sensLongitude)} {super().toString(eAngleFormat.DMM)} "
                    f"[{str(self.sensLongitude)} {super().toString(eAngleFormat.DMS)}]"
                )
            case (
                eAngleFormat.DMM
                | eAngleFormat.DD
                | eAngleFormat.DMS
                | eAngleFormat.RAD
                | eAngleFormat.R8
            ):
                return f"{str(self.sensLongitude)} {super().toString(format)}"
            case _:
                return "Not implemented"

    def __iadd__(self, other: object) -> cLongitude:
        if not isinstance(other, (cLongitude, int, float)):
            raise cMyException("cLongitude iadd type error")
        
        y : float = other.longitudeEnDeg if isinstance(other, cLongitude) else other
        asDeg: float = self.longitudeEnDeg + y
        if math.fabs(asDeg) > 180.0:
            raise cMyException("| Longitude | > 180.0")

        self.sensLongitude = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        self.angleAsDeg = math.fabs(asDeg)
        return self

    def __add__(self, other: object) -> cLongitude:
        if not isinstance(other, (cLongitude, int, float)):
            raise cMyException("cLongitude iadd type error")

        y : float = other.longitudeEnDeg if isinstance(other, cLongitude) else other
        x : cLongitude = copy.deepcopy(self)
        x += y
        return x

    def __isub__(self, other: object) -> cLongitude:
        if not isinstance(other, (cLongitude, int, float)):
            raise cMyException("cLongitude iadd type error")

        y : float = other.longitudeEnDeg if isinstance(other, cLongitude) else other
        asDeg: float = self.longitudeEnDeg - y
        if math.fabs(asDeg) > 180.0:
            raise cMyException("| Longitude | > 180.0")

        self.sensLongitude = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        self.angleAsDeg = math.fabs(asDeg)
        return self

    def __sub__(self, other: object) -> cLongitude:
        if not isinstance(other, (cLongitude, int, float)):
            raise cMyException("cLongitude iadd type error")

        y : float = other.longitudeEnDeg if isinstance(other, cLongitude) else other
        x : cLongitude = copy.deepcopy(self)
        x -= y
        return x

    def __imul__(self, other: object) -> cLongitude:
        if not isinstance(other, (cLongitude, int, float)):
            raise cMyException("cLongitude iadd type error")

        y : float = other.longitudeEnDeg if isinstance(other, cLongitude) else other
        asDeg: float = self.longitudeEnDeg * y
        if math.fabs(asDeg) > 180.0:
            raise cMyException("| Longitude | > 180.0")

        self.sensLongitude = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        self.angleAsDeg = math.fabs(asDeg)
        return self

    def __mul__(self, other: object) -> cLongitude:
        if not isinstance(other, (cLongitude, int, float)):
            raise cMyException("cLongitude iadd type error")

        y : float = other.longitudeEnDeg if isinstance(other, cLongitude) else other
        x : cLongitude = copy.deepcopy(self)
        x *= y
        return x

    def __itruediv__(self, other: object) -> cLongitude:
        if not isinstance(other, (cLongitude, int, float)):
            raise cMyException("cLongitude iadd type error")

        y : float = other.longitudeEnDeg if isinstance(other, cLongitude) else other
        asDeg: float = self.longitudeEnDeg  / y 
        if math.fabs(asDeg) > 180.0:
            raise cMyException("| Longitude | > 180.0")

        self.sensLongitude = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        self.angleAsDeg = math.fabs(asDeg)
        return self



    def __truediv__(self, other: object) -> cLongitude:
        if not isinstance(other, (cLongitude, int, float)):
            raise cMyException("cLongitude iadd type error")

        y : float = other.longitudeEnDeg if isinstance(other, cLongitude) else other
        x : cLongitude = copy.deepcopy(self)
        x /= y
        return x

