from __future__ import annotations
from datetime import datetime, timezone
from enum import Enum, IntEnum
from typing import List

from .mSatellite import cSatellite
from .mAngle import cAngle
from .mVelocite import cVelocite
from .mCap import cCap
from .mPosition import cPosition
from .mNavigationFormules import cNavigationFormules

import numpy as np
import math

# ==========================================================
# cEau
# ==========================================================


class cEtatMer:
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
    def fromDict(cls, data: dict) -> cEtatMer:
        return cls(
            profondeur=float(data[cVecteurEtatKeys.PROFONDEUR]),
            temperature=float(data[cVecteurEtatKeys.TEMPERATURE]),
            courant=cVelocite.fromObject(data[cVecteurEtatKeys.COURANT]),
        )

    def __str__(self) -> str:
        s: str = f"[eau profondeur={self.profondeur} temperature={self.temperature} courant={self.courant}]"
        return s

    def __repr__(self):
        return self.__str__()


# ==========================================================
# cAir
# ==========================================================


class cEtatAir:
    def __init__(self, temperature: float, vent: cVelocite, derive : cAngle) -> None:
        self.temperature = temperature
        self.vent = vent
        self._derive = derive

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

    @property
    def derive(self) -> cAngle:
        return self._derive
    @derive.setter
    def derive(self, value: cAngle) -> None:
        self._derive = value

    @classmethod
    def fromDict(cls, data: dict) -> cEtatAir:
        return cls(temperature=float(data[cVecteurEtatKeys.TEMPERATURE]), 
                   vent=cVelocite.fromObject(data[cVecteurEtatKeys.VENT]), 
                   derive=cAngle.fromObject(data[cVecteurEtatKeys.DERIVE]))

    def __str__(self) -> str:
        s: str = f"[air temperature={self.temperature} vent={self.vent} derive={self._derive}]"
        return s

    def __repr__(self):
        return self.__str__()


# ==========================================================
# cBateau
# ==========================================================


class cEtatBateau:
    def __init__(self, sog: cVelocite, position: cPosition, varMagnetique: cCap) -> None:
        self.sog = sog
        self.position = position
        self.varMagnetique = varMagnetique

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

    @classmethod
    def fromDict(cls, data: dict) -> "cEtatBateau":
        return cls(
            sog=cVelocite.fromObject(data[cVecteurEtatKeys.SOG]),
            position=cPosition.fromObject(data[cVecteurEtatKeys.POSITION]),
            varMagnetique=cCap.fromObject(data[cVecteurEtatKeys.VARIATION_MAGNETIQUE])
        )

    def __str__(self) -> str:
        s: str = (
            f"[bateau sog={self.sog},position={self.position}, varMagnetique={self.varMagnetique}]"
        )
        return s
    
    def __repr__(self):
        return self.__str__()


# ==========================================================
# ETAT GLOBAL
# ==========================================================

class cVecteurEtatKeys:
    HEURE : str = "Heure"
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
    SATELLITE: str = "Satellite"

class cVecteurEtat:
    def __init__(self, timestamp: float, bateau: cEtatBateau, air: cEtatAir, eau: cEtatMer, satellite: cSatellite) -> None:
        self._timestamp = timestamp
        self._bateau = bateau
        self._air = air
        self._eau = eau
        self._satellite = satellite

    @property
    def satellite(self) -> cSatellite:
        return self._satellite

    @property
    def bateau(self) -> cEtatBateau:
        return self._bateau

    @bateau.setter
    def bateau(self, value: cEtatBateau) -> None:
        if not isinstance(value, cEtatBateau):
            raise TypeError("cBateau doit être un cBateau")
        self._bateau = value

    @property
    def air(self) -> cEtatAir:
        return self._air

    @air.setter
    def air(self, value: cEtatAir) -> None:
        if not isinstance(value, cEtatAir):
            raise TypeError("cAir doit être un cAir")
        self._air = value

    @property
    def eau(self) -> cEtatMer:
        return self._eau

    @eau.setter
    def eau(self, value: cEtatMer) -> None:
        if not isinstance(value, cEtatMer):
            raise TypeError("cEau doit être un cEau")
        self._eau = value

    @classmethod
    def fromDict(cls, data: dict) -> cVecteurEtat:
        return cls(
            timestamp=data[cVecteurEtatKeys.HEURE],
            bateau=cEtatBateau.fromDict(data[cVecteurEtatKeys.BATEAU]),
            air=cEtatAir.fromDict(data[cVecteurEtatKeys.AIR]),
            eau=cEtatMer.fromDict(data[cVecteurEtatKeys.EAU]),
            satellite=cSatellite.fromDict(data[cVecteurEtatKeys.SATELLITE]),
        )

    def toString(self) -> str:
        return self.__str__()

    def __str__(self) -> str:
        s: str = f"[vecteurEtat heure={datetime.fromtimestamp(timestamp = self._timestamp, tz=timezone.utc).strftime('%d/%m/%y %H:%M:%S.%f')} bateau={self.bateau} air={self.air} eau={self.eau}]"
        return s

    def __repr__(self) -> str:
        return self.__str__()


# ==========================================================
# TRAJET
# ==========================================================
class cToleranceTrajet(IntEnum):
    PassageParWPT = 1 << 0
    PassageParWPTLePlusProche = 1 << 1
    PassageParWPTLePlusProcheDansLAXE = 1 << 2
    ZappeDepart = 1 << 3

class cTrajet:
    def __init__(self,  depart: cPosition, arrivee: cPosition, pointsDePassage: List[cPosition],
        tolerance: int = (cToleranceTrajet.ZappeDepart | cToleranceTrajet.PassageParWPTLePlusProcheDansLAXE)
    ) -> None:
        self._tolerance = tolerance
        self._depart = depart
        self._arrivee = arrivee
        self._waypoints = pointsDePassage

    @property
    def tolerance(self) -> int:
        return self._tolerance
    @tolerance.setter
    def tolerance(self, tolerance: int) -> None:
        self._tolerance = tolerance

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
    
    def __repr__(self):
        return self.__str__()
    
    @staticmethod
    def quick_sort(arr : List[(cPosition, cDistance)]) -> List[(cPosition, cDistance)]:
        if len(arr) <= 1:
            return arr
        (pivot, dpivot) = arr[0]
        left = [(x, y) for (x, y) in arr[1:] if y < dpivot]
        right = [(x, y) for (x, y) in arr[1:] if y >= dpivot]
        return cTrajet.quick_sort(left) + [(pivot, dpivot)] + cTrajet.quick_sort(right)


    def getTrajectoireInfo(self, b: cEtatBateau) -> cCap:
        # quel est le prochain point de la trajectoire a rejoindre a partir de la position courante
        # en fonction de la tolerance choisie
        
        # recup de tous les WPT
        allWPT : List[cPosition] = []    
        allWPT.append(self.depart)
        for p in self.waypoints:
            allWPT.append(p)
        allWPT.append(self.arrivee)


        # liste des points devant moi et pas trop pret  
        x : cNavigationFormules = cNavigationFormules(b.position)
        c : cCap
        d : cDistance
        wptDevant : List[(cPosition, cDistance)] = []    
        for p in allWPT:
            (c, d) = x.routeLoxodromique(arrivee=p)
            if d > 0.1:
                v1 = np.array([math.sin(c.capAsRad), math.cos(c.capAsRad)])
                v2 = np.array([math.sin(b.sog.sens.capAsRad), math.cos(b.sog.sens.capAsRad)])
                if np.dot(v1, v2) > 0:
                    wptDevant.append((p, d)) 
        
        # aucune solution - je vise l'arrivee
        wptDevantTrie : List[(cPosition, cDistance)] = cTrajet.quick_sort(wptDevant)
        if len(wptDevantTrie) == 0 :
            (c, d) = x.routeLoxodromique(arrivee=t.arrivee)
            return c
        
        # 1 solution - j'y vais
        if len(wptDevantTrie) == 1 :
            (c, d) = x.routeLoxodromique(arrivee=wptDevantTrie[0][0])
            return c
        

        # plusieur solution - prendre la meilleure des 2 premieres
        # je ne garde que les point assez loing (je n'impose pas de passer par le WPT)
        wptDevantTrieUtile : List[(cPosition, cDistance)] = []    
        for (p, d) in wptDevantTrie:
            if d > 0.5:
                wptDevantTrieUtile.append((p,d))

        # pour les deux premiers prendre l'angle avec la route le plus petit
        if len(wptDevantTrieUtile) == 0 or len(wptDevantTrieUtile) == 1:
            (u, v) = wptDevantTrie[0]
            (c, d) = x.routeLoxodromique(arrivee=u)
            return c
        
        (c1, d1) = x.routeLoxodromique(arrivee=wptDevantTrieUtile[0][0])
        (c2, d2) = x.routeLoxodromique(arrivee=wptDevantTrieUtile[1][0])
        if math.fabs(c1.capAsDeg - b.sog.sens.capAsDeg) < math.fabs(c2.capAsDeg - b.sog.sens.capAsDeg):
            return c1
        else:
            return c2
            