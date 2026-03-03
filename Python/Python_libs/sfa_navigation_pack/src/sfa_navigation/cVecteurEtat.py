from __future__ import annotations
from typing import List

from . import cAngle
from . import cVelocite
from . import cCap
from . import cPosition



# ==========================================================
# cEau
# ==========================================================


class cEau:
    def __init__(self, profondeur: float, temperature: float, courant: cVelocite) -> None:
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
    def courant(self) -> cVelocite:
        return self._courant

    @courant.setter
    def courant(self, value: cVelocite) -> None:
        if not isinstance(value, cVelocite):
            raise TypeError("courant doit être un cVecteur")
        self._courant = value

    @classmethod
    def fromDict(cls, data: dict) -> cEau:
        return cls(
            profondeur=float(data[cVecteurEtatKeys.PROFONDEUR]),
            temperature=float(data[cVecteurEtatKeys.TEMPERATURE]),
            courant=cVelocite.fromObject(data[cVecteurEtatKeys.COURANT]),
        )

    def __str__(self) -> str:
        s: str = f"[eau profondeur={self.profondeur} temperature={self.temperature} courant={self.courant}]"
        return s


# ==========================================================
# cAir
# ==========================================================


class cAir:
    def __init__(self, temperature: float, vent: cVelocite) -> None:
        self.temperature = temperature
        self.vent = vent

    @property
    def temperature(self) -> float:
        return self._temperature

    @temperature.setter
    def temperature(self, value: float) -> None:
        self._temperature = float(value)

    @property
    def vent(self) -> cVelocite:
        return self._vent

    @vent.setter
    def vent(self, value: cVelocite) -> None:
        if not isinstance(value, cVelocite):
            raise TypeError("vent doit être un cVecteur")
        self._vent = value

    @classmethod
    def fromDict(cls, data: dict) -> cAir:
        return cls(temperature=float(data[cVecteurEtatKeys.TEMPERATURE]), vent=cVelocite.fromObject(data[cVecteurEtatKeys.VENT]))

    def __str__(self) -> str:
        s: str = f"[air temperature={self.temperature} vent={self.vent}]"
        return s


# ==========================================================
# cBateau
# ==========================================================


class cBateau:
    def __init__(self, sog: cVelocite, position: cPosition, varMagnetique: cCap, derive : cAngle) -> None:
        self.sog = sog
        self.position = position
        self.varMagnetique = varMagnetique
        self._derive = derive

    @property
    def sog(self) -> cVelocite:
        return self._sog

    @sog.setter
    def sog(self, value: cVelocite) -> None:
        if not isinstance(value, cVelocite):
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

    @property
    def derive(self) -> cAngle:
        return self._derive

    @classmethod
    def fromDict(cls, data: dict) -> "cBateau":
        return cls(
            sog=cVelocite.fromObject(data[cVecteurEtatKeys.SOG]),
            position=cPosition.fromObject(data[cVecteurEtatKeys.POSITION]),
            varMagnetique=cCap.fromObject(data[cVecteurEtatKeys.VARIATION_MAGNETIQUE]),
            derive=cAngle.fromObject(data[cVecteurEtatKeys.DERIVE]),
        )

    def __str__(self) -> str:
        s: str = (
            f"[bateau sog={self.sog},position={self.position}, varMagnetique={self.varMagnetique}, derive={self._derive}]"
        )
        return s


# ==========================================================
# ETAT GLOBAL
# ==========================================================

class cVecteurEtatKeys:
    VENT : str = "Vent"
    TEMPERATURE : str = "Temperature"
    PROFONDEUR : str = "Profondeur"
    VARIATION_MAGNETIQUE : str = "VariationMagnetique"
    COURANT : str = "Courant"
    POSITION : str = "Position"
    DERIVE: str = "Derive"
    SOG: str = "SOG"
    BATEAU : str = "Bateau"
    AIR : str = "Air"
    EAU : str = "Eau"
    DEPART : str = "Depart"
    ARRIVEE : str = "Arrivee"
    WAYPOINTS : str = "wpt"

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
            cBateau=cBateau.fromDict(data[cVecteurEtatKeys.BATEAU]),
            cAir=cAir.fromDict(data[cVecteurEtatKeys.AIR]),
            cEau=cEau.fromDict(data[cVecteurEtatKeys.EAU]),
        )

    def toString(self) -> str:
        return self.__str__()

    def __str__(self) -> str:
        s: str = f"[vecteurEtat bateau={self.cBateau} air={self.cAir} eau={self.cEau}]"
        return s


# ==========================================================
# TRAJET
# ==========================================================


class cTrajet:
    def __init__(
        self, depart: cPosition, arrivee: cPosition, pointsDePassage: List[cPosition]
    ) -> None:
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
            depart=cPosition.fromObject(data[cVecteurEtatKeys.DEPART]),
            arrivee=cPosition.fromObject(data[cVecteurEtatKeys.ARRIVEE]),
            pointsDePassage=[cPosition.fromObject(p) for p in data.get(cVecteurEtatKeys.WAYPOINTS, [])],
        )

    def toString(self) -> str:
        return self.__str__()

    def __str__(self) -> str:
        s: str = f"[Trajet depart={self.depart}, arrivee={self.arrivee}, wpt={self._waypoints}]"
        return s