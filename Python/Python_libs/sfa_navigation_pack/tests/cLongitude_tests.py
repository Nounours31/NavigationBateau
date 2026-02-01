import pytest
import copy
from sfa_tools import cMyException

from sfa_navigation import cAngle, cLongitude, eAngleFormat
from sfa_navigation.cLongitude import eLongitudeSens


class cLongitude_tests:
    def test_init(self):
        l : cLongitude = cLongitude(0.0)
        assert l.toString(format=eAngleFormat.DMM) == "E 000°00.000'"
        with pytest.raises(cMyException) as err:
            j = cLongitude(valAsDeg=190)
        assert "Longitude invalide" in str(err.value)

        x: eLongitudeSens = eLongitudeSens.E
        assert x.__str__() == "E"
        assert x.__repr__() == "[eLongitudeSens.E]"
        x: eLongitudeSens = eLongitudeSens.W
        assert x.__str__() == "W"
        assert x.__repr__() == "[eLongitudeSens.W]"

    def test_properties(self):
        l : cLongitude = cLongitude(0.0)
        l.val = 10.0
        assert l.val == pytest.approx(10, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l.val = -15.0
        assert l.val == pytest.approx(-15, cAngle.EQUAL_TOLERANCE_IN_DEG)

        with pytest.raises(cMyException) as err:
            l.val = 190.0
        assert "Invalide longitude" in str(err.value)

        x: eLongitudeSens = eLongitudeSens.E
        assert x.__str__() == "E"
        assert x.__repr__() == "[eLongitudeSens.E]"
        x: eLongitudeSens = eLongitudeSens.W
        assert x.__str__() == "W"
        assert x.__repr__() == "[eLongitudeSens.W]"


    def test_fromstring_dd(self):
        x = "-47.12"
        l: cLongitude = cLongitude.fromString(x)
        assert l.toString(format=eAngleFormat.DMM) == "W 047°07.200'"

    def test_fromstring_dd2(self):
        x = "W 47.12"
        l: cLongitude = cLongitude.fromString(x)
        assert l.toString(format=eAngleFormat.DMM) == "W 047°07.200'"

    def test_fromstring_dd3(self):
        x = "W 47°07.200"
        l: cLongitude = cLongitude.fromString(x)
        assert l.toString(format=eAngleFormat.DMM) == "W 047°07.200'"

    def test_fromstring_dd4(self):
        x = "E 47°07.200"
        l: cLongitude = cLongitude.fromString(x)
        assert l.toString(format=eAngleFormat.DMM) == "E 047°07.200'"

    def test_fromstring_dd5(self):
        x = "47°07.200"
        l: cLongitude = cLongitude.fromString(x)
        assert l.toString(format=eAngleFormat.DMM) == "E 047°07.200'"

    def test_fromstring_dd6(self):
        x = "-47°07.200"
        l: cLongitude = cLongitude.fromString(x)
        assert l.toString(format=eAngleFormat.DMM) == "W 047°07.200'"

    def test_fromstring_dd7(self):
        x = "47.12"
        l: cLongitude = cLongitude.fromString(x)
        assert l.toString(format=eAngleFormat.DMM) == "E 047°07.200'"

    def test_fromstring_dd8(self):
        x = "-47.12"
        l: cLongitude = cLongitude.fromString(x)
        assert l.toString(format=eAngleFormat.DMM) == "W 047°07.200'"

    def test_fromstring_ko(self):
        x = "aaaaa"

        with pytest.raises(cMyException) as err:
            l: cLongitude = cLongitude.fromString(x)
        assert ("pas une longitude" in str(err.value))


    def test_add(self):
        x = "-47.12"
        y = "47.12"
        l: cLongitude = cLongitude.fromString(x)
        m: cLongitude = cLongitude.fromString(y)
        n : cLongitude = l + m
        assert n.val == pytest.approx(0.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx( -47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.val == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)

        n = copy.copy(l)
        n += m
        assert n.val == pytest.approx(0.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx( -47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.val == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.deepcopy(l)
        n += m
        assert n.val == pytest.approx(0.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx( -47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.val == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)

        with pytest.raises(cMyException) as err:
            m = l + 3
        assert "type error" in str(err.value)
        with pytest.raises(cMyException) as err:
            m += 3
        assert "type error" in str(err.value)



    def test_sub(self):
        x = "47.12"
        y = "47.12"
        l: cLongitude = cLongitude.fromString(x)
        m: cLongitude = cLongitude.fromString(y)
        n : cLongitude = l - m
        assert n.val == pytest.approx(0.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.val == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)

        n = copy.copy(l)
        n -= m
        assert n.val == pytest.approx(0.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.val == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.deepcopy(l)
        n -= m
        assert n.val == pytest.approx(0.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.val == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)

        with pytest.raises (cMyException) as err:
            m = l - 3
        assert "type error" in str(err.value)
        with pytest.raises(cMyException) as err:
            m -= 3
        assert "type error" in str(err.value)

    def test_mult(self):
        x = "-10.0"
        y = "2.0"
        l: cLongitude = cLongitude.fromString(x)
        m: cLongitude = cLongitude.fromString(y)
        n : cLongitude = l * m
        assert n.val == pytest.approx(-20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx( -10, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.val == pytest.approx(2, cAngle.EQUAL_TOLERANCE_IN_DEG)

        n = copy.copy(l)
        n *= m
        assert n.val == pytest.approx(-20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx( -10, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.val == pytest.approx(2, cAngle.EQUAL_TOLERANCE_IN_DEG)

        x = "-10.0"
        y = "-2.0"
        l: cLongitude = cLongitude.fromString(x)
        m: cLongitude = cLongitude.fromString(y)
        n : cLongitude = l * m
        assert n.val == pytest.approx(20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx( -10, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.val == pytest.approx(-2, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.copy(l)
        n *= m
        assert n.val == pytest.approx(20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx( -10, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.val == pytest.approx(-2, cAngle.EQUAL_TOLERANCE_IN_DEG)

        x = "-10.0"
        l: cLongitude = cLongitude.fromString(x)
        n : cLongitude = l * (-2.0)
        assert n.val == pytest.approx(20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx( -10, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.copy(l)
        n *= (-2.0)
        assert n.val == pytest.approx(20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx( -10, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l: cLongitude = cLongitude.fromString(x)
        n : cLongitude = l * 2
        assert n.val == pytest.approx(-20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx( -10, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.copy(l)
        n *= 2
        assert n.val == pytest.approx(-20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx( -10, cAngle.EQUAL_TOLERANCE_IN_DEG)


        with pytest.raises(cMyException) as err:
            m = l * complex(1,0)
        assert "type error" in str(err.value)
        with pytest.raises(cMyException) as err:
            m *= complex(1,0)
        assert "type error" in str(err.value)



    def test_div(self):
        x = "47.12"
        y = "47.12"
        l: cLongitude = cLongitude.fromString(x)
        m: cLongitude = cLongitude.fromString(y)
        n : cLongitude = l / m
        assert n.val == pytest.approx(1.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.val == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)

        n = copy.copy(l)
        n /= m
        assert n.val == pytest.approx(1.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.val == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)

        x = "47.12"
        y = "-47.12"
        l: cLongitude = cLongitude.fromString(x)
        m: cLongitude = cLongitude.fromString(y)
        n : cLongitude = l / m
        assert n.val == pytest.approx(-1.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.val == pytest.approx(-47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.copy(l)
        n /= m
        assert n.val == pytest.approx(-1.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.val == pytest.approx(-47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)

        x = "4.0"
        l: cLongitude = cLongitude.fromString(x)
        n : cLongitude = l / 2
        assert n.val == pytest.approx(2.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx(4.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.copy(l)
        n /= 2
        assert n.val == pytest.approx(2.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx(4.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l: cLongitude = cLongitude.fromString(x)
        n : cLongitude = l / (-2.0)
        assert n.val == pytest.approx(-2.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx(4.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.copy(l)
        n /= (-2.0)
        assert n.val == pytest.approx(-2.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx(4.0, cAngle.EQUAL_TOLERANCE_IN_DEG)


        with pytest.raises (cMyException) as err:
            m = l / complex(1,0)
        assert "type error" in str(err.value)
        with pytest.raises(cMyException) as err:
            m /= complex(1,0)
        assert "type error" in str(err.value)

    def test_normalise(self):
        l: cLongitude = cLongitude(valAsDeg=10.0, sens=eLongitudeSens.E)
        l.normalise()
        assert l.sens == eLongitudeSens.E

        l: cLongitude = cLongitude(valAsDeg=100.0, sens=eLongitudeSens.W)
        l.normalise()
        assert l.sens == eLongitudeSens.W

        with pytest.raises(cMyException) as err:
            l.val = 190.0
            l.normalise()

        l.sens = eLongitudeSens.W
        assert l.sens == eLongitudeSens.W
        m: cLongitude = cLongitude(valAsDeg=1.0, sens=eLongitudeSens.W)

        l *= m
        assert l.sens == eLongitudeSens.E
        l.sens = eLongitudeSens.W
        assert l.sens == eLongitudeSens.W


    def test_toString(self):
        l : cLongitude = cLongitude (valAsDeg=10.25, sens=eLongitudeSens.E)
        x : list = list()
        x.append((eAngleFormat.DD, "E 010.2500°"))
        x.append((eAngleFormat.DMM,"E 010°15.000'"))
        x.append((eAngleFormat.DMS,"E 010°15'00.000\""))
        x.append((eAngleFormat.RAD,"E 000.1789 rad"))
        x.append((eAngleFormat.Debug,"E 010.2500° - E 010°15.000' [E 010°15'00.000\"]"))
        x.append((eAngleFormat.R8,"E 010.2500"))

        for y in x:
            assert l.toString(y[0]) == y[1]

        l : cLongitude = cLongitude (valAsDeg=10.25414474, sens=eLongitudeSens.W)
        x : list = list()
        x.append((eAngleFormat.DD, "W 010.2541°"))
        x.append((eAngleFormat.DMM,"W 010°15.249'"))
        x.append((eAngleFormat.DMS,"W 010°15'14.921\""))
        x.append((eAngleFormat.RAD,"W 000.1790 rad"))
        x.append((eAngleFormat.Debug,"W 010.2541° - W 010°15.249' [W 010°15'14.921\"]"))
        x.append((eAngleFormat.R8,"W 010.2541"))

        for y in x:
            assert l.toString(y[0]) == y[1]

        assert l.toString("toto") == "Not implemented"

        assert str(l) == l.toString(eAngleFormat.DD)
        assert l.__repr__() == "[cLongitude: W 010.2541°]"

