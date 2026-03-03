import math
import pytest
import copy

from sfa_tools import cMyException

from sfa_navigation import cAngle, eAngleFormat


class cAngle_tests:
    def test_infra(self):
        x: eAngleFormat = eAngleFormat.DD
        for e in eAngleFormat:
            s: str = str(e)
            s = e.__repr__()
            print(s)

        ll: list = list()
        ll.append((x, "DD"))
        ll.append((eAngleFormat.DD, "DD"))
        ll.append((eAngleFormat.DMS, "DMS"))
        ll.append((eAngleFormat.DMM, "DMM"))
        ll.append((eAngleFormat.DD, "DD"))
        ll.append((eAngleFormat.R8, "Reel"))
        ll.append((eAngleFormat.RAD, "Rad"))
        ll.append((eAngleFormat.Debug, "Debug"))

        for y in ll:
            assert str(y[0]) == y[1]
            assert y[0].__repr__() == "[eAngleFormat: " + y[1] + "]"

    def test_init_vide(self):
        a: cAngle = cAngle(0.0)
        assert a.toString(eAngleFormat.DMM) == "000°00.000'"
        assert a.__repr__() == "[cAngle: 000.0000°]"

    def test_fromstring_dd(self):
        x = "-47.12"
        ll: cAngle = cAngle.fromString(x)
        assert ll.toString(eAngleFormat.DD) == "-047.1200°"

    def test_fromObject(self) :
        x = "47°12"
        ll: cAngle = cAngle.fromObject(x)
        assert ll.toString(eAngleFormat.DD) == "047.2000°"

        ll: cAngle = cAngle.fromObject(47.2)
        assert ll.toString(eAngleFormat.DD) == "047.2000°"

        ll: cAngle = cAngle.fromObject(47.2)
        l2: cAngle = cAngle.fromObject(ll)
        assert l2.toString(eAngleFormat.DD) == "047.2000°"

        with pytest.raises(cMyException):
            ll = cAngle.fromObject(complex(1,1))

    def test_fromstring_dd2(self):
        x = "47°12"
        ll: cAngle = cAngle.fromString(x)
        assert ll.toString(eAngleFormat.DD) == "047.2000°"

    def test_fromstring_dd3(self):
        x = "-47°12"
        ll: cAngle = cAngle.fromString(x)
        assert ll.toString(eAngleFormat.DD) == "-047.2000°"

    def test_fromstring_dd4(self):
        x = "47°59'59.9999"
        ll: cAngle = cAngle.fromString(x)
        assert ll.toString(eAngleFormat.DD) == "048.0000°"

    def test_fromstring_dd5(self):
        x = "-47°59'59.9999"
        ll: cAngle = cAngle.fromString(x)
        assert ll.toString(eAngleFormat.DD) == "-048.0000°"

    def test_fromstring_rad(self):
        ll: cAngle 

        with pytest.raises(cMyException) as err:
            ll = cAngle.fromString("- 3.1415 rad")
        assert "Ce n'est pas un angle" in str(err.value)

        ll = cAngle.fromString("-3.1415 rad")
        assert ll.toString(eAngleFormat.DD) == "-179.9947°"
        ll = cAngle.fromString("-3.1415rad")
        assert ll.toString(eAngleFormat.DD) == "-179.9947°"
        ll = cAngle.fromString("3.1415 rad")
        assert ll.toString(eAngleFormat.DD) == "179.9947°"
        ll = cAngle.fromString("+3.1415 rad")
        assert ll.toString(eAngleFormat.DD) == "179.9947°"

    def test_init(self):
        ll: cAngle = cAngle()
        assert ll.toString(eAngleFormat.R8) == "000.0000"

        ll = cAngle(valAsDeg=180.0)
        assert ll.toString(eAngleFormat.R8) == "180.0000"

        ll = cAngle(valAsRad=math.pi)
        assert ll.angleAsDeg == pytest.approx(180.0, abs=cAngle.EQUAL_TOLERANCE_IN_DEG)

    def test_tostring_base(self):
        x = "-47.12"
        ll: cAngle = cAngle.fromString(x)
        assert str(ll) == "-047.1200°"

    def test_tostring_all(self):
        x = "-47.123"
        ll: cAngle = cAngle.fromString(x)
        assert ll.toString() == "-047.1230°"
        assert ll.toString(eAngleFormat.R8) == "-047.1230"
        assert ll.toString(eAngleFormat.DD) == "-047.1230°"
        assert ll.toString(eAngleFormat.RAD) == "-000.8225 rad"
        assert ll.toString(eAngleFormat.DMM) == "-047°07.380'"
        assert ll.toString(eAngleFormat.DMS) == "-047°07'22.800\""

    def test_tostring_zarbi(self):
        x = "-0°"
        ll: cAngle = cAngle.fromString(x)
        assert ll.toString() == "000.0000°"

        ll = cAngle.fromString("24°59'59.99")
        assert ll.toString() == "025.0000°"
        assert ll.toString(eAngleFormat.DMS) == "024°59'59.990\""
        assert ll.angleAsDeg == pytest.approx(24.999997222222223, cAngle.EQUAL_TOLERANCE_IN_DEG)

        ll = cAngle.fromString("24°59'59.98")
        assert ll.toString(eAngleFormat.Debug) == "025.0000° [024°60.000' # 024°59'59.980\"]"

    def test_tostring_error(self):
        x = "-0°"
        ll: cAngle = cAngle.fromString(x)

        with pytest.raises(cMyException) as err:
            y: str = ll.toString(9)
            print(y)
        assert "Format demande inconnu" in str(err.value)

    def test_setter(self):
        ll: cAngle = cAngle(valAsDeg=0.0)
        ll.angleAsDeg = 90.0
        assert ll.angleAsDeg == pytest.approx(90.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

    def test_equal(self):
        l1: cAngle = cAngle(valAsDeg=0.0)
        l2: cAngle = cAngle(valAsDeg=0.0)
        assert l1 == l2

        l1.angleAsDeg = 3.500001
        l2.angleAsDeg = 3.5
        assert l1 == l2

        l1.angleAsDeg = 3.5
        l2.angleAsDeg = 3.6
        assert l1 != l2

        l1.angleAsDeg = 3.5
        assert l1 == 3.5

        l1.angleAsDeg = 3.0
        assert l1 == 3

        l1.angleAsDeg = 3.5
        assert l1 != 3.6

        with pytest.raises(cMyException) as err:
            b: bool = l1 == "ggg"
            assert not b
        assert "angle equal: type error" in str(err.value)

    def test_addition(self):
        l1: cAngle = cAngle(valAsDeg=45.0)
        l2: cAngle = cAngle(valAsDeg=45.0)
        assert l1 == l2
        assert l1.angleAsDeg == pytest.approx(45.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l3: cAngle = l1 + l2
        assert l3.angleAsDeg == pytest.approx(90.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l3 = l3 + 90.0
        assert l3.angleAsDeg == pytest.approx(180.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l4 = 90.0 + l3
        assert l3.angleAsDeg == pytest.approx(180.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l4.angleAsDeg == pytest.approx(270.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l3 += l1
        assert l3.angleAsDeg == pytest.approx(225.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l1.angleAsDeg == pytest.approx(45.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l3 += 45.0
        assert l3.angleAsDeg == pytest.approx(270.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        with pytest.raises(cMyException) as err:
            l3 += complex(2, 3)
        assert "angle add: type error" in str(err.value)

        with pytest.raises(cMyException) as err:
            l3 = l1 + complex(2, 3)
        assert "angle add: type error" in str(err.value)

    def test_substarct(self):
        l1: cAngle = cAngle(valAsDeg=45.0)
        l2: cAngle = cAngle(valAsDeg=45.0)
        assert l1 == l2
        assert l1.angleAsDeg == pytest.approx(45.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l3: cAngle = l1 - l2
        assert l3.angleAsDeg == pytest.approx(0.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l3 = l3 + 90.0
        l3 = l3 - 45.0
        assert l3.angleAsDeg == pytest.approx(45.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l4 = 45.0 - l3
        assert l3.angleAsDeg == pytest.approx(45.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l4.angleAsDeg == pytest.approx(0.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l3 -= l1
        assert l3.angleAsDeg == pytest.approx(0.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l1.angleAsDeg == pytest.approx(45.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l3 -= 45.0
        assert l3.angleAsDeg == pytest.approx(-45.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        with pytest.raises(cMyException) as err:
            l3 -= "ggg"
        assert "angle add: type error" in str(err.value)

        with pytest.raises(cMyException) as err:
            l3 = l1 - ("ggg")
        assert "angle add: type error" in str(err.value)

    def test_multi(self):
        l1: cAngle = cAngle(valAsDeg=45.0)
        l2: cAngle = cAngle(valAsDeg=45.0)
        assert l1 == l2
        assert l1.angleAsDeg == pytest.approx(45.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l3: cAngle = l1 * 2.0
        assert l3.angleAsDeg == pytest.approx(90.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l3: cAngle = l1 * (-1.0)
        assert l3.angleAsDeg == pytest.approx(-45.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l4: cAngle = (-1.0) * l1
        assert l4.angleAsDeg == pytest.approx(-45.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l5: cAngle = l1 * l2
        assert l5.angleAsDeg == pytest.approx(2025.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l4 *= 10.0
        assert l4.angleAsDeg == pytest.approx(-450.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l3 *= l4
        assert l3.angleAsDeg == pytest.approx(20250.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l4.angleAsDeg == pytest.approx(-450.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        with pytest.raises(cMyException) as err:
            l3 = l1 * complex(1, 1)
        assert "angle add: type error" in str(err.value)
        with pytest.raises(cMyException) as err:
            l3 = complex(1, 1) * l1
        assert "angle add: type error" in str(err.value)
        with pytest.raises(cMyException) as err:
            l3 *= complex(1, 1)
        assert "angle add: type error" in str(err.value)

    def test_divise(self):
        l1: cAngle = cAngle(valAsDeg=45.0)
        l2: cAngle = cAngle(valAsDeg=45.0)
        assert l1 == l2
        assert l1.angleAsDeg == pytest.approx(45.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l3: cAngle = l1 / 2.0
        assert l3.angleAsDeg == pytest.approx(22.5, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l3: cAngle = l1 / (-1.0)
        assert l3.angleAsDeg == pytest.approx(-45.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l4: cAngle = (-1.0) / l1
        assert l4.angleAsDeg == pytest.approx(-0.02222222222222222, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l1.angleAsDeg == pytest.approx(45.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l5: cAngle = l1 / l2
        assert l5.angleAsDeg == pytest.approx(1.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l1.angleAsDeg == pytest.approx(45.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l2.angleAsDeg == pytest.approx(45.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l4 = copy.copy(l1)
        l4 /= 45.0
        assert l4.angleAsDeg == pytest.approx(1.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l1.angleAsDeg == pytest.approx(45.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l4 = copy.deepcopy(l1)
        l4 /= 45.0
        assert l4.angleAsDeg == pytest.approx(1.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l1.angleAsDeg == pytest.approx(45.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l4 *= 10.0
        l3 /= l4
        assert l3.angleAsDeg == pytest.approx(-4.50, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l4.angleAsDeg == pytest.approx(10.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        with pytest.raises(cMyException) as err:
            l3 = l1 / complex(1, 1)
        assert "angle add: type error" in str(err.value)
        with pytest.raises(cMyException) as err:
            l3 = complex(1, 1) / l1
        assert "angle add: type error" in str(err.value)
        with pytest.raises(cMyException) as err:
            l3 /= complex(1, 1)
        assert "angle add: type error" in str(err.value)

    def test_normalise(self):
        l1: cAngle = cAngle(valAsDeg=45.0)
        l2: cAngle = cAngle(valAsDeg=45.0)
        assert l1 == l2
        assert l1.angleAsDeg == pytest.approx(45.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l3: cAngle = l1 * 100.0
        assert l3.angleAsDeg == pytest.approx(4500.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l3.normalise()
        assert l3.angleAsDeg == pytest.approx(180.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l2 = cAngle(361.0)
        l2.normalise()
        assert l2.angleAsDeg == pytest.approx(1.0, cAngle.EQUAL_TOLERANCE_IN_DEG)


        l2 = cAngle(-1.0)
        l2.normalise()
        assert l2.angleAsDeg == pytest.approx(359.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l2 = cAngle(-500.0)
        l2.normalise()
        assert l2.angleAsDeg == pytest.approx(220.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l2 = cAngle(5000.0)
        l2.normalise()
        assert l2.angleAsDeg == pytest.approx(320.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

    def test_properties(self):
        a: cAngle = cAngle(180.0)
        assert a.angleAsRad == pytest.approx(math.pi, cAngle.EQUAL_TOLERANCE_IN_DEG)
