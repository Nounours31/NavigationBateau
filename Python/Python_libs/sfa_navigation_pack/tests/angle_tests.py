import pytest
from sfa_navigation import cAngle, cCap, eAngleFormat


class cAngle_tests:
    def test_init(self):
        a : cAngle = cAngle(0.0)
        assert a.toString(eAngleFormat.DMM) == "000°00.000'"

    def test_fromstring_dd(self):
        x = "-47.12"
        l: cAngle = cAngle.fromString(x)
        assert l.toString(eAngleFormat.DMM) == "-047.1200°"

    def test_fromstring_dd2(self):
        x = "47°12"
        l: cAngle = cAngle.fromString(x)
        assert l.toString(eAngleFormat.DMM) == "047.2000°"

    def test_fromstring_dd3(self):
        x = "-47°12"
        l: cAngle = cAngle.fromString(x)
        assert l.toString(eAngleFormat.DMM) == "-047.2000°"


    def test_fromstring_dd4(self):
        x = "47°59'59.9999"
        l: cAngle = cAngle.fromString(x)
        assert l.toString(eAngleFormat.DMM) == "048.0000°"

    def test_fromstring_dd5(self):
        x = "-47°59'59.9999"
        l: cAngle = cAngle.fromString(x)
        assert l.toString(eAngleFormat.DMM) == "-048.0000°"



