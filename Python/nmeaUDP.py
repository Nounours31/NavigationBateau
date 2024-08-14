import socket 
from math import floor, pi, cos, sin, tan

from time import sleep
from datetime import datetime
from datetime import timezone

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

# ----------------------------------------------------------------------------------
# main function
# ----------------------------------------------------------------------------------
def main():
    UDP_IP = "127.0.0.1"
    UDP_PORT = 5005

    print("UDP target IP: %s" % UDP_IP)
    print("UDP target port: %s" % UDP_PORT)

    sleepTimeInSec=2
    nbSecondes=0
    vitesseEnNoeud = 5.0
    cap = 45.0
    variationMagnetique = -1.2
    # Port de St Quay
    latPortStQuay = 48.649665  # angleSexaToDecimal(degre = 2, minute = 56.23)
    longPortStQuay = -2.813217 # angleSexaToDecimal(degre = 2, minute = 56.23)
    
    latitudeEstimeeDecimale = latPortStQuay
    positionDepartLatitudeDecimale = latPortStQuay
    positionDepartLongitudeDecimale = longPortStQuay

    while True:
        now = datetime.now(tz = timezone.utc)
        cap = cap + 5 * cos(nbSecondes)
        vitesseEnNoeud = vitesseEnNoeud + 2 * cos(nbSecondes)
        positionLatitudeDecimale, positionLongitudeDecimale = nav (nbSecondes, vitesseEnNoeud, cap, latitudeEstimeeDecimale, positionDepartLatitudeDecimale, positionDepartLongitudeDecimale)

        gga = getGGA(now, positionLatitudeDecimale, positionLongitudeDecimale)  
        gsa = getGSA()  
        vtg = getVTG(vitesseEnNoeud, cap)  
        rmc = getRMC(now, positionLatitudeDecimale, positionLongitudeDecimale,vitesseEnNoeud, cap)  
        gsv = getGSV()  
        print (gga)
        print (gsv)
        print (gsa)
        print (vtg)
        print (rmc)
        print (getDepth(nbSecondes))
        print (getHDG(nbSecondes, cap, variationMagnetique))
        print (getWindInfo(nbSecondes, "Relative", 150.0, 12.2))
        print (getWindInfo(nbSecondes, "True", 150.0, 12.2))
        print (getWayPointInfoBWR(now, positionDepartLatitudeDecimale + 2.0, positionDepartLongitudeDecimale + 2.0, 45.0, variationMagnetique, 2.98, "WP1"))
        print (getWayPointInfoBWC(now, positionDepartLatitudeDecimale + 2.0, positionDepartLongitudeDecimale + 2.0, 45.0, variationMagnetique, 2.98, "WP1"))
        print ("Position lat:{lat} long:{long}".format(lat=fromLatDecimalToStr(positionLatitudeDecimale), long=fromLongDecimalToStr(positionLongitudeDecimale)))
        

        sock = socket.socket(socket.AF_INET, # Internet
                            socket.SOCK_DGRAM) # UDP
        sock.sendto(gga, (UDP_IP, UDP_PORT))
        sock.sendto('\x0d\x0a'.encode(encoding="utf-8"), (UDP_IP, UDP_PORT))
        
        for x in gsv:
            sock.sendto(x, (UDP_IP, UDP_PORT))
            sock.sendto('\x0d\x0a'.encode(encoding="utf-8"), (UDP_IP, UDP_PORT))
        sock.sendto(gsa, (UDP_IP, UDP_PORT))
        sock.sendto('\x0d\x0a'.encode(encoding="utf-8"), (UDP_IP, UDP_PORT))
        sock.sendto(vtg, (UDP_IP, UDP_PORT))
        sock.sendto('\x0d\x0a'.encode(encoding="utf-8"), (UDP_IP, UDP_PORT))
        sock.sendto(rmc, (UDP_IP, UDP_PORT))
        sock.sendto('\x0d\x0a'.encode(encoding="utf-8"), (UDP_IP, UDP_PORT))
        sock.sendto(getDepth(nbSecondes), (UDP_IP, UDP_PORT))
        sock.sendto('\x0d\x0a'.encode(encoding="utf-8"), (UDP_IP, UDP_PORT))
        sock.sendto(getHDG(nbSecondes, cap, variationMagnetique), (UDP_IP, UDP_PORT))
        sock.sendto('\x0d\x0a'.encode(encoding="utf-8"), (UDP_IP, UDP_PORT))
        sock.sendto(getWindInfo(nbSecondes, "Relative", 150.0, 12.2), (UDP_IP, UDP_PORT))
        sock.sendto('\x0d\x0a'.encode(encoding="utf-8"), (UDP_IP, UDP_PORT))
        sock.sendto(getWindInfo(nbSecondes, "True", 150.0, 12.2), (UDP_IP, UDP_PORT))
        sock.sendto('\x0d\x0a'.encode(encoding="utf-8"), (UDP_IP, UDP_PORT))
        sock.sendto(getWayPointInfoBWR(now, positionDepartLatitudeDecimale + 2.0, positionDepartLongitudeDecimale + 2.0, 45.0, variationMagnetique, 2.98, "WP1"), (UDP_IP, UDP_PORT))
        sock.sendto('\x0d\x0a'.encode(encoding="utf-8"), (UDP_IP, UDP_PORT))
        sock.sendto(getWayPointInfoBWC(now, positionDepartLatitudeDecimale + 2.0, positionDepartLongitudeDecimale + 2.0, 45.0, variationMagnetique, 2.98, "WP1"), (UDP_IP, UDP_PORT))
        sock.sendto('\x0d\x0a'.encode(encoding="utf-8"), (UDP_IP, UDP_PORT))

        sleep(sleepTimeInSec)
        nbSecondes = nbSecondes + sleepTimeInSec
        if nbSecondes > 300:
            break

    sock.close

if __name__ == '__main__':
    main()