
from myEnv import myEnv
from logging import Logger

class nmeaLib:
    def __init__(self):
        self.__logger : Logger = myEnv.logger
        
    
    def main():
        UDP_IP = "127.0.0.1"
        UDP_PORT = 5005

        global sock
        sock = socket.socket(socket.AF_INET, # Internet
                                socket.SOCK_DGRAM) # UDP

        logger.debug("UDP target IP: %s" % UDP_IP)
        logger.debug("UDP target port: %s" % UDP_PORT)

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
            #if nbSecondes > 300:
            #    break