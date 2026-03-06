import copy
import math

import pytest
from pSfaTools.mMyException import cMyException

from pSfaNavigation.mCap import cCap
from pSfaNavigation.mVelocite import cVelocite, cVitesse


# =========================
# Fixtures
# =========================


@pytest.fixture
def vecteur_base():
    return cVelocite(vitesse=cVitesse(valAsNoeud=10.0), sens=cCap(valAsDeg=90.0))


@pytest.fixture
def vecteur_autre():
    return cVelocite(vitesse=cVitesse(valAsNoeud=5.0), sens=cCap(valAsDeg=0.0))


class cVitesse_tests:
    def test_init(self):
        v: cVelocite = cVelocite()
        assert v is not None

    # =========================
    # Constructeur
    # =========================

    def test_init_default(self):
        v = cVelocite()
        assert v.vitesse.asNoeud == 0.0
        assert v.sens.capAsDeg == 0.0

    def test_init_with_values(self):
        d = cVitesse(valAsNoeud=12.0)
        c = cCap(valAsDeg=270)

        v = cVelocite(d, c)

        assert v.vitesse == d
        assert v.sens == c

    # =========================
    # Getters / Setters
    # =========================

    def test_set_distance(self, vecteur_base):
        d = cVitesse(valAsNoeud=25.0)
        vecteur_base.vitesse = d
        assert vecteur_base.vitesse == d

    def test_set_distance_wrong_type(self, vecteur_base):
        with pytest.raises(cMyException) as err:
            vecteur_base.vitesse = 12
            print(str(err.value))

    def test_set_sens(self, vecteur_base):
        c = cCap(valAsDeg=180)
        vecteur_base.sens = c
        assert vecteur_base.sens == c

    def test_set_sens_wrong_type(self, vecteur_base):
        with pytest.raises(cMyException) as err:
            vecteur_base.sens = 12
            print(str(err.value))

    # =========================
    # Addition / Soustraction
    # =========================

    def test_addition(self, vecteur_base, vecteur_autre):
        v = vecteur_base + vecteur_autre
        assert isinstance(v, cVelocite)
        assert v.vitesse.asNoeud > 0
        with pytest.raises(TypeError) as err:
            v = vecteur_base + "2"
            print(str(err.value))

        v = cVelocite(vitesse=10, sens=90) + cVelocite(vitesse=10, sens=90)
        assert v.sens == cCap(0)
        assert v.vitesse == cVitesse(20)

        v = cVelocite(vitesse=4, sens=0) + cVelocite(vitesse=3, sens=90)
        assert v.sens == cCap(36.8699)
        assert v.vitesse == cVitesse(5)

        x: float = 4
        v = cVelocite(vitesse=x, sens=0) + cVelocite(vitesse=x, sens=90)
        assert v.sens == cCap(45)
        assert v.vitesse == cVitesse(valAsNoeud= math.sqrt(2 * x * x))

    def test_iadd(self, vecteur_base, vecteur_autre):
        vecteur_base += vecteur_autre
        assert vecteur_base.vitesse.asNoeud > 0
        with pytest.raises(TypeError) as err:
            vecteur_base += "2"
            print(str(err.value))

    def test_subtraction(self, vecteur_base, vecteur_autre):
        v = vecteur_base - vecteur_autre
        assert isinstance(v, cVelocite)
        assert v.vitesse.asNoeud >= 0
        with pytest.raises(TypeError) as err:
            v = vecteur_base - "2"
            print(str(err.value))

        v = cVelocite(vitesse=10, sens=90) - cVelocite(vitesse=10, sens=90)
        assert v.sens == cCap(0)
        assert v.vitesse == cVitesse(0)

        v = cVelocite(vitesse=4, sens=0) - cVelocite(vitesse=3, sens=90)
        assert v.sens == cCap(323.1301)
        assert v.vitesse == cVitesse(5)

        x: float = 4
        v = cVelocite(vitesse=x, sens=0) - cVelocite(vitesse=x, sens=90)
        assert v.sens == cCap(315)
        assert v.vitesse == cVitesse(math.sqrt(2 * x * x))

    def test_isub(self, vecteur_base, vecteur_autre):
        vecteur_base -= vecteur_autre
        assert vecteur_base.vitesse.asNoeud >= 0
        with pytest.raises(TypeError) as err:
            vecteur_base -= "2"
            print(str(err.value))

    # =========================
    # Multiplication / Division
    # =========================

    @pytest.mark.parametrize("coef", [2, 0.5, -1])
    def test_mul(self, vecteur_base, coef):
        v = vecteur_base * coef
        assert pytest.approx(v.vitesse.asNoeud) == vecteur_base.vitesse.asNoeud * coef
        with pytest.raises(TypeError) as err:
            v = vecteur_base * "2"
            print(str(err.value))

    def test_rmul(self, vecteur_base):
        v = 2 * vecteur_base
        assert pytest.approx(v.vitesse.asNoeud) == vecteur_base.vitesse.asNoeud * 2
        with pytest.raises(TypeError) as err:
            v = "2" * vecteur_base
            print(str(err.value))

    def test_imul(self, vecteur_base):
        vecteur_base *= 3
        assert pytest.approx(vecteur_base.vitesse.asNoeud) == 30
        with pytest.raises(TypeError) as err:
            vecteur_base *= "2"
            print(str(err.value))

    @pytest.mark.parametrize("coef", [2, 4])
    def test_truediv(self, vecteur_base, coef):
        v = vecteur_base / coef
        assert pytest.approx(v.vitesse.asNoeud) == 10.0 / coef
        with pytest.raises(TypeError) as err:
            v = vecteur_base / "2"
            print(str(err.value))

    def test_itruediv(self, vecteur_base):
        vecteur_base /= 2
        assert pytest.approx(vecteur_base.vitesse.asNoeud) == 5
        with pytest.raises(TypeError) as err:
            vecteur_base /= "2"
            print(str(err.value))

    # =========================
    # Comparaisons
    # =========================

    def test_eq(self):
        v1 = cVelocite(cVitesse(10), cCap(90))
        v2 = cVelocite(cVitesse(10), cCap(90))
        assert v1 == v2

    def test_ne(self):
        v1 = cVelocite(cVitesse(10), cCap(90))
        v2 = cVelocite(cVitesse(5), cCap(90))
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
        assert "[cVitesse(" in r
