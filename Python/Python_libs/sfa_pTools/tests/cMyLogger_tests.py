import logging
from venv import logger


from pTools import mMyException
from pTools.mLogger import getLogger



def test_cLogger():
    """
    Test de la classe mMyException
    """
    logger : logging.Logger = getLogger("test_cLogger", logging.DEBUG)
    logger.error("x")
    logger.error("y")

    assert logger is not None


