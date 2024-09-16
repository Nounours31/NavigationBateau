import unittest
import platform
from maree.MareeScrapperShom import MareeScrapperShom
import os


class test_maree_shom(unittest.TestCase):
    def cc(self):
        print("Running on Python ver.{} on {} {}\n".format(
            platform.python_version(),
            platform.system(),
            platform.release()
            )
        )
        print ("Current dir: {}\n".format(os.getcwd))

    def test_getListPort (self) -> None :
        xx : MareeScrapperShom = MareeScrapperShom()
        y : list = xx.getListPort()
        self.assertIsNone(y)
        





if __name__ == '__main__':
    print ("Current dir: {} - prgm dir: {}\n".format(os.getcwd(), os.path.dirname(__file__)))
    print ("change to prgm dir ... \n")
    os.chdir (os.path.dirname(__file__))
    unittest.main()