import pytest

from sfa_navigation import cAngle, cLatitude, eAngleFormat


class cLatitude_tests:
    def test_init(self):
        l : cLatitude = cLatitude(0.0)
        assert l.toString(format=eAngleFormat.DMM) == "N 000°00.000'"


    def test_fromstring_dd(self):
        x = "-47.12"
        l: cLatitude = cLatitude.fromString(x)
        assert l.toString(format=eAngleFormat.DMM) == "S 047°07.200'"

    def test_fromstring_dd2(self):
        x = "S 47.12"
        l: cLatitude = cLatitude.fromString(x)
        assert l.toString(format=eAngleFormat.DMM) == "S 047°07.200'"

    def test_fromstring_dd3(self):
        x = "S 47°07.200"
        l: cLatitude = cLatitude.fromString(x)
        assert l.toString(format=eAngleFormat.DMM) == "S 047°07.200'"

    def test_fromstring_dd4(self):
        x = "N 47°07.200"
        l: cLatitude = cLatitude.fromString(x)
        assert l.toString(format=eAngleFormat.DMM) == "N 047°07.200'"

    def test_fromstring_dd5(self):
        x = "47°07.200"
        l: cLatitude = cLatitude.fromString(x)
        assert l.toString(format=eAngleFormat.DMM) == "N 047°07.200'"

    def test_fromstring_dd6(self):
        x = "-47°07.200"
        l: cLatitude = cLatitude.fromString(x)
        assert l.toString(format=eAngleFormat.DMM) == "S 047°07.200'"

    def test_fromstring_dd7(self):
        x = "47.12"
        l: cLatitude = cLatitude.fromString(x)
        assert l.toString(format=eAngleFormat.DMM) == "N 047°07.200'"

    def test_fromstring_dd8(self):
        x = "-47.12"
        l: cLatitude = cLatitude.fromString(x)
        assert l.toString(format=eAngleFormat.DMM) == "S 047°07.200'"

    def test_add(self):
        x = "-47.12"
        y = "47.12"
        l: cLatitude = cLatitude.fromString(x)
        m: cLatitude = cLatitude.fromString(y)
        n : cLatitude = l + m
        assert n.val == pytest.approx(0.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

    def test_sub(self):
        x = "-47.12"
        y = "47.12"
        l: cLatitude = cLatitude.fromString(x)
        m: cLatitude = cLatitude.fromString(y)
        n : cLatitude = l - m
        assert n.val == pytest.approx(0.0, cAngle.EQUAL_TOLERANCE_IN_DEG)


