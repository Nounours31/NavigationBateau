import pytest
from datetime import datetime

from myEnv import myEnv
from nmea.nmea0183Lib import nmea0183lib as nmea
from simulateurNav.simulateurNav import simulateurNav
from tools.angle import angle
from tools.position import position


def test_function():
    navconfig : dict[str, float | object] = {
        "vitesseEnNoeudMoyenne" : 15.0,
        "variationMagnetique" : -1.2,
        "nav": {
            "positionDepart": myEnv.postionTrinitee.toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT),
            "positionWayPoints" : [
                position.fromString("2, 2").toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT),
                position.fromString("2, 2").toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT)
            ],
            "positionArrivee": myEnv.postionTrinitee.toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT)
        },
        "vent": {
            "vitesseEnNd" : 15,
            "directionEnDeg": 75, # sens du vent attention !!!
            "temperature" : 20
        },
        "courant": {
            "vitesseEnNd" : 1.5,
            "directionEnDeg": 2.5,
        },
        "eau": {
            "profondeur" : 17,
            "temperature" : 12,
        },
    }
    x : simulateurNav = simulateurNav(navconfig)
    assert x is None

