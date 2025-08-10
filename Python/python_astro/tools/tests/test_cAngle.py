import sys
print(sys.executable) # show which Python we are running
print(sys.path)

from cAngle import cAngle
from pytest import approx

#from pprint import pprint

import logging
import logging.config

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("test_cAngle")


# ------------------------------------
def test_parsing1():
    x: cAngle = cAngle()
    y: float = x.parse("10.25°")
    y = y / 3600.0
    assert y == approx(10.25, abs=0.01)

def test_parsing1_neg():
    x: cAngle = cAngle()
    y: float = x.parse("-10.25°")
    y = y / 3600.0
    assert y == approx(-10.25, abs=0.01)

# ------------------------------------
def test_parsing2():
    x: cAngle = cAngle()
    y: float = x.parse("10°25.59'")
    y = y / 3600.0
    assert y == approx(10.4265, abs=0.01)

def test_parsing2_neg():
    x: cAngle = cAngle()
    y: float = x.parse("-10°25.59'")
    y = y / 3600.0
    assert y == approx(-10.4265, abs=0.01)


# ------------------------------------
def test_parsing3():
    x: cAngle = cAngle()
    y: float = x.parse("10°25'26.56\"")
    y = y / 3600.0
    assert y == approx(10.42404, abs=0.01)

def test_parsing3_neg():
    x: cAngle = cAngle()
    y: float = x.parse("-10°25'26.56\"")
    y = y / 3600.0
    assert y == approx(-10.42404, abs=0.01)


# ------------------------------------
def test_parsing4():
    x: cAngle = cAngle()
    y: float = x.parse("26.56'")
    y = y / 3600.0
    assert y == approx(0.4426, abs=0.01)

def test_parsing4_neg():
    x: cAngle = cAngle()
    y: float = x.parse("-26.56'")
    y = y / 3600.0
    assert y == approx(-0.4426, abs=0.01)

# ------------------------------------
def test_parsing5():
    x: cAngle = cAngle()
    y: float = x.parse("25'26.56\"")
    y = y / 3600.0
    assert y == approx(0.42404, abs=0.01)

def test_parsing5_neg():
    x: cAngle = cAngle()
    y: float = x.parse("-25'26.56\"")
    y = y / 3600.0
    assert y == approx(-0.42404, abs=0.01)

# ------------------------------------
def test_parsing6():
    x: cAngle = cAngle()
    y: float = x.parse("26.56\"")
    y = y / 3600.0
    assert y == approx(0.00737, abs=0.01)

def test_parsing6_neg():
    x: cAngle = cAngle()
    y: float = x.parse("-26.56\"")
    y = y / 3600.0
    assert y == approx(-0.00737, abs=0.01)

# ------------------------------------
def test_parsing7():
    x: cAngle = cAngle()
    y = x.parse ("56°23")
    y = y / 3600.0
    assert y == approx(56.383, abs=0.01)

def test_parsing7_neg():
    x: cAngle = cAngle()
    y = x.parse ("-56°23")
    y = y / 3600.0
    assert y == approx(-56.383, abs=0.01)



if __name__ == "__main__":
    # Execute when the module is not initialized from an import statement.
    test_parsing1()
    test_parsing1_neg()
    test_parsing2()
    test_parsing2_neg()
    test_parsing3()
    test_parsing3_neg()
    test_parsing4()
    test_parsing4_neg()
    test_parsing5()
    test_parsing5_neg()
    test_parsing6()
    test_parsing6_neg()
    test_parsing7()
    test_parsing7_neg()
