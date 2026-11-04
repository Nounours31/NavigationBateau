import logging
from typing import Dict, List

import requests

import datetime as dt
from datetime import datetime, timedelta

# https://arrow.readthedocs.io/en/latest/guide.html
import arrow

import json
from dataclasses import dataclass

from pMareeInfo import mListPortConnu


logging.basicConfig(
    level=logging.INFO,  # Set minimum log level
    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s",
    datefmt="%Y-%m-%d %H:%M:%S"
)

logger: logging.Logger = logging.getLogger("main.py")
logger.info("Start extartc")

@dataclass
class cInfoMaree:
    height: float
    time: datetime
    type: str

    def __ge__(self, other) -> bool:
        if not isinstance(other, cInfoMaree):
            return NotImplemented
        return self.time >= other.time

    def __gt__(self, other) -> bool:
        if not isinstance(other, cInfoMaree):
            return NotImplemented
        return self.time > other.time

    def __le__(self, other) -> bool:
        if not isinstance(other, cInfoMaree):
            return NotImplemented
        return self.time <= other.time

    def __lt__(self, other) -> bool:
        if not isinstance(other, cInfoMaree):
            return NotImplemented
        return self.time < other.time
    

    # je trie selon l'ordre de passge PAS la proximitée
    @staticmethod
    def quick_sort(arr : List[cInfoMaree]) -> List[cInfoMaree]:
        if len(arr) <= 1:
            return arr
        info : cInfoMaree = arr[0]
        pivot : cInfoMaree = info
        
        left : List[cInfoMaree] = [x for x in arr[1:] if x < pivot]
        right: List[cInfoMaree]= [x for x in arr[1:] if x >= pivot]
        return cInfoMaree.quick_sort(left) + [info] + cInfoMaree.quick_sort(right)




API_KEY = "7a7ac9e0-31c8-11f1-9fff-0242ac120004-7a7acabc-31c8-11f1-9fff-0242ac120004"

class CExtract:
    def __init__(self, port_index: int, start: float, end: float):
        self.port_index = port_index

        self.lat : float = float(mListPortConnu.listPortConnu[port_index]["latitude"])
        self.lng : float = float(mListPortConnu.listPortConnu[port_index]["longitude"])
        self.MSL : float = float(mListPortConnu.listPortConnu[port_index]["MSL"])
        self.PHMA : float = float(mListPortConnu.listPortConnu[port_index]["PHMA"])
        self.PBMA : float = float(mListPortConnu.listPortConnu[port_index]["PBMA"])

        self.start : datetime = datetime.fromtimestamp(start)
        self.end : datetime = datetime.fromtimestamp(end)



    def extract_tide_data_arrow(self):
        start = arrow.get(self.start,tzinfo=dt.timezone.utc).floor('day')
        end = arrow.get(self.end,tzinfo=dt.timezone.utc).ceil('day')
        params : Dict[str, float|str] = {
                'lat': self.lat,
                'lng': self.lng,
                'start': start.to('UTC').timestamp(),  # Convert to UTC timestamp
                'end': end.to('UTC').timestamp(),  # Convert to UTC timestamp
                'datum': 'MSL'  # MLLW: Mean Lower Low Water  / MSL: Medium Sea Level
            }
        logger.info(f"Requesting tide data with parameters: {params}")


        response = requests.get(
            'https://api.stormglass.io/v2/tide/extremes/point',
            params=params,
            headers={
                'Authorization': API_KEY
            }
        )
        
        """
            status_code = 200
            text = '{"data":[{"height":-5.354792546742339,"time":"2026-09-10T00:07:00+00:00","type":"low"},{"height":5.461728034641628,"time":"2026-09-10T05:33:00+00:00","type":"high"},{"height":-5.395681746206923,"time":"2026-09-10T12:27:00+00:00","type":"low"},{"height":6.052587915136537,"time":"2026-09-10T17:52:00+00:00","type":"high"},{"height":-5.953449518813226,"time":"2026-09-11T00:48:00+00:00","type":"low"},{"height":6.107263744202375,"time":"2026-09-11T06:14:00+00:00","type":"high"},{"height":-5.864274950567451,"time":"2026-09-11T13:04:00+00:00","type":"low"},{"height":6.542495299356997,"time":"2026-09-11T18:31:00+00:00","type":"high"},{"height":-6.1741485484293905,"time":"2026-09-12T01:24:00+00:00","type":"low"},{"height":6.426773569821868,"time":"2026-09-12T06:51:00+00:00","type":"high"},{"height":-5.976096001975533,"time":"2026-09-12T13:38:00+00:00","type":"low"},{"height":6.670217649600968,"time":"2026-09-12T19:08:00+00:00","type":"high"},{"height":-6.040666444766742,"time":"2026-09-13T01:56:00+00:00","type":"low"}...
            url = 'https://api.stormglass.io/v2/tide/extremes/point?lat=48.676&lng=-1.852&start=1788998400.0&end=1789430399.999999&datum=MSL'
            _content = b'{"data":[{"height":-5.354792546742339,"time":"2026-09-10T00:07:00+00:00","type":"low"},{"height":5.461728034641628,"time":"2026-09-10T05:33:00+00:00","type":"high"},{"height":-5.395681746206923,"time":"2026-09-10T12:27:00+00:00","type":"low"},{"height":6.052587915136537,"time":"2026-09-10T17:52:00+00:00","type":"high"},{"height":-5.953449518813226,"time":"2026-09-11T00:48:00+00:00","type":"low"},{"height":6.107263744202375,"time":"2026-09-11T06:14:00+00:00","type":"high"},{"height":-5.864274950567451,"time":"2026-09-11T13:04:00+00:00","type":"low"},{"height":6.542495299356997,"time":"2026-09-11T18:31:00+00:00","type":"high"},{"height":-6.1741485484293905,"time":"2026-09-12T01:24:00+00:00","type":"low"},{"height":6.426773569821868,"time":"2026-09-12T06:51:00+00:00","type":"high"},{"height":-5.976096001975533,"time":"2026-09-12T13:38:00+00:00","type":"low"},{"height":6.670217649600968,"time":"2026-09-12T19:08:00+00:00","type":"high"},{"height":-6.040666444766742,"time":"2026-09-13T01:56:00+00:00","type":"low"...
        """
        
        # Do something with response data.
        data : Dict[str, object] = {}
        if response is not None:
            data = response.json()
        logger.info(f"Received tide data: {json.dumps(data, indent=2)}")


        # affichage lisible
        infos : List[cInfoMaree] = []
        for tide in data["data"]:
            info : cInfoMaree = cInfoMaree(
                height=float(str(tide["height"])),
                time=arrow.get(str(tide["time"]),tzinfo=dt.timezone.utc).to('local').datetime,
                type=str(tide["type"])
            )
            infos.append(info)
        
        
        infosSorted : List[cInfoMaree] =  cInfoMaree.quick_sort(infos)
        logger.info(f"Sorted tide data: {[{'height': info.height, 'time': info.time.isoformat(), 'type': info.type} for info in infosSorted]}")

        indice : int = -1
        for info in infosSorted:
            indice += 1
            type : str = "PM" if info.type == "high" else "BM"
            jour : str = info.time.strftime("%Y-%m-%d")
            heure : str = info.time.strftime("%H:%M")
            
            fCoef : float = -1.0
            if info.type == "high" and indice > 0:
                fCoef = 20 + (info.height - infosSorted[indice - 1].height) / (self.PHMA - self.PBMA) * 100
            coef: str = str(fCoef) if info.type == "high" else ""
            height: str = f"{(self.MSL + info.height):.2f}"
            print(f"{type};{jour};{heure};{coef};{height}")

