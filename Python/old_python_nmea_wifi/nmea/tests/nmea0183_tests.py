import pytest
from datetime import datetime

from nmea.nmea0183Lib import nmea0183lib as nmea


def test_function():
    x : nmea = nmea()
    d : datetime = datetime.now()
    s : str = f"{d.hour:02d}{d.minute:02d}{d.second:02d}.{d.microsecond:06d}"
    assert f"{x.heure2GPSDecimale(d):5.6f}" == s