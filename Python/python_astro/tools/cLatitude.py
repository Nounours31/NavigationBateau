import logging
import logging.config

from cAngle import cAngle

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("cLatitude")


class cLatitude(cAngle):
    def __init__(self):
        super.__init__()


if __name__ == "__main__":
    print("cLatitude")
