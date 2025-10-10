import socket 
import signal
import sys
import logging
import logging.config

from math import floor, pi, cos, sin, tan
from time import sleep
from datetime import datetime, timezone
import calendar
from typing import List

from myEnv import myEnv

# Initialize the logger once as the application starts up.
logging.config.fileConfig('logging.conf')
 
# Get an instance of the logger and use it to write a log!
# Note: Do this AFTER the config is loaded above or it won't use the config.
logger = logging.getLogger(__name__)
logger.info("Configured the logger!")

'''
# ----------------------------------------------------------------------------------
# This is a simple calculator to compute the checksum field for the NMEA protocol. 
# The checksum is simple, just an XOR of all the bytes between the $ and the * (not including the delimiters themselves), and written in hexadecimal.
# ----------------------------------------------------------------------------------
def NMEAChecksum(s : str) -> str :
    xor = ord(s[0]) ^ ord(s[1]) 
    for i in range(2, len(s)):
        xor = xor ^ ord(s[i])

    retour = hex(xor)[2:].upper()
    if len(retour) < 2:
        retour = "0" + retour
    return retour 

# ----------------------------------------------------------------------------------
# concatene devant le message le "$" et ajoute en fin le "*" + checksum nmea
# ----------------------------------------------------------------------------------
def addNMEACheckSum(message : str) -> bytes:
    nmeaMessage = "".join(["$", message, "*", NMEAChecksum(message)])
    bytes_representation = nmeaMessage.encode(encoding="utf-8")
    return bytes_representation

# ----------------------------------------------------------------------------------
# doit retourner l'heure GPS au format
#   hhmmss.ssss
# ----------------------------------------------------------------------------------
def heure2GPSDecimale(now : datetime) -> float :
    return ((now.hour * 100 + now.minute) * 100) + now.second + (now.microsecond / 1000000)

# ----------------------------------------------------------------------------------
# doit retourner un angle en degre decimal
# ----------------------------------------------------------------------------------
def angleSexaToDecimal(degre : int, minute: float) -> float :
    x = 1 if degre >= 0 else -1
    return x * (abs(degre) + abs(minute) / 60)

# ----------------------------------------------------------------------------------
# doit retourner un angle en degre decimal / minute sagedecimal
# ----------------------------------------------------------------------------------
def angleDecimalToMinuteSexa(degreDecimal : float) -> float :
    x = 1 if degreDecimal >= 0 else -1
    y = abs(degreDecimal)
    degre = floor(y)
    minute = (y - degre) * 60 / 100 
    return x * (degre + minute)


# ----------------------------------------------------------------------------------
# Navigation a :
#   - cap       constant 
#   - vitesse   constante 
#   - lat moyenne
#   - a une position de depart (positionDepartLatitude / positionDepartLongitude) 
# ----------------------------------------------------------------------------------
def nav(iSecondeDepuisDepart : int, vitesseEnNoeud : float, cap : float, latitudeEstimee : float, positionDepartLatitude: float, positionDepartLongitude: float) -> tuple [float, float] :
    deg2rad = pi / 180.0
    
    capRad = cap * deg2rad
    latitudeEstimeeRad = latitudeEstimee * deg2rad

    pasEnLatitude = cos (capRad) * vitesseEnNoeud / 60 # noeud = mille/h - 1 mille = 1 minute d'arc
    pasEnLongitude = sin (capRad) * vitesseEnNoeud / (60 * cos (latitudeEstimeeRad))
    
    positionLatitude= positionDepartLatitude + pasEnLatitude * iSecondeDepuisDepart / 3600
    positionLongitude= positionDepartLongitude + pasEnLongitude * iSecondeDepuisDepart / 3600

    logger.info(">>> iSecondeDepuisDepart: {:03d}, vitesseEnNoeud: {:09.2f}, cap: {:09.2f}, latitudeEstimee: {:011.4f}, positionDepartLatitude: {:011.4f}, positionDepartLongitude: {:011.4f}".format(iSecondeDepuisDepart, vitesseEnNoeud, cap, latitudeEstimee, positionDepartLatitude, positionDepartLongitude))
    logger.info("<<< positionLatitude: {:011.4f}, positionLongitude: {:011.4f}".format(positionLatitude, positionLongitude))

    return positionLatitude, positionLongitude


# ----------------------------------------------------------------------------------
def getGGA(now : datetime, latitudeDecimale : float, longitudeDecimale : float):
    nmeaMessage = "GPGGA,{heure:09.2f},{lat:011.6f},{latSens},{long:012.6f},{longSens},1,10,1.2,27.0,M,-34.2,M,,".format(
        heure =  heure2GPSDecimale(now),
        lat = abs(angleDecimalToMinuteSexa(latitudeDecimale) * 100),
        latSens = "N" if latitudeDecimale > 0 else "S",
        long = abs(angleDecimalToMinuteSexa(longitudeDecimale) * 100),
        longSens = "E" if longitudeDecimale > 0 else "W")
    return addNMEACheckSum(nmeaMessage)


# ----------------------------------------------------------------------------------
def getGSA():
    nmeaMessage = "GPGSA,A,3,07,02,26,27,09,04,15,,,,,,1.8,1.2,1"
    return addNMEACheckSum(nmeaMessage)

# ----------------------------------------------------------------------------------
# cap t vitesse
def getVTG(vitesseEnNoeud, cap):
    capMagnetique = cap + 2.1
    vitesseEnkm = vitesseEnNoeud * 1.852;
    nmeaMessage = "GPVTG,{cap:05.1f},T,{capMagnetique:05.1f},M,{vitesseEnNoeud:05.1f},N,{vitesseEnkm:05.1f},K,A".format(
        cap = cap,
        capMagnetique = capMagnetique,
        vitesseEnNoeud = vitesseEnNoeud,
        vitesseEnkm = vitesseEnkm)
    return addNMEACheckSum(nmeaMessage)

# ----------------------------------------------------------------------------------
def getRMC(now : datetime, latitudeDecimale, longitudeDecimale, vitesseEnNoeud, cap):
    dateutc = now.day * 10000 + now.month * 100 + (now.year - 100 * floor (now.year / 100))
    nmeaMessage = "GPRMC,{heure:09.2f},A,{lat:011.6f},{latSens},{long:012.6f},{longSens},{vitesseEnNoeud:06.2f},{cap:06.2f},{dateutc:06d},002.1,W,A,V".format(
        heure =  heure2GPSDecimale(now),
        lat = abs(angleDecimalToMinuteSexa(latitudeDecimale) * 100),
        latSens = "N" if latitudeDecimale > 0 else "S",
        long = abs(angleDecimalToMinuteSexa(longitudeDecimale) * 100),
        longSens = "E" if longitudeDecimale > 0 else "W",
        vitesseEnNoeud = vitesseEnNoeud,
        cap = cap,
        dateutc = dateutc)        
    return addNMEACheckSum(nmeaMessage)

# ----------------------------------------------------------------------------------
def getGSV() -> list[bytes] :
    nmeaMessage = []
    nmeaMessage.append ("$GPGSV,3,1,09,20,49,293,27,05,10,116,,07,19,047,,10,27,057,,1*77".encode(encoding="utf-8"))
    nmeaMessage.append ("$GPGSV,3,2,09,11,06,075,,12,06,025,,19,08,322,,23,63,061,,1*78".encode(encoding="utf-8"))
    nmeaMessage.append ("$GPGSV,3,3,09,25,11,060,,1*4E".encode(encoding="utf-8"))
    return  nmeaMessage

def getDepth(i: int) -> bytes:
    depth = abs(5 + cos(i) * 100)
    distanceSondeQuille = 1.2
    nmeaMessage = "IIDPT,{depth:02.1f},{ecart:02.1f},".format(
        depth =  depth,
        ecart = -abs(distanceSondeQuille))        
    return addNMEACheckSum(nmeaMessage)

def getHDG(i: int, cap:float, variation:float) -> bytes:
    capM = (cap + variation)
    if capM < 0:
        capM += 360
    if capM > 360:
        capM -= 360

    nmeaMessage = "IIHDG,{capM:04.2f},{variationM:04.2f},{variationS:s},,".format(
        capM =  capM,
        variationM =  abs(variation),
        variationS = "E" if variation>0 else "W")        
    return addNMEACheckSum(nmeaMessage)

def getWindInfo(i: int, type: str, angle:float, speedInKnot:float) -> bytes:
    reference = "R" if "Relative" == type else "T"
    nmeaMessage = "IIMWV,{angle:04.2f},{reference:s},{speed:04.2f},K,A".format(
        angle =  angle,
        reference =  reference,
        speed =  speedInKnot)        
    return addNMEACheckSum(nmeaMessage)

def getWayPointInfoBWC(now: datetime, latDecimale: float, longDecimale: float, bearing:float,  variation:float, distance:float, id:str) -> bytes:
    bearingM = bearing + variation
    nmeaMessage = "IIBWC,{heure:09.2f},{lat:011.6f},{latSens},{long:012.6f},{longSens},{bearing:05.2f},T,{bearingM:05.2f},M,{distance:05.2f},N,{id:s}".format(
        heure =  heure2GPSDecimale(now),
        lat = abs(angleDecimalToMinuteSexa(latDecimale) * 100),
        latSens = "N" if latDecimale > 0 else "S",
        long = abs(angleDecimalToMinuteSexa(longDecimale) * 100),
        longSens = "E" if longDecimale > 0 else "W",
        bearing = bearing,
        bearingM = bearingM,
        distance = distance,
        id = id)      
    return addNMEACheckSum(nmeaMessage)

# BWR Bearing and Distance to Waypoint – Rhumb Line Latitude, N/S, Longitude, E/W,
def getWayPointInfoBWR(now: datetime, latDecimale: float, longDecimale: float, bearing:float,  variation:float, distance:float, id:str) -> bytes:
    bearingM = bearing + variation
    nmeaMessage = "IIBWR,{heure:09.2f},{lat:011.6f},{latSens},{long:012.6f},{longSens},{bearing:05.2f},T,{bearingM:05.2f},M,{distance:05.2f},N,{id:s}".format(
        heure =  heure2GPSDecimale(now),
        lat = abs(angleDecimalToMinuteSexa(latDecimale) * 100),
        latSens = "N" if latDecimale > 0 else "S",
        long = abs(angleDecimalToMinuteSexa(longDecimale) * 100),
        longSens = "E" if longDecimale > 0 else "W",
        bearing = bearing,
        bearingM = bearingM,
        distance = distance,
        id = id)      
    return addNMEACheckSum(nmeaMessage)

# ----------------------------------------------------------------------------------
#
# ----------------------------------------------------------------------------------
def fromLatDecimalToStr(positionLatitudeDecimale: float) -> str:
    signe = "N" if positionLatitudeDecimale > 0 else "S"
    angle = abs(positionLatitudeDecimale)
    degre = floor(angle)
    minute = (angle - degre) * 60
    return "{degre:02d}°{minute:07.4f}' {signe:s}".format(degre = degre, minute =  minute, signe = signe)

def fromLongDecimalToStr(positionLongitudeDecimale: float) -> str:
    signe = "E" if positionLongitudeDecimale > 0 else "W"
    angle = abs(positionLongitudeDecimale)
    degre = floor(angle)
    minute = (angle - degre) * 60
    return "{degre:02d}°{minute:07.4f}' {signe:s}".format(degre = degre, minute =  minute, signe = signe)

def exit_gracefully(signum, frame):
    # restore the original signal handler as otherwise evil things will happen
    # in raw_input when CTRL+C is pressed, and our signal handler is not re-entrant
    signal.signal(signal.SIGINT, original_sigint)

    try:
        if input("\nReally quit? (y/n)> ").lower().startswith('y'):
            sys.exit(1)

    except KeyboardInterrupt:
        print("Ok ok, quitting")
        sock.close
        sys.exit(1)

    # restore the exit gracefully handler here    
    signal.signal(signal.SIGINT, exit_gracefully)

# ----------------------------------------------------------------------------------
# main function
# ----------------------------------------------------------------------------------
def main():
    myEnv.UDP_IP = "127.0.0.1"
    myEnv.UDP_PORT = 5005

    global sock
    sock = socket.socket(socket.AF_INET, # Internet
                            socket.SOCK_DGRAM) # UDP

    logger.debug("UDP target IP: %s" % myEnv.UDP_IP)
    logger.debug("UDP target port: %s" % myEnv.UDP_PORT)

    sleepTimeInSec=2
    nbSecondes=0
    now = datetime.now(tz = timezone.utc)
    vitesseEnNoeudMoyenne = 5.0
    capMoyen = 45.0
    variationMagnetique = -1.2
    # Port de St Quay
    latPortStQuay = 48.649665  # angleSexaToDecimal(degre = 2, minute = 56.23)
    longPortStQuay = -2.813217 # angleSexaToDecimal(degre = 2, minute = 56.23)

    # Port de 
    latSamoa = -14.2456  # angleSexaToDecimal(degre = 2, minute = 56.23)
    longSamoa = -169.6100 # angleSexaToDecimal(degre = 2, minute = 56.23)

    positionDepartLatitudeDecimale = latSamoa
    positionDepartLongitudeDecimale = longSamoa

    positionLatitudeDecimale, positionLongitudeDecimale = positionDepartLatitudeDecimale, positionDepartLongitudeDecimale

    while True:
        prev = now
        now = datetime.now(tz = timezone.utc)
        nbSecondes = calendar.timegm(now.timetuple()) - calendar.timegm(prev.timetuple()) 

        cap = capMoyen + (5) * cos(10 * nbSecondes) # +/- 5 degre
        vitesseEnNoeud = vitesseEnNoeudMoyenne + (vitesseEnNoeudMoyenne / 10) * cos(100 * nbSecondes) # +/- 10%

        prevpositionLatitudeDecimale, prevpositionLongitudeDecimale = positionLatitudeDecimale, positionLongitudeDecimale
        positionLatitudeDecimale, positionLongitudeDecimale = nav (nbSecondes, vitesseEnNoeud, cap, prevpositionLatitudeDecimale, prevpositionLatitudeDecimale, prevpositionLongitudeDecimale)

        gga = getGGA(now, positionLatitudeDecimale, positionLongitudeDecimale)  
        rmc = getRMC(now, positionLatitudeDecimale, positionLongitudeDecimale,vitesseEnNoeud, cap)  
        vtg = getVTG(vitesseEnNoeud, cap)  
        gsa = getGSA()  
        gsv = getGSV()  
        logger.debug (gga)
        logger.debug (gsv)
        logger.debug (gsa)
        logger.debug (vtg)
        logger.debug (rmc)
        logger.debug (getDepth(nbSecondes))
        logger.debug (getHDG(nbSecondes, cap, variationMagnetique))
        logger.debug (getWindInfo(nbSecondes, "Relative", 150.0, 12.2))
        logger.debug (getWindInfo(nbSecondes, "True", 150.0, 12.2))
        logger.debug (getWayPointInfoBWR(now, positionDepartLatitudeDecimale + 2.0, positionDepartLongitudeDecimale + 2.0, 45.0, variationMagnetique, 2.98, "WP1"))
        logger.debug (getWayPointInfoBWC(now, positionDepartLatitudeDecimale + 2.0, positionDepartLongitudeDecimale + 2.0, 45.0, variationMagnetique, 2.98, "WP1"))
        logger.info ("Position duree:{duree:d} lat:{lat} long:{long}".format(duree=nbSecondes, lat=fromLatDecimalToStr(positionLatitudeDecimale), long=fromLongDecimalToStr(positionLongitudeDecimale)))
'''


class mySocket:
    __sock : socket = None
    __debug : bool = False
    
    def __init__(self, debug = False):
        mySocket.__debug = debug

        # TCP
        # handler_class = MyServer.MyServer
        # with socketserver.TCPServer(("", TCP_PORT), handler_class) as httpd:
        #     print("serving at port", TCP_PORT)
        #     httpd.serve_forever()

        if mySocket.__sock is None and not debug:
            mySocket.__sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM) # UDP
    
    @staticmethod
    def closeAll():
        if not mySocket.__debug:
            mySocket.__sock.close()

    def send(self, trames : List[bytes]):
        if not mySocket.__debug:
            sock = mySocket.__sock
            for t in trames :
                sock.sendto(t, (myEnv.UDP_IP, myEnv.UDP_PORT))
                sock.sendto('\x0d\x0a'.encode(encoding="utf-8"), (myEnv.UDP_IP, myEnv.UDP_PORT))
 
