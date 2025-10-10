import logging
import logging.config

logging.config.fileConfig("logging.conf")

class myEnv:
    UDP_IP = "127.0.0.1"
    UDP_PORT = 5005
    TCP_PORT = 10110
    sleepTimeInSec=2

    # Port de la trinitee
    latTrinitee = 47.565102  # angleSexaToDecimal(degre = 2, minute = 56.23)
    longTrinitee = -3.011115 # angleSexaToDecimal(degre = 2, minute = 56.23)

    # Port de St Quay
    latPortStQuay = 48.649665  # angleSexaToDecimal(degre = 2, minute = 56.23)
    longPortStQuay = -2.813217 # angleSexaToDecimal(degre = 2, minute = 56.23)

    # Port de 
    latSamoa = -14.2456  # angleSexaToDecimal(degre = 2, minute = 56.23)
    longSamoa = -169.6100 # angleSexaToDecimal(degre = 2, minute = 56.23)

    logger = logging.getLogger("NMEA")

    