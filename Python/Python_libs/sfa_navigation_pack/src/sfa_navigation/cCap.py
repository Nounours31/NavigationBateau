from __future__ import annotations
import math

from sfa_tools import cMyException
from .cAngle import cAngle

"""
Classe de base de cap
"""
class cCap(cAngle):
    def __init__(self, valAsDeg : float = 0.0):
        super().__init__(valAsDeg=valAsDeg)
        self.inner_normalise()

    def __repr__(self) -> str:
        return "[cCap: " + self.__str__() + "]"

    def __str__(self) -> str:
        """
        toString par defaut
        """
        return super().__str__()


    def __add__(self, other) -> cCap:
        a : cAngle = super().__add__(other)
        return cCap(a.valAsDeg)

    def __iadd__(self, other) -> cCap:
        super().__iadd__(other)
        self.inner_normalise()
        return self

    def __mul__(self, other) -> cCap:
        a = super().__mul__(other)
        return cCap(a.valAsDeg)

    def __imul__(self, other) -> cCap:
        super().__imul__(other)
        self.inner_normalise()
        return self

    def __sub__(self, other) -> cCap:
        a = super().__sub__(other)
        return cCap(a.valAsDeg)

    def __isub__(self, other) -> cCap:
        super().__isub__(other)
        self.inner_normalise()
        return self

    def __truediv__(self, other) -> cCap:
        a = super().__truediv__(other)
        return cCap(a.valAsDeg)

    def __itruediv__(self, other) -> cCap:
        super().__itruediv__(other)
        self.inner_normalise()
        return self
    
            