from __future__ import annotations
import copy

from .cVitesse import cVitesse
from .cPosition import cPosition

class cAirInfo:
    def __init__(self):
        pass

    vent
    temperature
    pression

class cEauInfo:
    def __init__(self):
        pass

    courant
    temperature
    profondeur

class cVecteurDeEtat:
    def __init__(self):
        self._position = None
        self._vitesse = None
        self._air = None
        self._eau = None
        self._variation = None

    @property
    def position(self) -> cPosition:
        return self._position

    @position.setter
    def position(self, x: cPosition) -> None:
        self._position = x

    @property
    def vitesse(self) -> cVitesse:
        return self._vitesse

    @vitesse.setter
    def vitesse(self, x: cVitesse) -> None:
        self._vitesse = x

    @property
    def air(self) -> cAirInfo:
        return self._air

    @air.setter
    def air(self, x: cAirInfo) -> None:
        self._air = x

