# This code is generated using PyUIbuilder: https://pyuibuilder.com

import signal
import sys
import re
from tools.cAngle import cAngle

from tools.cEphemerides import cEphemerides
from colorist import green, yellow, red, Color

__BORD_SUP : float = -0.5
__BORD_INF : float = 0.5
__BORD_MILIEU : float = 0

def getPlanete() -> str :
    # Choix de la planete
    allPlanet : list[str] = cEphemerides.getPlaneteStarNom()
    c = ""
    bFind = False
    astre = ""
    while not bFind :
        msg = "Planete ou Etoile (?: liste - ab? : liste commancant par ab)"
        print (f"{msg:20s}", end='\n', sep=' ')
        c = input()
        print(f">{c}<")

        # est ce que c'est un nom connu si oui on continu
        for p in allPlanet:
            if p == c :
                astre = c
                bFind = True

        # management du '?'
        LastChar = c[-1]
        if LastChar == "?" :
            prefix = c[0:-1]
            print (prefix) 

            for p in allPlanet:
                if p.lower().startswith(prefix.lower()) :
                    print (f"\t\t- >{p}<")
    
    green (f"\t{Color.MAGENTA}Planete: {Color.GREEN}{astre}{Color.OFF}")
    return astre

def hauteurSoleil() -> float :
    # Hauteur du soleil
    hauteurAstreDeg : float = 0.0
    bFind = False
    while not bFind:
        msg = "Hauteur soleil (10°59.99) : "
        print (f"{msg:20s}", end='', sep=' ')
        hauteurAstre = input()
        
        regexp = r"(\d{1,3})°((\d{2}(\.\d+)?)'?)?"
        matches = re.finditer(regexp, hauteurAstre, re.NOFLAG)
        for matchNum, match in enumerate(matches, start=1):
            for groupNum in range(0, len(match.groups())):
                groupNum = groupNum + 1            

                if groupNum == 1:
                    hauteurAstreDeg += float(match.group(groupNum))
                    bFind = True

                if groupNum == 3:
                    decimale = (float(match.group(groupNum)))
                    if decimale >= 60.0:
                        print ("Minutes invalide")
                        bFind = False
                    else:    
                        hauteurAstreDeg += (float(match.group(groupNum))) / 60.0
                        bFind = True
    
    print (f"\t{Color.MAGENTA}Hauteur soleil: {Color.GREEN}{cAngle.toStringDebug(hauteurAstreDeg)}{Color.OFF}")
    print (f"\t")
    return hauteurAstreDeg


def getVisee() -> int :
    # Choix de la planete
    allPlanet : list[str] = ['sup [Bord sup]', 'milieu', 'inf [Bord inf]']
    allPlanetAsInt : list[float] = [__BORD_SUP, __BORD_MILIEU, __BORD_INF]
    c = ""
    bFind = False
    astre = ""
    visee = __BORD_SUP
    while not bFind :
        msg = "Visée (?: liste - ab? : liste commancant par ab)"
        print (f"{msg:20s}", end='\n', sep=' ')
        c = input()
        print(f">{c}<")

        # est ce que c'est un nom connu si oui on continu
        for i, p in enumerate(allPlanet):
            if p.startswith(c) :
                astre = c
                visee = allPlanetAsInt[i]
                bFind = True

        # management du '?'
        LastChar = c[-1]
        if LastChar == "?" :
            prefix = c[0:-1]
            print (prefix) 

            for p in allPlanet:
                if p.lower().startswith(prefix.lower()) :
                    print (f"\t\t- >{p}<")
    
    green (f"\t{Color.MAGENTA}Visée: {Color.GREEN}{astre} - {visee}{Color.OFF}")
    return visee

def hauteurOeil() -> float :
    # Hauteur du soleil
    hauteurOeilEnMetre : float = 0.0

    msg = "Hauteur oeil en metre : "
    print (f"{msg:20s}", end='', sep=' ')
    x = input()
    hauteurOeilEnMetre = float(x)

    print (f"\t{Color.MAGENTA}Hauteur oeil (en m): {Color.GREEN}hauteurOeilEnMetre{Color.OFF}")
    return hauteurOeilEnMetre


def start():
    # Jour
    msg = "Jour: (format= yyyy/mm/dd) "
    print (f"{msg:20s}", end='', sep=' ')
    jour = input()

    # Heure
    msg = "Heure: (format= hh:mm:ss) "
    print (f"{msg:20s}", end='', sep=' ')
    heure = input()
    
    # Choix de la planete
    astre = getPlanete()

    # type de la visee
    visee : int = getVisee()

    # Hauteur du soleil
    hauteurAstreDeg : float = hauteurSoleil()

    # Hauteur du soleil
    hauteurOeilEnMetre : float = hauteurOeil()
    


def signal_handler(sig, frame):
    print (".... je sors !")
    sys.exit(0)


if __name__ == '__main__':
    signal.signal(signal.SIGINT, signal_handler)
    start()
