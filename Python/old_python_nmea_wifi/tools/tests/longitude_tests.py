import pytest

from tools.angle import angle
from tools.longitude import longitude




class longitude_tests:
    def test_init(self):
        l : longitude = longitude(0.0)
        assert l.toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT) == "E 000°00.000'"

    def test_fromstring_dd(self):
        x = "-47.12"
        l: longitude = longitude.fromString(x)
        assert l.toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT) == "W 047°07.200'"

    def test_fromstring_dd2(self):
        x = "W 47.12"
        l: longitude = longitude.fromString(x)
        assert l.toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT) == "W 047°07.200'"

    def test_fromstring_dd3(self):
        x = "W 47°07.200"
        l: longitude = longitude.fromString(x)
        assert l.toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT) == "W 047°07.200'"

    def test_fromstring_dd4(self):
        x = "E 47°07.200"
        l: longitude = longitude.fromString(x)
        assert l.toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT) == "E 047°07.200'"

    def test_fromstring_dd5(self):
        x = "47°07.200"
        l: longitude = longitude.fromString(x)
        assert l.toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT) == "E 047°07.200'"

    def test_fromstring_dd6(self):
        x = "-47°07.200"
        l: longitude = longitude.fromString(x)
        assert l.toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT) == "W 047°07.200'"

    def test_fromstring_dd7(self):
        x = "47.12"
        l: longitude = longitude.fromString(x)
        assert l.toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT) == "E 047°07.200'"

    def test_fromstring_dd8(self):
        x = "-47.12"
        l: longitude = longitude.fromString(x)
        assert l.toString(base=angle.STR_AsMin, detail=angle.DISPLAY_SHORT) == "W 047°07.200'"




