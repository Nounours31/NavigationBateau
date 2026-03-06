import pytest
import copy




from pSfaNavigation.mDistance import cDistance, eDistanceFormat



class cDistance_tests:
    # ========================
    # Constructeur & propriétés
    # ========================

    def test_init_default(self):
        d = cDistance()
        assert d.asMn == 0.0

    def test_fromString(self):
        s: str = " 10.12 Mn"
        d: cDistance = cDistance.fromString(s)
        assert d.asMn == 10.12

        s: str = " 10.12 Mn"
        d: cDistance = cDistance.fromString(s)
        assert d.asMn == 10.12

        s: str = "10,12 Mn"
        d: cDistance = cDistance.fromString(s)
        assert d.asMn == 10.12

        s: str = "10.12Mn"
        d: cDistance = cDistance.fromString(s)
        assert d.asMn == 10.12

        s: str = "10.12mn "
        d: cDistance = cDistance.fromString(s)
        assert d.asMn == 10.12

        s: str = "10.12 km"
        d: cDistance = cDistance.fromString(s)
        assert d.asKm == 10.12

        s: str = "10,12 km"
        d: cDistance = cDistance.fromString(s)
        assert d.asKm == 10.12

        s: str = "10,12km"
        d: cDistance = cDistance.fromString(s)
        assert d.asKm == 10.12

        s: str = "10,12Km"
        d: cDistance = cDistance.fromString(s)
        assert d.asKm == 10.12

        s: str = "10km"
        d: cDistance = cDistance.fromString(s)
        assert d.asKm == 10

        s: str = "10.12"
        d: cDistance = cDistance.fromString(s)
        assert d.asMn == 10.12

        s: str = "12"
        d: cDistance = cDistance.fromString(s)
        assert d.asMn == 12

        s: str = ".12"
        d: cDistance = cDistance.fromString(s)
        assert d.asMn == 0.12

        s: str = "dfdf"
        d: cDistance = cDistance.fromString(s)
        assert d.asMn == 0.0

    def test_init_mn(self):
        d = cDistance(valAsMilleNautique=10)
        assert d.asMn == 10

    def test_init_km(self):
        d = cDistance(valAsKm=18.52)
        assert d.asKm == pytest.approx(18.52, cDistance.EQUAL_TOLERANCE_IN_MN)
        assert d.asMn == pytest.approx(10.0, cDistance.EQUAL_TOLERANCE_IN_MN)

    def test_asKm(self):
        d = cDistance(valAsMilleNautique=10)
        assert d.asKm == pytest.approx(18.52, cDistance.EQUAL_TOLERANCE_IN_MN)
        assert d.asMn == pytest.approx(10.0, cDistance.EQUAL_TOLERANCE_IN_MN)

    # ========================
    # Opérateurs arithmétiques
    # ========================

    def test_add_distance(self):
        d1 = cDistance(5)
        d2 = cDistance(3)
        r = d1 + d2
        assert r.asMn == 8

    def test_add_scalar(self):
        d = cDistance(5)
        r = d + 3
        assert r.asMn == 8

    def test_add(self):
        d = cDistance(5)
        with pytest.raises(TypeError) as err:
            r: cDistance = d + complex(1, 1)
            print(str(err.value))
            print(str(r))

        with pytest.raises(TypeError) as err:
            d += complex(1, 1)
            print(str(err.value))

    def test_iadd(self):
        d = cDistance(5)
        d += cDistance(2)
        assert d.asMn == 7
        d += 2
        assert d.asMn == 9
        d += 2.0
        assert d.asMn == 11

    def test_sub_distance(self):
        d1 = cDistance(10)
        d2 = cDistance(4)
        r = d1 - d2
        assert r.asMn == 6
        r = d1 - 1
        assert r.asMn == 9
        r = d1 - 1.0
        assert r.asMn == 9
        r -= 1
        assert r.asMn == 8
        r -= 1.0
        assert r.asMn == 7
        with pytest.raises(TypeError) as err:
            r: cDistance = d1
            r = d1 - complex(1, 1)
            print(str(err.value))

    def test_isub(self):
        d = cDistance(10)
        d -= 4
        assert d.asMn == 6
        e = cDistance(2)
        d -= e
        assert d.asMn == 4
        with pytest.raises(TypeError) as err:
            r: cDistance = d
            r -= complex(1, 1)
            print(str(err.value))

    def test_mul_scalar(self):
        d = cDistance(3)
        r = d * 4
        assert r.asMn == 12
        e = cDistance(3)
        r = d * e
        assert r.asMn == 9
        with pytest.raises(TypeError) as err:
            r: cDistance = d
            r = d * complex(1, 1)
            print(str(err.value))

    def test_rmul(self):
        d = cDistance(3)
        r = 4 * d
        assert r.asMn == 12

    def test_imul(self):
        d = cDistance(3)
        d *= 4
        assert d.asMn == 12
        e = cDistance(5)
        d *= e
        assert d.asMn == 60
        with pytest.raises(TypeError) as err:
            r: cDistance = d
            r *= complex(1, 1)
            print(str(err.value))

    def test_div_scalar(self):
        d = cDistance(10)
        r = d / 2
        assert r.asMn == 5
        e = cDistance(5)
        r = d / e
        assert r.asMn == 2
        with pytest.raises(TypeError) as err:
            r: cDistance = d
            r /= complex(1, 1)
            print(str(err.value))

        with pytest.raises(TypeError) as err:
            r: cDistance = d
            r = d / complex(1, 1)
            print(str(err.value))

    def test_idiv(self):
        d = cDistance(10)
        d /= 2
        assert d.asMn == 5

    def test_div_distance_ratio(self):
        d1 = cDistance(10)
        d2 = cDistance(2)
        r = d1 / d2
        assert r.asMn == 5
        d1 /= d2
        assert d1.asMn == 5

    # ========================
    # Comparaisons
    # ========================

    def test_eq(self):
        d1 = cDistance(10)
        d2 = cDistance(10)
        assert d1 == d2
        assert d1 == 10
        assert cDistance(5) != complex(5, 0)

    def test_eq_tolerance(self):
        d1 = cDistance(10.000000001)
        d2 = cDistance(10.000000002)
        assert d1 == d2

    def test_ne(self):
        d1 = cDistance(10)
        d2 = cDistance(11)
        assert d1 != d2

    def test_lt(self):
        assert cDistance(5) < cDistance(10)
        assert cDistance(5) < 10
        with pytest.raises(TypeError) as err:
            assert cDistance(5) < complex(10, 0)
            print(str(err.value))

    def test_le(self):
        assert cDistance(5) <= cDistance(5)
        assert cDistance(5) <= cDistance(6)
        assert cDistance(5) <= 6

    def test_gt(self):
        assert cDistance(10) > cDistance(5)
        assert cDistance(5) > 0
        with pytest.raises(TypeError) as err:
            assert cDistance(5) > complex(0, 0)
            print(str(err.value))

    def test_ge(self):
        assert cDistance(10) >= cDistance(10)
        assert cDistance(10) >= cDistance(5)
        assert cDistance(10) >= 5

    # ========================
    # Affichage
    # ========================

    def test_str(self):
        d = cDistance(10)
        s = str(d)
        assert "Mn" in s
        assert "km" not in s
        s = d.toString(format=eDistanceFormat.FULL)
        assert s == "10.000Mn (18.520 km)"

    def test_repr(self):
        d = cDistance(7.5)
        assert repr(d) == "[cDistance(7.500)]"

    def test_copy(self):
        c = cDistance(10)
        d = copy.copy(c)
        d.asMn = 5

        assert c != d
        assert d.asMn == 5
        assert c.asMn == 10

        d = copy.deepcopy(c)
        d.asMn = 5

        assert c != d
        assert d.asMn == 5
        assert c.asMn == 10
