# depuis stack over flow
# https://stackoverflow.com/questions/13664935/is-this-how-to-compute-greenwich-hour-angle-with-pyephem-under-python-3
#
# I have been using python3 and pyephem to study celestial navigation mostly working through some calculations 
# that would have to be done by hand in any case.
# For the sight reduction part I am trying to have pyephem output data comparable to the nautical almanac, 
# mostly greenwich hour angle and declination. 
# To get the hour angle of the first point of aries, I tried adding a body using ephem.readdb at 0.0 RA and 0.0 dec. 
# But after reading the doc further I think this is working.


import math

from skyfield.api import load
from skyfield.api import N, W, wgs84
from skyfield.api import Angle, Time
from skyfield.api import Star
from skyfield.data import hipparcos
from skyfield.named_stars import named_star_dict
from skyfield.positionlib import build_position

diametreLuneenKm=3474.8
diametreSoleilenKm=1392684.0
rayonTerreenKm=6371.008


def myFloatDisplay(abs : float) -> str:
    retour : str = ""
    if abs < 0.0 :
        retour = "-"
        abs = -1.0 * abs
    
    retour = retour + "{:02d}".format(int (abs)) + "°"
    abs = (abs - int(abs)) * 60.0
    retour = retour + "{:04.1f}".format(abs)
    return retour

def myAngleDisplay(a : Angle, hours: bool) -> str:
    retour : str = ""
    abs : float = 0.0
    if hours:
        abs = a.hours
    else:
        abs = a.degrees

    if abs < 0.0 :
        abs = 360.0 + abs
    
    retour = retour + "{:02d}".format(int (abs)) + "°"
    abs = (abs - int(abs)) * 60.0
    retour = retour + "{:04.1f}".format(abs)
    return retour

def myLatDisplay(a : Angle) -> str:
    retour : str = ""
    abs : float = a.degrees
    if a.degrees < 0.0 :
        retour = "-"
        abs = -1.0 * abs
    
    retour = retour + str(int (abs)) + "°"
    abs = (abs - int(abs)) * 60.0
    retour = retour + "{:04.1f}".format(abs)
    return retour

def myLongDisplay(a : Angle) -> str:
    retour : str = ""
    abs : float = a.degrees
    if (abs >= 0.0): # Est
        abs = 360.0 - abs

    if a.degrees < 0.0 : # Ouest
        abs = -1.0 * abs
    
    retour = retour + str(int (abs)) + "°"
    abs = (abs - int(abs)) * 60.0
    retour = retour + "{:04.1f}".format(abs)
    return retour
    
# def computePlanet(name:str, t:Time) -> tuple :
    
   
planets = load('de421.bsp')
earth, sun, moon = planets['earth'], planets['sun'], planets['moon']
moon = planets['moon']

ts = load.timescale()
t : Time = ts.utc(2010, 5, 5, 8, 0, 0)

point = wgs84.subpoint(earth.at(t).observe(sun))
distanceauSoleil =  point.elevation.km
HP=(180.0 / math.pi) * math.asin(rayonTerreenKm/(rayonTerreenKm + point.elevation.km))
SD=0.5 * (180.0 / math.pi) * 2.0 * math.atan ((diametreSoleilenKm/2.0) / point.elevation.km)
print ("== Sun =======================================================")
print ("Dec: " + myLatDisplay(point.latitude))
print ("GHA: " + myLongDisplay(point.longitude))
print ("HP:  " + myFloatDisplay(HP))
print ("SD:  " + myFloatDisplay(SD))

point = wgs84.subpoint(earth.at(t).observe(moon))
HP=(180.0 / math.pi) * math.asin(rayonTerreenKm/(rayonTerreenKm + point.elevation.km))
SD=0.5 * (180.0 / math.pi) * 2.0 * math.atan ((diametreLuneenKm/2.0) / point.elevation.km)
print ("== Lune =======================================================")
print ("Dec: " + myLatDisplay(point.latitude))
print ("GHA: " + myLongDisplay(point.longitude))
print ("HP:  " + myFloatDisplay(HP))
print ("SD:  " + myFloatDisplay(SD))


icrs_xyz_au = [1.0, 0.0, 0.0]
position = build_position(icrs_xyz_au, t=t)
precession = 4/3600
deltaYear = t._utc_tuple(0.0)[0] - 2010
ra_hours=-0.009 - (precession * (deltaYear))
star = Star(ra_hours= ra_hours, dec_degrees=0.0) # point vernal

point = wgs84.subpoint(earth.at(t).observe(star))
HP=0.0
SD=0.0
print ("== Aries mais semble KO =======================================================")
print ("Dec: " + myLatDisplay(point.latitude))
print ("GHA: " + myLongDisplay(point.longitude))
print ("HP:  " + myFloatDisplay(HP))
print ("SD:  " + myFloatDisplay(SD))


with load.open(hipparcos.URL) as f:
    df = hipparcos.load_dataframe(f)

starName="Ankaa"
star=Star.from_dataframe(df.loc[named_star_dict[starName]])
point = wgs84.subpoint(earth.at(t).observe(star))
HP=(180.0 / math.pi) * math.asin(rayonTerreenKm/(rayonTerreenKm + point.elevation.km))
SD=0.0
print ("== "+ starName +" =======================================================")
print ("Dec: " + myLatDisplay(point.latitude))
print ("GHA: " + myLongDisplay(point.longitude))
print ("HP:  " + myFloatDisplay(HP))
print ("SD:  " + myFloatDisplay(SD))
