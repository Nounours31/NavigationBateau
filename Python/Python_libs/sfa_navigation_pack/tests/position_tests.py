import pytest

from sfa_navigation import cPosition, cLatitude, cLongitude, cAngle, cCap, cDistance
from myEnv import myEnv


class position_tests:
    def test_init(self):
        l : cLatitude = cLatitude(0.0)
        longi : cLongitude = cLongitude(0.0)
        p : cPosition = cPosition(lat=l, lon=longi)
        assert p.toString(base=cAngle.STR_AsMin, detail=cAngle.DISPLAY_SHORT) == "N 000°00.000',E 000°00.000'"

    def test_fromstring_dd(self):
        p : cPosition = cPosition.fromString("-1.0, -1.0")
        assert p.toString(base=cAngle.STR_AsMin, detail=cAngle.DISPLAY_SHORT) == "S 001°00.000',W 001°00.000'"

    def test_fromstring_ddmm(self):
        p : cPosition = cPosition.fromString("N 1°0.0, E 1°0.0")
        assert p.toString(base=cAngle.STR_AsMin, detail=cAngle.DISPLAY_SHORT) == "N 001°00.000',E 001°00.000'"

    def test_fromstring_ddmm(self):
        p : cPosition = cPosition.fromString(cPosition.fromString("2, 2").toString(base=cAngle.STR_AsMin, detail=cAngle.DISPLAY_SHORT))
        assert p.toString(base=cAngle.STR_AsMin, detail=cAngle.DISPLAY_SHORT) == "N 002°00.000',E 002°00.000'"


    def test_gudermann(self):
        p : cPosition = myEnv.postionTrinitee
        x : float = p.gudermannInverse()
        assert x == pytest.approx(1.365, 0.001)

    def test_gudermann2(self):
        p : cPosition = cPosition (lat = cLatitude(0.0), lon= cLongitude(0.0))
        x : float = p.gudermannInverse()
        assert x == pytest.approx(0, 0.001)

    def test_gudermann3(self):
        p : cPosition = cPosition (lat = cLatitude(89.9), lon= cLongitude(0.0))
        x : float = p.gudermannInverse()
        assert x == pytest.approx(10.162, 0.001)

    def test_route(self):
        a : cPosition = myEnv.postionStQuay
        d : cPosition = myEnv.postionTrinitee

        c : cCap
        dist : cDistance
        c, dist  = cPosition.positionementRelatif(d,a)
        assert c.valAsDeg == pytest.approx(4.8, 0.1)
        assert dist.val == pytest.approx(60.1, 0.1)

    def test_route2(self):
        d : cPosition = myEnv.postionStQuay
        a : cPosition = myEnv.postionTrinitee

        c : cCap
        dist : cDistance
        c, dist  = cPosition.positionementRelatif(d,a)
        assert c.valAsDeg == pytest.approx(184.8, 0.1)
        assert dist.val == pytest.approx(60.1, 0.1)


