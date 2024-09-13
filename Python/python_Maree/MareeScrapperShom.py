from MareeScrapper import MareeScrapper
import logging
import logging.config
import os
import json

from urllib import request
from urllib.error import URLError, HTTPError

from datetime import datetime

import xml.etree.ElementTree as ET

loggingInitPath = os.path.join(os.path.dirname(__file__), ".\\logging.json")
with open(loggingInitPath, 'r') as f:
    CONFIG = f.read()
logging.config.dictConfig(json.loads(CONFIG))
logger = logging.getLogger("MareeScrapperShom")


class MareeScrapperShom (MareeScrapper):
    def __init__(self) -> None:
        super().__init__()
        self.type = "Shom"

    def __str__(self) -> str:
        return self.type

    def getListPort(self, xml : str = None) -> list:
        if xml == None:
            url2Port : str = "https://services.data.shom.fr/spm/listHarbors"
            user_agent : str = 'Mozilla/5.0 (Windows NT 6.1; Win64; x64)'
            headers = {
                "User-Agent": user_agent,
                "Accept": "*/*"
            }
            req = request.Request(url=url2Port, data=None, headers=headers)
            try:
                response = request.urlopen (req, timeout=5.0)
                xml = response.read()
            except HTTPError as e:
                logger.critical(e.reason)
                logger.critical(e.code)
                return []
            except URLError as e:
                if hasattr(e, 'reason'):
                    logger.critical('We failed to reach a server.')
                    logger.critical('Reason: ', e.reason)
                elif hasattr(e, 'code'):
                    logger.critical('The server couldn\'t fulfill the request.')
                    logger.critical('Error code: ', e.code)
                return []
            else:
                logger.info(" OK getListPort")
        info = self.__parsePortReponse(xml)
        self.__afficheInfo(info)

    def getPortMaree(self, port : str, dateDebut : str, dureeEnJour: int, outputName: str = ".\\maree.shom.csv", xml : str = None) -> list:
        logger.info("getPortMaree")
        if xml == None:
            shomDate = "{}-{}-{}".format(dateDebut[0:4],dateDebut[4:6],dateDebut[6:8])
            if dureeEnJour > 10: 
                idureeEnJour = 10
            url2Port : str = "https://services.data.shom.fr/b2q8lrcdl4s04cbabsj4nhcb/hdm/spm/hlt?harborName={}&duration={}&date={}&utc=0&correlation=1".format(port, idureeEnJour, shomDate)
            headers = {
                "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/115.0",
                "Accept": "*/*",
                "Accept-Language": "en-US,en;q=0.5",
                "Accept-Encoding": "gzip, deflate, br", 
                "Origin": "https://maree.shom.fr",
                "DNT": "1",
                "Connection": "keep-alive", 
                "Referer": "https://maree.shom.fr/", 
                "Sec-Fetch-Dest": "empty",
                "Sec-Fetch-Mode": "cors", 
                "Sec-Fetch-Site": "same-site", 
                "If-None-Match": "W/\"424-Ng/G7gNycc89MWh94K1u+DvGCmE\""
            }
            req = request.Request(url=url2Port, data=None, headers=headers)
            try:
                response = request.urlopen (req, timeout=5.0)
                xml = response.read()
            except HTTPError as e:
                logger.critical(e.reason)
                logger.critical(e.code)
                return []
            except URLError as e:
                if hasattr(e, 'reason'):
                    logger.critical('We failed to reach a server.')
                    logger.critical('Reason: ', e.reason)
                elif hasattr(e, 'code'):
                    logger.critical('The server couldn\'t fulfill the request.')
                    logger.critical('Error code: ', e.code)
                return []
            else:
                logger.info(" OK getListPort")
        self.__afficheCSVPortMareeInfo(xml, outputName)

    def __parsePortReponse(self, xml : str) -> dict :
        retour : dict = {}
        rootDoc = ET.fromstring(xml)
        for child in rootDoc:
            # <harbor cst="ALDERNEY_BRAYE" name="Alderney (Braye)" country="Îles Anglo-Normandes" defaultUT="0" additionalUT="0" isCoeffAvailable="0" isOfficial="0" hLegale="0" nota="0" />
            retour[child.attrib['cst']] = {
                "nom": child.attrib['name'],
                "pays": child.attrib['country'],
                "UTC": "{}".format(child.attrib['defaultUT']),
                "tag": child.attrib['cst'],
            }
        return dict(sorted(retour.items()))
        

    def __afficheInfo(self, info : dict) -> None :
        logger.info("__afficheInfo")
        for x in info:
            print("code: {:25s} - info: {:40s}/{:30s}/utc:{:3s}".format(info[x]["tag"], info[x]["nom"], info[x]["pays"], info[x]["UTC"]))

    def __afficheCSVPortMareeInfo(self, info: str, outputName:str) -> None :
        infoMaree = json.loads(info)
        f = open(os.path.join(os.path.dirname(__file__), outputName),"wt") 
        for unJour in infoMaree:
                for i in range(len(infoMaree[unJour])):
                    if (infoMaree[unJour][i][1] != "--:--"):
                        # 2015/03/06;00:06;28;08,22
                        # jour;heure;coeff;hauteur
                        s : str = "{};{};{};{}".format(unJour, infoMaree[unJour][i][1], infoMaree[unJour][i][3] if infoMaree[unJour][i][3] != "---" else "", infoMaree[unJour][i][2])
                        print(s)
                        f.write (s + "\n")
        f.close

    @property
    def type(self) -> str:
        return self.__type 
    @type.setter
    def type(self, value):
        logger.info("Setting value...")
        self.__type = value
