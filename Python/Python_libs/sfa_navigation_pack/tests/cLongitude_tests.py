import pytest

from sfa_navigation import cAngle, cLongitude



class longitude_tests:
    """
    def test_init(self):
        l : cLongitude = cLongitude(0.0)
        assert l.toString(base=cAngle.STR_AsMin, detail=cAngle.DISPLAY_SHORT) == "E 000°00.000'"

    def test_fromstring_dd(self):
        x = "-47.12"
        l: cLongitude = cLongitude.fromString(x)
        assert l.toString(base=cAngle.STR_AsMin, detail=cAngle.DISPLAY_SHORT) == "W 047°07.200'"

    def test_fromstring_dd2(self):
        x = "W 47.12"
        l: cLongitude = cLongitude.fromString(x)
        assert l.toString(base=cAngle.STR_AsMin, detail=cAngle.DISPLAY_SHORT) == "W 047°07.200'"

    def test_fromstring_dd3(self):
        x = "W 47°07.200"
        l: cLongitude = cLongitude.fromString(x)
        assert l.toString(base=cAngle.STR_AsMin, detail=cAngle.DISPLAY_SHORT) == "W 047°07.200'"

    def test_fromstring_dd4(self):
        x = "E 47°07.200"
        l: cLongitude = cLongitude.fromString(x)
        assert l.toString(base=cAngle.STR_AsMin, detail=cAngle.DISPLAY_SHORT) == "E 047°07.200'"

    def test_fromstring_dd5(self):
        x = "47°07.200"
        l: cLongitude = cLongitude.fromString(x)
        assert l.toString(base=cAngle.STR_AsMin, detail=cAngle.DISPLAY_SHORT) == "E 047°07.200'"

    def test_fromstring_dd6(self):
        x = "-47°07.200"
        l: cLongitude = cLongitude.fromString(x)
        assert l.toString(base=cAngle.STR_AsMin, detail=cAngle.DISPLAY_SHORT) == "W 047°07.200'"

    def test_fromstring_dd7(self):
        x = "47.12"
        l: cLongitude = cLongitude.fromString(x)
        assert l.toString(base=cAngle.STR_AsMin, detail=cAngle.DISPLAY_SHORT) == "E 047°07.200'"

    def test_fromstring_dd8(self):
        x = "-47.12"
        l: cLongitude = cLongitude.fromString(x)
        assert l.toString(base=cAngle.STR_AsMin, detail=cAngle.DISPLAY_SHORT) == "W 047°07.200'"

    """


