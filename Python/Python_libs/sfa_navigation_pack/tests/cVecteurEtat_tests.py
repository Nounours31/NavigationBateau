import pytest

from sfa_navigation import cVitesse, cDistance, cCap, cVecteurEtat, cVitesse, cPosition, cAngle
from sfa_navigation.cVitesse import cNormeVitesse


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
        print(v)
        print(v)
