import pytest
from datetime import datetime

from nmea.nmea0183Lib import nmea0183lib as nmea


def test_function():
    x : nmea = nmea()
    assert x.heure2GPSDecimale(datetime.now()) == 4