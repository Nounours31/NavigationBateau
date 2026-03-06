import json
from ast import literal_eval
from typing import Any, Dict


def string_to_dict(s: str) -> Dict[str, Any]:
    """
    Convertit une chaîne en dictionnaire Python.
    Gère JSON et notation Python littérale.
    """
    s = s.strip()
    if not s:
        return {}

    # Première tentative : JSON
    try:
        result = json.loads(s)
        if isinstance(result, dict):
            return result
    except json.JSONDecodeError:
        pass

    # Deuxième tentative : Python literal
    try:
        result = literal_eval(s)
        if isinstance(result, dict):
            return result
    except (ValueError, SyntaxError):
        pass

    raise ValueError("La chaîne ne correspond ni à un JSON valide ni à un dict Python valide.")

