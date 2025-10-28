import pytest

from myEnv import myEnv
from tools.angle import angle
from tools.cap import cap
from tools.distance import distance
from tools.latitude import latitude
from tools.longitude import longitude
from tools.position import position



class position_tests:
    def test_init(self):
        l : latitude = latitude(0.0)
        longi : longitude = longitude(0.0)
        p : position = position(lat=l, lon=longi)
        assert p.toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT) == "N 000°00.000',E 000°00.000'"

    def test_fromstring_dd(self):
        p : position = position.fromString("-1.0, -1.0")
        assert p.toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT) == "S 001°00.000',W 001°00.000'"

    def test_fromstring_ddmm(self):
        p : position = position.fromString("N 1°0.0, E 1°0.0")
        assert p.toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT) == "N 001°00.000',E 001°00.000'"

    def test_fromstring_ddmm(self):
        p : position = position.fromString(position.fromString("2, 2").toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT))
        assert p.toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT) == "N 002°00.000',E 002°00.000'"


    def test_gudermann(self):
        p : position = myEnv.postionTrinitee
        x : float = p.gudermannInverse()
        assert x == pytest.approx(1.365, 0.001)

    def test_gudermann2(self):
        p : position = position (lat = latitude(0.0), lon= longitude(0.0))
        x : float = p.gudermannInverse()
        assert x == pytest.approx(0, 0.001)

    def test_gudermann3(self):
        p : position = position (lat = latitude(89.9), lon= longitude(0.0))
        x : float = p.gudermannInverse()
        assert x == pytest.approx(10.162, 0.001)

    def test_route(self):
        a : position = myEnv.postionStQuay
        d : position = myEnv.postionTrinitee

        c : cap
        dist : distance
        c, dist  = position.positionementRelatif(d,a)
        assert c.valAsDeg == pytest.approx(4.8, 0.1)
        assert dist.val == pytest.approx(60.1, 0.1)

    def test_route2(self):
        d : position = myEnv.postionStQuay
        a : position = myEnv.postionTrinitee

        c : cap
        dist : distance
        c, dist  = position.positionementRelatif(d,a)
        assert c.valAsDeg == pytest.approx(184.8, 0.1)
        assert dist.val == pytest.approx(60.1, 0.1)


