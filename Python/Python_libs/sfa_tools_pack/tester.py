import logging

from src.sfa_tools import myLogger as logger

logger.setup_logging()
_logger : logging.Logger = logger.getLogger("toto")

_logger.error("x")
_logger.error("y")

