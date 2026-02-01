from __future__ import annotations

import math
import re
import copy

from enum import Enum, unique

from docutils.frontend import validate_encoding_and_error_handler
from sfa_tools import cMyException
from .cAngle import cAngle,eAngleFormat

@unique
class eLongitudeSens(Enum):
    E = "Est"
    W = "Ouest"

    def __repr__(self) -> str:
        return "[eLongitudeSens.E]" if self is eLongitudeSens.E else "[eLongitudeSens.W]"

    def __str__ (self) -> str:
        return "E" if self is eLongitudeSens.E else "W"

    def __float__(self) -> float:
        return +1.0 if self is eLongitudeSens.E else -1.0


class cLongitude (cAngle):

    def __init__(self, valAsDeg : float = 0.0, sens : eLongitudeSens | None = None):
        if math.fabs(valAsDeg) > 180.0:
            raise cMyException("Longitude invalide " + str(valAsDeg))

        super().__init__(math.fabs(valAsDeg))

        self._sens: eLongitudeSens = eLongitudeSens.E
        if sens is None:
            self._sens : eLongitudeSens = eLongitudeSens.E if valAsDeg >= 0.0 else eLongitudeSens.W
        else:
            self._sens: eLongitudeSens = sens

    # Attention KO ne marche pas: https://stackoverflow.com/questions/10810369/python-super-and-setting-parent-class-property
    @cAngle.valAsDeg.getter
    def valAsDeg(self) -> float:
        raise NotImplemented

    @cAngle.valAsDeg.setter
    def valAsDeg(self, val:float) -> None:
        """
        if math.fabs(val) > 180.0:
            raise cMyException("Invalide longitude " + str(val))

        self._sens = eLongitudeSens.E if val >= 0.0 else eLongitudeSens.W
        # Attention KO ne marche pas: https://stackoverflow.com/questions/10810369/python-super-and-setting-parent-class-property
        # super().valAsDeg = math.fabs(val)
        self._angleEnDeg = math.fabs(val)
        """
        raise NotImplemented

    @property
    def val(self) -> float:
        return float(self._sens) * self._angleEnDeg

    @val.setter
    def val(self, val:float) -> None:
        if math.fabs(val) > 180.0:
            raise cMyException("Invalide longitude " + str(val))

        self._sens = eLongitudeSens.E if val >= 0.0 else eLongitudeSens.W
        # Attention KO ne marche pas: https://stackoverflow.com/questions/10810369/python-super-and-setting-parent-class-property
        # super().valAsDeg = math.fabs(val)
        self._angleEnDeg = math.fabs(val)
        return

    @property
    def sens(self) -> eLongitudeSens:
        return self._sens

    @sens.setter
    def sens(self, val: eLongitudeSens) -> None:
        self._sens = val


    @staticmethod
    def fromString(angleAsString : str = "") -> cLongitude:
        retour : cLongitude | None = None

        regex_dd1 = r"\s*([E|W])\s*(.*)" # N 12.12°

        try:
            try:
                a : cAngle = cAngle.fromString(angleAsString)
                sens : eLongitudeSens = eLongitudeSens.E if a.valAsDeg >= 0 else eLongitudeSens.W
                retour = cLongitude (valAsDeg=math.fabs(a.valAsDeg), sens=sens)

            except Exception as e:
                retour = None

            if retour is None:
                y = re.fullmatch(regex_dd1, angleAsString)
                if y:
                    sens : eLongitudeSens = eLongitudeSens.E  if y.group(1) == "E" else eLongitudeSens.W
                    a : cAngle = cAngle.fromString(y.group(2))
                    retour = cLongitude (valAsDeg=math.fabs(a.valAsDeg), sens=sens)

                else :
                    raise cMyException(f"Ce n'est pas une longitude >{angleAsString}<")

        except Exception as e:
            raise cMyException (str(e))
        return retour

    def normalise(self) -> None:
        x = self.val
        if abs(x) > 180.0:
            raise cMyException("Longitude invalide")

        self._sens = eLongitudeSens.E
        if x < 0:
            self._sens = eLongitudeSens.W
        self._angleEnDeg = math.fabs(x)


    def __str__(self) -> str:
        return self.toString()

    def __repr__(self) -> str:
        return "[cLongitude: " + self.toString() + "]"

    def toString(self, format: eAngleFormat = eAngleFormat.DD) -> str:
         match format:
            case eAngleFormat.Debug:
                return f"{str(self._sens)} {super().toString(eAngleFormat.DD)} - {str(self._sens)} {super().toString(eAngleFormat.DMM)} [{str(self._sens)} {super().toString(eAngleFormat.DMS)}]"
            case eAngleFormat.DMM | eAngleFormat.DD | eAngleFormat.DMS | eAngleFormat.RAD | eAngleFormat.R8:
                return f"{str(self._sens)} {super().toString(format)}"
            case _:
                return  "Not implemented"


    def __iadd__(self, val: cLongitude) -> cLongitude:
        if not isinstance(val, cLongitude):
            raise cMyException("longitude iadd type error")

        asDeg : float = self.val + val.val
        if math.fabs(asDeg) > 180.0 :
            raise cMyException("longitude > 180.0")

        self._sens = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        self._angleEnDeg = math.fabs(asDeg)
        return self


    def __add__(self, val : cLongitude) -> cLongitude:
        if not isinstance(val, cLongitude):
            raise cMyException("longitude iadd type error")

        asDeg: float = self.val + val.val
        if math.fabs(asDeg) > 180.0:
            raise cMyException("longitude > 180.0")

        sens = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        angleEnDeg = math.fabs(asDeg)
        return cLongitude (valAsDeg=angleEnDeg, sens=sens)

    def __isub__(self, val: cLongitude) -> cLongitude:
        if not isinstance(val, cLongitude):
            raise cMyException("longitude iadd type error")

        asDeg: float = self.val - val.val
        if math.fabs(asDeg) > 180.0:
            raise cMyException("longitude > 180.0")

        self._sens = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        self._angleEnDeg = math.fabs(asDeg)
        return self

    def __sub__(self, val: cLongitude) -> cLongitude:
        if not isinstance(val, cLongitude):
            raise cMyException("longitude iadd type error")

        asDeg: float = self.val - val.val
        if math.fabs(asDeg) > 180.0:
            raise cMyException("longitude > 180.0")

        sens = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        angleEnDeg = math.fabs(asDeg)
        return cLongitude(valAsDeg=angleEnDeg, sens=sens)


    def __mul__(self, val : cLongitude | float) -> cLongitude:
        if not isinstance(val, (cLongitude, int, float)):
            raise cMyException("longitude iadd type error")

        asDeg: float = self.val
        if isinstance(val, cLongitude):
            asDeg *= val.val
        else:
            asDeg *= val

        if math.fabs(asDeg) > 180.0:
            raise cMyException("longitude > 180.0")

        sens = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        angleEnDeg = math.fabs(asDeg)
        return cLongitude (valAsDeg=angleEnDeg, sens=sens)

    def __imul__(self, val : cLongitude | float) -> cLongitude:
        if not isinstance(val, (cLongitude, int, float)):
            raise cMyException("longitude iadd type error")

        asDeg: float = self.val
        if isinstance(val, cLongitude):
            asDeg *= val.val
        else:
            asDeg *= val

        if math.fabs(asDeg) > 180.0:
            raise cMyException("longitude > 180.0")

        self._sens = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        self._angleEnDeg = math.fabs(asDeg)
        return self

    def __truediv__(self, val : cLongitude | float) -> cLongitude:
        if not isinstance(val, (cLongitude, int, float)):
            raise cMyException("longitude iadd type error")

        asDeg: float = self.val
        if isinstance(val, cLongitude):
            asDeg /= val.val
        else:
            asDeg /= val

        if math.fabs(asDeg) > 180.0:
            raise cMyException("longitude > 180.0")

        sens = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        angleEnDeg = math.fabs(asDeg)
        return cLongitude (valAsDeg=angleEnDeg, sens=sens)

    def __itruediv__(self, val : cLongitude | float) -> cLongitude:
        if not isinstance(val, (cLongitude, int, float)):
            raise cMyException("longitude iadd type error")

        asDeg: float = self.val
        if isinstance(val, cLongitude):
            asDeg /= val.val
        else:
            asDeg /= val

        if math.fabs(asDeg) > 180.0:
            raise cMyException("longitude > 180.0")

        self._sens = eLongitudeSens.E if asDeg >= 0.0 else eLongitudeSens.W
        self._angleEnDeg = math.fabs(asDeg)
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
        result._sens = copy.copy(self._sens)
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
        result._sens = copy.deepcopy(self._sens, memodict)
        return result


