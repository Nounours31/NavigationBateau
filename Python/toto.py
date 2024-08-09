import socket 

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

    return hex(xor)[2:].upper()


UDP_IP = "127.0.0.1"
UDP_PORT = 5005

print("UDP target IP: %s" % UDP_IP)
print("UDP target port: %s" % UDP_PORT)

i=0
while True:
    now = datetime.now(tz = timezone.utc)
    # $GPGGA,002153.000,3342.6618,N,11751.3858,W,1,10,1.2,27.0,M,-34.2,M,,0000*5E
    postionLatitude=33.426618 - i * 2 /60
    postionLongitude=117.513858

    postionLatitudeDegre = round(postionLatitude)
    postionLatitudeMinute = (postionLatitude - round(postionLatitude)) * 60.0
    postionLatitudeSens = "N" # S

    postionLongitudeDegre = round(postionLongitude)
    postionLongitudeMinute = (postionLongitude - round(postionLongitude)) * 60.0
    postionLongitudeSens = "W" # "E"

    nmeaMessage = "$GPGGA,{heure:02d}{minute:02d}{seconde:02d}.000,{latDeg:02d}{latMin:02.4f},{latSens},{longDeg:03d}{longMin:02.4f},{longSens},1,10,1.2,27.0,M,-34.2,M,,0000".format(
        heure = now.hour,
        minute = now.minute,
        seconde = now.second,
        latDeg = postionLatitudeDegre,
        latMin = postionLatitudeMinute,
        latSens = postionLatitudeSens,
        longDeg = postionLongitudeDegre,
        longMin = postionLongitudeMinute,
        longSens = postionLongitudeSens)

    message = "".join([nmeaMessage, "*", NMEAChecksum(nmeaMessage)])

    print (nmeaMessage)
    print (message)
    bytes_representation = message.encode(encoding="utf-8")  
    
    sock = socket.socket(socket.AF_INET, # Internet
                        socket.SOCK_DGRAM) # UDP
    sock.sendto(bytes_representation, (UDP_IP, UDP_PORT))

    sleep(1)
    i = i +1
    if i > 120:
        break


