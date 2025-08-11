import sys

from cLongitude import cLongitude
from cAstroError import cAstroError

print(sys.executable) # show which Python we are running
print(sys.path)

from pytest import approx, raises

#from pprint import pprint

import logging
import logging.config

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("test_cLongitude")


# ------------------------------------
def test_parsing1():
    x: cLongitude = cLongitude()
    x = x.parse("E 10°59'56")
    y = x.asDeg()
    assert y == approx(10.99888, abs=0.00001)

    x: cLongitude = cLongitude()
    x = x.parse("E10°59'56")
    y = x.asDeg()
    assert y == approx(10.99888, abs=0.00001)

    x = x.parse("W 10°59'56")
    y = x.asDeg()
    assert y == approx(-10.99888, abs=0.00001)

    x = x.parse("O 10°59'56")
    y = x.asDeg()
    assert y == approx(-10.99888, abs=0.00001)

    x: cLongitude = cLongitude()
    x = x.parse("10°59'56\" E")
    y = x.asDeg()
    assert y == approx(10.99888, abs=0.00001)

    x: cLongitude = cLongitude()
    x = x.parse("10°59'56\"E")
    y = x.asDeg()
    assert y == approx(10.99888, abs=0.00001)

    x = x.parse("10°59'56\" W")
    y = x.asDeg()
    assert y == approx(-10.99888, abs=0.00001)

    x = x.parse("10°59'56\" O")
    y = x.asDeg()
    assert y == approx(-10.99888, abs=0.00001)

def test_str():
    x: cLongitude = cLongitude()
    x = x.parse("E 10°59'56")
    assert x.toString(format=0) == "E 10°59'56.00\""
    assert x.toString() == "E 10.99889° [E 10°59.933' # E 10°59'56.00\"]"

    x = x.parse("E10°59'56")
    assert x.toString(format=0) == "E 10°59'56.00\""
    assert x.toString() == "E 10.99889° [E 10°59.933' # E 10°59'56.00\"]"

    x = x.parse("W 10°59'56")
    assert x.toString(format=0) == "W 10°59'56.00\""
    assert x.toString() == "W 10.99889° [W 10°59.933' # W 10°59'56.00\"]"

    x = x.parse("O 10°59'56")
    assert x.toString(format=0) == "W 10°59'56.00\""
    assert x.toString() == "W 10.99889° [W 10°59.933' # W 10°59'56.00\"]"

    x = x.parse("10°59'56\" E")
    assert x.toString(format=0) == "E 10°59'56.00\""
    assert x.toString() == "E 10.99889° [E 10°59.933' # E 10°59'56.00\"]"

    x = x.parse("10°59'56\" W")
    assert x.toString(format=0) == "W 10°59'56.00\""
    assert x.toString() == "W 10.99889° [W 10°59.933' # W 10°59'56.00\"]"

def test_parsingKO():
    x: cLongitude = cLongitude()
    with raises(cAstroError) as e_info:
        x = x.parse("E -10°59'56")

    with raises(cAstroError) as e_info:
        x = x.parse("W -10°59'56")
    
def test_parsingKO2():
    x: cLongitude = cLongitude()
    with raises(cAstroError) as e_info:
        x = x.parse("E 191°59'56")

    x: cLongitude = cLongitude()
    with raises(cAstroError) as e_info:
        x = x.parse("191°59'56 E")




if __name__ == "__main__":
    # Execute when the module is not initialized from an import statement.
    test_parsing1()
