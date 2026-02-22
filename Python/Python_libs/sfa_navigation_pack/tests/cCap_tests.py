import pytest
import copy

from sfa_navigation import cAngle, cCap


class cCap_tests:
    def test_init(self):
        assert cCap().capAsDeg == 0
        assert cCap(0).capAsDeg == 0
        assert cCap(360).capAsDeg == 0
        assert cCap(370).capAsDeg == 10
        assert cCap(-10).capAsDeg == 350
        assert cCap(-10).capAsRad == 350 * cAngle.DEG2RAD
        assert cCap(valAsAngleTrigonometriqueEnDeg=0).capAsDeg == 90.0
        assert cCap(valAsAngleTrigonometriqueEnDeg=90).capAsDeg == 0.0
        assert cCap(valAsAngleTrigonometriqueEnDeg=180).capAsDeg == 270.0
        assert cCap(valAsAngleTrigonometriqueEnDeg=270).capAsDeg == 180.0
        assert cCap(valAsAngleTrigonometriqueEnRad=0 * cAngle.DEG2RAD).capAsDeg == 90.0
        assert cCap(valAsAngleTrigonometriqueEnRad=90 * cAngle.DEG2RAD).capAsDeg == 0.0
        assert cCap(valAsAngleTrigonometriqueEnRad=180 * cAngle.DEG2RAD).capAsDeg == 270.0
        assert cCap(valAsAngleTrigonometriqueEnRad=270 * cAngle.DEG2RAD).capAsDeg == 180.0

    def test_fromString(self):
        assert cCap.fromString("255°").capAsDeg == 255
        assert cCap.fromString("370°").capAsDeg == 10

    def test_init_normalisation(self):
        assert cCap(0).capAsDeg == 0
        assert cCap(360).capAsDeg == 0
        assert cCap(370).capAsDeg == 10
        assert cCap(-10).capAsDeg == 350

    # ---------- ADDITION ----------

    def test_add(self):
        c = cCap(350)
        r = c + 20
        assert isinstance(r, cCap)
        assert r.capAsDeg == 10

    def test_iadd(self):
        c = cCap(350)
        c += 20
        assert c.capAsDeg == 10

    # ---------- MULTIPLICATION ----------

    def test_mul(self):
        c = cCap(45)
        r = c * 3
        assert isinstance(r, cCap)
        assert r.capAsDeg == 135

    def test_mul_overflow(self):
        c = cCap(45)
        r = c * 10
        assert r.capAsDeg == 90

    def test_imul(self):
        c = cCap(100)
        c *= 4
        assert c.capAsDeg == 40

    # ---------- SOUSTRACTION ----------

    def test_sub(self):
        c = cCap(10)
        r = c - 30
        assert r.capAsDeg == 340

    def test_isub(self):
        c = cCap(10)
        c -= 30
        assert c.capAsDeg == 340

    # ---------- DIVISION ----------

    def test_div(self):
        c = cCap(180)
        r = c / 2
        assert r.capAsDeg == 90

    def test_idiv(self):
        c = cCap(180)
        c /= 2
        assert c.capAsDeg == 90

    # ---------- CHAINE ----------

    def test_str(self):
        c = cCap(370)
        s = str(c)
        assert "10.0000" in s
        assert c.__repr__() == "[cCap: 010.0000°]"

    # ---------- COMPATIBILITÉ cAngle ----------

    def test_angle_compatibility(self):
        c = cCap(370)
        a = cAngle(10)

        assert c == a
        assert a == c
        assert c == 10
        assert not c == "10"
        assert c != "10"
        assert c != 11
        assert c != cAngle(11)

    # ---------- STABILITÉ ----------

    def test_many_operations(self):
        c = cCap(0)
        for _ in range(1000):
            c += 123.456
        assert 0 <= c.capAsDeg < 360

    def test_copy(self):
        c = cCap(10)
        d = copy.copy(c)
        d.capAsDeg = 23.3

        assert c != d
        assert d.capAsDeg == 23.3
        assert c.capAsDeg == 10

        d = copy.deepcopy(c)
        d.capAsDeg = 23.3

        assert c != d
        assert d.capAsDeg == 23.3
        assert c.capAsDeg == 10

    def test_property(self):
        c: cCap = cCap(valAsAngleTrigonometriqueEnRad=1.5)
        assert c.asAngleTrigonometriqueEnRad == 1.5
        assert c.asAngleTrigonometriqueEnDeg == pytest.approx(
            1.5 * cAngle.RAD2DEG, cAngle.EQUAL_TOLERANCE_IN_DEG
        )

        c: cCap = cCap(valAsAngleTrigonometriqueEnDeg=10)
        assert c.asAngleTrigonometriqueEnDeg == 10
        assert c.asAngleTrigonometriqueEnRad == pytest.approx(
            10 * cAngle.DEG2RAD, cAngle.EQUAL_TOLERANCE_IN_DEG
        )
