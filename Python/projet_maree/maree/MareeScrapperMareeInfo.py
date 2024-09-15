from maree import MareeScrapper

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

import logging
import logging.config
import json

loggingInitPath = os.path.join(os.path.dirname(__file__), ".\\logging.json")
with open(loggingInitPath, 'r') as f:
    CONFIG = f.read()
logging.config.dictConfig(json.loads(CONFIG))
logger = logging.getLogger("MareeScrapperMareeInfo")
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


class MareeScrapperMareeInfo (MareeScrapper.MareeScrapper):
    def __init__(self) -> None:
        super().__init__()
        self.type = "Shom"

    def __str__(self) -> str:
        return self.type

    def getType(self) -> str:
        return "MareeInfo"
    
    def getListPort(self) -> list:
        portPrincipaux : tuple = self.__getPortPrincipaux()
        for p in portPrincipaux:
            logger.debug("recherche port secondaire de " + p)
            self.__getPortSecondaire (p)

    def getPortMaree(self, port : str, dateDebut : str, dureeEnjour: int, outputName: str, content4Odt: str = None) -> list:
        portId : int = -1
        try:
            portId = int(port)
        except Exception as e:
            logger.critical ("Code de port incorrect " + port)
            logger.critical (e)
            portId = -1

        x: tuple = self.__getPortMareeUTC (portId, dateDebut, dureeEnjour, content4Odt)

        outputFile : str = outputName
        if not os.path.exists (outputFile): 
            outputFileDir = os.path.dirname(outputFile)
            if not os.path.exists (outputFileDir):
                os.makedirs(name=outputFileDir, mode=777)

        s: str = self.__convertTupleForXLS(x, outputFile)
        with open(outputFile) as f:
            print (f.read())
        return x


    # --------------------------------------------------------------
    # liste des ports principaux a partir de fichier telecherge de l'URL
    #   https://maree.info/
    # --------------------------------------------------------------
    def __getPortPrincipaux (self) -> tuple: 
        print ("==== Liste des ports principaux ====")
        retour = {}
        htmlparser = etree.HTMLParser()
        tree = etree.parse(os.path.join(os.path.dirname(__file__), ".\\maree.info.ports.html"), htmlparser)
        

        for elem in tree.xpath('//*[@id="PortsListe_Content_0"]/table/tr/td'):
            for child in elem:
                if (child.attrib["class"] == TAG_PORT_PP):
                    logger.debug("{} {} {}".format(child.text, child.attrib["class"], child.attrib["href"]))
                    print("Port principal: {:30s}  / id:{}".format(child.text, child.attrib["href"][1:]))
                    retour[child.text] = {"type": child.attrib["class"], "Id": child.attrib["href"]}
        return retour

    # --------------------------------------------------------------
    # liste des ports ratachés  a partir de fichier telecherge de l'URL
    #   https://maree.info/
    # --------------------------------------------------------------
    def __getPortSecondaire (self, portPrincipal : str) -> tuple: 
        print ("==== Liste des ports ratachés de {} ====".format(portPrincipal))
        retour = {}
        if (portPrincipal == None):
            return retour
        
        htmlparser = etree.HTMLParser()
        tree = etree.parse(os.path.join(os.path.dirname(__file__),".\\maree.info.ports.html"), htmlparser)
        
        found : bool = False
        isPortPrincipal : bool = False
        isGoodPortPrincipal : bool = False
        for elem in tree.xpath('//*[@id="PortsListe_Content_0"]/table/tr/td'):
            for child in elem:
                
                isPortPrincipal = (child.attrib["class"] == TAG_PORT_PP)
                isGoodPortPrincipal = (child.attrib["class"] == TAG_PORT_PP) and (child.text == portPrincipal)

                if isPortPrincipal and isGoodPortPrincipal:
                    found = True
                    portPrincipal = child.text

                if isPortPrincipal and not isGoodPortPrincipal:
                    found = False

                if (not (found)):
                    continue
                    
                logger.debug("Port principal: {} - Port rataché {} / {} / id:{}".format(portPrincipal, child.text, child.attrib["class"], child.attrib["href"]))
                print("Code:{:3s} - Port principal: {:30s} - Port : {:35s}".format(child.attrib["href"][1:], portPrincipal, child.text))
        return retour


    # --------------------------------------------------------------
    # Parse UTC+2 en decalage horaire
    # --------------------------------------------------------------
    def __getUTCZone (self, utcInfo: str) -> int :
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
    def __getDebut (self, debut: str, offset: int) -> str: 
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
    def __getPortMareeWS (self, idPort: int, debut: str) -> requests.Response: 
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
    def __convertionTextVersValeurMaree (self, iData: list, jour: datetime.datetime, offsetUTC : int = 0) -> list :
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
    def __getPortMareeUTC (self, idPort: int, debut: str, duree: int, responseAsText : str = None) -> tuple: 
        htmlparser = etree.HTMLParser()
        if (responseAsText == None):
            response = self.__getPortMareeWS(idPort, debut)
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
                decalageUTC : int = self.__getUTCZone (trChild.attrib[TAG_UTC_ATTR_MAREE])
            

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

                    allInfosMareeDuJourParType = self.__convertionTextVersValeurMaree(allInfosMareeDuJourParType, jourdebut, decalageUTC)
                    allInfosMareeDuJour.append(allInfosMareeDuJourParType)
                allInfosMaree[jourdebut] = allInfosMareeDuJour

        logger.info (allInfosMaree)
        return allInfosMaree


    # --------------------------------------------------------------
    # maree au format CSV pour XLS
    # Jour;	        Heure;	Coef;	Hauteur (m);
    # 2024/8/24;	11:06;	94;	    10,82
    # 2024/8/24;	17:33;	;	    1,69
    # --------------------------------------------------------------
    def __convertTupleForXLS(self, x : tuple, filePath : str = ".\\toto.csv") -> None:
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

