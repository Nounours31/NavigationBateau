import pytest
from pSfaTools.mMyException import cMyException

from pSfaNavigation.mNavigationFormules import cNavigationFormules, cMethodeCalcul
from pSfaNavigation.mCap import cCap
from pSfaNavigation.mDistance import cDistance
from pSfaNavigation.mVelocite import cVelocite, cVitesse
from pSfaNavigation.mPosition import cPosition
from pSfaNavigation.mLatitude import cLatitude, eLatitudeSens
from pSfaNavigation.mLongitude import cLongitude, eLongitudeSens
from pSfaNavigation.mAngle import cAngle, eAngleFormat

from myEnv import myEnv


class position_tests:
    def test_init(self):
        ll: cLatitude = cLatitude(0.0)
        longi: cLongitude = cLongitude(0.0)
        p: cPosition = cPosition(lat=ll, lon=longi)
        assert p.toString(format=eAngleFormat.DMM) == "N 000°00.000', E 000°00.000'"

    def test_fromstring_dd(self):
        p: cPosition = cPosition.fromString("-1.0, -1.0")
        assert p.toString(format=eAngleFormat.DMM) == "S 001°00.000', W 001°00.000'"

    def test_fromstring_ddmm(self):
        p: cPosition = cPosition.fromString("N 1°0.0, E 1°0.0")
        assert p.toString(format=eAngleFormat.DMM) == "N 001°00.000', E 001°00.000'"

    def test_fromstring_ddmm2(self):
        p: cPosition = cPosition.fromString(
            cPosition.fromString("2, 2").toString(format=eAngleFormat.DMM)
        )
        assert p.toString(format=eAngleFormat.DMM) == "N 002°00.000', E 002°00.000'"

    def test_fromstring_ko(self):
        with pytest.raises(cMyException) as err:
            p: cPosition = cPosition.fromString("N 110°0.0, E 4°0.0")
            print(p.toString())
            print(str(err.value))

        with pytest.raises(cMyException) as err:
            p: cPosition = cPosition.fromString("N 10°0.0, E 200°0.0")
            print(p.toString())
            print(str(err.value))

    def test_properties(self):
        p: cPosition = cPosition.fromString("-1.0, -1.0")
        p.latitude = cLatitude(valAsDeg=45.0)
        p.longitude = cLongitude(valAsDeg=45.0)
        assert p.toString(format=eAngleFormat.DMM) == "N 045°00.000', E 045°00.000'"

    def test_properties_2(self):
        p: cPosition = cPosition.fromString("-1.0, -1.0")
        p.latitude = cLatitude(valAsDeg=45.0)
        p.longitude = cLongitude(valAsDeg=45.0)
        assert p.toString(format=eAngleFormat.DMM) == "N 045°00.000', E 045°00.000'"

    def test_gudermann(self):
        p: cPosition = myEnv.postionTrinitee
        x: float = p.gudermannInverse()
        assert x == pytest.approx(0.9461, 0.001)

    def test_gudermann2(self):
        p: cPosition = cPosition(lat=cLatitude(0.0), lon=cLongitude(0.0))
        x: float = p.gudermannInverse()
        assert x == pytest.approx(0, 0.001)

    def test_gudermann3(self):
        p: cPosition = cPosition(lat=cLatitude(89.9), lon=cLongitude(0.0))
        x: float = p.gudermannInverse()
        assert x == pytest.approx(7.0439, 0.001)
