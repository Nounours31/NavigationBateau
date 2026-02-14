import logging
import logging.config
from logging import Logger
from pathlib import Path

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


def setup_logging():
    logging.config.dictConfig(LOGGING_CONFIG)

_internalLogger : Logger = logging.getLogger("")

def getLogger(name : str) -> Logger:
    x : Logger = logging.getLogger(name)

    handlers = []
    if x.level == logging.NOTSET:
        handlers = [type(h).__name__ for h in x.parent.handlers]
    else:
        handlers = [type(h).__name__ for h in x.parent.handlers]

    _internalLogger.info(f"Creation logger {name} - level {logging.getLevelName(x.getEffectiveLevel())} - handler {handlers}")
    return x
