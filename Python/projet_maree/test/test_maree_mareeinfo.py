import unittest
import platform
import pytz
from maree.MareeScrapperMareeInfo import MareeScrapperMareeInfo
from datetime import datetime
import os
import tempfile


class test_maree_mareeinfo(unittest.TestCase):
    def cc(self):
        print("Running on Python ver.{} on {} {}\n".format(
            platform.python_version(),
            platform.system(),
            platform.release()
            )
        )

        print ("Current dir: {}\n".format(os.getcwd))

    def test_getListPort (self) -> None :
        xx : MareeScrapperMareeInfo = MareeScrapperMareeInfo()
        y : list = xx.getListPort()
        


    def test_privateGetUTCZone (self) -> None :
        xx : MareeScrapperMareeInfo = MareeScrapperMareeInfo()
        self.assertEqual (xx._MareeScrapperMareeInfo__getUTCZone("UTC+2"), 2)   
        self.assertEqual (xx._MareeScrapperMareeInfo__getUTCZone("UTC+1"), 1)   
        self.assertEqual (xx._MareeScrapperMareeInfo__getUTCZone("UTC+0"), 0)   
        self.assertEqual (xx._MareeScrapperMareeInfo__getUTCZone("UTC"), 0)   
        self.assertEqual (xx._MareeScrapperMareeInfo__getUTCZone("UTC-1"), -1)   
        self.assertEqual (xx._MareeScrapperMareeInfo__getUTCZone("xxx"), 0)   

    def test_getDebut (self) -> None: 
        xx : MareeScrapperMareeInfo = MareeScrapperMareeInfo()
        self.assertEqual (xx._MareeScrapperMareeInfo__getDebut("20240101", -2), "20231230")    
        self.assertEqual (xx._MareeScrapperMareeInfo__getDebut("20240101", +2), "20240103")    
        self.assertEqual (xx._MareeScrapperMareeInfo__getDebut("20240225", +5), "20240301")    

    def test_privateConvertionTextVersValeurMaree (self) -> None : 
        xx : MareeScrapperMareeInfo = MareeScrapperMareeInfo()
        self.assertEqual(xx._MareeScrapperMareeInfo__convertionTextVersValeurMaree(["2,23m"], datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc),0), [2.23])
        self.assertEqual(xx._MareeScrapperMareeInfo__convertionTextVersValeurMaree(["2.23m"], datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc),0), [2.23])
        self.assertEqual(xx._MareeScrapperMareeInfo__convertionTextVersValeurMaree(["2h23"], datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc),0), 
                         [datetime(2015, 3, 4, 2, 23, tzinfo=pytz.utc)])
        self.assertEqual(xx._MareeScrapperMareeInfo__convertionTextVersValeurMaree(["2h23"], datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc),2), 
                         [datetime(2015, 3, 4, 0, 23, tzinfo=pytz.utc)])
        self.assertEqual(xx._MareeScrapperMareeInfo__convertionTextVersValeurMaree(["23"], datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc),0), [23])
        self.assertEqual(xx._MareeScrapperMareeInfo__convertionTextVersValeurMaree(["2,23m","2h23", "23"], datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc),0), 
                         [2.23, datetime(2015, 3, 4, 2, 23, tzinfo=pytz.utc), 23])
        self.assertEqual(xx._MareeScrapperMareeInfo__convertionTextVersValeurMaree(["2m","2h", "ddd"], datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc),0), 
                         [2, datetime(2015, 3, 4, 2, 0, tzinfo=pytz.utc), -1])

    def test_getPortMareeScrapperMareeInfoUTC (self) -> None: 
        xx : MareeScrapperMareeInfo = MareeScrapperMareeInfo()
        with open(os.path.join(os.path.dirname(__file__),".\\odtref\\maree.info.52.odt.html")) as f:
            contents = f.read()
 
        f = tempfile.NamedTemporaryFile(delete=False)
        x = xx.getPortMaree("52", "20150301", 5, f.name, contents)
        self.assertEqual (len(x), 7)   
        self.assertEqual (len(x[datetime(2015, 3, 1, 0, 0, tzinfo=pytz.utc)]), 3)    
        self.assertEqual (len(x[datetime(2015, 3, 1, 0, 0, tzinfo=pytz.utc)][0]), 4)    
        self.assertEqual (len(x[datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc)]), 3)  
        self.assertEqual (len(x[datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc)][0]), 3)  
        
        self.assertEqual (x[datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc)][0], 
                          [datetime(2015, 3, 4, 4, 27, tzinfo=pytz.utc), datetime(2015, 3, 4, 9, 57, tzinfo=pytz.utc), datetime(2015, 3, 4, 16, 57, tzinfo=pytz.utc)])
        self.assertEqual (x[datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc)][1], [4.38, 9.32, 4.71])
        self.assertEqual (x[datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc)][2], [-1, 39, -1])
        self.assertEqual (x[datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc)][2], [-1, 39, -1])

        self.assertEqual (x[datetime(2015, 3, 7, 0, 0, tzinfo=pytz.utc)], 
                          [[datetime(2015, 3, 7, 2, 23, tzinfo=pytz.utc), datetime(2015, 3, 7, 8, 59, tzinfo=pytz.utc), datetime(2015, 3, 7, 15, 3, tzinfo=pytz.utc), datetime(2015, 3, 7, 21, 44, tzinfo=pytz.utc)], [8.59, 4.99, 9.19, 4.12], [37, -1, 45, -1]])





    def test_convertTupleForXLS (self) -> None: 
        xx : MareeScrapperMareeInfo = MareeScrapperMareeInfo()
        with open(os.path.join(os.path.dirname(__file__),".\\odtref\\maree.info.52.odt.html")) as f:
            contents = f.read()
 
        f = tempfile.NamedTemporaryFile(delete=False)
        x = xx.getPortMaree("52", "20150301", 5, f.name)
        filePath = os.path.join(os.path.dirname(__file__),".\\odtref\\maree.odt.csv")
        xx._MareeScrapperMareeInfo__convertTupleForXLS(x, filePath)

        with open(os.path.join(os.path.dirname(__file__),".\\odtref\\maree.odt.csv")) as f:
            contentsRef = f.read()

        with open(filePath) as f:
            contents = f.read()
        
        self.assertEqual (contents, contentsRef)

        with open(os.path.join(os.path.dirname(__file__),".\\odtref\\maree.info.52.14j.odt.html")) as f:
            contents = f.read()

        f = tempfile.NamedTemporaryFile(delete=False)
        x = xx.getPortMaree("52", "20150301", 14, f.name)
        filePath = os.path.join(os.path.dirname(__file__),".\\odtref\\maree.14j.csv")
        xx._MareeScrapperMareeInfo__convertTupleForXLS(x, filePath)
        with open(filePath) as f:
            contents = f.read()        

        f = tempfile.NamedTemporaryFile(delete=False)
        x = xx.getPortMaree("62", "20240911", 14, f.name)


    def test_isDateValide (self) -> None: 
        xx : MareeScrapperMareeInfo = MareeScrapperMareeInfo()
        x = xx.isDateValide("20150301")
        self.assertTrue (x)

        x = xx.isDateValide("201503010")
        self.assertFalse (x)

        x = xx.isDateValide("20150399")
        self.assertFalse (x)

        x = xx.isDateValide("20152201")
        self.assertFalse (x)

        x = xx.isDateValide("20152299")
        self.assertFalse (x)

        x = xx.isDateValide("99991201")
        self.assertTrue (x)



if __name__ == '__main__':
    print ("Current dir: {} - prgm dir: {}\n".format(os.getcwd(), os.path.dirname(__file__)))
    print ("change to prgm dir ... \n")
    os.chdir (os.path.dirname(__file__))
    unittest.main()