from TrameNMEANav.CLatitude import CLatitude
from TrameNMEANav.CLongitude import CLongitude


class CPosition:
    def __init__(self, *, lat: CLatitude = None, lon: CLongitude = None):
        if lat is None:
            lat = CLatitude(0.0)
        self.__latitude: CLatitude = lat

        if lon is None:
            lon = CLongitude(0.0)
        self.__longitude: CLongitude = lon

    def __str__(self):
        return f"{self.__latitude:s} {self.__longitude:s}"

    def __format__(self, format_spec):
        return str(self)

    def latitude(self, val: CLatitude = None):
        if val is None:
            return self.__latitude
        self.__latitude = val

    def longitude(self, val: CLongitude = None):
        if val is None:
            return self.__longitude
        self.__longitude = val


