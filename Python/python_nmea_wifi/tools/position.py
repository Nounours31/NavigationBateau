from __future__ import annotations
from tools.latitude import latitude as latitude
from tools.longitude import longitude as longitude

class position:
    def __init__(self, lat: latitude, lon: longitude):
        self.__lat = lat
        self.__long = lon
    
    @property
    def latitude(self) -> latitude:
        return self.__lat

    @latitude.setter
    def latitude(self,l:latitude) -> None:
        self.__lat = l

    @property
    def longitude(self) -> longitude:
        return self.__long

    @longitude.setter
    def longitude(self,l:longitude) -> None:
        self.__long = l
    
    def copy(self) -> position:
        retour : position = position(self.__lat.copy(), self.__long.copy())
        return retour 

    def toString(self): 
        return f"Position: \t[latitude:{self.latitude.toString()}  --- longitude:{self.longitude.toString()}]"

    
            