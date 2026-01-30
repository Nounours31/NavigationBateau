import math
import pytest
from coverage.debug import PytestTracker
from sfa_tools import cMyException

from sfa_navigation import cAngle, eAngleFormat


class cAngle_tests:
    def test_init(self):
        a : cAngle = cAngle(0.0)
        assert a.toString(eAngleFormat.DMM) == "000°00.000'"

    def test_fromstring_dd(self):
        x = "-47.12"
        l: cAngle = cAngle.fromString(x)
        assert l.toString(eAngleFormat.DD) == "-047.1200°"

    def test_fromstring_dd2(self):
        x = "47°12"
        l: cAngle = cAngle.fromString(x)
        assert l.toString(eAngleFormat.DD) == "047.2000°"

    def test_fromstring_dd3(self):
        x = "-47°12"
        l: cAngle = cAngle.fromString(x)
        assert l.toString(eAngleFormat.DD) == "-047.2000°"


    def test_fromstring_dd4(self):
        x = "47°59'59.9999"
        l: cAngle = cAngle.fromString(x)
        assert l.toString(eAngleFormat.DD) == "048.0000°"

    def test_fromstring_dd5(self):
        x = "-47°59'59.9999"
        l: cAngle = cAngle.fromString(x)
        assert l.toString(eAngleFormat.DD) == "-048.0000°"

    def test_fromstring_rad(self):
        l: cAngle = None

        with pytest.raises(cMyException) as err:
            l = cAngle.fromString("- 3.1415 rad")
        assert "Ce n'est pas un angle" in str(err.value)

        l = cAngle.fromString("-3.1415 rad")
        assert l.toString(eAngleFormat.DD) == "-179.9947°"
        l = cAngle.fromString("-3.1415rad")
        assert l.toString(eAngleFormat.DD) == "-179.9947°"
        l = cAngle.fromString("3.1415 rad")
        assert l.toString(eAngleFormat.DD) == "179.9947°"
        l = cAngle.fromString("+3.1415 rad")
        assert l.toString(eAngleFormat.DD) == "179.9947°"


    def test_init(self):
        l: cAngle = cAngle()
        assert l.toString(eAngleFormat.R8) == "000.0000"

        l = cAngle(valAsDeg=180.0)
        assert l.toString(eAngleFormat.R8) == "180.0000"

        l = cAngle(valAsRad=math.pi)
        assert l.valAsDeg == pytest.approx(180.0, abs = cAngle.EQUAL_TOLERANCE_IN_DEG)

    def test_tostring_base(self):
        x = "-47.12"
        l: cAngle = cAngle.fromString(x)
        assert str(l) == "-047.1200°"

    def test_tostring_all(self):
        x = "-47.123"
        l: cAngle = cAngle.fromString(x)
        assert l.toString() == "-047.1230°"
        assert l.toString(eAngleFormat.R8) == "-047.1230"
        assert l.toString(eAngleFormat.DD) == "-047.1230°"
        assert l.toString(eAngleFormat.RAD) == "-000.8225 rad"
        assert l.toString(eAngleFormat.DMM) == "-047°07.380'"
        assert l.toString(eAngleFormat.DMS) == "-047°07'22.800\""

    def test_tostring_zarbi(self):
        x = "-0°"
        l: cAngle = cAngle.fromString(x)
        assert l.toString() == "000.0000°"

        l = cAngle.fromString("24°59'59.99")
        assert l.toString() == "025.0000°"
        assert l.toString(eAngleFormat.DMS) == "024°59'59.990\""
        assert l.valAsDeg == pytest.approx(24.999997222222223, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l = cAngle.fromString("24°59'59.98")
        assert l.toString(eAngleFormat.Debug) == "025.0000° [024°60.000' # 024°59'59.980\"]"

    def test_tostring_error(self):
        x = "-0°"
        l: cAngle = cAngle.fromString(x)

        with pytest.raises(cMyException) as err:
            y : str = l.toString(9)
        assert "Format demande inconnu" in str(err.value)


    def test_setter(self):
        l : cAngle = cAngle(valAsDeg=0.0)
        l.valAsDeg = 90.0
        assert l.valAsDeg == pytest.approx(90.0, cAngle.EQUAL_TOLERANCE_IN_DEG)


    def test_equal(self):
        l1 : cAngle = cAngle(valAsDeg=0.0)
        l2 : cAngle = cAngle(valAsDeg=0.0)
        assert l1 == l2

        l1.valAsDeg = 3.500001
        l2.valAsDeg = 3.5
        assert l1 == l2

        l1.valAsDeg = 3.5
        l2.valAsDeg = 3.6
        assert l1 != l2

        l1.valAsDeg = 3.5
        assert l1 == 3.5

        l1.valAsDeg = 3.5
        assert l1 != 3.6

        with pytest.raises(cMyException) as err:
            b : bool = (l1 == "ggg")
        assert "angle equal: type error" in str(err.value)

