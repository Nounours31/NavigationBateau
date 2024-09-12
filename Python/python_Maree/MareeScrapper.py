from abc import ABC, abstractmethod
from datetime import datetime

class MareeScrapper (ABC):
    def __init__(self) -> None:
        super().__init__()

    @abstractmethod
    def __str__(self) -> str:
        return super().__str__()

    @abstractmethod
    def getListPort(self) -> list:
        pass

    @abstractmethod
    def getPortMaree(self, port : int, dateDebut : datetime, dureeEnHour: int) -> list:
        pass

    @property
    def type(self):
        pass
