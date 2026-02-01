import pytest
import copy
from sfa_tools import cMyException

from sfa_navigation import cAngle, cLatitude, eAngleFormat
from sfa_navigation.cLatitude import eLatitudeSens


class cLatitude_tests:
    def test_init(self):
        l : cLatitude = cLatitude(0.0)
        assert l.toString(format=eAngleFormat.DMM) == "N 000°00.000'"
        with pytest.raises(cMyException) as err:
            j = cLatitude(valAsDeg=100)
        assert "Latitude invalide" in str(err.value)

        x: eLatitudeSens = eLatitudeSens.N
        assert x.__str__() == "N"
        assert x.__repr__() == "[eLatitudeSens.N]"
        x: eLatitudeSens = eLatitudeSens.S
        assert x.__str__() == "S"
        assert x.__repr__() == "[eLatitudeSens.S]"

    def test_properties(self):
        l : cLatitude = cLatitude(0.0)
        l.val = 10.0
        assert l.val == pytest.approx(10, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l.val = -15.0
        assert l.val == pytest.approx(-15, cAngle.EQUAL_TOLERANCE_IN_DEG)

        with pytest.raises(cMyException) as err:
            l.val = 110.0
        assert "Invalide latitude" in str(err.value)

        x: eLatitudeSens = eLatitudeSens.N
        assert x.__str__() == "N"
        assert x.__repr__() == "[eLatitudeSens.N]"
        x: eLatitudeSens = eLatitudeSens.S
        assert x.__str__() == "S"
        assert x.__repr__() == "[eLatitudeSens.S]"


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

    def test_fromstring_ko(self):
        x = "aaaaa"

        with pytest.raises(cMyException) as err:
            l: cLatitude = cLatitude.fromString(x)
        assert ("pas une latitude" in str(err.value))


    def test_add(self):
        x = "-47.12"
        y = "47.12"
        l: cLatitude = cLatitude.fromString(x)
        m: cLatitude = cLatitude.fromString(y)
        n : cLatitude = l + m
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
        l: cLatitude = cLatitude.fromString(x)
        m: cLatitude = cLatitude.fromString(y)
        n : cLatitude = l - m
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
        l: cLatitude = cLatitude.fromString(x)
        m: cLatitude = cLatitude.fromString(y)
        n : cLatitude = l * m
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
        l: cLatitude = cLatitude.fromString(x)
        m: cLatitude = cLatitude.fromString(y)
        n : cLatitude = l * m
        assert n.val == pytest.approx(20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx( -10, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.val == pytest.approx(-2, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.copy(l)
        n *= m
        assert n.val == pytest.approx(20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx( -10, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.val == pytest.approx(-2, cAngle.EQUAL_TOLERANCE_IN_DEG)

        x = "-10.0"
        l: cLatitude = cLatitude.fromString(x)
        n : cLatitude = l * (-2.0)
        assert n.val == pytest.approx(20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx( -10, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.copy(l)
        n *= (-2.0)
        assert n.val == pytest.approx(20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx( -10, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l: cLatitude = cLatitude.fromString(x)
        n : cLatitude = l * 2
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
        l: cLatitude = cLatitude.fromString(x)
        m: cLatitude = cLatitude.fromString(y)
        n : cLatitude = l / m
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
        l: cLatitude = cLatitude.fromString(x)
        m: cLatitude = cLatitude.fromString(y)
        n : cLatitude = l / m
        assert n.val == pytest.approx(-1.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.val == pytest.approx(-47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.copy(l)
        n /= m
        assert n.val == pytest.approx(-1.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.val == pytest.approx(-47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)

        x = "4.0"
        l: cLatitude = cLatitude.fromString(x)
        n : cLatitude = l / 2
        assert n.val == pytest.approx(2.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx(4.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.copy(l)
        n /= 2
        assert n.val == pytest.approx(2.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert l.val == pytest.approx(4.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        l: cLatitude = cLatitude.fromString(x)
        n : cLatitude = l / (-2.0)
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
        l: cLatitude = cLatitude(valAsDeg=10.0, sens=eLatitudeSens.N)
        l.normalise()
        assert l.sens == eLatitudeSens.N

        l: cLatitude = cLatitude(valAsDeg=10.0, sens=eLatitudeSens.S)
        l.normalise()
        assert l.sens == eLatitudeSens.S

        with pytest.raises(cMyException) as err:
            l.val = 98.0
            l.normalise()

        l.sens = eLatitudeSens.S
        assert l.sens == eLatitudeSens.S
        m: cLatitude = cLatitude(valAsDeg=1.0, sens=eLatitudeSens.S)

        l *= m
        assert l.sens == eLatitudeSens.N
        l.sens = eLatitudeSens.S
        assert l.sens == eLatitudeSens.S


    def test_toString(self):
        l : cLatitude = cLatitude (valAsDeg=10.25, sens=eLatitudeSens.N)
        x : list = list()
        x.append((eAngleFormat.DD, "N 010.2500°"))
        x.append((eAngleFormat.DMM,"N 010°15.000'"))
        x.append((eAngleFormat.DMS,"N 010°15'00.000\""))
        x.append((eAngleFormat.RAD,"N 000.1789 rad"))
        x.append((eAngleFormat.Debug,"N 010.2500° - N 010°15.000' [N 010°15'00.000\"]"))
        x.append((eAngleFormat.R8,"N 010.2500"))

        for y in x:
            assert l.toString(y[0]) == y[1]

        l : cLatitude = cLatitude (valAsDeg=10.25414474, sens=eLatitudeSens.S)
        x : list = list()
        x.append((eAngleFormat.DD, "S 010.2541°"))
        x.append((eAngleFormat.DMM,"S 010°15.249'"))
        x.append((eAngleFormat.DMS,"S 010°15'14.921\""))
        x.append((eAngleFormat.RAD,"S 000.1790 rad"))
        x.append((eAngleFormat.Debug,"S 010.2541° - S 010°15.249' [S 010°15'14.921\"]"))
        x.append((eAngleFormat.R8,"S 010.2541"))

        for y in x:
            assert l.toString(y[0]) == y[1]

        assert l.toString("toto") == "Not implemented"

        assert str(l) == l.toString(eAngleFormat.DD)
        assert l.__repr__() == "[cLatitude: S 010.2541°]"

