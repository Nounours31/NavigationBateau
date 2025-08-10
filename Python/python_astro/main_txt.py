# This code is generated using PyUIbuilder: https://pyuibuilder.com

import signal
import sys

# print(sys.executable) # show which Python we are running
# print(sys.path)
import re
from tools.cAngle import cAngle

from datetime import datetime, timezone, timedelta
from tools.cEphemerides import cEphemerides
from colorist import green, Color

from tools.cAngle import cAngle
from tools.cAstroError import cAstroError

import logging
import logging.config
logging.config.fileConfig("logging.conf")
logger = logging.getLogger("main_txt")


__BORD_SUP: float = -0.5
__BORD_INF: float = 0.5
__BORD_MILIEU: float = 0


def getPlanete() -> str:
    # Choix de la planete
    allPlanet: list[str] = cEphemerides.getPlaneteStarNom()
    c = ""
    bFind = False
    astre = ""
    while not bFind:
        print(
            f"{Color.YELLOW}Planete ou Etoile (?: liste - ab? : liste commancant par ab [default: sun]){Color.OFF}",
            end="\n",
            sep=" ",
        )
        print("          (enter pour defaut) ->", end="", sep=" ")
        sTxt = input()
        logger.debug (f"planete = {sTxt}")

        if sTxt == '':
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

    green(f"\t{Color.GREEN}Planete: {astre}{Color.OFF}")
    return astre


def hauteurSoleil() -> float:
    # Hauteur du soleil
    hauteurAstreDeg: float = 0.0
    bFind = False
    while not bFind:
        msg = "Hauteur soleil (10°59.99) : "
        print(f"{msg:20s}", end="", sep=" ")
        hauteurAstre = input()

        try:
            hauteurAstreDeg = cAngle.parse (hauteurAstre)
        except cAstroError as e:
            print (f"Unable to parse angle {repr(e)}")

    print(
        f"\t{Color.MAGENTA}Hauteur soleil: {Color.GREEN}{cAngle.toStringDebug(hauteurAstreDeg)}{Color.OFF}"
    )
    print("\t")
    return hauteurAstreDeg

def getCollimation() -> float:
    # Hauteur du soleil
    collimation: float = 0.0
    bFind = False
    while not bFind:
        msg = "collimation (0°09.99) : "
        print(f"{msg:20s}", end="", sep=" ")
        sCollimation = input()

        regexp = r"(\d{1,3})°((\d{2}(\.\d+)?)'?)?"
        matches = re.finditer(regexp, sCollimation, re.NOFLAG)
        for matchNum, match in enumerate(matches, start=1):
            for groupNum in range(0, len(match.groups())):
                groupNum = groupNum + 1

                if groupNum == 1:
                    hauteurAstreDeg += float(match.group(groupNum))
                    bFind = True

                if groupNum == 3:
                    decimale = float(match.group(groupNum))
                    if decimale >= 60.0:
                        print("Minutes invalide")
                        bFind = False
                    else:
                        hauteurAstreDeg += (float(match.group(groupNum))) / 60.0
                        bFind = True

    print(
        f"\t{Color.MAGENTA}Hauteur soleil: {Color.GREEN}{cAngle.toStringDebug(hauteurAstreDeg)}{Color.OFF}"
    )
    print("\t")
    return hauteurAstreDeg

def getVisee() -> int:
    # Choix de la planete
    allPlanet: list[str] = ["sup [Bord sup]", "milieu", "inf [Bord inf]"]
    allPlanetAsInt: list[float] = [__BORD_SUP, __BORD_MILIEU, __BORD_INF]
    c = ""
    bFind = False
    astre = ""
    visee = __BORD_SUP
    while not bFind:
        msg = "Visée (?: liste - ab? : liste commancant par ab)"
        print(f"{msg:20s}", end="\n", sep=" ")
        c = input()
        print(f">{c}<")

        # est ce que c'est un nom connu si oui on continu
        for i, p in enumerate(allPlanet):
            if p.startswith(c):
                astre = c
                visee = allPlanetAsInt[i]
                bFind = True

        # management du '?'
        LastChar = c[-1]
        if LastChar == "?":
            prefix = c[0:-1]
            print(prefix)

            for p in allPlanet:
                if p.lower().startswith(prefix.lower()):
                    print(f"\t\t- >{p}<")

    green(f"\t{Color.MAGENTA}Visée: {Color.GREEN}{astre} - {visee}{Color.OFF}")
    return visee


def hauteurOeil() -> float:
    # Hauteur du soleil
    hauteurOeilEnMetre: float = 0.0

    msg = "Hauteur oeil en metre : "
    print(f"{msg:20s}", end="", sep=" ")
    x = input()
    hauteurOeilEnMetre = float(x)

    print(f"\t{Color.MAGENTA}Hauteur oeil (en m): {Color.GREEN}hauteurOeilEnMetre{Color.OFF}")
    return hauteurOeilEnMetre


def getHoraireMeusure_getDate() -> datetime:
    retour: datetime = datetime(1, 1, 1, 0, 0, 0, 0, timezone.utc)
    now: datetime = datetime.now(timezone.utc)
    bFind: bool = False
    iRegExpOk: int = 0
    while not bFind:
        bFind = True
        iRegExpOk = 0
        msg = f"{Color.YELLOW}Jour: [format= yyyy/mm/dd] - {Color.CYAN}defaut : {now.year:04d}/{now.month:02d}/{now.day:02d}{Color.OFF}"
        print(f"{msg:20s}", end="\n", sep=" ")
        print(f"{'          (enter pour defaut) ->':10s}", end="", sep=" ")
        jour = input()
        if len(jour) == 0:
            retour = retour.replace(year=now.year, month=now.month, day=now.day)
        else:
            regexp = r"(\d{4})/(\d{2})/(\d{2})"
            matches = re.finditer(regexp, jour, re.NOFLAG)
            for matchNum, match in enumerate(matches, start=1):
                for groupNum in range(0, len(match.groups())):
                    groupNum = groupNum + 1

                    if groupNum == 1:
                        year = int(match.group(groupNum))
                        iRegExpOk += 1
                    if groupNum == 2:
                        month = int(match.group(groupNum))
                        iRegExpOk += 1
                        if month > 12 or month < 1:
                            bFind = False
                    if groupNum == 3:
                        day = int(match.group(groupNum))
                        iRegExpOk += 1
                        if day > 31 or day < 1:
                            bFind = False
            if iRegExpOk != 3:
                bFind = False

            if iRegExpOk == 3 and bFind:
                retour = retour.replace(year=year, month=month, day=day)
            else:
                print(f"{Color.RED} ******************************************************")
                print(f"{Color.RED} ********* >>> Date incorrecte >>{jour:s}<< - format yyyy/mm/dd")
                print(f"{Color.RED} ********* >>>              1 <= yyyy  <= ????")
                print(f"{Color.RED} ********* >>>              1 <= mm    <= 12")
                print(f"{Color.RED} ********* >>>              1 <= dd    <= 31")
                print(f"{Color.RED} ******************************************************")
    return retour


def getHoraireMeusure_getHeure(retour: datetime) -> None:
    heure, min, seconde = 0, 0, 0
    dMinute : int = 0
    dSeconde : int = 0
    decaleUTC : int = 0

    bFind: bool = False
    iRegexpFoud: int = 0
    while not bFind:
        iRegexpFoud = 0
        bFind = True
        regexp = r"(\d{2}):(\d{2}):(\d{2})"

        print(
            f"{Color.YELLOW}Heure de ref: [heure de demarrage du chronos - format 'hh:mm:ss']{Color.OFF}",
            end="\n",
            sep=" ",
        )
        print(f"{'                              ->':10s}", end="", sep=" ")
        sTxt = input()
        logger.debug (f"heure saisir: {sTxt}")
        matches = re.finditer(regexp, sTxt, re.NOFLAG)
        for matchNum, match in enumerate(matches, start=1):
            for groupNum in range(0, len(match.groups())):
                groupNum = groupNum + 1

                if groupNum == 1:
                    heure = int(match.group(groupNum))
                    logger.debug (f"group 1: heure = {heure}")
                    iRegexpFoud += 1
                    if heure > 23 or heure < 0:
                        bFind = False
                        heure = 0

                if groupNum == 2:
                    min = int(match.group(groupNum))
                    logger.debug (f"group 2: minute = {min}")
                    iRegexpFoud += 1
                    if min > 59 or min < 0:
                        bFind = False
                        min = 0

                if groupNum == 3:
                    seconde = int(match.group(groupNum))
                    logger.debug (f"group 3: seconde = {seconde}")
                    iRegexpFoud += 1
                    if seconde > 59 or seconde < 0:
                        bFind = False
                        seconde = 0

        if iRegexpFoud != 3:
            bFind = False

        if not bFind:
            print(f"{Color.RED} ******************************************************")
            print(f"{Color.RED} ********* >>> heure incorrecte >>{sTxt:s}<< - format correct hh:mm:ss")
            print(f"{Color.RED} ********* >>>              0 <= hh < 24")
            print(f"{Color.RED} ********* >>>              0 <= mm < 60")
            print(f"{Color.RED} ********* >>>              0 <= ss < 60")
            print(f"{Color.RED} ******************************************************")

    bFind = False
    iRegexpFoud = 0
    while not bFind:
        iRegexpFoud = 0
        bFind = True
        regexp = r"(\d+):(\d{2})"

        print(
            f"{Color.YELLOW}Variation du chronos: [valeur du chronos - format 'm+:ss']{Color.OFF}",
            end="\n",
            sep=" ",
        )
        print(f"{'                              ->':10s}", end="", sep=" ")
        sTxt = input()
        logger.debug (f"Chronis = {sTxt}")

        matches = re.finditer(regexp, sTxt, re.NOFLAG)
        for matchNum, match in enumerate(matches, start=1):
            for groupNum in range(0, len(match.groups())):
                groupNum = groupNum + 1

                if groupNum == 1:
                    dMinute = int(match.group(groupNum))
                    logger.debug (f"Chronos dMinute = {dMinute}")
                    iRegexpFoud += 1
                    if dMinute < 0:
                        bFind = False
                        dMinute = 0

                if groupNum == 2:
                    dSeconde = int(match.group(groupNum))
                    logger.debug (f"Chronos dSeconde = {dSeconde}")
                    iRegexpFoud += 1
                    if dSeconde > 59 or dSeconde < 0:
                        bFind = False
                        dSeconde = 0

        if iRegexpFoud != 2:
            bFind = False

        if not bFind:
            print(f"{Color.RED} ******************************************************")
            print(f"{Color.RED} ********* >>> decalage chronos KO >>{sTxt:s}<< - format m+:ss")
            print(f"{Color.RED} ********* >>>              0 <= mm < ....")
            print(f"{Color.RED} ********* >>>              0 <= ss < 60")
            print(f"{Color.RED} ******************************************************")

        logger.debug(f"{Color.GREEN} Chronos: {dMinute}:{dSeconde}")

    bFind = False
    iRegexpFoud = 0
    while not bFind:
        iRegexpFoud = 0
        bFind = True
        regexp = r"([\+\-])?\s*(\d{1,2})"

        print(
            f"{Color.YELLOW}Variation UTC: [decalage heure montre VS UTC (ex montre été = UTC+2: donner +2) - format '+/-:h' - defaut 0[=UTC]]{Color.OFF}",
            end="\n",
            sep=" ",
        )
        print(f"{'                              ->':10s}", end="", sep=" ")
        sTxt = input()
        logger.debug (f"UTC = {sTxt}")

        if len(sTxt) == 0:
            decaleUTC = 0
            iRegexpFoud = 1
            bFind = True
        else:
            matches = re.finditer(regexp, sTxt, re.NOFLAG)
            for matchNum, match in enumerate(matches, start=1):
                for groupNum in range(0, len(match.groups())):
                    groupNum = groupNum + 1

                    if groupNum == 1:
                        signe = -1 if match.group(groupNum) == "-" else +1
                        logger.debug (f"UTC txt [{match.group(groupNum)}] signe = {signe}")

                    if groupNum == 2:
                        decaleUTC = int(match.group(groupNum))
                        logger.debug (f"UTC decaleUTC = {decaleUTC}")
                        iRegexpFoud += 1
                        if decaleUTC > 12:
                            bFind = False
                        else:
                            decaleUTC = signe * decaleUTC

        if iRegexpFoud != 1:
            bFind = False

        if not bFind:
            print(f"{Color.RED} ******************************************************")
            print(f"{Color.RED} ********* >>> decalage  - format +/-h")
            print(f"{Color.RED} ********* >>>              0 <= h <= 12")
            print(f"{Color.RED} ******************************************************")

    logger.debug(f"     {Color.GREEN} Decalage: {decaleUTC}")

    retour = retour.replace(hour=heure, minute=min, second=seconde)

    decaleUTC = -1 * decaleUTC
    retour = retour + timedelta(hours=decaleUTC, minutes=dMinute, seconds=dSeconde)
    print(f"\t\t\t --- {Color.CYAN}heure:     {heure:02d}:{min:02d}:{seconde:02d}")
    print(f"\t\t\t --- {Color.CYAN}chrono:    00:{dMinute:02d}:{dSeconde:02d}")
    print(f"\t\t\t --- {Color.CYAN}vers UTC: {decaleUTC:03d}:00:00")
    print(f"\t\t\t --- {Color.CYAN}retenue:   {retour.hour:02d}:{retour.minute:02d}:{retour.second:02d}")
    return retour


def getHoraireMeusure() -> datetime:
    retour: datetime = getHoraireMeusure_getDate()

    retour = getHoraireMeusure_getHeure(retour)
    print(f"{Color.GREEN}", f"     Jour: {retour}", f"{Color.OFF}", end="\n", sep=" ")
    return retour


def start():
    # Jour
    t: datetime = getHoraireMeusure()
    print (t)

    # Choix de la planete
    astre = getPlanete()
    print (astre)

    # type de la visee
    visee: int = getVisee()
    print (visee)

    # Hauteur du soleil
    hauteurAstreDeg: float = hauteurSoleil()
    print (hauteurAstreDeg)

    collimation: float = getCollimation()
    print (collimation)
    
    # Hauteur du soleil
    hauteurOeilEnMetre: float = hauteurOeil()
    print (hauteurOeilEnMetre)


    print ("------------------------------------------------------------------------")
    print ("-- Resume --")
    print ("------------------------------------------------------------------------")
    print (f"-- heure:              {t}")
    print (f"-- astre:              {astre}")
    print (f"-- visee:              {visee}")
    print (f"-- hauteurAstreDeg:    {hauteurAstreDeg}")
    print (f"-- hauteurOeilEnMetre: {hauteurOeilEnMetre}")
    print (f"-- collimation:        {collimation}")

def signal_handler(sig, frame):
    print(".... je sors !")
    sys.exit(0)


if __name__ == "__main__":
    signal.signal(signal.SIGINT, signal_handler)
    start()
