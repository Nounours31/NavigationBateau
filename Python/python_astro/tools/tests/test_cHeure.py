from cHeure import cHeure
from pytest import approx

from pprint import pprint

import logging
import logging.config

logging.config.fileConfig('logging.conf')
logger = logging.getLogger("test_cHeure")

def test_parsing_none():
    x : cHeure = cHeure()
    y : float = x.parse(None)
    logger.info("y: %f", y)
    assert y == approx(-1.0, abs = 0.01)

def test_parsing_vide():
    x : cHeure = cHeure()
    y : float = x.parse("")
    logger.info("y: %f", y)
    assert y == approx(-1.0, abs = 0.01)

def test_parsing_invalide1():
    x : cHeure = cHeure()
    y : float = x.parse("12:")
    logger.info("y: %f", y)
    assert y == approx(-1.0, abs = 0.01)

def test_parsing_invalide2():
    x : cHeure = cHeure()
    y : float = x.parse("12:4:12")
    logger.info("y: %f", y)
    assert y == approx(-1.0, abs = 0.01)

def test_parsing():
    x : cHeure = cHeure()
    y : float = x.parse("10:11:12.13")
    logger.info("y: %f", y)
    assert y == approx(36672.13000, abs = 0.01)
    assert y == x.get_val()

def test_initKO():
    x : cHeure = cHeure(sVal = "dsdf sdfs df sd")
    logger.info (x)
    pprint(vars(x))
    assert x.get_val() == approx(-1.0, abs = 0.01)

def test_usage():
    x : str = cHeure.usage("toto")
    assert len(x) > 0

def test_str():
    x : cHeure = cHeure()
    y : float = x.parse(sVal="10:11:12.13")
    assert str(x) == "36672.13000"

def test_toString():
    x : cHeure = cHeure(sVal="10:11:12.130")
    y : float = x.toString()
    assert x.toString() == "10:11:12.130"
    
if __name__ == '__main__':
    # Execute when the module is not initialized from an import statement.
    test_initKO()