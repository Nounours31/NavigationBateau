from cAstroError import cAstroError
from cHeure import cHeure
from pytest import approx, raises

from pprint import pprint

import logging
import logging.config

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("test_cHeure")


def test_parsing_none():
    x: cHeure = cHeure()
    with raises(cAstroError) as e_info:
        x.parse(None)


def test_parsing_vide():
    x: cHeure = cHeure()
    with raises(cAstroError) as e_info:
        x.parse("")


def test_parsing_chronos1():
    with raises(cAstroError) as e_info: 
        y: float = cHeure.parseChrono2Seconde("120:0.1")

def test_parsing_chronos2():
    y: float = cHeure.parseChrono2Seconde("120:00.1")
    logger.info("y: %f", y)
    assert y == approx(7200.1, abs=0.01)

def test_parsing_invalide1():
    x: cHeure = cHeure()
    with raises(cAstroError) as e_info:
        x.parse("12:")
    

def test_parsing_invalide2():
    x: cHeure = cHeure()
    with raises(cAstroError) as e_info:
        x.parse("12:4:12")


def test_parsing():
    x: cHeure = cHeure()
    x.parse("10:11:12.13")
    logger.info("y: %f", x.val())
    assert x.val() == approx(36672.13000, abs=0.01)


def test_initKO():
    with raises(cAstroError) as e_info:
        x: cHeure = cHeure(sVal="dsdf sdfs df sd")


def test_str():
    x: cHeure = cHeure()
    x.parse("10:11:12.13")
    assert str(x) == "36672.130"


def test_toString():
    x: cHeure = cHeure(sVal="10:11:12.130")
    assert x.toString() == "10:11:12.130"


if __name__ == "__main__":
    # Execute when the module is not initialized from an import statement.
    test_initKO()
