import pytest

from sfa_navigation import cCap, cVecteurEtat, cVitesse, cPosition, cAngle


# =========================
# Fixtures
# =========================


class cVecteurEtat_tests:
    def test_init(self):
        data: dict[str, object] = {
            "bateau": {
                "SOG": cVitesse(normeVitesse=15.0, sens=75),
                "derive": cAngle(valAsDeg=2.5),
                "position": cPosition.fromDict({"latitude": "N 12°", "longitude": "W 10°"}),
                "varMagnetique": cCap(valAsDeg=-1.2),
            },
            "eau": {
                "courant": cVitesse(normeVitesse=15.0, sens=75),
                "profondeur": 17,
                "temperature": 12,
            },
            "air": {
                "vent": cVitesse(normeVitesse=15.0, sens=75),
                "temperature": 20,
            },
        }

        v: cVecteurEtat = cVecteurEtat.fromDict(data)
        ref : str = "[vecteurEtat bateau=[bateau sog=15.000Kt 075.0000° position=N 012.0000°, W 010.0000° varMagnetique=358.8000°] air=[air temperature=20.0 vent=15.000Kt 075.0000°] eau=[eau profondeur=17.0 temperature=12.0 courant=15.000Kt 075.0000°]]"
        print(v)
        assert v.toString() == ref
