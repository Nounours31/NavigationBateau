import pytest

from sfa_navigation import cVitesse, cDistance, cCap, cVecteurEtat, cVitesse, cPosition


# =========================
# Fixtures
# =========================




class cVecteurEtat_tests:
    def test_init(self):

        data : dict[str, object] = {
            "bateau" : {
                "sog" : cVitesse(valAsNoeud=15.0),
                "position" : cPosition.fromDict({"latitude":"N 12°","longitude":"W 10°"}),
                "varMagnetique": cCap(valAsDeg=-1.2)
            },
            "eau": {
                "courant": {
                    "vitesseEnNd" : cVitesse(valAsNoeud=1.50),
                    "directionEnDeg": cCap(valAsDeg=1.5),
                },
                "profondeur": 17,
                "temperature": 12
            },
            "air": {
                "vitesseEnNd": cVitesse(valAsNoeud=15.0),
                "directionEnDeg": cCap(valAsDeg=75.0),  # sens du vent attention !!!
                "temperature": 20
            },
        }

        v : cVecteurEtat = cVecteurEtat.fromDict(data)
        print (v)
        print (v)
