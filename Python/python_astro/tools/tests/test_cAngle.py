from cAngle import cAngle
from pytest import approx

from pprint import pprint

import logging
import logging.config

logging.config.fileConfig('logging.conf')
logger = logging.getLogger("test_cAngle")

def test_parsing():
    x : cAngle = cAngle()
    y : float = x.parse("10.25°")
    logger.info("y: %f", y)
    logger.info("x: %s", x.toString())
    assert y == approx(-1.0, abs = 0.01)

    
if __name__ == '__main__':
    # Execute when the module is not initialized from an import statement.
    test_parsing()