
import exception

class MyException(Exception):

    def __init__(self, msg : str):
        super().__init__()
        self._msg = msg

    def __str__(self) -> str:
        retour = super().__str__() + f"- msg:[{self._msg}]"
        return retour


