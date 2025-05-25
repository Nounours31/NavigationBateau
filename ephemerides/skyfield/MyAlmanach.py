# -*- coding: utf-8 -*-
# 
# # depuis stack over flow
# https://stackoverflow.com/questions/13664935/is-this-how-to-compute-greenwich-hour-angle-with-pyephem-under-python-3
#
# I have been using python3 and pyephem to study celestial navigation mostly working through some calculations 
# that would have to be done by hand in any case.
# For the sight reduction part I am trying to have pyephem output data comparable to the nautical almanac, 
# mostly greenwich hour angle and declination. 
# To get the hour angle of the first point of aries, I tried adding a body using ephem.readdb at 0.0 RA and 0.0 dec. 
# But after reading the doc further I think this is working.


import math
import argparse
import sys

from skyfield.api import load
from skyfield.api import N, W, wgs84
from skyfield.api import Angle, Time
from skyfield.api import Star
from skyfield.data import hipparcos
from skyfield.named_stars import named_star_dict
from skyfield.positionlib import build_position


    # recup des parametre
parser = argparse.ArgumentParser( prog="MyAlmanach")

requiredNamed = parser.add_argument_group('Arg necessaires')
requiredNamed.add_argument('-astre', type=str, nargs=1, help='nom astre  (sun, moon, ankaa, ...)', required=True)
requiredNamed.add_argument('-a', type=int, nargs=1, help='annee', required=True)
requiredNamed.add_argument('-m', type=int, nargs=1, help='mois', required=True)
requiredNamed.add_argument('-d', type=int, nargs=1, help='jour', required=True)
requiredNamed.add_argument('-H', type=int, nargs=1, help='heure utc', required=True)
requiredNamed.add_argument('-M', type=int, nargs=1, help='minute', required=True)
requiredNamed.add_argument('-S', type=float, nargs=1, help='seconde', required=True)
requiredNamed.add_argument('-t', type=str, nargs=1, help='type demande  (DEC, GHA...)', required=True)

args = parser.parse_args()

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

def myAngleAsFloat(a : Angle) -> float:
    abs : float = 0.0
    return  a.degrees

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
t : Time = ts.utc(args.a[0], args.m[0], args.d[0], args.H[0], args.M[0], args.S[0])

point = wgs84.subpoint(earth.at(t).observe(sun))
distanceauSoleil =  point.elevation.km
HP=(180.0 / math.pi) * math.asin(rayonTerreenKm/(rayonTerreenKm + point.elevation.km))
SD=0.5 * (180.0 / math.pi) * 2.0 * math.atan ((diametreSoleilenKm/2.0) / point.elevation.km)


if args.t[0] == 'DEC':
    print (myAngleAsFloat(point.latitude))

if args.t[0] == 'GHA':
    print (myAngleAsFloat(point.longitude))
