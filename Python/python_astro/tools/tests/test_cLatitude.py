import sys

from cLatitude import cLatitude
from cAstroError import cAstroError

print(sys.executable) # show which Python we are running
print(sys.path)

from pytest import approx, raises

#from pprint import pprint

import logging
import logging.config

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("test_cLatitude")


# ------------------------------------
def test_parsing1():
    x: cLatitude = cLatitude()
    x = x.parse("N 10°59'56")
    y = x.asDeg()
    assert y == approx(10.99888, abs=0.00001)

    x: cLatitude = cLatitude()
    x = x.parse("N10°59'56")
    y = x.asDeg()
    assert y == approx(10.99888, abs=0.00001)

    x = x.parse("S 10°59'56")
    y = x.asDeg()
    assert y == approx(-10.99888, abs=0.00001)

    x: cLatitude = cLatitude()
    x = x.parse("10°59'56\" N")
    y = x.asDeg()
    assert y == approx(10.99888, abs=0.00001)

    x: cLatitude = cLatitude()
    x = x.parse("10°59'56\"N")
    y = x.asDeg()
    assert y == approx(10.99888, abs=0.00001)

    x = x.parse("10°59'56\" S")
    y = x.asDeg()
    assert y == approx(-10.99888, abs=0.00001)

def test_str():
    x: cLatitude = cLatitude()
    x = x.parse("N 10°59'56")
    assert x.toString(format=0) == "N 10°59'56.00\""
    assert x.toString() == "N 10.99889° [N 10°59.933' # N 10°59'56.00\"]"

    x = x.parse("N10°59'56")
    assert x.toString(format=0) == "N 10°59'56.00\""
    assert x.toString() == "N 10.99889° [N 10°59.933' # N 10°59'56.00\"]"

    x = x.parse("S 10°59'56")
    assert x.toString(format=0) == "S 10°59'56.00\""
    assert x.toString() == "S 10.99889° [S 10°59.933' # S 10°59'56.00\"]"

    x = x.parse("10°59'56\" N")
    assert x.toString(format=0) == "N 10°59'56.00\""
    assert x.toString() == "N 10.99889° [N 10°59.933' # N 10°59'56.00\"]"

    x = x.parse("10°59'56\" S")
    assert x.toString(format=0) == "S 10°59'56.00\""
    assert x.toString() == "S 10.99889° [S 10°59.933' # S 10°59'56.00\"]"

def test_parsingKO():
    x: cLatitude = cLatitude()
    with raises(cAstroError) as e_info:
        x = x.parse("N -10°59'56")

    with raises(cAstroError) as e_info:
        x = x.parse("S -10°59'56")
    
def test_parsingKO2():
    x: cLatitude = cLatitude()
    with raises(cAstroError) as e_info:
        x = x.parse("N 91°59'56")

    x: cLatitude = cLatitude()
    with raises(cAstroError) as e_info:
        x = x.parse("91°59'56 N")




if __name__ == "__main__":
    # Execute when the module is not initialized from an import statement.
    test_parsing1()
