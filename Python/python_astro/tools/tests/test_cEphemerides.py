from datetime import datetime, timezone
from pprint import pprint
import sys

from cEphemerides import cEphemerides
from cLatitude import cLatitude
from cLongitude import cLongitude
from cAstroError import cAstroError
from cPosition import cPosition
from cAngle import cAngle

print(sys.executable) # show which Python we are running
print(sys.path)

from pytest import approx, raises

#from pprint import pprint

import logging
import logging.config

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("test_cEphemerides")


# ------------------------------------
def test_cas1():
    x : cEphemerides = cEphemerides()
    t : datetime = datetime(2050, 1, 2, 19, 0, 0, 0, tzinfo=timezone.utc)
    astre : str = "moon"
    lat : cLatitude = cLatitude()
    lat = lat.parse("27°30 S")
    long : cLongitude = cLongitude()
    long = long.parse("63° E")
    dr : cPosition = cPosition()
    dr.setLongitude(long)
    dr.setLatitude(lat)
    
    xx = x.getEpherideAstre(t, astre, dr)
    print(f"GHA Aries: {cAngle.fromDeg(float(xx["gha_aries"])).toString()}")
    print( "--------------------------------------------------")
    print(f"GHA:       {cAngle.fromDeg(float(xx["gha"])).toString()}")
    print(f"Dec:       {cAngle.fromDeg(float(xx["dec"])).toString()}")
    print(f"SD:        {cAngle.fromDeg(float(xx["sd"])).toString()}")
    print( "--------------------------------------------------")
    print(f"Az:        {cAngle.fromDeg(float(xx["az"])).toString()}")
    print(f"HP:        {cAngle.fromDeg(float(xx["hp"])).toString()}")
    print(f"Hc:        {cAngle.fromDeg(float(xx["hauteurObservee"])).toString()}")
    print(f"Hc + HP:   {cAngle.fromDeg(float(xx["hauteurObserveeCorrigeeParallaxe"])).toString()}")

    assert xx is None

def test_cas2():
    x : cEphemerides = cEphemerides()
    t : datetime = datetime(2050, 1, 2, 19, 0, 0, 0, tzinfo=timezone.utc)
    astre : str = "Ankaa"
    lat : cLatitude = cLatitude()
    lat = lat.parse("27°30 S")
    long : cLongitude = cLongitude()
    long = long.parse("63° E")
    dr : cPosition = cPosition()
    dr.setLongitude(long)
    dr.setLatitude(lat)
    
    xx = x.getEpherideAstre(t, astre, dr)
    print(f"GHA Aries: {cAngle.fromDeg(float(xx["gha_aries"])).toString()}")
    print( "--------------------------------------------------")
    print(f"GHA:       {cAngle.fromDeg(float(xx["gha"])).toString()}")
    print(f"Dec:       {cAngle.fromDeg(float(xx["dec"])).toString()}")
    print(f"SD:        {cAngle.fromDeg(float(xx["sd"])).toString()}")
    print( "--------------------------------------------------")
    print(f"Az:        {cAngle.fromDeg(float(xx["az"])).toString()}")
    print(f"HP:        {cAngle.fromDeg(float(xx["hp"])).toString()}")
    print(f"Hc:        {cAngle.fromDeg(float(xx["hauteurObservee"])).toString()}")
    print(f"Hc + HP:   {cAngle.fromDeg(float(xx["hauteurObserveeCorrigeeParallaxe"])).toString()}")

    assert xx is None




if __name__ == "__main__":
    # Execute when the module is not initialized from an import statement.
    test_cas1()
