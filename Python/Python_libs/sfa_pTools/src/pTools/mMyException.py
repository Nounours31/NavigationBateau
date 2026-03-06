"""
Gestion specialisee des exceptions et erreurs
"""
class cMyException(Exception):
    """
    Ma classe exception
    """
    def __init__(self, msg : str):
        """
        Initialise l'exception    
        :param msg: message d'erreur
        """
        super().__init__(msg)
        self._msg = msg

    def __str__(self) -> str:
        """
        Retourne le message d'erreur
        :return: message d'erreur
        """
        retour = super().__str__() + f"- msg:[{self._msg}]"
        return retour


