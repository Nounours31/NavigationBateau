from __future__ import annotations
from typing import List

from . import cVecteur, cCap
from .cPosition import cPosition



from typing import List


# ==========================================================
# EAU
# ==========================================================

class Eau:

    def __init__(self, profondeur: float, temperature: float, courant: cVecteur) -> None:
        self.profondeur = profondeur
        self.temperature = temperature
        self.courant = courant

    # --- profondeur ---

    @property
    def profondeur(self) -> float:
        return self._profondeur

    @profondeur.setter
    def profondeur(self, value: float) -> None:
        if value < 0:
            raise ValueError("La profondeur ne peut pas être négative")
        self._profondeur = float(value)

    # --- temperature ---

    @property
    def temperature(self) -> float:
        return self._temperature

    @temperature.setter
    def temperature(self, value: float) -> None:
        self._temperature = float(value)

    # --- courant ---

    @property
    def courant(self) -> cVecteur:
        return self._courant

    @courant.setter
    def courant(self, value: cVecteur) -> None:
        if not isinstance(value, cVecteur):
            raise TypeError("courant doit être un cVecteur")
        self._courant = value


    @classmethod
    def from_dict(cls, data: dict) -> Eau:
        return cls(
            profondeur=float(data["profondeur"]),
            temperature=float(data["temperature"]),
            courant=cVecteur.from_dict(data["courant"])
        )


# ==========================================================
# AIR
# ==========================================================

class Air:

    def __init__(self, temperature: float, vent: cVecteur) -> None:
        self.temperature = temperature
        self.vent = vent

    @property
    def temperature(self) -> float:
        return self._temperature

    @temperature.setter
    def temperature(self, value: float) -> None:
        self._temperature = float(value)

    @property
    def vent(self) -> cVecteur:
        return self._vent

    @vent.setter
    def vent(self, value: cVecteur) -> None:
        if not isinstance(value, cVecteur):
            raise TypeError("vent doit être un cVecteur")
        self._vent = value

    @classmethod
    def from_dict(cls, data: dict) -> Air:
        return cls(
            temperature=float(data["temperature"]),
            vent=cVecteur.from_dict(data["vent"])
        )

# ==========================================================
# BATEAU
# ==========================================================

class Bateau:

    def __init__(self, sog: cVecteur, position: cPosition, varMagnetique: cCap) -> None:
        self.sog = sog
        self.position = position
        self.varMagnetique = varMagnetique

    @property
    def sog(self) -> cVecteur:
        return self._sog

    @sog.setter
    def sog(self, value: cVecteur) -> None:
        if not isinstance(value, cVecteur):
            raise TypeError("sog doit être un cVecteur")
        self._sog = value

    @property
    def position(self) -> cPosition:
        return self._position

    @position.setter
    def position(self, value: cPosition) -> None:
        if not isinstance(value, cPosition):
            raise TypeError("position doit être un cPosition")
        self._position = value

    @property
    def varMagnetique(self) -> cCap:
        return self._varMagnetique

    @varMagnetique.setter
    def varMagnetique(self, value: cCap) -> None:
        if not isinstance(value, cCap):
            raise TypeError("varMagnetique doit être un cCap")
        self._varMagnetique = value

    @classmethod
    def from_dict(cls, data: dict) -> "Bateau":
        return cls(
            sog=cVecteur.from_dict(data["sog"]),
            position=cPosition.fromString(data["position"]),
            varMagnetique=cCap.fromString(data["varMagnetique"])
        )

# ==========================================================
# ETAT GLOBAL
# ==========================================================

class cVecteurEtat:

    def __init__(self, bateau: Bateau, air: Air, eau: Eau) -> None:
        self.bateau = bateau
        self.air = air
        self.eau = eau

    @property
    def bateau(self) -> Bateau:
        return self._bateau

    @bateau.setter
    def bateau(self, value: Bateau) -> None:
        if not isinstance(value, Bateau):
            raise TypeError("bateau doit être un Bateau")
        self._bateau = value

    @property
    def air(self) -> Air:
        return self._air

    @air.setter
    def air(self, value: Air) -> None:
        if not isinstance(value, Air):
            raise TypeError("air doit être un Air")
        self._air = value

    @property
    def eau(self) -> Eau:
        return self._eau

    @eau.setter
    def eau(self, value: Eau) -> None:
        if not isinstance(value, Eau):
            raise TypeError("eau doit être un Eau")
        self._eau = value

    @classmethod
    def from_dict(cls, data: dict) -> cVecteurEtat:
        return cls(
            bateau=Bateau.from_dict(data["bateau"]),
            air=Air.from_dict(data["air"]),
            eau=Eau.from_dict(data["eau"])
        )

# ==========================================================
# TRAJET
# ==========================================================

class cTrajet:

    def __init__(self, depart: cPosition, arrivee: cPosition, pointsDePassage: List[cPosition]) -> None:
        self.depart = depart
        self.arrivee = arrivee
        self.waypoints = pointsDePassage

    @property
    def depart(self) -> cPosition:
        return self._depart

    @depart.setter
    def depart(self, value: cPosition) -> None:
        if not isinstance(value, cPosition):
            raise TypeError("depart doit être un cPosition")
        self._depart = value

    @property
    def arrivee(self) -> cPosition:
        return self._arrivee

    @arrivee.setter
    def arrivee(self, value: cPosition) -> None:
        if not isinstance(value, cPosition):
            raise TypeError("arrivee doit être un cPosition")
        self._arrivee = value

    @property
    def waypoints(self) -> List[cPosition]:
        return self._waypoints

    @waypoints.setter
    def waypoints(self, value: List[cPosition]) -> None:
        if not isinstance(value, list):
            raise TypeError("waypoints doit être une liste")
        for p in value:
            if not isinstance(p, cPosition):
                raise TypeError("Chaque waypoint doit être un cPosition")
        self._waypoints = value

    @classmethod
    def from_dict(cls, data: dict) -> cTrajet:
        return cls(
            depart=cPosition.fromString(data["depart"]),
            arrivee=cPosition.fromString(data["arrivee"]),
            pointsDePassage=[
                cPosition.fromString(p) for p in data.get("waypoints", [])
            ]
        )