from __future__ import annotations

import math
import re
import copy

from enum import Enum, unique


from sfa_tools import cMyException
from .cAngle import cAngle, eAngleFormat


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
        s: str = data["longitude"]
        return cLongitude.fromString(s)

    @classmethod
    def fromString(cls, angleAsString: str = "") -> cLongitude:
        retour: cLongitude | None = None

        regex_dd1 = r"\s*([E|W])\s*(.*)"  # N 12.12°

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
        return "[cLongitude: " + self.toString() + "]"

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

    def __iadd__(self, val: cLongitude) -> cLongitude:
        if not isinstance(val, cLongitude):
            raise cMyException("longitude iadd type error")

        asDeg: float = self.longitudeEnDeg + val.longitudeEnDeg
        if math.fabs(asDeg) > 180.0:
            raise cMyException("longitude > 180.0")

        self.sensLongitude = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        self.angleAsDeg = math.fabs(asDeg)
        return self

    def __add__(self, val: cLongitude) -> cLongitude:
        if not isinstance(val, cLongitude):
            raise cMyException("longitude iadd type error")

        asDeg: float = self.longitudeEnDeg + val.longitudeEnDeg
        if math.fabs(asDeg) > 180.0:
            raise cMyException("longitude > 180.0")

        sens = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        angleAsDeg = math.fabs(asDeg)
        return cLongitude(valAsDeg=angleAsDeg, sens=sens)

    def __isub__(self, val: cLongitude) -> cLongitude:
        if not isinstance(val, cLongitude):
            raise cMyException("longitude iadd type error")

        asDeg: float = self.longitudeEnDeg - val.longitudeEnDeg
        if math.fabs(asDeg) > 180.0:
            raise cMyException("longitude > 180.0")

        self.sensLongitude = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        self.angleAsDeg = math.fabs(asDeg)
        return self

    def __sub__(self, val: cLongitude) -> cLongitude:
        if not isinstance(val, cLongitude):
            raise cMyException("longitude iadd type error")

        asDeg: float = self.longitudeEnDeg - val.longitudeEnDeg
        if math.fabs(asDeg) > 180.0:
            raise cMyException("longitude > 180.0")

        sens = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        angleAsDeg = math.fabs(asDeg)
        return cLongitude(valAsDeg=angleAsDeg, sens=sens)

    def __mul__(self, val: cLongitude | float) -> cLongitude:
        if not isinstance(val, (cLongitude, int, float)):
            raise cMyException("longitude iadd type error")

        asDeg: float = self.longitudeEnDeg
        if isinstance(val, cLongitude):
            asDeg *= val.longitudeEnDeg
        else:
            asDeg *= val

        if math.fabs(asDeg) > 180.0:
            raise cMyException("longitude > 180.0")

        sens = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        angleAsDeg = math.fabs(asDeg)
        return cLongitude(valAsDeg=angleAsDeg, sens=sens)

    def __imul__(self, val: cLongitude | float) -> cLongitude:
        if not isinstance(val, (cLongitude, int, float)):
            raise cMyException("longitude iadd type error")

        asDeg: float = self.longitudeEnDeg
        if isinstance(val, cLongitude):
            asDeg *= val.longitudeEnDeg
        else:
            asDeg *= val

        if math.fabs(asDeg) > 180.0:
            raise cMyException("longitude > 180.0")

        self.sensLongitude = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        self.angleAsDeg = math.fabs(asDeg)
        return self

    def __truediv__(self, val: cLongitude | float) -> cLongitude:
        if not isinstance(val, (cLongitude, int, float)):
            raise cMyException("longitude iadd type error")

        asDeg: float = self.longitudeEnDeg
        if isinstance(val, cLongitude):
            asDeg /= val.longitudeEnDeg
        else:
            asDeg /= val

        if math.fabs(asDeg) > 180.0:
            raise cMyException("longitude > 180.0")

        sens = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        angleAsDeg = math.fabs(asDeg)
        return cLongitude(valAsDeg=angleAsDeg, sens=sens)

    def __itruediv__(self, val: cLongitude | float) -> cLongitude:
        if not isinstance(val, (cLongitude, int, float)):
            raise cMyException("longitude iadd type error")

        asDeg: float = self.longitudeEnDeg
        if isinstance(val, cLongitude):
            asDeg /= val.longitudeEnDeg
        else:
            asDeg /= val

        if math.fabs(asDeg) > 180.0:
            raise cMyException("longitude > 180.0")

        self.sensLongitude = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        self.angleAsDeg = math.fabs(asDeg)
        return self

    def __copy__(self) -> cAngle:
        """
        normalise renvoie un angle compris entre 0 et 360
        Args:
            aucun
        Returns:
            self
        Raises:
            aucun
        """
        result = super().__copy__()
        result.sensLongitude = copy.copy(self.sensLongitude)
        return result

    def __deepcopy__(self, memodict={}) -> cAngle:
        """
        normalise renvoie un angle compris entre 0 et 360
        Args:
            aucun
        Returns:
            self
        Raises:
            aucun
        """
        result = super().__deepcopy__(memodict)
        result.sensLongitude = copy.deepcopy(self.sensLongitude, memodict)
        return result
