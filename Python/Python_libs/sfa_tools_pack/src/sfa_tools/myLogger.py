import logging
import logging.config
import sys
from logging import Logger, handlers, StreamHandler
from logging.handlers import RotatingFileHandler
from pathlib import Path
from typing import Dict, List


# le logger du logger ...
_internalLogger : Logger = logging.getLogger("init")
_internalLogger.setLevel(logging.INFO)
console_handler = logging.StreamHandler()
console_handler.setLevel(logging.INFO)  # Initialement WARNING+
_internalLogger.addHandler(console_handler)


LOG_DIR = Path("logs")
LOG_DIR.mkdir(exist_ok=True)

LOGGING_CONFIG = {
    "version": 1,
    "disable_existing_loggers": False,

    "formatters": {
        "standard": {
            "format": "%(asctime)s | %(levelname)-8s | %(name)s | %(message)s",
        },
        "detailed": {
            "format": "%(asctime)s | %(levelname)-8s | %(name)s | %(filename)s:%(lineno)d | %(message)s",
        },
    },

    "handlers": {
        "console": {
            "class": "logging.StreamHandler",
            "level": "INFO",
            "formatter": "standard",
            "stream": "ext://sys.stdout",
        },
        "file": {
            "class": "logging.handlers.RotatingFileHandler",
            "level": "DEBUG",
            "formatter": "detailed",
            "filename": str(LOG_DIR / "app.log"),
            "maxBytes": 1_000_000,
            "backupCount": 5,
            "encoding": "utf-8",
        },
    },

    "root": {
        "level": "DEBUG",
        "handlers": ["console", "file"],
    },

    "loggers": {
        "myapp": {
            "handlers": ["console", "file"],
            "level": "DEBUG",
            "propagate": False,
        },
        "myapp.database": {
            "handlers": ["file"],
            "level": "WARNING",
            "propagate": False,
        },
    },
}


def setup_logging() -> None:
    """
    Ne rien faire
    """
    # logging.config.dictConfig(LOGGING_CONFIG)
    pass



def createFormater() -> logging.Formatter:
    """
    Creation du formateur par defaut
    :return: le formaterur: %(asctime)s | %(levelname)-8s | %(name)s | %(filename)s:%(lineno)d | %(message)s
    """
    return  logging.Formatter("%(asctime)s | %(levelname)-8s | %(name)s | %(filename)s:%(lineno)d | %(message)s")


def createConsoleHandler() -> StreamHandler:
    """
    creation d'un Console Handler sur stderr
    :return: le console handler sur stderr
    """
    # Create a console handler
    console_handler = logging.StreamHandler()
    console_handler.setLevel(logging.DEBUG)  # This handler only handles DEBUG and above
    console_handler.setStream(stream=sys.stderr)

    # Create a formatter and attach it to the handler
    formatter = createFormater()
    console_handler.setFormatter(formatter)
    return console_handler


def createFileHandler(filepath: str) -> RotatingFileHandler:
    """
    creation d'un File Handler sur filepath
    :rtype: object
    """
    # Création du handler de type RotatingFileHandler
    # - 'app.log' : nom du fichier
    # - maxBytes=2000 : taille max du fichier en octets avant rotation
    # - backupCount=5 : nombre de fichiers de backup conservés
    rotating_handler = logging.handlers.RotatingFileHandler(filepath, maxBytes=2000000, backupCount=5)
    rotating_handler.setLevel(logging.DEBUG)  # This handler only handles DEBUG and above

    # Create a formatter and attach it to the handler
    formatter = createFormater()
    rotating_handler.setFormatter(formatter)
    return rotating_handler


def getLogger(name : str, level : int = logging.INFO, myHandlers : List[logging.handlers] = None) -> Logger:
    """
    creation d'un logger
    :param name: Nom du loger
    :param level: le niveai par defaut info
    :param myHandlers: ARRAY les handlers
    :return: le logger
    """
    x : Logger = logging.getLogger(name)
    x.setLevel(level)
    if myHandlers is not None and len(myHandlers) > 0:
        for h in myHandlers:
            x.addHandler(h)
    else:
        x.addHandler(createConsoleHandler())

    y: Logger = x
    allHandler = []
    if x.level == logging.NOTSET:
        y : Logger = x.parent

    hs = y.handlers
    for h in hs:
        info: Dict[str, str] = {}
        info ["name"] = h.name
        info ["type"] = type(h).__name__
        if isinstance(h, handlers.BaseRotatingHandler):
            z : handlers.BaseRotatingHandler = h
            info ["file"] = z.baseFilename

        if isinstance(h, StreamHandler):
            z : StreamHandler = h
            info ["file"] = z.stream.name
        allHandler.append(info)

    _internalLogger.info(f"Creation logger {name} - level {logging.getLevelName(x.getEffectiveLevel())} - handler {allHandler}")
    return x
