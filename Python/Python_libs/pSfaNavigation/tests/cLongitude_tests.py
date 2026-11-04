import pytest
import copy
from pSfaTools.mMyException import cMyException


from pSfaNavigation.mLongitude import cLongitude, eLongitudeSens
from pSfaNavigation.mAngle import cAngle, eAngleFormat


class cLongitude_tests:
    def test_init(self):
        ll: cLongitude = cLongitude(0.0)
        assert ll.toString(format=eAngleFormat.DMM) == "E 000°00.000'"
        with pytest.raises(cMyException) as err:
            j = cLongitude(valAsDeg=190)
            print(str(j))
        assert "Longitude invalide" in str(err.value)

        x: eLongitudeSens = eLongitudeSens.E
        assert x.__str__() == "E"
        assert x.__repr__() == "[eLongitudeSens.E]"
        x: eLongitudeSens = eLongitudeSens.W
        assert x.__str__() == "W"
        assert x.__repr__() == "[eLongitudeSens.W]"

    def test_properties(self):
        ll: cLongitude = cLongitude(0.0)
        ll.longitudeEnDeg = 10.0
        assert ll.longitudeEnDeg == pytest.approx(10, cAngle.EQUAL_TOLERANCE_IN_DEG)

        ll.longitudeEnDeg = -15.0
        assert ll.longitudeEnDeg == pytest.approx(-15, cAngle.EQUAL_TOLERANCE_IN_DEG)

        with pytest.raises(cMyException) as err:
            ll.longitudeEnDeg = 190.0
        assert "Invalide longitude" in str(err.value)

        x: eLongitudeSens = eLongitudeSens.E
        assert x.__str__() == "E"
        assert x.__repr__() == "[eLongitudeSens.E]"
        x: eLongitudeSens = eLongitudeSens.W
        assert x.__str__() == "W"
        assert x.__repr__() == "[eLongitudeSens.W]"

    def test_fromstring_dd(self):
        x = "-47.12"
        ll: cLongitude = cLongitude.fromString(x)
        assert ll.toString(format=eAngleFormat.DMM) == "W 047°07.200'"

    def test_fromstring_dd2(self):
        x = "W 47.12"
        ll: cLongitude = cLongitude.fromString(x)
        assert ll.toString(format=eAngleFormat.DMM) == "W 047°07.200'"

    def test_fromstring_dd3(self):
        x = "W 47°07.200"
        ll: cLongitude = cLongitude.fromString(x)
        assert ll.toString(format=eAngleFormat.DMM) == "W 047°07.200'"

    def test_fromstring_dd4(self):
        x = "E 47°07.200"
        ll: cLongitude = cLongitude.fromString(x)
        assert ll.toString(format=eAngleFormat.DMM) == "E 047°07.200'"

    def test_fromstring_dd5(self):
        x = "47°07.200"
        ll: cLongitude = cLongitude.fromString(x)
        assert ll.toString(format=eAngleFormat.DMM) == "E 047°07.200'"

    def test_fromstring_dd6(self):
        x = "-47°07.200"
        ll: cLongitude = cLongitude.fromString(x)
        assert ll.toString(format=eAngleFormat.DMM) == "W 047°07.200'"

    def test_fromstring_dd7(self):
        x = "47.12"
        ll: cLongitude = cLongitude.fromString(x)
        assert ll.toString(format=eAngleFormat.DMM) == "E 047°07.200'"

    def test_fromstring_dd8(self):
        x = "-47.12"
        ll: cLongitude = cLongitude.fromString(x)
        assert ll.toString(format=eAngleFormat.DMM) == "W 047°07.200'"

    def test_fromstring_ko(self):
        x = "aaaaa"

        with pytest.raises(cMyException) as err:
            ll: cLongitude = cLongitude.fromString(x)
            print(ll.toString())
        assert "pas une longitude" in str(err.value)

    def test_add(self):
        x = "-47.12"
        y = "47.12"
        ll: cLongitude = cLongitude.fromString(x)
        m: cLongitude = cLongitude.fromString(y)
        n: cLongitude = ll + m
        assert n.longitudeEnDeg == pytest.approx(0.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(-47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.longitudeEnDeg == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)

        n = copy.copy(ll)
        n += m
        assert n.longitudeEnDeg == pytest.approx(0.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(-47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.longitudeEnDeg == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.deepcopy(ll)
        n += m
        assert n.longitudeEnDeg == pytest.approx(0.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(-47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.longitudeEnDeg == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)

        m = ll + 3
        assert m.longitudeEnDeg == pytest.approx(-44.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(-47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)

        m += 3
        assert m.longitudeEnDeg == pytest.approx(-41.12, cAngle.EQUAL_TOLERANCE_IN_DEG)

        with pytest.raises(cMyException) as err:
            m = ll + complex(1,1)
        assert "type error" in str(err.value)
        with pytest.raises(cMyException) as err:
            m += complex(1,1)
        assert "type error" in str(err.value)

    def test_sub(self):
        x = "47.12"
        y = "47.12"
        ll: cLongitude = cLongitude.fromString(x)
        m: cLongitude = cLongitude.fromString(y)
        n: cLongitude = ll - m
        assert n.longitudeEnDeg == pytest.approx(0.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.longitudeEnDeg == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)

        n = copy.copy(ll)
        n -= m
        assert n.longitudeEnDeg == pytest.approx(0.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.longitudeEnDeg == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.deepcopy(ll)
        n -= m
        assert n.longitudeEnDeg == pytest.approx(0.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.longitudeEnDeg == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)

        m = ll - 3
        assert ll.longitudeEnDeg == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.longitudeEnDeg == pytest.approx(44.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        m -= 3
        assert m.longitudeEnDeg == pytest.approx(41.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        
        with pytest.raises(cMyException) as err:
            m = ll - complex(1,1)
        assert "type error" in str(err.value)
        with pytest.raises(cMyException) as err:
            m -= complex(1,1)
        assert "type error" in str(err.value)

    def test_mult(self):
        x = "-10.0"
        y = "2.0"
        ll: cLongitude = cLongitude.fromString(x)
        m: cLongitude = cLongitude.fromString(y)
        n: cLongitude = ll * m
        assert n.longitudeEnDeg == pytest.approx(-20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(-10, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.longitudeEnDeg == pytest.approx(2, cAngle.EQUAL_TOLERANCE_IN_DEG)

        n = copy.copy(ll)
        n *= m
        assert n.longitudeEnDeg == pytest.approx(-20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(-10, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.longitudeEnDeg == pytest.approx(2, cAngle.EQUAL_TOLERANCE_IN_DEG)

        x = "-10.0"
        y = "-2.0"
        ll: cLongitude = cLongitude.fromString(x)
        m: cLongitude = cLongitude.fromString(y)
        n: cLongitude = ll * m
        assert n.longitudeEnDeg == pytest.approx(20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(-10, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.longitudeEnDeg == pytest.approx(-2, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.copy(ll)
        n *= m
        assert n.longitudeEnDeg == pytest.approx(20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(-10, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.longitudeEnDeg == pytest.approx(-2, cAngle.EQUAL_TOLERANCE_IN_DEG)

        x = "-10.0"
        ll: cLongitude = cLongitude.fromString(x)
        n: cLongitude = ll * (-2.0)
        assert n.longitudeEnDeg == pytest.approx(20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(-10, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.copy(ll)
        n *= -2.0
        assert n.longitudeEnDeg == pytest.approx(20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(-10, cAngle.EQUAL_TOLERANCE_IN_DEG)

        ll: cLongitude = cLongitude.fromString(x)
        n: cLongitude = ll * 2
        assert n.longitudeEnDeg == pytest.approx(-20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(-10, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.copy(ll)
        n *= 2
        assert n.longitudeEnDeg == pytest.approx(-20.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(-10, cAngle.EQUAL_TOLERANCE_IN_DEG)

        with pytest.raises(cMyException) as err:
            m = ll * complex(1, 0)
        assert "type error" in str(err.value)
        with pytest.raises(cMyException) as err:
            m *= complex(1, 0)
        assert "type error" in str(err.value)

    def test_div(self):
        x = "47.12"
        y = "47.12"
        ll: cLongitude = cLongitude.fromString(x)
        m: cLongitude = cLongitude.fromString(y)
        n: cLongitude = ll / m
        assert n.longitudeEnDeg == pytest.approx(1.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.longitudeEnDeg == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)

        n = copy.copy(ll)
        n /= m
        assert n.longitudeEnDeg == pytest.approx(1.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.longitudeEnDeg == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)

        x = "47.12"
        y = "-47.12"
        ll: cLongitude = cLongitude.fromString(x)
        m: cLongitude = cLongitude.fromString(y)
        n: cLongitude = ll / m
        assert n.longitudeEnDeg == pytest.approx(-1.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.longitudeEnDeg == pytest.approx(-47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.copy(ll)
        n /= m
        assert n.longitudeEnDeg == pytest.approx(-1.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert m.longitudeEnDeg == pytest.approx(-47.12, cAngle.EQUAL_TOLERANCE_IN_DEG)

        x = "4.0"
        ll: cLongitude = cLongitude.fromString(x)
        n: cLongitude = ll / 2
        assert n.longitudeEnDeg == pytest.approx(2.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(4.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.copy(ll)
        n /= 2
        assert n.longitudeEnDeg == pytest.approx(2.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(4.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        ll: cLongitude = cLongitude.fromString(x)
        n: cLongitude = ll / (-2.0)
        assert n.longitudeEnDeg == pytest.approx(-2.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(4.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        n = copy.copy(ll)
        n /= -2.0
        assert n.longitudeEnDeg == pytest.approx(-2.0, cAngle.EQUAL_TOLERANCE_IN_DEG)
        assert ll.longitudeEnDeg == pytest.approx(4.0, cAngle.EQUAL_TOLERANCE_IN_DEG)

        with pytest.raises(cMyException) as err:
            m = ll / complex(1, 0)
        assert "type error" in str(err.value)

        with pytest.raises(cMyException) as err:
            m /= complex(1, 0)
        assert "type error" in str(err.value)

    def test_normalise(self):
        ll: cLongitude = cLongitude(valAsDeg=10.0, sens=eLongitudeSens.E)
        ll.normalise()
        assert ll.sensLongitude == eLongitudeSens.E

        ll: cLongitude = cLongitude(valAsDeg=100.0, sens=eLongitudeSens.W)
        ll.normalise()
        assert ll.sensLongitude == eLongitudeSens.W

        with pytest.raises(cMyException) as err:
            ll.longitudeEnDeg = 190.0
            ll.normalise()
            print(str(err.value))

        ll.sensLongitude = eLongitudeSens.W
        assert ll.sensLongitude == eLongitudeSens.W
        m: cLongitude = cLongitude(valAsDeg=1.0, sens=eLongitudeSens.W)

        ll *= m
        assert ll.sensLongitude == eLongitudeSens.E
        ll.sensLongitude = eLongitudeSens.W
        assert ll.sensLongitude == eLongitudeSens.W

    def test_toString(self):
        ll: cLongitude = cLongitude(valAsDeg=10.25, sens=eLongitudeSens.E)
        x: list = list()
        x.append((eAngleFormat.DD, "E 010.2500°"))
        x.append((eAngleFormat.DMM, "E 010°15.000'"))
        x.append((eAngleFormat.DMS, "E 010°15'00.000\""))
        x.append((eAngleFormat.RAD, "E 000.1789 rad"))
        x.append((eAngleFormat.Debug, "E 010.2500° - E 010°15.000' [E 010°15'00.000\"]"))
        x.append((eAngleFormat.R8, "E 010.2500"))

        for y in x:
            assert ll.toString(y[0]) == y[1]

        ll: cLongitude = cLongitude(valAsDeg=10.25414474, sens=eLongitudeSens.W)
        x: list = list()
        x.append((eAngleFormat.DD, "W 010.2541°"))
        x.append((eAngleFormat.DMM, "W 010°15.249'"))
        x.append((eAngleFormat.DMS, "W 010°15'14.921\""))
        x.append((eAngleFormat.RAD, "W 000.1790 rad"))
        x.append((eAngleFormat.Debug, "W 010.2541° - W 010°15.249' [W 010°15'14.921\"]"))
        x.append((eAngleFormat.R8, "W 010.2541"))

        for y in x:
            assert ll.toString(y[0]) == y[1]

        assert ll.toString("toto") == "Not implemented"

        assert str(ll) == ll.toString(eAngleFormat.DD)
        assert ll.__repr__() == "[Longitude: W 010.2541°]"
