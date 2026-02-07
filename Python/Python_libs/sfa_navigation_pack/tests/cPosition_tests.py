import pytest

from sfa_navigation import cPosition, cLatitude, cLongitude, cAngle, cCap, cDistance, eAngleFormat, cVecteur
from myEnv import myEnv


class position_tests:
    def test_init(self):
        l : cLatitude = cLatitude(0.0)
        longi : cLongitude = cLongitude(0.0)
        p : cPosition = cPosition(lat=l, lon=longi)
        assert p.toString(format=eAngleFormat.DMM) == "N 000°00.000', E 000°00.000'"

    def test_fromstring_dd(self):
        p : cPosition = cPosition.fromString("-1.0, -1.0")
        assert p.toString(format=eAngleFormat.DMM) == "S 001°00.000', W 001°00.000'"

    def test_fromstring_ddmm(self):
        p : cPosition = cPosition.fromString("N 1°0.0, E 1°0.0")
        assert p.toString(format=eAngleFormat.DMM) == "N 001°00.000', E 001°00.000'"

    def test_fromstring_ddmm(self):
        p : cPosition = cPosition.fromString(cPosition.fromString("2, 2").toString(format=eAngleFormat.DMM))
        assert p.toString(format=eAngleFormat.DMM) == "N 002°00.000', E 002°00.000'"


    def test_gudermann(self):
        p : cPosition = myEnv.postionTrinitee
        x : float = p.gudermannInverse()
        assert x == pytest.approx(0.9461, 0.001)

    def test_gudermann2(self):
        p : cPosition = cPosition (lat = cLatitude(0.0), lon= cLongitude(0.0))
        x : float = p.gudermannInverse()
        assert x == pytest.approx(0, 0.001)

    def test_gudermann3(self):
        p : cPosition = cPosition (lat = cLatitude(89.9), lon= cLongitude(0.0))
        x : float = p.gudermannInverse()
        assert x == pytest.approx(7.0439, 0.001)

    def test_route(self):
        a : cPosition = myEnv.postionStQuay
        d : cPosition = myEnv.postionTrinitee

        v: cVecteur = d.positionementRelatif(a)
        assert v.distance.asMn == pytest.approx(65.5779, 0.001)
        assert v.sens.capAsDeg == pytest.approx(6.95, 0.01)

    def test_route2(self):
        d : cPosition = cPosition(lat=cLatitude(valAsDeg=49.1), lon=cLongitude(valAsDeg=2.5))
        a : cPosition = cPosition(lat=cLatitude(valAsDeg=28.1), lon=cLongitude(valAsDeg=-20.5))

        v: cVecteur  = d.positionementRelatif(a)
        assert v.distance.asMn == pytest.approx(1644, 1)
        assert v.sens.capAsDeg == pytest.approx(210, 1)

    def test_route3(self):
        d : cPosition = cPosition(lat=cLatitude(valAsDeg=49.1), lon=cLongitude(valAsDeg=2.5))
        a : cPosition = cPosition(lat=cLatitude(valAsDeg=49.5), lon=cLongitude(valAsDeg=-50.7))

        v: cVecteur  = d.positionementRelatif(a)
        assert v.distance.asMn == pytest.approx(2042, 1)
        assert v.sens.capAsDeg == pytest.approx(270, 1)

    def test_route4(self):
        d : cPosition = cPosition(lat=cLatitude(valAsDeg=23.7), lon=cLongitude(valAsDeg=-67.1))
        a : cPosition = cPosition(lat=cLatitude(valAsDeg=49.5), lon=cLongitude(valAsDeg=-0.8))

        v: cVecteur  = d.positionementRelatif(a)
        assert v.distance.asMn == pytest.approx(3427, 1)
        assert v.sens.capAsDeg == pytest.approx(45, 1)

    def test_route5(self):
        d : cPosition = cPosition(lat=cLatitude(valAsDeg=23.7), lon=cLongitude(valAsDeg=-67.1))
        a : cPosition = cPosition(lat=cLatitude(valAsDeg=-34), lon=cLongitude(valAsDeg=18.8))

        v: cVecteur  = d.positionementRelatif(a)
        assert v.distance.asMn == pytest.approx(6003, 1)
        assert v.sens.capAsDeg == pytest.approx(125, 1)

    def test_route6(self):
        d : cPosition = cPosition(lat=cLatitude(valAsDeg=49.5), lon=cLongitude(valAsDeg=-56.5))
        a : cPosition = cPosition(lat=cLatitude(valAsDeg=49.5), lon=cLongitude(valAsDeg=-5.7))

        v: cVecteur  = d.positionementRelatif(a)
        assert v.distance.asMn == pytest.approx(1981, 1)
        assert v.sens.capAsDeg == pytest.approx(90, 1)

    def test_route7(self):
        d : cPosition = cPosition(lat=cLatitude(valAsDeg=80), lon=cLongitude(valAsDeg=-56.5))
        a : cPosition = cPosition(lat=cLatitude(valAsDeg=80), lon=cLongitude(valAsDeg=-5.7))

        v: cVecteur  = d.positionementRelatif(a)
        assert v.distance.asMn == pytest.approx(604, 1)
        assert v.sens.capAsDeg == pytest.approx(90, 1)
