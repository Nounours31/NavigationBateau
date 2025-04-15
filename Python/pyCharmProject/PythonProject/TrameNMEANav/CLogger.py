from logging import Logger
import logging.config
from pathlib import Path



class CLogger:
    __LOG_FILE__ : str = './logging.ini'
    def __init__(self):
        pass

    @staticmethod
    def getLogger(name: str, logback : str, logdir : str) -> Logger:
        logger: Logger = None
        if not logback is None :
            my_file = Path(logback)
            if not my_file.is_file():
                my_file = Path(CLogger.__LOG_FILE__)

            if my_file.is_file():
                # Initialize the logger once as the application starts up.
                logging.config.fileConfig(CLogger.__LOG_FILE__, defaults={"LOG_DIR": logdir})
                logger: Logger = logging.getLogger(name)

        if logger is None:
            logger = logging.getLogger(name)
            logger.setLevel(logging.DEBUG)

            # create console handler and set level to debug
            ch = logging.StreamHandler()
            ch.setLevel(logging.DEBUG)

            # create formatter
            formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')

            # add formatter to ch
            ch.setFormatter(formatter)

            # add ch to logger
            logger.addHandler(ch)

        logger.info("Configured the logger! {}".format(logger))
        return logger

