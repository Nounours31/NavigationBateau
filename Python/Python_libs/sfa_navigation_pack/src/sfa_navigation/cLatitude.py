from __future__ import annotations

import math
import re
from enum import Enum, unique

from docutils.frontend import validate_encoding_and_error_handler
from sfa_tools import cMyException
from .cAngle import cAngle,eAngleFormat

@unique
class eLatitudeSens(Enum):
    S = "Sud"
    N = "Nord"

    def __repr__(self) -> str:
        return "[eLatitudeSens.N]" if self is eLatitudeSens.N else "[eLatitudeSens.S]"

    def __str__ (self) -> str:
        return "N" if self is eLatitudeSens.N else "S"

    def __float__(self) -> float:
        return +1.0 if self is eLatitudeSens.N else -1.0


class cLatitude (cAngle):

    def __init__(self, valAsDeg : float = 0.0, sens : eLatitudeSens | None = None):
        if math.fabs(valAsDeg) > 90.0:
            raise cMyException("Latitude invalide " + str(valAsDeg))

        super().__init__(math.fabs(valAsDeg))

        self._sens: eLatitudeSens = eLatitudeSens.N
        if sens is None:
            self._sens : eLatitudeSens = eLatitudeSens.N if valAsDeg >= 0.0 else eLatitudeSens.S
        else:
            self._sens: eLatitudeSens = sens


    @property
    def val(self) -> float:
        return float(self._sens) * super().valAsDeg

    @val.setter
    def val(self,val:float) -> None:
        if math.fabs(val) > 90.0:
            raise cMyException("Invalide latitude " + str(val))

        self._sens = eLatitudeSens.N if val >= 0.0 else eLatitudeSens.S
        self.valAsDeg = math.fabs(val)

    @staticmethod
    def fromString(angleAsString : str = "") -> cLatitude:
        retour : cLatitude | None = None

        regex_dd1 = r"\s*([N|S])\s*(.*)" # N 12.12°
        regex_dd2 = r"\s*([\+|\-])\s*(.*)" # N 12.12°

        try:
            try:
                a : cAngle = cAngle.fromString(angleAsString)
                sens : eLatitudeSens = eLatitudeSens.N if a.valAsDeg >= 0 else eLatitudeSens.S
                retour = cLatitude (valAsDeg=math.fabs(a.valAsDeg), sens=sens)

            except Exception as e:
                retour = None

            if retour is None:
                y = re.fullmatch(regex_dd1, angleAsString)
                if y:
                    sens : eLatitudeSens = eLatitudeSens.N  if y.group(1) == "N" else eLatitudeSens.S
                    a : cAngle = cAngle.fromString(y.group(2))
                    retour = cLatitude (valAsDeg=math.fabs(a.valAsDeg), sens=sens)

                else :
                    y = re.fullmatch(regex_dd2, angleAsString)
                    if y:
                        sens: eLatitudeSens = eLatitudeSens.N if y.group(1) == "N" else eLatitudeSens.S
                        a: cAngle = cAngle.fromString(y.group(2))
                        retour = cLatitude(valAsDeg=math.fabs(a.valAsDeg), sens=sens)

                    else:
                        raise cMyException(f"Ce n'est pas une latitude >{angleAsString}<")

        except Exception as e:
            raise cMyException (str(e))
        return retour

    def normalise(self) -> None:
        x = self.val * float(self._sens)
        if abs(x) > 90.0:
            raise cMyException("Latitude invalide")

        self._sens = eLatitudeSens.N
        if x < 0:
            self._sens = eLatitudeSens.S
        self.valAsDeg = math.fabs(x)


    def __str__(self) -> str:
        return self.toString()

    def __repr__(self) -> str:
        return "[cLatitude: " + self.toString() + "]"

    def toString(self, format: eAngleFormat = eAngleFormat.DD) -> str:
        if format == eAngleFormat.Debug:
            return f"{str(self._sens)} {super().toString(eAngleFormat.DD)} - {str(self._sens)} {super().toString(eAngleFormat.DMM)} [{str(self._sens)} {super().toString(eAngleFormat.DMS)}]"
        if format == eAngleFormat.DMM:
            return f"{str(self._sens)} {super().toString(eAngleFormat.DMM)}"
        return ""


    def __iadd__(self, val: cLatitude) -> cLatitude:
        if not isinstance(val, cLatitude):
            raise cMyException("latitude iadd type error")

        asDeg : float = self.val + val.val
        if math.fabs(asDeg) > 90.0 :
            raise cMyException("latitude > 90.0")

        self._sens = eLatitudeSens.N if asDeg >= 0.0 else eLatitudeSens.S
        self._angleEnDeg = math.fabs(asDeg)
        return self


    def __add__(self, val : cLatitude) -> cLatitude:
        if not isinstance(val, cLatitude):
            raise cMyException("latitude iadd type error")

        asDeg: float = self.val + val.val
        if math.fabs(asDeg) > 90.0:
            raise cMyException("latitude > 90.0")

        sens = eLatitudeSens.N if asDeg >= 0.0 else eLatitudeSens.S
        angleEnDeg = math.fabs(asDeg)
        return cLatitude (valAsDeg=angleEnDeg, sens=sens)

    def __isub__(self, val: cLatitude) -> cLatitude:
        if not isinstance(val, cLatitude):
            raise cMyException("latitude iadd type error")

        asDeg: float = self.val - val.val
        if math.fabs(asDeg) > 90.0:
            raise cMyException("latitude > 90.0")

        self._sens = eLatitudeSens.N if asDeg >= 0.0 else eLatitudeSens.S
        self._angleEnDeg = math.fabs(asDeg)
        return self

    def __sub__(self, val: cLatitude) -> cLatitude:
        if not isinstance(val, cLatitude):
            raise cMyException("latitude iadd type error")

        asDeg: float = self.val - val.val
        if math.fabs(asDeg) > 90.0:
            raise cMyException("latitude > 90.0")

        sens = eLatitudeSens.N if asDeg >= 0.0 else eLatitudeSens.S
        angleEnDeg = math.fabs(asDeg)
        return cLatitude(valAsDeg=angleEnDeg, sens=sens)


    def __mul__(self, val : cLatitude | float) -> cLatitude:
        if not isinstance(val, (cLatitude, int, float)):
            raise cMyException("latitude iadd type error")

        asDeg: float = self.val
        if isinstance(val, cLatitude):
            asDeg *= val.valAsDeg
        else:
            asDeg *= val

        if math.fabs(asDeg) > 90.0:
            raise cMyException("latitude > 90.0")

        sens = eLatitudeSens.N if asDeg >= 0.0 else eLatitudeSens.S
        angleEnDeg = math.fabs(asDeg)
        return cLatitude (valAsDeg=angleEnDeg, sens=sens)

    def __imul__(self, val : cLatitude | float) -> cLatitude:
        if not isinstance(val, (cLatitude, int, float)):
            raise cMyException("latitude iadd type error")

        asDeg: float = self.val
        if isinstance(val, cLatitude):
            asDeg *= val.valAsDeg
        else:
            asDeg *= val

        if math.fabs(asDeg) > 90.0:
            raise cMyException("latitude > 90.0")

        self._sens = eLatitudeSens.N if asDeg >= 0.0 else eLatitudeSens.S
        self.val = math.fabs(asDeg)
        return self

    def __truediv__(self, val : cLatitude | float) -> cLatitude:
        if not isinstance(val, (cLatitude, int, float)):
            raise cMyException("latitude iadd type error")

        asDeg: float = self.val
        if isinstance(val, cLatitude):
            asDeg /= val.valAsDeg
        else:
            asDeg /= val

        if math.fabs(asDeg) > 90.0:
            raise cMyException("latitude > 90.0")

        sens = eLatitudeSens.N if asDeg >= 0.0 else eLatitudeSens.S
        angleEnDeg = math.fabs(asDeg)
        return cLatitude (valAsDeg=angleEnDeg, sens=sens)

    def __itruediv__(self, val : cLatitude | float) -> cLatitude:
        if not isinstance(val, (cLatitude, int, float)):
            raise cMyException("latitude iadd type error")

        asDeg: float = self.val
        if isinstance(val, cLatitude):
            asDeg /= val.valAsDeg
        else:
            asDeg /= val

        if math.fabs(asDeg) > 90.0:
            raise cMyException("latitude > 90.0")

        self._sens = eLatitudeSens.N if asDeg >= 0.0 else eLatitudeSens.S
        self.val = math.fabs(asDeg)
        return self


    def copy(self) -> cLatitude:
        retour : cLatitude = cLatitude()
        retour._sens = self._sens
        retour._angleEnDeg = self._angleEnDeg
        return retour


