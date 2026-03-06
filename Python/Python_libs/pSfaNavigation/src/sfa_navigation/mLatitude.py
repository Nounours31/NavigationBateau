from __future__ import annotations

import math
import re
import copy

from enum import Enum, unique

from pSfaTools.mMyException import cMyException
from .mAngle import cAngle, eAngleFormat


@unique
class eLatitudeSens(Enum):
    S = "Sud"
    N = "Nord"

    def __repr__(self) -> str:
        return "[eLatitudeSens.N]" if self is eLatitudeSens.N else "[eLatitudeSens.S]"

    def __str__(self) -> str:
        return "N" if self is eLatitudeSens.N else "S"

    def __float__(self) -> float:
        return +1.0 if self is eLatitudeSens.N else -1.0


class cLatitude(cAngle):
    NOM : str = "Latitude"

    def __init__(self, valAsDeg: float = 0.0, sens: eLatitudeSens | None = None):
        if math.fabs(valAsDeg) > 90.0:
            raise cMyException("Latitude invalide " + str(valAsDeg))

        super().__init__(math.fabs(valAsDeg))

        self._sens: eLatitudeSens = eLatitudeSens.N
        if sens is None:
            self._sens: eLatitudeSens = eLatitudeSens.N if valAsDeg >= 0.0 else eLatitudeSens.S
        else:
            self._sens: eLatitudeSens = sens

    # Attention KO ne marche pas: https://stackoverflow.com/questions/10810369/python-super-and-setting-parent-class-property

    @property
    def latitudeEnRad(self) -> float:
        return self.latitudeEnDeg * cAngle.DEG2RAD

    @property
    def latitudeEnDeg(self) -> float:
        return float(self.sensLatitude) * self._angleEnDeg

    @latitudeEnDeg.setter
    def latitudeEnDeg(self, val: float) -> None:
        if math.fabs(val) > 90.0:
            raise cMyException("Invalide latitude " + str(val))

        self.sensLatitude = eLatitudeSens.N if val >= 0.0 else eLatitudeSens.S
        # Attention KO ne marche pas: https://stackoverflow.com/questions/10810369/python-super-and-setting-parent-class-property
        # super().valAsDeg = math.fabs(val)
        self._angleEnDeg = math.fabs(val)
        return

    @property
    def sensLatitude(self) -> eLatitudeSens:
        return self._sens

    @sensLatitude.setter
    def sensLatitude(self, val: eLatitudeSens) -> None:
        self._sens = val

    def gudermannInverse(self):
        latitudeEnRad: float = self.latitudeEnRad
        return math.log(math.tan((math.pi / 4.0) + (latitudeEnRad / 2.0)))
    
    def normalise(self) -> None:
        x = self.latitudeEnDeg
        if abs(x) > 90.0:
            raise cMyException("Latitude invalide")

        self.sensLatitude = eLatitudeSens.N
        if x < 0:
            self.angleAsDeg = math.fabs(x)
            self.sensLatitude = eLatitudeSens.S

    @classmethod
    def fromDict(cls, data: dict = {}) -> cLatitude:
        s: str = data[cLatitude.NOM]
        return cLatitude.fromString(s)

    @classmethod
    def fromString(cls, angleAsString: str = "") -> cLatitude:
        retour: cLatitude | None = None

        regex_dd1 = r"\s*([N|S])\s*(.*)"  # N 12.12°
        regex_dd2 = r"\s*(.*)\s*([N|S])\s*"  # 12.12° N

        try:
            try:
                a: cAngle = cAngle.fromString(angleAsString)
                sens: eLatitudeSens = eLatitudeSens.N if a.angleAsDeg >= 0 else eLatitudeSens.S
                retour = cls(valAsDeg=math.fabs(a.angleAsDeg), sens=sens)

            except Exception:
                retour = None

            if retour is None:
                y = re.fullmatch(regex_dd1, angleAsString)
                if y:
                    sens: eLatitudeSens = eLatitudeSens.N if y.group(1) == "N" else eLatitudeSens.S
                    a: cAngle = cAngle.fromString(y.group(2))
                    retour = cls(valAsDeg=math.fabs(a.angleAsDeg), sens=sens)

                else:
                    y = re.fullmatch(regex_dd2, angleAsString)
                    if y:
                        sens: eLatitudeSens = eLatitudeSens.N if y.group(2) == "N" else eLatitudeSens.S
                        a: cAngle = cAngle.fromString(y.group(1))
                        retour = cls(valAsDeg=math.fabs(a.angleAsDeg), sens=sens)   
                    else:
                        raise cMyException(f"Ce n'est pas une latitude >{angleAsString}<") from None

        except Exception as e:
            raise cMyException(str(e)) from e
        return retour

    def toString(self, format: eAngleFormat = eAngleFormat.DD) -> str:
        match format:
            case eAngleFormat.Debug:
                return (
                    f"{str(self.sensLatitude)} {super().toString(eAngleFormat.DD)} - "
                    f"{str(self.sensLatitude)} {super().toString(eAngleFormat.DMM)} "
                    f"[{str(self.sensLatitude)} {super().toString(eAngleFormat.DMS)}]"
                )
            case (
                eAngleFormat.DMM
                | eAngleFormat.DD
                | eAngleFormat.DMS
                | eAngleFormat.RAD
                | eAngleFormat.R8
            ):
                return f"{str(self.sensLatitude)} {super().toString(format)}"
            case _:
                return "Not implemented"

    def __str__(self) -> str:
        return self.toString()

    def __repr__(self) -> str:
        return "["+cLatitude.NOM+": " + self.toString() + "]"


    def __iadd__(self, other: object) -> cLatitude:
        if not isinstance(other, (cLatitude, int, float)):
            raise cMyException("latitude iadd type error")

        y : float = other.latitudeEnDeg if isinstance(other, cLatitude) else other

        asDeg: float = self.latitudeEnDeg + y 
        if math.fabs(asDeg) > 90.0:
            raise cMyException("latitude > 90.0")

        self.sensLatitude = eLatitudeSens.N if asDeg >= 0.0 else eLatitudeSens.S
        self.angleAsDeg = math.fabs(asDeg)
        return self

    def __add__(self, other: object) -> cLatitude:
        if not isinstance(other, (cLatitude, int, float)):
            raise cMyException("latitude iadd type error")

        y : float = other.latitudeEnDeg if isinstance(other, cLatitude) else other

        x : cLatitude = copy.deepcopy(self)
        x += y
        return x

    def __isub__(self, other: object) -> cLatitude:
        if not isinstance(other, (cLatitude, int, float)):
            raise cMyException("latitude iadd type error")

        y : float = other.latitudeEnDeg if isinstance(other, cLatitude) else other

        asDeg: float = self.latitudeEnDeg  - y 
        if math.fabs(asDeg) > 90.0:
            raise cMyException("latitude > 90.0")

        self.sensLatitude = eLatitudeSens.N if asDeg >= 0.0 else eLatitudeSens.S
        self.angleAsDeg = math.fabs(asDeg)
        return self

    def __sub__(self, other: object) -> cLatitude:
        if not isinstance(other, (cLatitude, int, float)):
            raise cMyException("latitude iadd type error")

        y : float = other.latitudeEnDeg if isinstance(other, cLatitude) else other

        x : cLatitude = copy.deepcopy(self)
        x -= y
        return x


    def __imul__(self, other: object) -> cLatitude:
        if not isinstance(other, (cLatitude, int, float)):
            raise cMyException("latitude iadd type error")

        y : float = other.latitudeEnDeg if isinstance(other, cLatitude) else other

        asDeg: float = self.latitudeEnDeg  * y 
        if math.fabs(asDeg) > 90.0:
            raise cMyException("latitude > 90.0")

        self.sensLatitude = eLatitudeSens.N if asDeg >= 0.0 else eLatitudeSens.S
        self.angleAsDeg = math.fabs(asDeg)
        return self

    def __mul__(self, other: object) -> cLatitude:
        if not isinstance(other, (cLatitude, int, float)):
            raise cMyException("latitude iadd type error")

        y : float = other.latitudeEnDeg if isinstance(other, cLatitude) else other

        x : cLatitude = copy.deepcopy(self)
        x *= y
        return x


    def __itruediv__(self, other: object) -> cLatitude:
        if not isinstance(other, (cLatitude, int, float)):
            raise cMyException("latitude iadd type error")

        y : float = other.latitudeEnDeg if isinstance(other, cLatitude) else other
        asDeg: float = self.latitudeEnDeg / y
        if math.fabs(asDeg) > 90.0:
            raise cMyException("latitude > 90.0")

        self.sensLatitude = eLatitudeSens.N if asDeg >= 0.0 else eLatitudeSens.S
        self.angleAsDeg = math.fabs(asDeg)
        return self

    def __truediv__(self, other: object) -> cLatitude:
        if not isinstance(other, (cLatitude, int, float)):
            raise cMyException("latitude iadd type error")

        y : float = other.latitudeEnDeg if isinstance(other, cLatitude) else other
        x : cLatitude = copy.deepcopy(self)
        x /= y
        return x

