import logging

from src.sfa_tools import myLogger as logger

logger.setup_logging()
filehandler = logger.createFileHandler("/tmp/logs")
consoleHandler = logger.createConsoleHandler()
handlers = [
    filehandler,
    consoleHandler
]
_logger : logging.Logger = logger.getLogger("toto", logging.DEBUG, handlers)

_logger.error("x")
_logger.error("y")

