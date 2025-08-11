# This code is generated using PyUIbuilder: https://pyuibuilder.com

import signal
import sys

# print(sys.executable) # show which Python we are running
# print(sys.path)
import re

from colorist import green, Color
from datetime import datetime, timezone, timedelta

from tools.cHeure import cHeure
from tools.cJour import cJour
from tools.cEphemerides import cEphemerides
from tools.cHeureJour import cHeureJour
from tools.cAstroError import cAstroError
from tools.cAngle import cAngle
from tools.cLatitude import cLatitude
from tools.cLongitude import cLongitude
from tools.cPosition import cPosition

from pprint import pprint

import logging
import logging.config

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("main_txt")




def getPlanete() -> str:
    # Choix de la planete
    allPlanet: list[str] = cEphemerides.getPlaneteNom()
    allPlanet.append("------")
    allPlanet += cEphemerides.getStarNom()
    c = ""
    bFind = False
    astre = ""
    while not bFind:
        msg = f"{Color.YELLOW}Planete ou Etoile (? / al?)- {Color.CYAN}sun{Color.OFF}"
        print(f"{msg:20s}\n->", end="", sep=" ")
        sTxt = input()
        if len(sTxt) == 0:
            astre  = "sun"
            bFind = True
        else:
            # est ce que c'est un nom connu si oui on continu
            for p in allPlanet:
                if p == sTxt:
                    astre = sTxt
                    bFind = True

            # management du '?'
            LastChar = sTxt[-1]
            if LastChar == "?":
                prefix = sTxt[0:-1]
                print(prefix)

                for p in allPlanet:
                    if p.lower().startswith(prefix.lower()):
                        print(f"\t\t- >{p}<")

    print(f"{Color.GREEN}\tPlanete:           {astre}{Color.OFF}", end="\n", sep=" ")
    return astre


def getHauteurAstre() -> cAngle:
    a : cAngle = cAngle()
    bFind: bool = False
    while not bFind:
        msg = f"{Color.YELLOW}Hauteur astre [angle]{Color.YELLOW}"
        print(f"{msg:20s}\n->", end="", sep=" ")
        sTxt = input()
        try:
            a = a.parse(sTxt)
            bFind = True
        except Exception as e:
            print (f"{Color.RED}{e.args}{Color.OFF}")

    print(f"{Color.GREEN}\tHauteur astre:     {a.toString()}{Color.OFF}", end="\n", sep=" ")
    return a


def getCollimation() -> cAngle:
    a : cAngle = cAngle()
    bFind: bool = False
    while not bFind:
        msg = f"{Color.YELLOW}Collimation [angle]- {Color.CYAN}0°00'{Color.OFF}"
        print(f"{msg:20s}\n->", end="", sep=" ")
        sTxt = input()
        if len(sTxt) == 0:
            collimation = 0.0
            bFind = True
        else:
            try:
                collimation = a.parse(sTxt).asDeg()
                bFind = True
            except Exception as e:
                print (f"{Color.RED}{e.args}{Color.OFF}")

    print(f"{Color.GREEN}\tCollimation:       {a.toString()}{Color.OFF}", end="\n", sep=" ")
    return a

def getVisee() -> str:
    # Choix de la planete
    allViseeInfo: list[dict[str,float]] = cEphemerides.getViseeInfo()
    visee: str = cEphemerides.getViseeDefaut()
    bFind = False
    while not bFind:
        msg = f"{Color.YELLOW}Visée - {Color.CYAN}{visee}{Color.OFF}"
        print(f"{msg:20s}\n->", end="", sep=" ")
        sTxt = input()

        if len(sTxt) == 0:
            visee = cEphemerides.getViseeDefaut()
            bFind = True

        else:
            # est ce que c'est un nom connu si oui on continu
            for i, p in enumerate(allViseeInfo):
                if p["nom"].startswith(sTxt):
                    visee =p["nom"]
                    bFind = True

            # management du '?'
            LastChar = sTxt[-1]
            if LastChar == "?":
                prefix = sTxt[0:-1]
                print(prefix)

                for p in allViseeInfo:
                    if p["nom"].lower().startswith(prefix.lower()):
                        print(f"\t\t- >{p["nom"]}<")

    print(f"{Color.GREEN}\tVisée:              {visee}{Color.OFF}", end="\n", sep=" ")
    return visee


def getHauteurOeil() -> float:
    # Hauteur du soleil
    hauteurOeilEnMetre: float = 0.0

    bFind: bool = False
    while not bFind:
        msg = f"{Color.YELLOW}Hauteur oeil (m) - {Color.CYAN}2m{Color.OFF}"
        print(f"{msg:20s}\n->", end="", sep=" ")
        sTxt = input()
        if len(sTxt) == 0:
            hauteurOeilEnMetre = 2.0
            bFind = True
        else:
            try:
                hauteurOeilEnMetre = float(sTxt)
                bFind = True
            except Exception as e:
                print (f"{Color.RED}{e.args}{Color.OFF}")

    print(f"{Color.GREEN}\tHauteur oeil (m):   {hauteurOeilEnMetre}{Color.OFF}", end="\n", sep=" ")
    return hauteurOeilEnMetre


def __getHoraireMeusure_jour() -> datetime:
    retour: datetime = datetime(1, 1, 1, 0, 0, 0, 0, timezone.utc)
    now: datetime = datetime.now(timezone.utc)

    bFind: bool = False
    while not bFind:
        msg = f"{Color.YELLOW}Jour: [yyyy/mm/dd] - {Color.CYAN}{now.year:04d}/{now.month:02d}/{now.day:02d}{Color.OFF}"
        print(f"{msg:20s}\n->", end="", sep=" ")
        jour = input()
        if len(jour) == 0:
            retour = retour.replace(year=now.year, month=now.month, day=now.day)
            bFind = True
        else:
            try:
                j : cJour = cJour()
                ts = j.parse(jour).val()
                retour = datetime.fromtimestamp(ts, timezone.utc)
                bFind = True
            except cAstroError as e:
                print (f"{Color.RED}{e.args}{Color.OFF}")

    return retour


def __getHoraireMeusure_heure(jour: datetime) -> None:
    # ---------------------------------
    # on chope le fuseau horaire
    # ---------------------------------
    bFind: bool = False
    decaleUTC = 0
    bFind = False
    while not bFind:
        msg = f"{Color.YELLOW}Fuseau horaire: [+2 pour UTC+2 (été)] - {Color.CYAN}0 (UTC){Color.OFF}"
        print(f"{msg:20s}\n->", end="", sep=" ")
        sTxt = input()
        if len(sTxt) == 0:
            decaleUTC = 0
            bFind = True
        else:
            try:
                decaleUTC = -1.0 * cHeure.parseFuseau(sTxt) * 3600.0
                bFind = True
            except cAstroError as e:
                print (f"{Color.RED}{e.args}{Color.OFF}")
    print(f"{Color.GREEN}\tFuseau:             {(decaleUTC/-3600.0):3.1f}{Color.OFF}", end="\n", sep=" ")

    # ---------------------------------
    # on chope l'heure de demarrage du chrono
    # ---------------------------------
    h : cHeure = cHeure()
    startChronoInSecondes = 0
    bFind = False
    while not bFind:
        msg = f"{Color.YELLOW}Heure start chrono: [hh:mm:ss.ss]{Color.OFF}"
        print(f"{msg:20s}\n->", end="", sep=" ")
        sTxt = input()
        try:
            startChronoInSecondes = h.parse(sTxt).val()
            bFind = True
        except Exception as e:
            print (f"{Color.RED}{e.args}{Color.OFF}")
    print(f"{Color.GREEN}\tHeure:              {h.toString()}{Color.OFF}", end="\n", sep=" ")

    # ---------------------------------
    # on chope le chrono
    # ---------------------------------
    chronoInSecondes = 0
    bFind = False
    while not bFind:
        msg = f"{Color.YELLOW}Valeur chronos: [m+:ss.ss] - {Color.CYAN}0{Color.OFF}"
        print(f"{msg:20s}\n->", end="", sep=" ")
        sTxt = input()
        if len(sTxt) == 0:
            chronoInSecondes = 0
            bFind = True
        else:
            try:
                chronoInSecondes = cHeureJour.parseChrono2Seconde(sTxt)
                bFind = True
            except cAstroError as e:
                print (f"{Color.RED}{e.args}{Color.OFF}")
    print(f"{Color.GREEN}\tChrono (s):           {chronoInSecondes}{Color.OFF}", end="\n", sep=" ")

    decalageTotalInSeconde = chronoInSecondes + startChronoInSecondes + decaleUTC

    jour = jour + timedelta(milliseconds=(decalageTotalInSeconde * 1000.0))
    return jour


def getHoraireMeusure() -> datetime:
    retour: datetime = __getHoraireMeusure_jour()
    print(f"{Color.GREEN}\tJour:              {retour}{Color.OFF}", end="\n", sep=" ")

    retour = __getHoraireMeusure_heure(retour)
    print(f"{Color.GREEN}\t ==>Jour + heure:      {retour}{Color.OFF}", end="\n", sep=" ")
    return retour

def __getDRPosition_latitude() -> cLatitude:
    a : cLatitude = cLatitude()
    bFind: bool = False
    while not bFind:
        msg = f"{Color.YELLOW}DR Lat. [Latitude]"
        print(f"{msg:20s}\n->", end="", sep=" ")
        sTxt = input()
        try:
            a = a.parse(sTxt)
            bFind = True
        except Exception as e:
            print (f"{Color.RED}{e.args}{Color.OFF}")

    print(f"{Color.GREEN}\tDR Latitude:     {a.toString()}{Color.OFF}", end="\n", sep=" ")
    return a

def __getDRPosition_longitude() -> cLongitude:
    a : cLongitude = cLongitude()
    bFind: bool = False
    while not bFind:
        msg = f"{Color.YELLOW}DR Long. [cLongitude]{Color.OFF}"
        print(f"{msg:20s}\n->", end="", sep=" ")
        sTxt = input()
        try:
            a = a.parse(sTxt)
            bFind = True
        except Exception as e:
            print (f"{Color.RED}{e.args}{Color.OFF}")

    print(f"{Color.GREEN}\tDR Longitude:     {a.toString()}{Color.OFF}", end="\n", sep=" ")
    return a

def getDRPosition() -> cPosition:
    retour: cPosition = cPosition()
    a : cLatitude = __getDRPosition_latitude() 
    retour.lat (a)
    b : cLongitude = __getDRPosition_longitude() 
    retour.long (b)

    print(f"{Color.GREEN}\t ==>DR:                {retour.toString()}{Color.OFF}", end="\n", sep=" ")
    return retour


def start():
    print (f"{Color.MAGENTA}------------------------------------------------------------------------{Color.OFF}")
    print (f"{Color.MAGENTA}-- Info --{Color.OFF}")
    print (f"{Color.MAGENTA}------------------------------------------------------------------------{Color.OFF}")
    print (f"{Color.MAGENTA}-- les angles sont au format (xxx.xx°) ou (xxx°yy.yyy' min base 60) ou (xxx°yy'zz.zzz\"){Color.OFF}")
    print (f"{Color.MAGENTA}-- les latitude sont au format N <angle>{Color.OFF}")
    print (f"{Color.MAGENTA}-- les longitude sont au format W <angle>{Color.OFF}")
    print (f"{Color.MAGENTA}-- les heures sont au format hhh:mm:ss{Color.OFF}")
    print (f"{Color.MAGENTA}-- ")
    print (f"{Color.MAGENTA}-- les questions sont en {Color.YELLOW}JAUNE{Color.OFF}")
    print (f"{Color.MAGENTA}-- les valeurs par defaut sont en {Color.CYAN}CYAN - elles sont obtenu par <Enter>{Color.OFF}")
    print (f"{Color.MAGENTA}-- les liste de valeurs '?' : {Color.OFF}")
    print (f"{Color.MAGENTA}--               {Color.CYAN}-  '?' : toutes {Color.OFF}")
    print (f"{Color.MAGENTA}--               {Color.CYAN}-  'aa?' : toutes qui commencent par aa{Color.OFF}")
    print (f"{Color.MAGENTA}-- ")
    print (f"{Color.MAGENTA}-- la valeur prises en compte dans les calculs {Color.GREEN}VERTE{Color.OFF}")
    print (f"{Color.MAGENTA}-- ")
    print (f"{Color.MAGENTA}-- Erreurs ou suggestions {Color.RED}ROUGE{Color.OFF}")
    print (f"{Color.MAGENTA}------------------------------------------------------------------------{Color.OFF}")
    print ("\n\n")


    t: datetime = getHoraireMeusure()
    dr: cPosition = getDRPosition()
    hauteurOeilEnMetre: float = getHauteurOeil()
    collimation: cAngle = getCollimation()
    astre = getPlanete()
    visee: int = getVisee()
    hauteurAstre: cAngle = getHauteurAstre()


    print ("------------------------------------------------------------------------")
    print ("-- Resume --")
    print ("------------------------------------------------------------------------")
    print (f"-- heure:              {t}")
    print (f"-- collimation:        {collimation.toString()}")
    print (f"-- hauteurOeilEnMetre: {hauteurOeilEnMetre}")
    print (f"-- Position:           {dr.toString()}")
    print (f"-- astre:              {astre}")
    print (f"-- visee:              {visee}")
    print (f"-- hauteurAstreDeg:    {hauteurAstre.toString()}")


    print ("\n\n\n")
    x : cEphemerides = cEphemerides()
    xx = x.getEpherideAstre(t, astre, dr)
    pprint.pp(xx)

    print ("------------------------------------------------------------------------")
    print ("-- calcul --")
    print ("------------------------------------------------------------------------")
    print (f"-- GHA:              {t}")
    print (f"-- LHA:              {astre}")


def signal_handler(sig, frame):
    print(".... je sors !")
    sys.exit(0)


if __name__ == "__main__":
    signal.signal(signal.SIGINT, signal_handler)
    start()
