import logging
import logging.config

from tools.position import position

logging.config.fileConfig("logging.conf")





class myEnv:
    UDP_IP = "127.0.0.1"
    UDP_PORT = 5005
    TCP_PORT = 5006
    UDP=1
    TCP=2
    ModeReseau=TCP
    sleepTimeInSec=5 # garder 2s

    # Port de la trinitee
    latTrinitee = 47.565102  # angleSexaToDecimal(degre = 2, minute = 56.23)
    longTrinitee = -3.011115 # angleSexaToDecimal(degre = 2, minute = 56.23)
    postionTrinitee : position = position.fromString(f"{latTrinitee:.6f},{longTrinitee:.6f}")

    # Port de St Quay
    latPortStQuay = 48.649665  # angleSexaToDecimal(degre = 2, minute = 56.23)
    longPortStQuay = -2.813217 # angleSexaToDecimal(degre = 2, minute = 56.23)
    postionStQuay : position = position.fromString(f"{latPortStQuay:.6f},{longPortStQuay:.6f}")

    # Port de 
    latSamoa = -14.2456  # angleSexaToDecimal(degre = 2, minute = 56.23)
    longSamoa = -169.6100 # angleSexaToDecimal(degre = 2, minute = 56.23)

    logger = logging.getLogger("NMEA")


