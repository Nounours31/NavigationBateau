
import logging
import logging.config

from math import floor, cos, sin, tan
from datetime import datetime, timezone
from typing import List

from myEnv import myEnv
from nmea.nmeaLib import nmeaLib
from simulateurNav.simulateurNav import simulateurNav as Nav
from tools.latitude import latitude
from tools.longitude import longitude
from tools.myTools import tools as tools
from tools.position import position
from tools.vecteur import vecteur

# Initialize the logger once as the application starts up.
logging.config.fileConfig('logging.conf')
 
# Get an instance of the logger and use it to write a log!
# Note: Do this AFTER the config is loaded above or it won't use the config.
logger = logging.getLogger(__name__)
logger.info(f"Configured the logger for {__name__}!")


# ----------------------------------------------------------------------------------
# Oui, il est possible de connecter TZ iBoat à des instruments externes qui envoient des NMEA0183 via Wi-Fi. TZ iBoat peut aujourd’hui décoder les données suivantes :
# 
#     Position (GLL, GGA, RMC, VDO)
#     Cap fond et vitesse (RMC, VTG, VDO)
#     Cap surface (HDG, HDM, HDT, VHW, PFECATT, VDO)
#     Profondeur (DBT, DPT)
#     Vent (MWV, MWD)
#     Température de surface de la mer (MDA, MTW)
#     AIS (VDM) (*)
# 
# TZ iBoat est compatible avec toute passerelle Wi-Fi qui supporte les phrases NMEA0183 via TCP ou UDP. Bien que TZ iBoat ne soit pas directement compatible avec les données NMEA2000, toutes les passerelles NMEA2000 vers Wi-Fi peuvent convertir des données NMEA2000 en données NMEA0183 avant de les envoyer en UDP ou TCP. Ci-dessous quelques appareils qui ont été testés avec succès avec TZ iBoat :
# 
#     Multiplexeur MiniPlex-3Wi NMEA0183 ShipModul avec Wi-Fi : Plus d’informations
#     Serveur NMEA0183 Wi-Fi WLN10 Digital Yacht : Plus d’informations
#     Passerelle NMEA2000 vers Wi-Fi W2K-1 Actisense : Plus d’informations
#     Interface NMEA vers Wi-Fi i300W Comar : Plus d’informations
#     Passerelle NMEA2000 Wi-Fi YDWG-02 Yacht Devices : Plus d’informations
#     Transpondeurs et multiplexeur AIS Vesper XB-8000 (*) - Plus d’informations
#     Récepteur iAIS Digital Yacht (*) - Plus d’informations
#     Transpondeur AIS Digital Yacht Nomad (*) - Plus d’informations
# 
# En général, la passerelle NMEA0183 Wi-Fi crée son propre point d’accès au réseau sans fil. L’iPhone ou iPad doit être configuré pour se connecter à ce réseau Wi-Fi. Si la passerelle Wi-Fi est configurée pour émettre des phrases NMEA0183 par Wi-Fi par le protocole UDP, il vous suffit d’entrer le numéro du port UDP dans TZ iBoat. Si c’est le protocole TCP qui est utilisé, il vous faut entrer le numéro du port TCP et l’adresse IP de la passerelle Wi-Fi dans TZ iBoat. Pour la plupart des applications, nous recommandons d’utiliser le protocole UDP lorsque possible.
# 
# (*) L’affichage des cibles AIS est possible uniquement après achat du module optionnel AIS.
# ----------------------------------------------------------------------------------


'''
Trame NMEA d’un GPS

par Cédrick FAURY · Publié 7 novembre 2022 · Mis à jour 17 novembre 2023


Protocole NMEA

La norme NMEA 0183 est une spécification pour la communication entre équipements électroniques marins (GPS, radar, girouette, 
pilote automatique, …). Elle est définie et contrôlée par la National Marine Electronics Association (NMEA), 
association américaine de fabricants d’appareils électroniques maritimes.

Dans le cas d’un GPS, les trames sont transmises en boucle via une simple transmission série 
à la vitesse de 4800 baud, sous la forme d’une suite de caractères ASCII, tous imprimables, ainsi que les caractères [CR] 
Retour Chariot et [LF] Retour à la ligne.

Chaque trame commence par le caractère $, suivi par un groupe de 2 lettres pour l’identifiant du récepteur 
(par exemple GP pour Global Positioning System). La virgule est utilisée pour séparer les différents champs de donnée.

Suit un groupe de 3 lettres pour l’identifiant de la trame, comme par exemple :

    GGA: pour GPS Fix et Date,
    GLL: pour Positionnement Géographique Longitude – Latitude,
    GSA: pour DOP et satellites actifs,
    GSV: pour Satellites visibles,
    VTG: pour Direction (cap) et vitesse de déplacement (en nœuds et km/h),
    RMC: pour données minimales exploitables spécifiques.

En queue de trame, un champ nommé checksum, précédé du signe *, représente 
le OU exclusif de tous les caractères compris entre $et *(exclus).

 

Parmi les messages émis par les GPS on rencontre les données d’acquisition du FIX (trame GGA) :

$GPGGA,123519,4807.038,N,01131.324,E,1,08,0.9,545.4,M,46.9,M, , *42

        123519      = Acquisition du FIX à 12:35:19 UTC
        4807.038,N  = Latitude 48°07.038′ N
        01131.324,E = Longitude 11°31.324′ E
        1           = Fix qualification : (0 = non valide, 1 = Fix GPS, 2 = Fix DGPS)
        08          = Nombre de satellites en poursuite.
        0.9         = DOP (horizontal dilution of position) Dilution horizontale.
        545.4,M     = Altitude, en Mètres, au dessus du MSL (mean see level) niveau moyen des Océans.
        46.9,M      = Correction de la hauteur de la géoïde en Mètres par rapport à l’ellipsoïde WGS84 (MSL).
        (Champ vide)= nombre de secondes écoulées depuis la dernière mise à jour DGPS.
        (Champ vide)= Identification de la station DGPS.
        *42         = checksum

-----------------------------------------------------------------------------------------------------------        
Talker ID

Le type d'équipement à l'origine du signal (talker id) est défini par les deux caractères qui suivent le $[2]. Les principaux préfixes sont :

    BD ou GB - Beidou ;
    GA - Galileo ;
    GP - GPS ;
    GL - GLONASS
Autre talker:
    AG = Pilote automatique ( cas général )
    AP = Pilote automatique ( magnétique)
    CC = Ordinateur
    CD = Appel sélectif ( radio) 
    CS = Communication par satellite
    CT = Radio téléphone MF/HF
    CV = Radio téléphone VHF
    CX = Récepteur à scanner
    DE = DECCA
    DF = « direction finder »
    EC = Cartographie électronique et système d'information
    EP = Balise de position de détresse
    ER = Système de contrôle de la salle des machines
    GP = GPS
    HC = Compas
    HE = Gyroscope «  North Seeking »
    HN = Gyroscope «   Non-North Seeking »
    II = Instrument intégré
    IN = Instrument de navigation intégré
    LA = Loran A
    LC = Loran C
    OM = Oméga
    RA =Radar
    SD = Sondeur
    TR = Système de positionnement
    SS = Sondeur à scanner
    TI = «       Turn Rate Indicator « 
    TR = Système de navigation TRANSIT
    VD = Capteur de vitesse à effet doppler
    VM = Capteur de vitesse magnétic
    VW = Capteur de vitesse mécanique
    YX = Transducteur
    ZA = Horloge atomique
    ZC = Chronomètre
    ZQ = Quartz
    ZV = Mise à l'heure radio        
    WI = Station météo


'''

class nmea0183lib(nmeaLib) :
    FORMAT_LAT : int = 0
    FORMAT_LONGI : int = 1

    def __init__(self):
        super().__init__()
        self.__logger : logging.Logger = myEnv.logger

    # ----------------------------------------------------------------------------------
    # concatene devant le message le "$" et ajoute en fin le "*" + checksum nmea
    # ----------------------------------------------------------------------------------
    @staticmethod
    def __addNMEACheckSum(message : str) -> bytes:
        nmeaMessage = "".join(["$", message, "*", nmea0183lib.__NMEAChecksum(message)])
        bytes_representation = nmeaMessage.encode(encoding="utf-8")
        return bytes_representation
    
    # ----------------------------------------------------------------------------------
    # This is a simple calculator to compute the checksum field for the NMEA protocol. 
    # The checksum is simple, just an XOR of all the bytes between the $ and the * (not including the delimiters themselves), and written in hexadecimal.
    # ----------------------------------------------------------------------------------
    @staticmethod
    def __NMEAChecksum(s : str) -> str :
        xor = ord(s[0]) ^ ord(s[1]) 
        for i in range(2, len(s)):
            xor = xor ^ ord(s[i])

        retour = hex(xor)[2:].upper()
        if len(retour) < 2:
            retour = "0" + retour
        return retour 

    @staticmethod
    def __nmeaLatitudeFormat(l: latitude) -> str :
        return nmea0183lib.__nmeaLatLongFormat (l.valAsDeg, nmea0183lib.FORMAT_LAT)

    @staticmethod
    def __nmeaLongitudeFormat(l: longitude) -> str :
        return nmea0183lib.__nmeaLatLongFormat (l.valAsDeg, nmea0183lib.FORMAT_LONGI)

    @staticmethod
    def __nmeaLatLongFormat(x: float, f : int) -> str :
        # E 7,682288°  -> 7° 40' 56.238" Est --> 00740.9373,E
        x = abs(x)
        deg : int = int(floor(x))
        min : float = (x - deg) * 60.0

        if f == nmea0183lib.FORMAT_LONGI:
            return f"{deg:03d}{min:07.4f}"
        if f == nmea0183lib.FORMAT_LAT:
            return f"{deg:02d}{min:07.4f}"
        return "xxxxxx"

    def computeTrames(self, nav: Nav) -> List[bytes]:
        retour : List[bytes] = []
        retour.append(self.getGGA(nav.heure, nav.positionCourante))
        """
        retour.append(self.getRMC(
            nav.heure, 
            nav.positionCourante.latitude.val, 
            nav.positionCourante.longitude.val,
            nav.vitesse, 
            nav.cap) )
        retour.append(self.getVTG(nav.vitesse, nav.cap, nav.variationMagnetiqueEnDeg))
        retour.append(self.getGSA())
        for t in self.getGSV():
            retour.append(t)

        retour.append(self.getDepth(nav.profondeur)))
        retour.append(self.getHDG(nav.capEnDeg, nav.variationMagnetiqueEnDeg))
        retour.append(self.getWindInfo(nav, "Relative", 150.0, 12.2))
        retour.append(self.getWindInfo(nbSecondes, "True", 150.0, 12.2))
        retour.append(self.getWayPointInfoBWR(now, positionDepartLatitudeDecimale + 2.0, positionDepartLongitudeDecimale + 2.0, 45.0, variationMagnetique, 2.98, "WP1"))
        retour.append(self.getWayPointInfoBWC(now, positionDepartLatitudeDecimale + 2.0, positionDepartLongitudeDecimale + 2.0, 45.0, variationMagnetique, 2.98, "WP1"))
        """
        if self.__logger.isEnabledFor(logging.DEBUG) :
            for t in retour:
                self.__logger.debug(t)

        return retour

    # ==================================================================================
    #     Position (GLL, GGA, RMC, VDO)
    # ==================================================================================
    #    GLL - Position géographique, Latitude / Longitude
    # This is one of the sentences commonly emitted by GPS units.
    #         1      2    3     4    5      6 7
    #         |      |    |     |    |      | |
    # $--GLL,ddmm.mm,a,dddmm.mm,a,hhmmss.ss,a*hh<CR><LF>
    # NMEA 2.3:
    # $--GLL,ddmm.mm,a,dddmm.mm,a,hhmmss.ss,a,m*hh<CR><LF>
    # Field Number:
    # 1. Latitude, dd is degrees, mm.mm is minutes
    # 2. N or S (North or South)
    # 3. Longitude, dd is degrees, mm.mm is minutes
    # 4. E or W (East or West)
    # 5. UTC of this position, hh is hours, mm is minutes, ss.ss is seconds
    # 6. Status A - Data Valid, V - Data Invalid
    # 7. FAA mode indicator (NMEA 2.3 and later)
    # 8. Checksum
    # The number of digits past the decimal point for Time, Latitude and Longitude is model dependent.
    # Example: $GNGLL,4404.14012,N,12118.85993,W,001037.00,A,A*67
    # ----------------------------------------------------------------------------------
    def getGLL(now : datetime, latitudeDecimale : float, longitudeDecimale : float):
        pass

    def getVDO(now : datetime, latitudeDecimale : float, longitudeDecimale : float):
        pass

    '''
        Elle est très courante car elle fait partie de celles qui sont utilisées pour connaître la position courante du récepteur GPS.

        $GPGGA,064036.289,4836.5375,N,00740.9373,E,1,04,3.2,200.2,M,,,,0000*0E

        $GPGGA       : Type de trame
        064036.289   : Trame envoyée à 06 h 40 min 36 s 289 (heure UTC)
        4836.5375,N  : Latitude 48,608958° Nord = 48° 36' 32.25" Nord
        00740.9373,E : Longitude 7,682288° Est = 7° 40' 56.238" Est
        1            : Type de positionnement (le 1 est un positionnement GPS)
        04           : Nombre de satellites utilisés pour calculer les coordonnées
        3.2          : Précision horizontale ou HDOP (Horizontal dilution of precision)
        200.2,M      : Altitude 200,2, en mètres
        ,,,,,0000    : D'autres informations peuvent être inscrites dans ces champs
        *            : séparateur de checksum
        0E           : Somme de contrôle de parité, un simple XOR sur les caractères entre $ et *
    '''
    def getGGA(self, now : datetime, positionCourante : position) -> bytes:
        nmeaMessage = "GPGGA,{heure:09.2f},{lat},{latSens},{long},{longSens},1,10,1.2,27.0,M,-34.2,M,,".format(
            heure =  tools.heure2GPSDecimale(now),
            lat = nmea0183lib.__nmeaLatitudeFormat(positionCourante.latitude),
            latSens = positionCourante.latitude.sensAsString(),
            long = nmea0183lib.__nmeaLongitudeFormat(positionCourante.longitude),
            longSens = positionCourante.longitude.sensAsString())
        return nmea0183lib.__addNMEACheckSum(nmeaMessage)
    
    '''
    Une autre trame très courante pour les bateaux est la RMC, qui donne l'heure, la latitude, la longitude, la date, ainsi que la vitesse et la route sur le fond mais pas l'altitude. 
        $GPRMC,053740.000,A,2503.6319,N,12136.0099,E,2.69,79.65,100106,,,A*53

        $GPRMC       : type de trame
        053740.000   : heure UTC exprimée en hhmmss.sss : 5 h 37 min 40 s
        A            : état A=données valides, V=données invalides
        2503.6319    : Latitude exprimée en ddmm.mmmmm: 25° 03.6319' = 25° 03' 37,914"
        N            : indicateur de latitude N=nord, S=sud
        12136.0099   : Longitude exprimée en dddmm.mmmmm: 121° 36.0099' = 121° 36' 00,594"
        E            : indicateur de longitude E=est, W=ouest
        2.69         : vitesse sur le fond en nœuds (2,69 nd = 3,10 mph = 4,98 km/h)
        79.65        : route sur le fond en degrés
        100106       : date exprimée en format « jjmmaa » : 10 janvier 2006
        ,            : déclinaison magnétique en degrés (souvent vide pour un GPS)
        ,            : sens de la déclinaison E=est, W=ouest (souvent vide pour un GPS)
        A            : mode de positionnement A=autonome, D=DGPS, E=DR
        *            : séparateur de checksum
        53           : somme de contrôle de parité au format hexadécimal[4] 
'''
    def getRMC(self, now : datetime, 
               latitudeDecimale : latitude, 
               longitudeDecimale : longitude, 
               vitesse: vecteur) -> bytes :
        dateutc = tools.date2GPSDecimale(now)
        nmeaMessage = "GPRMC,{heure:09.2f},A,{lat:011.6f},{latSens},{long:012.6f},{longSens},{vitesseEnNoeud:06.2f},{cap:06.2f},{dateutc:06d},002.1,W,A,V".format(
            heure =  tools.heure2GPSDecimale(now),
            lat = abs(tools.angleDecimalToMinuteSexa(latitudeDecimale) * 100),
            latSens = latitudeDecimale.sensAsString(),
            long = abs(tools.angleDecimalToMinuteSexa(longitudeDecimale) * 100),
            longSens = longitudeDecimale.sensAsString(),
            vitesseEnNoeud = vitesse.val,
            cap = vitesse.dir.valAsDeg,
            dateutc = dateutc)        
        return nmea0183lib.__addNMEACheckSum(nmeaMessage)



    # ----------------------------------------------------------------------------------
    #     Position (GLL, GGA, RMC, VDO)
    #     Cap fond et vitesse (RMC, VTG, VDO)
    #     Cap surface (HDG, HDM, HDT, VHW, PFECATT, VDO)
    #     Profondeur (DBT, DPT)
    #     Vent (MWV, MWD)
    #     Température de surface de la mer (MDA, MTW)
    #     AIS (VDM) (*)
    # ----------------------------------------------------------------------------------
    def getGSA(self):
        nmeaMessage = "GPGSA,A,3,07,02,26,27,09,04,15,,,,,,1.8,1.2,1"
        return nmea0183lib.__addNMEACheckSum(nmeaMessage)

    # ----------------------------------------------------------------------------------
    #     Position (GLL, GGA, RMC, VDO)
    #     Cap fond et vitesse (RMC, VTG, VDO)
    #     Cap surface (HDG, HDM, HDT, VHW, PFECATT, VDO)
    #     Profondeur (DBT, DPT)
    #     Vent (MWV, MWD)
    #     Température de surface de la mer (MDA, MTW)
    #     AIS (VDM) (*)
    # ----------------------------------------------------------------------------------
    def getVTG(self,vitesseEnNoeud, capEnDeg, deviationEnDeg):
        capMagnetique = capEnDeg + deviationEnDeg
        vitesseEnkm = vitesseEnNoeud * 1.852;
        nmeaMessage = "GPVTG,{capEnDeg:05.1f},T,{capMagnetique:05.1f},M,{vitesseEnNoeud:05.1f},N,{vitesseEnkm:05.1f},K,A".format(
            capEnDeg = capEnDeg,
            capMagnetique = capMagnetique,
            vitesseEnNoeud = vitesseEnNoeud,
            vitesseEnkm = vitesseEnkm)
        return nmea0183lib.__addNMEACheckSum(nmeaMessage)


    # ----------------------------------------------------------------------------------
    #     Position (GLL, GGA, RMC, VDO)
    #     Cap fond et vitesse (RMC, VTG, VDO)
    #     Cap surface (HDG, HDM, HDT, VHW, PFECATT, VDO)
    #     Profondeur (DBT, DPT)
    #     Vent (MWV, MWD)
    #     Température de surface de la mer (MDA, MTW)
    #     AIS (VDM) (*)
    # ----------------------------------------------------------------------------------
    def getGSV(self) -> list[bytes] :
        nmeaMessage = []
        nmeaMessage.append ("$GPGSV,3,1,09,20,49,293,27,05,10,116,,07,19,047,,10,27,057,,1*77".encode(encoding="utf-8"))
        nmeaMessage.append ("$GPGSV,3,2,09,11,06,075,,12,06,025,,19,08,322,,23,63,061,,1*78".encode(encoding="utf-8"))
        nmeaMessage.append ("$GPGSV,3,3,09,25,11,060,,1*4E".encode(encoding="utf-8"))
        return  nmeaMessage

    # ----------------------------------------------------------------------------------
    #     Position (GLL, GGA, RMC, VDO)
    #     Cap fond et vitesse (RMC, VTG, VDO)
    #     Cap surface (HDG, HDM, HDT, VHW, PFECATT, VDO)
    #     Profondeur (DBT, DPT)
    #     Vent (MWV, MWD)
    #     Température de surface de la mer (MDA, MTW)
    #     AIS (VDM) (*)
    # ----------------------------------------------------------------------------------
    def getDepth(self,i: int) -> bytes:
        depth = abs(5 + cos(i) * 100)
        distanceSondeQuille = 1.2
        nmeaMessage = "IIDPT,{depth:02.1f},{ecart:02.1f},".format(
            depth =  depth,
            ecart = -abs(distanceSondeQuille))        
        return nmea0183lib.__addNMEACheckSum(nmeaMessage)

    # ----------------------------------------------------------------------------------
    #     Position (GLL, GGA, RMC, VDO)
    #     Cap fond et vitesse (RMC, VTG, VDO)
    #     Cap surface (HDG, HDM, HDT, VHW, PFECATT, VDO)
    #     Profondeur (DBT, DPT)
    #     Vent (MWV, MWD)
    #     Température de surface de la mer (MDA, MTW)
    #     AIS (VDM) (*)
    # ----------------------------------------------------------------------------------
    def getHDG(self,i: int, cap:float, variation:float) -> bytes:
        capM = (cap + variation)
        if capM < 0:
            capM += 360
        if capM > 360:
            capM -= 360

        nmeaMessage = "IIHDG,{capM:04.2f},{variationM:04.2f},{variationS:s},,".format(
            capM =  capM,
            variationM =  abs(variation),
            variationS = "E" if variation>0 else "W")        
        return nmea0183lib.__addNMEACheckSum(nmeaMessage)

    # ----------------------------------------------------------------------------------
    #     Position (GLL, GGA, RMC, VDO)
    #     Cap fond et vitesse (RMC, VTG, VDO)
    #     Cap surface (HDG, HDM, HDT, VHW, PFECATT, VDO)
    #     Profondeur (DBT, DPT)
    #     Vent (MWV, MWD)
    #     Température de surface de la mer (MDA, MTW)
    #     AIS (VDM) (*)
    # ----------------------------------------------------------------------------------
    def getWindInfo(self,i: int, type: str, angle:float, speedInKnot:float) -> bytes:
        reference = "R" if "Relative" == type else "T"
        nmeaMessage = "IIMWV,{angle:04.2f},{reference:s},{speed:04.2f},K,A".format(
            angle =  angle,
            reference =  reference,
            speed =  speedInKnot)        
        return nmea0183lib.__addNMEACheckSum(nmeaMessage)

    # ----------------------------------------------------------------------------------
    #     Position (GLL, GGA, RMC, VDO)
    #     Cap fond et vitesse (RMC, VTG, VDO)
    #     Cap surface (HDG, HDM, HDT, VHW, PFECATT, VDO)
    #     Profondeur (DBT, DPT)
    #     Vent (MWV, MWD)
    #     Température de surface de la mer (MDA, MTW)
    #     AIS (VDM) (*)
    # ----------------------------------------------------------------------------------
    def getWayPointInfoBWC(self,now: datetime, latDecimale: float, longDecimale: float, bearing:float,  variation:float, distance:float, id:str) -> bytes:
        bearingM = bearing + variation
        nmeaMessage = "IIBWC,{heure:09.2f},{lat:011.6f},{latSens},{long:012.6f},{longSens},{bearing:05.2f},T,{bearingM:05.2f},M,{distance:05.2f},N,{id:s}".format(
            heure =  tools.heure2GPSDecimale(now),
            lat = abs(tools.angleDecimalToMinuteSexa(latDecimale) * 100),
            latSens = "N" if latDecimale > 0 else "S",
            long = abs(tools.angleDecimalToMinuteSexa(longDecimale) * 100),
            longSens = "E" if longDecimale > 0 else "W",
            bearing = bearing,
            bearingM = bearingM,
            distance = distance,
            id = id)      
        return nmea0183lib.__addNMEACheckSum(nmeaMessage)

    # ----------------------------------------------------------------------------------
    #     Position (GLL, GGA, RMC, VDO)
    #     Cap fond et vitesse (RMC, VTG, VDO)
    #     Cap surface (HDG, HDM, HDT, VHW, PFECATT, VDO)
    #     Profondeur (DBT, DPT)
    #     Vent (MWV, MWD)
    #     Température de surface de la mer (MDA, MTW)
    #     AIS (VDM) (*)
    # ----------------------------------------------------------------------------------
    def getWayPointInfoBWR(self,now: datetime, latDecimale: float, longDecimale: float, bearing:float,  variation:float, distance:float, id:str) -> bytes:
        bearingM = bearing + variation
        nmeaMessage = "IIBWR,{heure:09.2f},{lat:011.6f},{latSens},{long:012.6f},{longSens},{bearing:05.2f},T,{bearingM:05.2f},M,{distance:05.2f},N,{id:s}".format(
            heure =  tools.heure2GPSDecimale(now),
            lat = abs(tools.angleDecimalToMinuteSexa(latDecimale) * 100),
            latSens = "N" if latDecimale > 0 else "S",
            long = abs(tools.angleDecimalToMinuteSexa(longDecimale) * 100),
            longSens = "E" if longDecimale > 0 else "W",
            bearing = bearing,
            bearingM = bearingM,
            distance = distance,
            id = id)      
        return nmea0183lib.__addNMEACheckSum(nmeaMessage)

    # ----------------------------------------------------------------------------------
    #     Position (GLL, GGA, RMC, VDO)
    #     Cap fond et vitesse (RMC, VTG, VDO)
    #     Cap surface (HDG, HDM, HDT, VHW, PFECATT, VDO)
    #     Profondeur (DBT, DPT)
    #     Vent (MWV, MWD)
    #     Température de surface de la mer (MDA, MTW)
    #     AIS (VDM) (*)
    # ----------------------------------------------------------------------------------
