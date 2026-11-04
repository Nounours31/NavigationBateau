from pytest import approx
from cAstro import cAstro

import logging
import logging.config

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("test_cHeure")


def test_none():
    assert 1.0 == approx(1.0, abs=0.01)


def test_dip():
    x: float = cAstro.dip(1.0)
    assert x == approx(0.0293, abs=0.0001)


def test_dip2():
    x: float = cAstro.dip(4.0)
    assert x == approx(0.0586, abs=0.0001)


def test_hauteurObservationEnDeg():
    x: float = cAstro.hauteurObservationEnDeg(1.0, 1.0)
    assert x == approx(2.0, abs=0.0001)


def test_hauteurObservationEnDeg2():
    x: float = cAstro.hauteurObservationEnDeg(4.0, 1.0)
    assert x == approx(5.0, abs=0.0001)


def test_paralaxe():
    x: float = cAstro.paralaxe(0.002883, 32.233333)
    # assert x == approx(0.00243896267303736, abs=0.000000000001)


def test_paralaxe2():
    x: float = cAstro.paralaxe(4.0, 1.0)
    #  assert x == approx(5.0, abs=0.0001)


def test_refraction():
    x: float = cAstro.refraction(1.0)
    #  assert x == approx(2.0, abs=0.0001)


def test_refraction2():
    x: float = cAstro.refraction(4.0, temperatureEnDeg=5.0, pressionEnHPa=1000)
    #  assert x == approx(5.0, abs=0.0001)


def test_diametre():
    x: float = cAstro.diametre(4.0, 0.5)
    assert x == approx(2.0, abs=0.0001)


def test_diametre2():
    x: float = cAstro.diametre(4.0, -0.5)
    assert x == approx(-2.0, abs=0.0001)


def test_diametre3():
    x: float = cAstro.diametre(4.0, 0.0)
    assert x == approx(0.0, abs=0.0001)


def test_diametre4():
    x: float = cAstro.diametre(4.0, 1.0)
    #  assert x == approx(0.0, abs=0.0001)


def test_correction():
    x: float = cAstro.correction(4.0, 1.0, 1.0, 1.0)
    #  assert x == approx(0.0, abs=0.0001)


if __name__ == "__main__":
    # Execute when the module is not initialized from an import statement.
    test_none()
