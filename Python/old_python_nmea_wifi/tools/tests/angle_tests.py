import pytest

from tools.angle import angle
from tools.latitude import latitude




class angle_tests:
    def test_init(self):
        a : angle = angle(0.0)
        assert a.toString(base=angle.STR_AsMin) == "000°00.000'"

    def test_fromstring_dd(self):
        x = "-47.12"
        l: angle = angle.fromString(x)
        assert l.toString(base = angle.STR_AsREAL) == "-047.1200°"

    def test_fromstring_dd2(self):
        x = "47°12"
        l: angle = angle.fromString(x)
        assert l.toString(base = angle.STR_AsREAL) == "047.2000°"

    def test_fromstring_dd3(self):
        x = "-47°12"
        l: angle = angle.fromString(x)
        assert l.toString(base = angle.STR_AsREAL) == "-047.2000°"


    def test_fromstring_dd4(self):
        x = "47°59'59.9999"
        l: angle = angle.fromString(x)
        assert l.toString(base = angle.STR_AsREAL) == "048.0000°"

    def test_fromstring_dd5(self):
        x = "-47°59'59.9999"
        l: angle = angle.fromString(x)
        assert l.toString(base = angle.STR_AsREAL) == "-048.0000°"



