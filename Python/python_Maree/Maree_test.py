import unittest
import platform
import pytz
import Maree
import datetime
import os

class maree_test(unittest.TestCase):
    def cc(self):
        print("Running on Python ver.{} on {} {}\n".format(
            platform.python_version(),
            platform.system(),
            platform.release()
            )
        )

        print ("Current dir: {}\n".format(os.getcwd))


    def test_getPortPrincipaux(self):
        x = Maree.getPortPrincipaux()
        self.assertEqual(x["Saint-Malo"]["Id"], "/52")
        self.assertEqual(x["Saint-Jean-de-Luz"]["Id"], "/141")
        self.assertEqual(x["Paimpol"]["Id"], "/62")
        self.assertEqual(x["Dunkerque"]["Id"], "/3")

    def test_getPortSecondaire(self):
        x = Maree.getPortSecondaire(portPrincipal=None, idPortPrincipal=None)
        self.assertEqual(len(x), 0)

        x = Maree.getPortSecondaire(portPrincipal="Paimpol", idPortPrincipal=None)
        self.assertEqual(len(x), 11)
        self.assertEqual(x["62"]["NomPP"], "Paimpol")
        self.assertEqual(x["62"]["Nom"], "Paimpol")
        self.assertEqual(x["62"]["Id"], "/62")
        self.assertEqual(x["68"]["NomPP"], "Paimpol")
        self.assertEqual(x["68"]["Nom"], "Trébeurden")
        self.assertEqual(x["68"]["Id"], "/68")

        x = Maree.getPortSecondaire(portPrincipal=None, idPortPrincipal=52)
        self.assertEqual(len(x), 10)
        self.assertEqual(x["52"]["NomPP"], "Saint-Malo")
        self.assertEqual(x["52"]["Nom"], "Saint-Malo")
        self.assertEqual(x["52"]["Id"], "/52")

        self.assertEqual(x["59"]["NomPP"], "Saint-Malo")
        self.assertEqual(x["59"]["Nom"], "Saint-Quay-Portrieux")
        self.assertEqual(x["59"]["Id"], "/59")
        

    def test_privateGetUTCZone (self) -> None :
        self.assertEqual (Maree.privateGetUTCZone("UTC+2"), 2)   
        self.assertEqual (Maree.privateGetUTCZone("UTC+1"), 1)   
        self.assertEqual (Maree.privateGetUTCZone("UTC+0"), 0)   
        self.assertEqual (Maree.privateGetUTCZone("UTC"), 0)   
        self.assertEqual (Maree.privateGetUTCZone("UTC-1"), -1)   
        self.assertEqual (Maree.privateGetUTCZone("xxx"), 0)   

    def test_getDebut (self) -> None: 
        self.assertEqual (Maree.getDebut("20240101", -2), "20231230")    
        self.assertEqual (Maree.getDebut("20240101", +2), "20240103")    
        self.assertEqual (Maree.getDebut("20240225", +5), "20240301")    

    def test_privateConvertionTextVersValeurMaree (self) -> None: 
        self.assertEqual(Maree.privateConvertionTextVersValeurMaree(["2,23m"], datetime.datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc),0), [2.23])
        self.assertEqual(Maree.privateConvertionTextVersValeurMaree(["2.23m"], datetime.datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc),0), [2.23])
        self.assertEqual(Maree.privateConvertionTextVersValeurMaree(["2h23"], datetime.datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc),0), 
                         [datetime.datetime(2015, 3, 4, 2, 23, tzinfo=pytz.utc)])
        self.assertEqual(Maree.privateConvertionTextVersValeurMaree(["2h23"], datetime.datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc),2), 
                         [datetime.datetime(2015, 3, 4, 0, 23, tzinfo=pytz.utc)])
        self.assertEqual(Maree.privateConvertionTextVersValeurMaree(["23"], datetime.datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc),0), [23])
        self.assertEqual(Maree.privateConvertionTextVersValeurMaree(["2,23m","2h23", "23"], datetime.datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc),0), 
                         [2.23, datetime.datetime(2015, 3, 4, 2, 23, tzinfo=pytz.utc), 23])
        self.assertEqual(Maree.privateConvertionTextVersValeurMaree(["2m","2h", "ddd"], datetime.datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc),0), 
                         [2, datetime.datetime(2015, 3, 4, 2, 0, tzinfo=pytz.utc), -1])

    def test_getPortMareeUTC (self) -> None: 
        with open(".\\maree.info.52.odt.html") as f:
            contents = f.read()
 
        isODT : bool = True
        x = Maree.getPortMareeUTC(52, "20150301", 5, contents, isODT)
        self.assertEqual (len(x), 7)   
        self.assertEqual (len(x[datetime.datetime(2015, 3, 1, 0, 0, tzinfo=pytz.utc)]), 3)    
        self.assertEqual (len(x[datetime.datetime(2015, 3, 1, 0, 0, tzinfo=pytz.utc)][0]), 4)    
        self.assertEqual (len(x[datetime.datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc)]), 3)  
        self.assertEqual (len(x[datetime.datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc)][0]), 3)  
        
        self.assertEqual (x[datetime.datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc)][0], 
                          [datetime.datetime(2015, 3, 4, 4, 27, tzinfo=pytz.utc), datetime.datetime(2015, 3, 4, 9, 57, tzinfo=pytz.utc), datetime.datetime(2015, 3, 4, 16, 57, tzinfo=pytz.utc)])
        self.assertEqual (x[datetime.datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc)][1], [4.38, 9.32, 4.71])
        self.assertEqual (x[datetime.datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc)][2], [-1, 39, -1])
        self.assertEqual (x[datetime.datetime(2015, 3, 4, 0, 0, tzinfo=pytz.utc)][2], [-1, 39, -1])

        self.assertEqual (x[datetime.datetime(2015, 3, 7, 0, 0, tzinfo=pytz.utc)], 
                          [[datetime.datetime(2015, 3, 7, 2, 23, tzinfo=pytz.utc), datetime.datetime(2015, 3, 7, 8, 59, tzinfo=pytz.utc), datetime.datetime(2015, 3, 7, 15, 3, tzinfo=pytz.utc), datetime.datetime(2015, 3, 7, 21, 44, tzinfo=pytz.utc)], [8.59, 4.99, 9.19, 4.12], [37, -1, 45, -1]])


        with open(".\\maree.info.52.14j.odt.html") as f:
            contents = f.read()
 
        x = Maree.getPortMareeUTC(52, "20150301", 14, contents, isODT)
        self.assertEqual (len(x), 14)   



    def test_convertTupleForXLS (self) -> None: 
        with open(".\\maree.info.52.odt.html") as f:
            contents = f.read()
 
        x = Maree.getPortMareeUTC(52, "20150301", 5, contents)
        filePath = ".\\maree.csv"
        Maree.convertTupleForXLS(x, filePath)

        with open(".\\maree.odt.csv") as f:
            contentsRef = f.read()

        with open(filePath) as f:
            contents = f.read()
        
        self.assertEqual (contents, contentsRef)

        with open(".\\maree.info.52.14j.odt.html") as f:
            contents = f.read()
        isODT : bool = True
        x = Maree.getPortMareeUTC(52, "20150301", 14, contents, isODT)
        filePath = ".\\maree.14j.csv"
        Maree.convertTupleForXLS(x, filePath)
        with open(filePath) as f:
            contents = f.read()        

        x = Maree.getPortMareeUTC(62, "20240911", 14)


    def test_isDateValide (self) -> None: 
        x = Maree.isDateValide("20150301")
        self.assertTrue (x)

        x = Maree.isDateValide("201503010")
        self.assertFalse (x)

        x = Maree.isDateValide("20150399")
        self.assertFalse (x)

        x = Maree.isDateValide("20152201")
        self.assertFalse (x)

        x = Maree.isDateValide("20152299")
        self.assertFalse (x)

        x = Maree.isDateValide("99991201")
        self.assertTrue (x)

    def test_getPortIdFromName (self) -> None: 
        x = Maree.getPortIdFromName(None)
        self.assertEqual (x, -1)
        x = Maree.getPortIdFromName("Toto")
        self.assertEqual (x, -1)
        x = Maree.getPortIdFromName("Saint-Malo")
        self.assertEqual (x, 52)
        x = Maree.getPortIdFromName("Arcachon (Jetée d'Eyrac)")
        self.assertEqual (x, 136)
        x = Maree.getPortIdFromName("Iles Saint-Marcouf")
        self.assertEqual (x, 30)


if __name__ == '__main__':
    print ("Current dir: {} - prgm dir: {}\n".format(os.getcwd(), os.path.dirname(__file__)))
    print ("change to prgm dir ... \n")
    os.chdir (os.path.dirname(__file__))
    unittest.main()