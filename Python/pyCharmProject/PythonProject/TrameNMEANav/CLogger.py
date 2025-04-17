import asyncio
import logging.config
import platform
from logging import Logger
from pathlib import Path

from TrameNMEANav.CEnv import CEnv


class CLogger:
    logger: Logger = None

    def __init__(self):
        pass

    @staticmethod
    def init(name: str, logback: str, logdir: str) -> Logger:
        logger = asyncio.run(CLogger.__getLogger__(name, logback, logdir))
        return logger

    @staticmethod
    def getLogger(info : str) -> Logger:
        # print(f"getLogger ({info}) {CLogger.logger}")
        return CLogger.logger

    @staticmethod
    async def __getLogger__(name: str, logback: str, logdir: str) -> Logger:
        lock = asyncio.Lock()
        await lock.acquire()
        try:
            logger: Logger = None
            if name is None:
                name = CEnv.getProjectName()

            logbackfile = logback
            if not logback is None:
                my_file = Path(logback)
                if not my_file.is_file():
                    logbackfile = CEnv.getDefaultLogbackFile()
                    my_file = Path(CEnv.getDefaultLogbackFile())

                if my_file.is_file():
                    osinfo: str = platform.system()
                    if osinfo == "Windows":
                        logdir = logdir.replace('\\', "/")

                    # Initialize the logger once as the application starts up.
                    logging.config.fileConfig(logbackfile, defaults={"LOG_DIR": logdir})
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
            CLogger.logger = logger
        finally:
            lock.release()

        return logger
