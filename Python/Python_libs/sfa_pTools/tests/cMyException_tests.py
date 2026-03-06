from pTools import mMyException

def test_mMyException():
    """
    Test de la classe mMyException
    """
    try:
        raise mMyException.cMyException("test")
    except mMyException.cMyException as e:
        assert str(e) == "test- msg:[test]"

