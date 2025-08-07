from pprint import pprint
import re
import logging
import logging.config
import math
from skyfield.api import Angle, load
from skyfield import api
import numpy as np


logging.config.fileConfig('logging.conf')
logger = logging.getLogger("cEphemerides")

class cEphemerides:
    """ gestion des heures """

    def __init__(self):
        self.__secondeDecimale : float = 0.0

    def __str__(self) -> str :
        return f""

    def get_val(self) -> str:
        ts = load.timescale()
        time = ts.utc(2025, 8, 7, 20, 47, 59.99999)

        eph = load('de421.bsp')
        earth, neptune, sun = eph['earth'], eph['neptune barycenter'], eph['sun']
        radius_km = 24764.0

        astrometric = earth.at(time).observe(sun)
        ra, dec, distance = astrometric.apparent().radec()
        apparent_diameter = Angle(radians=np.arcsin(radius_km / distance.km) * 2.0)
        print('{:.6f} arcseconds'.format(apparent_diameter.arcseconds()))

        geographic = api.wgs84.latlon(latitude_degrees=0, longitude_degrees=0)
        observer = geographic.at(time)
        pos = observer.from_altaz(alt_degrees=90, az_degrees=0)

        ra, dec, distance = pos.radec()
        print(ra)
        print(dec)

