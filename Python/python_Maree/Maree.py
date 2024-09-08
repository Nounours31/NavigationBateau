import os
import sys

import datetime
import pytz

import signal
import argparse
import requests

from io import StringIO  

from lxml import etree
import xml.etree.ElementTree as ET
from urllib.request import urlopen

"""
TODO: loggng
Logging : pbs 
    - fichier de ref en .ini au lieu de yaml
    - fichier absolu au lieu du classpath
"""
import logging
import logging.config
# Initialize the logger once as the application starts up.
logging.config.fileConfig('E:\\data\\git\\NavigationBateau\\Python\\python_Maree\\logging.ini')
 
# Get an instance of the logger and use it to write a log!
# Note: Do this AFTER the config is loaded above or it won't use the config.
logger = logging.getLogger(__name__)
logger.info("Configured the logger!")


# --------------------------------------------------------------
# les constantes
# --------------------------------------------------------------
from typing import Final
TAG_PORT_PP: Final[str] = "Port PP"
TAG_PORT_PS: Final[str] = "Port PS"
TAG_JOUR_MAREE: Final[str] = "MareeJours_"
TAG_UTC_ATTR_MAREE: Final[str] = "title"
TAG_UTC_ENCODE_MAREE: Final[str] = "UTC"

# --------------------------------------------------------------
# liste des ports principaux a partir de fichier telecherge de l'URL
#   https://maree.info/
# --------------------------------------------------------------
def getPortPrincipaux () -> tuple: 
    print ("==== Liste des ports principaux ====")
    retour = {}
    htmlparser = etree.HTMLParser()
    tree = etree.parse("E:\\data\\git\\NavigationBateau\\Python\\python_Maree\\maree.info.ports.html", htmlparser)
    

    for elem in tree.xpath('//*[@id="PortsListe_Content_0"]/table/tr/td'):
        for child in elem:
            if (child.attrib["class"] == TAG_PORT_PP):
                logger.info("{} {} {}".format(child.text, child.attrib["class"], child.attrib["href"]))
                retour[child.text] = {"type": child.attrib["class"], "Id": child.attrib["href"]}
    return retour

# --------------------------------------------------------------
# liste des ports ratachés  a partir de fichier telecherge de l'URL
#   https://maree.info/
# --------------------------------------------------------------
def getPortSecondaire (portPrincipal : str = None, idPortPrincipal: int = None) -> tuple: 
    print ("==== Liste des ports ratachés de {} ====".format(portPrincipal))
    retour = {}
    if (portPrincipal == None) and (idPortPrincipal == None):
        return retour
    
    htmlparser = etree.HTMLParser()
    tree = etree.parse("E:\\data\\git\\NavigationBateau\\Python\\python_Maree\\maree.info.ports.html", htmlparser)
    

    found : bool = False
    isPortPrincipal : bool = False
    isGoodPortPrincipal : bool = False
    for elem in tree.xpath('//*[@id="PortsListe_Content_0"]/table/tr/td'):
        for child in elem:
            
            isPortPrincipal = (child.attrib["class"] == TAG_PORT_PP)
            if (portPrincipal == None):
                isGoodPortPrincipal = (child.attrib["href"] == ("/"+"{:01d}".format(idPortPrincipal)))
            else :
                isGoodPortPrincipal = (child.attrib["class"] == TAG_PORT_PP) and (child.text == portPrincipal)

            if isPortPrincipal and isGoodPortPrincipal:
                found = True
                portPrincipal = child.text

            if isPortPrincipal and not isGoodPortPrincipal:
                found = False

            if (not (found)):
                continue
                
            logger.info("Port principal: {} - Port rataché {} / {} / id:{}".format(portPrincipal, child.text, child.attrib["class"], child.attrib["href"]))
            retour[child.attrib["href"][1:]] = {"NomPP": portPrincipal, "Nom": child.text, "type": child.attrib["class"], "Id": child.attrib["href"]}
    return retour


# --------------------------------------------------------------
# Parse UTC+2 en decalage horaire
# --------------------------------------------------------------
def privateGetUTCZone (utcInfo: str) -> int :
    retour : int = 0
    match utcInfo:
        case "UTC+2":
            retour = 2
        case "UTC+1":
            retour = 1
        case "UTC+0" | "UTC" :
            retour = 0
        case "UTC-1":
            retour = -1
        case _: 
            retour = 0
    return retour


# --------------------------------------------------------------
# conversion de la date au formay 'YearMonthDay' + offset day 
# --------------------------------------------------------------
def getDebut (debut: str, offset: int) -> str: 
    jourdebut: datetime.datetime = datetime.datetime(int(debut[:4]), int(debut[4:6]), int(debut[6:8]), 0, 0, 0, 0, pytz.utc)
    time_change = datetime.timedelta(days=offset) 
    jourdebut = jourdebut + time_change 
    retour : str = "{:04d}{:02d}{:02d}".format(jourdebut.year, jourdebut.month, jourdebut.day)
    return retour



# --------------------------------------------------------------
# parse le contenu de l'URL  "https://maree.info/{idPort:01d}?d={debut}"
# et recupere un tableaiu des infos maree
#   {
#       datetime.datetime(2015, 10, 10, 0, 0, tzinfo=<UTC>): [[hauteurs], [heures], [coef]], 
#       datetime.datetime(2015, 10, 11, 0, 0, tzinfo=<UTC>): [[...], [...], [...]], 
#       ...
#   }
# --------------------------------------------------------------
def private_getPortMareeWS (idPort: int, debut: str) -> requests.Response: 
    url = "https://maree.info/{idPort:01d}?d={debut}".format(idPort=idPort, debut=debut)

    session = requests.Session()
    session.max_redirects = 3
    headers = {
        "Authorization": "Bearer YOUR_ACCESS_TOKEN",
        "Content-Type": "text/html",
        "user-agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36"
    }
    response = session.get(url,headers=headers)
    return response

# --------------------------------------------------------------
# Converti 
#   ["2h32"] -> [02:32:00]
#   ["2.32m"] -> [2.32]
#   ["32", "na"] -> [32, -1]
# --------------------------------------------------------------
def privateConvertionTextVersValeurMaree (iData: list, jour: datetime.datetime, offsetUTC : int = 0) -> list :
    retour = []
    x : list
    i : int = 0
    for x in iData:
        if "h" in x: 
            i = 0
        elif "m" in x:
            i = 1
        else:
            i = 2

        if (i == 0): # ce sont des heures
            st : list = x.split("h")
            if (len(st) == 1):
                st.append("0")
            if (len(st[1]) == 0):
                st[1] = "0"
            
            dt : datetime.timedelta = datetime.timedelta(hours=int(st[0])-offsetUTC, minutes=int(st[1]))
            t : datetime.datetime = jour + dt
            retour.append(t)

        if (i == 1): # ce sont des hauteurs en m
            sh = x.split("m")[0]
            sh = sh.replace(".", ",")
            h : list = sh.split(",")

            h1 : float = float(h[0])
            h2 : float = 0.0
            if len(h) > 1 : 
                h2 = float(h[1])
            while abs (h2) > 1.0:
                h2 = h2 / 10.0
            retour.append(h1 + h2)

        if (i == 2): # ce sont les coefs
            try:
                coef : int = int(x)
            except Exception as e:
                print(e, type(e))
                coef = -1
            retour.append(coef)
    return retour



# --------------------------------------------------------------
# Calcul des hauteurs d'eau / maree
# --------------------------------------------------------------
def getPortMareeUTC (idPort: int, debut: str, duree: int, responseAsText : str = None) -> tuple: 
    htmlparser = etree.HTMLParser()
    if responseAsText == None:
        response = private_getPortMareeWS(idPort, debut)
        tree = etree.parse(StringIO(response.text), htmlparser)
        logger.info(response.text)
    else:
        tree = etree.parse(StringIO(responseAsText), htmlparser)



    isJourDeMaree : bool = False
    elem : ET = None
    for elem in tree.xpath('//*[@id="MareeJours"]'):
        """ 
        <tr class="MJ MJ0 Selected" id="MareeJours_0" title="UTC+1" >
            <th><a href="/8" onmouseover="QSr(this,'?d=201501030');" onclick="this.onmouseover();this.blur();return false;">Sam.<br><b>03</b></a></th>
            <td>05h16<br><b>10h26</b><br>17h47<br><b>22h55</b></td>
            <td>2,15m<br><b>8,80m</b><br>1,85m<br><b>8,80m</b></td>
            <td>&nbsp;<br><b>73</b><br>&nbsp;<br><b>76</b></td>
        </tr>
        """                
        #  <tr class="MJ MJ0 Selected" id="MareeJours_0" title="UTC+1" >
        trChild : ET = None
        allInfosMaree = {}
        for trChild in elem:   
            if trChild.attrib.get("id") == None:
                continue         

            isJourDeMaree = (trChild.attrib["id"].startswith(TAG_JOUR_MAREE))
            if (not isJourDeMaree):
                continue

            cJourOffset = trChild.attrib["id"][len(TAG_JOUR_MAREE):]
            iJourOffset = int(cJourOffset)
            jourdebut: datetime.datetime = datetime.datetime(int(debut[:4]), int(debut[4:6]), int(debut[6:8])+iJourOffset, 0, 0, 0, 0, pytz.utc)
            decalageUTC : int = privateGetUTCZone (trChild.attrib[TAG_UTC_ATTR_MAREE])
           

            # <td>05h16<br><b>10h26</b><br>17h47<br><b>22h55</b></td>
            allInfosMareeDuJour = []
            
            tdchild : ET = None
            for tdchild in trChild.getchildren() :
                allInfosMareeDuJourParType = []
                
                logger.debug(tdchild.tag)
                if tdchild.tag != "td":
                    continue

                if (tdchild.text != None) :
                    logger.debug (tdchild.text)
                    allInfosMareeDuJourParType.append(tdchild.text)
                elif (tdchild.tail != None) :
                    logger.debug (tdchild.tail)
                    allInfosMareeDuJourParType.append(tdchild.tail)
                
                
                for xxx in tdchild.getchildren() :
                    logger.debug(xxx.tag)
                    if (xxx.text != None) :
                        logger.debug (xxx.text)
                        allInfosMareeDuJourParType.append(xxx.text)
                    elif (xxx.tail != None) :
                        logger.debug (xxx.tail)
                        allInfosMareeDuJourParType.append(xxx.tail)

                allInfosMareeDuJourParType = privateConvertionTextVersValeurMaree(allInfosMareeDuJourParType, jourdebut, decalageUTC)
                allInfosMareeDuJour.append(allInfosMareeDuJourParType)
            allInfosMaree[jourdebut] = allInfosMareeDuJour

    if (len(allInfosMaree) < duree) : 
        duree = duree - 7
        debut = getDebut (debut, +7)

        nextAllInfosMaree = getPortMareeUTC (idPort, debut, duree) 
        allInfosMaree.update (nextAllInfosMaree) 

    logger.info (allInfosMaree)
    return allInfosMaree


# --------------------------------------------------------------
# maree au format CSV pour XLS
# Jour;	        Heure;	Coef;	Hauteur (m);
# 2024/8/24;	11:06;	94;	    10,82
# 2024/8/24;	17:33;	;	    1,69
# --------------------------------------------------------------
def convertTupleForXLS(x : tuple, filePath : str = ".\\toto.csv") -> None:
    with open(filePath, "wt") as f:
        f.write("Jour;Heure;Coef;Hauteur (m)\n")
        for uneMaree in x:
            logger.info (uneMaree)
            y : list = x[uneMaree]
            if len(y) != 3:
                continue

            iSize : int = len(y[0])
            i : int = 0
            while i < iSize:
                t : datetime.datetime = y[0][i]
                h : float = y[1][i]
                sh : str = "{:05.2f}".format(h)
                sh = sh.replace(".",",")
                f.write("{};{};{};{}\n".format(t.strftime("%Y/%m/%d"), t.strftime("%H:%M"), "{:01d}".format(y[2][i]) if y[2][i] > 0 else "", sh))
                i += 1
        f.close
    return


# --------------------------------------------------------------
# gestion du CtrlC / pas de coverage
# --------------------------------------------------------------
def exit_gracefully(signum, frame):
    # restore the original signal handler as otherwise evil things will happen
    # in raw_input when CTRL+C is pressed, and our signal handler is not re-entrant
    signal.signal(signal.SIGINT, original_sigint)

    try:
        if input("\nReally quit? (y/n)> ").lower().startswith('y'):
            sys.exit(1)

    except KeyboardInterrupt:
        print("Ok ok, quitting")
        sys.exit(1)

    # restore the exit gracefully handler here    
    signal.signal(signal.SIGINT, exit_gracefully)

# --------------------------------------------------------------
# Le main 
# --------------------------------------------------------------
if __name__ == '__main__':
    logger.info ("Start ...")
    global original_sigint
    original_sigint = signal.getsignal(signal.SIGINT)
    signal.signal(signal.SIGINT, exit_gracefully)

    parser = argparse.ArgumentParser()
    parser.add_argument(        '--cgi',        action='store_true',                                                help='run as CGI server')
    parser.add_argument('-b',   '--bind',       metavar='ADDRESS',                                                  help='bind to this address (default: all interfaces)')
    parser.add_argument('-d',   '--directory',                      default=os.getcwd(),                            help='serve this directory (default: current directory)')
    parser.add_argument('-p',   '--protocol',   metavar='VERSION',  default='HTTP/1.0',                             help='conform to this HTTP version (default: %(default)s)')
    parser.add_argument(        'port',                             default=8000,           type=int, nargs='?',    help='bind to this port (default: %(default)s)')
    args = parser.parse_args()

    getPortPrincipaux()
    getPortSecondaire(None, 52)
    depuis = "20151003" 
    dureeEnJour = 12
    x: tuple = getPortMareeUTC (52, depuis, dureeEnJour)
    s: str = convertTupleForXLS(x, "E:\\data\\git\\NavigationBateau\\Python\\python_Maree\\maree.csv")

    logger.info ("The end ...")
