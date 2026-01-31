"""
Packages de la gestion des angles dans le cadre du projet de navigation bateau

"""
from __future__ import annotations

import math
import re

from enum import Enum, unique
from sfa_tools import cMyException

@unique
class eAngleFormat(Enum):
    """
    Gestion des format d'angle
    Attention au formats:
        DMS - Degrees, Minutes, Seconds (DMS): e.g. "N 50° 7' 20.9122", "W 8° 39' 56.52"
        DMM - Degrees and Decimal Minutes (DMM): e.g. "50° 7.34854N", "8° 39.942W"
        DD  - Decimal Degrees (DD): e.g. "+50.12257°", "-8.66570°"
        R8  - en chiffre (R8): e.g. "+50.12257", "-8.66570"
    """
    DMS = "DMS"
    DMM = "DMM"
    DD  = "DD"
    R8  = "R8"
    RAD  = "RAD"
    Debug  = "Debug"

    def __repr__(self) -> str:
        return "[eAngleFormat: " + self.__str__() + "]"

    def __str__(self) -> str:
        """
        toString par defaut
        """
        match self:
            case eAngleFormat.DMS:
                return "DMS"
            case eAngleFormat.DMM:
                return "DMM"
            case eAngleFormat.DD:
                return "DD"
            case eAngleFormat.R8:
                return "Reel"
            case eAngleFormat.RAD:
                return "Rad"
            case eAngleFormat.Debug:
                return "Debug"
            case _:
                return "xxx"


class cAngle:
    """
    Classe de gestion des angles
    """
    RAD2DEG: float = 180.0 / math.pi
    DEG2RAD: float = math.pi / 180.0

    DISPLAY_SHORT : int = 0
    DISPLAY_LONG : int = 1

    EQUAL_TOLERANCE_IN_DEG : float = 0.00001

    def __init__(self, valAsDeg : float | None = None, valAsRad : float | None = None):
        """
        constructeur avec priorite au degre
        Args:
            valAsDeg (float): valeur de l'angle en degre, si defini il est prioritaire sur la valeur en rad
            valAsRad (float): valeur de l'angle en rad, ignore si la val en deg existe
        Returns:
            cAngle: instance de cAngle
        Raises:
            Aucune
        """
        self._equalToleranceInDeg = cAngle.EQUAL_TOLERANCE_IN_DEG
        if valAsDeg is not None :
            self._angleEnDeg : float = valAsDeg
        elif valAsRad is not None :
            self._angleEnDeg : float = valAsRad * cAngle.RAD2DEG
        else:
            self._angleEnDeg = 0.0


    def __repr__(self) -> str :
        """
        return self.toString(format = eAngleFormat.DD)
        """
        return "[cAngle: " + self.__str__() + "]"

    def __str__(self) -> str :
        """
        toString par defaut
        """
        return self.toString(format = eAngleFormat.DD)

    def toString(self, format: eAngleFormat = eAngleFormat.DD) -> str:
        """
        Converti en string un angle.
        Args:
            format (eAngleFormat): le format attendu
        Returns:
            str: la string representant cet angle
        Raises:
            Aucune
        """

        sign : str = "" if self._angleEnDeg >= 0.0 else "-"

        angleEnValeurAbsolue : float = math.fabs(self._angleEnDeg)
        angleValeurEntiere : int = math.floor (angleEnValeurAbsolue)
        minute : float = (angleEnValeurAbsolue - angleValeurEntiere) * 60.0
        minuteEnValeurEntiere : int = math.floor(minute)
        seconde : float = (minute - math.floor (minute)) * 60.0

        if format == eAngleFormat.R8:
            return f"{sign}{angleEnValeurAbsolue:08.4f}"

        elif format == eAngleFormat.RAD:
            return f"{sign}{(angleEnValeurAbsolue * cAngle.DEG2RAD):08.4f} rad"

        elif format == eAngleFormat.DD:
            return f"{sign}{angleEnValeurAbsolue:08.4f}°"

        elif format == eAngleFormat.DMM:
            return f"{sign}{angleValeurEntiere:03d}°{minute:06.3f}'"

        elif format == eAngleFormat.DMS:
            return f"{sign}{angleValeurEntiere:03d}°{minuteEnValeurEntiere:02d}'{seconde:06.3f}\""

        elif format == eAngleFormat.Debug:
            return f"{sign}{angleEnValeurAbsolue:08.4f}° [{sign}{angleValeurEntiere:03d}°{minute:06.3f}' # {sign}{angleValeurEntiere:03d}°{minuteEnValeurEntiere:02d}'{seconde:06.3f}\"]"

        else:
            raise cMyException('Format demande inconnu: ' + str(format))

    @staticmethod
    def fromString (angleAsString : str = "") -> cAngle :
        """
        Converti en string un angle.
        Args:
            angleAsString (str): le format attendu
        Returns:
            str: la string representant cet angle
        Raises:
            cMyException: si s ne represente pas un angle
        """
        regex_rad = r"\s*([\+|\-])?([0-9]*(\.[0-9]*)?)\s*rad\s*" # 12.12 rad
        regex_dd = r"\s*([\+|\-])?([0-9]*(\.[0-9]*)?)°?\s*" # 12.12°
        regex_ddmm = r"\s*([\+|\-])?([0-9]+)°([0-9]{1,2}(\.[0-9]*)?)'?\s*" # 12°12.25'
        regex_ddmmss = r"\s*([\+|\-])?([0-9]+)°([0-9]{1,2})'([0-9]{1,2}\.[0-9]*)\"?\s*" # 12°45'56.12"

        try:
            sign : float = 1.0
            a : cAngle | None = None
            x =  re.fullmatch(regex_dd, angleAsString)
            if x:
                sign = 1.0 if ((x.group(1) is None) or (x.group(1) == "+")) else -1.0
                deg = x.group(2)
                a = cAngle (valAsDeg = sign * float(deg))

            else:
                x = re.fullmatch(regex_ddmm, angleAsString)
                if x:
                    sign = 1.0 if ((x.group(1) is None) or (x.group(1) == "+")) else -1.0
                    deg = x.group(2)
                    min = x.group(3)
                    a = cAngle (valAsDeg = sign * (float(deg) + float(min) / 60.0))

                else:
                    x = re.fullmatch(regex_ddmmss, angleAsString)
                    if x:
                        sign = 1.0 if ((x.group(1) is None) or (x.group(1) == "+")) else -1.0
                        deg = x.group(2)
                        min = x.group(3)
                        sec = x.group(4)
                        a = cAngle (valAsDeg = sign * (float(deg) + float(min) / 60.0 + float(sec) / 3600.0))
                    else:
                        x = re.fullmatch(regex_rad, angleAsString)
                        if x:
                            sign = 1.0 if ((x.group(1) is None) or (x.group(1) == "+")) else -1.0
                            rad = x.group(2)
                            a = cAngle(valAsRad = sign * float(rad))
                        else:
                            raise cMyException("Ce n'est pas un angle")

        except Exception as e:
            raise cMyException (str(e))

        return a

    @property
    def valAsDeg(self) -> float:
        return self._angleEnDeg

    @property
    def valAsRad(self) -> float:
        return cAngle.DEG2RAD * self._angleEnDeg

    @valAsDeg.setter
    def valAsDeg(self, x:float) -> None:
        self._angleEnDeg = x

    def __ne__(self, val : cAngle | float) -> bool:
        return not self.__eq__(val)
    def __eq__(self, val: cAngle | float) -> bool:
        """
        equals, a1 == a2 ?
        Args:
            val (cAngle | float) : la valeur a tester - ATTENTION si float en degré
        Returns:
            bool: true ou false
        Raises:
            cMyException: si val n'est pas un type valide
        """
        if isinstance(val, cAngle):
            return math.fabs(self._angleEnDeg - val._angleEnDeg) < self._equalToleranceInDeg

        if isinstance(val, int):
            val = 1.0 * val

        if isinstance(val, float):
            return math.fabs(self._angleEnDeg - val) < self._equalToleranceInDeg

        raise cMyException("angle equal: type error")



    def __iadd__(self, val : cAngle | float) -> cAngle:
        """
        in-place add, en gros angle += 2.1°
        Args:
            val (cAngle | float) : la valeur a ajouter - ATTENTION si flat en degré
        Returns:
            self
        Raises:
            cMyException: si val n'est pas un type valide
        """
        if isinstance(val, cAngle):
            self._angleEnDeg += val.valAsDeg
            return self

        if isinstance(val, int):
            val = 1.0 * val

        if isinstance(val, float):
            self._angleEnDeg += val
            return self

        raise cMyException("angle add: type error")

    def __radd__(self, val : cAngle | float) -> cAngle:
        return self.__add__(val)
    def __add__(self, val : cAngle | float) -> cAngle:
        """
        add, en gros angle = a1 + a2
        Args:
            val (cAngle | float) : la valeur a ajouter - ATTENTION si flat en degré
        Returns:
            cAngle: le nouvel angle
        Raises:
            cMyException: si val n'est pas un type valide
        """
        if issubclass(val.__class__, cAngle):
            return cAngle (valAsDeg = self._angleEnDeg + val._angleEnDeg)

        if isinstance(val, int):
            val = 1.0 * val

        if isinstance(val, float):
            return cAngle (valAsDeg = self._angleEnDeg + val)

        raise cMyException("angle add: type error")


    def __rsub__(self, val : cAngle | float) -> cAngle:
        return self.__sub__ (val)
    def __sub__(self, val : cAngle | float) -> cAngle:
        try :
            return self.__add__ (-1.0 * val)
        except :
            raise cMyException("angle add: type error")

    def __isub__(self, val : cAngle | float) -> cAngle:
        try :
            return self.__iadd__ (-1.0 * val)
        except :
            raise cMyException("angle add: type error")


    def __rmul__(self, other : cAngle | float) -> cAngle :
        return self.__mul__(other)
    def __mul__(self, other: cAngle | float) -> cAngle:
        """
        Multiplication, en gros angle = a1 * 3.0
        Args:
            val (float) : la valeur a ajouter - ATTENTION si flat en degré
        Returns:
            cAngle: le nouvel angle
        Raises:
            cMyException: si val n'est pas un type valide
        """
        if issubclass(other.__class__, cAngle):
            return cAngle (valAsDeg = self._angleEnDeg * other.valAsDeg)

        if isinstance(other, int):
            other = 1.0 * other

        if isinstance(other, float):
            return cAngle (valAsDeg = self._angleEnDeg * other)

        raise cMyException("angle add: type error")

    def __imul__(self, other: cAngle | float) -> cAngle:
        """
        Multiplication, en gros angle = a1 * 3.0
        Args:
            val (float) : la valeur a ajouter - ATTENTION si flat en degré
        Returns:
            cAngle: le nouvel angle
        Raises:
            cMyException: si val n'est pas un type valide
        """
        if isinstance(other, cAngle):
            self._angleEnDeg = self._angleEnDeg * other._angleEnDeg
            return self

        if isinstance(other, int):
            other = 1.0 * other

        if isinstance(other, float):
            self._angleEnDeg = self._angleEnDeg * other
            return self

        raise cMyException("angle add: type error")

    def __rtruediv__(self, other : cAngle | float) -> cAngle :
        if isinstance(other, float):
            return cAngle(valAsDeg= other / self._angleEnDeg)

        raise cMyException("angle add: type error")


    def __truediv__(self, other: cAngle | float) -> cAngle:
        """
        Multiplication, en gros angle = a1 * 3.0
        Args:
            val (float) : la valeur a ajouter - ATTENTION si flat en degré
        Returns:
            cAngle: le nouvel angle
        Raises:
            cMyException: si val n'est pas un type valide
        """
        if isinstance(other, cAngle):
            return cAngle (valAsDeg = self._angleEnDeg / other.valAsDeg)

        if isinstance(other, int):
            other = 1.0 * other

        if isinstance(other, float):
            return cAngle (valAsDeg = self._angleEnDeg / other)

        raise cMyException("angle add: type error")

    def __itruediv__(self, other: cAngle | float) -> cAngle:
        """
        Multiplication, en gros angle = a1 * 3.0
        Args:
            val (float) : la valeur a ajouter - ATTENTION si flat en degré
        Returns:
            cAngle: le nouvel angle
        Raises:
            cMyException: si val n'est pas un type valide
        """
        if isinstance(other, cAngle):
            self._angleEnDeg = self._angleEnDeg / other._angleEnDeg
            return self

        if isinstance(other, int):
            other = 1.0 * other

        if isinstance(other, float):
            self._angleEnDeg = self._angleEnDeg / other
            return self

        raise cMyException("angle add: type error")

    def normalise(self) -> cAngle:
        """
        normalise renvoie un angle compris entre 0 et 360
        Args:
            aucun
        Returns:
            self
        Raises:
            aucun
        """
        x : float = self._angleEnDeg
        while x < 0.0:
             x += 360.0
        while x >= 360.0:
             x -= 360.0

        return cAngle(valAsDeg=x)

    def inner_normalise(self):
        x : float = self._angleEnDeg
        while x < 0.0:
             x += 360.0
        while x >= 360.0:
             x -= 360.0
        self._angleEnDeg = x



    def copy(self) -> cAngle:
        """
        normalise renvoie un angle compris entre 0 et 360
        Args:
            aucun
        Returns:
            self
        Raises:
            aucun
        """
        x : float = self._angleEnDeg
        return cAngle(valAsDeg=x)
