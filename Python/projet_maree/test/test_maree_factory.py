import unittest
import platform
from maree.MareeScrapperShom import MareeScrapperShom
from maree.MareeScrapperFactory import MareeScrapperFactory
from maree.MareeScrapper import MareeScrapper
from maree.MareeScrapperMareeInfo import MareeScrapperMareeInfo
import os


class test_maree_factory(unittest.TestCase):
    def cc(self):
        print("Running on Python ver.{} on {} {}\n".format(
            platform.python_version(),
            platform.system(),
            platform.release()
            )
        )
        print ("Current dir: {}\n".format(os.getcwd))

    def test_origines (self) -> None :
        xx : dict = MareeScrapperFactory.getOrigines()
        self.assertFalse("toto" in xx)
        self.assertIsNotNone (xx["Shom"])
        self.assertIsNotNone (xx["MareeInfo"])
        self.assertEqual (xx["Shom"], 0)
        self.assertIsNotNone (xx["MareeInfo"], 1)

    def test_ (self) -> None :
        xx : MareeScrapper = MareeScrapperFactory.getScrapper(0)
        self.assertTrue(type(xx) is MareeScrapperShom)

        xx = MareeScrapperFactory.getScrapper(1)
        self.assertTrue(type(xx) is MareeScrapperMareeInfo)

if __name__ == '__main__':
    print ("Current dir: {} - prgm dir: {}\n".format(os.getcwd(), os.path.dirname(__file__)))
    print ("change to prgm dir ... \n")
    os.chdir (os.path.dirname(__file__))
    unittest.main()