import copy
import math

import pytest
from sfa_tools import cMyException

from sfa_navigation import cVecteur, cDistance, cCap
from myEnv import myEnv


# =========================
# Fixtures
# =========================

@pytest.fixture
def vecteur_base():
    return cVecteur(
        distance=cDistance(valAsMilleNautique=10.0),
        sens=cCap(valAsDeg=90.0)
    )


@pytest.fixture
def vecteur_autre():
    return cVecteur(
        distance=cDistance(valAsMilleNautique=5.0),
        sens=cCap(valAsDeg=0.0)
    )


class cVecteur_tests:
    def test_init(self):
        v : cVecteur = cVecteur()
        assert v != None



    # =========================
    # Constructeur
    # =========================

    def test_init_default(self):
        v = cVecteur()
        assert v.distance.asMn == 0.0
        assert v.sens.capAsDeg == 0.0


    def test_init_with_values(self):
        d = cDistance(valAsMilleNautique=12.5)
        c = cCap(valAsDeg=270)

        v = cVecteur(d, c)

        assert v.distance == d
        assert v.sens == c


    # =========================
    # Getters / Setters
    # =========================

    def test_set_distance(self, vecteur_base):
        d = cDistance(valAsMilleNautique=25)
        vecteur_base.distance = d
        assert vecteur_base.distance == d


    def test_set_distance_wrong_type(self, vecteur_base):
        with pytest.raises(cMyException):
            vecteur_base.distance = 12


    def test_set_sens(self, vecteur_base):
        c = cCap(valAsDeg=180)
        vecteur_base.sens = c
        assert vecteur_base.sens == c


    def test_set_sens_wrong_type(self, vecteur_base):
        with pytest.raises(cMyException):
            vecteur_base.sens = 12


    # =========================
    # Addition / Soustraction
    # =========================

    def test_addition(self, vecteur_base, vecteur_autre):
        v = vecteur_base + vecteur_autre
        assert isinstance(v, cVecteur)
        assert v.distance.asMn > 0
        with pytest.raises(TypeError) as err:
            v = vecteur_base + "2"
        v = cVecteur(distance=10, sens=90) + cVecteur(distance=10, sens=90)
        assert v.sens == cCap(0)
        assert v.distance == cDistance(20)

        v = cVecteur(distance=4, sens=0) + cVecteur(distance=3, sens=90)
        assert v.sens == cCap(36.8699)
        assert v.distance == cDistance(5)

        x : float = 4
        v = cVecteur(distance=x, sens=0) + cVecteur(distance=x, sens=90)
        assert v.sens == cCap(45)
        assert v.distance == cDistance(math.sqrt(2*x*x))


    def test_iadd(self, vecteur_base, vecteur_autre):
        vecteur_base += vecteur_autre
        assert vecteur_base.distance.asMn > 0
        with pytest.raises(TypeError) as err:
            vecteur_base += "2"


    def test_subtraction(self, vecteur_base, vecteur_autre):
        v = vecteur_base - vecteur_autre
        assert isinstance(v, cVecteur)
        assert v.distance.asMn >= 0
        with pytest.raises(TypeError) as err:
            v = vecteur_base - "2"
        v = cVecteur(distance=10, sens=90) - cVecteur(distance=10, sens=90)
        assert v.sens == cCap(0)
        assert v.distance == cDistance(0)

        v = cVecteur(distance=4, sens=0) - cVecteur(distance=3, sens=90)
        assert v.sens == cCap(323.1301)
        assert v.distance == cDistance(5)

        x : float = 4
        v = cVecteur(distance=x, sens=0) - cVecteur(distance=x, sens=90)
        assert v.sens == cCap(315)
        assert v.distance == cDistance(math.sqrt(2*x*x))

    def test_isub(self, vecteur_base, vecteur_autre):
        vecteur_base -= vecteur_autre
        assert vecteur_base.distance.asMn >= 0
        with pytest.raises(TypeError) as err:
            vecteur_base -= "2"


    # =========================
    # Multiplication / Division
    # =========================

    @pytest.mark.parametrize("coef", [2, 0.5, -1])
    def test_mul(self, vecteur_base, coef):
        v = vecteur_base * coef
        assert pytest.approx(v.distance.asMn) == vecteur_base.distance.asMn * coef
        with pytest.raises(TypeError) as err:
            v = vecteur_base * "2"


    def test_rmul(self, vecteur_base):
        v = 2 * vecteur_base
        assert pytest.approx(v.distance.asMn) == vecteur_base.distance.asMn * 2
        with pytest.raises(TypeError) as err:
            v = "2" * vecteur_base


    def test_imul(self, vecteur_base):
        vecteur_base *= 3
        assert pytest.approx(vecteur_base.distance.asMn) == 30
        with pytest.raises(TypeError) as err:
            vecteur_base *= "2"



    @pytest.mark.parametrize("coef", [2, 4])
    def test_truediv(self, vecteur_base, coef):
        v = vecteur_base / coef
        assert pytest.approx(v.distance.asMn) == 10.0 / coef
        with pytest.raises(TypeError) as err:
            v = vecteur_base / "2"


    def test_itruediv(self, vecteur_base):
        vecteur_base /= 2
        assert pytest.approx(vecteur_base.distance.asMn) == 5
        with pytest.raises(TypeError) as err:
            vecteur_base /= "2"


    # =========================
    # Comparaisons
    # =========================

    def test_eq(self):
        v1 = cVecteur(cDistance(10), cCap(90))
        v2 = cVecteur(cDistance(10), cCap(90))
        assert v1 == v2


    def test_ne(self):
        v1 = cVecteur(cDistance(10), cCap(90))
        v2 = cVecteur(cDistance(5), cCap(90))
        assert v1 != v2


    def test_eq_wrong_type(self, vecteur_base):
        assert vecteur_base != 12


    # =========================
    # Copy / Deepcopy
    # =========================

    def test_copy(self, vecteur_base):
        v = copy.copy(vecteur_base)
        assert v == vecteur_base
        assert v is not vecteur_base


    def test_deepcopy(self, vecteur_base):
        v = copy.deepcopy(vecteur_base)
        assert v == vecteur_base
        assert v is not vecteur_base


    # =========================
    # Str / Repr
    # =========================

    def test_str(self, vecteur_base):
        s = str(vecteur_base)
        assert isinstance(s, str)
        assert len(s) > 0


    def test_repr(self, vecteur_base):
        r = repr(vecteur_base)
        assert "[cVecteur(" in r