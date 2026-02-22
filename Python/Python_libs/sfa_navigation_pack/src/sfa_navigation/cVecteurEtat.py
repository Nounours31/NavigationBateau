from __future__ import annotations
from typing import List

from .cVitesse import cVitesse
from .cCap import cCap
from .cPosition import cPosition

from typing import List


# ==========================================================
# cEau
# ==========================================================

class cEau:
    def __init__(self, profondeur: float, temperature: float, courant: cVitesse) -> None:
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
    def courant(self) -> cVitesse:
        return self._courant

    @courant.setter
    def courant(self, value: cVitesse) -> None:
        if not isinstance(value, cVitesse):
            raise TypeError("courant doit être un cVecteur")
        self._courant = value


    @classmethod
    def fromDict(cls, data: dict) -> cEau:
        return cls(
            profondeur=float(data["profondeur"]),
            temperature=float(data["temperature"]),
            courant=cVitesse.fromDict(data["courant"])
        )

    def __str__(self) -> str:
        s : str = f"[eau profondeur={self.profondeur} temperature={self.temperature} courant={self.courant}]"
        return s

# ==========================================================
# cAir
# ==========================================================

class cAir:
    def __init__(self, temperature: float, vent: cVitesse) -> None:
        self.temperature = temperature
        self.vent = vent

    @property
    def temperature(self) -> float:
        return self._temperature

    @temperature.setter
    def temperature(self, value: float) -> None:
        self._temperature = float(value)

    @property
    def vent(self) -> cVitesse:
        return self._vent

    @vent.setter
    def vent(self, value: cVitesse) -> None:
        if not isinstance(value, cVitesse):
            raise TypeError("vent doit être un cVecteur")
        self._vent = value

    @classmethod
    def fromDict(cls, data: dict) -> cAir:
        return cls(
            temperature=float(data["temperature"]),
            vent=cVitesse.fromDict(data["vent"])
        )

    def __str__(self) -> str:
        s : str = f"[air temperature={self.temperature} vent={self.vent}]"
        return s



# ==========================================================
# cBateau
# ==========================================================

class cBateau:
    def __init__(self, sog: cVitesse, position: cPosition, varMagnetique: cCap) -> None:
        self.sog = sog
        self.position = position
        self.varMagnetique = varMagnetique

    @property
    def sog(self) -> cVitesse:
        return self._sog

    @sog.setter
    def sog(self, value: cVitesse) -> None:
        if not isinstance(value, cVitesse):
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
    def fromDict(cls, data: dict) -> "cBateau":
        return cls(
            sog=cVitesse.fromDict(data["sog"]),
            position=cPosition.fromString(data["position"]),
            varMagnetique=cCap.fromString(data["varMagnetique"])
        )

    def __str__(self) -> str:
        s : str = f"[bateau sog={self.sog} position={self.position} varMagnetique={self.varMagnetique}]"
        return s


# ==========================================================
# ETAT GLOBAL
# ==========================================================

class cVecteurEtat:
    def __init__(self, cBateau: cBateau, cAir: cAir, cEau: cEau) -> None:
        self.cBateau = cBateau
        self.cAir = cAir
        self.cEau = cEau

    @property
    def cBateau(self) -> cBateau:
        return self._bateau

    @cBateau.setter
    def cBateau(self, value: cBateau) -> None:
        if not isinstance(value, cBateau):
            raise TypeError("cBateau doit être un cBateau")
        self._bateau = value

    @property
    def cAir(self) -> cAir:
        return self._air

    @cAir.setter
    def cAir(self, value: cAir) -> None:
        if not isinstance(value, cAir):
            raise TypeError("cAir doit être un cAir")
        self._air = value

    @property
    def cEau(self) -> cEau:
        return self._eau

    @cEau.setter
    def cEau(self, value: cEau) -> None:
        if not isinstance(value, cEau):
            raise TypeError("cEau doit être un cEau")
        self._eau = value

    @classmethod
    def fromDict(cls, data: dict) -> cVecteurEtat:
        return cls(
            cBateau=cBateau.fromDict(data["bateau"]),
            cAir=cAir.fromDict(data["air"]),
            cEau=cEau.fromDict(data["eau"])
        )


    def __str__(self) -> str:
        s : str = f"[vecteurEtat bateau={self.cBateau} air={self.cAir} eau={self.cEau}]"
        return s

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
    def fromDict(cls, data: dict) -> cTrajet:
        return cls(
            depart=cPosition.fromDict(data["depart"]),
            arrivee=cPosition.fromDict(data["arrivee"]),
            pointsDePassage=[
                cPosition.fromString(p) for p in data.get("waypoints", [])
            ]
        )