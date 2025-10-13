
import logging
import logging.config
import math
from idlelib.textview import view_text

from typing import List, Dict

from math import floor, cos, sin, tan
from datetime import datetime, timezone, tzinfo
from zoneinfo import ZoneInfo

from myEnv import myEnv
from nmea.nmeaLib import nmeaLib
from simulateurNav.simulateurNav import simulateurNav as Nav
from tools.latitude import latitude
from tools.longitude import longitude
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
    distanceSondeQuilleEnMetre: float = 1.45
    MILLE2KM_HEURE: float = 1.852
    PIED2METRE: float = 0.33
    FANTOM2METRE: float = 1.8288
    InchMercure2Bar: float = 0.0334211

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

    @staticmethod
    def date2GPSDecimale(now : datetime) -> int :
        # 100106       : date exprimée en format « jjmmaa » : 10 janvier 2006
        return int(now.day * 10000 + now.month * 100 + (now.year - 100 * floor (now.year / 100)))

    @staticmethod
    def heure2GPSDecimale(now : datetime) -> float :
        #  064036.289   : Trame envoyée à 06 h 40 min 36 s 289 (heure UTC)
        return ((now.hour * 100 + now.minute) * 100) + now.second + (now.microsecond / 1000000)

    def computeTrames(self, nav: Nav) -> List[bytes]:
        retour : List[bytes] = []

        tzinfo = timezone.utc
        utc_time = nav.heure.astimezone(tzinfo)
        retour.append(self.getGGA(utc_time, nav.positionCourante))
        retour.append(self.getGLL(utc_time, nav.positionCourante))
        retour.append(self.getGSA(nav.satellite))
        for t in self.getGSV(nav.satellite):
            retour.append(t)
        retour.append(self.getVTG(nav.vitesse, nav.variationMagnetiqueEnDeg))
        retour.append(self.getRMC(utc_time, nav.positionCourante, nav.vitesse))
        for t in self.getDPT(nav.profondeur):
            retour.append(t)

        for t in self.getHDG(vitesse=nav.vitesse, variation=nav.variationMagnetiqueEnDeg):
            retour.append(t)

        for t in self.getVHW(vitesse=nav.vitesse, variation=nav.variationMagnetiqueEnDeg):
            retour.append(t)

        for t in self.getMTW(nav.eauTemp, nav.airTemp):
            retour.append(t)

        for t in self.getWindInfo(nav.ventReel, nav.ventApparent, nav.variationMagnetiqueEnDeg, nav.vitesse):
            retour.append(t)

        """
        retour.append(self.getWayPointInfoBWR(now, positionDepartLatitudeDecimale + 2.0, positionDepartLongitudeDecimale + 2.0, 45.0, variationMagnetique, 2.98, "WP1"))
        retour.append(self.getWayPointInfoBWC(now, positionDepartLatitudeDecimale + 2.0, positionDepartLongitudeDecimale + 2.0, 45.0, variationMagnetique, 2.98, "WP1"))
        """
        if self.__logger.isEnabledFor(logging.DEBUG) :
            for t in retour:
                self.__logger.debug(t)

        return retour




    # ----------------------------------------------------------------------------------
    #     Position (GLL, GGA, RMC, VDO)
    #     Cap fond et vitesse (RMC, VTG, VDO)
    #     Cap surface (HDG, HDM, HDT, VHW, PFECATT, VDO)
    #     Profondeur (DBT, DPT)
    #     Vent (MWV, MWD)
    #     Température de surface de la mer (MDA, MTW)
    #     AIS (VDM) (*)
    # ----------------------------------------------------------------------------------
    """
    MTW - Mean Temperature of Water
        1 2 3
        | | |
        $--MTW,x.x,C*hh<CR><LF>
        Field Number:
        1. Temperature, degrees
        2. Unit of Measurement, Celsius
        3. Checksum
        [GLOBALSAT] lists this as "Meteorological Temperature of Water", which is probably incorrect.
        Example: $INMTW,17.9,C*1B
    MDA - Meteorological Composite
        1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21
        | | | | | | | | | | | | | | | | | | | | |
        $--MDA,n.nn,I,n.nnn,B,n.n,C,n.C,n.n,n,n.n,C,n.n,T,n.n,M,n.n,N,n.n,M*hh<CR><LF>
        Field Number:
        1. Barometric pressure, inches of mercury, to the nearest 0.01 inch
        2. I = inches of mercury
        3. Barometric pressure, bars, to the nearest .001 bar
        4. B = bars
        5. Air temperature, degrees C, to the nearest 0.1 degree C
        6. C = degrees C
        7. Water temperature, degrees C (this field left blank by WeatherStation)
        8. C = degrees C
        9. Relative humidity, percent, to the nearest 0.1 percent
        10. Absolute humidity, percent
        11. Dew point, degrees C, to the nearest 0.1 degree C
        12. C = degrees C
        13. Wind direction, degrees True, to the nearest 0.1 degree
        14. T = true
        15. Wind direction, degrees Magnetic, to the nearest 0.1 degree
        16. M = magnetic
        17. Wind speed, knots, to the nearest 0.1 knot
        18. N = knots
        19. Wind speed, meters per second, to the nearest 0.1 m/s
        20. M = meters per second
        21. Checksum
        Obsolete as of 2009.
    """
    def getMTW(self, Teau: float,Tair: float) -> List[bytes]:
        retour: List[bytes] = []
        nmeaMessage = f"IIMTW,{Teau:04.2f},C"
        retour.append(nmea0183lib.__addNMEACheckSum(nmeaMessage))

#        pAtm = 1.013
#        nmeaMessage = f"IIMDA,{(pAtm * nmea0183lib.InchMercure2Bar):04.2f},I,{pAtm:04.2f},B,{Tair:04.2f},C,{Teau:04.2f},C,0.0,0.0,10.0,C,"
#        retour.append(nmea0183lib.__addNMEACheckSum(nmeaMessage))

        return retour

    """
    VWR - Relative Wind Speed and Angle
        1 2 3 4 5 6 7 8 9
        | | | | | | | | |
        $--VWR,x.x,a,x.x,N,x.x,M,x.x,K*hh<CR><LF>
        Field Number:
        1. Wind direction magnitude in degrees
        2. Wind direction Left/Right of bow
        3. Speed
        4. N = Knots
        5. Speed
        6. M = Meters Per Second
        7. Speed
        8. K = Kilometers Per Hour
        9. Checksum
    
        
    MWD - Wind Direction & Speed
        1 2 3 4 5 6 7 8 9
        | | | | | | | | |
        $--MWD,x.x,a,x.x,a,x.x,a,x.x,a*hh<CR><LF>
        Field Number:
        1. Wind Direction, 0 to 359.9 degrees
        2. Reference, T = True, M = Magnetic
        3. Wind Direction, 0 to 359.9 degrees
        4. Reference, T = True, M = Magnetic
        5. Wind Speed
        6. Wind Speed Units, K/M/N
        7. Wind Speed
        8. Wind Speed Units, K/M/N
        9. Checksum
        Example: $WIMWD,302.4,T,289.6,M,10.5,N,5.4,M*6F
        
    MWV - Wind Speed and Angle
        1 2 3 4 5
        | | | | |
        $--MWV,x.x,a,x.x,a*hh<CR><LF>
        Field Number:
        1. Wind Angle, 0 to 359 degrees
        2. Reference, R = Relative, T = True
        3. Wind Speed
        4. Wind Speed Units, K/M/
        5. Status, A = Data Valid, V = Invalid
        6. Checksum
    """
    def getWindInfo(self, ventReel: vecteur, ventApparent: vecteur, variation: float, vitesse: vecteur) -> list[bytes]:
        retour : List[bytes] = []


        # Wind direction Left / Right of bow
        WindBowDir = "R" if (vitesse.dir.valAsDeg > ventApparent.dir.valAsDeg) else "L"
        vitesseVentN = ventApparent.val
        vitesseVentK = ventApparent.val * nmea0183lib.MILLE2KM_HEURE
        vitesseVentM = vitesseVentK / 3.6
        nmeaMessage = f"IIVWR,{ventApparent.dir.valAsDeg:04.2f},{WindBowDir},{vitesseVentN:04.2f},N,{vitesseVentM:04.2f},M,{vitesseVentM:04.2f},K"
        retour.append(nmea0183lib.__addNMEACheckSum(nmeaMessage))

        vitesseVentN = ventReel.val
        vitesseVentK = ventReel.val * nmea0183lib.MILLE2KM_HEURE
        vitesseVentM = vitesseVentK / 3.6
        nmeaMessage = f"IIMWD,{ventReel.dir.valAsDeg:04.2f},T,{(ventReel.dir.valAsDeg + variation):04.2f},M,{vitesseVentN:04.2f},N,{vitesseVentM:04.2f},M"
        retour.append(nmea0183lib.__addNMEACheckSum(nmeaMessage))

        nmeaMessage = f"IIMWV,{ventReel.dir.valAsDeg:04.2f},T,{vitesseVentM:04.2f},A"
        retour.append(nmea0183lib.__addNMEACheckSum(nmeaMessage))

        return retour



    # ----------------------------------------------------------------------------------
    #     Position (GLL, GGA, RMC, VDO)
    #     Cap fond et vitesse (RMC, VTG, VDO)
    #     Cap surface (HDG, HDM, HDT, VHW, PFECATT, VDO)
    #     Profondeur (DBT, DPT)
    #     Vent (MWV, MWD)
    #     Température de surface de la mer (MDA, MTW)
    #     AIS (VDM) (*)
    # ----------------------------------------------------------------------------------

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
            heure =  nmea0183lib.heure2GPSDecimale(now),
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
            heure =  nmea0183lib.heure2GPSDecimale(now),
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


    # ----------------------------------------------------------------------------------
    #  Info AIS - ?
    # ----------------------------------------------------------------------------------
    def getVDO(h: datetime, latitudeDecimale: float, longitudeDecimale: float):
        pass
    def getVDM(h: datetime, latitudeDecimale: float, longitudeDecimale: float):
        pass


    # ----------------------------------------------------------------------------------
    #  SONDEUR - SD* - Profondeur (DBT, DPT)
    # ----------------------------------------------------------------------------------
    """
    DPT - Depth of Water
        1 2 3 4
        | | | |
        $--DPT,x.x,x.x,x.x*hh<CR><LF>
        Field Number:
        1. Water depth relative to transducer, meters
        2. Offset from transducer, meters positive means distance from transducer to water line negative means distance from transducer to keel
        3. Maximum range scale in use (NMEA 3.0 and above)
        4. Checksum
        This sentence was incorrectly titled "Heading - Deviation & Variation" in [BETKE]. It’s documented at http://www.humminbird.com/normal.asp?id=853
        Example: $INDPT,2.3,0.0*46

    DBK - Depth Below Keel
        1 2 3 4 5 6 7
        | | | | | | |
        $--DBK,x.x,f,x.x,M,x.x,F*hh<CR><LF>
        Field Number:
        1. Depth, feet
        2. f = feet
        3. Depth, meters
        4. M = meters
        5. Depth, Fathoms
        6. F = Fathoms
        7. Checksum
    DBS - Depth Below Surface
        1 2 3 4 5 6 7
        | | | | | | |
        $--DBS,x.x,f,x.x,M,x.x,F*hh<CR><LF>
        Field Number:
        1. Depth, feet
        2. f = feet
        3. Depth, meters
        4. M = meters
        5. Depth, Fathoms
        6. F = Fathoms
        7. Checksum
    DBT - Depth below transducer
        1 2 3 4 5 6 7
        | | | | | | |
        $--DBT,x.x,f,x.x,M,x.x,F*hh<CR><LF>
        Field Number:
        1. Water depth, feet
        2. f = feet
        3. Water depth, meters
        4. M = meters
        5. Water depth, Fathoms
        6. F = Fathoms
        7. Checksum
        In real-world sensors, sometimes not all three
        """

    def getDPT(self, profondeur: float) -> List[bytes]:
        nmeaMessage = []
        x = "SDDBS,{depthP:02.1f},f,{depth:02.1f},M,{depthF:02.1f},F".format(
            depthP=profondeur / nmea0183lib.PIED2METRE,
            depth=profondeur,
            depthF=profondeur / nmea0183lib.FANTOM2METRE)
        nmeaMessage.append(nmea0183lib.__addNMEACheckSum(x))

        x = "SDDPT,{depth:02.1f},{ecart:02.1f},".format(
            depth=profondeur,
            ecart=-abs(nmea0183lib.distanceSondeQuilleEnMetre))
        nmeaMessage.append(nmea0183lib.__addNMEACheckSum(x))

        profondeur = profondeur - nmea0183lib.distanceSondeQuilleEnMetre
        x = "SDDBK,{depthP:02.1f},f,{depth:02.1f},M,{depthF:02.1f},F".format(
            depthP=profondeur / nmea0183lib.PIED2METRE,
            depth=profondeur,
            depthF=profondeur / nmea0183lib.FANTOM2METRE)
        nmeaMessage.append(nmea0183lib.__addNMEACheckSum(x))

        x = "SDDBT,{depthP:02.1f},f,{depth:02.1f},M,{depthF:02.1f},F".format(
            depthP=profondeur / nmea0183lib.PIED2METRE,
            depth=profondeur,
            depthF=profondeur / nmea0183lib.FANTOM2METRE)
        nmeaMessage.append(nmea0183lib.__addNMEACheckSum(x))

        return nmeaMessage



    # ----------------------------------------------------------------------------------
    #  Info GPS - GP*
    # ----------------------------------------------------------------------------------
    # ----------------------------------------------------------------------------------
    #        Info vitesse
    # ----------------------------------------------------------------------------------
    """
    VTG - Track made good and Ground speed
        This is one of the sentences commonly emitted by GPS units.
        1 2 3 4 5 6 7 8 9
        | | | | | | | | |
        $--VTG,x.x,T,x.x,M,x.x,N,x.x,K*hh<CR><LF>
        NMEA 2.3:
        $--VTG,x.x,T,x.x,M,x.x,N,x.x,K,m*hh<CR><LF>
        Field Number:
        1. Course over ground, degrees True
        2. T = True
        3. Course over ground, degrees Magnetic
        4. M = Magnetic
        5. Speed over ground, knots
        6. N = Knots
        7. Speed over ground, km/h
        8. K = Kilometers Per Hour
        9. FAA mode indicator (NMEA 2.3 and later)
            Table 1. FAA Mode Indicator
            A Autonomous mode
            D Differential mode
            E Estimated (dead reckoning) mode
            M Manual input mode
            S Simulator mode
            N Data not valid

        10. Checksum
        Some devices, such as those described in [GLOBALSAT], leave the magnetic-bearing fields 3 and 4 empty.
        Example: $GPVTG,220.86,T,,M,2.550,N,4.724,K,A*34
    """
    def getVTG(self, vitesse : vecteur, deviationEnDeg : float):
        capMagnetique = vitesse.dir.valAsDeg + deviationEnDeg
        vitesseEnkm = vitesse.val * nmea0183lib.MILLE2KM_HEURE
        nmeaMessage = "GPVTG,{capEnDeg:05.1f},T,{capMagnetique:05.1f},M,{vitesseEnNoeud:05.1f},N,{vitesseEnkm:05.1f},K,A".format(
            capEnDeg = vitesse.dir.valAsDeg,
            capMagnetique = capMagnetique,
            vitesseEnNoeud = vitesse.val,
            vitesseEnkm = vitesseEnkm)
        return nmea0183lib.__addNMEACheckSum(nmeaMessage)

    # ----------------------------------------------------------------------------------
    #        Info Positionement
    # ----------------------------------------------------------------------------------
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
    def getGLL(self, utc_time: datetime, positionCourante: position) -> bytes:
        nmeaMessage = "GPGLL,{lat},{latSens},{long},{longSens},{heure:09.2f},A,A".format(
            heure=nmea0183lib.heure2GPSDecimale(utc_time),
            lat=nmea0183lib.__nmeaLatitudeFormat(positionCourante.latitude),
            latSens=positionCourante.latitude.sensAsString(),
            long=nmea0183lib.__nmeaLongitudeFormat(positionCourante.longitude),
            longSens=positionCourante.longitude.sensAsString())
        return nmea0183lib.__addNMEACheckSum(nmeaMessage)
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

    def getGGA(self, utc_time: datetime, positionCourante: position) -> bytes:
        nmeaMessage = "GPGGA,{heure:09.2f},{lat},{latSens},{long},{longSens},1,10,1.2,27.0,M,-34.2,M,,".format(
            heure=nmea0183lib.heure2GPSDecimale(utc_time),
            lat=nmea0183lib.__nmeaLatitudeFormat(positionCourante.latitude),
            latSens=positionCourante.latitude.sensAsString(),
            long=nmea0183lib.__nmeaLongitudeFormat(positionCourante.longitude),
            longSens=positionCourante.longitude.sensAsString())
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

    def getRMC(self, utc_time: datetime, positionCourant: position, vitesse: vecteur) -> bytes:
        nmeaMessage = "GPRMC,{heure:09.2f},A,{lat},{latSens},{long},{longSens},{vitesseEnNoeud:06.2f},{cap:06.2f},{dateutc:06d},002.1,W,A,V".format(
            heure=nmea0183lib.heure2GPSDecimale(utc_time),
            lat=nmea0183lib.__nmeaLatitudeFormat(positionCourant.latitude),
            latSens=positionCourant.latitude.sensAsString(),
            long=nmea0183lib.__nmeaLongitudeFormat(positionCourant.longitude),
            longSens=positionCourant.longitude.sensAsString(),
            vitesseEnNoeud=vitesse.val,
            cap=vitesse.dir.valAsDeg,
            dateutc=nmea0183lib.date2GPSDecimale(utc_time))
        return nmea0183lib.__addNMEACheckSum(nmeaMessage)

    # ----------------------------------------------------------------------------------
    #        Info satellites
    # ----------------------------------------------------------------------------------
    '''
    GSA - GPS DOP and active satellites
        This is one of the sentences commonly emitted by GPS units.
        1 2 3 14 15 16 17 18
        | | | | | | | |
        $--GSA,a,a,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x.x,x.x,x.x*hh<CR><LF>
        Field Number:
        1. Selection mode: M=Manual, forced to operate in 2D or 3D, A=Automatic, 2D/3D
        2. Mode (1 = no fix, 2 = 2D fix, 3 = 3D fix)
        3. ID of 1st satellite used for fix
        4. ID of 2nd satellite used for fix
        5. ID of 3rd satellite used for fix
        6. ID of 4th satellite used for fix
        7. ID of 5th satellite used for fix
        8. ID of 6th satellite used for fix
        9. ID of 7th satellite used for fix
        10. ID of 8th satellite used for fix
        11. ID of 9th satellite used for fix
        12. ID of 10th satellite used for fix
        13. ID of 11th satellite used for fix
        14. ID of 12th satellite used for fix
        15. PDOP
        16. HDOP
        17. VDOP
        18. System ID (NMEA 4.11), see above
        xx. Checksum
        Example: $GNGSA,A,3,80,71,73,79,69,,,,,,,,1.83,1.09,1.47*17
        Note: NMEA 4.1+ systems (u-blox 9, Quectel LCD79) may emit an extra field, System ID, just before the checksum.
        1 = GPS L1C/A, L2CL, L2CM
        2 = GLONASS L1 OF, L2 OF
        3 = Galileo E1C, E1B, E5 bl, E5 bQ
        4 = BeiDou B1I D1, B1I D2, B2I D1, B2I D12
    '''

    def getGSA(self, satellite: Dict[str, object]) -> bytes:
        nmeaMessage: str = "GPGSA,A,3,"
        satIds: str = ""
        nbsat: int = 0
        NBSATMAX: int = 14
        for sat in satellite["IDs"]:
            satIds += f"{sat:02d}"
            nbsat += 1
            if nbsat == NBSATMAX:
                break
            satIds += ","

        for i in range(nbsat, NBSATMAX, 1):
            satIds += ","

        nmeaMessage += satIds
        nmeaMessage += f"{satellite['PDOP']:4.2f},{satellite['HDOP']:4.2f},{satellite['VDOP']:4.2f}"
        return nmea0183lib.__addNMEACheckSum(nmeaMessage)

    '''
    GSV - Satellites in view
        This is one of the sentences commonly emitted by GPS units.
        These sentences describe the sky position of a UPS satellite in view. Typically they’re shipped in a group of 2 or 3.
        1 2 3 4 5 6 7 n
        | | | | | | | |
        $--GSV,x,x,x,x,x,x,x,...*hh<CR><LF>
        Field Number:
        1. total number of GSV sentences to be transmitted in this group
        NMEA Revealed https://gpsd.gitlab.io/gpsd/NMEA.html
        13 sur 28 06/10/2025, 19:19
        2. Sentence number, 1-9 of this GSV message within current group
        3. total number of satellites in view (leading zeros sent)
        4. satellite ID or PRN number (leading zeros sent)
        5. elevation in degrees (-90 to 90) (leading zeros sent)
        6. azimuth in degrees to true north (000 to 359) (leading zeros sent)
        7. SNR in dB (00-99) (leading zeros sent) more satellite info quadruples like 4-7 n-1) Signal ID (NMEA 4.11) n) checksum

        Example: 
            $GPGSV,3,1,11, 03,03,111,00, 04,15,270,00, 06,01,010,00, 13,06,292,00 *74 
            $GPGSV,3,2,11, 14,25,170,00, 16,57,208,39, 18,67,296,40, 19,40,246,00 *74
            $GPGSV,3,3,11, 22,42,067,42, 24,14,311,43, 27,05,244,00, ,,,*4D
        Some GPS receivers may emit more than 12 quadruples (more than three GPGSV sentences), even though NMEA-0813 doesn’t allow this. (The extras might
        be WAAS satellites, for example.) Receivers may also report quads for satellites they aren’t tracking, in which case the SNR field will be null; we don’t know whether
        this is formally allowed or not.
        Example: $GLGSV,3,3,09,88,07,028*51
        Note: NMEA 4.10+ systems (u-blox 9, Quectel LCD79) may emit an extra field, Signal ID, just before the checksum. See the description of Signal ID’s above.
        Note: $GNGSV uses PRN in field 4. Other $GxGSV use the satellite ID in field 4. Jackson Labs, Quectel, Telit, and others get this wrong, in various conflicting
        ways
    '''

    def __extractGSVInfoSatellites(self, indiceSatellite: int, allSatellite: list[str]):
        retour: str = ""

        nbSat: int = len(allSatellite)
        elevation: int = 78
        azimut: int = 254
        snr: int = 45

        nbMaxSatelliteParTrame: int = 4

        for i in range(0, nbMaxSatelliteParTrame, 1):
            if indiceSatellite + i >= nbSat:
                retour += ",,,"
            else:
                retour += f"{allSatellite[indiceSatellite + i]:02d},{elevation:02d},{azimut:03d},{snr:02d}"

            if not i == (nbMaxSatelliteParTrame - 1):
                retour += ','
        return retour

    def getGSV(self, satellite: Dict[str, object]) -> List[bytes]:
        retour: list[bytes] = []
        allSatellite: list[str] = satellite["IDs"]
        nbSat: int = len(allSatellite)
        modulo: int = int(nbSat % 4)
        nbTrame: int = math.floor(nbSat / 4.0) + (0 if modulo == 0 else 1)

        indiceSatellite = 0
        for iTrame in range(1, nbTrame + 1, 1):
            curTrame = f"GPGSV,{nbTrame:1d},{iTrame:1d},{nbSat:02d},{self.__extractGSVInfoSatellites(indiceSatellite, allSatellite)}"
            indiceSatellite += 4
            retour.append(nmea0183lib.__addNMEACheckSum(curTrame))

        return retour

    # ----------------------------------------------------------------------------------
    #     Position (GLL, GGA, RMC, VDO)
    #     Cap fond et vitesse (RMC, VTG, VDO)
    #     Cap surface (HDG, HDM, HDT, VHW, PFECATT, VDO)
    #     Profondeur (DBT, DPT)
    #     Vent (MWV, MWD)
    #     Température de surface de la mer (MDA, MTW)
    #     AIS (VDM) (*)
    # ----------------------------------------------------------------------------------
    """
        HDG - Heading - Deviation & Variation
            1 2 3 4 5 6
            | | | | | |
            $--HDG,x.x,x.x,a,x.x,a*hh<CR><LF>
            Field Number:
            1. Magnetic Sensor heading in degrees
            2. Magnetic Deviation, degrees
            3. Magnetic Deviation direction, E = Easterly, W = Westerly
            4. Magnetic Variation degrees
            5. Magnetic Variation direction, E = Easterly, W = Westerly
            6. Checksum
       HDM - Heading - Magnetic
            Vessel heading in degrees with respect to magnetic north produced by any device or system producing magnetic heading.
            1 2 3
            | | |
            $--HDM,x.x,M*hh<CR><LF>
            Field Number:
            1. Heading Degrees, magnetic
            2. M = magnetic
            3. Checksum
       HDT - Heading - True
            Actual vessel heading in degrees true produced by any device or system producing true heading.
            1 2 3
            | | |
            $--HDT,x.x,T*hh<CR><LF>
            Field Number:
            1. Heading, degrees True
            2. T = True
            3. Checksum
            Example: $GPHDT,274.07,T*03
    """
    def getHDG(self, vitesse: vecteur, variation:float) -> List[bytes] :
        retour : List[bytes] = []
        Cv = vitesse.dir.valAsDeg
        Cc = Cv + variation
        while Cc < 0:
            Cc += 360.0
        while Cc > 360.0:
            Cc -= 360.0
        SensVariation = "E" if variation>0 else "W"

        nmeaMessage = f"IIHDG,{Cc:04.2f},{abs(variation):04.2f},{SensVariation},0.0,E"
        retour.append(nmea0183lib.__addNMEACheckSum(nmeaMessage))

        nmeaMessage = f"IIHDM,{Cc:04.2f},M"
        retour.append(nmea0183lib.__addNMEACheckSum(nmeaMessage))

        nmeaMessage = f"IIHDT,{Cv:04.2f},T"
        retour.append(nmea0183lib.__addNMEACheckSum(nmeaMessage))

        return retour

    """
        VHW - Water speed and heading
            1 2 3 4 5 6 7 8 9
            | | | | | | | | |
            $--VHW,x.x,T,x.x,M,x.x,N,x.x,K*hh<CR><LF>
            Field Number:
            1. Heading degrees, True
            2. T = True
            3. Heading degrees, Magnetic
            4. M = Magnetic
            5. Speed of vessel relative to the water, knots
            6. N = Knots
            7. Speed of vessel relative to the water, km/h
            8. K = Kilometers
            9. Checksum
    """
    def getVHW(self, vitesse: vecteur, variation:float) -> List[bytes] :
        retour : List[bytes] = []
        Cv = vitesse.dir.valAsDeg
        Cc = Cv + variation
        while Cc < 0:
            Cc += 360.0
        while Cc > 360.0:
            Cc -= 360.0
        SensVariation = "E" if variation>0 else "W"

        vitesseLockNoeud : float = vitesse.val * 0.85
        nmeaMessage = f"IIVHW,{Cv:04.2f},T,{Cc:04.2f},M,{vitesseLockNoeud:04.2f},N,{vitesseLockNoeud*nmea0183lib.MILLE2KM_HEURE},K"
        retour.append(nmea0183lib.__addNMEACheckSum(nmeaMessage))


        return retour
