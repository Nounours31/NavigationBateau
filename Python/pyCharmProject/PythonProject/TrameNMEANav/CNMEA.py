from datetime import datetime
from math import cos, floor

from TrameNMEANav.CAngle import CAngle
from TrameNMEANav.CHeure import CHeure


class CNMEA:
    def __init__(self, uid : str) -> None:
        self.uid = uid

    def __str__(self) -> str :
        return f"uid {self.uid}"


    # ----------------------------------------------------------------------------------
    # This is a simple calculator to compute the checksum field for the NMEA protocol.
    # The checksum is simple, just an XOR of all the bytes between the $ and the * (not including the delimiters themselves), and written in hexadecimal.
    # ----------------------------------------------------------------------------------
    def nmeachecksum(self, s: str) -> str:
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
    def add_nmeachecksum(self, message: str) -> bytes:
        nmeaMessage = "".join(["$", message, "*", self.nmeachecksum(message)])
        bytes_representation = nmeaMessage.encode(encoding="utf-8")
        return bytes_representation

    # ----------------------------------------------------------------------------------
    def getGGA(self, now: datetime, latitudeDecimale: float, longitudeDecimale: float) -> bytes:
        nmeaMessage = "GPGGA,{heure:09.2f},{lat:011.6f},{latSens},{long:012.6f},{longSens},1,10,1.2,27.0,M,-34.2,M,,".format(
            heure = CHeure.heure2GPSDecimale(now),
            lat=abs(CAngle.angleDecimalToMinuteSexa(latitudeDecimale) * 100),
            latSens="N" if latitudeDecimale > 0 else "S",
            long=abs(CAngle.angleDecimalToMinuteSexa(longitudeDecimale) * 100),
            longSens="E" if longitudeDecimale > 0 else "W")
        return self.add_nmeachecksum(nmeaMessage)


    # ----------------------------------------------------------------------------------
    def getGSA(self)-> bytes:
        nmeaMessage = "GPGSA,A,3,07,02,26,27,09,04,15,,,,,,1.8,1.2,1"
        return self.add_nmeachecksum(nmeaMessage)


    # ----------------------------------------------------------------------------------
    # cap t vitesse
    def getVTG(self, vitesseEnNoeud, cap)-> bytes:
        capMagnetique = cap + 2.1
        vitesseEnkm = vitesseEnNoeud * 1.852
        nmeaMessage = "GPVTG,{cap:05.1f},T,{capMagnetique:05.1f},M,{vitesseEnNoeud:05.1f},N,{vitesseEnkm:05.1f},K,A".format(
            cap=cap,
            capMagnetique=capMagnetique,
            vitesseEnNoeud=vitesseEnNoeud,
            vitesseEnkm=vitesseEnkm)
        return self.add_nmeachecksum(nmeaMessage)


    # ----------------------------------------------------------------------------------
    def getRMC(self, now: datetime, latitudeDecimale, longitudeDecimale, vitesseEnNoeud, cap)-> bytes:
        dateutc = now.day * 10000 + now.month * 100 + (now.year - 100 * floor(now.year / 100))
        nmeaMessage = "GPRMC,{heure:09.2f},A,{lat:011.6f},{latSens},{long:012.6f},{longSens},{vitesseEnNoeud:06.2f},{cap:06.2f},{dateutc:06d},002.1,W,A,V".format(
            heure=CHeure.heure2GPSDecimale(now),
            lat=abs(CAngle.angleDecimalToMinuteSexa(latitudeDecimale) * 100),
            latSens="N" if latitudeDecimale > 0 else "S",
            long=abs(CAngle.angleDecimalToMinuteSexa(longitudeDecimale) * 100),
            longSens="E" if longitudeDecimale > 0 else "W",
            vitesseEnNoeud=vitesseEnNoeud,
            cap=cap,
            dateutc=dateutc)
        return self.add_nmeachecksum(nmeaMessage)


    # ----------------------------------------------------------------------------------
    def getGSV(self) -> list[bytes]:
        nmeaMessage = ["$GPGSV,3,1,09,20,49,293,27,05,10,116,,07,19,047,,10,27,057,,1*77".encode(encoding="utf-8"),
                       "$GPGSV,3,2,09,11,06,075,,12,06,025,,19,08,322,,23,63,061,,1*78".encode(encoding="utf-8"),
                       "$GPGSV,3,3,09,25,11,060,,1*4E".encode(encoding="utf-8")]
        return nmeaMessage


    def getDepth(self, i: int) -> bytes:
        depth = abs(5 + cos(i) * 100)
        distanceSondeQuille = 1.2
        nmeaMessage = "IIDPT,{depth:02.1f},{ecart:02.1f},".format(
            depth=depth,
            ecart=-abs(distanceSondeQuille))
        return self.add_nmeachecksum(nmeaMessage)


    def getHDG(self, cap: float, variation: float) -> bytes:
        capM = (cap + variation)
        if capM < 0:
            capM += 360
        if capM > 360:
            capM -= 360

        nmeaMessage = "IIHDG,{capM:04.2f},{variationM:04.2f},{variationS:s},,".format(
            capM=capM,
            variationM=abs(variation),
            variationS="E" if variation > 0 else "W")
        return self.add_nmeachecksum(nmeaMessage)


    def getWindInfo(self, typeDeVent: str, angle: float, speedInKnot: float) -> bytes:
        reference = "R" if "Relative" == typeDeVent else "T"
        nmeaMessage = "IIMWV,{angle:04.2f},{reference:s},{speed:04.2f},K,A".format(
            angle=angle,
            reference=reference,
            speed=speedInKnot)
        return self.add_nmeachecksum(nmeaMessage)


    def getWayPointInfoBWC(self, now: datetime, latDecimale: float, longDecimale: float, bearing: float, variation: float,
                           distance: float, uid: str) -> bytes:
        bearingM = bearing + variation
        nmeaMessage = "IIBWC,{heure:09.2f},{lat:011.6f},{latSens},{long:012.6f},{longSens},{bearing:05.2f},T,{bearingM:05.2f},M,{distance:05.2f},N,{id:s}".format(
            heure=CHeure.heure2GPSDecimale(now),
            lat=abs(CAngle.angleDecimalToMinuteSexa(latDecimale) * 100),
            latSens="N" if latDecimale > 0 else "S",
            long=abs(CAngle.angleDecimalToMinuteSexa(longDecimale) * 100),
            longSens="E" if longDecimale > 0 else "W",
            bearing=bearing,
            bearingM=bearingM,
            distance=distance,
            id=uid)
        return self.add_nmeachecksum(nmeaMessage)


    # BWR Bearing and Distance to Waypoint – Rhumb Line Latitude, N/S, Longitude, E/W,
    def getWayPointInfoBWR(self, now: datetime, latDecimale: float, longDecimale: float, bearing: float, variation: float,
                           distance: float, uid: str) -> bytes:
        bearingM = bearing + variation
        nmeaMessage = "IIBWR,{heure:09.2f},{lat:011.6f},{latSens},{long:012.6f},{longSens},{bearing:05.2f},T,{bearingM:05.2f},M,{distance:05.2f},N,{id:s}".format(
            heure=CHeure.heure2GPSDecimale(now),
            lat=abs(CAngle.angleDecimalToMinuteSexa(latDecimale) * 100),
            latSens="N" if latDecimale > 0 else "S",
            long=abs(CAngle.angleDecimalToMinuteSexa(longDecimale) * 100),
            longSens="E" if longDecimale > 0 else "W",
            bearing=bearing,
            bearingM=bearingM,
            distance=distance,
            id=uid)
        return self.add_nmeachecksum(nmeaMessage)