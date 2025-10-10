import logging
import logging.config

from math import floor, pi, cos, sin, tan
from time import sleep
from datetime import datetime, timezone
import calendar


# Initialize the logger once as the application starts up.
logging.config.fileConfig('logging.conf')
 
# Get an instance of the logger and use it to write a log!
# Note: Do this AFTER the config is loaded above or it won't use the config.
logger = logging.getLogger(__name__)
logger.info("Configured the logger!")



class tools:
    # ----------------------------------------------------------------------------------
    # doit retourner l'heure au format
    #   ddmmyy
    # ----------------------------------------------------------------------------------
    @staticmethod
    def date2GPSDecimale(now : datetime) -> float :
        return now.day * 10000 + now.month * 100 + (now.year - 100 * floor (now.year / 100))

    # ----------------------------------------------------------------------------------
    # doit retourner l'heure GPS au format
    #   hhmmss.ssss
    # ----------------------------------------------------------------------------------
    @staticmethod
    def heure2GPSDecimale(now : datetime) -> float :
        return ((now.hour * 100 + now.minute) * 100) + now.second + (now.microsecond / 1000000)

    # ----------------------------------------------------------------------------------
    # doit retourner un angle en degre decimal
    # ----------------------------------------------------------------------------------
    @staticmethod
    def angleSexaToDecimal(degre : int, minute: float) -> float :
        x = 1 if degre >= 0 else -1
        return x * (abs(degre) + abs(minute) / 60)

    # ----------------------------------------------------------------------------------
    # doit retourner un angle en degre decimal / minute sagedecimal
    # ----------------------------------------------------------------------------------
    @staticmethod
    def angleDecimalToMinuteSexa(degreDecimal : float) -> float :
        x = 1 if degreDecimal >= 0 else -1
        y = abs(degreDecimal)
        degre = floor(y)
        minute = (y - degre) * 60 / 100 
        return x * (degre + minute)



    # ----------------------------------------------------------------------------------
    # debug
    # ----------------------------------------------------------------------------------
    @staticmethod
    def fromLatDecimalToStr(positionLatitudeDecimale: float) -> str:
        signe = "N" if positionLatitudeDecimale > 0 else "S"
        angle = abs(positionLatitudeDecimale)
        degre = floor(angle)
        minute = (angle - degre) * 60
        return "{degre:02d}°{minute:07.4f}' {signe:s}".format(degre = degre, minute =  minute, signe = signe)

    @staticmethod
    def fromLongDecimalToStr(positionLongitudeDecimale: float) -> str:
        signe = "E" if positionLongitudeDecimale > 0 else "W"
        angle = abs(positionLongitudeDecimale)
        degre = floor(angle)
        minute = (angle - degre) * 60
        return "{degre:02d}°{minute:07.4f}' {signe:s}".format(degre = degre, minute =  minute, signe = signe)

