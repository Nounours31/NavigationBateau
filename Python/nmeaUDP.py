import socket 
import math

from time import sleep
from datetime import datetime
from datetime import timezone

def xor_two_str(a,b):
    xored = []
    for i in range(max(len(a), len(b))):
        xored_value = ord(a[i%len(a)]) ^ ord(b[i%len(b)])
        xored.append(hex(xored_value)[2:])
    return ''.join(xored)

# This is a simple calculator to compute the checksum field for the NMEA protocol. 
# The checksum is simple, just an XOR of all the bytes between the $ and the * (not including the delimiters themselves), and written in hexadecimal.
def NMEAChecksum(s):
    xor = ord(s[0]) ^ ord(s[1]) 
    for i in range(2, len(s)):
        xor = xor ^ ord(s[i])

    s = hex(xor)[2:].upper()
    if len(s) < 2:
        s = "0" + s
    return s 

def addNMEACheckSum(message : str) -> bytes:
    nmeaMessage = "".join([message, "*", NMEAChecksum(message)])
    bytes_representation = nmeaMessage.encode(encoding="utf-8")
    return bytes_representation

def heure2GPSDecimale(now : datetime) -> float :
    return ((now.hour * 100 + now.minute) * 100) + now.second + (now.microsecond / 1000000)

def angle2GPSDecimale(alpha : float) -> float :
    return (math.floor(alpha) * 100 + (alpha - math.floor(alpha))) * 100

def getGGA(now : datetime, latitude, longitude):
    nmeaMessage = "$GPGGA,{heure:09.2f},{lat:011.6f},{latSens},{long:012.6f},{longSens},1,10,1.2,27.0,M,-34.2,M,,".format(
        heure =  heure2GPSDecimale(now),
        lat = latitude * 100,
        latSens = "N" if latitude > 0 else "S",
        long = longitude * 100,
        longSens = "E" if longitude > 0 else "W")
    return addNMEACheckSum(nmeaMessage)


def getGSA():
    nmeaMessage = "$GPGSA,A,3,07,02,26,27,09,04,15,,,,,,1.8,1.2,1"
    return addNMEACheckSum(nmeaMessage)

# cap t vitesse
def getVTG(vitesseEnNoeud, cap):
    capMagnetique = cap + 2.1
    vitesseEnkm = vitesseEnNoeud * 1.852;
    nmeaMessage = "$GPVTG,{cap:05.1f},T,{capMagnetique:05.1f},M,{vitesseEnNoeud:05.1f},N,{vitesseEnkm:05.1f},K,A".format(
        cap = cap,
        capMagnetique = capMagnetique,
        vitesseEnNoeud = vitesseEnNoeud,
        vitesseEnkm = vitesseEnkm)
    return addNMEACheckSum(nmeaMessage)

def getRMC(now : datetime, latitude, longitude, vitesseEnNoeud, cap):
    dateutc = now.day * 10000 + now.month * 100 + (now.year - 100 * math.floor (now.year / 100))
    nmeaMessage = "$GPRMC,{heure:09.2f},A,{lat:011.6f},{latSens},{long:012.6f},{longSens},{vitesseEnNoeud:06.2f},{cap:06.2f},{dateutc:06d},002.1,W,A,V".format(
        heure =  heure2GPSDecimale(now),
        lat = latitude * 100,
        latSens = "N" if latitude > 0 else "S",
        long = longitude * 100,
        longSens = "E" if longitude > 0 else "W",
        vitesseEnNoeud = vitesseEnNoeud,
        cap = cap,
        dateutc = dateutc)        
    return addNMEACheckSum(nmeaMessage)

def getGSV() -> list[bytes] :
    nmeaMessage = []
    nmeaMessage.append ("$GPGSV,3,1,09,20,49,293,27,05,10,116,,07,19,047,,10,27,057,,1*77".encode(encoding="utf-8"))
    nmeaMessage.append ("$GPGSV,3,2,09,11,06,075,,12,06,025,,19,08,322,,23,63,061,,1*78".encode(encoding="utf-8"))
    nmeaMessage.append ("$GPGSV,3,3,09,25,11,060,,1*4E".encode(encoding="utf-8"))
    return  nmeaMessage

def nav(i : int,vitesseEnNoeud : float, cap : float, latitudeEstimee : float) -> tuple [float, float] :
    capRad = cap * (math.pi / 180.0)
    latitudeEstimeeRad = latitudeEstimee * (math.pi / 180.0)
    pasEnLatitude = math.cos (capRad) * vitesseEnNoeud
    pasEnLongitude = math.sin (capRad) * vitesseEnNoeud / math.cos (latitudeEstimeeRad)
    
    positionLatitude=positionDepartLatitude + pasEnLatitude * i / 3600
    positionLongitude=positionDepartLongitude + pasEnLongitude * i / 3600


    return positionLatitude, positionLongitude

UDP_IP = "127.0.0.1"
UDP_PORT = 5005

print("UDP target IP: %s" % UDP_IP)
print("UDP target port: %s" % UDP_PORT)

sleepTimeInSec=2
nbSecondes=0
vitesseEnNoeud = 50.0
cap = 45.0
latitudeEstimee = 49.0
positionDepartLatitude=48.33 
positionDepartLongitude=2.80

while True:
    now = datetime.now(tz = timezone.utc)

    positionLatitude, positionLongitude = nav (nbSecondes, vitesseEnNoeud, cap, latitudeEstimee)
    # $GPGGA,002153.000,3342.6618,N,11751.3858,W,1,10,1.2,27.0,M,-34.2,M,,0000*5E

    gga = getGGA(now, positionLatitude, positionLongitude)  
    gsa = getGSA()  
    vtg = getVTG(vitesseEnNoeud, cap)  
    rmc = getRMC(now, positionLatitude, positionLongitude,vitesseEnNoeud, cap)  
    gsv = getGSV()  
    print (gga)
    print (gsv)
    print (gsa)
    print (vtg)
    print (rmc)
    

    sock = socket.socket(socket.AF_INET, # Internet
                        socket.SOCK_DGRAM) # UDP
    sock.sendto(gga, (UDP_IP, UDP_PORT))
    
    for x in gsv:
        sock.sendto(x, (UDP_IP, UDP_PORT))
    sock.sendto(gsa, (UDP_IP, UDP_PORT))
    sock.sendto(vtg, (UDP_IP, UDP_PORT))
    sock.sendto(rmc, (UDP_IP, UDP_PORT))

    sleep(sleepTimeInSec)
    nbSecondes = nbSecondes + sleepTimeInSec
    if nbSecondes > 3600:
        break

sock.close

